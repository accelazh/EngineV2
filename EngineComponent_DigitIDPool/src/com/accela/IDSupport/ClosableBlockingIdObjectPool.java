package com.accela.IDSupport;

import com.accela.SynchronizeSupport.standardSupport.*;

/**
 * 
 * ClosableBlockingIdObjectPool��BlockingIdObjectPool�Ļ����ϸĽ��������������ӵ��id�Ķ���
 * ��ClosableBlockingIdObjectPool�У����ݶ����id����Ŷ�������Ը��ݶ����id����ȡ����
 * 
 * ClosableBlockingIdObjectPool��BlockingIdObjectPool�Ĳ�֮ͬ�����ڣ�ClosableBlockingIdObjectPool
 * ���д򿪺͹رյķ��������ǿɿ���������μ���OpenCloseSupport�еĶ��塣����Ҫ�ȴ�ClosableBlockingPool
 * �����ʹ�á��رպ������������̶߳��ᱻ�ϳ������׳�AlreadyClosedException�����Ҷ������������ܹ���ʹ�ã�
 * ���ܹ������̵߳ķ���һ�����ܹ���ʹ�á�
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
	 * �ر������ע��رպ��������д洢�Ķ���
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
	 * ����һ��ӵ��id�Ķ��������������ӵ����ͬid�Ķ��� ����׳�IllegalArgumentException�쳣
	 * 
	 * @param object
	 *            ������Ķ���
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
	 * ȡ��ָ��id����Ӧ�Ķ�������ö�����BlockingIdObjectPool�У�
	 * ����߳�����ֱ����Ӧ�Ķ��󱻷���BlockingIdObjectPool��
	 * @param id Ҫȥȡ���Ķ����id
	 * @return ָ��Ҫ��ȡ���Ķ�������ö���û�б����룬�������ȴ���
	 * @throws InterruptedException ����ȴ������б��жϡ�
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
			// һ����˵������쳣Ӧ�����������Ĺر������ġ�
			// ��������������жϽ�������������̣߳������
			// �쳣�Ϳ����ڲ��������رյ�ʱ������
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
	 * �����Ƿ���ָ����id����Ӧ�Ķ���
	 * @param id ָ����id��
	 * @return �������һ�����󣬸ö����id�ž���ָ����id�ţ���ô����true�����򷵻�false
	 */
	public boolean contains(int id)
	{
		return pool.contains(id);
	}

	/**
	 * ������д洢�Ķ���
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
