package com.accela.SocketConnectionCenter;

import java.net.Socket;

import com.accela.ConnectionCenter.shared.NewConnectionInfo;

public class SocketNewConnectionInfo extends NewConnectionInfo
{
	private Socket socket;
	public SocketNewConnectionInfo(Socket socket)
	{
		if(null==socket)
		{
			throw new NullPointerException();
		}
		
		this.socket=socket;
	}
	
	public Socket getSocket()
	{
		return socket;
	}
	
}
