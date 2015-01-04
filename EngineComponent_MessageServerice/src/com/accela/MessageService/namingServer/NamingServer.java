package com.accela.MessageService.namingServer;

import java.net.UnknownHostException;
import java.util.HashSet;
import java.util.Set;

import com.accela.ConnectionCenter.BroadcastFunctionClosedException;
import com.accela.ConnectionCenter.ClientNotConnectedException;
import com.accela.ConnectionCenter.ConnectionCenter;
import com.accela.ConnectionCenter.ConnectionCenterAlreadyShutDownException;
import com.accela.ConnectionCenter.ConnectionReceivingFunctionNotStartedException;
import com.accela.ConnectionCenter.FailedToGetConnectionReceivingClientIDException;
import com.accela.ConnectionCenter.FailedToOpenBroadcastFunctionException;
import com.accela.ConnectionCenter.FailedToShutDownConnectionCenterException;
import com.accela.ConnectionCenter.FailedWhenStartToReceiveConnectionException;
import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.ConnectionCenter.shared.RemotePackage;
import com.accela.ConnectionCenter.util.SwitchLock;
import com.accela.MessageService.messages.namingHostToNamingServer.NamingHostRegisterMessage;
import com.accela.MessageService.messages.namingHostToNamingServer.NamingHostRegisterReplyMessage;
import com.accela.MessageService.messages.namingHostToNamingServer.NamingQueryMessage;
import com.accela.MessageService.messages.namingHostToNamingServer.NamingUpdateMessage;
import com.accela.MessageService.messages.namingServerToNamingHost.NamingExistanceMessage;
import com.accela.MessageService.messages.namingServerToNamingHost.NamingInvalidMessage;
import com.accela.MessageService.messages.namingServerToNamingHost.NamingQueryReplyMessage;
import com.accela.MessageService.messages.namingServerToNamingHost.NamingUpdateReplyMessage;
import com.accela.MessageService.shared.ConvenientObjectPool;
import com.accela.MessageService.shared.Name;
import com.accela.SocketConnectionCenter.SocketConnectionCenter;

/**
 * 
 * 一、NamingServer是干什么的？ 每个NamingHost都可以注册自己的Name，这些Name活动于注册它们的NamingHost之上。
 * 比如NamingHost1注册了NameA，NamingHost2注册了NameB。当NamingHost1上有什么程序，想要给，
 * NameA发送一个信息包的时候，我们希望它只需要指出目标是NameB，然后发送，
 * 这个信息包就可以被某种机制自动送到NamingHostB上的NameB处。 为了达到这个功能，就需要知道一个Name所指出的目标，在网络上的位置，
 * 这个位置可以是IP地址等等，这里用ConnectionCenter中定义的ClientID标识。
 * 也就是说，需要有一个功能，你给出一个Name，它就告诉你这个是哪个NamingHost
 * 注册了这个Name，并给出这个NamingHost的ClientID。 NamingServer就是干这个事的。
 * 
 * 二、NamingServer如何工作？ 1、NamingServer中保存有NamingTable，这是一个映射表，记录着一个个Name
 * 映射到什么ClientID上。 2、NamingServer可已响应NamingHost的查询。NamingHost可以发送个NamingServer消息
 * NamingQueryMessage，询问一个Name对应着什么ClientID。NamingServer通过消息
 * NamingQeryReplyMessage回复发出查询的NamingHost。
 * 3、NamingServer可以响应NamingHost对所注册的Name的变更。当一个NamingHost变更了自己
 * 所注册的Name后，会发送消息NamingUpdateMessage来通知NamingServer，NamingServer
 * 根据收到的消息来更新自己的NamingTable，根据更新是否成功，回复
 * NamingUpdateReplyMessage。NamingTable具有回滚能力，即如果更新失败，则回滚到 开始更新之前的状态。
 * 如果更新成功，NamingServer还会通知除那个发起更新的NamingHost以外的所有NamingHost，
 * 它们自己所保存的Name-Client对应表已经失效。
 * 4、NamingServer会自动广播，以告知各个NamingHost自己的存在，并且提供连接到自己的 方法，一边NamingHost连接上自己
 * 5、NamingHost可能会关闭并离开NamingServer。因此NamingServer会自动检测失去了NamingHost
 * 的Name，并且删除它们。
 * 
 * 
 */
public class NamingServer
{
	private NamingTable namingTable;

	private HostTable hostTable;

	private ConnectionCenter connectionCenter;

	private static final long EXISTANCE_BROADCAST_PERIOD = 2000;

	private MessageProcessingThread messageProcThread;

	private NamingSweepingThread namingSweepingThread;

