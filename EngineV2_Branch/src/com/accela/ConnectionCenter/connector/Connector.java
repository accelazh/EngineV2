package com.accela.ConnectionCenter.connector;

import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.ConnectionCenter.shared.RemotePackage;
import com.accela.SynchronizeSupport.standard.ClosableBlockingQueue;
import com.accela.SynchronizeSupport.standard.FailedToCloseException;
import com.accela.SynchronizeSupport.standard.FailedToOpenException;
import com.accela.SynchronizeSupport.standard.IOpenClosable;
import com.accela.SynchronizeSupport.standard.OpenCloseSupport;
import com.accela.SynchronizeSupport.standard.TargetMethod;

/**
 * 
 * �������� ����ͻ�������һ�����Ӻ������ʹ���������ӡ� �������sendMessage��retriveMessage��ͨ������ �����ͺͽ�����Ϣ��
 * 
 * ע�⣬Connector����Ƴ�Ϊ�ɿ����ࣨ���嶨���com.accela.synchronizeSupport.standardSupport.
 * OpenCloseSupport��
 * �����̳�����࣬������µķ�����ʱ���������صĿɿ��������Ʊ�׼��Connector��ʹ�õ�OpenCloseSupport����
 * �Ѿ�����Ƴ�protected���ͣ��ɹ�ʹ�á�
 * 
 */
public abstract class Connector implements Comparable<Connector>, IOpenClosable
{
	/**
	 * ��Connector��Client�յ�һ������ʱ��������󽫻ᱻ���뵽receivingQueue��
	 */
	private ClosableBlockingQueue<RemotePackage> receivingQueue = new ClosableBlockingQueue<RemotePackage>();
	/**
	 * �����½�Connector���̵߳��̳߳�
	 */
	private static final ExecutorService THREAD_POOL = Executors
			.newCachedThreadPool();
	/**
	 * ִ��ReceivingThreadTask������̵߳�Future�����������Ƹ��߳�
	 */
	private Future<?> receivingThread;
	/**
	 * ��¼���Connector�����ӵ�Զ�̶˵�ClientID
	 */
	private ClientID storedClientID;
	/**
	 * Ϊ��ʹConnector����ɿ���������Ҫ�󣬶�ʹ�õ�ͬ������ʵ�ְ���
	 */
	protected OpenCloseSupport ocs;

	public Connector()
	{
		ocs = new OpenCloseSupport(new OpenMethod(), new CloseMethod());
	}

	/**
	 * �����������֤Connector���Ͷ����ʱ����һ��һ�����͵ġ�
	 */
	private ReentrantLock sendLock = new ReentrantLock();

