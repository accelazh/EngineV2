package com.accela.ConnectionCenter;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.broadcaster.Broadcaster;
import com.accela.ConnectionCenter.broadcaster.FailedToBroadcastMessageException;
import com.accela.ConnectionCenter.connectionLauncher.ConnectionLauncher;
import com.accela.ConnectionCenter.connectionLauncher.FailedToLaunchConnectionException;
import com.accela.ConnectionCenter.connectionReceiver.ConnectionReceiver;
import com.accela.ConnectionCenter.connector.Connector;
import com.accela.ConnectionCenter.connector.FailedToSendMessageException;
import com.accela.ConnectionCenter.connectorCreator.ConnectorCreator;
import com.accela.ConnectionCenter.connectorCreator.FailedToCreateConnectorException;
import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.ConnectionCenter.shared.NewConnectionInfo;
import com.accela.ConnectionCenter.shared.RemotePackage;
import com.accela.SynchronizeSupport.standard.ClosableBlockingQueue;
import com.accela.SynchronizeSupport.standard.FailedToCloseException;
import com.accela.SynchronizeSupport.standard.FailedToOpenException;
import com.accela.SynchronizeSupport.standard.IOpenClosable;
import com.accela.SynchronizeSupport.standard.OpenCloseSupport;
import com.accela.SynchronizeSupport.standard.TargetMethod;

/**
 * ConnectionCenterģ������
 * 
 * �������ġ�����������Ϣ���������ӵȵȡ� ����԰��������Ŀ������󲿷֣�һ���ֹ����ָ��Client�������Լ���Ϣ���͡����ܣ�
 * ��һ���ֹ�����ڹ㲥�ķ��ͺͽ��ա���������֧�ֶ��̡߳�
 * 
 * ǰ�벿�ֵĹ��ܣ� 1���򿪺͹رն�Client�����ӵļ���������򿪣���ô����Client��ͼ���ӵ�����ʱ���ͻὨ����
 * �������ӣ��������ConnectionCenter���������رպ��µ�Client�����ܷ�����������ӡ�
 * 2����������Client�����ӡ������ӳɹ�ʱ���ͻ�����һ�����ӣ�ConnectionCenter����������
 * 3�������Ѿ�������ĳ��Client�����Ӻ���Ϳ�������������Ϣ�ˡ���Ҫʹ��ClientIDָ����˭���� ��Ϣ��
 * 4������Client���㷢�͵���Ϣ�ᱻ�Զ����������������ȡ������ 5������Թرպ�ĳ��Client������
 * 
 * ��벿�ֵĹ��ܣ� 1���򿪺͹رչ㲥���ܡ�����򿪵�ʱ�򣬹㲥��Ϣ�Żᱻ���պʹ洢��ͬʱ��ſ��Է��͹㲥�� 2�������ȡ�����յ��Ĺ㲥��Ϣ
 * 3������Է����㲥
 * 
 * �������ܣ� 1����ConnectionCenter������Ҫ�ȴ�ConnectionCenter��Ȼ����ܹ�ʹ����
 * 2���ر�ConnectionCenter��һ���رգ��������������ܹ�ʹ�ã��߳̽����˳��������������������ConnectionCenter��
 * 
 * һЩע����� 1�������򿪽�����Ϣ�Ĺ��ܣ�������Client���͹�������Ϣ�����ǹ㲥��Ϣ�����ᱻ�洢������㲻������ȡ���������ǽ���Զ����
 * ���ͷţ�Խ��Խ�ࡣ���п�������ڴ������ 2��������Ҫ�̳�ConnectionCenter����ʹ�̳У�Ҳ��Ҫ����µķ�������������ӵķ�������ͬ����
 * ��ػ���
 * 
 * ע�⣬ConnectionCenter����Ƴ�Ϊ�ɿ����ࣨ���嶨���com.accela.synchronizeSupport.
 * standardSupport.OpenCloseSupport��
 * �����̳�����࣬������µķ�����ʱ���������صĿɿ��������Ʊ�׼��ConnectionCenter��ʹ�õ�OpenCloseSupport����
 * �Ѿ�����Ƴ�protected���ͣ��ɹ�ʹ�á�
 * 
 * ConnectionCenter����Ƴɶ����ھ�������缼��������Ҫʹ��һ�־�������缼��
 * ��ʵ�ֺ󣬲���ʹ�á�Ĭ�ϵ�ʹ��Socket������������������SocketConnectionCenter�� �����ֱ��ʹ�á�
 * 
 * //TODO ͬ�����滹����һЩ���⣬�������һ��Ҫ��Connector ����Ϣ�ĺ����ڼ��Connector����һ�����������Connector
 * �ر��ˡ����ܸ����������ͬ���ģ����������潨������ຯ��δ ����ͬ���������쳣���ƿ��Ա�֤���������ͨ���쳣������
 * 
 */
