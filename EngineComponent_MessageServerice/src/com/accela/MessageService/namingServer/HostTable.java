package com.accela.MessageService.namingServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.shared.ClientID;

/**
 * 
 * 每个Host都会给出一个其它Host可以连接到自己的ClientID。但是这个ClientID
 * 并不是这个Host连接到NamingServer所用的ClientID。因此NamingServer需要记
 * 录每个Host连接到NamingServer时所用的ClientID，以及这个Host开发给其它 Host的ClientID，以及两者的对应关系。
 * 
 */
public class HostTable
{
	/**
	 * 映射中，键是host连接到NamingServer所使用的ClientID； 值是host开放给其他host的ClientID
	 */
	private Map<ClientID, ClientID> table = new ConcurrentHashMap<ClientID, ClientID>();

	private ReentrantLock modifyLock = new ReentrantLock();

	public boolean add(ClientID hostToServer, ClientID hostToHost)
	{
		if (null == hostToServer)
		{
			throw new IllegalArgumentException(
					"hostToServer should not be null");
		}
		if (null == hostToHost)
		{
			throw new IllegalArgumentException("hostToHost should not be null");
		}

		modifyLock.lock();

		try
		{
			if (table.containsKey(hostToServer))
			{
				return false;
			}

			table.put(hostToServer, hostToHost);

			return true;
		} finally
		{
			modifyLock.unlock();
		}

	}

	public boolean remove(ClientID hostToServer)
	{
		if (null == hostToServer)
		{
			throw new IllegalArgumentException(
					"hostToServer should not be null");
		}

		modifyLock.lock();
		try
		{
			if (!table.containsKey(hostToServer))
			{
				return false;
			}

			table.remove(hostToServer);

			return true;
		} finally
		{
			modifyLock.unlock();
		}
	}

	public ClientID lookup(ClientID hostToServer)
	{
		if (null == hostToServer)
		{
			throw new IllegalArgumentException(
					"hostToServer should not be null");
		}
		
		return table.get(hostToServer);
	}
	
	public void clear()
	{
		table.clear();
	}

}
