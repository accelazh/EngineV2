package com.accela.IDSupport;

import com.accela.SynchronizeSupport.standardSupport.*;

/**
 * 
 * ClosableBlockingIdObjectPool在BlockingIdObjectPool的基础上改进而来，用来存放拥有id的对象。
 * 在ClosableBlockingIdObjectPool中，根据对象的id来存放对象，你可以根据对象的id来存取对象。
 * 
 * ClosableBlockingIdObjectPool与BlockingIdObjectPool的不同之处在于，ClosableBlockingIdObjectPool
 * 又有打开和关闭的方法。它是可开关组件，参见类OpenCloseSupport中的定义。你需要先打开ClosableBlockingPool
 * 后才能使用。关闭后，所有阻塞的线程都会被赶出来，抛出AlreadyClosedException，并且多数方法将不能够在使用，
 * 且能够阻塞线程的方法一定不能够在使用。
 * 
 */
public class ClosableBlockingIdObjectPool implements IOpenClosable
{
	private BlockingIdObjectPool pool = new BlockingIdObjectPool();

	private OpenCloseSupport ocs;

	public ClosableBlockingIdObjectPool()
	{
		ocs = new OpenCloseSupport(new OpenMethod(), new CloseMethod());
	}

	@Override
	public boolean isOpen()
	{
		return ocs.isOpen();
	}

	@Override
	public void open() throws AlreadyOpenedException, FailedToOpenException
	{
		ocs.open();
	}

	/**
	 * 关闭组件。注意关闭后会清空所有存储的对象
	 */
	@Override
	public void close() throws AlreadyClosedException, FailedToCloseException
	{
		ocs.close();
	}

	private class OpenMethod implements TargetMethod
	{
		@Override
		public void run() throws Exception
		{
			pool.clear();
		}

	}

	private class CloseMethod implements TargetMethod
	{
		@Override
		public void run() throws Exception
		{

		}

	}

	// /////////////////////////////////////////////////////////////////////////////

	/**
	 * 放入一个拥有id的对象。如果放入两个拥有相同id的对象， 则会抛出IllegalArgumentException异常
	 * 
	 * @param object
	 *            被放入的对象
	 */
	public void put(IIdObject object)
	{
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();
			
			pool.put(object);
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * 取回指定id所对应的对象。如果该对象不再BlockingIdObjectPool中，
	 * 则会线程阻塞直到相应的对象被放入BlockingIdObjectPool中
	 * @param id 要去取出的对象的id
	 * @return 指定要被取出的对象。如果该对象还没有被放入，则阻塞等待。
	 * @throws InterruptedException 如果等待过程中被中断。
	 */
	public IIdObject retrieve(int id)
	{
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();
			
			return pool.retrieve(id);
		} catch (InterruptedException ex)
		{
			// 一般来说，这个异常应该是由这个类的关闭引发的。
			// 但是如果外界故意中断进入这个方法的线程，则这个
			// 异常就可能在不是这个类关闭的时候发生了
			throw new AlreadyClosedException(
					"there should be somebody who has closed "
							+ "ClosableBlockingQueue, or someone has interrupted "
							+ "the thread intentively");
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * 测试是否含有指定的id所对应的对象
	 * @param id 指定的id号
	 * @return 如果含有一个对象，该对象的id号就是指定的id号，那么返回true；否则返回false
	 */
	public boolean contains(int id)
	{
		return pool.contains(id);
	}

	/**
	 * 清空所有存储的对象
	 */
	public void clear()
	{
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();
			
			pool.clear();
		} finally
		{
			ocs.unlockMethod();
		}
	}

}
