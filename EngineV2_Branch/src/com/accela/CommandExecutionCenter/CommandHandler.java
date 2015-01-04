package com.accela.CommandExecutionCenter;

import com.accela.CommandExecutionCenter.shared.ExecutionCommand;
import com.accela.CommandExecutionCenter.shared.ExecutionCommanderID;

/**
 * 命令处理器（支持多线程）。
 * 
 * CommanderHandler用来处理指定的某一种命令，
 * 你需要指定它应该处理的命令的CommandHead。
 * 命令处理器规定了应该怎样处理某一种命令的。
 * 
 * 继承此类以提供处理方法。
 * 
 * //Inheritance needed
 */
public abstract class CommandHandler
{
	/**
	 * 这个CommanderHandler专门处理的命令CommandHead，用来指定
	 * 这个CommanderHandler处理哪种命令
	 */
	private String aimCommandHead;
	
	/**
	 * @param aimCommandHead 指定CommandHandler用来处理哪种命令
	 */
	public CommandHandler(String aimCommandHead)
	{
		if(!ExecutionCommand.checkCommandHeadValid(aimCommandHead))
		{
			throw new IllegalArgumentException("aimCommandHead is not valid");
		}
		
		this.aimCommandHead=aimCommandHead;
	}
	
	public synchronized void handleCommand(ExecutionCommand command) throws Exception
	{
		if(null==command)
		{
			throw new NullPointerException("command should not be null");
		}
		if(!aimCommandHead.equals(command.getCommandHead()))
		{
			assert(false);    //命令处理器不应该接到不该自己处理的命令
		}
		assert(command.getCommanderID()!=null);
		if(null==command.getCommanderID())
		{
			throw new NullPointerException("commanderID should not be null");
		}
		
		handleCommandImpl(command.getCommanderID(), command.getArgument());
	}
	
	protected abstract void handleCommandImpl(ExecutionCommanderID command, Object argument) throws Exception;

	public String getAimCommandHead()
	{
		return aimCommandHead;
	}

}
