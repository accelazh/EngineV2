package com.accela.TestCases.exceptionCenter;

import com.accela.ExceptionCenter.ExceptionHandler;

public class ExceptionHandlerAA extends ExceptionHandler
{
	private boolean invoked=false;
	private int invokeCount=0;
	
	public ExceptionHandlerAA()
	{
		super(ExceptionAA.class);
	}

	@Override
	protected void handleExceptionImpl(Exception exception)
	{
		invoked=true;
		invokeCount++;
	}
	
	public boolean isInvoked()
	{
		return invoked;
	}

	public void clearInvokation()
	{
		this.invoked = false;
		this.invokeCount=0;
	}

	public int getInvokeCount()
	{
		return invokeCount;
	}

}
