package com.accela.AuthorityCenter.authorityBase;

import com.accela.AuthorityCenter.shared.IConstants;

/**
 * 
 * 描述AuthorityBase中的操作引起的异常
 *
 */
public class AuthorityBaseOperatingException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;
	
	public AuthorityBaseOperatingException()
	{
		super();
	}
	
	public AuthorityBaseOperatingException(String message)
	{
		super(message);
	}
	
	public AuthorityBaseOperatingException(Throwable cause)
	{
		super(cause);
	}
	
	public AuthorityBaseOperatingException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
