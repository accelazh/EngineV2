package com.accela.AuthorityCenter.changeManager;

import com.accela.AuthorityCenter.shared.IConstants;

/**
 * 
 * ����AuthorityChangeManager�������׳����쳣��
 * ��ԻȨ�ޱ���г��ֵ��쳣
 *
 */
public class AuthorityChangingException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public AuthorityChangingException()
	{
		super();
	}
	
	public AuthorityChangingException(String message)
	{
		super(message);
	}
	
	public AuthorityChangingException(Throwable cause)
	{
		super(cause);
	}
	
	public AuthorityChangingException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
