package com.accela.SynchronizeSupport.standard;

import com.accela.SynchronizeSupport.shared.IConstants;

/**
*
* ����쳣��һ������쳣��
* ����ʾ�����״̬�����⡣�������ڴ���һ�����������ͼ����һ�Σ�
* �ͻ��׳�����쳣
*
*/
public class AlreadyOpenedException extends IllegalStateException
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public AlreadyOpenedException()
	{
		super();
	}

	public AlreadyOpenedException(String message)
	{
		super(message);
	}

	public AlreadyOpenedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public AlreadyOpenedException(Throwable cause)
	{
		super(cause);
	}

}
