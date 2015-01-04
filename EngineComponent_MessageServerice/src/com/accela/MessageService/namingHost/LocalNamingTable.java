package com.accela.MessageService.namingHost;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.util.BlockingQueueWrap;
import com.accela.MessageService.shared.Name;

public class LocalNamingTable
{
	private Map<Name, BlockingQueueWrap<Object>> namingBuffers = new ConcurrentHashMap<Name, BlockingQueueWrap<Object>>();

	private int namingBufferMaxSize = 150;

	private Map<Name, BlockingQueueWrap<Object>> backup;

	private ReentrantLock modifyLock = new ReentrantLock();

	public boolean containsName(Name name)
	{
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}

		return namingBuffers.containsKey(name);
	}

	public boolean addName(Name name)
	{
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}

		modifyLock.lock();
		try
		{
			if (namingBuffers.containsKey(name))
			{
				return false;
			}

			Object old = namingBuffers.put(name,
					new BlockingQueueWrap<Object>());
			assert (null == old);

			return true;
		} finally
		{
			modifyLock.unlock();
		}
	}

	public boolean removeName(Name name)
	{
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}

		modifyLock.lock();

		try
		{
			if (!namingBuffers.containsKey(name))
			{
				return false;
			}

			Object old = namingBuffers.remove(name);
			assert (old != null);

			return true;
		} finally
		{
			modifyLock.unlock();
		}
	}

	public Set<Name> getAddedNames()
	{
		return namingBuffers.keySet();
	}

	public Object retrieveMessage(Name name) throws InterruptedException
	{
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}
		if (!namingBuffers.containsKey(name))
		{
			throw new IllegalArgumentException("name has not been added yet!");
		}

		BlockingQueueWrap<Object> queue = namingBuffers.get(name);
		assert (queue != null);

		return queue.dequeue();

	}

	public void putMessage(Name name, Object message)
			throws InterruptedException
	{
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}
		if (null == message)
		{
			throw new NullPointerException("message should not be null");
		}
		if (!namingBuffers.containsKey(name))
		{
			throw new IllegalArgumentException("name has not been added yet!");
		}

		BlockingQueueWrap<Object> queue = namingBuffers.get(name);
		assert (queue != null);

		assert (namingBufferMaxSize >= 1);
		while (queue.size() > namingBufferMaxSize - 1)
		{
			queue.dequeue();
		}

		queue.enqueue(message);

	}

	public int getMaxBufferSizeForEachName()
	{
		return namingBufferMaxSize;
	}

	public void setMaxBufferSizeForEachName(int maxSize)
	{
		if (maxSize < 1)
		{
			throw new IllegalArgumentException("maxSize should be positive");
		}

		this.namingBufferMaxSize = maxSize;
	}

	public void backup()
	{
		namingBuffers = new ConcurrentHashMap<Name, BlockingQueueWrap<Object>>(
				namingBuffers);
	}

	public void rollBack()
	{
		modifyLock.lock();
		try
		{
			this.namingBuffers = backup;
		} finally
		{
			modifyLock.unlock();
		}
	}

}
