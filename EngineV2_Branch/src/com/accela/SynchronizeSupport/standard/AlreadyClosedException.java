package com.accela.SynchronizeSupport.standard;

import com.accela.SynchronizeSupport.shared.IConstants;

/**
 *
 * ����쳣��һ������쳣��
 * ����ʾ�����״̬�����⡣�������ڹر���һ�����������ͼ�ر���һ�Σ�
 * �ͻ��׳�����쳣
 *
 */
public class AlreadyClosedException extends IllegalStateException
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public AlreadyClosedException()
	{
		super();
	}

	public AlreadyClosedException(String message)
	{
		super(message);
	}

	public AlreadyClosedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AlreadyClosedException(Throwable cause)
	{
		super(cause);
	}

}
