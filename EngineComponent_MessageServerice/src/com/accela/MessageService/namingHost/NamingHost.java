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
{// TODO NamingHost的同步控制以及其各个组件的同步控制都还没有着落，另外几个错误也没有更正
	// TODO 别忘了switchLock，以及在其基础上的同步控制
	// TODO 改造ConnectionCenter的时候，注意Connector中原来对发送信息的串行性的假设
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
		// 初始化变量
		localTable = new LocalNamingTable();
		localTableUpdater = new LocalNamingTableUpdater();
		remoteTable = new RemoteNamingTable();
		accesser = new NamingServerAccesser();
		connectionCenter = SocketConnectionCenter.createInstance(true);

		// 启动NamingServerAccesser
		accesser.open();

		// 启动connectionCenter
		connectionCenter.startToReceiveConnection(); // TODO
		// NamingServer接收的ClientID错了，不应该是连接它的Client的ClientID，而应该是Host给它发过去的

		// 告知NamingServer自己的ClientID
		sendSelfClientID(connectionCenter.getConnectionReceivingClientID());

		// 启动工作线程
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
