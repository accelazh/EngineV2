package com.accela.SynchronizeSupport.standard;

/**
 * 
 * 这个类用来装在一个可以抛出任何异常的方法
 *
 */
public interface TargetMethod
{
	public abstract void run() throws Exception;
}
