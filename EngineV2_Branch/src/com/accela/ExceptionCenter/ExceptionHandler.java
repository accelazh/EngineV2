package com.accela.ExceptionCenter;

/**
 * 
 * �쳣��������
 * �쳣��������������ָ�����͵��쳣�����������������
 * ���Ի����쳣��������
 */
public abstract class ExceptionHandler
{
	/**
	 * ��¼����쳣����������������͵��쳣��
	 */
	private Class<? extends Exception> exceptionClass;
	
	/**
	 * @param exceptionClass ָ��ExceptionHandler��Ҫ������쳣���͡���
	 * һ���쳣����������exceptionClass��ָ�������ʱ�򣬻�������쳣��
	 * exceptionClass��ָ������������ʱ������쳣���ܹ�������쳣����
	 * ������
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
	 * �������쳣�������ܷ�������쳣exception�ܱ������ҽ�����
	 * 1��exception!=null
	 * 2��exceptionClass.isInstance(exception)==true
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
     * �쳣�����������쳣�ķ���
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
     * �쳣�����������쳣�ķ�����ʵ��
     */
    protected abstract void handleExceptionImpl(Exception exception);
    
}
