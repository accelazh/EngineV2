package com.accela.ExceptionCenter;

/**
 * 
 * Ĭ���쳣���������������н�����ӡ���쳣�Ķ�ջ�ټ�
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
