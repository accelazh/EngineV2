package com.accela.MessageService.namingServer;

import com.accela.MessageService.shared.IConstants;

public class FailedToOpenNamingServerException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToOpenNamingServerException()
	{
		super();
	}

	public FailedToOpenNamingServerException(String message)
	{
		super(message);
	}

	public FailedToOpenNamingServerException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FailedToOpenNamingServerException(Throwable cause)
	{
		super(cause);
	}
}
