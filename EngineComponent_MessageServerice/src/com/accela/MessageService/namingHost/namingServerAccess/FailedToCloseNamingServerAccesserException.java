package com.accela.MessageService.namingHost.namingServerAccess;

import com.accela.MessageService.shared.IConstants;

public class FailedToCloseNamingServerAccesserException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToCloseNamingServerAccesserException()
	{
		super();
	}

	public FailedToCloseNamingServerAccesserException(String message)
	{
		super(message);
	}

	public FailedToCloseNamingServerAccesserException(String message,
			Throwable cause)
	{
		super(message, cause);
	}

	public FailedToCloseNamingServerAccesserException(Throwable cause)
	{
		super(cause);
	}

}
