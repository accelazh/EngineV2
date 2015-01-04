package com.accela.CommandExecutionCenter;

import com.accela.CommandExecutionCenter.shared.ExecutionCommand;
import com.accela.CommandExecutionCenter.shared.ExecutionCommanderID;

/**
 * ���������֧�ֶ��̣߳���
 * 
 * CommanderHandler��������ָ����ĳһ�����
 * ����Ҫָ����Ӧ�ô���������CommandHead��
 * ��������涨��Ӧ����������ĳһ������ġ�
 * 
 * �̳д������ṩ��������
 * 
 * //Inheritance needed
 */
public abstract class CommandHandler
{
	/**
	 * ���CommanderHandlerר�Ŵ��������CommandHead������ָ��
	 * ���CommanderHandler������������
	 */
	private String aimCommandHead;
	
	/**
	 * @param aimCommandHead ָ��CommandHandler����������������
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
			assert(false);    //���������Ӧ�ýӵ������Լ����������
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
