package net.paraoffice.framework;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used as the main class for JVM to load at start-up.
 * It will load some objects, initialize and start the thread of serverscoket.
 * 
 * @author Xiaoming
 */
public class Server {
	public static final String DEFALT_CONFIG_FILEPATH = "server.conf";
	
	/** Instance of the server, globally available. */
	public static Server INSTANCE = new Server();
	private static final Logger logger = LogManager.getLogger();
	private ServerConfig sc = null;
	private SafeAlertStatThread sast;
	private RequestDaemon rd;
	
	
	/**
	 * Make the constructor private to prevent create this object outside.
	 */
	private Server(){}
	
	public static void main(String[] args) {
		//��ʾ�����ΪMAX_VALUE
		logger.debug("The biggest number is {}", Long.MAX_VALUE);
		try {
			//��ʼ��������
			INSTANCE.init();
		} catch (IOException e1) {
			//�׳��쳣ʱ��ʾ����
			logger.error("Cannot initialize the server, please check the configuration and re-start the program.");
			e1.printStackTrace();
			return;
		}
		try {
			//�����������ͼ����߳�
			INSTANCE.start();
			//��ʾ�������˳�
			logger.info("Exitting...");
		} catch (IOException e) {
			//�׳��쳣ʱ��ʾ����
			logger.error("Cannot start Server, please check the configuration and re-start the program.");
			e.printStackTrace();
		}
	}

	private void init() throws IOException {
		//����һ���������б����
		sc = new ServerConfig();
		//�ѷ������������ص������б������
		sc.load(DEFALT_CONFIG_FILEPATH);
		//������������
		//���þ�̬����������SafeAlertStatThread����  
		//�ö���ֱ�ӷ��룺��ȫ�����ź��߳�
		sast = SafeAlertStatThread.INSTANCE;
		//��sc�����У����Ҽ�ض˿ڵ�ֵ���ٽ���Ϊ���ͱ������趨��ض˿ڣ��˿�Ϊ2222
		sast.init(Integer.parseInt(sc.getConfigValue(ServerConfig.MONITORPORT)));
		//���þ�̬����������RequestDaemon����
		rd = RequestDaemon.INSTANCE;
		//��sc�����У����ҷ������˿ڵ�ֵ���ڽ���Ϊ���ͱ��������÷������˿ڣ��˿�Ϊ15233
		rd.init(Integer.parseInt(sc.getConfigValue(ServerConfig.SERVERPORT)));
		//��ʾ��������ʼ�����
		logger.debug("Server has been initialized.");
	}

	/**
	 * The server start to work.
	 * It never returns until there's anything wrong, or finish his job.
	 * Is it possible to implement a server that can be shutdown/reboot?
	 * Maybe in the future.
	 * @throws IOException 
	 */
	private void start() throws IOException {
		//��ʼ��ض˿��߳�
		sast.start();
		//��ʼ�������߳�
		rd.start();
		
	}
	
	
	public ServerConfig getServerConfig()
	{
		return sc;
	}

	public RequestDaemon getRequestDaemon() {
		return rd;
	}

}
