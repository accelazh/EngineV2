package com.accela.AuthorityCenter.ruleFilter;

import java.util.LinkedList;
import java.util.List;

import com.accela.AuthorityCenter.shared.IConstants;

public class AuthorityFilteringException extends Exception
{
	private static final long serialVersionUID = IConstants.SERIAL_VERSION_UID;
	
	private List<Throwable> causeList=new LinkedList<Throwable>(); 
	
	public AuthorityFilteringException()
	{
		super();
	}
	
	public AuthorityFilteringException(String message)
	{
		super(message);
	}
	
	public AuthorityFilteringException(List<Throwable> causes)
	{
		super();
		carryInCauses(causes);
	}
	
	public AuthorityFilteringException(String message, List<Throwable> causes)
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
