package com.accela.MessageService.messages.namingHostToNamingServer;

import java.util.*;

import com.accela.MessageService.shared.NamingUpdateOperation;

/**
 * 
 * ��NamingHost�������Լ���ע���Name��ʱ�򣬾ͻ���NamingServer���������Ϣ��
 * ֪ͨ��ҲӦ����Ӧ�����������
 * 
 * NamingHost���Զ�Name���ı���У�
 * 1��NamingHost���԰��Լ�ע���Nameɾ��
 * 2��NamingHost����Ϊ�Լ������µ�Name
 * 
 * ���⣬NamingServer����һ��NamingUpdateMessage���µ�ʱ�����ʧ�ܣ�
 * ���ع�����ʼ����֮ǰ��״̬
 *
 */
public class NamingUpdateMessage
{
	/**
	 * ��¼NamingHost������ı������Ϊ������в���ģ�����ö���
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
