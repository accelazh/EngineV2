package com.accela.MessageService.namingServer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.MessageService.shared.Name;
import com.accela.MessageService.shared.NamingUpdateOperation;

/**
 * 
 * NamingServer中，记录Name-ClientID 对应关系的数据结构。
 * 
 * 每条对应关系由一个Name对象和一个ClientID 对向组成，它们形成了一个映射表。
 * 
 */
public class NamingTable
{
	private Map<Name, ClientID> table = new ConcurrentHashMap<Name, ClientID>();

	private ReentrantLock modifyLock = new ReentrantLock();

	/**
	 * 清除所有已经记录的关系
	 */
	public void clear()
	{
		table.clear();
	}

	/**
	 * 查找指定的那么所对应的ClientID，如果找不到则返回null
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
	 * 向NamingTable加入一条记录
	 * 
	 * @param name
	 *            该记录中的name
	 * @param clientID
	 *            该记录中name对应的ClientID
	 * @return 是否成功。成功的加入要求NamingTable中此前不存在一条记录，这条记录 中的name与指定的name冲突
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
	 * 从NamingTable删除一条记录，这条记录的内容应该是指定的name对应指定的clientID。
	 * 如果NamingTable中不存在这样一条记录，则操作失败并，返回false。反之则操作成功， 并返回true。
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
	 * 根据一个NamingUpdateOperation的队列来完成一系列更新操作。
	 * 如果有某个更新操作失败了，则NamingTable自动回滚到调用这个 方法之前的状态。
	 * 
	 * @param operationQueue
	 *            指定了一系列更新的内容以及更新操作的顺序
	 * @param clientID
	 *            所有的更新操作都是针对一个host的，这个传入参数 就是这个host的ClientID
	 * @return 如果更新成功，则返回一个负数，如果更新失败，则返回引起失 败的操作的下标。
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