public class ConnectionCenter implements IOpenClosable
{
	/**
	 * ConnectionCenter�����֮һ�����𴴽�Connector
	 */
	private ConnectorCreator connectorCreator;
	/**
	 * ConnectionCenter�����֮һ�����������������
	 */
	private ConnectionReceiver connectionReceiver;
	/**
	 * ConnectionCenter�����֮һ������������
	 */
	private ConnectionLauncher connectionLauncher;
	/**
	 * ConenctionCenter�����֮һ������㲥����
	 */
	private Broadcaster broadcaster;
	/**
	 * �����洢ConnectionCenter�����֮һ����Connector��Connector��������һ������)��
	 * ������ݽṹ�����洢��Connector�������ӵĿͻ���ClientID����Ϊ��
	 */
	private Map<ClientID, Connector> connectors = new ConcurrentHashMap<ClientID, Connector>();
	/**
	 * receivingThread�����̳߳ط���ģ�������������Future���󱣴������
	 * ��ͨ����ӦConnector�����ӵĿͻ���ClientID����Ϊ���������������� Connectorһһ��Ӧ��
	 */
	private Map<ClientID, Future<?>> receivingThreads = new ConcurrentHashMap<ClientID, Future<?>>();
	/**
	 * �洢�͹���receivingThread���̳߳�
	 */
	private static final ExecutorService RECEIVING_THREAD_POOL = Executors
			.newCachedThreadPool();
	/**
	 * ��Connector�յ�����Ϣ����װ��������У����յ�����Ϣ��װ��
	 */
	private ClosableBlockingQueue<RemotePackage> receivingQueue = new ClosableBlockingQueue<RemotePackage>();
	/**
	 * receiveingQueue������С��������ܵ���Ϣ���࣬ ���Զ��Ƴ�������յ���Ϣ
	 */
	private int receivingQueueMaxSize = 150;
	/**
	 * һ�����ϸ���ConnectionReceiver�ṩ����Ϣ�������µ����ӵ��߳�
	 */
	private ConnectionReceiverThread connectionReceiverThread;
	/**
	 * һ�����Ͻ�broadcaster���յ�����Ϣװ��broadcasterReceivingQueue���߳�
	 */
	private BroadcasterReceivingThread broadcasterReceivingThread;
	/**
	 * broadcaster���յ�����Ϣ���ᱻװ���������
	 */
	private ClosableBlockingQueue<RemotePackage> broadcasterReceivingQueue = new ClosableBlockingQueue<RemotePackage>();
	/**
	 * broadcasterReceivingQueue�Ĵ�С���ޣ�������ܵ���Ϣ���࣬ ���Զ��Ƴ�������յ���Ϣ
	 */
	private int broadcasterReceivingQueueMaxSize = 150;
	/**
	 * �����Զ�������رյ�Connector���߳�
	 */
	private ConnectorSweeperThread connectorSweeperThread;
	/**
	 * Ϊ��ʹ���Ӻ�ɾ��Connector�ķ���ͬ������ͬ��������Ҫʹ�������
	 */
	private ReentrantLock addRemoveConnectorLock = new ReentrantLock();
	/**
	 * Ϊ��ʹConnectionCenter����ɿ���������Ҫ�󣬶�ʹ�õ�ͬ������ʵ�ְ���
	 */
	protected OpenCloseSupport ocs;
	/**
	 * �趨��������������ƣ�Ϊ������ʾ��������������Ϊ�˷��������������
	 * ������������ʱ��ֱ��ʹ��connectors.size()��������Щ�Ѿ��Ͽ�����
	 * �ǻ�δ�����������Ҳ�����ˡ�
	 */
	private int maxConnectionNum = -1; // TODO �����������Ĺ��ܻ�û�в���

	/**
	 * ����ConnectionCenterInitializer�½�ConnectionCenter
	 * 
	 * @param initializer
	 */
	public ConnectionCenter(ConnectionCenterInitializer initializer)
	{
		if (null == initializer)
		{
			throw new NullPointerException("initializer should not be null");
		}

		ocs = new OpenCloseSupport(new OpenMethod(), new CloseMethod());
		construct(initializer);

	}

