package com.accela.TestCases.eventCenter;

import com.accela.EventCenter.Event;
import com.accela.EventCenter.EventCenter;
import com.accela.EventCenter.EventProcessingException;

import junit.framework.TestCase;

public class TestingEventCenter extends TestCase
{
	private EventCenter eventCenter = null;

	protected void setUp() throws Exception
	{
		super.setUp();
		eventCenter = new EventCenter();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
		eventCenter = null;
	}

	public void testListeners()
	{
		ClassA senderA = new ClassA();
		ClassB senderB = new ClassB();
		ClassAA senderAA = new ClassAA();

		GeneralListenerForTest g1 = new GeneralListenerForTest(Event1.class);
		eventCenter.addGeneralListener(g1);
		eventCenter.addGeneralListener(g1);
		eventCenter.addGeneralListener(g1);
		GeneralListenerForTest g2 = new GeneralListenerForTest(Event2.class);
		eventCenter.addGeneralListener(g2);
		GeneralListenerForTest g11 = new GeneralListenerForTest(Event11.class);
		eventCenter.addGeneralListener(g11);

		ClassListenerForTest cA1 = new ClassListenerForTest(Event1.class,
				ClassA.class);
		eventCenter.addClassListener(cA1);
		eventCenter.addClassListener(cA1);
		eventCenter.addClassListener(cA1);
		ClassListenerForTest cB1 = new ClassListenerForTest(Event1.class,
				ClassB.class);
		eventCenter.addClassListener(cB1);
		ClassListenerForTest cAA1 = new ClassListenerForTest(Event1.class,
				ClassAA.class);
		eventCenter.addClassListener(cAA1);

		ClassListenerForTest cA2 = new ClassListenerForTest(Event2.class,
				ClassA.class);
		eventCenter.addClassListener(cA2);
		ClassListenerForTest cB2 = new ClassListenerForTest(Event2.class,
				ClassB.class);
		eventCenter.addClassListener(cB2);
		ClassListenerForTest cAA2 = new ClassListenerForTest(Event2.class,
				ClassAA.class);
		eventCenter.addClassListener(cAA2);

		ClassListenerForTest cA11 = new ClassListenerForTest(Event11.class,
				ClassA.class);
		eventCenter.addClassListener(cA11);
		ClassListenerForTest cB11 = new ClassListenerForTest(Event11.class,
				ClassB.class);
		eventCenter.addClassListener(cB11);
		ClassListenerForTest cAA11 = new ClassListenerForTest(Event11.class,
				ClassAA.class);
		eventCenter.addClassListener(cAA11);

		InstanceListenerForTest iA1 = new InstanceListenerForTest(Event1.class,
				senderA);
		eventCenter.addInstanceListener(iA1);
		eventCenter.addInstanceListener(iA1);
		eventCenter.addInstanceListener(iA1);
		InstanceListenerForTest iB1 = new InstanceListenerForTest(Event1.class,
				senderB);
		eventCenter.addInstanceListener(iB1);
		InstanceListenerForTest iAA1 = new InstanceListenerForTest(
				Event1.class, senderAA);
		eventCenter.addInstanceListener(iAA1);

		InstanceListenerForTest iA2 = new InstanceListenerForTest(Event2.class,
				senderA);
		eventCenter.addInstanceListener(iA2);
		InstanceListenerForTest iB2 = new InstanceListenerForTest(Event2.class,
				senderB);
		eventCenter.addInstanceListener(iB2);
		InstanceListenerForTest iAA2 = new InstanceListenerForTest(
				Event2.class, senderAA);
		eventCenter.addInstanceListener(iAA2);

		InstanceListenerForTest iA11 = new InstanceListenerForTest(
				Event11.class, senderA);
		eventCenter.addInstanceListener(iA11);
		InstanceListenerForTest iB11 = new InstanceListenerForTest(
				Event11.class, senderB);
		eventCenter.addInstanceListener(iB11);
		InstanceListenerForTest iAA11 = new InstanceListenerForTest(
				Event11.class, senderAA);
		eventCenter.addInstanceListener(iAA11);

		Event event = null;

		// =====================

		// test1 classA - event1
		event = new Event1(senderA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==3);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==3);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==3);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test2 classA - event2
		event = new Event2(senderA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==1);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==1);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==1);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test3 classA - event11
		event = new Event11(senderA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==1);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==1);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==1);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// =====================

		// test4 classB - event1
		event = new Event1(senderB);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==3);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==1);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==1);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test5 classB - event2
		event = new Event2(senderB);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==1);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==1);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==1);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test6 classB - event11
		event = new Event11(senderB);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==1);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==1);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==1);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// =====================

		// test7 classAA - event1
		event = new Event1(senderAA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==3);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==1);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==1);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test8 classAA - event2
		event = new Event2(senderAA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==1);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==1);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==1);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test9 classAA - event11
		event = new Event11(senderAA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==1);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==1);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==1);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// =====================
		
		//removing some listeners
		assert(eventCenter.removeGeneralListener(Event1.class).length==3);
		assert(eventCenter.removeClassListener(Event2.class, ClassA.class).length==1);
		assert(eventCenter.removeInstanceListener(Event11.class, senderB).length==1);
		
		// =====================
		
		//retesting
		
		// test1 classA - event1
		event = new Event1(senderA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==3);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==3);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test2 classA - event2
		event = new Event2(senderA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==1);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==1);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test3 classA - event11
		event = new Event11(senderA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==1);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==1);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==1);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// =====================

		// test4 classB - event1
		event = new Event1(senderB);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==1);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==1);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test5 classB - event2
		event = new Event2(senderB);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==1);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==1);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==1);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test6 classB - event11
		event = new Event11(senderB);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==1);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==1);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// =====================

		// test7 classAA - event1
		event = new Event1(senderAA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==1);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==1);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test8 classAA - event2
		event = new Event2(senderAA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==1);
		assert(g11.getInvokeCount()==0);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==1);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==0);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==1);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==0);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// test9 classAA - event11
		event = new Event11(senderAA);
		try
		{
			eventCenter.postEvent(event);
		} catch (EventProcessingException ex)
		{
			ex.printStackTrace();
			assert(false);
		}

		assert(g1.getInvokeCount()==0);
		assert(g2.getInvokeCount()==0);
		assert(g11.getInvokeCount()==1);
		
		assert(cA1.getInvokeCount()==0);
		assert(cB1.getInvokeCount()==0);
		assert(cAA1.getInvokeCount()==0);
		
		assert(cA2.getInvokeCount()==0);
		assert(cB2.getInvokeCount()==0);
		assert(cAA2.getInvokeCount()==0);
		
		assert(cA11.getInvokeCount()==0);
		assert(cB11.getInvokeCount()==0);
		assert(cAA11.getInvokeCount()==1);
		
		assert(iA1.getInvokeCount()==0);
		assert(iB1.getInvokeCount()==0);
		assert(iAA1.getInvokeCount()==0);
		
		assert(iA2.getInvokeCount()==0);
		assert(iB2.getInvokeCount()==0);
		assert(iAA2.getInvokeCount()==0);
		
		assert(iA11.getInvokeCount()==0);
		assert(iB11.getInvokeCount()==0);
		assert(iAA11.getInvokeCount()==1);
		
		g1.clearInvokeCount();
		g2.clearInvokeCount();
		g11.clearInvokeCount();
		cA1.clearInvokeCount();
		cB1.clearInvokeCount();
		cAA1.clearInvokeCount();
		cA2.clearInvokeCount();
		cB2.clearInvokeCount();
		cAA2.clearInvokeCount();
		cA11.clearInvokeCount();
		cB11.clearInvokeCount();
		cAA11.clearInvokeCount();
		iA1.clearInvokeCount();
		iB1.clearInvokeCount();
		iAA1.clearInvokeCount();
		iA2.clearInvokeCount();
		iB2.clearInvokeCount();
		iAA2.clearInvokeCount();
		iA11.clearInvokeCount();
		iB11.clearInvokeCount();
		iAA11.clearInvokeCount();
		
		// =====================
		
		//测试Listener的对目标的识别
		assert(g1.isListeningToEvent(Event1.class));
		assert(!g1.isListeningToEvent(Event2.class));
		assert(g1.isListeningToInstance(senderA));
		assert(g1.isListeningToInstance(senderB));
		
		assert(cA1.isListeningToEvent(Event1.class));
		assert(!cA1.isListeningToEvent(Event11.class));
		assert(cA1.isListeningToInstance(senderA));
		assert(cA1.isListeningToInstance(new ClassA()));
		assert(!cA1.isListeningToInstance(senderB));
		
		assert(iA2.isListeningToEvent(Event2.class));
		assert(!iA2.isListeningToEvent(Event1.class));
		assert(iA2.isListeningToInstance(senderA));
		assert(!iA2.isListeningToInstance(senderB));
		assert(!iA2.isListeningToInstance(new ClassA()));
		
	}
	
	public void testExceptionProcessing()
	{
		ClassA senderA=new ClassA();
		Event event=new Event1(senderA);
		
		GeneralListenerForTest g1=new GeneralListenerForTest(Event1.class);
		GeneralListenerForTest g2=new GeneralListenerForTest(Event1.class);
		GeneralListenerForTest g3=new GeneralListenerForTest(Event1.class, 
				new Runnable(){
			public void run()
			{
				throw new NullPointerException();				
			}
		
		});
		
		GeneralListenerForTest g4=new GeneralListenerForTest(Event1.class, 
				new Runnable(){
			public void run()
			{
				throw new IllegalArgumentException();
			}
		});
		
		ClassListenerForTest c1=new ClassListenerForTest(Event1.class, ClassA.class);
		ClassListenerForTest c2=new ClassListenerForTest(Event1.class, ClassA.class);
		ClassListenerForTest c3=new ClassListenerForTest(Event1.class, ClassA.class,
				new Runnable(){
			public void run()
			{
				throw new NullPointerException();
			}
		});
		ClassListenerForTest c4=new ClassListenerForTest(Event1.class, ClassA.class,
				new Runnable(){
			public void run()
			{
				throw new IllegalArgumentException();
			}
		});
		
		InstanceListenerForTest i1=new InstanceListenerForTest(Event1.class, senderA);
		InstanceListenerForTest i2=new InstanceListenerForTest(Event1.class, senderA);
		InstanceListenerForTest i3=new InstanceListenerForTest(Event1.class, senderA,
				new Runnable(){
			public void run()
			{
				throw new NullPointerException();
			}
		});
		InstanceListenerForTest i4=new InstanceListenerForTest(Event1.class, senderA,
				new Runnable(){
			public void run()
			{
				throw new IllegalArgumentException();
			}
		});
		
		//===================
		
		eventCenter.addGeneralListener(g1);
		eventCenter.addGeneralListener(g2);
		eventCenter.addGeneralListener(g3);
		eventCenter.addGeneralListener(g4);
		
		eventCenter.addClassListener(c1);
		eventCenter.addClassListener(c2);
		eventCenter.addClassListener(c3);
		eventCenter.addClassListener(c4);
		
		eventCenter.addInstanceListener(i1);
		eventCenter.addInstanceListener(i2);
		eventCenter.addInstanceListener(i3);
		eventCenter.addInstanceListener(i4);
		
		//===================
		
		EventProcessingException exception=null;
		try
		{
			eventCenter.postEvent(event);
		}catch(Exception ex)
		{
			assert(ex instanceof EventProcessingException);
			exception=(EventProcessingException)ex;
			
		}
		
		assert(g1.getInvokeCount()==1);
		assert(g2.getInvokeCount()==1);
		assert(g3.getInvokeCount()==1);
		assert(g4.getInvokeCount()==1);
		
		assert(c1.getInvokeCount()==1);
		assert(c2.getInvokeCount()==1);
		assert(c3.getInvokeCount()==1);
		assert(c4.getInvokeCount()==1);
		
		assert(i1.getInvokeCount()==1);
		assert(i2.getInvokeCount()==1);
		assert(i3.getInvokeCount()==1);
		assert(i4.getInvokeCount()==1);
		
		int nullPointerExceptionCount=0;
		int illegalArgumentExceptionCount=0;
		for(Throwable ex : exception.getCauseList())
		{
			if(ex instanceof NullPointerException)
			{
				nullPointerExceptionCount++;
			}
			else if(ex instanceof IllegalArgumentException)
			{
				illegalArgumentExceptionCount++;
			}
			else
			{
				assert(false);
			}
		}
		
		assert(3==nullPointerExceptionCount);
		assert(3==illegalArgumentExceptionCount);
		
	}

}
