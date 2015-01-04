package com.accela.MessageService.messages.namingHostToNamingServer;

/**
 * 
 * 如果NamingHost向NamingServer发送NamingHostRegisterMessage
 * 所要求的事务进行完毕后，NamingServer将回复这个消息，来表明 成功与否。
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
