package com.accela.ConnectionCenter.connectionReceiver;

import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.ConnectionCenter.shared.NewConnectionInfo;
import com.accela.SynchronizeSupport.standard.ClosableBlockingQueue;
import com.accela.SynchronizeSupport.standard.FailedToCloseException;
import com.accela.SynchronizeSupport.standard.FailedToOpenException;
import com.accela.SynchronizeSupport.standard.IOpenClosable;
import com.accela.SynchronizeSupport.standard.OpenCloseSupport;
import com.accela.SynchronizeSupport.standard.TargetMethod;

/**
 * 
 * �Ҽٶ����½�һ�����ӵ�������
 * 1��ConnectionReceiver���ϼ����Ƿ���Client����
 * 2����������Client���Ӻ�����NewConnectionInfo
 * 3��ͨ��NewConnectionInfo����һ��Connector
 * 4�����Connector�����Client�����ӣ����Ի���ͨ��
 * 
 * ConnectionReceiver�������������Ƿ���Client���ӵġ�������
 * �Ƴɶ�����������缼�����ھ�������缼���У���Ҫ����������
 * �����ʵ�ʵĹ��ܡ�
 * 
 * �����ļ̳��߽����þ�������缼��ʵ�����������ʵ�ּ���Client
 * �Ĺ��ܣ���������Client����������������Ҫ�������ӵ�ʱ��ʱ����
 * ����Ҫ����NewConnectionInfo��Ȼ�����acceptConnection��
 * ���������ķ���ֵ��һ���������������ط�����Ϣ��IMessageSender��
 * �˺�������缼���Ĳ�ͬ�����������Ҫ��IMessageSender���󴫸�Client��
 * ������������Ѿ������ȫ��Ԥ��Ĺ����ˡ�
 * 
 * ע�⣬ConnectionReceiver����Ƴ�Ϊ�ɿ����ࣨ���嶨���com.accela.synchronizeSupport.standardSupport.OpenCloseSupport��
 * �����̳�����࣬������µķ�����ʱ���������صĿɿ��������Ʊ�׼��ConnectionReceiver��ʹ�õ�OpenCloseSupport����
 * �Ѿ�����Ƴ�protected���ͣ��ɹ�ʹ�á�
 *
 */
public abstract class ConnectionReceiver implements IOpenClosable
{
	/**
	 * �����������Ϣ�Ķ���
	 */
	private ClosableBlockingQueue<NewConnectionInfo> infoQueue = new ClosableBlockingQueue<NewConnectionInfo>();
	/**
	 * ����Client�����ӵ��߳�
	 */
	private DetectingThread detectingThread;
	/**
	 * Ϊ��ʹConnectionReceiver����ɿ���������Ҫ�󣬶�ʹ�õ�ͬ������ʵ�ְ���
	 */
	protected OpenCloseSupport ocs;

	public ConnectionReceiver()
	{
		ocs = new OpenCloseSupport(new OpenMethod(), new CloseMethod());
	}

	/**
	 * 
	 * ���ݾ�������缼��ʵ�ֵļ���Client�����ӵķ���������ClientҪ������
	 * ��ʱ�򣬷���NewConnectionInfo�������ʱû��Client���ӣ�Ӧ��������
	 * ��������
	 * 
	 * @return ����ClientҪ�����ӵ�ʱ�򣬷���NewConnectionInfo����¼�������ӵı�Ҫ��Ϣ
	 *
	 * @throws Exception
	 */
	protected abstract NewConnectionInfo detectingConnection() throws Exception;
	
	/**
	 * 
	 * ȡ��NewConnectionInfo�����û�У�������ֱ���С�
	 * ���ConnectionReceiver�Ѿ����رգ���NewConnectionInfo��û�б�
	 * ȡ���ʱ�򣬻����Ե��������������NewConnectionInfo�Ѿ���ȡ���
	 * �ٵ���������������׳�AlreadyClosedException��
	 */
	public NewConnectionInfo retriveNewConnectionInfo()
	{
		ocs.lockMethod();
		try
		{
			if (!ocs.isOpen() && !hasNewConnectionInfoToRetrieve())
			{
				ocs.ensureOpen();
			}

			return infoQueue.dequeue();
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * ����ǻ���NewConnectionInfoû��ȡ��  
	 */
	public boolean hasNewConnectionInfoToRetrieve()
	{
		return !infoQueue.isEmpty();
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

	protected abstract void closeImpl() throws Exception;

	private class OpenMethod implements TargetMethod
	{

		@Override
		public void run() throws Exception
		{
			if (detectingThread != null)
			{
				if (detectingThread.isAlive())
				{
					throw new IllegalStateException(
							"the inner thread has not been exited yet, please wait");
				}
			}

			openImpl();
			infoQueue.open();
			detectingThread = new DetectingThread();
			detectingThread.start();

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
				infoQueue.close();
			}

		}

	}

	/**
	 * @return ����һ��ClientID�����ClientIDָ���������
	 * ��ConnectionReceiver����ConnectionCenter�е�
	 * openConnection������ʹ�����ClientID�������ӵ�
	 * ConnectionReceiver�ϣ��Ӷ��������ӦConnectionCenter
	 * �����ӡ�
	 */
	public ClientID getConnectionReceivingClientID()
	{
		ocs.lockMethod();
		
		try
		{
			ocs.ensureOpen();

			ClientID clientID = getConnectionReceivingClientIDImpl();

			assert (clientID != null);
			return clientID;
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * getConnectionReceivingClientID()��ʵ�ַ�������ֻ֤�ڴ򿪵�����µ���
	 */
	protected abstract ClientID getConnectionReceivingClientIDImpl();

	// //////////////////////////////////////////////////////////////////////

	/**
	 * ����Client���ӣ����������ӵ��߳�
	 */
	private class DetectingThread extends Thread
	{
		public DetectingThread()
		{
			super("ConnectionReceiver - DetectingThread");
		}

		public void run()
		{
			while (ocs.isOpen())
			{
				try
				{
					NewConnectionInfo info = null;

					info = detectingConnection();
					if (null == info)
					{
						assert (false);
						throw new NullPointerException(
								"detectingConnection() should not return null");
					}
					infoQueue.enqueue(info);

				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}

	}

}
