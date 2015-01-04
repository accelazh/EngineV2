package com.accela.SocketConnectionCenter;

import com.accela.ConnectionCenter.ConnectionCenterInitializer;
import com.accela.ConnectionCenter.broadcaster.Broadcaster;
import com.accela.ConnectionCenter.connectionLauncher.ConnectionLauncher;
import com.accela.ConnectionCenter.connectionReceiver.ConnectionReceiver;
import com.accela.ConnectionCenter.connectorCreator.ConnectorCreator;

public class SocketConnectionCenterInitializer extends ConnectionCenterInitializer
{

	@Override
	public Broadcaster specifyBroadcaster()
	{
		return new SocketBroadcaster();
	}

	@Override
	public ConnectionLauncher specifyConnectionLauncher()
	{
		return new SocketConnectionLauncher();
	}

	@Override
	public ConnectionReceiver specifyConnectionReceiver()
	{
		return new SocketConnectionReceiver();
	}

	@Override
	public ConnectorCreator specifyConnectorCreator()
	{
		return new SocketConnectorCreator();
	}

}
