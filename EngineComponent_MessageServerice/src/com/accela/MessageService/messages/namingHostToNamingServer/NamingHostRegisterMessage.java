package com.accela.MessageService.messages.namingHostToNamingServer;

import com.accela.ConnectionCenter.shared.ClientID;

/**
 * 
 * һ��NamingHost��������Ҫ����NamingServer������Host���Ҫ���ӵ��Լ���Ӧ��
 * ͨ��ʲôClientID�������Ϣ�͸�����¡�
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
