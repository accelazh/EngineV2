package com.accela.MessageService.messages.namingHostToNamingServer;

/**
 * 
 * ���NamingHost��NamingServer����NamingHostRegisterMessage
 * ��Ҫ������������Ϻ�NamingServer���ظ������Ϣ�������� �ɹ����
 * 
 */
public class NamingHostRegisterReplyMessage
{
	private boolean succ;

	private NamingHostRegisterReplyMessage()
	{

	}

	public NamingHostRegisterReplyMessage(boolean succ)
	{
		this();
		this.succ = succ;
	}

	public boolean isSucc()
	{
		return succ;
	}

	public void setSucc(boolean succ)
	{
		this.succ = succ;
	}

}
