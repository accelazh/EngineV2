package com.accela.EventCenter;

import java.util.*;

import com.accela.EventCenter.shared.IConstants;

/**
 * 
 * EventCenter�ڴ����¼���ʱ����������쳣�����׳�����ࡣ
 * ����Ե���getCauseList()�������鿴�����쳣��ԭ��
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
