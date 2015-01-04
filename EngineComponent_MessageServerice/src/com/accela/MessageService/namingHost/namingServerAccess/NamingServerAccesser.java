package com.accela.MessageService.namingHost.namingServerAccess;

import java.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.*;
import com.accela.ConnectionCenter.shared.*;
import com.accela.ConnectionCenter.util.SwitchLock;
import com.accela.MessageService.messages.namingHostToNamingServer.*;
import com.accela.MessageService.messages.namingServerToNamingHost.*;
import com.accela.MessageService.shared.*;
import com.accela.SocketConnectionCenter.SocketConnectionCenter;

public class NamingServerAccesser
{
	private ConnectionCenter connectionCenter;

	private ClientID namingServerClientID = null;

	private SwitchLock switchLock = new SwitchLock();

	private ReentrantLock synLock = new ReentrantLock();

	private boolean open = false;

	public boolean isOpen()
	{
		return open;
	}

	public void open() throws InterruptedException,
			FailedToOpenNamingServerAccesserException
	{
		switchLock.lockSwitch();

		boolean shouldRemainOpen = false;
		try
		{
			if (isOpen())
			{
				shouldRemainOpen = true;
				throw new IllegalStateException(
						"NamingServerAccesser is already opened");
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
			throw new FailedToOpenNamingServerAccesserException(ex);
		} finally
		{
			switchLock.unlockSwitch();
		}
	}

	private void openImpl() throws FailedToConnectToNamingServerException,
			InterruptedException
	{
		// ������NamingServer
		connectToNamingServer();
	}

	public void close() throws InterruptedException,
			FailedToCloseNamingServerAccesserException
	{
		switchLock.lockSwitch();

		try
		{
			if (!isOpen())
			{
				throw new IllegalStateException(
						"NamingServerAccesser is already closed");
			}

			open = false;

			closeImpl();
		} catch (Exception ex)
		{
			throw new FailedToCloseNamingServerAccesserException(ex);
		} finally
		{
			switchLock.unlockSwitch();
		}
	}

	private void closeImpl() throws InterruptedException,
			ConnectionCenterAlreadyShutDownException,
			FailedToShutDownConnectionCenterException
	{
		assert (connectionCenter != null);
		connectionCenter.shutDownConnectionCenter();
		namingServerClientID = null;
	}

	private void connectToNamingServer()
			throws FailedToConnectToNamingServerException, InterruptedException
	{
		// ��ʼ������
		connectionCenter = SocketConnectionCenter.createInstance(true);

		try
		{
			connectionCenter.openBroadcastFunction();

			while (true)
			{
				RemotePackage pack = connectionCenter.retriveBroadcastMessage();
				assert (pack != null);
				if (pack.getMessage() instanceof NamingExistanceMessage)
				{
					NamingExistanceMessage message = (NamingExistanceMessage) pack
							.getMessage();
					if (connectionCenter.openConnection(message
							.getHowToConnectMe()))
					{
						namingServerClientID = message.getHowToConnectMe();
					}

					break;
				}
			}

			connectionCenter.closeBroadcastFunction();
		} catch (FailedToOpenBroadcastFunctionException ex)
		{
			ex.printStackTrace();
			assert (false);
			throw new IllegalStateException(
					"this statement should not be reached", ex);
		} catch (ConnectionCenterAlreadyShutDownException ex)
		{
			ex.printStackTrace();
			assert (false);
			throw new IllegalStateException(
					"this statement should not be reached", ex);
		} catch (InterruptedException ex)
		{
			throw ex;
		} catch (FailedToOpenConnectionException ex)
		{
			throw new FailedToConnectToNamingServerException(ex);
		} catch (FailedToCloseBroadcastFunctionException ex)
		{
			ex.printStackTrace();
			assert (false);
			throw new IllegalStateException(
					"this statement should not be reached", ex);
		}

	}

	public int updateNaming(Queue<NamingUpdateOperation> operationQueue)
			throws InterruptedException
	{
		if (null == operationQueue)
		{
			throw new NullPointerException("operationQueue should not be null");
		}

		synLock.lock();
		switchLock.lockMethod();

		try
		{
			if (!isOpen())
			{
				throw new IllegalStateException(
						"NamingServerAccesser is not open");
			}

			// ���connectionCenter���Ѿ��洢���յ��İ�
			while (connectionCenter.hasMessageToRetrieve())
			{
				connectionCenter.retriveMessage();
				assert (false);
			}

			// TODO ����switchLock���, ���϶��Ƿ�򿪵ļ���
			// ��namingServer������Ϣ
			NamingUpdateMessage message = new NamingUpdateMessage();
			assert (message != null);
			assert (message.getOperationQueue() != null);
			assert (message.getOperationQueue().isEmpty());

			message.getOperationQueue().addAll(operationQueue);
			connectionCenter.sendMessage(namingServerClientID, message);

			// ���ջش���Ϣ
			NamingUpdateReplyMessage reply = null;
			while (true)
			{
				RemotePackage pack = connectionCenter.retriveMessage();
				if (pack.getMessage() instanceof NamingUpdateReplyMessage)
				{
					reply = (NamingUpdateReplyMessage) pack.getMessage();
					break;
				} else
				{
					assert (false);
				}
			}
			assert (reply != null);

			int result = 0;
			if (!reply.isSucc())
			{
				result = reply.getFailedOerationIndex();
			} else
			{
				result = -1;
			}

			// TODO ConnectionCenter���첽���ͻ��Ƶ��·��͵���Ϣ�����ڱ��ͷź�ű�HPObjectStreamsд��
			return result;

		} catch (ClientNotConnectedException ex)
		{
			ex.printStackTrace();
			assert (false);
			throw new IllegalStateException(
					"this statement should not be reached", ex);
		} catch (ConnectionCenterAlreadyShutDownException ex)
		{
			ex.printStackTrace();
			assert (false);
			throw new IllegalStateException(
					"this statement should not be reached", ex);
		} catch (InterruptedException ex)
		{
			throw ex;
		} finally
		{
			switchLock.unlockMethod();
			synLock.unlock();
		}
	}

	public ClientID queryNaming(Name name) throws InterruptedException
	{
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}

		synLock.lock();
		switchLock.lockMethod();

		try
		{
			if (!isOpen())
			{
				throw new IllegalStateException(
						"NamingServerAccesser is not open");
			}

			// ���connectionCenter���Ѿ��洢���յ��İ�
			while (connectionCenter.hasMessageToRetrieve())
			{
				connectionCenter.retriveMessage();
				assert (false);
			}

			// ��NamingServer������Ϣ
			NamingQueryMessage message = ConvenientObjectPool
					.newNamingQueryMessage();
			assert (message != null);
			assert (message.getQueriedNames() != null);
			assert (message.getQueriedNames().isEmpty());

			message.addQueriedName(name);

			// ����NamingServer�Ļش���Ϣ
			NamingQueryReplyMessage reply = null;
			while (true)
			{
				RemotePackage pack = connectionCenter.retriveMessage();
				if (pack.getMessage() instanceof NamingQueryReplyMessage)
				{
					reply = (NamingQueryReplyMessage) pack.getMessage();
					break;
				} else
				{
					assert (false);
				}
			}
			assert (reply != null);
			assert (reply.getQueriedClientIDs() != null);
			assert (reply.getQueriedClientIDs().size() == 1);

			ClientID result = null;
			if (reply.getQueriedClientIDs().size() > 0)
			{
				for (ClientID element : reply.getQueriedClientIDs())
				{
					if (element != null)
					{
						result = element;
						break;
					}
				}
			} else
			{
				result = null;
				assert (false);
			}

			ConvenientObjectPool.deleteNamingQueryMessage(message);
			ConvenientObjectPool.deleteNamingQueryReplyMessage(reply);

			return result;

		} catch (InterruptedException ex)
		{
			throw ex;
		} catch (ConnectionCenterAlreadyShutDownException ex)
		{
			ex.printStackTrace();
			assert (false);
			throw new IllegalStateException(
					"this statement should not be reached", ex);
		} finally
		{
			switchLock.unlockMethod();
			synLock.unlock();
		}

	}