	/**
	 * ��ʼ��������ֻ�ڹ��췽�������
	 * 
	 * @param initializer
	 */
	private void construct(ConnectionCenterInitializer initializer)
	{
		assert (!ocs.isOpen());
		assert (initializer != null);

		ConnectorCreator connectorCreator = initializer
				.specifyConnectorCreator();
		if (null == connectorCreator)
		{
			throw new NullPointerException(
					"connectorCreator should not be null in ConnectionCenterInitializer");
		}
		this.connectorCreator = connectorCreator;

		ConnectionReceiver connectionReceiver = initializer
				.specifyConnectionReceiver();
		if (null == connectionReceiver)
		{
			throw new NullPointerException(
					"connectionReceiver should not be null in ConnectionCenterInitializer");
		}
		this.connectionReceiver = connectionReceiver;

		ConnectionLauncher connectionLauncher = initializer
				.specifyConnectionLauncher();
		if (null == connectionLauncher)
		{
			throw new NullPointerException(
					"connectionLauncher should not be null in ConnectionCenterInitializer");
		}
		this.connectionLauncher = connectionLauncher;

		Broadcaster broadcaster = initializer.specifyBroadcaster();
		if (null == broadcaster)
		{
			throw new NullPointerException(
					"broadcaster should not be null in ConnectionCenterInitializer");
		}
		this.broadcaster = broadcaster;

	}

