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
 * 这个测试用例只是对SocketConnectionCenter进行最简单的测试，详尽的测试
 * 需要使用SocketConnectionCenter自带的命令行调试器以及Debug工具中的线 程观察工具等，人工地测试。
 * 
 * 如果你看见Console中打印出大量XXXClosedException、InterruptedException等等，
 * 可以不必担心，这些异常是正常的，一般来说，它们用来是阻塞中的线程能够退出。
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