	public boolean registerNamingHost(ClientID clientID) throws InterruptedException
	{
		if (null == clientID)
		{
			throw new NullPointerException("clientID should not be null");
		}
		
		synLock.lock();
		switchLock.lockMethod();

		try
		{
			NamingHostRegisterMessage message = new NamingHostRegisterMessage(
					clientID);
			connectionCenter.sendMessage(namingServerClientID, message);

			// ����NamingServer�Ļش���Ϣ
			NamingHostRegisterReplyMessage reply = null;
			while (true)
			{
				RemotePackage pack = connectionCenter.retriveMessage();
				if (pack.getMessage() instanceof NamingHostRegisterReplyMessage)
				{
					reply = (NamingHostRegisterReplyMessage) pack.getMessage();
					break;
				} else
				{
					assert (false);
				}
			}
			assert (reply != null);

			return reply.isSucc();
		} catch (ClientNotConnectedException ex)
		{
			ex.printStackTrace();
			assert (false);
			throw new IllegalStateException(
					"this statement should not be reached", ex);
		} catch (ConnectionCenterAlreadyShutDownException ex)
		{
			ex.printStackTrace();
			assert (false);
			throw new IllegalStateException(
					"this statement should not be reached", ex);
		} catch (InterruptedException ex)
		{
			throw ex;
		}
		finally
		{
			switchLock.unlockMethod();
			synLock.unlock();
		}

	}

}
