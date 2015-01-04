package com.accela.MessageService.messages.namingHostToNamingServer;

import com.accela.ConnectionCenter.shared.ClientID;

/**
 * 
 * 一个NamingHost启动后，需要告诉NamingServer，其它Host如果要连接到自己，应该
 * 通过什么ClientID。这个消息就干这件事。
 *
 */
public class NamingHostRegisterMessage
{
	private ClientID howToConnectMe;

	private NamingHostRegisterMessage()
	{

	}

	public NamingHostRegisterMessage(ClientID howToConnectMe)
	{
		this();

		if (null == howToConnectMe)
		{
			throw new NullPointerException("howToConnectMe should not be null");
		}

		this.howToConnectMe = howToConnectMe;
	}

	public ClientID getHowToConnectMe()
	{
		assert (howToConnectMe != null);
		return howToConnectMe;
	}

}
