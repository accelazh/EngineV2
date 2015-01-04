package com.accela.TestCases.synchronizeSupport;

import com.accela.SynchronizeSupport.other.SwitchLock;

/**
 * 
 * SwitchLock���Դ�� 1������method1��method2�Ĳ������������� 2������open��close�Ĵ�������������
 * 3������open��close����method1��method2 4������open��close����method1��method2��
 * method1��method2���߳�����open��close���� ��ǰȥ���� 5������open��close�������Ϻ�Ĳ�������������
 * 
 */
public class SwitchLockTester
{
	private SwitchLock switchLock = new SwitchLock();

	private boolean openInvokded=false;
	private boolean openInnerInvokded=false;
	private boolean closeInvokded=false;
	private boolean method1Invokded=false;
	private boolean method2Invokded=false;
	
	
	public void open()
	{
		switchLock.lockSwitch();

		try
		{
			switchLock.lockSwitch();
			openInnerInvokded=true;
			switchLock.unlockSwitch();

			openInvokded=true;
		} finally
		{
			switchLock.unlockSwitch();
		}
	}

	public void close()
	{
		switchLock.lockSwitch();

		try
		{
			closeInvokded=true;
		} finally
		{
			switchLock.unlockSwitch();
		}
	}

	public void method1() throws InterruptedException
	{
		switchLock.lockMethod();

		try
		{
			synchronized (this)
			{
				wait();
			}
			method1Invokded=true;
		} finally
		{
			switchLock.unlockMethod();
		}
	}

	public void method2() throws InterruptedException
	{
		switchLock.lockMethod();

		try
		{
			synchronized (this)
			{
				wait();
			}
			method2Invokded=true;
		} finally
		{
			switchLock.unlockMethod();
		}
	}

	public void startTestThreads()
	{
		new OpenThread().start();
		new CloseThread().start();
		new Method1Thread().start();
		new Method2Thread().start();
	}
	
	public void bombTest()
	{
		for(int i=0;i<4;i++)
		{
			new OpenThread().start();
			new CloseThread().start();
			new Method1Thread().start();
			new Method2Thread().start();
		}
		
		try
		{
			Thread.sleep(6000);
		} catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}
		
		assert(openInvokded);
		assert(openInnerInvokded);
		assert(closeInvokded);
		assert(method1Invokded);
		assert(method2Invokded);
		
		System.exit(0);
		
	}

	private class OpenThread extends Thread
	{
		public OpenThread()
		{
			super("TestSwitchLock - OpenThread");
		}

		public void run()
		{
			while (true)
			{
				open();
			}
		}
	}

	private class CloseThread extends Thread
	{
		public CloseThread()
		{
			super("TestSwitchLock - CloseThread");
		}

		public void run()
		{
			while (true)
			{
				close();
			}
		}
	}

	private class Method1Thread extends Thread
	{
		public Method1Thread()
		{
			super("TestSwitchLock - Method1Thread");
		}

		public void run()
		{
			while (true)
			{
				try
				{
					method1();
				} catch (InterruptedException ex)
				{
					//ex.printStackTrace();
				}
			}
		}
	}

	private class Method2Thread extends Thread
	{
		public Method2Thread()
		{
			super("TestSwitchLock - Method2Thread");
		}

		public void run()
		{
			while (true)
			{
				try
				{
					method2();
				} catch (InterruptedException ex)
				{
					//ex.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args)
	{
		//new TestSwitchLock().startTestThreads();
		new SwitchLockTester().bombTest();
	}

}
