package com.accela.ConnectionCenter;

import com.accela.ConnectionCenter.shared.IConstants;

public class ConnectionFullException extends FailedToCreateConnectionException
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;
	
	public ConnectionFullException()
	{
		super();
	}

	public ConnectionFullException(String message)
	{
		super(message);
	}

	public ConnectionFullException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ConnectionFullException(Throwable cause)
	{
		super(cause);
	}
	
}
