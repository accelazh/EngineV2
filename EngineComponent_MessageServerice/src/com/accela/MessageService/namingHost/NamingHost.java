package com.accela.MessageService.namingHost;

import com.accela.ConnectionCenter.ConnectionCenter;
import com.accela.ConnectionCenter.ConnectionCenterAlreadyShutDownException;
import com.accela.ConnectionCenter.ConnectionReceivingFunctionNotStartedException;
import com.accela.ConnectionCenter.FailedToGetConnectionReceivingClientIDException;
import com.accela.ConnectionCenter.FailedToShutDownConnectionCenterException;
import com.accela.ConnectionCenter.FailedWhenStartToReceiveConnectionException;
import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.ConnectionCenter.util.SwitchLock;
import com.accela.MessageService.messages.namingHostToNamingServer.NamingHostRegisterMessage;
import com.accela.MessageService.namingHost.namingServerAccess.FailedToCloseNamingServerAccesserException;
import com.accela.MessageService.namingHost.namingServerAccess.FailedToOpenNamingServerAccesserException;
import com.accela.MessageService.namingHost.namingServerAccess.NamingServerAccesser;
import com.accela.SocketConnectionCenter.SocketConnectionCenter;

public class NamingHost
{// TODO NamingHost��ͬ�������Լ�����������ͬ�����ƶ���û�����䣬���⼸������Ҳû�и���
	// TODO ������switchLock���Լ���������ϵ�ͬ������
	// TODO ����ConnectionCenter��ʱ��ע��Connector��ԭ���Է�����Ϣ�Ĵ����Եļ���
	private LocalNamingTable localTable;

	private LocalNamingTableUpdater localTableUpdater;

	private RemoteNamingTable remoteTable;

	private NamingServerAccesser accesser;

	private ConnectionCenter connectionCenter;

	private MessageReceivingThread messageReceivingThread;

	private boolean open = false;

	private SwitchLock switchLock = new SwitchLock();

	// /////////////////////////////////////////////////////////////////

	public boolean isOpen()
	{
		return open;
	}

	public void open() throws FailedToOpenNamingHostException,
			InterruptedException
	{
		switchLock.lockSwitch();

		boolean shouldRemainOpen = false;
		try
		{
			if (isOpen())
			{
				shouldRemainOpen = true;
				throw new IllegalStateException(
						"NamingServer is already opened");
			}

			open = true;
			openImpl();
		} catch (Exception ex)
		{
			if (shouldRemainOpen)
			{
				assert (open);
			} else
			{
				open = false;
			}
			throw new FailedToOpenNamingHostException(ex);
		} finally
		{
			switchLock.unlockSwitch();
		}
	}

	private void openImpl() throws FailedWhenStartToReceiveConnectionException,
			ConnectionCenterAlreadyShutDownException, InterruptedException,
			FailedToOpenNamingServerAccesserException,
			ConnectionReceivingFunctionNotStartedException,
			FailedToGetConnectionReceivingClientIDException
	{
		// ��ʼ������
		localTable = new LocalNamingTable();
		localTableUpdater = new LocalNamingTableUpdater();
		remoteTable = new RemoteNamingTable();
		accesser = new NamingServerAccesser();
		connectionCenter = SocketConnectionCenter.createInstance(true);

		// ����NamingServerAccesser
		accesser.open();

		// ����connectionCenter
		connectionCenter.startToReceiveConnection(); // TODO
		// NamingServer���յ�ClientID���ˣ���Ӧ������������Client��ClientID����Ӧ����Host��������ȥ��

		// ��֪NamingServer�Լ���ClientID
		sendSelfClientID(connectionCenter.getConnectionReceivingClientID());

		// ���������߳�
		messageReceivingThread = new MessageReceivingThread();
		messageReceivingThread.start();

	}

	private void sendSelfClientID(ClientID clientID) throws InterruptedException
	{
		assert (clientID != null);

		accesser.registerNamingHost(clientID);

	}

	public void close() throws FailedToCloseNamingHostException,
			InterruptedException
	{
		switchLock.lockSwitch();

		try
		{
			if (!isOpen())
			{
				throw new IllegalStateException(
						"NamingServer is already closed");
			}

			open = false;

			closeImpl();
		} catch (Exception ex)
		{
			throw new FailedToCloseNamingHostException(ex);
		} finally
		{
			switchLock.unlockSwitch();
		}
	}

	private void closeImpl() throws InterruptedException,
			ConnectionCenterAlreadyShutDownException,
			FailedToShutDownConnectionCenterException,
			FailedToCloseNamingServerAccesserException
	{
		try
		{
			try
			{
				connectionCenter.shutDownConnectionCenter();
			} finally
			{
				accesser.close();
			}

		} finally
		{
			messageReceivingThread.interrupt();
		}
	}

	// ////////////////////////////////////////////////////////

	private class MessageReceivingThread extends Thread
	{
		public MessageReceivingThread()
		{
			super("NamingHost - MessageReceivingThread");
		}
	}

}
