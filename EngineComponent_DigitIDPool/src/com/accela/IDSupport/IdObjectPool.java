package com.accela.IDSupport;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * IdObjectPool�������ӵ��id�Ķ�����IdObjectPool
 * �У����ݶ����id����Ŷ�������Ը��ݶ����id����
 * ȡ����
 *
 */
public class IdObjectPool
{
	private Map<Integer, IIdObject> table=new ConcurrentHashMap<Integer, IIdObject>();
	
	/**
	 * ����һ�����������ȥ�����������ͬid�ŵĶ������ȥ�Ķ��󽫱���������
	 * @param object ��������Ķ���
	 * @return ��ȥ��ӵ����ͬid�ŵĶ��󣬻���null���û���������
	 */
	public IIdObject put(IIdObject object)
	{
		if(null==object)
		{
			throw new NullPointerException("object should not be null");
		}
		if(object.getId()<0)
		{
			throw new IllegalArgumentException("object.getId() returns an invalid id");
		}
		return table.put(object.getId(), object);
	}
	
	/**
	 * ����id��ȡ����Ӧ�Ķ������û�ж�Ӧ�����򷵻�null��
	 * @param id ָ����id��
	 * @return ��ָ����id�����Ӧ�Ķ���
	 */
	public IIdObject retrieve(int id)
	{
		if(id<0)
		{
			throw new IllegalArgumentException("id should be non negtive");
		}
		
		return table.get(id);
	}
	
	/**
	 * �����Ƿ���ָ����id����Ӧ�Ķ���
	 * @param id ָ����id��
	 * @return �������һ�����󣬸ö����id�ž���ָ����id�ţ���ô����true�����򷵻�false
	 */
	public boolean contains(int id)
	{
		if(id<0)
		{
			throw new IllegalArgumentException("id should be non negtive");
		}
		
		return table.containsKey(id);
	}
	
	/**
	 * ������д洢�Ķ���
	 */
	public void clear()
	{
		table.clear();
	}
	

}
