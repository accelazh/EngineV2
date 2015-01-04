package com.accela.MessageService.namingHost;

import java.util.LinkedList;
import java.util.Queue;

import com.accela.MessageService.shared.Name;
import com.accela.MessageService.shared.NamingUpdateOperation;

public class LocalNamingTableUpdater
{
	private Queue<NamingUpdateOperation> operationQueue=new LinkedList<NamingUpdateOperation>();
	
	public synchronized void addName(Name name)
	{
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		operationQueue.add(new NamingUpdateOperation(NamingUpdateOperation.ADD, name));
	}
	
	public synchronized void removeName(Name name)
	{
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		operationQueue.add(new NamingUpdateOperation(NamingUpdateOperation.REMOVE, name));
	}
	
	public synchronized int checkOperationsPerfomable(LocalNamingTable namingTable)
	{
		if(null==namingTable)
		{
			throw new NullPointerException("namingTable should not be null");
		}
		
		namingTable.backup();
		
		boolean succ = true;
		int operationIdx = 0;
		for(NamingUpdateOperation op : operationQueue)
		{
			assert(op!=null);
			
			boolean result=false;
			if (op.getUpdateType() == NamingUpdateOperation.ADD)
			{
				result = namingTable.addName(op.getUpdateName());
			} else if (op.getUpdateType() == NamingUpdateOperation.REMOVE)
			{
				result = namingTable.removeName(op.getUpdateName());
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
		
		namingTable.rollBack();
		if(!succ)
		{
			return operationIdx;
		}
		else
		{
			return -1;
		}
	}
	
	public synchronized int perforOperations(LocalNamingTable namingTable)
	{
		if(null==namingTable)
		{
			throw new NullPointerException("namingTable should not be null");
		}
		
		namingTable.backup();
		
		boolean succ = true;
		int operationIdx = 0; 
		for(NamingUpdateOperation op : operationQueue)
		{
			assert(op!=null);
			
			boolean result=false;
			if (op.getUpdateType() == NamingUpdateOperation.ADD)
			{
				result = namingTable.addName(op.getUpdateName());
			} else if (op.getUpdateType() == NamingUpdateOperation.REMOVE)
			{
				result = namingTable.removeName(op.getUpdateName());
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
		
		if(!succ)
		{
			namingTable.rollBack();
			return operationIdx;
		}
		else
		{
			return -1;
		}
	}
	
	public synchronized Queue<NamingUpdateOperation> getOperationQueue()
	{
		return new LinkedList<NamingUpdateOperation>(operationQueue);
	}
	
	public synchronized void clear()
	{
		operationQueue.clear();
	}

}
