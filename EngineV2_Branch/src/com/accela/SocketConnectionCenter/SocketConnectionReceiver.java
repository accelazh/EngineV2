package com.accela.SocketConnectionCenter;

import java.io.IOException;
import java.net.*;

import com.accela.ConnectionCenter.connectionReceiver.ConnectionReceiver;
import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.ConnectionCenter.shared.NewConnectionInfo;

public class SocketConnectionReceiver extends ConnectionReceiver
{
	private ServerSocket serverSocket;

	@Override
	protected void closeImpl() throws IOException
	{
		serverSocket.close();
		serverSocket = null;
	}

	@Override
	protected NewConnectionInfo detectingConnection() throws IOException
	{
		Socket client = serverSocket.accept();
		return new SocketNewConnectionInfo(client);
	}

	@Override
	protected void openImpl() throws IOException
	{
		serverSocket = new ServerSocket(getIdlePort());
	}

	public int getSocketServerPort()
	{
		ocs.lockMethod();

		try
		{
			ocs.ensureOpen();
			return serverSocket.getLocalPort();
		} finally
		{
			ocs.unlockMethod();
		}
	}

	private int getIdlePort() throws IOException
	{
		ServerSocket s = new ServerSocket(0);
		int port = s.getLocalPort();
		s.close();
		return port;
	}

	@Override
	protected ClientID getConnectionReceivingClientIDImpl()
	{
		try
		{
			return new SocketClientID(InetAddress.getLocalHost(),
					getSocketServerPort());
		} catch (UnknownHostException ex)
		{
			ex.printStackTrace();
			assert (false);
			throw new IllegalStateException(
					"The UnknowHostException should not occured.", ex);
		}
	}

}
