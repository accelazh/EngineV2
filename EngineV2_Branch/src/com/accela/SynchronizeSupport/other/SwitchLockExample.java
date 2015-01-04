package com.accela.SynchronizeSupport.other;

/**
 * 
 * �����Ϊ�����ر��SiwthLock��ʹ�÷�������д
 *
 */
public class SwitchLockExample
{
	private boolean open = false;

	private SwitchLock lock = new SwitchLock();

	public boolean isOpen()
	{
		return open;
	}

	/**
	 * ���ǿ��ط����еĴ򿪷�����Ӧ����
	 * lockSwitch()...unlockSwitch()��
	 * ��������
	 */
	public void open()
	{
		lock.lockSwitch();
		try
		{
			open = true;
		} finally
		{
			lock.unlockSwitch();
		}
	}

	/**
	 * ���ǿ��ط����еĹرշ�����Ӧ����
	 * lockSwitch()...unlockSwitch()��
	 * ��������
	 */
	public void close()
	{
		lock.lockSwitch();
		try
		{
			open = false;
		} finally
		{
			lock.unlockSwitch();
		}
	}

	/**
	 * ����һ����ͨ������Ӧ����
	 * lockMethod()...unlockMethod()��������
	 */
	public void method1() throws InterruptedException
	{
		lock.lockMethod();
		try
		{
			Thread.sleep(1000);
		} finally
		{
			/*
			 * ������ע�⣬��unlockMethod()��������finally�־��У�
			 * ����try�������׳��쳣�ķ�����������������һ�ֺõ�
			 * ���ϰ�ߣ����Ҷ���switchLock�Ǳ���ġ�
			 * 
			 * ��Ϊ��lockSwitch()ͨ��ʹ������ͨ�������߳��׳��쳣
			 * ������ϳ�������
			 */
			lock.unlockMethod();	
		}
	}

	/**
	 * ����һ����ͨ������Ӧ����
	 * lockMethod()...unlockMethod()��������
	 */
	public void method2()
	{
		lock.lockMethod();
		try
		{
			System.out.println("method2 invoked!");
		} finally
		{
			lock.unlockMethod();
		}
	}

}
