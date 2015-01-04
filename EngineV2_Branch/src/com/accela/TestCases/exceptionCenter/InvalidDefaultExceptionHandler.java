package com.accela.TestCases.exceptionCenter;

import com.accela.ExceptionCenter.ExceptionHandler;

public class InvalidDefaultExceptionHandler extends ExceptionHandler
{
	private boolean invoked=false;

	public InvalidDefaultExceptionHandler()
	{
		super(NullPointerException.class);
	}

	@Override
	protected void handleExceptionImpl(Exception exception)
	{
		invoked=true;
	}

	public boolean isInvoked()
	{
		return invoked;
	}
}
