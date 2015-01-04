package com.accela.EventCenter;

/**
 * 
 * �¼���ĸ��ࡣ
 * �������Ҫ�½�һ���¼����ͣ���ô��Ӧ�ü̳�����࣬
 * д������¼����͵����ࡣ
 * 
 * �¼����½���Ϻ󣬲���������¼������ߵ�����
 *
 */
public abstract class Event
{
	private Object sender;
	
	public Event(Object sender)
	{
		if(null==sender)
		{
			throw new NullPointerException("sender should not be null");
		}
		
		this.sender=sender;
	}

	public Object getSender()
	{
		return sender;
	}
	

}
