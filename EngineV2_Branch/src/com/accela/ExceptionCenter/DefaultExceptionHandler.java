package com.accela.ExceptionCenter;

/**
 * 
 * 默认异常处理器，处理方法中仅仅打印出异常的堆栈踪迹
 *
 */
public class DefaultExceptionHandler extends ExceptionHandler
{
	public DefaultExceptionHandler()
	{
		super(Exception.class);
	}

	@Override
	protected void handleExceptionImpl(Exception exception)
	{
		exception.printStackTrace();
	}
	
}
