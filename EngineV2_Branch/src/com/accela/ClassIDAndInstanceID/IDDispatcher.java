package com.accela.ClassIDAndInstanceID;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

import com.accela.ObjectPool.ObjectPool;

/**
 * 
 * ClassID��InstanceIDӦ��ͨ��IDDispatcher�������� IDDispatcher��֤
 * 1����ÿһ����ͬ������������ClassID������һ���� 2����ÿһ����ͬ�Ķ�����������InstanceID������һ��������Ĳ�ͬ��ָ��==��֤��ͬ��
 * ��==��֤��ͬ��������equals������֤��ȵĶ��󣬿��Խ�����ͬ��InstanceID�������� ������InstanceID��ͬ
 * 
 * IDDispatcher֧�ֶ��߳�
 */
public class IDDispatcher
{
	/**
	 * ��¼�Ѿ���������ClassID
	 */
	private static Map<Class<?>, ClassID> classIDMap = new HashMap<Class<?>, ClassID>();

	/**
	 * ��¼�Ѿ���������InstanceID
	 */
	private static Map<Integer, List<InstanceID>> instanceIDMap = new HashMap<Integer, List<InstanceID>>();

	private static ReentrantLock classIDLock = new ReentrantLock();

	private static ReentrantLock instanceIDLock = new ReentrantLock();

	/**
	 * ����ClassID
	 * 
	 * @param aimClass
	 *            �½���ClassID��ָ���Ŀ����
	 */
	public static ClassID createClassID(Class<?> aimClass)
	{
		if (null == aimClass)
		{
			throw new NullPointerException("aimClass should not be null");
		}

		classIDLock.lock();

		try
		{
			ClassID classID = findOldClassID(aimClass);
			if (null == classID)
			{
				classID = new ClassID(aimClass);
				recordClassID(aimClass, classID);
			}

			assert (classID != null);
			return classID;
		} finally
		{
			classIDLock.unlock();
		}
	}

	/**
	 * ����ClassID
	 * 
	 * @param aimClass
	 *            �½���ClassID��ָ���Ŀ���������
	 * @throws ClassNotFoundException
	 *             ���������ָ�����ಢ������
	 */
	public static ClassID createClassID(String className)
			throws ClassNotFoundException
	{
		if (null == className)
		{
			throw new NullPointerException("className should not be null");
		}

		return createClassID(Class.forName(className));
	}

	/**
	 * �½�InstanceID
	 * 
	 * @param aimInstance
	 *            �½���InstanceID��ָ��Ķ���
	 */
	public static InstanceID createInstanceID(Object aimInstance)
	{
		if (null == aimInstance)
		{
			throw new NullPointerException("aimInstance should not be null");
		}

		instanceIDLock.lock();
		try
		{

			InstanceID instanceID = findOldInstanceID(aimInstance);
			if (null == instanceID)
			{
				instanceID = new InstanceID(aimInstance);
				recordInstanceID(aimInstance, instanceID);

				/*
				 * assert(instanceID.isValid());
				 * 
				 * ������������������ģ������ҷ��ֵ���������ʱ������ for (int i = 0; i <
				 * 100000000;i++) { IDDispatcher.createInstanceID(newObject());
				 * } ��ʱ��ͻ���������Ų飬Ҳû���׼ȷԭ�򣬾ͺ�����JVM �ڻ���ǿ���õ�ʱ��ͳԵ���instanceIDָ��Ķ���
				 */
			}

			assert (instanceID != null);
			return instanceID;
		} finally
		{
			instanceIDLock.unlock();
		}
	}

	private static ClassID findOldClassID(Class<?> aimClass)
	{
		assert (aimClass != null);

		ClassID classID = classIDMap.get(aimClass);
		assert (null == classID || classID.isIDOf(aimClass));

		return classID;
	}

	private static void recordClassID(Class<?> aimClass, ClassID classID)
	{
		assert (aimClass != null);
		assert (classID != null);
		assert (classID.isIDOf(aimClass));

		ClassID lastVal = classIDMap.put(aimClass, classID);
		assert (null == lastVal);
	}

	private static InstanceID findOldInstanceID(Object aimInstance)
	{
		assert (aimInstance != null);

		List<InstanceID> list = instanceIDMap.get(getInstanceCode(aimInstance));
		if (null == list)
		{
			return null;
		} else
		{
			assert (list.size() > 0);

			boolean meetInvalidID = false;
			InstanceID result = null;
			for (InstanceID id : list)
			{
				assert (list != null);

				if (id.isValid())
				{
					if (id.isIDOf(aimInstance))
					{
						result = id;
						break;
					}
				} else
				{
					meetInvalidID = true;
				}
			}

			// ����instanceIDMap()
			if (meetInvalidID)
			{
				cleanInstanceIDMap();
			}

			return result;
		}
	}

	private static void recordInstanceID(Object aimInstance,
			InstanceID instanceID)
	{
		assert (aimInstance != null);
		assert (instanceID != null);
		assert (instanceID.isIDOf(aimInstance));

		List<InstanceID> list = instanceIDMap.get(getInstanceCode(aimInstance));
		if (null == list)
		{
			list = new LinkedList<InstanceID>();
			list.add(instanceID);
			instanceIDMap.put(getInstanceCode(aimInstance), list);
		} else
		{
			assert (list.size() > 0);
			assert (!list.contains(instanceID));
			list.add(instanceID);
		}
	}

	private static int getInstanceCode(Object instance)
	{
		assert (instance != null);
		return System.identityHashCode(instance);
	}

	/**
	 * ���������������instanceIDMap�����������Ч��instanceID
	 */
	@SuppressWarnings("unchecked")
	private static void cleanInstanceIDMap()
	{
		List<Integer> keysToBeRemoved = (LinkedList<Integer>) ObjectPool
				.retrieve(LinkedList.class);
		if (null == keysToBeRemoved)
		{
			keysToBeRemoved = new LinkedList<Integer>();
		} else
		{
			keysToBeRemoved.clear();
		}

		for (Integer key : instanceIDMap.keySet())
		{
			assert (key != null);
			List<InstanceID> list = instanceIDMap.get(key);

			assert (list != null);
			boolean meetInvalidID = false;
			do
			{
				meetInvalidID = false;

				int index = 0;
				for (InstanceID id : list)
				{
					assert (id != null);

					if (!id.isValid())
					{
						InstanceID removedID = list.remove(index);
						assert (!removedID.isValid());
						meetInvalidID = true;
						break;
					}

					index++;
				}

			} while (meetInvalidID);

			if (list.size() <= 0)
			{
				keysToBeRemoved.add(key);
			}

		}

		for (Integer key : keysToBeRemoved)
		{
			assert (key != null);
			List<InstanceID> removedList = instanceIDMap.remove(key);
			assert (removedList != null);
			assert (removedList.size() == 0);
		}

		ObjectPool.put(keysToBeRemoved);
		
		////////////////////////////////////////////////////////////
		
		//��ȷ��У��
		/*for(List<InstanceID> list : instanceIDMap.values())
		{
			assert(list!=null);
			assert(list.size()>0);
		}*/

	}

}
