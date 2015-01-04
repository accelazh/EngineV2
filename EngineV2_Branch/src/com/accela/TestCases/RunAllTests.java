package com.accela.TestCases;

import com.accela.TestCases.authorityCenter.TestingAuthorityBase;
import com.accela.TestCases.authorityCenter.TestingAuthorityCenter;
import com.accela.TestCases.authorityCenter.TestingAuthorityChangeManager;
import com.accela.TestCases.authorityCenter.TestingAuthorityFilter;
import com.accela.TestCases.authorityCenter.TestingSimplePasswordManager;
import com.accela.TestCases.authorityCenter.TestingTypedAuthorityBase;
import com.accela.TestCases.authorityCenter.TestingTypedAuthorityLevelManager;
import com.accela.TestCases.authorityCenter.TestingTypedSimplePasswordManager;
import com.accela.TestCases.commandExecutionCenter.TestingCommandExecutionCenter;
import com.accela.TestCases.connectionCenterUnionHPObjectStreams.TestingConnectoinCenterUnionHPObjectStreams;
import com.accela.TestCases.container.TestingContainerMultiThread;
import com.accela.TestCases.container.TestingContainerSingleThread;
import com.accela.TestCases.eventCenter.TestingEventCenter;
import com.accela.TestCases.exceptionCenter.TestingExceptionCenter;
import com.accela.TestCases.objectPool.TestingObjectPool;
import com.accela.TestCases.objectStreams.TestingObjectStreams;
import com.accela.TestCases.reflectionSupport.TestingObjectCleaner;
import com.accela.TestCases.socketConnectionCenter.TestingSocketConnectionCenter;
import com.accela.TestCases.synchronizeSupport.TestingConfigViewLock;
import com.accela.TestCases.synchronizeSupport.TestingSwitchLock;

import junit.framework.Test;
import junit.framework.TestSuite;

public class RunAllTests
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for com.accela.TestCases");

		// $JUnit-BEGIN$
		suite.addTestSuite(TestingTypedAuthorityBase.class);
		suite.addTestSuite(TestingTypedAuthorityLevelManager.class);
		suite.addTestSuite(TestingAuthorityCenter.class);
		suite.addTestSuite(TestingSimplePasswordManager.class);
		suite.addTestSuite(TestingAuthorityChangeManager.class);
		suite.addTestSuite(TestingTypedSimplePasswordManager.class);
		suite.addTestSuite(TestingAuthorityBase.class);
		suite.addTestSuite(TestingAuthorityFilter.class);

		suite.addTestSuite(TestingCommandExecutionCenter.class);

		suite.addTestSuite(TestingEventCenter.class);

		suite.addTestSuite(TestingExceptionCenter.class);

		suite.addTestSuite(TestingSocketConnectionCenter.class);

		suite.addTestSuite(TestingContainerSingleThread.class);
		suite.addTestSuite(TestingContainerMultiThread.class);

		suite.addTestSuite(TestingObjectPool.class);

		suite.addTestSuite(TestingObjectStreams.class);

		suite.addTestSuite(TestingObjectCleaner.class);

		suite.addTestSuite(TestingConnectoinCenterUnionHPObjectStreams.class);

		suite.addTestSuite(TestingConfigViewLock.class);
		suite.addTestSuite(TestingSwitchLock.class);

		// suite.addTestSuite(TestingClassAndInstanceID.class); 这个测试非常耗时

		// $JUnit-END$
		return suite;
	}

}
