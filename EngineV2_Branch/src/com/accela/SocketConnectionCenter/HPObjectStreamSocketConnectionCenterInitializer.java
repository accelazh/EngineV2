package com.accela.SocketConnectionCenter;

import com.accela.ConnectionCenter.ConnectionCenterInitializer;
import com.accela.ConnectionCenter.broadcaster.Broadcaster;
import com.accela.ConnectionCenter.connectionLauncher.ConnectionLauncher;
import com.accela.ConnectionCenter.connectionReceiver.ConnectionReceiver;
import com.accela.ConnectionCenter.connectorCreator.ConnectorCreator;

/**
 * 
 * 与SocketConnectionCenter完全相同，除了这个初始化器使用
 * HPObjectStreamSocketConnectorCreator以外
 *
 */
public class HPObjectStreamSocketConnectionCenterInitializer extends ConnectionCenterInitializer
{
	@Override
	public Broadcaster specifyBroadcaster()
	{
		return new HPObjectStreamSocketBroadcaster();
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
		return new HPObjectStreamSocketConnectorCreator();
	}

}
