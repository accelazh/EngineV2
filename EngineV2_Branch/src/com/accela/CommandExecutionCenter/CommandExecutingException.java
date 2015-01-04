package com.accela.CommandExecutionCenter;

import java.util.*;

import com.accela.CommandExecutionCenter.shared.IConstants;


/**
 * 
 * CommandExecutor��ִ�������ʱ����������쳣�����׳�����ࡣ
 * ����Ե���getCauseList()�������鿴�����쳣��ԭ��
 *
 */
public class CommandExecutingException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;
	
	private List<Throwable> causeList=new LinkedList<Throwable>(); 
	
	public CommandExecutingException()
	{
		super();
	}
	
	public CommandExecutingException(String message)
	{
		super(message);
	}
	
	public CommandExecutingException(List<Throwable> causes)
	{
		super();
		carryInCauses(causes);
	}
	
	public CommandExecutingException(String message, List<Throwable> causes)
	{
		super(message);
		carryInCauses(causes);
	}
	
	private void carryInCauses(List<Throwable> causes)
	{
		if(null==causes)
		{
			return;
		}
		
		for(Throwable cause : causes)
		{
			if(null==cause)
			{
				continue;
			}
			
			causeList.add(cause);
		}
		
		
	}

	public List<Throwable> getCauseList()
	{
		return causeList;
	}
	
    

}
