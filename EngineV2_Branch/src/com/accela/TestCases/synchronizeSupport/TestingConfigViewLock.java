package com.accela.TestCases.synchronizeSupport;

public class TestingConfigViewLock
{
	/**
	 * ConfigViewLock�Ĳ����ǻ��ڶ��̵߳ģ��޷��Զ����ԡ�
	 * �����ʹ��ConfigViewLockTester��������ע���е�˵
	 * ������ֶ�����
	 */
	public void test()
	{
		ConfigViewLockTester t=new ConfigViewLockTester();
		t.startTestThreads();
		t.bombTest();
	}

}
