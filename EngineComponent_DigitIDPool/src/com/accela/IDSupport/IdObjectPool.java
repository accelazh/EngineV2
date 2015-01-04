package com.accela.IDSupport;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * IdObjectPool用来存放拥有id的对象。在IdObjectPool
 * 中，根据对象的id来存放对象，你可以根据对象的id来存
 * 取对象。
 *
 */
public class IdObjectPool
{
	private Map<Integer, IIdObject> table=new ConcurrentHashMap<Integer, IIdObject>();
	
	/**
	 * 放入一个对象。如果过去曾经放入过相同id号的对象，则过去的对象将被换出来。
	 * @param object 将被放入的对像
	 * @return 过去的拥有相同id号的对象，或者null如果没有这个对象
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
	 * 按照id号取出相应的对象。如果没有对应对象则返回null。
	 * @param id 指定的id号
	 * @return 和指定的id号相对应的对象
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
	 * 测试是否含有指定的id所对应的对象
	 * @param id 指定的id号
	 * @return 如果含有一个对象，该对象的id号就是指定的id号，那么返回true；否则返回false
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
	 * 清空所有存储的对像
	 */
	public void clear()
	{
		table.clear();
	}
	

}
