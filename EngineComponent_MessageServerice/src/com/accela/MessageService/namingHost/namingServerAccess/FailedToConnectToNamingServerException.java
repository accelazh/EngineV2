package com.accela.MessageService.namingHost.namingServerAccess;

import com.accela.MessageService.shared.IConstants;

public class FailedToConnectToNamingServerException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToConnectToNamingServerException()
	{
		super();
	}

	public FailedToConnectToNamingServerException(String message)
	{
		super(message);
	}

	public FailedToConnectToNamingServerException(String message,
			Throwable cause)
	{
		super(message, cause);
	}

	public FailedToConnectToNamingServerException(Throwable cause)
	{
		super(cause);
	}

}
