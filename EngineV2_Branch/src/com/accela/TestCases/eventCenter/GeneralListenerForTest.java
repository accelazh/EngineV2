package com.accela.TestCases.eventCenter;

import com.accela.EventCenter.Event;
import com.accela.EventCenter.GeneralListener;

public class GeneralListenerForTest extends GeneralListener
{
	private Runnable target;
	
	public GeneralListenerForTest(Class<? extends Event> aimEventClass)
	{
		super(aimEventClass);
	}
	
	public GeneralListenerForTest(Class<? extends Event> aimEventClass, Runnable target)
	{
		super(aimEventClass);
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
