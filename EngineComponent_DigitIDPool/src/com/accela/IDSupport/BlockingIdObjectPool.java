package com.accela.IDSupport;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * BlockingIdObjectPool在IdObjectPool的基础上改进而来，用来存放拥有id的对象。
 * 在BlockingIdObjectPool中，根据对象的id来存放对象，你可以根据对象的id来存取对象。
 * 
 * BlockingIdObjectPool与IdObjectPool的不同之处在于，BlockingIdObjectPool
 * 的retrieve(int)方法会在取不到指定的对像的时候，等待直到指定的对象被放入。
 * 
 */
public class BlockingIdObjectPool
{
	private IdObjectPool pool = new IdObjectPool();

	private ReentrantLock lock = new ReentrantLock();

	private Condition waitCondition = lock.newCondition();

	/**
	 * 放入一个拥有id的对象。如果放入两个拥有相同id的对象， 则会抛出IllegalArgumentException异常
	 * 
	 * @param object
	 *            被放入的对象
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
	 * 取回指定id所对应的对象。如果该对象不再BlockingIdObjectPool中，
	 * 则会线程阻塞直到相应的对象被放入BlockingIdObjectPool中
	 * @param id 要去取出的对象的id
	 * @return 指定要被取出的对象。如果该对象还没有被放入，则阻塞等待。
	 * @throws InterruptedException 如果等待过程中被中断。
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
	 * 测试是否含有指定的id所对应的对象
	 * @param id 指定的id号
	 * @return 如果含有一个对象，该对象的id号就是指定的id号，那么返回true；否则返回false
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
	 * 清空所有存储的对象
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
