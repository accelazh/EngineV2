package com.accela.MessageService.messages.namingServerToNamingHost;

import com.accela.ConnectionCenter.shared.ClientID;


/**
 * 
 * ���Message���������㲥NamingServer�Ĵ��ڣ�
 * �Լ����ӵ�NamingServer�ķ���������NamingServer
 * �㲥����
 *
 */
public class NamingExistanceMessage
{
	private ClientID howToConnectMe;
	
	private NamingExistanceMessage()
	{
		
	}
	
	public NamingExistanceMessage(ClientID howToConnectMe)
	{
		this();
		
		if(null==howToConnectMe)
		{
			throw new NullPointerException("howToConnectMe should not be null");
		}
		
		this.howToConnectMe=howToConnectMe;
	}

	public ClientID getHowToConnectMe()
	{
		assert(howToConnectMe!=null);
		return howToConnectMe;
	}
	
	
}
