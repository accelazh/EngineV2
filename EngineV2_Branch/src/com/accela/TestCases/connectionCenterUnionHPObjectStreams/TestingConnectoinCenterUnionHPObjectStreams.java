package com.accela.TestCases.connectionCenterUnionHPObjectStreams;

import java.net.InetAddress;

import com.accela.ConnectionCenter.connector.FailedToSendMessageException;
import com.accela.SocketConnectionCenter.SocketClientID;
import com.accela.SocketConnectionCenter.SocketConnectionCenter;

import junit.framework.TestCase;

public class TestingConnectoinCenterUnionHPObjectStreams extends TestCase
{
	private SocketConnectionCenter scc1;

	private SocketConnectionCenter scc2;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		scc1 = SocketConnectionCenter.createInstance(true);
		scc2 = SocketConnectionCenter.createInstance(true);
		scc1.open();
		scc2.open();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		scc1.close();
		scc2.close();
	}

	public void testPerformance()
	{
		try
		{
			scc1.startToReceiveConnection();
			scc2.openConnection(new SocketClientID(InetAddress.getLocalHost(),
					scc1.getConnectionReceivingPort()));
			scc2.startToReceiveConnection();
			scc1.openConnection(new SocketClientID(InetAddress.getLocalHost(),
					scc2.getConnectionReceivingPort()));

			for (int i = 0; i < 100; i++)
			{
				System.out.println("testing i = "+i);
				sendAndReceive(new SocketClientID(InetAddress.getLocalHost(),
						scc1.getConnectionReceivingPort()), 
							   new SocketClientID(InetAddress.getLocalHost(), 
						scc2.getConnectionReceivingPort()));
			}

		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	private void sendAndReceive(SocketClientID c1, SocketClientID c2) throws FailedToSendMessageException
	{
		scc1.sendMessage(c2, "Good");
		scc2.retriveMessage();

		scc2.sendMessage(c1, "Bad");
		scc1.retriveMessage();
	}

}
