package com.accela.MessageService.messages.namingHostToNamingServer;

import java.util.*;

import com.accela.MessageService.shared.Name;

/**
 * 
 * NamingHost��NamingServerѯ��һ��Name�����Ӧ��ClientID��ʲô��ʱ�򣬻�
 * ��NamingServer���������Ϣ
 *
 */
public class NamingQueryMessage implements Iterable<Name>
{
	/**
	 * NamingHost��ѯ�ʵ�Name�������ж�������ʹ�ñ�
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