	private boolean open = false;

	private SwitchLock switchLock = new SwitchLock();

	// /////////////////////////////////////////////////////////////////

	public boolean isOpen()
	{
		return open;
	}

	public void open() throws InterruptedException,
			FailedToOpenNamingServerException
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
			throw new FailedToOpenNamingServerException(ex);
		} finally
		{
			switchLock.unlockSwitch();
		}
	}

	private void openImpl() throws FailedWhenStartToReceiveConnectionException,
			ConnectionCenterAlreadyShutDownException, InterruptedException,
			FailedToOpenBroadcastFunctionException, UnknownHostException,
			BroadcastFunctionClosedException,
			ConnectionReceivingFunctionNotStartedException,
			FailedToGetConnectionReceivingClientIDException
	{
		// 初始化变量
		namingTable = new NamingTable();
		hostTable = new HostTable();
		connectionCenter = SocketConnectionCenter.createInstance(true);
		messageProcThread = new MessageProcessingThread();

		// 开始广播，并开始接受连接
		connectionCenter.startToReceiveConnection();
		connectionCenter.openBroadcastFunction();
		connectionCenter.setBroadcastMessageBufferMaxSize(1); // NamingServer不准备接收广播
		connectionCenter.broadcastMessage(new NamingExistanceMessage(
				connectionCenter.getConnectionReceivingClientID()),
				EXISTANCE_BROADCAST_PERIOD);

		// 启动工作线程
		messageProcThread = new MessageProcessingThread();
		messageProcThread.start();

		namingSweepingThread = new NamingSweepingThread();
		namingSweepingThread.start();
	}

	public void close() throws InterruptedException,
			FailedToCloseNamingServerException
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
			throw new FailedToCloseNamingServerException(ex);
		} finally
		{
			closeOnFailure();

			switchLock.unlockSwitch();
		}
	}

	private void closeImpl() throws ConnectionCenterAlreadyShutDownException,
			InterruptedException, FailedToShutDownConnectionCenterException
	{
		assert (namingTable != null);
		namingTable.clear();

		assert (hostTable != null);
		hostTable.clear();

		assert (connectionCenter != null);
		assert (!connectionCenter.isShutDown());
		assert (connectionCenter.isBroadcastFunctionOpen());
		assert (connectionCenter.isReceivingConnection());
		connectionCenter.shutDownConnectionCenter();

		assert (messageProcThread != null);
		assert (messageProcThread.isAlive());
		messageProcThread.interrupt();

		assert (namingSweepingThread != null);
		assert (namingSweepingThread.isAlive());
		namingSweepingThread.interrupt();

	}

	private void closeOnFailure()
	{
		assert (messageProcThread != null);
		messageProcThread.interrupt();
	}

	// //////////////////////////////////////////////////////////

	private class MessageProcessingThread extends Thread
	{
		public MessageProcessingThread()
		{
			super("NamingServer - MessageProcessingThread");
		}

		public void run()
		{
			while (isOpen())
			{
				try
				{
					RemotePackage pack = connectionCenter.retriveMessage();
					assert (pack != null);
					if (null == pack.getMessage())
					{
						continue;
					}

					if (pack.getMessage() instanceof NamingQueryMessage)
					{
						handleNamingQueryMessage((NamingQueryMessage) pack
								.getMessage(), pack.getClientID());
					} else if (pack.getMessage() instanceof NamingUpdateMessage)
					{
						handleNamingUpdateMessage((NamingUpdateMessage) pack
								.getMessage(), pack.getClientID());
					} else if (pack.getMessage() instanceof NamingHostRegisterMessage)
					{
						handleNamingHostRegisterMessage(
								(NamingHostRegisterMessage) pack.getMessage(),
								pack.getClientID());
					} else
					{
						// some error happens or some one has send here an
						// invalid message

						assert (false);
					}
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

	private void handleNamingHostRegisterMessage(
			NamingHostRegisterMessage message, ClientID sender)
			throws ClientNotConnectedException,
			ConnectionCenterAlreadyShutDownException, InterruptedException
	{
		assert (sender != null);
		assert (message != null);

		boolean succ = true;

		ClientID clientID = message.getHowToConnectMe();
		if (null == clientID)
		{
			succ = false;
		}

		if (succ)
		{
			boolean result = hostTable.add(sender, clientID);
			if (!result)
			{
				succ = false;
			}
		}

		NamingHostRegisterReplyMessage reply = new NamingHostRegisterReplyMessage(
				succ);

		connectionCenter.sendMessage(sender, reply);
	}

	private void handleNamingQueryMessage(NamingQueryMessage message,
			ClientID sender) throws ClientNotConnectedException,
			ConnectionCenterAlreadyShutDownException, InterruptedException
	{
		assert (sender != null);
		assert (message != null);

		NamingQueryReplyMessage reply = ConvenientObjectPool
				.newNamingQueryReplyMessage();
		assert (reply != null);

		for (Name n : message)
		{
			if (null == n)
			{
				reply.addQueriedClientID(null);
			} else
			{
				reply.addQueriedClientID(namingTable.lookup(n));
			}
		}

		connectionCenter.sendMessage(sender, reply);

		ConvenientObjectPool.deleteNamingQueryMessage(message);
		ConvenientObjectPool.deleteNamingQueryReplyMessage(reply);
	}

	private void handleNamingUpdateMessage(NamingUpdateMessage message,
			ClientID sender) throws ClientNotConnectedException,
			ConnectionCenterAlreadyShutDownException, InterruptedException
	{
		assert (sender != null);
		assert (message != null);

		// 更新namingTable并回复发起更新者
		NamingUpdateReplyMessage reply = new NamingUpdateReplyMessage();

		ClientID hostToHost = hostTable.lookup(sender);
		if (null == hostToHost)
		{
			reply.setSucc(false);
			reply.setFailedOerationIndex(-1);

			connectionCenter.sendMessage(sender, reply);
			return;
		}

		int result = namingTable
				.update(message.getOperationQueue(), hostToHost);
		if (result < 0)
		{
			reply.setSucc(true);
			reply.setFailedOerationIndex(-1);
		} else
		{
			reply.setSucc(false);
			reply.setFailedOerationIndex(result);
		}

		connectionCenter.sendMessage(sender, reply);

		// 如果更新成功，则向其它host发出NamingInvalidMessage
		if (result < 0)
		{
			ClientID[] hosts = connectionCenter.getConnectedClientIDs();
			assert (hosts != null);
			for (ClientID clientID : hosts)
			{
				if (null == clientID)
				{
					assert (false);
					continue;
				}
				if (clientID.equals(sender))
				{
					continue;
				}

				sendNamingInvalidMessage(clientID);
			}
		}

	}

	private void sendNamingInvalidMessage(ClientID clientID)
			throws ClientNotConnectedException,
			ConnectionCenterAlreadyShutDownException, InterruptedException
	{
		assert (clientID != null);

		NamingInvalidMessage message = new NamingInvalidMessage();
		assert (message != null);

		connectionCenter.sendMessage(clientID, message);

	}

	/**
	 * 
	 * 当一个NamingHost退出后，它所占用的Name应该被删除，这就是这个线程 干的事。
	 * 
	 * 这个线程每个一定周期扫描一次，当第一次发现某个NamingHost已经断开时，
	 * 不会立即删除其在namingTable中的记录。而当连续两次扫描都发现
	 * 某个NamingHost已经断开的时候，才会删除其在namingTable中的对应记录。
	 * 
	 */
	private class NamingSweepingThread extends Thread
	{
		public NamingSweepingThread()
		{
			super("NamingServer - NamingSweepingThread");
			this.setPriority(MIN_PRIORITY);
		}

		private Set<ClientID> possibleExitedClients = new HashSet<ClientID>();

		private static final long PERIOD = 30 * 1000;

		public void run()
		{
			while (isOpen())
			{
				try
				{
					// 得到哪些ClientID已经变得无效了
					Set<ClientID> curHosts = new HashSet<ClientID>();
					for (ClientID c : connectionCenter.getConnectedClientIDs())
					{
						if (c != null)
						{
							ClientID hostToHost = hostTable.lookup(c);
							if (hostToHost != null)
							{
								curHosts.add(hostToHost);
							}
						}
					}

					Set<ClientID> recordedHosts = namingTable.valueSet();
					recordedHosts.removeAll(curHosts);

					final Set<ClientID> invalidHosts = recordedHosts;

					// 查找出应该被删除的ClientID
					curHosts.clear();
					final Set<ClientID> toBeRemoved = curHosts;

					for (ClientID clientID : possibleExitedClients)
					{
						if (invalidHosts.contains(clientID))
						{
							// 两次被被发现无效host应该被删除
							toBeRemoved.add(clientID);
						}
					}

					// 更新possibleExitedClients
					invalidHosts.removeAll(toBeRemoved);
					possibleExitedClients = invalidHosts;

					// 实施删除
					namingTable.removeClients(toBeRemoved);

					// hostTable中此处留有一个Bug，就是hostTable没有删除对应的项目

					Thread.sleep(PERIOD);
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}

	}
}
