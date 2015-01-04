package com.accela.ConnectionCenter;

import com.accela.ConnectionCenter.broadcaster.Broadcaster;
import com.accela.ConnectionCenter.connectionLauncher.ConnectionLauncher;
import com.accela.ConnectionCenter.connectionReceiver.ConnectionReceiver;
import com.accela.ConnectionCenter.connectorCreator.ConnectorCreator;

/**
 * 
 * ConnectionCenter�ĳ�ʼ����������ָ������ConnectionCenter�����
 *
 */
public abstract class ConnectionCenterInitializer
{
	/**
	 * ָ��һ��ConnectorCreator��������ΪConnectionCenter�Ļ����������
	 */
	public abstract ConnectorCreator specifyConnectorCreator();
	/**
	 * ͬ��
	 */
	public abstract ConnectionReceiver specifyConnectionReceiver();
	/**
	 * ͬ��
	 */
	public abstract ConnectionLauncher specifyConnectionLauncher();
	/**
	 * ͬ��
	 */
	public abstract Broadcaster specifyBroadcaster();

}
