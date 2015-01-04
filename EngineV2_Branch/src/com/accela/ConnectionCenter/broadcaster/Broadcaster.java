package com.accela.ConnectionCenter.broadcaster;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.shared.RemotePackage;
import com.accela.SynchronizeSupport.standard.ClosableBlockingQueue;
import com.accela.SynchronizeSupport.standard.FailedToCloseException;
import com.accela.SynchronizeSupport.standard.FailedToOpenException;
import com.accela.SynchronizeSupport.standard.IOpenClosable;
import com.accela.SynchronizeSupport.standard.OpenCloseSupport;
import com.accela.SynchronizeSupport.standard.TargetMethod;


/**
 * 
 * ������������������ϵĹ㲥��
 * ���ݾ�������缼���������̳к�ʵ�֡�
 *
 * ע�⣬Broadcaster����Ƴ�Ϊ�ɿ����ࣨ���嶨���com.accela.synchronizeSupport.standardSupport.OpenCloseSupport��
 * �����̳�����࣬������µķ�����ʱ���������صĿɿ��������Ʊ�׼��Broadcaster��ʹ�õ�OpenCloseSupport����
 * �Ѿ�����Ƴ�protected���ͣ��ɹ�ʹ�á�
 *
 */
public abstract class Broadcaster implements IOpenClosable
{
	/**
	 * �յ��Ĺ㲥��Ϣ����洢����������У����ȡ��
	 */
	private ClosableBlockingQueue<RemotePackage> receivingQueue = new ClosableBlockingQueue<RemotePackage>();

	private ReceivingThread receivingThread;
	/**
	 * ���������Եط��͹㲥��Ϣ�Ķ�ʱ��
	 */
	private Timer timer;

	private static final String TIMER_NAME = "Broadcaster - timer";
	/**
	 * Ϊ��ʹBroadcaster����ɿ���������Ҫ�󣬶�ʹ�õ�ͬ������ʵ�ְ���
	 */
	protected OpenCloseSupport ocs;

	public Broadcaster()
	{
		ocs = new OpenCloseSupport(new OpenMethod(), new CloseMethod());
	}

	/**
	 * 
	 * ��messageQueue��ȡ��һ���Ѿ��յ��Ķ������û�У����������ʹ�߳�����ֱ�����µ�
	 * �յ��Ķ���
	 * 
	 * ���Broadcaster�Ѿ����رգ�����Ϣ��û�б�ȡ���ʱ�򣬻����Ե����������������Ϣ
	 * �Ѿ���ȡ����ٵ���������������׳�AlreadyClosedException��
	 * 
	 * ���ص�RemotePackage��װ��ָ����Ϣ�����ߵ�ClientID�������͵���Ϣ
	 */
	public RemotePackage retrieveMessage()
	{
		ocs.lockMethod();
		try
		{
			if (!ocs.isOpen() && !hasMessageToRetrieve())
			{
				ocs.ensureOpen();
			}

			return receivingQueue.dequeue();
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * ����Ƿ�����Ϣû��ȡ�� 
	 */
	public boolean hasMessageToRetrieve()
	{
		return !receivingQueue.isEmpty();
	}

	/**
	 * ���ݾ������缼��ʵ�ֵĽ��չ㲥��Ϣ�ķ����������ʱû�пɽ��յ���Ϣ����Ӧ������
	 */
	protected abstract RemotePackage receiveMessageFromBroadcast()
			throws Exception;

	/**
	 * ��message�㲥һ��
	 * @throws FailedToBroadcastMessageException
	 */
	public void broadcastMessage(Object message)
			throws FailedToBroadcastMessageException
	{
		if (null == message)
		{
			throw new NullPointerException();
		}

		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			broadcastMessageImpl(message);
		} catch (Exception ex)
		{
			throw new FailedToBroadcastMessageException(ex);
		} finally
		{
			ocs.unlockMethod();
		}

	}

	/**
	 * ���÷������ܿ췵�أ�Ȼ���ڲ��̻߳�ÿ��periodָ���ĺ���
	 * ʱ�佫message�㲥һ��
	 */
	public void broadcastMessage(Object message, long period)
	{
		if (null == message)
		{
			throw new NullPointerException();
		}
		if (period <= 0)
		{
			throw new IllegalArgumentException();
		}

		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			timer.schedule(new BroadcastMessageTask(message), 0, period);
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * ���ݾ�������缼������message�㲥һ�εķ�������Ҫ������ʵ��
	 */
	protected abstract void broadcastMessageImpl(Object message)
			throws Exception;

	private ReentrantLock synLock = new ReentrantLock();

	/**
	 * ��ֹ��ǰ�������ظ��㲥��Ϣ������
	 * 
	 * @throws BroadcasterClosedException
	 * @throws InterruptedException
	 */
	public void cancelAllBroadcast()
	{
		ocs.lockMethod();
		synLock.lock();
		try
		{
			ocs.ensureOpen();

			timer.cancel();
			timer = new Timer(TIMER_NAME, true);
		} finally
		{
			synLock.unlock();
			ocs.unlockMethod();
		}
	}
	
	@Override
	public boolean isOpen()
	{
		return ocs.isOpen();
	}

	@Override
	public void open() throws FailedToOpenException
	{
		ocs.open();
	}

	protected abstract void openImpl() throws Exception;

	@Override
	public void close() throws FailedToCloseException
	{
		ocs.close();

	}

	/**
	 * ����ʵ�ֵ�ֹͣ���չ㲥��Ϣ�Ĵ�����̣������������close()�е���
	 */
	protected abstract void closeImpl() throws Exception;

	private class OpenMethod implements TargetMethod
	{

		@Override
		public void run() throws Exception
		{
			if (receivingThread != null)
			{
				if (receivingThread.isAlive())
				{
					throw new IllegalStateException(
							"the inner thread has not been exited yet, please wait");
				}
			}

			openImpl();
			receivingQueue.open();
			receivingThread = new ReceivingThread();
			receivingThread.start();
			timer = new Timer(TIMER_NAME, true);

		}

	}

	private class CloseMethod implements TargetMethod
	{

		@Override
		public void run() throws Exception
		{
			try
			{
				closeImpl();
			} finally
			{
				receivingQueue.close();
				timer.cancel();
			}

		}

	}

	// /////////////////////////////////////////////////////////////////////////

	/**
	 * Broadcaster�ڵ��̣߳������Ͻ�����Ϣ��������messageQueue
	 */
	private class ReceivingThread extends Thread
	{
		public ReceivingThread()
		{
			super("Broadcaster - ReceivingThread");
		}

		public void run()
		{
			while (ocs.isOpen())
			{
				try
				{
					RemotePackage message = null;

					message = receiveMessageFromBroadcast();
					if (null == message)
					{
						assert (false);
						throw new NullPointerException(
								"receiveMessageFromBroadcast() should not return null");
					}
					receivingQueue.enqueue(message);

				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * Broadcaster��Timer������ʹ�õ�TimerTask�����������㲥
	 */
	private class BroadcastMessageTask extends TimerTask
	{
		private Object message;

		public BroadcastMessageTask(Object message)
		{

			assert (message != null);
			if (null == message)
			{
				throw new NullPointerException("message should not be null");
			}
			this.message = message;
		}

		@Override
		public void run()
		{
			try
			{
				broadcastMessageImpl(message);
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

	}

}