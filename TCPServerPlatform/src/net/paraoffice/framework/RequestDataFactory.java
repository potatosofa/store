package net.paraoffice.framework;

import java.io.BufferedReader;
import java.io.IOException;

public class RequestDataFactory {
	
	public static RequestData createRequestData(BufferedReader br, String cn) throws InstantiationException, IllegalAccessException, ClassNotFoundException, IOException
	{
		//��������и����ַ����������ӿ�������� Class ���󣬲������ö����ʵ����ǿ��ת��ΪRequestData����
		RequestData instance = (RequestData) Class.forName(cn).newInstance();
		//
		instance.load(br);
		return instance;
	}
}
