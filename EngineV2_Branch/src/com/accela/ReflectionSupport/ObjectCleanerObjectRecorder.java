package com.accela.ReflectionSupport;

import java.util.*;

import com.accela.ClassIDAndInstanceID.IDDispatcher;
import com.accela.ClassIDAndInstanceID.InstanceID;

/**
 * 
 * ������ObjectCleanerʹ�ã�������¼�Ѿ���������Ķ���
 *
 */
class ObjectCleanerObjectRecorder
{
	/**
	 * ��¼�Ѿ�������Ķ���InstanceID����һ����������Ψһ�����ֶ���
	 */
	private Set<InstanceID> objectSet=new HashSet<InstanceID>();
	
	/**
	 * ��¼һ������
	 */
	public void recordObject(Object object)
	{
		if(null==object)
		{
			throw new NullPointerException("object should not be null");
		}
		
		InstanceID objectId=IDDispatcher.createInstanceID(object);
		assert(objectId!=null);
		
		if(objectSet.contains(objectId))
		{
			throw new IllegalArgumentException("the object is already recorded, it should never be recorded more than once");
		}
		
		putObject(objectId);
		
	}

	/**
	 * ��¼һ�����󣬽���InstanceID����objectSet
	 */
	private void putObject(InstanceID objectId)
	{
		assert(objectId!=null);
		assert(!objectSet.contains(objectId));
		
		objectSet.add(objectId);
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
		
		InstanceID objectId=IDDispatcher.createInstanceID(object);
		assert(objectId!=null);
		
		return objectSet.contains(objectId);
	}
	
	public void clearObjectRecord()
	{
		objectSet.clear();
	}

}
