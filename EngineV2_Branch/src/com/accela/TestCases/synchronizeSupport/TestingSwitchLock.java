package com.accela.TestCases.synchronizeSupport;

import junit.framework.TestCase;

public class TestingSwitchLock extends TestCase
{
	/**
	 * SwitchLock�Ĳ����ǻ��ڶ��̵߳ģ��޷��Զ����ԡ�
	 * �����ʹ��SwitchLockTester��������ע���е�˵
	 * ������ֶ�����
	 */
	public void test()
	{
		SwitchLockTester t=new SwitchLockTester();
		t.startTestThreads();
		t.bombTest();
	}

}
