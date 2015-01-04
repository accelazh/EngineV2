package com.accela.ConnectionCenter;

import com.accela.ConnectionCenter.shared.IConstants;

public class ConnectionDoesNotExistException extends IllegalArgumentException
{
private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;
	
	public ConnectionDoesNotExistException()
	{
		super();
	}

	public ConnectionDoesNotExistException(String message)
	{
		super(message);
	}

	public ConnectionDoesNotExistException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ConnectionDoesNotExistException(Throwable cause)
	{
		super(cause);
	}
}
