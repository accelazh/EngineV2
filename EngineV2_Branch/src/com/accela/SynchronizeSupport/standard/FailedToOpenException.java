package com.accela.SynchronizeSupport.standard;

import com.accela.SynchronizeSupport.shared.IConstants;

/**
 * 
 * ����쳣ӦIOpenClosable�ӿڶ���ƣ���ʾ�ڴ򿪷������������쳣��
 * ʹ�ùر�ʧ�ܡ�
 *
 */
public class FailedToOpenException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public FailedToOpenException()
	{
		super();
	}

	public FailedToOpenException(String message)
	{
		super(message);
	}

	public FailedToOpenException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public FailedToOpenException(Throwable cause)
	{
		super(cause);
	}

}