	/**
	 * ��һ�������Connector��Ҫ��������������͸�Client�� ����message���������͡�
	 * 
	 * @param message
	 *            ����Connector�Ķ���
	 * @throws FailedToSendMessageException
	 */
	public void sendMessage(Object message) throws FailedToSendMessageException
	{
		if (null == message)
		{
			throw new NullPointerException();
		}

		sendLock.lock();
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			sendMessageToClient(message);
		} catch (Exception ex)
		{
			throw new FailedToSendMessageException(ex);
		} finally
		{
			ocs.unlockMethod();
			sendLock.unlock();
		}
	}

	/**
	 * ���������message�����͸�Client�����ݾ������缼����ͬӦ���в�ͬ��ʵ��
	 * 
	 * @throws Exception
	 */
	protected abstract void sendMessageToClient(Object message)
			throws Exception;

	/**
	 * ��Connector��ȡ�������������ӵ�Client�����յ��Ķ��� ��ε��������������������ȡ��Client�������Ķ�������
	 * ���Ķ����ȴ�����������ء�
	 * 
	 * ע�����û�ж�����Ա�ȡ�أ�����������������߳������� ֱ���ж�����Ա�ȡ��ʱ���ŷ����������
	 * 
	 * �رպ������ʣ����Ϣû��ȡ��������Ȼ���Ե������������
	 * ���������Ϣ�Ѿ���ȡ�⣬�ٵ�����������ͻ��׳�AlreadyClosedException��
	 * 
	 * return Connector�����յ���Client���͹����Ķ�������Ѿ� ȡ����ȫ���Ķ����򷵻�null
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
	 * 
	 * ����Ƿ��еȴ�ȡ������Ϣ
	 * 
	 */
	public boolean hasMessageToRetrieve()
	{
		return !receivingQueue.isEmpty();
	}

	/**
	 * ���ݾ������缼��ʵ�ֵĽ��ն���ķ��� ��Client�����յ�һ�����������ʱû�ж����͹�����
	 * ����ø÷������߳̽��뱻����ֱ���յ��Է��������Ķ��� ����������ݾ������缼����ͬӦ���в�ͬ��ʵ�֡�
	 * 
	 * ��������ӵ�Զ�̶˵Ŀͻ��Ͽ����ӣ��򱾵����Ӧ���׳�ClientDisconnectException��
	 * ��Connector���յ�����쳣ʱ�����Զ��ر�
	 * 
	 * @return Client��������һ������
	 * @throws Exception
	 */
	protected abstract Object receiveMessageFromClient() throws Exception;

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

	protected abstract void closeImpl() throws Exception;

	private class OpenMethod implements TargetMethod
	{

		@Override
		public void run() throws Exception
		{
			if (receivingThread != null) 
			{
				if (!receivingThread.isDone())
				{
					throw new IllegalStateException(
							"the inner thread has not been exited yet, please wait");
				}
			}

			storedClientID = null;
			openImpl();
			receivingQueue.open();
			receivingThread = THREAD_POOL.submit(new ReceivingThreadTask());
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
				storedClientID = null;
				receivingQueue.close();
			}

		}

	}

	/**
	 * �����Connector�����ӵ�Client��ClientID�� ��������������getClientIDImpl()���õ�ClientID
	 * 
	 * ע�ⷵ�ص�Ӧ�������Connector�����ӵ�Զ�̶˵Ŀͻ���ClientID�� �������Լ���ClientID
	 * 
	 * ������Connector��û�д򿪣�������null
	 */
	public ClientID getClientID()
	{
		if (!ocs.isOpen())
		{
			return null;
		}

		if (null == storedClientID)
		{
			ocs.lockMethod();
			try
			{
				storedClientID = getClientIDImpl();
			} finally
			{
				ocs.unlockMethod();
			}

			assert (storedClientID != null);
			if (null == storedClientID)
			{
				throw new NullPointerException(
						"getClientIDImpl() should not return null");
			}
		}

		return storedClientID;
	}

	/**
	 * �������Connector�����ӵ�Client��ClientID����ֻ֤�ڴ򿪵�����µ���
	 */
	protected abstract ClientID getClientIDImpl();

	// /////////////////////////////////////////////////////////////////////

	/**
	 * Connector�е��̣߳�����Ϊ�����߳���ִ�е����� �����𲻶Ͻ���Client���͹����Ķ��󣬲�������� receivingQueue
	 */
	private class ReceivingThreadTask implements Runnable
	{
		private int exceptionCount=0;
		
		public void run()
		{
			boolean exitAndClose = false;

			while (ocs.isOpen())
			{
				try
				{
					Object message = null;

					message = receiveMessageFromClient();
					if (null == message)
					{
						assert (false);
						throw new NullPointerException(
								"receiveMessageFromClient() should not return null");
					}
					receivingQueue.enqueue(RemotePackage.newObject(
							getClientID(), message));
					
					exceptionCount=0;
				} catch (ClientDisconnectException ex)
				{
					exitAndClose = true;
					break;
				} catch (Exception ex)
				{
					exceptionCount++;
					if(exceptionCount>3)
					{
						exitAndClose=true;
					}
					
					ex.printStackTrace();
					
					break;
				}
			}

			try
			{
				if (exitAndClose)
				{
					close();
				}
			} catch (FailedToCloseException ex)
			{
				ex.printStackTrace();
			}

		}
	}

	// ///////////////////////////////////////////////////////////////////////

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof Connector))
		{
			return false;
		}

		Connector other = (Connector) obj;

		// get ClientIDs
		ClientID selfClientID = null;
		ClientID otherClientID = null;
		boolean selfHasClientID = true;
		boolean otherHasClientID = true;

		selfClientID = getClientID();

		if (selfClientID == null)
		{
			selfHasClientID = false;
		}

		otherClientID = other.getClientID();

		if (otherClientID == null)
		{
			otherHasClientID = false;
		}

		// compare ClientIDs
		if (selfHasClientID && otherHasClientID)
		{
			return selfClientID.equals(otherClientID);
		} else if (!selfHasClientID && otherHasClientID)
		{
			return false;
		} else if (selfHasClientID && !otherHasClientID)
		{
			return false;
		} else
		{
			return true;
		}

	}

	@Override
	public int hashCode()
	{
		// get ClientIDs
		ClientID selfClientID = null;
		boolean selfHasClientID = true;

		selfClientID = getClientID();

		if (selfClientID == null)
		{
			selfHasClientID = false;
		}

		// get hashCode
		if (selfHasClientID)
		{
			return selfClientID.hashCode();
		} else
		{
			return 0; // ����û��ClientID��Connector����Ϊ���
		}

	}

	@Override
	public int compareTo(Connector other)
	{
		if (null == other)
		{
			return 1;
		}

		// get ClientIDs
		ClientID selfClientID = null;
		ClientID otherClientID = null;
		boolean selfHasClientID = true;
		boolean otherHasClientID = true;

		selfClientID = getClientID();

		if (selfClientID == null)
		{
			selfHasClientID = false;
		}

		otherClientID = other.getClientID();

		if (otherClientID == null)
		{
			otherHasClientID = false;
		}

		// compare CliendIDs
		if (selfHasClientID && otherHasClientID)
		{
			return selfClientID.compareTo(otherClientID);
		} else if (!selfHasClientID && otherHasClientID)
		{
			return -1;
		} else if (selfHasClientID && !otherHasClientID)
		{
			return 1;
		} else
		{
			return 0;
		}
	}

}
