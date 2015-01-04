package com.accela.EventCenter;

import java.util.*;

/**
 * EventCenter模块的入口
 * 
 * 事件处理中心（支持多线程）。
 * 时间处理中心中存储有多个监听器，当你将一个事件
 * 发送到事件处理中心的时候，事件处理中心会寻找匹配
 * 的监听器来处理它。
 * 
 * //根据Sun JDK BUG ID=6321873， 事件中心在处理大量事件的时候，
 * 其使用的ID机制可能出错，导致事件错发。
 *
 */
public class EventCenter implements IEventCenterConfigurer, IEventPoster
{
	/**
	 * 存储监听器的容器
	 */
	private HashMap<ListenerKey, List<Listener>> listenerHolder;
	
	public EventCenter()
	{
		listenerHolder=new HashMap<ListenerKey, List<Listener>>();
	}
	
	/**
	 * 向事件处理中心中加入GeneralListener。
	 * 你可以重复加入监听同一目标的监听器，它们都是有效的。
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
	 * 向事件处理中心中加入ClasslListener。
	 * 你可以重复加入监听同一目标的监听器，它们都是有效的。
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
	 * 向事件处理中心中加入InstanceListener。
	 * 你可以重复加入监听同一目标的监听器，它们都是有效的。 
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
	 * 删除监听指定的事件aimEventClass的所有GeneralListener。 
	 * @param aimEventClass 指定的事件类
	 * @return 所有被删除的GeneralListener
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
				assert(false);    //此异常绝不应该发生
				return null;
			}
		}
	}
	
	/**
	 * 删除监听指定的事件aimEventClass和指定的类aimClass的所有ClassListener。
	 * @param aimEventClass 指定的事件类
	 * @param aimClass 指定的类
	 * @return 所有被删除的ClassListener
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
				assert(false);    //此异常绝不应该发生
				return null;
			}
		}
	}
	
	/**
	 * 删除监听指定的事件aimEventClass和指定的实例aimInstance的所有ClassListener。
	 * @param aimEventClass 指定的事件类
	 * @param aimInstance 指定的实例
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
				assert(false);    //此异常绝不应该发生
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
	 * 向事件处理中心发送一个事件。事件处理中心会依次找出所有和这个事件相匹配的
	 * GeneralListener，ClassListener和InstanceListener，来处理这个事件。
	 * 
	 * @throws EventProcessingException 当某个监听器处理时发生异常的时候，事件处理中心不会
	 * 中断处理，而是继续完成其他监听器的处理，到最后才会抛出这个异常。处理过程中可能发生多个
	 * 异常，它们都会被存储在这个异常对象中。
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
