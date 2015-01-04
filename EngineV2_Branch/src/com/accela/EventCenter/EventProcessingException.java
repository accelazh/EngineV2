package com.accela.EventCenter;

import java.util.*;

import com.accela.EventCenter.shared.IConstants;

/**
 * 
 * EventCenter在处理事件的时候，如果发生异常，将抛出这个类。
 * 你可以调用getCauseList()方法来查看引起异常的原因
 *
 */
public class EventProcessingException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;
	
	private List<Throwable> causeList=new LinkedList<Throwable>(); 
	
	public EventProcessingException()
	{
		super();
	}
	
	public EventProcessingException(String message)
	{
		super(message);
	}
	
	public EventProcessingException(List<Throwable> causes)
	{
		super();
		carryInCauses(causes);
	}
	
	public EventProcessingException(String message, List<Throwable> causes)
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
