package net.paraoffice.framework;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * This class is used to deal with the connection from the client.
 * It will listens on specified port, until the connection come in.
 * It then start a handler thread to handle this connection.
 * It also provides the information of thread pool.
 * 
 * @author Xiaoming Li (May 23, 2016)
 *
 */
public class RequestDaemon {
	
	public static final int BIND_PORT = 15233;
	public static final RequestDaemon INSTANCE = new RequestDaemon();
	
	private ExecutorService threadPool = null;
	private static long cConn = 0;
	private ServerSocket ss;
	
	/**
	 * Constructor hided 
	 */
	private RequestDaemon(){}

	/**
	 * Default initialization method
	 * @throws IOException 
	 */
	public void init() throws IOException {
		init(BIND_PORT);
	}

	/**
	 * Initialization with the parameter
	 * 
	 * @param port the port to listen on
	 * @throws IOException 
	 */
	public void init(int port) throws IOException{
		//创建一个可根据需要创建新线程的线程池，但是在以前构造的线程可用时将重用它们
		threadPool = Executors.newCachedThreadPool();
		ss = new ServerSocket(port);
	}
	
	/**
	 * start the daemon process.
	 * @throws IOException
	 */
	public void start() {
		while(true){
			try{
				//监听客户端
				Socket s = ss.accept();
				//客户端连接量计数
				cConn ++;				
				//创建RequestHandler对象
				RequestHandler handler = new RequestHandler(s);
				threadPool.execute(handler);
			}catch(IOException e){
				e.printStackTrace();
			}
		}
	};
	
	/**
	 * Get the amount of connections
	 * @return
	 */
	public long getStatConn()
	{
		return cConn;
	}
	
	public int getThreads()
	{
		Map<Thread, StackTraceElement[]> map = Thread.getAllStackTraces();
		return map.size();
	}

}
