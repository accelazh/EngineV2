package com.accela.SynchronizeSupport.standard;

import com.accela.SynchronizeSupport.other.ConfigViewLock;

/**
 * 
 * �����ֻ�Ǽ򵥵ض�ConfigViewLock�ṩ��һ���װ�����ܻ���һ����
 * ֻ�ǰ�ConfigViewLock��������OpenCloseSupportһ����ģʽ����Ϊ
 * һЩ���ͬ�������ṩ����
 *
 */
public class ConfigViewSupport
{
	private ConfigViewLock lock=new ConfigViewLock();

	/**
	 * @see ConfigViewLock
	 */
	public void lockConfig()
	{
		lock.lockConfig();
	}

	/**
	 * @see ConfigViewLock
	 */
	public void lockView()
	{
		lock.lockView();
	}

	/**
	 * @see ConfigViewLock
	 */
	public void unlockConfig()
	{
		lock.unlockConfig();
	}

	/**
	 * @see ConfigViewLock
	 */
	public void unlockView()
	{
		lock.unlockView();
	}

}
