package net.paraoffice.framework;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.BlockingQueue;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * To process the request using this class.
 * 
 * @author Xiaoming Li (May 22, 2016)
 *
 */
public class RequestHandler implements Runnable {
	public static final int CODE_OK = 0;
	public static final int CODE_ERR_PROTOCOL = 1;
	public static final int CODE_ERR_DATA = 2;
	public static final int CODE_ERR_OTHER = 1;

	private Logger log = LogManager.getLogger();
	
	private Socket s = null;
	private BufferedReader br = null;
	private BufferedWriter bw = null;
	private RequestData data;
	//用于存放物理地址
	private String macAddress = "";
	//判断是不是设备连接后收到的第一条消息
	private boolean isFirstData = true;
	
	RequestHandler rh = null;
	
	public static ArrayList<RequestHandler> connectedSocket = new ArrayList<RequestHandler>(50);
	
	
	public RequestHandler(Socket s) throws IOException{
		//Socket对象
		this.s = s;
		//包装输入流
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		//包装输出流
		bw = new BufferedWriter(new OutputStreamWriter(s.getOutputStream()));
	}

	@Override
	public void run() {
		while(true) {
			try {
				//把数据读进来
				data = RequestDataFactory.createRequestData(br, Server.INSTANCE.getServerConfig().getConfigValue(ServerConfig.REQUESTDATACLASS));
				
				if(data == null){
//					//如果没有读进来任何字符，则提示未从客户端读取到数据
//					log.warn("Cannot correctly receive the data from client.");
//					//回复2，表示程序错误
//					response(CODE_ERR_DATA);				
					return;
				}	
				
				if(isFirstData) {
					//如果是连接后第一次发的数据，是物理地址
					macAddress = data.toString();
					isFirstData = false;
					connectedSocket.add(this);
				}
				else {
					for(int i = 0;i < connectedSocket.size();i++) {
						rh = connectedSocket.get(i);
						if(rh.getMacAddress().equals(macAddress) && rh.getS() != s) {
							BufferedWriter bw1 = rh.getBw();	
							try{
								bw1.write(data.toString());
								bw1.flush();
							}catch (IOException e){
								connectedSocket.remove(rh);
								i--;
							}
						}
					}
				}
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {			
				e.printStackTrace();
			} catch (IOException e1) {
				e1.printStackTrace();
			}finally{
			}
		}

		
	}
//	private void response(int code) throws IOException
//	{
//		log.debug(code);
//		bw.write(data.toString());
//		bw.flush();
//		br.close();
//		bw.close();
//		s.close();
//	}

	public Socket getS() {
		return s;
	}

	public void setS(Socket s) {
		this.s = s;
	}

	public BufferedWriter getBw() {
		return bw;
	}

	public void setBw(BufferedWriter bw) {
		this.bw = bw;
	}
	

	public BufferedReader getBr() {
		return br;
	}

	public void setBr(BufferedReader br) {
		this.br = br;
	}

	public String getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(String macAddress) {
		this.macAddress = macAddress;
	}
	

}
