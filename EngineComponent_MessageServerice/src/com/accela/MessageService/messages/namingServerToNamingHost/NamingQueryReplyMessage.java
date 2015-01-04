package com.accela.MessageService.messages.namingServerToNamingHost;

import java.util.*;

import com.accela.ConnectionCenter.shared.ClientID;

/**
 * 
 * NamingServer�յ�һ��NamingHost������NamingQueryMessage�������ظ�������Ϣ
 *
 */
public class NamingQueryReplyMessage implements Iterable<ClientID>
{
	/**
	 * ��NamingHost���͹�����NamingQueryMessage�е�queriedNames˳���Ӧ�ģ�
	 * ���ҵ���ClientID���б����ĳһ��λ������null�����ʾû���ҵ�
	 */
	private List<ClientID> queriedClientIDs;
	
	public NamingQueryReplyMessage()
	{
		queriedClientIDs=new LinkedList<ClientID>();
	}
	
	/**
	 * Note: �����null�����ʾû���ҵ����Ӧ��ClientID 
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
