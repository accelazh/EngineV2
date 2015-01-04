package com.accela.TestCases.synchronizeSupport;

import com.accela.SynchronizeSupport.other.SwitchLock;

/**
 * 
 * SwitchLock测试大纲 1、测试method1、method2的并行运行与重入 2、测试open、close的串行运行与重入
 * 3、测试open、close抢断method1、method2 4、测试open、close抢断method1、method2后，
 * method1、method2的线程抢在open、close的线 程前去抢锁 5、测试open、close的在抢断后的并行运行与重入
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
