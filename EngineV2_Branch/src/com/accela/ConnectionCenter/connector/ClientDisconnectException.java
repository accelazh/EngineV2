package com.accela.ConnectionCenter.connector;

/**
 * 
 * ����쳣��ʾ�����ӵ�Զ�̶˵Ŀͻ��Ͽ����ӡ�
 *
 */
public class ClientDisconnectException extends Exception
{
	private static final long serialVersionUID = com.accela.ConnectionCenter.shared.IConstants.SERIAL_VERSION_UID;

	public ClientDisconnectException()
	{
		super();
	}

	public ClientDisconnectException(String message)
	{
		super(message);
	}

	public ClientDisconnectException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ClientDisconnectException(Throwable cause)
	{
		super(cause);
	}

}
