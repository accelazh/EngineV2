package com.accela.MessageService.namingServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.shared.ClientID;

/**
 * 
 * ÿ��Host�������һ������Host�������ӵ��Լ���ClientID���������ClientID
 * ���������Host���ӵ�NamingServer���õ�ClientID�����NamingServer��Ҫ��
 * ¼ÿ��Host���ӵ�NamingServerʱ���õ�ClientID���Լ����Host���������� Host��ClientID���Լ����ߵĶ�Ӧ��ϵ��
 * 
 */
public class HostTable
{
	/**
	 * ӳ���У�����host���ӵ�NamingServer��ʹ�õ�ClientID�� ֵ��host���Ÿ�����host��ClientID
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
