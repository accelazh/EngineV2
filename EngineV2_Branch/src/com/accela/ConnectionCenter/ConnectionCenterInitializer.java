package com.accela.ConnectionCenter;

import com.accela.ConnectionCenter.broadcaster.Broadcaster;
import com.accela.ConnectionCenter.connectionLauncher.ConnectionLauncher;
import com.accela.ConnectionCenter.connectionReceiver.ConnectionReceiver;
import com.accela.ConnectionCenter.connectorCreator.ConnectorCreator;

/**
 * 
 * ConnectionCenter的初始化器，用来指定各个ConnectionCenter的组件
 *
 */
public abstract class ConnectionCenterInitializer
{
	/**
	 * 指定一个ConnectorCreator对象，它作为ConnectionCenter的基本功能组件
	 */
	public abstract ConnectorCreator specifyConnectorCreator();
	/**
	 * 同上
	 */
	public abstract ConnectionReceiver specifyConnectionReceiver();
	/**
	 * 同上
	 */
	public abstract ConnectionLauncher specifyConnectionLauncher();
	/**
	 * 同上
	 */
	public abstract Broadcaster specifyBroadcaster();

}
