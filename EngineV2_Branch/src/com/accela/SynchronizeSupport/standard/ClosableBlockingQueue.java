package com.accela.SynchronizeSupport.standard;

import java.util.concurrent.LinkedBlockingQueue;

/**
 * 
 * ClosableBlockingQueue��BlockingQueue�Ļ����Ϸ�չ������
 * ������Ƴɿɿ��������ӵ�д򿪺͹رշ�����
 * 
 * ClosableBlockingQueue��BlockingQueue�Ĳ�֮ͬ�����ڣ�ClosableBlockingQueue
 * ���д򿪺͹رյķ��������ǿɿ���������μ���OpenCloseSupport�еĶ��塣����Ҫ�ȴ�ClosableBlockingQueue
 * �����ʹ�á��رպ������������̶߳��ᱻ�ϳ������׳�AlreadyClosedException�����Ҷ������������ܹ���ʹ�ã�
 * ���ܹ������̵߳ķ���һ�����ܹ���ʹ�á�
 *
 */
public class ClosableBlockingQueue<T> implements IOpenClosable
{
	private LinkedBlockingQueue<T> queue;	//ע�⣬���еĴ�С���������޴�ģ����ܷ������������

	private OpenCloseSupport openCloseSupport;

	public ClosableBlockingQueue()
	{
		queue = new LinkedBlockingQueue<T>();

		openCloseSupport = new OpenCloseSupport(new OpenMethod(), new CloseMethod());
	}
	
	@Override
	public boolean isOpen()
	{
		return openCloseSupport.isOpen();
	}

	/**
	 * Inserts the specified element at the tail of this queue.
	 * 
	 * @param value
	 *            the inserted element
	 * @throws InterruptedException
	 * 
	 * @throws InterruptedException
	 * @throws NullPointerException
	 *             - if the specified element is null
	 */
	public void enqueue(T value)
	{
		if (null == value)
		{
			throw new NullPointerException("value == null");
		}

		openCloseSupport.lockMethod();

		try
		{
			openCloseSupport.ensureOpen();

			queue.put(value);
		} catch (InterruptedException ex)
		{
			// ��Ϊ���д�С���ޣ������ܷ����ȴ���������������쳣�����ܷ���
			assert (false);
			ex.printStackTrace();
		} finally
		{
			openCloseSupport.unlockMethod();
		}
	}

	/**
	 * Retrieves and removes the head of this queue, waiting if necessary until
	 * an element becomes available.
	 * 
	 * @return the head of this queue
	 * 
	 * @throws InterruptedException
	 */
	public T dequeue()
	{
		openCloseSupport.lockMethod();

		try
		{
			if(!openCloseSupport.isOpen()&&isEmpty())
			{
				openCloseSupport.ensureOpen();
			}

			return queue.take();
		} catch (InterruptedException ex)
		{
			// һ����˵������쳣Ӧ�����������Ĺر������ġ�
			// ��������������жϽ�������������̣߳������
			// �쳣�Ϳ����ڲ��������رյ�ʱ������
			throw new AlreadyClosedException(
					"there should be somebody who has closed "
							+ "ClosableBlockingQueue, or someone has interrupted "
							+ "the thread intentively");
		} finally
		{
			openCloseSupport.unlockMethod();
		}

	}

	/**
	 * Returns <tt>true</tt> if this queue contains no elements.
	 * 
	 * @return <tt>true</tt> if this queue contains no elements
	 */
	public boolean isEmpty()
	{
		return queue.isEmpty();
	}

	/**
	 * clear all elements contained in the blocking queue
	 */
	public void clear()
	{
		openCloseSupport.lockMethod();

		try
		{
			openCloseSupport.ensureOpen();
			
			queue.clear();
		} finally
		{
			openCloseSupport.unlockMethod();
		}
	}

	/**
	 * return the number of the elements contained in the blocking queue
	 */
	public int size()
	{
		return queue.size();
	}

	/**
	 * �ر������ע��رպ��������д洢�Ķ���
	 */
	@Override
	public void close()
	{
		try
		{
			openCloseSupport.close();
		} catch (FailedToCloseException ex)
		{
			assert(false);
			ex.printStackTrace();
		}

	}

	@Override
	public void open()
	{
		try
		{
			openCloseSupport.open();
		} catch (FailedToOpenException ex)
		{
			assert(false);
			ex.printStackTrace();
		}
	}
	
	private class OpenMethod implements TargetMethod
	{

		@Override
		public void run() throws Exception
		{
			queue.clear();
		}

	}

	private class CloseMethod implements TargetMethod
	{

		@Override
		public void run() throws Exception
		{

		}

	}

}
