package com.accela.EventCenter;

import java.util.*;

/**
 * EventCenterģ������
 * 
 * �¼��������ģ�֧�ֶ��̣߳���
 * ʱ�䴦�������д洢�ж�������������㽫һ���¼�
 * ���͵��¼��������ĵ�ʱ���¼��������Ļ�Ѱ��ƥ��
 * �ļ���������������
 * 
 * //����Sun JDK BUG ID=6321873�� �¼������ڴ�������¼���ʱ��
 * ��ʹ�õ�ID���ƿ��ܳ��������¼�����
 *
 */
public class EventCenter implements IEventCenterConfigurer, IEventPoster
{
	/**
	 * �洢������������
	 */
	private HashMap<ListenerKey, List<Listener>> listenerHolder;
	
	public EventCenter()
	{
		listenerHolder=new HashMap<ListenerKey, List<Listener>>();
	}
	
	/**
	 * ���¼����������м���GeneralListener��
	 * ������ظ��������ͬһĿ��ļ����������Ƕ�����Ч�ġ�
	 */
	public synchronized void addGeneralListener(GeneralListener listener)
	{
		if(null==listener)
		{
			throw new NullPointerException("listener should not be null");
		}
		
		addListener(listener);
	}
	
	/**
	 * ���¼����������м���ClasslListener��
	 * ������ظ��������ͬһĿ��ļ����������Ƕ�����Ч�ġ�
	 */
	public synchronized void addClassListener(ClassListener listener)
	{
		if(null==listener)
		{
			throw new NullPointerException("listener should not be null");
		}
		
		addListener(listener);
	}
	
	/**
	 * ���¼����������м���InstanceListener��
	 * ������ظ��������ͬһĿ��ļ����������Ƕ�����Ч�ġ� 
	 */
	public synchronized void addInstanceListener(InstanceListener listener)
	{
		if(null==listener)
		{
			throw new NullPointerException("listener should not be null");
		}
		
		addListener(listener);
	}
	
	private synchronized void addListener(Listener listener)
	{
		if(null==listener)
		{
			throw new NullPointerException("listener should not be null");
		}
		
		ListenerKey key=ListenerKey.createListenerKey(listener);
		List<Listener> list=listenerHolder.get(key);
		
		if(null==list)
		{
			list=new LinkedList<Listener>();
			list.add(listener);
			listenerHolder.put(key, list);
		}
		else
		{
			list.add(listener);
		}
	}
	
	/**
	 * ɾ������ָ�����¼�aimEventClass������GeneralListener�� 
	 * @param aimEventClass ָ�����¼���
	 * @return ���б�ɾ����GeneralListener
	 */
	public synchronized GeneralListener[] removeGeneralListener(Class<? extends Event> aimEventClass)
	{
		if(null==aimEventClass)
		{
			throw new NullPointerException("aimEventClass should not be null");
		}
		
		List<Listener> list=removeListener(
				ListenerKey.createListenerKeyForGeneralListener(aimEventClass));
		
		if(null==list)
		{
			return new GeneralListener[0];
		}
		else
		{
			try
			{
				return list.toArray(new GeneralListener[0]);
				
			}catch(ClassCastException ex)
			{
				ex.printStackTrace();
				assert(false);    //���쳣����Ӧ�÷���
				return null;
			}
		}
	}
	
	/**
	 * ɾ������ָ�����¼�aimEventClass��ָ������aimClass������ClassListener��
	 * @param aimEventClass ָ�����¼���
	 * @param aimClass ָ������
	 * @return ���б�ɾ����ClassListener
	 */
	public synchronized ClassListener[] removeClassListener(Class<? extends Event> aimEventClass,
			Class<? extends Object> aimClass)
	{
		if(null==aimEventClass)
		{
			throw new NullPointerException("aimEventClass should not be null");
		}
		if(null==aimClass)
		{
			throw new NullPointerException("aimClass should not be null");
		}
		
		List<Listener> list=removeListener(
				ListenerKey.createListenerKeyForClassListener(aimEventClass, aimClass));
		
		if(null==list)
		{
			return new ClassListener[0];
		}
		else
		{
			try
			{
				return list.toArray(new ClassListener[0]);
			}catch(ClassCastException ex)
			{
				ex.printStackTrace();
				assert(false);    //���쳣����Ӧ�÷���
				return null;
			}
		}
	}
	
