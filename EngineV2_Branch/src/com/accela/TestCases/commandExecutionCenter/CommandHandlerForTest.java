package com.accela.TestCases.commandExecutionCenter;

import com.accela.CommandExecutionCenter.CommandHandler;
import com.accela.CommandExecutionCenter.shared.ExecutionCommanderID;

public class CommandHandlerForTest extends CommandHandler
{
	private int invokeCount=0;
	
	private Runnable target=null;

	public CommandHandlerForTest(String aimCommandHead)
	{
		super(aimCommandHead);
	}
	
	public CommandHandlerForTest(String aimCommanderHead, Runnable target)
	{
		super(aimCommanderHead);
		this.target=target;
	}

	@Override
	protected void handleCommandImpl(ExecutionCommanderID command,
			Object argument) throws Exception
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
		invokeCount=0;
	}

}
