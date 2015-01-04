package com.accela.MessageService.namingHost;

import com.accela.MessageService.shared.IConstants;

public class FailedToOpenNamingHostException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToOpenNamingHostException()
	{
		super();
	}

	public FailedToOpenNamingHostException(String message)
	{
		super(message);
	}

	public FailedToOpenNamingHostException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FailedToOpenNamingHostException(Throwable cause)
	{
		super(cause);
	}

}
