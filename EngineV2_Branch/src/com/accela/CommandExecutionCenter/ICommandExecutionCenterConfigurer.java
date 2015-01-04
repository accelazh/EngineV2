package com.accela.CommandExecutionCenter;

public interface ICommandExecutionCenterConfigurer 
extends ICommandExecutor
{
	public void addCommandHandler(CommandHandler commandHandler);
	
	public CommandHandler[] removeCommandHandler(String commandHead);
	
}
