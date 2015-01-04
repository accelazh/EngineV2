package com.accela.ObjectStreams.support;

import java.util.*;

/**
 * 
 * ������ObjectInputStreamSupportʹ�ã���Ϊ�˷���
 * ObjectInputStreamSupport��¼��ȡ�Ķ����Լ�����
 * ѭ���������͵Ķ����д��
 *
 */
class ObjectInputStreamObjectRecorder
{
	/**
	 * ������¼�Ѿ�����Ķ��󡣼���һ������������id��
	 */
	private final Map<Integer, Object> objectMap=new HashMap<Integer, Object>();
	
	/**
	 * ��¼һ���Ѿ������Ķ���
	 * 
	 * @param objectId ���ö�������id��
	 * @param object ����¼�Ķ���
	 */
	public void recordObject(int objectId, Object object)
	{
		if(null==object)
		{
			throw new NullPointerException("object should not be null");
		}
		if(objectId<0)
		{
			throw new IllegalArgumentException("objectId should be non negtive");
		}
		if(containsObjectId(objectId))
		{
			throw new IllegalArgumentException("the object is already recorded, it should never be recorded more than once");
		}
		
		objectMap.put(objectId, object);
	}
	
	/**
	 * �õ�һ���Ѿ�����¼�Ķ��������
	 * @param objectId �ö��󱻷����id�� 
	 * @return ��Ӧ�Ķ�������Ҳ������׳��쳣�������Ƿ���null
	 */
	public Object getRecordedObject(int objectId)
	{
		Object result=objectMap.get(objectId);
		if(null==result)
		{
			throw new IllegalArgumentException("the object specified by objectId is not recorded");
		}
		
		return result;
	}
	
	/**
	 * ��������Ѿ���¼�Ķ���
	 */
	public void clearObjectRecord()
	{
		objectMap.clear();
	}
	
	/**
	 * �����Ƿ��¼��id��ΪobjectId�Ķ���
	 */
	public boolean containsObjectId(int objectId)
	{
		if(objectId<0)
		{
			throw new IllegalArgumentException("objectId should be non negtive");
		}
		return objectMap.containsKey(objectId);
	}

}
