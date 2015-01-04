package com.accela.ConnectionCenter.broadcaster;

import com.accela.ConnectionCenter.shared.IConstants;

public class FailedToBroadcastMessageException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToBroadcastMessageException()
	{
		super();
	}

	public FailedToBroadcastMessageException(String message)
	{
		super(message);
	}

	public FailedToBroadcastMessageException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FailedToBroadcastMessageException(Throwable cause)
	{
		super(cause);
	}

}
