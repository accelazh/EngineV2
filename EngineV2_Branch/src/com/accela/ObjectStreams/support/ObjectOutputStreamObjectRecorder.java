package com.accela.ObjectStreams.support;

import java.util.*;

import com.accela.ClassIDAndInstanceID.*;

public class ObjectOutputStreamObjectRecorder
{
	/**
	 * ��¼�Ѿ�д��Ķ���InstanceID����һ����������Ψһ�����ֶ���
	 * �������objectIdentifier����Integer��ʾ�������������id��
	 */
	private Map<InstanceID, Integer> objectMap=new HashMap<InstanceID, Integer>();
	
	/**
	 * ��¼һ������
	 */
	public void recordObject(Object object)
	{
		if(null==object)
		{
			throw new NullPointerException("object should not be null");
		}
		
		InstanceID objectIdentifier=IDDispatcher.createInstanceID(object);
		assert(objectIdentifier!=null);
		if(objectMap.containsKey(objectIdentifier))
		{
			throw new IllegalArgumentException("the object is already recorded, it should never be recorded more than once");
		}
		
		putObject(objectIdentifier);
		
	}

	/**
	 * ��¼һ�����󣬽���InstanceID����objectMap��ʹ�ô�ʱobjectMap
	 * �Ĵ�С��Ϊ��id��
	 */
	private void putObject(InstanceID objectIdentifier)
	{
		assert(objectIdentifier!=null);
		assert(!objectMap.containsKey(objectIdentifier));
		
		objectMap.put(objectIdentifier, objectMap.size());
	}
	
	/**
	 * @return �Ƿ��Ѿ���¼��ָ����object
	 */
	public boolean hasRecordOf(Object object)
	{
		if(null==object)
		{
			throw new NullPointerException("object should not be null");
		}
		
		InstanceID objectIdentifier=IDDispatcher.createInstanceID(object);
		assert(objectIdentifier!=null);
		
		return objectMap.containsKey(objectIdentifier);
	}
	
	/**
	 * �õ�һ���Ѿ���¼���Ķ����id��
	 */
	public int getObjectId(Object object)
	{
		if(null==object)
		{
			throw new NullPointerException("object should not be null");
		}
		
		InstanceID objectIdentifier=IDDispatcher.createInstanceID(object);
		assert(objectIdentifier!=null);
		if(!objectMap.containsKey(objectIdentifier))
		{
			throw new IllegalArgumentException("the object have not been recorded yet!");
		}
		
		Integer id=objectMap.get(objectIdentifier);
		assert(id!=null);
		
		return id;
	}

	public void clearObjectRecord()
	{
		objectMap.clear();
	}

}
