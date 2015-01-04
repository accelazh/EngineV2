package com.accela.TestCases.exceptionCenter;

import com.accela.ExceptionCenter.ExceptionCenter;
import com.accela.ExceptionCenter.ExceptionHandler;

import junit.framework.TestCase;

public class TestingExceptionCenter extends TestCase
{
	private ExceptionCenter exceptionCenter=null;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		exceptionCenter=new ExceptionCenter();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		exceptionCenter=null;
	}
	
	public void testDefaultExceptionHandler()
	{
		TestingException testingException=new TestingException();
		exceptionCenter.postException(testingException);
		assert(testingException.isHandled());
		
		boolean exceptionThrowed=false;
		try{
		exceptionCenter.replaceDefaultExceptionHandler(new InvalidDefaultExceptionHandler());
		}catch(Exception ex)
		{
			exceptionThrowed=true;
			assert(ex instanceof IllegalArgumentException);
		}
		assert(exceptionThrowed);
		
		DefaultExceptionHandler deh=new DefaultExceptionHandler();
		ExceptionHandler oldEh=exceptionCenter.replaceDefaultExceptionHandler(deh);
		assert(oldEh.isHandlerOf(new Exception()));
		
		exceptionCenter.postException(new Exception());
		assert(deh.isInvoked());
		
	}
	
	public void testExceptionHandlers()
	{
		exceptionCenter.replaceDefaultExceptionHandler(new DefaultExceptionHandler());
		
		ExceptionHandlerA hA=new ExceptionHandlerA();
		ExceptionHandlerB hB=new ExceptionHandlerB();
		ExceptionHandlerC hC=new ExceptionHandlerC();
		
		ExceptionHandlerAA hAA=new ExceptionHandlerAA();
		ExceptionHandlerAB hAB=new ExceptionHandlerAB();
		
		ExceptionHandlerABA hABA=new ExceptionHandlerABA();
		
		assert(ExceptionA.class.isInstance(new ExceptionAA()));
		assert(ExceptionA.class.isInstance(new ExceptionAB()));
		assert(ExceptionAB.class.isInstance(new ExceptionABA()));
		
		exceptionCenter.addExceptionHandler(hA);
		exceptionCenter.addExceptionHandler(hA);
		exceptionCenter.addExceptionHandler(hA);
		
		exceptionCenter.addExceptionHandler(hAA);
		exceptionCenter.addExceptionHandler(hAB);
		exceptionCenter.addExceptionHandler(hABA);
		
		exceptionCenter.addExceptionHandler(hB);
		exceptionCenter.addExceptionHandler(hC);
		
		//test 1
		exceptionCenter.postException((Exception)(new ExceptionA()));
		
		assert(hA.isInvoked());
		assert(hA.getInvokeCount()==3);
		assert(!hB.isInvoked());
		assert(hB.getInvokeCount()==0);
		assert(!hC.isInvoked());
		assert(hC.getInvokeCount()==0);
		
		assert(!hAA.isInvoked());
		assert(hAA.getInvokeCount()==0);
		assert(!hAB.isInvoked());
		assert(hAB.getInvokeCount()==0);
		assert(!hABA.isInvoked());
		assert(hABA.getInvokeCount()==0);
		
		hA.clearInvokation();
		hB.clearInvokation();
		hC.clearInvokation();
		hAA.clearInvokation();
		hAB.clearInvokation();
		hABA.clearInvokation();
		
		//test 2
		exceptionCenter.postException(new ExceptionB());
		
		assert(!hA.isInvoked());
		assert(hA.getInvokeCount()==0);
		assert(hB.isInvoked());
		assert(hB.getInvokeCount()==1);
		assert(!hC.isInvoked());
		assert(hC.getInvokeCount()==0);
		
		assert(!hAA.isInvoked());
		assert(hAA.getInvokeCount()==0);
		assert(!hAB.isInvoked());
		assert(hAB.getInvokeCount()==0);
		assert(!hABA.isInvoked());
		assert(hABA.getInvokeCount()==0);
		
		hA.clearInvokation();
		hB.clearInvokation();
		hC.clearInvokation();
		hAA.clearInvokation();
		hAB.clearInvokation();
		hABA.clearInvokation();		
		
		//test 3
		exceptionCenter.postException(new ExceptionAA());
		
		assert(hA.isInvoked());
		assert(hA.getInvokeCount()==3);
		assert(!hB.isInvoked());
		assert(hB.getInvokeCount()==0);
		assert(!hC.isInvoked());
		assert(hC.getInvokeCount()==0);
		
		assert(hAA.isInvoked());
		assert(hAA.getInvokeCount()==1);
		assert(!hAB.isInvoked());
		assert(hAB.getInvokeCount()==0);
		assert(!hABA.isInvoked());
		assert(hABA.getInvokeCount()==0);
		
		hA.clearInvokation();
		hB.clearInvokation();
		hC.clearInvokation();
		hAA.clearInvokation();
		hAB.clearInvokation();
		hABA.clearInvokation();
		
		//test 4
		exceptionCenter.addExceptionHandler(hAB);
		ExceptionHandlerA hA2=new ExceptionHandlerA();
		exceptionCenter.addExceptionHandler(hA2);
		
		exceptionCenter.postException(new ExceptionABA());
		
		assert(hA.isInvoked());
		assert(hA.getInvokeCount()==3);
		assert(hA2.isInvoked());
		assert(hA2.getInvokeCount()==1);
		assert(!hB.isInvoked());
		assert(hB.getInvokeCount()==0);
		assert(!hC.isInvoked());
		assert(hC.getInvokeCount()==0);
		
		assert(!hAA.isInvoked());
		assert(hAA.getInvokeCount()==0);
		assert(hAB.isInvoked());
		assert(hAB.getInvokeCount()==2);
		assert(hABA.isInvoked());
		assert(hABA.getInvokeCount()==1);
		
		hA.clearInvokation();
		hA2.clearInvokation();
		hB.clearInvokation();
		hC.clearInvokation();
		hAA.clearInvokation();
		hAB.clearInvokation();
		hABA.clearInvokation();
		
		//test 5
		assert(exceptionCenter.removeExceptionHandler(hA));
		
		exceptionCenter.postException(new ExceptionABA());
		
		assert(hA.isInvoked());
		assert(hA.getInvokeCount()==2);
		assert(hA2.isInvoked());
		assert(hA2.getInvokeCount()==1);
		assert(!hB.isInvoked());
		assert(hB.getInvokeCount()==0);
		assert(!hC.isInvoked());
		assert(hC.getInvokeCount()==0);
		
		assert(!hAA.isInvoked());
		assert(hAA.getInvokeCount()==0);
		assert(hAB.isInvoked());
		assert(hAB.getInvokeCount()==2);
		assert(hABA.isInvoked());
		assert(hABA.getInvokeCount()==1);
		
		hA.clearInvokation();
		hA2.clearInvokation();
		hB.clearInvokation();
		hC.clearInvokation();
		hAA.clearInvokation();
		hAB.clearInvokation();
		hABA.clearInvokation();

		//test 6
		assert(exceptionCenter.removeExceptionHandler(hC));
		DefaultExceptionHandler dh=new DefaultExceptionHandler();
		exceptionCenter.replaceDefaultExceptionHandler(dh);
		
		exceptionCenter.postException(new ExceptionC());
		
		assert(!hA.isInvoked());
		assert(hA.getInvokeCount()==0);
		assert(!hA2.isInvoked());
		assert(hA2.getInvokeCount()==0);
		assert(!hB.isInvoked());
		assert(hB.getInvokeCount()==0);
		assert(!hC.isInvoked());
		assert(hC.getInvokeCount()==0);
		
		assert(!hAA.isInvoked());
		assert(hAA.getInvokeCount()==0);
		assert(!hAB.isInvoked());
		assert(hAB.getInvokeCount()==0);
		assert(!hABA.isInvoked());
		assert(hABA.getInvokeCount()==0);
		
		assert(dh.isInvoked());
		assert(dh.getInvokeCount()==1);
		
		hA.clearInvokation();
		hA2.clearInvokation();
		hB.clearInvokation();
		hC.clearInvokation();
		hAA.clearInvokation();
		hAB.clearInvokation();
		hABA.clearInvokation();
		dh.clearInvokation();
		
	}

}