	/**
	 * ɾ������ָ�����¼�aimEventClass��ָ����ʵ��aimInstance������ClassListener��
	 * @param aimEventClass ָ�����¼���
	 * @param aimInstance ָ����ʵ��
	 * @return
	 */
	public synchronized InstanceListener[] removeInstanceListener(Class<? extends Event> aimEventClass,
			Object aimInstance)
	{
		if(null==aimEventClass)
		{
			throw new NullPointerException("aimEventClass should not be null");
		}
		if(null==aimInstance)
		{
			throw new NullPointerException("aimInstance should not be null");
		}
		
		List<Listener> list=removeListener(
				ListenerKey.createListenerKeyForInstanceListener(aimEventClass, aimInstance));
		
		if(null==list)
		{
			return new InstanceListener[0];
		}
		else
		{
			try
			{
				return list.toArray(new InstanceListener[0]);
			}catch(ClassCastException ex)
			{
				ex.printStackTrace();
				assert(false);    //���쳣����Ӧ�÷���
				return null;
			}
		}
	}
	
	private synchronized List<Listener> removeListener(ListenerKey key)
	{
		if(null==key)
		{
			throw new NullPointerException("key should not be null");
		}
		
		return listenerHolder.remove(key);
	}
	
	/**
	 * ���¼��������ķ���һ���¼����¼��������Ļ������ҳ����к�����¼���ƥ���
	 * GeneralListener��ClassListener��InstanceListener������������¼���
	 * 
	 * @throws EventProcessingException ��ĳ������������ʱ�����쳣��ʱ���¼��������Ĳ���
	 * �жϴ������Ǽ�����������������Ĵ��������Ż��׳�����쳣����������п��ܷ������
	 * �쳣�����Ƕ��ᱻ�洢������쳣�����С�
	 * 
	 */
	public synchronized void postEvent(final Event event) throws EventProcessingException
	{
		if(null==event)
		{
			throw new NullPointerException("event should not be null");
		}
		
		boolean hasException=false;
		List<Throwable> exceptionList=new LinkedList<Throwable>();
		
		//invoke general listeners
		ListenerKey key=ListenerKey.
		createListenerKeyForGeneralListener(event.getClass());
		try
		{
			invokeEventHandlers(key, event);
		} catch (EventProcessingException ex)
		{
			hasException=true;
			exceptionList.addAll(ex.getCauseList());
		}
		
		//invoke class listeners
		key=ListenerKey.
		createListenerKeyForClassListener(event.getClass(), 
				event.getSender().getClass());
		try
		{
			invokeEventHandlers(key, event);
		} catch (EventProcessingException ex)
		{
			hasException=true;
			exceptionList.addAll(ex.getCauseList());
		}
		
		//invoke instance listeners
		key=ListenerKey.
		createListenerKeyForInstanceListener(
				event.getClass(), event.getSender());
		try
		{
			invokeEventHandlers(key, event);
		} catch (EventProcessingException ex)
		{
			hasException=true;
			exceptionList.addAll(ex.getCauseList());
		}
		
		if(hasException)
		{
			throw new EventProcessingException(exceptionList);
		}
		
	}
	
	private synchronized void invokeEventHandlers(
			ListenerKey key, Event event) 
	throws EventProcessingException
	{
		if(null==key)
		{
			throw new NullPointerException("key should not be null");
		}
		if(null==event)
		{
			throw new NullPointerException("event should not be null");
		}
		
		List<Listener> list=listenerHolder.get(key);
		if(null==list)
		{
			return;
		}
		
		boolean hasException=false;
		List<Throwable> exceptionList=new LinkedList<Throwable>();
		
		for(Listener listener : list)
		{
			assert(listener!=null);
			try
			{
				listener.handleEvent(event);
			} catch (Exception ex)
			{
				hasException=true;
				exceptionList.add(ex);
			}
		}
		
		if(hasException)
		{
			throw new EventProcessingException(exceptionList);
		}
	}

}
