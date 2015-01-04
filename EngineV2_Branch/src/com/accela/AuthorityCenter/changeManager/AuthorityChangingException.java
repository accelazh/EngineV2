package com.accela.AuthorityCenter.changeManager;

import com.accela.AuthorityCenter.shared.IConstants;

/**
 * 
 * 描述AuthorityChangeManager工作中抛出的异常，
 * 或曰权限变更中出现的异常
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
