package com.accela.MessageService.namingServer;

import com.accela.MessageService.shared.IConstants;

public class FailedToCloseNamingServerException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToCloseNamingServerException()
	{
		super();
	}

	public FailedToCloseNamingServerException(String message)
	{
		super(message);
	}

	public FailedToCloseNamingServerException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FailedToCloseNamingServerException(Throwable cause)
	{
		super(cause);
	}

}
