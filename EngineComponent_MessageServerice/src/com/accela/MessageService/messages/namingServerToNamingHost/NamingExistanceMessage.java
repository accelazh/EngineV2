package com.accela.MessageService.messages.namingServerToNamingHost;

import com.accela.ConnectionCenter.shared.ClientID;


/**
 * 
 * 这个Message用来向外界广播NamingServer的存在，
 * 以及连接到NamingServer的方法。它由NamingServer
 * 广播出来
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
