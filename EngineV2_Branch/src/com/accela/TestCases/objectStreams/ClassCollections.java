package com.accela.TestCases.objectStreams;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ClassCollections
{
	private List<String> strList = new LinkedList<String>();

	private Set<Integer> intSet = new HashSet<Integer>();

	private Map<Long, String> lsMap = new ConcurrentHashMap<Long, String>();

	private List<List<String>> strListList = new LinkedList<List<String>>();

	private final transient Object transObj=new Object();
	
	private transient Object transObj2=new Object();
	
	private static Object transObj3=new Object();
	
	private static final Object transObj4=new Object();
	
	public ClassCollections()
	{
		for (int i = 0; i < 100; i++)
		{
			strList.add("" + i);
			intSet.add(2 * i);
			lsMap.put(3L * i, "" + 3 * i);

			List<String> list = new LinkedList<String>();
			for (int j = 0; j < 100; j++)
			{
				list.add("" + Math.random());
			}

			strListList.add(list);
		}
	}
	
	public boolean equals(Object obj)
	{
		ClassCollections other=(ClassCollections)obj;

		if(strList.equals(other.strList)
				&&intSet.equals(other.intSet)
				&&lsMap.equals(other.lsMap)
				&&strListList.equals(other.strListList))
		{
			return true;
		}
		
		return false;
	}
	
	public boolean isObjTransiented()
	{
		return null==transObj
		&&null==transObj2
		&&transObj3!=null
		&&transObj4!=null;
	}

}
