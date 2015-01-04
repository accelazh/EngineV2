package com.accela.MessageService.messages.namingHostToNamingServer;

import java.util.*;

import com.accela.MessageService.shared.NamingUpdateOperation;

/**
 * 
 * 当NamingHost更新了自己所注册的Name的时候，就会像NamingServer发出这个信息，
 * 通知它也应当相应地作出变更。
 * 
 * NamingHost可以对Name作的变更有：
 * 1、NamingHost可以把自己注册的Name删除
 * 2、NamingHost可以为自己申请新的Name
 * 
 * 另外，NamingServer按照一个NamingUpdateMessage更新的时候，如果失败，
 * 则会回滚到开始更新之前的状态
 *
 */
public class NamingUpdateMessage
{
	/**
	 * 记录NamingHost所提出的变更，因为变更是有步骤的，因此用队列
	 */
	private Queue<NamingUpdateOperation> operationQueue;
	
	public NamingUpdateMessage()
	{
		operationQueue=new LinkedList<NamingUpdateOperation>();
	}
	
	public void enqueueOperation(NamingUpdateOperation op)
	{
		if(null==op)
		{
			throw new NullPointerException("op should not be null");
		}
		
		operationQueue.add(op);
	}
	
	public NamingUpdateOperation dequeueUpdateOperation()
	{
		return operationQueue.poll();
	}
	
	public Queue<NamingUpdateOperation> getOperationQueue()
	{
		return operationQueue;
	}
	
	public boolean isEmpty()
	{
		return operationQueue.isEmpty();
	}

	public void setOperationQueue(Queue<NamingUpdateOperation> operationQueue)
	{
		this.operationQueue = operationQueue;
	}
	
}
