package com.accela.MessageService.namingHost;

import com.accela.MessageService.shared.IConstants;

public class FailedToCloseNamingHostException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToCloseNamingHostException()
	{
		super();
	}

	public FailedToCloseNamingHostException(String message)
	{
		super(message);
	}

	public FailedToCloseNamingHostException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FailedToCloseNamingHostException(Throwable cause)
	{
		super(cause);
	}

}
