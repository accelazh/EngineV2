package com.accela.EventCenter;

import com.accela.ClassIDAndInstanceID.*;

/**
 * 
 * ���м������ĸ��ࡣ
 * ������������GeneralListener��ClassListener��InstanceListener��
 * �������Ҫ����һ���µļ���������ô��Ӧ�ü̳���������������֮һ����
 * ��������ࡣ
 *
 */
abstract class Listener
{
	/**
	 * ���������¼�����ID
	 */
	private ClassID aimEventClassID;
	/**
	 * �������������ID�����Ϊnull�����ʾ����������
	 */
	private ClassID aimClassID;
	/**
	 * ��������ʵ����ʵ��ID�����Ϊnull�����ʾ����aimClassID��Ӧ������ʵ����
	 * ����aimInstanceID��Ϊnull��ʱ��aimClassIDӦ�õ���aimInstanceID��Ӧ
	 * ����������ID
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
	 * �¼�������
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
	 * �¼���������ʵ�� 
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
