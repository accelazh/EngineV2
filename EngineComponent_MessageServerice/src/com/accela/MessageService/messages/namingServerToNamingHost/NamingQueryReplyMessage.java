package com.accela.MessageService.messages.namingServerToNamingHost;

import java.util.*;

import com.accela.ConnectionCenter.shared.ClientID;

/**
 * 
 * NamingServer收到一个NamingHost发出的NamingQueryMessage后，用来回复它的消息
 *
 */
public class NamingQueryReplyMessage implements Iterable<ClientID>
{
	/**
	 * 与NamingHost发送过来的NamingQueryMessage中的queriedNames顺序对应的，
	 * 查找到的ClientID的列表，如果某一个位子上是null，则表示没有找到
	 */
	private List<ClientID> queriedClientIDs;
	
	public NamingQueryReplyMessage()
	{
		queriedClientIDs=new LinkedList<ClientID>();
	}
	
	/**
	 * Note: 如果是null，则表示没有找到相对应的ClientID 
	 */
	public void addQueriedClientID(ClientID clientID)
	{
		queriedClientIDs.add(clientID);
	}

	@Override
	public Iterator<ClientID> iterator()
	{
		assert(queriedClientIDs!=null);
		return queriedClientIDs.iterator();
	}

	public List<ClientID> getQueriedClientIDs()
	{
		return queriedClientIDs;
	}

	public void setQueriedClientIDs(List<ClientID> queriedClientIDs)
	{
		this.queriedClientIDs = queriedClientIDs;
	}
	

}
