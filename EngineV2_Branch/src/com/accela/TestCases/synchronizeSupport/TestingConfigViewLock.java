package com.accela.TestCases.synchronizeSupport;

public class TestingConfigViewLock
{
	/**
	 * ConfigViewLock的测试是基于对线程的，无法自动测试。
	 * 你可以使用ConfigViewLockTester，根据其注释中的说
	 * 明大纲手动测试
	 */
	public void test()
	{
		ConfigViewLockTester t=new ConfigViewLockTester();
		t.startTestThreads();
		t.bombTest();
	}

}
