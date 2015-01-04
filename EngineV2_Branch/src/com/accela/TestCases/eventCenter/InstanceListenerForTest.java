package com.accela.TestCases.eventCenter;

import com.accela.EventCenter.Event;
import com.accela.EventCenter.InstanceListener;

public class InstanceListenerForTest extends InstanceListener
{
	private Runnable target;
	
	public InstanceListenerForTest(Class<? extends Event> aimEventClass,
			Object aimInstance)
	{
		super(aimEventClass, aimInstance);
	}
	
	public InstanceListenerForTest(Class<? extends Event> aimEventClass,
			Object aimInstance, Runnable target)
	{
		super(aimEventClass, aimInstance);
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
