package com.accela.TestCases.synchronizeSupport;

import junit.framework.TestCase;

public class TestingSwitchLock extends TestCase
{
	/**
	 * SwitchLock的测试是基于对线程的，无法自动测试。
	 * 你可以使用SwitchLockTester，根据其注释中的说
	 * 明大纲手动测试
	 */
	public void test()
	{
		SwitchLockTester t=new SwitchLockTester();
		t.startTestThreads();
		t.bombTest();
	}

}
