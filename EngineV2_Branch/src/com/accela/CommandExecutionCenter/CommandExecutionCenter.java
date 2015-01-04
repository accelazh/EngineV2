package com.accela.CommandExecutionCenter;

import java.util.*;

import com.accela.CommandExecutionCenter.shared.ExecutionCommand;

/**
 * CommandExecutionCenterģ������
 * 
 * ����ִ�����ģ�֧�ֶ��̣߳���
 * ����ִ����������ִ���������ִ���������ж��CommandHandler����һ�������
 * ������ִ�������е�ʱ������Ѱ��ƥ���CommandHander�������������
 * 
 */
public class CommandExecutionCenter
implements ICommandExecutor, ICommandExecutionCenterConfigurer
{
	/**
	 * װ��CommandHandler��������֧��ͬ�������õ���CommandHead��ֵ�õ���
	 * CommandHandler��������Ϊ����ͬһ�������CommandHandler�����ж��
	 */
	private Map<String, List<CommandHandler>> commandHandlerHolder=new HashMap<String, List<CommandHandler>>();
    
	public CommandExecutionCenter()
	{
		
	}
	
	/**
	 * ���һ��CommandHandler��
	 * ����ͬ�������ComandHandler�����ж������ִ����������ʱ�����ǻ�
	 * ���ռ�����Ⱥ�˳��ִ��
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
	 * ɾ�����ڴ���comandHead�����������CommandHandler
	 * @param commandHead ��ɾ����CommandHandler����������CommandHead
	 * @return ��ɾ����CommandHandler�����û�У��򷵻�null
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
			assert(false);    //��Ӧ�ó����������
			return list.toArray(new CommandHandler[0]);
		}
		
	}
	
	/**
	 * ִ���������ƥ���CommandHandler���ᱻִ�У�ִ�е�
	 * ˳�������Ǽ����˳����ͬ��
	 * ��νƥ�䣬��ָ
	 * CommandHandler.getAimCommandHead().equals(Command.getCommandHead())==true
	 * 
	 * @param command ��ִ�е�����
	 * @return �Ƿ�������һ��CommandHandler�����˴��������
	 * @throws CommandExecutingException ��ִ�������ʱ����������쳣������¼�쳣
	 * ��������������CommandHandler��������CommandHandler��������Ϻ󣬲Ż��׳��쳣
	 * CommandExecutingException��ִ�������ʱ�������쳣�ᱻ��¼���쳣CommandExecutingException�С�
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
			assert(false);    //��Ӧ�ó����������
			return false;
		}
	}
	
	
	
}
