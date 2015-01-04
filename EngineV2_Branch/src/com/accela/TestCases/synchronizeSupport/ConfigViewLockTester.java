package com.accela.TestCases.synchronizeSupport;

import com.accela.SynchronizeSupport.other.ConfigViewLock;

/**
 * ConfigViewLock测试大纲： 1、测试configLock的串行性与可重入性 2、测试viewLock的并行性与可重入性
 * 3、测试configLock与viewLock的互斥性
 * 
 * 
 */
public class ConfigViewLockTester
{
	private ConfigViewLock lock = new ConfigViewLock();

	private boolean config1Invokded = false;
	private boolean config1InnerInvokded = false;
	private boolean config2Invokded = false;
	private boolean view1Invokded = false;
	private boolean view2Invokded = false;

	public void config1()
	{
		lock.lockConfig();

		try
		{
			lock.lockConfig();
			config1InnerInvokded = true;
			lock.unlockConfig();

			config1Invokded = true;
		} finally
		{
			lock.unlockConfig();
		}
	}

	public void config2()
	{
		lock.lockConfig();

		try
		{
			config2Invokded = true;
		} finally
		{
			lock.unlockConfig();
		}
	}

	public void view1()
	{
		lock.lockView();

		try
		{
			view1Invokded = true;
		} finally
		{
			lock.unlockView();
		}
	}

	public void view2()
	{
		lock.lockView();

		try
		{
			view2Invokded = true;
		} finally
		{
			lock.unlockView();
		}
	}

	public void startTestThreads()
	{
		new Config1Thread().start();
		new Config2Thread().start();
		new View1Thread().start();
		new View2Thread().start();
	}

	public void bombTest()
	{
		for (int i = 0; i < 4; i++)
		{
			new Config1Thread().start();
			new Config2Thread().start();
			new View1Thread().start();
			new View2Thread().start();
		}

		try
		{
			Thread.sleep(6000);
		} catch (InterruptedException ex)
		{
			ex.printStackTrace();
		}

		assert (config1Invokded);
		assert (config1InnerInvokded);
		assert (config2Invokded);
		assert (view1Invokded);
		assert (view2Invokded);

		System.exit(0);

	}

	private class Config1Thread extends Thread
	{
		public Config1Thread()
		{
			super("TestConfigViewLock - Config1Thread");
		}

		public void run()
		{
			while (true)
			{
				config1();
			}
		}
	}

	private class Config2Thread extends Thread
	{
		public Config2Thread()
		{
			super("TestConfigViewLock - Config2Thread");
		}

		public void run()
		{
			while (true)
			{
				config2();
			}
		}
	}

	private class View1Thread extends Thread
	{
		public View1Thread()
		{
			super("TestConfigViewLock - View1Thread");
		}

		public void run()
		{
			while (true)
			{
				view1();
			}
		}
	}

	private class View2Thread extends Thread
	{
		public View2Thread()
		{
			super("TestConfigViewLock - View2Thread");
		}

		public void run()
		{
			while (true)
			{
				view2();
			}
		}
	}

	public static void main(String[] args)
	{
		new ConfigViewLockTester().startTestThreads();
	}

}
