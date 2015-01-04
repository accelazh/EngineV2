package com.accela.ExceptionCenter;

import java.util.*;

/**
 * ExecptionCenterģ������
 * 
 * �쳣�������ģ�֧�ֶ��̣߳���
 * �쳣���������д洢�ж���쳣�������������һ���쳣����
 * ���͵��쳣�������ĵ�ʱ���쳣�������Ļ�Ѱ��ƥ����쳣
 * ����������������
 *
 */
public class ExceptionCenter implements IExceptionPoster, IExceptionCenterConfigurer
{
	/**
	 * ��ͨ�쳣���������������쳣�����͵��쳣�������ĺ�
	 * �ͻ�Ѱ��ƥ����쳣������������
	 */
	private List<ExceptionHandler> exceptionHandlerHolder;
	
	/**
	 * Ĭ���쳣���������쳣���͵��������ĺ�����Ҳ���ƥ��
	 * ���쳣���������ͻ����Ĭ���쳣��������
	 */
	private ExceptionHandler defaultExceptionHandler;
	
	public ExceptionCenter()
	{
		exceptionHandlerHolder=new LinkedList<ExceptionHandler>();
		defaultExceptionHandler=new DefaultExceptionHandler();
	}
	
	/**
	 * �����ͨ�쳣��������
	 * ������Ӷ������ͬ���쳣���쳣�����������Ƕ�����Ч�ġ�
	 * 
	 */
	public synchronized void addExceptionHandler(ExceptionHandler handler)
	{
		if(null==handler)
		{
			throw new NullPointerException("handler should not be null");
		}
		
		exceptionHandlerHolder.add(handler);
	}
	
	/**
	 * ɾ����ͨ�쳣������
	 * @return true����쳣���������д洢��ָ�����쳣������handler
	 */
	public synchronized boolean removeExceptionHandler(ExceptionHandler handler)
	{
		if(null==handler)
		{
			throw new NullPointerException("handler should not be null");
		}
		
		return exceptionHandlerHolder.remove(handler);
	}
	
	/**
	 * ����ָ��һ��Ĭ���쳣������
	 * @return ��ȥ��Ĭ���쳣������
	 */
	public synchronized ExceptionHandler replaceDefaultExceptionHandler(
			ExceptionHandler handler)
	{
		if(null==handler)
		{
			throw new NullPointerException("handler should not be null");
		}
		if(!handler.isHandlerOf(new Exception()))
		{
			throw new IllegalArgumentException("handler should be able to handle any exception");
		}
		
		ExceptionHandler oldDefaultExceptionHandler=this.defaultExceptionHandler;
		assert(oldDefaultExceptionHandler!=null);
		this.defaultExceptionHandler = handler;
		return oldDefaultExceptionHandler;
	}
	
	/**
	 * ��һ���쳣����exception���͵��쳣�������ġ��쳣�������Ļ��ҳ�����������쳣����
	 * exceptionƥ�����ͨ�쳣����������������쳣�����û����ͨ�쳣������ƥ�䣬��ô��
	 * ����Ĭ���쳣����������������쳣����exception��
	 */
	public synchronized void postException(Exception exception)
	{
		if(null==exception)
		{
			throw new NullPointerException("exception should not be null");
		}
		
		//ʹ����ͨ�쳣������������
		boolean isHandled=false;
		for(ExceptionHandler handler : exceptionHandlerHolder)
		{
			if(handler.isHandlerOf(exception))
			{
				isHandled=true;
				handler.handleException(exception);
			}
		}
		
		//ʹ��Ĭ�ϴ����������쳣
		if(!isHandled)
		{
			assert(defaultExceptionHandler.isHandlerOf(exception));
			defaultExceptionHandler.handleException(exception);
		}
		
	}
	
}
