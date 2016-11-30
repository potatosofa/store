package net.paraoffice.framework;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

public class ServerConfig {
	
	public static final String SERVERPORT = "server.port";
	public static final String MONITORPORT = "monitor.port";
	public static final String REQUESTDATACLASS =  "requestdata.class";
	
	Properties p = null;
	
	public ServerConfig()
	{
		p = new Properties();
	}

	public void load(String path) throws IOException {
		File f = new File(path);
		FileReader fr = new FileReader(f);
		p.load(fr);
		fr.close();
	}
	
	public String getConfigValue(String param){
		return p.getProperty(param);
	}

}
