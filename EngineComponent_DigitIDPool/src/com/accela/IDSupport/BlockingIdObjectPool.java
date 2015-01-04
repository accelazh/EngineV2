package com.accela.IDSupport;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * BlockingIdObjectPool��IdObjectPool�Ļ����ϸĽ��������������ӵ��id�Ķ���
 * ��BlockingIdObjectPool�У����ݶ����id����Ŷ�������Ը��ݶ����id����ȡ����
 * 
 * BlockingIdObjectPool��IdObjectPool�Ĳ�֮ͬ�����ڣ�BlockingIdObjectPool
 * ��retrieve(int)��������ȡ����ָ���Ķ����ʱ�򣬵ȴ�ֱ��ָ���Ķ��󱻷��롣
 * 
 */
public class BlockingIdObjectPool
{
	private IdObjectPool pool = new IdObjectPool();

	private ReentrantLock lock = new ReentrantLock();

	private Condition waitCondition = lock.newCondition();

	/**
	 * ����һ��ӵ��id�Ķ��������������ӵ����ͬid�Ķ��� ����׳�IllegalArgumentException�쳣
	 * 
	 * @param object
	 *            ������Ķ���
	 */
	public void put(IIdObject object)
	{
		if (null == object)
		{
			throw new NullPointerException("object should not be null");
		}
		if (object.getId() < 0)
		{
			throw new IllegalArgumentException(
					"object.getId() returns an invalid id");
		}

		lock.lock();
		try
		{
			if (pool.contains(object.getId()))
			{
				throw new IllegalArgumentException(
						"object.getId() returns an invalid id");
			}

			Object old = pool.put(object);
			assert (null == old);

			waitCondition.signalAll();
		} finally
		{
			lock.unlock();
		}
	}

	/**
	 * ȡ��ָ��id����Ӧ�Ķ�������ö�����BlockingIdObjectPool�У�
	 * ����߳�����ֱ����Ӧ�Ķ��󱻷���BlockingIdObjectPool��
	 * @param id Ҫȥȡ���Ķ����id
	 * @return ָ��Ҫ��ȡ���Ķ�������ö���û�б����룬�������ȴ���
	 * @throws InterruptedException ����ȴ������б��жϡ�
	 */
	public IIdObject retrieve(int id) throws InterruptedException
	{
		if (id < 0)
		{
			throw new IllegalArgumentException("id should be non negtive");
		}

		lock.lock();
		try
		{
			IIdObject result = null;
			while (null == (result = pool.retrieve(id)))
			{
				waitCondition.await();
			}

			assert (result != null);
			return result;
		} finally
		{
			lock.unlock();
		}
	}

	/**
	 * �����Ƿ���ָ����id����Ӧ�Ķ���
	 * @param id ָ����id��
	 * @return �������һ�����󣬸ö����id�ž���ָ����id�ţ���ô����true�����򷵻�false
	 */
	public boolean contains(int id)
	{
		if (id < 0)
		{
			throw new IllegalArgumentException("id should be non negtive");
		}

		return pool.contains(id);
	}

	/**
	 * ������д洢�Ķ���
	 */
	public void clear()
	{
		lock.lock();
		try
		{
			pool.clear();
		} finally
		{
			lock.unlock();
		}
	}

}
