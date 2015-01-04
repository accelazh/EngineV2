package com.accela.ConnectionCenter.connectionLauncher;

import com.accela.ConnectionCenter.shared.IConstants;

public class FailedToLaunchConnectionException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToLaunchConnectionException()
	{
		super();
	}

	public FailedToLaunchConnectionException(String message)
	{
		super(message);
	}

	public FailedToLaunchConnectionException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FailedToLaunchConnectionException(Throwable cause)
	{
		super(cause);
	}

}
