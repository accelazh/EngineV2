package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.shared.IConstants;

public class AuthorityCenterOperatingException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;

	public AuthorityCenterOperatingException()
	{
		super();
	}
	
	public AuthorityCenterOperatingException(String message)
	{
		super(message);
	}
	
	public AuthorityCenterOperatingException(Throwable cause)
	{
		super(cause);
	}
	
	public AuthorityCenterOperatingException(String message, Throwable cause)
	{
		super(message, cause);
	}

}
