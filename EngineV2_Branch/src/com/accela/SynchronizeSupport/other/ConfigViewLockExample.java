package com.accela.SynchronizeSupport.other;

/**
 * 
 * Ϊ�������չʾConfigViewLock���ʹ�ö���д��ʾ����
 * 
 */
public class ConfigViewLockExample
{
	private ConfigViewLock lock = new ConfigViewLock();

	private int data = 0;

	/**
	 * ����������޸�����ڲ����ݣ���config������
	 * �����lockConfig()...unlockConfig()������
	 * ����������Լ�decrease()��������һ��ֻ�ܴ���ִ�С����������������
	 * �����getData()ͬʱִ��
	 */
	public void increase()
	{
		lock.lockConfig();
		try
		{
			data++;
		} finally
		{
			lock.unlockConfig();
		}
	}

	/**
	 * ����������޸�����ڲ����ݣ���config������
	 * �����lockConfig()...unlockConfig()����
	 * ����������Լ�icrease()��������һ��ֻ�ܴ���ִ�С����������������
	 * �����getData()ͬʱִ��
	 */
	public void decrease()
	{
		lock.lockConfig();
		try
		{
			data--;
		} finally
		{
			lock.unlockConfig();
		}
	}

	/**
	 * ������������޸�����ڲ����ݣ���view������
	 * �����lockView()...unlockView()������
	 * ����������Բ���ִ�С������Բ�����increase()��decrease()ͬʱ
	 * ִ�С�
	 */
	public int getData()
	{
		lock.lockView();
		try
		{
			return data;
		} finally
		{
			lock.unlockView();
		}
	}

}
