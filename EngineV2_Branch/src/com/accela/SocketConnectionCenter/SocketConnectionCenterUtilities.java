package com.accela.SocketConnectionCenter;

import java.net.Socket;

public class SocketConnectionCenterUtilities
{
	/**
	 * ���socket�Ƿ��Ѿ���
	 */
	public static boolean checkOpened(Socket socket)
	{
		if(null==socket)
		{
			return false;
		}
		
		return (socket.isBound()
				&&!socket.isClosed()
				&&socket.isConnected()
				&&!socket.isInputShutdown()
				&&!socket.isOutputShutdown());
	}

}
