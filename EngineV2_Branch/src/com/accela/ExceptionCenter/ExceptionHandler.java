package com.accela.ExceptionCenter;

/**
 * 
 * 异常处理器。
 * 异常处理器用来处理指定类型的异常。你可以用它来定制
 * 个性化的异常处理方法。
 */
public abstract class ExceptionHandler
{
	/**
	 * 记录这个异常处理器处理何种类型的异常。
	 */
	private Class<? extends Exception> exceptionClass;
	
	/**
	 * @param exceptionClass 指定ExceptionHandler所要处理的异常类型。当
	 * 一个异常的类型正是exceptionClass所指定的类的时候，或者这个异常是
	 * exceptionClass所指定的类的子类的时候，这个异常就能够被这个异常处理
	 * 器处理。
	 */
	public ExceptionHandler(Class<? extends Exception> exceptionClass)
	{
		if(null==exceptionClass)
		{
			throw new NullPointerException("exceptionClass should not be null");
		}
		this.exceptionClass=exceptionClass;
	}
	
	/**
	 * 检查这个异常处理其能否处理传入的异常exception能被处理当且仅当：
	 * 1、exception!=null
	 * 2、exceptionClass.isInstance(exception)==true
	 */
    public boolean isHandlerOf(Exception exception)
	{
    	if(null==exception)
    	{
    		return false;
    	}
    	
    	return exceptionClass.isInstance(exception);
	}
    
    /**
     * 异常处理器处理异常的方法
     */
    public synchronized void handleException(Exception exception)
    {
    	if(null==exception)
    	{
    		throw new NullPointerException("exception should not be null");
    	}
    	if(!isHandlerOf(exception))
    	{
    		throw new IllegalArgumentException("exception can not be handled by this ExceptionHandler");
    	}
    	
    	handleExceptionImpl(exception);
    }
    
    /**
     * 异常处理器处理异常的方法的实现
     */
    protected abstract void handleExceptionImpl(Exception exception);
    
}
