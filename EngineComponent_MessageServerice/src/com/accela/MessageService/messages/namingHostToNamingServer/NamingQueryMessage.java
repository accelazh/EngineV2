package com.accela.MessageService.messages.namingHostToNamingServer;

import java.util.*;

import com.accela.MessageService.shared.Name;

/**
 * 
 * NamingHost向NamingServer询问一个Name对象对应的ClientID是什么的时候，会
 * 向NamingServer发送这个信息
 *
 */
public class NamingQueryMessage implements Iterable<Name>
{
	/**
	 * NamingHost所询问的Name，可以有多个，因此使用表
	 */
	private List<Name> queriedNames;
	
	public NamingQueryMessage()
	{
		queriedNames=new LinkedList<Name>();
	}
	
	public void addQueriedName(Name name)
	{
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		queriedNames.add(name);
	}

	@Override
	public Iterator<Name> iterator()
	{
		assert(queriedNames!=null);
		return queriedNames.iterator();
	}

	public List<Name> getQueriedNames()
	{
		return queriedNames;
	}

	public void setQueriedNames(List<Name> queriedNames)
	{
		this.queriedNames = queriedNames;
	}
	
	
	
	

}
