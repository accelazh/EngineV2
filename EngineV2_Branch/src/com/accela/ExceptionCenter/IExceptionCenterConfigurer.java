package com.accela.ExceptionCenter;

/**
 * 
 * 这个接口由异常处理中心实现，通过它来访问异常处理中心
 * 用来配置的方法。
 *
 */
public interface IExceptionCenterConfigurer
{
	/**
	 * 添加普通异常处理器。
	 * 可以添加多个处理同种异常的异常处理器，它们都是有效的。
	 * 
	 */
	public void addExceptionHandler(ExceptionHandler handler);
	
	/**
	 * 删除普通异常处理器
	 * @return true如果异常处理中心中存储有指定的异常处理器handler
	 */
	public boolean removeExceptionHandler(ExceptionHandler handler);
	
	/**
	 * 重新指定一个默认异常处理器
	 * @return 过去的默认异常处理器
	 */
	public ExceptionHandler replaceDefaultExceptionHandler(
			ExceptionHandler handler);
}
