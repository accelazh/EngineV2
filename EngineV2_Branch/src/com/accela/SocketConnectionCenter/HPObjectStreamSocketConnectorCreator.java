package com.accela.SocketConnectionCenter;

import com.accela.ConnectionCenter.connector.Connector;
import com.accela.ConnectionCenter.connectorCreator.ConnectorCreator;
import com.accela.ConnectionCenter.shared.NewConnectionInfo;

public class HPObjectStreamSocketConnectorCreator extends ConnectorCreator 
{
	@Override
	protected Connector createConnectorImpl(NewConnectionInfo info)
	{
		if(!(info instanceof SocketNewConnectionInfo))
		{
			assert(false);
			throw new IllegalArgumentException("The NewConnectionInfo should be a SocketNewConnectionInfo");
		}
		
		SocketNewConnectionInfo socketInfo=(SocketNewConnectionInfo)info;
		
		return new HPObjectStreamSocketConnector(socketInfo.getSocket());
	}

}
