package com.accela.TestCases.reflectionSupport;

import java.util.*;

import com.accela.ClassIDAndInstanceID.IDDispatcher;
import com.accela.ClassIDAndInstanceID.InstanceID;

public class ClassAB
{
	private static final List<Object> creationSet=new LinkedList<Object>();
	
	private static final Set<InstanceID> instanceIDSet=new HashSet<InstanceID>();
	
	public static void addToSet(Object object)
	{
		if(null==object)
		{
			throw new NullPointerException("object should not be null");
		}
		if(instanceIDSet.contains(IDDispatcher.createInstanceID(object)))
		{
			if(object instanceof Boolean
					||object instanceof Integer
					||object instanceof Long
					||object instanceof Enum
					||object instanceof Short
					||object instanceof Byte
					||object instanceof Character)
			{
				return;
			}
			else
			{
				throw new IllegalArgumentException("repeat object! object = "+object);
			}
		}
		
		creationSet.add(object);
		instanceIDSet.add(IDDispatcher.createInstanceID(object));
	}
	
	public static void cleanSet()
	{
		creationSet.clear();
		instanceIDSet.clear();
	}
	
	public static List<Object> getAddedObjects()
	{
		return creationSet;
	}
	
	private ClassAA head;
	
	public ClassAB()
	{
		init();
	}
	
	private void init()
	{
		head=new ClassAA();
		ClassAB.addToSet(head);
		
		ClassAA cur=head;
		
		for(int i=0;i<20;i++)
		{
			ClassAA aa=new ClassAA();
			cur.setNext(aa);
			ClassAB.addToSet(aa);
			cur=cur.getNext();
		}
		
		cur.setNext(head);
	}

	public ClassAA getHead()
	{
		return head;
	}

	public void setHead(ClassAA head)
	{
		this.head = head;
	}

}
