package net.paraoffice.framework.test;

import java.io.BufferedReader;
import java.io.IOException;

import net.paraoffice.framework.RequestData;

/**
 * This class is a simple implementation of interface `RequestData`.
 * It can be configured in the server.conf.
 * This follows a non-interactive protocol, just like HTTP.
 * It echos back what you send through the socket.
 * 
 * @author Xiaoming Li (May 22, 2016)
 *
 */
public class ExampleRequestData implements RequestData {
	private String msg;
	private int id;
	
	public ExampleRequestData(){
		id = 0;
		msg = "";
	}

	@Override
	public void load(BufferedReader br) throws IOException {
		//In this example, the first line of the input from the socket
		//will be saved as the data
		String line;
//		while((line = br.readLine()) != null){
//			if(line.trim().isEmpty()){
//				break;
//			}
//			id ++;
//			msg = msg + line + "\n";		
//		}
		
		if((line = br.readLine()) != null) {
			id ++;
			msg = msg + line + "\n";				
		}
	}

	public String toString()
	{
		return msg;
	}

	@Override
	public int getID() {
		return id;
	}
}
