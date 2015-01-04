package com.accela.CommandExecutionCenter;

import com.accela.CommandExecutionCenter.shared.ExecutionCommand;

public interface ICommandExecutor
{
	public boolean executeCommand(ExecutionCommand command) throws CommandExecutingException;
}
