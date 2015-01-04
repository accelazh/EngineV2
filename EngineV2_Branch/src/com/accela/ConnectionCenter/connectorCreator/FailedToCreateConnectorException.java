package com.accela.ConnectionCenter.connectorCreator;

import com.accela.ConnectionCenter.shared.IConstants;

public class FailedToCreateConnectorException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToCreateConnectorException()
	{
		super();
	}

	public FailedToCreateConnectorException(String message)
	{
		super(message);
	}

	public FailedToCreateConnectorException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FailedToCreateConnectorException(Throwable cause)
	{
		super(cause);
	}

}
