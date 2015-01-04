package com.accela.EventCenter;

/**
 * 
 * 事件类的父类。
 * 如果你需要新建一种事件类型，那么你应该继承这个类，
 * 写出这个事件类型的子类。
 * 
 * 事件类新建完毕后，不会包含对事件发送者的引用
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
