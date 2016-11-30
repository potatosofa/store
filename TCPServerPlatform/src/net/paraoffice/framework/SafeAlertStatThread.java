/*
 * 
 */

package net.paraoffice.framework;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Class SafeAlertStatThread is used to send hear-beat signal to monitor program.
 * Here is the working principle:
 * (1) This thread will listen designated port
 * (2) If a connection come in, send an Alive Message and statistic message.
 * (3) Then close the connection. Go back to (1).
 * 
 * @author XiaomingLi  May 22, 2016
 */
public class SafeAlertStatThread extends Thread {

	public static final SafeAlertStatThread INSTANCE = new SafeAlertStatThread();
	public static final int DEFAULTPORT = 2222;
	public static final String ALIVEMSG = "Hello!";

	private int port;
	private ServerSocket ss;
	private Logger logger = LogManager.getLogger();

	private SafeAlertStatThread() {
	}

	@Override
	public void run() {
		process();
	}

	public void init() throws IOException {
		this.port = DEFAULTPORT;
		ss = new ServerSocket(port);
	}

	public void init(int port) throws IOException {
		this.port = port;
		ss = new ServerSocket(port);
	}

	private void process() {
		while (true) {
			try {
				Socket s = ss.accept();
				logger.debug("Socket created.");
				PrintWriter pw = new PrintWriter(s.getOutputStream());
				signalAlive(pw);
				sendStat(pw);
				pw.flush();
				logger.debug("Send stat string.");
				pw.close();
				s.close();

			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}

	private void signalAlive(PrintWriter pw) {
		pw.println(ALIVEMSG);
	}

	private void sendStat(PrintWriter pw) {
		long c = Server.INSTANCE.getRequestDaemon().getStatConn();
		pw.println("Total Connetions: " + Long.toString(c));
	}
}
