package com.accela.MessageService.shared;

import java.util.LinkedList;

import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.MessageService.messages.namingHostToNamingServer.NamingQueryMessage;
import com.accela.MessageService.messages.namingServerToNamingHost.NamingQueryReplyMessage;
import com.accela.ObjectPool.ObjectPool;

public class ConvenientObjectPool
{

	public static void deleteNamingQueryReplyMessage(
			NamingQueryReplyMessage message)
	{
		if (null == message)
		{
			throw new NullPointerException("message should not be null");
		}

		ObjectPool.put(message, false);
	}

	public static void deleteNamingQueryMessage(NamingQueryMessage message)
	{
		if (null == message)
		{
			throw new NullPointerException("message should not be null");
		}

		ObjectPool.put(message, false);
	}

	public static NamingQueryReplyMessage newNamingQueryReplyMessage()
	{
		NamingQueryReplyMessage message = ObjectPool
				.retrieve(NamingQueryReplyMessage.class);
		if (null == message)
		{
			message = new NamingQueryReplyMessage();
		} else
		{
			if (message.getQueriedClientIDs() != null)
			{
				message.getQueriedClientIDs().clear();
			} else
			{
				message.setQueriedClientIDs(new LinkedList<ClientID>());
			}
		}

		assert (message.getQueriedClientIDs().isEmpty());
		return message;
	}

	public static NamingQueryMessage newNamingQueryMessage()
	{
		NamingQueryMessage message = ObjectPool
				.retrieve(NamingQueryMessage.class);
		if (null == message)
		{
			message = new NamingQueryMessage();
		} else
		{
			if (message.getQueriedNames() != null)
			{
				message.getQueriedNames().clear();
			} else
			{
				message.setQueriedNames(new LinkedList<Name>());
			}
		}

		assert (message.getQueriedNames().isEmpty());
		return message;
	}

}
