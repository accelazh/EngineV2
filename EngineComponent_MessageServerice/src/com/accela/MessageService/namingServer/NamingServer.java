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
 * һ��NamingServer�Ǹ�ʲô�ģ� ÿ��NamingHost������ע���Լ���Name����ЩName���ע�����ǵ�NamingHost֮�ϡ�
 * ����NamingHost1ע����NameA��NamingHost2ע����NameB����NamingHost1����ʲô������Ҫ����
 * NameA����һ����Ϣ����ʱ������ϣ����ֻ��Ҫָ��Ŀ����NameB��Ȼ���ͣ�
 * �����Ϣ���Ϳ��Ա�ĳ�ֻ����Զ��͵�NamingHostB�ϵ�NameB���� Ϊ�˴ﵽ������ܣ�����Ҫ֪��һ��Name��ָ����Ŀ�꣬�������ϵ�λ�ã�
 * ���λ�ÿ�����IP��ַ�ȵȣ�������ConnectionCenter�ж����ClientID��ʶ��
 * Ҳ����˵����Ҫ��һ�����ܣ������һ��Name�����͸�����������ĸ�NamingHost
 * ע�������Name�����������NamingHost��ClientID�� NamingServer���Ǹ�����µġ�
 * 
 * ����NamingServer��ι����� 1��NamingServer�б�����NamingTable������һ��ӳ�����¼��һ����Name
 * ӳ�䵽ʲôClientID�ϡ� 2��NamingServer������ӦNamingHost�Ĳ�ѯ��NamingHost���Է��͸�NamingServer��Ϣ
 * NamingQueryMessage��ѯ��һ��Name��Ӧ��ʲôClientID��NamingServerͨ����Ϣ
 * NamingQeryReplyMessage�ظ�������ѯ��NamingHost��
 * 3��NamingServer������ӦNamingHost����ע���Name�ı������һ��NamingHost������Լ�
 * ��ע���Name�󣬻ᷢ����ϢNamingUpdateMessage��֪ͨNamingServer��NamingServer
 * �����յ�����Ϣ�������Լ���NamingTable�����ݸ����Ƿ�ɹ����ظ�
 * NamingUpdateReplyMessage��NamingTable���лع����������������ʧ�ܣ���ع��� ��ʼ����֮ǰ��״̬��
 * ������³ɹ���NamingServer����֪ͨ���Ǹ�������µ�NamingHost���������NamingHost��
 * �����Լ��������Name-Client��Ӧ���Ѿ�ʧЧ��
 * 4��NamingServer���Զ��㲥���Ը�֪����NamingHost�Լ��Ĵ��ڣ������ṩ���ӵ��Լ��� ������һ��NamingHost�������Լ�
 * 5��NamingHost���ܻ�رղ��뿪NamingServer�����NamingServer���Զ����ʧȥ��NamingHost
 * ��Name������ɾ�����ǡ�
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
		// ��ʼ������
		namingTable = new NamingTable();
		hostTable = new HostTable();
		connectionCenter = SocketConnectionCenter.createInstance(true);
		messageProcThread = new MessageProcessingThread();

		// ��ʼ�㲥������ʼ��������
		connectionCenter.startToReceiveConnection();
		connectionCenter.openBroadcastFunction();
		connectionCenter.setBroadcastMessageBufferMaxSize(1); // NamingServer��׼�����չ㲥
		connectionCenter.broadcastMessage(new NamingExistanceMessage(
				connectionCenter.getConnectionReceivingClientID()),
				EXISTANCE_BROADCAST_PERIOD);

		// ���������߳�
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

		// ����namingTable���ظ����������
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

		// ������³ɹ�����������host����NamingInvalidMessage
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
	 * ��һ��NamingHost�˳�������ռ�õ�NameӦ�ñ�ɾ�������������߳� �ɵ��¡�
	 * 
	 * ����߳�ÿ��һ������ɨ��һ�Σ�����һ�η���ĳ��NamingHost�Ѿ��Ͽ�ʱ��
	 * ��������ɾ������namingTable�еļ�¼��������������ɨ�趼����
	 * ĳ��NamingHost�Ѿ��Ͽ���ʱ�򣬲Ż�ɾ������namingTable�еĶ�Ӧ��¼��
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
					// �õ���ЩClientID�Ѿ������Ч��
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

					// ���ҳ�Ӧ�ñ�ɾ����ClientID
					curHosts.clear();
					final Set<ClientID> toBeRemoved = curHosts;

					for (ClientID clientID : possibleExitedClients)
					{
						if (invalidHosts.contains(clientID))
						{
							// ���α���������ЧhostӦ�ñ�ɾ��
							toBeRemoved.add(clientID);
						}
					}

					// ����possibleExitedClients
					invalidHosts.removeAll(toBeRemoved);
					possibleExitedClients = invalidHosts;

					// ʵʩɾ��
					namingTable.removeClients(toBeRemoved);

					// hostTable�д˴�����һ��Bug������hostTableû��ɾ����Ӧ����Ŀ

					Thread.sleep(PERIOD);
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}

	}
}
