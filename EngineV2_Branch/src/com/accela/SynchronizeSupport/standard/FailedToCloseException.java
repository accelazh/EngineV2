package com.accela.SynchronizeSupport.standard;

import com.accela.SynchronizeSupport.shared.IConstants;

/**
 * 
 * ����쳣ӦIOpenClosable�ӿڶ���ƣ���ʾ�ڹرշ������������쳣��
 * ʹ�ùر�ʧ�ܡ�
 *
 */
public class FailedToCloseException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToCloseException()
	{
		super();
	}

	public FailedToCloseException(String message)
	{
		super(message);
	}

	public FailedToCloseException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FailedToCloseException(Throwable cause)
	{
		super(cause);
	}

}
