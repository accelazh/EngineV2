package com.accela.CommandExecutionCenter;

import java.util.*;

import com.accela.CommandExecutionCenter.shared.ExecutionCommand;

/**
 * CommandExecutionCenter模块的入口
 * 
 * 命令执行中心（支持多线程）。
 * 命令执行中心用来执行命令。命令执行中心中有多个CommandHandler，当一个命令发送
 * 到命令执行中心中的时候，它会寻找匹配的CommandHander来处理这条命令。
 * 
 */
public class CommandExecutionCenter
implements ICommandExecutor, ICommandExecutionCenterConfigurer
{
	/**
	 * 装载CommandHandler的容器，支持同步。键用的是CommandHead，值用的是
	 * CommandHandler的链表，因为处理同一种命令的CommandHandler可以有多个
	 */
	private Map<String, List<CommandHandler>> commandHandlerHolder=new HashMap<String, List<CommandHandler>>();
    
	public CommandExecutionCenter()
	{
		
	}
	
	/**
	 * 添加一个CommandHandler。
	 * 处理同种命令的ComandHandler可以有多个，当执行这种命令时，它们会
	 * 按照加入的先后顺序被执行
	 */
	public synchronized void addCommandHandler(CommandHandler commandHandler)
	{
		if(null==commandHandler)
		{
			throw new NullPointerException("commandHandler should not be null");
		}
		
		String commandHead=commandHandler.getAimCommandHead();
		assert(ExecutionCommand.checkCommandHeadValid(commandHead));
		
		List<CommandHandler> list=commandHandlerHolder.get(commandHead);
		if(null==list)
		{
			list=new LinkedList<CommandHandler>();
			list.add(commandHandler);
			commandHandlerHolder.put(commandHead, list);
		}
		else
		{
			list.add(commandHandler);
		}
	}
	
	/**
	 * 删除用于处理comandHead类命令的所有CommandHandler
	 * @param commandHead 欲删除的CommandHandler处理的命令的CommandHead
	 * @return 被删除的CommandHandler，如果没有，则返回null
	 */
	public synchronized CommandHandler[] removeCommandHandler(String commandHead)
	{
		if(!ExecutionCommand.checkCommandHeadValid(commandHead))
		{
			throw new IllegalArgumentException("commandHead is not valid");
		}
		
		List<CommandHandler> list=commandHandlerHolder.remove(commandHead);
		if(null==list)
		{
			return null;
		}
		else if(list.size()>0)
		{
			return list.toArray(new CommandHandler[0]);
		}
		else
		{
			assert(false);    //不应该出现这种情况
			return list.toArray(new CommandHandler[0]);
		}
		
	}
	
	/**
	 * 执行命令。所有匹配的CommandHandler都会被执行，执行的
	 * 顺序与它们加入的顺序相同。
	 * 所谓匹配，是指
	 * CommandHandler.getAimCommandHead().equals(Command.getCommandHead())==true
	 * 
	 * @param command 被执行的命令
	 * @return 是否至少有一个CommandHandler处理了传入的命令
	 * @throws CommandExecutingException 当执行命令的时候，如果发生异常，则会记录异常
	 * 并继续调用其它CommandHandler。当所有CommandHandler都调用完毕后，才会抛出异常
	 * CommandExecutingException。执行命令的时候发生的异常会被记录在异常CommandExecutingException中。
	 */
	public synchronized boolean executeCommand(ExecutionCommand command) throws CommandExecutingException
	{
		if(null==command)
		{
			throw new NullPointerException("command should not be null");
		}
		
		boolean hasException=false;
		List<Throwable> exceptionList=new LinkedList<Throwable>();
		
		List<CommandHandler> list=commandHandlerHolder.get(command.getCommandHead());
		if(null==list)
		{
			return false;
		}
		else if (list.size() > 0)
		{
			for (CommandHandler handler : list)
			{
				assert (handler != null);
				try
				{
					handler.handleCommand(command);
				} catch(Exception ex)
				{
					hasException=true;
					exceptionList.add(ex);
				}
			}
			
			if(hasException)
			{
				throw new CommandExecutingException(exceptionList);
			}
			
			return true;
		}
		else
		{
			assert(false);    //不应该出现这种情况
			return false;
		}
	}
	
	
	
}
