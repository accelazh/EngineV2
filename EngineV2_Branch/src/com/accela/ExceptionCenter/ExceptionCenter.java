package com.accela.ExceptionCenter;

import java.util.*;

/**
 * ExecptionCenter模块的入口
 * 
 * 异常处理中心（支持多线程）。
 * 异常处理中心中存储有多个异常处理器，当你把一个异常对象
 * 发送到异常处理中心的时候，异常处理中心会寻找匹配的异常
 * 处理其来处理它。
 *
 */
public class ExceptionCenter implements IExceptionPoster, IExceptionCenterConfigurer
{
	/**
	 * 普通异常处理器的容器。异常对象发送到异常处理中心后，
	 * 就会寻找匹配的异常处理器来处理。
	 */
	private List<ExceptionHandler> exceptionHandlerHolder;
	
	/**
	 * 默认异常处理器。异常发送到处理中心后，如果找不到匹配
	 * 的异常处理器，就会调用默认异常处理器。
	 */
	private ExceptionHandler defaultExceptionHandler;
	
	public ExceptionCenter()
	{
		exceptionHandlerHolder=new LinkedList<ExceptionHandler>();
		defaultExceptionHandler=new DefaultExceptionHandler();
	}
	
	/**
	 * 添加普通异常处理器。
	 * 可以添加多个处理同种异常的异常处理器，它们都是有效的。
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
	 * 删除普通异常处理器
	 * @return true如果异常处理中心中存储有指定的异常处理器handler
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
	 * 重新指定一个默认异常处理器
	 * @return 过去的默认异常处理器
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
	 * 将一个异常对象exception发送到异常处理中心。异常处理中心会找出所有与这个异常对象
	 * exception匹配的普通异常处理器来处理这个异常。如果没有普通异常处理器匹配，那么将
	 * 调用默认异常处理器来处理这个异常对象exception。
	 */
	public synchronized void postException(Exception exception)
	{
		if(null==exception)
		{
			throw new NullPointerException("exception should not be null");
		}
		
		//使用普通异常处理器来处理
		boolean isHandled=false;
		for(ExceptionHandler handler : exceptionHandlerHolder)
		{
			if(handler.isHandlerOf(exception))
			{
				isHandled=true;
				handler.handleException(exception);
			}
		}
		
		//使用默认处理器处理异常
		if(!isHandled)
		{
			assert(defaultExceptionHandler.isHandlerOf(exception));
			defaultExceptionHandler.handleException(exception);
		}
		
	}
	
}
