package com.accela.AuthorityCenter.authorityBase;

import com.accela.AuthorityCenter.shared.IConstants;

/**
 * 
 * ����AuthorityBase�еĲ���������쳣
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
