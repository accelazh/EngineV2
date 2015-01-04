package com.accela.TestCases.socketConnectionCenter;

import java.net.InetAddress;

import com.accela.ConnectionCenter.shared.RemotePackage;
import com.accela.SocketConnectionCenter.SocketClientID;
import com.accela.SocketConnectionCenter.SocketConnectionCenter;
import com.accela.SynchronizeSupport.standard.AlreadyClosedException;
import com.accela.SynchronizeSupport.standard.AlreadyOpenedException;

import junit.framework.TestCase;

/**
 * 
 * �����������ֻ�Ƕ�SocketConnectionCenter������򵥵Ĳ��ԣ��꾡�Ĳ���
 * ��Ҫʹ��SocketConnectionCenter�Դ��������е������Լ�Debug�����е��� �̹۲칤�ߵȣ��˹��ز��ԡ�
 * 
 * ����㿴��Console�д�ӡ������XXXClosedException��InterruptedException�ȵȣ�
 * ���Բ��ص��ģ���Щ�쳣�������ģ�һ����˵�����������������е��߳��ܹ��˳���
 */
public class TestingSocketConnectionCenter extends TestCase
{
	private SocketConnectionCenter scc;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		scc = SocketConnectionCenter.createInstance();
		scc.open();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();

		try
		{
			scc.close();
		} catch (Exception ex)
		{
			assert (false);
		}

	}

	public void testSocketConnectionCenter()
	{
		try
		{
			//test 0: test some trivial methods
			boolean hasException = false;
			try
			{
				scc.getConnectionReceivingPort();
			} catch (AlreadyClosedException ex)
			{
				hasException = true;
			}
			assert (hasException);
			
			// test 1: testing start/stop to receive connections
			scc.startToReceiveConnection();

			hasException = false;
			try
			{
				scc.startToReceiveConnection();
			} catch (AlreadyOpenedException ex)
			{
				hasException = true;
			}
			assert (hasException);

			scc.stopToReceiveConnection();

			hasException = false;
			try
			{
				scc.stopToReceiveConnection();
			} catch (AlreadyClosedException ex)
			{
				hasException = true;
			}
			assert (hasException);

			// test 2: test open/close broadcast function
			scc.openBroadcastFunction();
			hasException = false;
			try
			{
				scc.openBroadcastFunction();
			} catch (AlreadyOpenedException ex)
			{
				hasException = true;
			}
			assert (hasException);

			scc.closeBroadcastFunction();
			hasException = false;
			try
			{
				scc.closeBroadcastFunction();
			} catch (AlreadyClosedException ex)
			{
				hasException = true;
			}
			assert (hasException);

			// test 3: establish connections and remove connections
			scc.startToReceiveConnection();
			scc.openConnection(new SocketClientID(InetAddress.getLocalHost(),
					scc.getConnectionReceivingPort()));
			scc.sendMessage(new SocketClientID(InetAddress.getLocalHost(), scc
					.getConnectionReceivingPort()), "hello");
			RemotePackage p = scc.retriveMessage();
			assert (p.getMessage().equals("hello"));
			assert (!p.getClientID().equals(
					new SocketClientID(InetAddress.getLocalHost(), scc
							.getConnectionReceivingPort())));
			
			scc.sendMessage(RemotePackage.newObject(new SocketClientID(InetAddress.getLocalHost(), scc
					.getConnectionReceivingPort()), "hello2"));
			p = scc.retriveMessage();
			assert (p.getMessage().equals("hello2"));
			assert (!p.getClientID().equals(
					new SocketClientID(InetAddress.getLocalHost(), scc
							.getConnectionReceivingPort())));
			
			scc.hasConnectionOf(new SocketClientID(InetAddress.getLocalHost(), scc
					.getConnectionReceivingPort()));
			
			assert(scc.getConnectedClientIDs().size()==2);
			
			scc.closeConnection(new SocketClientID(InetAddress.getLocalHost(),
					scc.getConnectionReceivingPort()));
			
			assert(scc.getConnectedClientIDs().size()<2);
			
			// test4: broadcast
			scc.openBroadcastFunction();
			scc.broadcastMessage("good morning!");
			p = scc.retriveBroadcastMessage();
			assert (p.getMessage().equals("good morning!"));
			assert (p.getClientID().equals(new SocketClientID(InetAddress
					.getLocalHost(), scc.getBroadcastPort())));
			scc.broadcastMessage("good afternoon", 20);
			for (int i = 0; i < 100; i++)
			{
				p = scc.retriveBroadcastMessage();
				assert (p.getMessage().equals("good afternoon"));
				assert (p.getClientID().equals(new SocketClientID(InetAddress
						.getLocalHost(), scc.getBroadcastPort())));
			}

			scc.cancelAllBroadcast();
			scc.broadcastMessage("good afternoon, bye bye!", 2);
			scc.broadcastMessage("good morning, bye bye!", 2);
			scc.broadcastMessage("good evening, bye bye!", 2);
			
			scc.cancelAllBroadcast();
			scc.cancelAllBroadcast();
			scc.broadcastMessage("good afternoon, bye bye!", 2);
			scc.broadcastMessage("good morning, bye bye!", 2);
			scc.broadcastMessage("good evening, bye bye!", 2);
			
			//test trivial methods
			assert(scc.isReceivingConnection());
			assert(scc.isOpen());
			assert(scc.isBroadcastFunctionOpen());
			
			hasException=false;
			try
			{
				scc.setBroadcastPort(12345);
			}
			catch(AlreadyOpenedException ex)
			{
				hasException=true;
			}
			assert(hasException);
			
			hasException=false;
			try
			{
				scc.setBroadcastGroupAddress("230.0.0.1");
			}
			catch(AlreadyOpenedException ex)
			{
				hasException=true;
			}
			assert(hasException);
			
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}
	
	public void testBroadcastSetters()
	{
		try
		{
			scc.setBroadcastPort(12345);
			scc.setBroadcastGroupAddress("230.0.0.17");

			scc.openBroadcastFunction();
			scc.broadcastMessage("good morning!");
			RemotePackage p = scc.retriveBroadcastMessage();
			assert (p.getMessage().equals("good morning!"));
			assert (p.getClientID().equals(new SocketClientID(InetAddress
					.getLocalHost(), scc.getBroadcastPort())));
			scc.broadcastMessage("good afternoon", 20);
			for (int i = 0; i < 100; i++)
			{
				p = scc.retriveBroadcastMessage();
				assert (p.getMessage().equals("good afternoon"));
				assert (p.getClientID().equals(new SocketClientID(InetAddress
						.getLocalHost(), scc.getBroadcastPort())));
			}

			scc.cancelAllBroadcast();
			scc.broadcastMessage("good afternoon, bye bye!", 2);
			scc.broadcastMessage("good morning, bye bye!", 2);
			scc.broadcastMessage("good evening, bye bye!", 2);

			assert (scc.getBroadcastGroupAddress().equals(InetAddress
					.getByName("230.0.0.17")));
			assert (scc.getBroadcastPort() == 12345);
			
			scc.closeBroadcastFunction();
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

}
