package com.accela.ExceptionCenter;

/**
 * 
 * 这个接口由异常处理中心实现，通过它来访问异常处理中心
 * 用来发送异常的方法。
 *
 */
public interface IExceptionPoster
{
	/**
	 * 将一个异常对象exception发送到异常处理中心。异常处理中心会找出所有与这个异常对象
	 * exception匹配的普通异常处理器来处理这个异常。如果没有普通异常处理器匹配，那么将
	 * 调用默认异常处理器来处理这个异常对象exception。
	 */
	public void postException(Exception exception);
}
