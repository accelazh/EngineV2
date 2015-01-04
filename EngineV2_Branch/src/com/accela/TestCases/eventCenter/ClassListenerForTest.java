package com.accela.TestCases.eventCenter;

import com.accela.EventCenter.ClassListener;
import com.accela.EventCenter.Event;

public class ClassListenerForTest extends ClassListener
{
	private Runnable target;

	public ClassListenerForTest(Class<? extends Event> aimEventClass,
			Class<? extends Object> aimClass)
	{
		super(aimEventClass, aimClass);
	}
	
	public ClassListenerForTest(Class<? extends Event> aimEventClass,
			Class<? extends Object> aimClass, Runnable target)
	{
		super(aimEventClass, aimClass);
		this.target=target;
	}
	
	private int invokeCount=0;
	
	@Override
	protected void handleEventImpl(Event event) throws Exception
	{
		invokeCount++;
		if(target!=null)
		{
			target.run();
		}
	}

	public int getInvokeCount()
	{
		return invokeCount;
	}

	public void clearInvokeCount()
	{
		this.invokeCount = 0;
	}

}
