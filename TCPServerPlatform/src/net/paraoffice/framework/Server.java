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
		//显示最大数为MAX_VALUE
		logger.debug("The biggest number is {}", Long.MAX_VALUE);
		try {
			//初始化服务器
			INSTANCE.init();
		} catch (IOException e1) {
			//抛出异常时显示错误
			logger.error("Cannot initialize the server, please check the configuration and re-start the program.");
			e1.printStackTrace();
			return;
		}
		try {
			//启动服务器和监听线程
			INSTANCE.start();
			//显示服务器退出
			logger.info("Exitting...");
		} catch (IOException e) {
			//抛出异常时显示错误
			logger.error("Cannot start Server, please check the configuration and re-start the program.");
			e.printStackTrace();
		}
	}

	private void init() throws IOException {
		//创建一个空属性列表对象
		sc = new ServerConfig();
		//把服务器配置下载到属性列表对象中
		sc.load(DEFALT_CONFIG_FILEPATH);
		//创建发送数据
		//调用静态变量，创建SafeAlertStatThread对象  
		//该对象直接翻译：安全警觉信号线程
		sast = SafeAlertStatThread.INSTANCE;
		//在sc对象中，查找监控端口的值，再解析为整型变量，设定监控端口，端口为2222
		sast.init(Integer.parseInt(sc.getConfigValue(ServerConfig.MONITORPORT)));
		//调用静态变量，创建RequestDaemon对象
		rd = RequestDaemon.INSTANCE;
		//在sc对象中，查找服务器端口的值，在解析为整型变量，设置服务器端口，端口为15233
		rd.init(Integer.parseInt(sc.getConfigValue(ServerConfig.SERVERPORT)));
		//显示服务器初始化完毕
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
		//开始监控端口线程
		sast.start();
		//开始服务器线程
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
