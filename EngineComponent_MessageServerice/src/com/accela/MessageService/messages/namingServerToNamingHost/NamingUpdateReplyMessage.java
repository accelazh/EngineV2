package com.accela.MessageService.messages.namingServerToNamingHost;

/**
 * 
 * NamingServer�յ�һ��NamingHost������NamingUpdateMessage�󣬸���
 * Name-Client��Ӧ�������²����Ľ�����������Ϣ�з��ظ��Ǹ�
 * NamingHost
 *
 */
public class NamingUpdateReplyMessage
{
	/**
	 * �����Ƿ�ɹ�
	 */
	private boolean succ;
	/**
	 * �������û�гɹ������Ƕ�Ӧ��NamingUpdateMessage�е��±�Ϊ��
	 * �Ĳ���������ʧ��
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
