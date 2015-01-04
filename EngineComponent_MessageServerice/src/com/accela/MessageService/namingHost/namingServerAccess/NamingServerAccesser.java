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
		// 连接上NamingServer
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
		// 初始化变量
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

			// 清空connectionCenter中已经存储的收到的包
			while (connectionCenter.hasMessageToRetrieve())
			{
				connectionCenter.retriveMessage();
				assert (false);
			}

			// TODO 加上switchLock语句, 加上对是否打开的检验
			// 向namingServer发送消息
			NamingUpdateMessage message = new NamingUpdateMessage();
			assert (message != null);
			assert (message.getOperationQueue() != null);
			assert (message.getOperationQueue().isEmpty());

			message.getOperationQueue().addAll(operationQueue);
			connectionCenter.sendMessage(namingServerClientID, message);

			// 接收回答消息
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

			// TODO ConnectionCenter的异步发送机制导致发送的消息可能在被释放后才被HPObjectStreams写出
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

			// 清空connectionCenter中已经存储的收到的包
			while (connectionCenter.hasMessageToRetrieve())
			{
				connectionCenter.retriveMessage();
				assert (false);
			}

			// 向NamingServer发送消息
			NamingQueryMessage message = ConvenientObjectPool
					.newNamingQueryMessage();
			assert (message != null);
			assert (message.getQueriedNames() != null);
			assert (message.getQueriedNames().isEmpty());

			message.addQueriedName(name);

			// 接收NamingServer的回答消息
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

			// 接收NamingServer的回答消息
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
