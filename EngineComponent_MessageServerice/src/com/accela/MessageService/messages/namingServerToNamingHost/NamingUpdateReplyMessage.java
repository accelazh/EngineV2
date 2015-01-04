package com.accela.MessageService.messages.namingServerToNamingHost;

/**
 * 
 * NamingServer收到一个NamingHost发出的NamingUpdateMessage后，更新
 * Name-Client对应表，将更新操作的结果放在这个消息中返回给那个
 * NamingHost
 *
 */
public class NamingUpdateReplyMessage
{
	/**
	 * 更新是否成功
	 */
	private boolean succ;
	/**
	 * 如果更新没有成功，则是对应的NamingUpdateMessage中的下标为几
	 * 的操作引起了失败
	 */
	private int failedOerationIndex=0;
	
	public NamingUpdateReplyMessage()
	{
		
	}
	
	public NamingUpdateReplyMessage(boolean succ, int failedOerationIndex)
	{
		this();
		
		if(failedOerationIndex<0)
		{
			throw new IllegalArgumentException("failedOerationIndex should be non netgive");
		}
		
		this.succ=succ;
	}

	public boolean isSucc()
	{
		return succ;
	}

	public int getFailedOerationIndex()
	{
		return failedOerationIndex;
	}

	public void setSucc(boolean succ)
	{
		this.succ = succ;
	}

	public void setFailedOerationIndex(int failedOerationIndex)
	{
		this.failedOerationIndex = failedOerationIndex;
	}

}
