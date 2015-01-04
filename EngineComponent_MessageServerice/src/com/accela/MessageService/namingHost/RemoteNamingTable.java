package com.accela.MessageService.namingHost;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.MessageService.shared.Name;

public class RemoteNamingTable
{
	private Map<Name, ClientID> table = new ConcurrentHashMap<Name, ClientID>();

	private ReentrantLock modifyLock = new ReentrantLock();

	public void addName(Name name, ClientID clientID)
	{
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}
		if (null == clientID)
		{
			throw new NullPointerException("clientID should not be null");
		}

		modifyLock.lock();
		try
		{
			if (table.containsKey(name))
			{
				throw new IllegalArgumentException(
						"name should not be contained before");
			}

			Object old = table.put(name, clientID);
			assert (null == old);
		} finally
		{
			modifyLock.unlock();
		}
	}

	public void clear()
	{
		modifyLock.lock();
		try
		{
			table.clear();
		} finally
		{
			modifyLock.unlock();
		}
	}

	public ClientID lookup(Name name)
	{
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}

		return table.get(name);
	}

}