	// /////////////////////////�����Client���ӵļ���ConnectionReceiver///////////////////////////
	/**
	 * ��ʼ����Client�����ӡ� ������������� �����Client�������ӵ�ConenctioinCenter���ͻᱻ��������Ȼ�� �������ӡ�
	 * 
	 * @throws FailedToOpenException
	 *             ����򿪵Ĺ����������쳣
	 */
	public void startToReceiveConnection() throws FailedToOpenException
	{
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			connectionReceiver.open();

			connectionReceiverThread = new ConnectionReceiverThread(
					connectionReceiver);
			connectionReceiverThread.start();
		} finally
		{
			ocs.unlockMethod();
		}

	}

	/**
	 * ֹͣ����Client�����ӣ������Ѿ��򱾵ط������ӵ���û�н������ӵ�Client ��ֹͣ�󲻻ᱻ������������
	 * 
	 * @throws FailedToCloseException
	 *             ����رյĹ����������쳣
	 */
	public void stopToReceiveConnection() throws FailedToCloseException
	{
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			connectionReceiver.close();
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * @return �Ƿ����ڼ���Client������
	 */
	public boolean isReceivingConnection()
	{
		if (!ocs.isOpen())
		{
			assert (!connectionReceiver.isOpen());
			return false;
		}

		return connectionReceiver.isOpen();
	}

	/**
	 * @return ��������ͻ���Ҫ���ӵ����ConnectionCenter�ϣ�Ӧ�� ͨ������������ص�ClientID��
	 *         ����������ص�ʵ�����Ǽ������ӵ�ģ�飬���ڵ�ClientID
	 */
	public ClientID getConnectionReceivingClientID()
	{
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			return connectionReceiver.getConnectionReceivingClientID();
		} finally
		{
			ocs.unlockMethod();
		}

	}

	// ///////////////////////////��������ConnectionLauncher////////////////////////////

	/**
	 * һ����ͨ��ClientID��ָ����Client�����ӡ������Ӻ�Ϳ��������Client������Ϣ��
	 * 
	 * @return �����ȥ�Ѿ��򿪹�������ӣ���û�йرգ��򷵻�false����Ϊ����������������
	 *         ������ʲô�²�����false�����û�п��õ����ӣ�����������ͻ��½�һ����������true
	 * 
	 * @throws ����򿪹����г����쳣
	 */
	public boolean openConnection(ClientID clientID)
			throws ConnectionFullException, FailedToCreateConnectionException
	{
		if (null == clientID)
		{
			throw new NullPointerException("clientID should not be null");
		}

		addRemoveConnectorLock.lock();
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			if (hasConnectionOf(clientID))
			{
				return false;
			}

			createConnection(connectionLauncher.launchConnection(clientID));
			return true;
		} catch (FailedToCreateConnectorException ex)
		{
			throw new FailedToCreateConnectionException(ex);
		} catch (ConnectionFullException ex)
		{
			throw ex;
		} catch (FailedToLaunchConnectionException ex)
		{
			throw new FailedToCreateConnectionException(ex);
		} catch (FailedToOpenException ex)
		{
			throw new FailedToCreateConnectionException(ex);
		} finally
		{
			ocs.unlockMethod();
			addRemoveConnectorLock.unlock();
		}

	}

	// ////////////////////////////�ر�����////////////////////////////////

	/**
	 * �ر�ͨ��ClientID��ָ����Client�����ӡ���ѻ������е�ʣ�µ���Ϣ������Ϻ��ֹͣ��
	 * ClientIDָ��һ��Զ�̶˵Ŀͻ�������������ͨ��������������������ر�������ӡ�
	 * 
	 * �����ָ�������ӣ�Զ�̶˵Ŀͻ��Ѿ��ر��ˣ���ô����false
	 */
	public boolean closeConnection(ClientID clientID)
			throws FailedToCloseException
	{
		if (null == clientID)
		{
			throw new NullPointerException();
		}

		addRemoveConnectorLock.lock();
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			if (!hasConnectionOf(clientID))
			{
				return false;
			}

			destroyConnection(clientID);
			return true;
		} finally
		{
			ocs.unlockMethod();
			addRemoveConnectorLock.unlock();
		}

	}

	// //////////////////////////////���ͺ�ȡ����ϢConnector///////////////////////////////

	/**
	 * ȡ��ConnectionCenter�յ�����Ϣ�������ʱû����Ϣ���������
	 * ��ConnectionCenter�Ѿ��رպ�����Լ����������������ȡ ��ʣ�����Ϣ����û��ʣ�����Ϣʱ���ٵ�����������ͻ��׳�
	 * AlreadyClosedException��
	 */
	public RemotePackage retriveMessage()
	{
		ocs.lockMethod();

		try
		{
			if (!ocs.isOpen() && !hasMessageToRetrieve()) // TODO
			// �����ڹرպ󣬻����п����ǵ������̶߳�ͨ������ֻ��һ����Ϣ�����Ӷ����һ���߳�����
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
	 * ����retrieveMessage()��ʱ�Ƿ��п���ȡ�ص���Ϣ
	 */
	public boolean hasMessageToRetrieve()
	{
		return !receivingQueue.isEmpty();
	}

	/**
	 * ������Ϣ��RemotePackage�е�ClientID����ָ����ϢӦ�÷��͸��ĸ�Client��
	 * RemotePackage�е�ClientIDָ��Ҫ���͸���Զ�̶ˡ�
	 * 
	 * @throws FailedToSendMessageException
	 */
	public void sendMessage(RemotePackage remotePackage)
			throws FailedToSendMessageException
	{
		if (null == remotePackage)
		{
			throw new NullPointerException("remotePackage should not be null");
		}
		if (null == remotePackage.getClientID())
		{
			throw new NullPointerException(
					"remotePackage.clientID should not be null");
		}
		if (null == remotePackage.getMessage())
		{
			throw new NullPointerException(
					"remotePackage.message should not be null");
		}

		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			sendMessage(remotePackage.getClientID(), remotePackage.getMessage());
			
			RemotePackage.deleteObject(remotePackage);
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * ������Ϣ
	 * 
	 * @param clientID
	 *            ClientIDָ��Ҫ���͸���Զ�̶ˡ�ָ����Ϣ�����͸��ĸ�Client
	 * @param message
	 *            Ҫ���͵���Ϣ
	 */
	public void sendMessage(ClientID clientID, Object message)
			throws FailedToSendMessageException, ConnectionDoesNotExistException
	{
		if (null == clientID)
		{
			throw new NullPointerException("clientID should not be null");
		}
		if (null == message)
		{
			throw new NullPointerException("message should not be null");
		}
		if (!hasConnectionOf(clientID))
		{
			throw new ConnectionDoesNotExistException(
					"this client specified by clientID is not connected");
		}

		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			Connector connector = connectors.get(clientID);
			if (null == connector)
			{
				throw new ConnectionDoesNotExistException(
						"this client specified by clientID is not connected");
			}

			connector.sendMessage(message);
		} finally
		{
			ocs.unlockMethod();
		}
	}

	// /////////////////////////��ѯClientID////////////////////////////

	/**
	 * @return �Ƿ��Ѿ���clientIDָ����Client���������ӣ������������û�б��ر�
	 */
	public boolean hasConnectionOf(ClientID clientID)
	{
		if (null == clientID)
		{
			return false;
		}
		if (!ocs.isOpen())
		{
			assert (!connectors.containsKey(clientID) || !connectors.get(
					clientID).isOpen());
			return false;
		}

		return connectors.containsKey(clientID)
				&& connectors.get(clientID).isOpen();
	}

	/**
	 * @return �����Ѿ����������ӣ�������û�б��رյ�Client��ClientID
	 */
	public List<ClientID> getConnectedClientIDs()
	{
		if (!ocs.isOpen())
		{
			return Collections.unmodifiableList(new LinkedList<ClientID>());
		}

		List<ClientID> list = new LinkedList<ClientID>();

		Set<ClientID> clientIDs = connectors.keySet();
		for (ClientID clientID : clientIDs)
		{
			assert (clientID != null);
			assert (connectors.get(clientID).getClientID() == null || connectors
					.get(clientID).getClientID().equals(clientID));
			if (clientID != null && hasConnectionOf(clientID))
			{
				list.add(clientID);
			}
		}

		return Collections.unmodifiableList(list);
	}

	// ///////////////////////////////����㲥Broadcaster///////////////////////////////////

	/**
	 * �򿪹㲥���ܡ�ֻ�д򿪹㲥���ܺ󣬲��ܹ����չ㲥��Ϣ�ͷ��͹㲥��Ϣ
	 * 
	 * @throws FailedToOpenException
	 */
	public void openBroadcastFunction() throws FailedToOpenException
	{
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			broadcaster.open();
			broadcasterReceivingThread = new BroadcasterReceivingThread(
					broadcaster);
			broadcasterReceivingThread.start();
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * �رչ㲥����
	 * 
	 * @throws FailedToCloseException
	 */
	public void closeBroadcastFunction() throws FailedToCloseException
	{
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			broadcaster.close();
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * @return ���ع㲥�����Ƿ��Ѿ���
	 */
	public boolean isBroadcastFunctionOpen()
	{
		if (!ocs.isOpen())
		{
			assert (!broadcaster.isOpen());
			return false;
		}

		return broadcaster.isOpen();
	}

	/**
	 * ȡ���յ��Ĺ㲥��Ϣ�������ʱû����Ϣ��������� ��ConnectionCenter�Ѿ��رպ�����Լ����������������ȡ
	 * ��ʣ�����Ϣ����û��ʣ�����Ϣʱ���ٵ�����������ͻ��׳� AlreadyClosedException��
	 */
	public RemotePackage retriveBroadcastMessage()
	{
		ocs.lockMethod();

		try
		{
			if (!ocs.isOpen() && !hasBroadcastMessageToRetrieve())
			{
				ocs.ensureOpen();
			}

			return broadcasterReceivingQueue.dequeue();
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * ����retrieveBroadcastMessage()��ʱ�Ƿ��п���ȡ�ص���Ϣ
	 */
	public boolean hasBroadcastMessageToRetrieve()
	{
		return !broadcasterReceivingQueue.isEmpty();
	}

	/**
	 * ��message�㲥һ��
	 * 
	 * @throws FailedToBroadcastMessageException
	 */
	public void broadcastMessage(Object message)
			throws FailedToBroadcastMessageException
	{
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			broadcaster.broadcastMessage(message);
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * ���÷������ܿ췵�أ�Ȼ���ڲ��̻߳�ÿ��periodָ���ĺ��� ʱ�佫message�㲥һ��
	 */
	public void broadcastMessage(Object message, long period)
	{
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			broadcaster.broadcastMessage(message, period);
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * ��ֹ��ǰConnectionCenter�����еĹ㲥
	 */
	public void cancelAllBroadcast()
	{
		ocs.lockMethod();

		try
		{
			ocs.isOpen();

			broadcaster.cancelAllBroadcast();
		} finally
		{
			ocs.unlockMethod();
		}
	}

	// ///////////////////////////////����ConnectionCenter//////////////////////////////////

	/**
	 * ����ConnectionCenter�Ƿ��Ѿ���
	 */
	@Override
	public boolean isOpen()
	{
		return ocs.isOpen();
	}

	/**
	 * ֻ���ȴ�ConnectionCenter�������ʹ����
	 * 
	 * @throws FialedToOpenException
	 */
	@Override
	public void open() throws FailedToOpenException
	{
		ocs.open();
	}

	/**
	 * �ر�ConnectionCenter��ֹͣ������Ϣ��ֹͣ�㲥����մ洢����Ϣ�ͻ������� �ر��������ӣ��ر������ڲ��̡߳�
	 * �ر�ʱ�������������������������̻߳ᱻ�ж��Ա��˳����رպ����з����������ã�����
	 * retriveMessage()������retriveBroadcastMessage()�������������������Լ���ʹ�ã��Ա�
	 * ȡ��ConnectionCenter��ʣ�����Ϣ�����������Ϣȡ���ˣ����ٵ��������������ͻ��׳�
	 * FailedToCloseException�쳣��
	 * 
	 * @throws FailedToCloseException
	 */
	@Override
	public void close() throws FailedToCloseException
	{
		ocs.close();
	}

	private class OpenMethod implements TargetMethod
	{

		@Override
		public void run() throws Exception
		{
			for (Future<?> task : receivingThreads.values())
			{
				assert (task != null);
				if (!task.isDone())
				{
					throw new IllegalStateException(
							"the inner thread has not been exited yet, please wait");
				}
			}
			if (connectionReceiverThread != null)
			{
				if (connectionReceiverThread.isAlive())
				{
					throw new IllegalStateException(
							"the inner thread has not been exited yet, please wait");
				}
			}
			if (broadcasterReceivingThread != null)
			{
				if (broadcasterReceivingThread.isAlive())
				{
					throw new IllegalStateException(
							"the inner thread has not been exited yet, please wait");
				}
			}
			if (connectorSweeperThread != null)
			{
				if (connectorSweeperThread.isAlive())
				{
					throw new IllegalStateException(
							"the inner thread has not been exited yet, please wait");
				}
			}

			connectors.clear();
			receivingThreads.clear();
			receivingQueue.open();
			broadcasterReceivingQueue.open();
			connectorSweeperThread = new ConnectorSweeperThread();
			connectorSweeperThread.start();

		}

	}

	private class CloseMethod implements TargetMethod
	{

		@Override
		public void run() throws Exception
		{
			// �ر����
			if (connectionReceiver.isOpen())
			{
				connectionReceiver.close();
			}
			if (broadcaster.isOpen())
			{
				broadcaster.close();
			}

			// �ر�Connector
			for (ClientID client : connectors.keySet())
			{
				assert (client != null);
				if (client != null)
				{
					uncheckedDestroyConnection(client);
				}
			}

			// ��ջ�����
			connectors.clear();
			receivingQueue.close();
			broadcasterReceivingQueue.close();

		}

	}

	/**
	 * ����ConnectionCenter�����洢�������н��յ�����Ϣ���Ļ����������������
	 * ������յ�����Ϣ���Ų��£�ConnectionCenter���Զ��Ƴ�������յ��İ�
	 */
	public int getMessageBufferMaxSize()
	{
		return receivingQueueMaxSize;
	}

	/**
	 * �趨ConnectionCenter�����洢�������н��յ�����Ϣ���Ļ����������������
	 * ������յ�����Ϣ���Ų��£�ConnectionCenter���Զ��Ƴ�������յ��İ�
	 */
	public void setMessageBufferMaxSize(int messageBufferMaxSize)
	{
		if (messageBufferMaxSize <= 0)
		{
			throw new IllegalArgumentException(
					"messageBufferMaxSize should be positive");
		}

		this.receivingQueueMaxSize = messageBufferMaxSize;
	}

	/**
	 * ����ConnectionCenter�ܹ���������Ľ��յ��Ĺ㲥��Ϣ���������� �����յ�����Ĺ㲥��Ϣ����ʱ��������յİ��ᱻ�Զ��Ƴ�
	 */
	public int getBroadcastMessageBufferMaxSize()
	{
		return broadcasterReceivingQueueMaxSize;
	}

	/**
	 * �趨ConnectionCenter�ܹ���������Ľ��յ��Ĺ㲥��Ϣ���������� �����յ�����Ĺ㲥��Ϣ����ʱ��������յİ��ᱻ�Զ��Ƴ�
	 */
	public void setBroadcastMessageBufferMaxSize(
			int broadcastMessageBufferMaxSize)
	{
		if (broadcastMessageBufferMaxSize <= 0)
		{
			throw new IllegalArgumentException(
					"broadcastMessageBufferMaxSize should be positive");
		}

		this.broadcasterReceivingQueueMaxSize = broadcastMessageBufferMaxSize;
	}

	/**
	 * @return ConnectionCenter����������������
	 */
	public int getMaxConnectionNum()
	{
		return maxConnectionNum;
	}

	/**
	 * �趨������������ƣ������Ѿ������ϵĿͻ������������������ �������߳��� ��maxConnectionNum�趨ΪΪ������ʾ������������
	 */
	public void setMaxConnectionNum(int maxConnectionNum)
	{
		this.maxConnectionNum = maxConnectionNum;
	}

	// ///////////////////////////////////////////////////////////////////

	/**
	 * �½�һ��Connector�Լ�����receivingThread�� �������ǣ�Ȼ�����Ǽ�����Ӧ��������
	 * �����NewConnectionInfo�������ָ�����ӵ�Զ�̶˵�������Ϣ��
	 * 
	 * �����������hasConnectionOf����Ƿ�Ὠ���ظ�������
	 * 
	 * @param info
	 *            �½����Connector���������Ϣ
	 * @return �½���Connector
	 * 
	 */
	private void createConnection(NewConnectionInfo info)
			throws FailedToCreateConnectorException, FailedToOpenException,
			ConnectionFullException
	{
		if (null == info)
		{
			throw new NullPointerException("info should not be null");
		}

		addRemoveConnectorLock.lock();
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			Connector connector = connectorCreator.createConnector(info);
			if (hasConnectionOf(connector.getClientID()))
			{
				throw new IllegalArgumentException(
						"Trying to connect to a connected client: "
								+ connector.getClientID().toString());
			}
			Runnable threadTask = new ReceivingThreadTask(connector);

			connector.open();
			Future<?> future = RECEIVING_THREAD_POOL.submit(threadTask);

			connectors.put(connector.getClientID(), connector);
			receivingThreads.put(connector.getClientID(), future);

			// ����Ƿ��Ѿ��������������������
			// TODO ����Զ������������Ĺ��ܻ�û�в��Թ�
			if (maxConnectionNum >= 0 && connectors.size() > maxConnectionNum)
			{
				try
				{
					uncheckedDestroyConnection(connector.getClientID());
				} catch (FailedToCloseException ex)
				{
					// there is nothing can do here
					System.err
							.println("Connection is full and failed to close the exceeded connection");
					ex.printStackTrace();
				}

				throw new ConnectionFullException(
						"Connection num is exceeding the limit: "
								+ maxConnectionNum);
			}

		} finally
		{
			ocs.unlockMethod();
			addRemoveConnectorLock.unlock();
		}
	}

	/**
	 * �ر�ͨ��ClientID��ָ����Client�����ӡ���ѻ������е�ʣ�µ���Ϣ������Ϻ��ֹͣ��
	 * ClientIDָ��һ��Զ�̶˵Ŀͻ�������������ͨ��������������������ر�������ӡ�
	 * 
	 * ����ͨ��clientID��ָ����Connector�Ƿ��Ѿ��رգ�����������ܹ�ɾ����
	 */
	private void destroyConnection(ClientID clientID)
			throws FailedToCloseException
	{
		if (null == clientID)
		{
			throw new NullPointerException("clientID should not be null");
		}

		addRemoveConnectorLock.lock();
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();

			uncheckedDestroyConnection(clientID);

		} finally
		{
			ocs.unlockMethod();
			addRemoveConnectorLock.unlock();
		}

	}

	private void uncheckedDestroyConnection(ClientID clientID)
			throws FailedToCloseException
	{
		if (null == clientID)
		{
			throw new NullPointerException("clientID should not be null");
		}

		addRemoveConnectorLock.lock();
		try
		{
			Connector connector = connectors.remove(clientID);
			if (null == connector)
			{
				throw new ConnectionDoesNotExistException(
						"Trying to disconnect from a not connected client: "
								+ clientID.toString());
			}

			Future<?> future = receivingThreads.remove(clientID);
			assert (future != null);

			if (connector.isOpen())
			{
				connector.close();
			}

		} finally
		{
			addRemoveConnectorLock.unlock();
		}
	}

	// /////////////////////////////////////////////////////////////////////////////////////

	/**
	 * ���ϰ�Connector���յ�����Ϣ����receivingQueue���̣߳�ÿ��Connector�����Ӧһ����
	 * ����ΪreceivingThread���������ִ�е�����
	 */
	private class ReceivingThreadTask implements Runnable
	{
		private Connector aimConnector;

		public ReceivingThreadTask(Connector aimConnector)
		{
			assert (aimConnector != null);
			if (null == aimConnector)
			{
				throw new NullPointerException(
						"aimConnector should not be null");
			}
			this.aimConnector = aimConnector;
		}

		public void run()
		{
			assert (aimConnector.isOpen());

			while (aimConnector.isOpen() || aimConnector.hasMessageToRetrieve())
			{
				try
				{
					RemotePackage message = aimConnector.retrieveMessage();
					if (null == message)
					{
						assert (false);
						throw new NullPointerException();
					}

					assert (receivingQueueMaxSize >= 1);
					while (receivingQueue.size() > receivingQueueMaxSize - 1)
					{
						RemotePackage.deleteObject(receivingQueue.dequeue());
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
	 * 
	 * ����̼߳��ConnectionCenter�д洢���Ѿ��رյ�Connector����ɾ��֮
	 * 
	 */
	private class ConnectorSweeperThread extends Thread
	{
		private static final long SWEEPING_INTERVAL = 5000;

		public ConnectorSweeperThread()
		{
			super("ConnectionCenter - ConnectorSweeperThread");
		}

		public void run()
		{
			while (ocs.isOpen())
			{
				try
				{
					for (ClientID clientID : connectors.keySet())
					{
						assert (clientID != null);
						assert (connectors.get(clientID).getClientID() == null || connectors
								.get(clientID).getClientID().equals(clientID));
						if (clientID != null
								&& !connectors.get(clientID).isOpen())
						{
							destroyConnection(clientID);
							//System.out
							//		.println("ConnectionCenter - "
							//				+ "ConnectorSweeperThread has removed a connector: "
							//				+ clientID);

						}
					}

					Thread.sleep(SWEEPING_INTERVAL);

				} catch (Exception ex)
				{
					ex.printStackTrace();
				}
			}
		}
	}

	/**
	 * һ�����ϸ���ConnectionReceiver�ṩ����Ϣ�������µ����ӵ��߳�
	 */
	private class ConnectionReceiverThread extends Thread
	{
		private ConnectionReceiver connectionReceiver;

		public ConnectionReceiverThread(ConnectionReceiver connectionReceiver)
		{
			super("ConnectionCenter - ConnectionReceiverThread");

			assert (connectionReceiver != null);
			if (null == connectionReceiver)
			{
				throw new NullPointerException(
						"connectionReceiver should not be null");
			}

			this.connectionReceiver = connectionReceiver;
		}

		public void run()
		{
			while (connectionReceiver.isOpen()
					|| connectionReceiver.hasNewConnectionInfoToRetrieve())
			{
				try
				{
					NewConnectionInfo info = connectionReceiver
							.retriveNewConnectionInfo();
					if (null == info)
					{
						assert (false);
						throw new NullPointerException();
					}
					createConnection(info);
				} catch (Exception ex)
				{
					ex.printStackTrace();

				}

			}
		}

	}

	/**
	 * һ�����Ͻ�broadcaster���յ�����Ϣװ��broadcasterReceivingQueue���߳�
	 */
	private class BroadcasterReceivingThread extends Thread
	{
		private Broadcaster broadcaster;

		public BroadcasterReceivingThread(Broadcaster broadcaster)
		{
			super("ConnectionCenter - BroadcasterReceivingThread");

			assert (broadcaster != null);
			if (null == broadcaster)
			{
				throw new NullPointerException("broadcaster should not be null");
			}

			this.broadcaster = broadcaster;
		}

		public void run()
		{
			while (broadcaster.isOpen() || broadcaster.hasMessageToRetrieve())
			{
				try
				{
					RemotePackage message = broadcaster.retrieveMessage();
					if (null == message)
					{
						assert (false);
						throw new NullPointerException();
					}

					assert (broadcasterReceivingQueueMaxSize >= 1);
					while (broadcasterReceivingQueue.size() > broadcasterReceivingQueueMaxSize - 1)
					{
						RemotePackage.deleteObject(broadcasterReceivingQueue.dequeue());
					}

					broadcasterReceivingQueue.enqueue(message);
				} catch (Exception ex)
				{
					ex.printStackTrace();
				}

			}
		}

	}

	/**
	 * �÷�����ConnectionCenter�ڲ������¶����ļ̳��ߣ� ��ļ̳���Ӧ��ֻ��ȡ�������ģ���������������
	 */
	protected ConnectorCreator getConnectorCreator()
	{
		return connectorCreator;
	}

	/**
	 * �÷�����ConnectionCenter�ڲ������¶����ļ̳��ߣ� �Ա��ȡĳЩ���ԡ� ��ļ̳���Ӧ��ֻ��ȡ�������ģ���������������
	 */
	protected ConnectionReceiver getConnectionReceiver()
	{
		return connectionReceiver;
	}

	/**
	 * �÷�����ConnectionCenter�ڲ������¶����ļ̳��ߣ� �Ա��ȡĳЩ���ԡ� ��ļ̳���Ӧ��ֻ��ȡ�������ģ���������������
	 */
	protected ConnectionLauncher getConnectionLauncher()
	{
		return connectionLauncher;
	}

	/**
	 * �÷�����ConnectionCenter�ڲ������¶����ļ̳��ߣ� �Ա��ȡĳЩ���ԡ� ��ļ̳���Ӧ��ֻ��ȡ�������ģ���������������
	 */
	protected Broadcaster getBroadcaster()
	{
		return broadcaster;
	}

	/**
	 * �÷�����ConnectionCenter�ڲ������¶����ļ̳��ߣ� �Ա��ȡĳЩ���ԡ� ��ļ̳���Ӧ��ֻ��ȡ�������ģ���������������
	 */
	protected Map<ClientID, Connector> getConnectors()
	{
		return Collections.unmodifiableMap(connectors);
	}

}

// WARNING: ע�ⲻӦ�ð��κ��̸߳ĳ��ػ��̣߳���Ϊ����رպ���Ȼ�з���ʣ����Ϣ������
