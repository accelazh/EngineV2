package com.accela.MessageService.namingHost.namingServerAccess;

import com.accela.MessageService.shared.IConstants;

public class FailedToOpenNamingServerAccesserException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToOpenNamingServerAccesserException()
	{
		super();
	}

	public FailedToOpenNamingServerAccesserException(String message)
	{
		super(message);
	}

	public FailedToOpenNamingServerAccesserException(String message,
			Throwable cause)
	{
		super(message, cause);
	}

	public FailedToOpenNamingServerAccesserException(Throwable cause)
	{
		super(cause);
	}

}
