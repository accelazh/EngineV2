package com.accela.ConnectionCenter.connector;

import com.accela.ConnectionCenter.shared.IConstants;

public class FailedToSendMessageException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToSendMessageException() {
        super();
    }

    public FailedToSendMessageException(String message) {
        super(message);
    }

    public FailedToSendMessageException(String message, Throwable cause) {
        super(message, cause);
    }

    public FailedToSendMessageException(Throwable cause) {
        super(cause);
    }

}
