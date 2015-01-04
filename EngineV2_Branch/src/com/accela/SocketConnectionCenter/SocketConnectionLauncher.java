package com.accela.SocketConnectionCenter;

import java.io.IOException;
import java.net.*;

import com.accela.ConnectionCenter.connectionLauncher.ConnectionLauncher;
import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.ConnectionCenter.shared.NewConnectionInfo;

public class SocketConnectionLauncher extends ConnectionLauncher
{

	@Override
	protected NewConnectionInfo launchConnectionImpl(ClientID clientID) throws IOException
	{
		if(!(clientID instanceof SocketClientID))
		{
			assert(false);
			throw new IllegalArgumentException("The clientID should be a SocketClientID");
		}
		
		SocketClientID socketClientID=(SocketClientID)clientID;
		Socket server=new Socket(socketClientID.getAddress(), socketClientID.getPort());
		return new SocketNewConnectionInfo(server);
	}

}
