package net.paraoffice.framework;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestDataFactory {
	
	public static RequestData createRequestData(BufferedReader br, String cn) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException
	{
		//返回与带有给定字符串名的类或接口相关联的 Class 对象，并创建该对象的实例，强制转换为RequestData类型
		RequestData instance = (RequestData) Class.forName(cn).newInstance();
		//
		instance.load(br);
		return instance;
	}
}
