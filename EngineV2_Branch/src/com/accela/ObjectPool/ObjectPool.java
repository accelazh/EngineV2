package com.accela.ObjectPool;

import java.util.*;

import com.accela.ClassIDAndInstanceID.IDDispatcher;
import com.accela.ClassIDAndInstanceID.InstanceID;
import com.accela.ReflectionSupport.ObjectCleaner;

/**
 * 
 * ����ء� �������Ϊ�˱����ڸ�Ƶ�ʵش����½���ɾ�������ʱ�򣬲��� ��JVM�������������ģ��Լ�ʹ�ڴ��г��ִ�����Ƭ��
 * 
 * ����ص���Ҫ˼���ǣ��½��Ķ����ڲ��õ�ʱ�򣬿��Էŵ�������У� �������ͷŸ�����������������Ҫ�½������ʱ���ٴӶ��������ȡ��
 * 
 * ObjectPool�Ϳ��Ժͽӿ�IObjectPoolSensitive���ʹ�ã���� IObjectPoolSensitive
 * 
 */
public class ObjectPool
{
	/**
	 * ��ű��ͷŵĶ��������
	 */
	private static final Map<Class<?>, Stack<Object>> objectHolder = new HashMap<Class<?>, Stack<Object>>();

	private static final Set<InstanceID> instanceIDSet = new HashSet<InstanceID>();

	private static final ObjectPoolObjectCleaner objectCleaner=new ObjectPoolObjectCleaner();
	
	private ObjectPool()
	{
		throw new IllegalStateException(
				"You should never create an instance of ObjectPool");
	}

	private static boolean hasRecordOf(Object object)
	{
		assert (object != null);

		return instanceIDSet.contains(IDDispatcher.createInstanceID(object));
	}

	private static void recordObject(Object object)
	{
		assert (object != null);
		assert (!hasRecordOf(object));

		boolean result = instanceIDSet.add(IDDispatcher
				.createInstanceID(object));
		assert (result);
	}

	private static void disrecordObject(Object object)
	{
		assert (object != null);
		assert (hasRecordOf(object));

		boolean result = instanceIDSet.remove(IDDispatcher
				.createInstanceID(object));
		assert (result);
	}

	/**
	 * ͬput(object, false);
	 */
	public static void put(Object object)
	{
		put(object, false);
	}

	/**
	 * ��һ������������أ����������������顢�ַ�����ö�ٻ��װ���ͣ�
	 * ��ô����������������ǣ���Ϊ����ز����������Щ���͵Ķ���
	 * 
	 * @param object ���������صĶ���
	 * @param fullyClean �Ƿ�����������ĳ�Ա�����������false��
	 * ��ֱ�ӰѶ���������أ������Ѵ���Ķ���ķ�static�ҷ�final
	 * �������ͳ�Ա����ȫ����Ϊnull�����Ұѱ�������������õĶ���Ҳ��
	 * put(object, true)�����������ء�
	 * 
	 * ��������ܹ��Ը�ѭ���������͵ı���
	 * 
	 */
	public static void put(Object object, boolean fullyClean)
	{
		if (null == object)
		{
			throw new NullPointerException("object should not be null");
		}
		// �����Ƿ�������ظ�����
		if (hasRecordOf(object))
		{
			throw new IllegalArgumentException(
				"You should never put one object into object pool twice");
		}

		putImpl(object, fullyClean);
		
	}

	// ////////////////////////////////////////////////////////////////////////

	/**
	 * ���帺����η������ķ���
	 */
	private synchronized static boolean putImpl(Object object,
			boolean fullyClean)
	{
		assert (object != null);

		if(fullyClean)
		{
			List<Object> cleanedObjects=objectCleaner.cleanObject(object);
			assert(cleanedObjects!=null);
			assert(cleanedObjects.contains(object));
			
			boolean succ=true;
			for(Object element : cleanedObjects)
			{
				assert(element!=null);
				if(element!=null)
				{
					if(!putAnObject(element))
					{
						succ=false;
					}
				}
			}
			
			return succ;
		}
		else
		{
			if (!hasRecordOf(object))
			{
				if (object instanceof IObjectPoolSensitive)
				{
					((IObjectPoolSensitive) object).onPut();
				}
			}
			
			return putAnObject(object);
		}
	}
	
	/**
	 * �������һ������
	 */
	private static boolean putAnObject(Object object)
	{
		assert(object!=null);
		
		if (hasRecordOf(object))
		{
			return false;
		}
		
		if(object.getClass().isArray()
				||object.getClass().isEnum()
				||(object.getClass() == Integer.class)
				||(object.getClass() == Boolean.class)
				||(object.getClass() == Double.class)
				||(object.getClass() == Float.class)
				||(object.getClass() == Long.class)
				||(object.getClass() == Character.class)
				||(object.getClass() == Short.class)
				||(object.getClass() == Byte.class)
				||(object.getClass() == String.class))
		{
			return true;
		}
		
		// ������װ�����
		Stack<Object> list = objectHolder.get(object.getClass());
		if (null == list)
		{
			list = new Stack<Object>();
			list.add(object);
			objectHolder.put(object.getClass(), list);
		} else
		{
			list.add(object);
		}
		
		// ��¼�����Ѿ�װ�ع�
		recordObject(object);
		
		return true;
		
	}

	private static class ObjectPoolObjectCleaner extends ObjectCleaner
	{
		@Override
		protected void onClean(Object object)
		{
			super.onClean(object);
			assert(object!=null);
			
			if (ObjectPool.hasRecordOf(object))
			{
				return;
			}
			
			if (object instanceof IObjectPoolSensitive)
			{
				((IObjectPoolSensitive) object).onPut();
			}
			
		}
		
	}

	// ////////////////////////////////////////////////////////////////////////

	@SuppressWarnings("unchecked")
	public static <T> T retrieve(Class<T> objectClass)
	{
		if (null == objectClass)
		{
			throw new NullPointerException("objectClass should not be null");
		}

		Object result = retrieveImpl((Class<?>) objectClass);

		T typedResult = null;

		if (result != null)
		{
			try
			{
				typedResult = (T) result;
			} catch (Exception ex)
			{
				ex.printStackTrace();
				assert (false);
			}
		} else
		{
			typedResult = null;
		}

		return typedResult;
	}

	private synchronized static Object retrieveImpl(Class<?> objectClass)
	{
		assert (objectClass != null);

		Stack<Object> list = objectHolder.get(objectClass);
		if (null == list)
		{
			return null;
		} else
		{
			if (!list.isEmpty())
			{
				assert (list.size() > 0);

				Object result = list.pop();

				assert (result != null);
				assert (hasRecordOf(result));

				disrecordObject(result);

				if (result instanceof IObjectPoolSensitive)
				{
					((IObjectPoolSensitive) result).onRetrieve();
				}

				assert (!hasRecordOf(result));

				return result;
			} else
			{
				return null;
			}
		}

	}

}
