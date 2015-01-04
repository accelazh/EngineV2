package com.accela.TestCases.exceptionCenter;

class TestingException extends Exception
{
	private static final long serialVersionUID = 1L;

	private boolean handled=false;
	
	@Override
	public void printStackTrace()
	{
		handled=true;
	}

	public boolean isHandled()
	{
		return handled;
	}

	public void clearHandled()
	{
		this.handled = false;
	}
	
	
	
	
	
}
