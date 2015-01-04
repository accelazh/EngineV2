package com.accela.EventCenter;

import com.accela.ClassIDAndInstanceID.*;

/**
 * 
 * 所有监听器的父类。
 * 它有三个子类GeneralListener、ClassListener和InstanceListener。
 * 如果你需要创建一种新的监听器，那么你应该继承上述三个监听器之一，而
 * 不是这个类。
 *
 */
abstract class Listener
{
	/**
	 * 所监听的事件的类ID
	 */
	private ClassID aimEventClassID;
	/**
	 * 所监听的类的类ID，如果为null，则表示监听所有类
	 */
	private ClassID aimClassID;
	/**
	 * 所监听的实例的实例ID，如果为null，则表示监听aimClassID对应的所有实例。
	 * 但是aimInstanceID不为null的时候，aimClassID应该等于aimInstanceID对应
	 * 对象的类的类ID
	 */
	private InstanceID aimInstanceID;
	
	public Listener(Class<? extends Event> aimEventClass,
			Class<? extends Object> aimClass,
			Object aimInstance)
	{
		if(null==aimEventClass)
		{
			throw new NullPointerException("aimEventClass should not be null");
		}
		if(null==aimClass)
		{
			if(aimInstance!=null)
			{
				throw new IllegalArgumentException("invalid aimInstance");
			}
		}
		else
		{
			if(aimInstance!=null
					&&aimClass!=aimInstance.getClass())
			{
				throw new IllegalArgumentException("invalid aimInstance"); 
			}
		}
		
		this.aimEventClassID=IDDispatcher.createClassID(aimEventClass);
		if(aimClass!=null)
		{
			this.aimClassID=IDDispatcher.createClassID(aimClass);
		}
		else
		{
			this.aimClassID=null;
		}
		if(aimInstance!=null)
		{
			this.aimInstanceID=IDDispatcher.createInstanceID(aimInstance);
		}
		else
		{
			this.aimInstanceID=null;
		}
	}
	
	/**
	 * 事件处理方法
	 */
	public synchronized void handleEvent(Event event) throws Exception
	{
		if(null==event)
		{
			throw new NullPointerException("event should not be null");
		}
		if(!isListeningToEvent(event.getClass()))
		{
			throw new IllegalArgumentException("event can not be handled by this Listener");
		}
		if(!isListeningToInstance(event.getSender()))
	    {
			throw new IllegalArgumentException("event can not be handled by this Listener");
	    }

		handleEventImpl(event);
	}
	
	/**
	 * 事件处理方法的实现 
	 */
	protected abstract void handleEventImpl(Event event) throws Exception;
	
	public boolean isListeningToEvent(Class<? extends Event> eventClass)
	{
		if(null==eventClass)
		{
			throw new NullPointerException("eventClass should not be null");
		}
		
		return aimEventClassID.isIDOf(eventClass);
	}
	
	public boolean isListeningToInstance(Object instance)
	{
		if(null==instance)
		{
			throw new NullPointerException("instance should not be null");
		}
		
		if(null==aimClassID)
		{
			if(null==aimInstanceID)
			{
				return true;
			}
			else
			{
				assert(false);
				throw new IllegalStateException(
						"illegal state, aimClassID==null but aimInstanceID!=null");
			}
		}
		else
		{
			if(null==aimInstanceID)
			{
				return aimClassID.isIDOf(instance.getClass());
			}
			else
			{
				if(!aimClassID.isOfSameClass(aimInstanceID))
				{
					assert(false);
					throw new IllegalStateException(
							"illegal state, aimClassID.isOfSameClass(aimInstanceID)==false");
				}
				return aimInstanceID.isIDOf(instance);
			}
		}
		
	}

	protected ClassID getAimEventClassID()
	{
		return aimEventClassID;
	}

	protected ClassID getAimClassID()
	{
		return aimClassID;
	}

	protected InstanceID getAimInstanceID()
	{
		return aimInstanceID;
	}
	
}
