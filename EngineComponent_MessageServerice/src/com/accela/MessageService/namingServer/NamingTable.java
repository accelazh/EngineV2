package com.accela.MessageService.namingServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.MessageService.shared.Name;
import com.accela.MessageService.shared.NamingUpdateOperation;

/**
 * 
 * NamingServer�У���¼Name-ClientID ��Ӧ��ϵ�����ݽṹ��
 * 
 * ÿ����Ӧ��ϵ��һ��Name�����һ��ClientID ������ɣ������γ���һ��ӳ���
 * 
 */
public class NamingTable
{
	private Map<Name, ClientID> table = new ConcurrentHashMap<Name, ClientID>();

	private ReentrantLock modifyLock = new ReentrantLock();

	/**
	 * ��������Ѿ���¼�Ĺ�ϵ
	 */
	public void clear()
	{
		table.clear();
	}

	/**
	 * ����ָ������ô����Ӧ��ClientID������Ҳ����򷵻�null
	 */
	public ClientID lookup(Name name)
	{
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}

		return table.get(name);
	}

	/**
	 * ��NamingTable����һ����¼
	 * 
	 * @param name
	 *            �ü�¼�е�name
	 * @param clientID
	 *            �ü�¼��name��Ӧ��ClientID
	 * @return �Ƿ�ɹ����ɹ��ļ���Ҫ��NamingTable�д�ǰ������һ����¼��������¼ �е�name��ָ����name��ͻ
	 */
	public boolean add(Name name, ClientID clientID)
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
				return false;
			}

			Object result = table.put(name, clientID);
			assert (null == result);

			return true;
		} finally
		{
			modifyLock.unlock();
		}
	}

	/**
	 * ��NamingTableɾ��һ����¼��������¼������Ӧ����ָ����name��Ӧָ����clientID��
	 * ���NamingTable�в���������һ����¼�������ʧ�ܲ�������false����֮������ɹ��� ������true��
	 */
	public boolean remove(Name name, ClientID clientID)
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
			if (table.get(name) == null || !table.get(name).equals(clientID))
			{
				return false;
			}

			Object old = table.remove(name);
			assert (old != null);

			return true;
		} finally
		{
			modifyLock.unlock();
		}
	}

	/**
	 * ����һ��NamingUpdateOperation�Ķ��������һϵ�и��²�����
	 * �����ĳ�����²���ʧ���ˣ���NamingTable�Զ��ع���������� ����֮ǰ��״̬��
	 * 
	 * @param operationQueue
	 *            ָ����һϵ�и��µ������Լ����²�����˳��
	 * @param clientID
	 *            ���еĸ��²����������һ��host�ģ����������� �������host��ClientID
	 * @return ������³ɹ����򷵻�һ���������������ʧ�ܣ��򷵻�����ʧ �ܵĲ������±ꡣ
	 */
	public int update(Queue<NamingUpdateOperation> operationQueue,
			ClientID clientID)
	{
		if (null == operationQueue)
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
			Map<Name, ClientID> backupTable = cloneTable();

			boolean succ = true;
			int operationIdx = 0;
			while (!operationQueue.isEmpty())
			{
				NamingUpdateOperation op = operationQueue.poll();
				if (null == op)
				{
					continue;
				}

				boolean result = false;
				if (op.getUpdateType() == NamingUpdateOperation.ADD)
				{
					result = add(op.getUpdateName(), clientID);
				} else if (op.getUpdateType() == NamingUpdateOperation.REMOVE)
				{
					result = remove(op.getUpdateName(), clientID);
				} else
				{
					result = false;
					assert (false);
				}

				if (!result)
				{
					succ = false;
					break;
				}

				operationIdx++;
			}

			if (!succ)
			{
				table = backupTable;
				return operationIdx;
			} else
			{
				return -1;
			}
		} finally
		{
			modifyLock.unlock();
		}
	}

	public Set<ClientID> valueSet()
	{
		Set<ClientID> values = new HashSet<ClientID>(table.values());
		return values;
	}
	
	public Set<Name> keySet()
	{
		return table.keySet();
	}

	@SuppressWarnings("unchecked")
	private Map<Name, ClientID> cloneTable()
	{
		Map<Name, ClientID> newTable = new ConcurrentHashMap(table);

		return newTable;
	}

	public void removeClients(Set<ClientID> toBeRemoved)
	{
		if (null == toBeRemoved)
		{
			throw new NullPointerException("toBeRemoved should not be null");
		}

		modifyLock.lock();

		try
		{
			for (Name name : table.keySet())
			{
				assert (name != null);
				assert (table.get(name) != null);

				if (toBeRemoved.contains(table.get(name)))
				{
					ClientID old = table.remove(name);
					assert (old != null);
				}
			}
		} finally
		{
			modifyLock.unlock();
		}
	}

}
