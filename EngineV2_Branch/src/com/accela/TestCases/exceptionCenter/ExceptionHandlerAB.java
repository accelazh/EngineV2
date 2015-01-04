package com.accela.TestCases.exceptionCenter;

public class ExceptionHandlerAB extends com.accela.ExceptionCenter.ExceptionHandler
{
	private boolean invoked=false;
	private int invokeCount=0;
	
	public ExceptionHandlerAB()
	{
		super(ExceptionAB.class);
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
