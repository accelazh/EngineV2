package com.accela.Container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.accela.SynchronizeSupport.standard.ConfigViewSupport;

/**
 * 
 * ����(Container)�� ���������洢���󣬱��洢�Ķ������Item������Ը����洢�Ķ�����
 * ������(name)����ϣ����������ȡ����������ʱ��Ҫ������������(lookup)��
 * 
 * Container���Կ��Ʊ��洢��Item�ķ��ʼ��𣬷ֱ���Global��Class��Instance��
 * Global���ʼ���ָ�κζ��󶼿��Դ�Container��ȡ����������Item��
 * Class���ʼ����Item��ֻ��ָ������Ķ���ſ��Դ�������ȡ�����Item��
 * Instance���ʼ����Item��ֻ��ָ����ʵ���ſ��Դ�������ȡ�����Item��
 * 
 * ����Ը������������Ӷ���ֹ����������ɾ���󣬼�lock modification����ʱ�Կ��Դ�����
 * �в�ѯ���õ�ָ���Ķ�������á������������󻹿��Ը�������������unlockModification��
 * �������߱����������ߡ�
 * 
 * �����ṩ�Զ��̵߳�֧�֡����ڶ��̵߳ı�׼:
 * 1�������������м����ɾ��Item�ķ���������clear(),�Լ�lockModification()��
 * unlockModification()������ֻ�ܴ��л�ִ�С�
 * 2��lookup�������Բ���ִ��
 * 3�����������м����ɾ��Item�ķ���������clear()��������һ�飬lookup��������һ�顣
 * ���鷽��ͬһʱ��ֻ����һ�����߳����С�lockModification������unlockModification
 * ������������������������ϲ���ɡ�
 * 4��isEmpty��isModificationLocked������ִ��û���κ�ͬ��Ҫ��
 *    
 * 
 * 
 */
public class Container
{
	/**
	 * װ�����д洢�������еĶ����Map
	 */
	private static Map<ItemKey, Object> itemHolder = new ConcurrentHashMap<ItemKey, Object>();
	/**
	 * �ṩ�߳�ͬ����֧�֣��ο���ע��
	 */
	private static ConfigViewSupport cvs=new ConfigViewSupport();
	/**
	 * ��¼˭��������������ͬʱ���Լ��������Ƿ������ˡ�
	 */
	private static Object containerLocker = null;

	/**
	 * �������з���Global���ʼ����Item��
	 * @param name Item������
	 * @param item �����������Ķ���
	 * @throws ContainerModificationLockedException 
	 */
	public static void putGlobalItem(String name, Object item) throws ContainerModificationLockedException
	{
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}
		if (null == item)
		{
			throw new NullPointerException("item should not be null");
		}
		
		if(isModificationLocked())
		{
			throw new ContainerModificationLockedException("Container is locked by: "+containerLocker);
		}

		cvs.lockConfig();
		try
		{
			ItemKey key = ItemKey.createGlobalItemKey(name);
			if (hasOverlappedItemKey(key))
			{
				throw new IllegalArgumentException(
						"if this item was put in with this name,"
								+ "it would happen that when a lookupper tried to look up an item with"
								+ "this name, there would be two matched items");
			}
			assert (!itemHolder.containsKey(key));

			itemHolder.put(key, item);
		} finally
		{
			cvs.unlockConfig();
		}
	}
	
	/**
	 * �������з���Class���ʼ����Item��
	 * 
	 * @param classOfLookupper ָ��һ���ֻ࣬�������Ķ�����Ϊlookupper��
	 * 						   ���ܴ�������ȡ��Item
	 * @param name Item������
	 * @param item �����������Ķ���
	 * @throws ContainerModificationLockedException 
	 */
	public static void putClassItem(Class<?> classOfLookupper, String name,
			Object item) throws ContainerModificationLockedException
	{
		if (null == classOfLookupper)
		{
			throw new NullPointerException(
					"classOfLookupper should not be null");
		}
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}
		if (null == item)
		{
			throw new NullPointerException("item should not be null");
		}

		if(isModificationLocked())
		{
			throw new ContainerModificationLockedException("Container is locked by: "+containerLocker);
		}
		
		cvs.lockConfig();
		try
		{
			ItemKey key = ItemKey.createClassItemKey(classOfLookupper, name);
			if (hasOverlappedItemKey(key))
			{
				throw new IllegalArgumentException(
						"if this item was put in with this name,"
								+ "it would happen that when a lookupper tried to look up an item with"
								+ "this name, there would be two matched items");
			}
			assert (!itemHolder.containsKey(key));

			itemHolder.put(key, item);
		} finally
		{
			cvs.unlockConfig();
		}
	}

	/**
	 * �������з���Instance���ʼ����Item��
	 * 
	 * @param lookupper ָ��һ������ֻ�����������Ϊlookupper��
	 * 		  ���ܴ�������ȡ��Item
	 * @param name Item������
	 * @param item �����������Ķ���
	 * @throws ContainerModificationLockedException 
	 */
	public static void putInstanceItem(Object lookupper, String name,
			Object item) throws ContainerModificationLockedException
	{
		if (null == lookupper)
		{
			throw new NullPointerException("lookupper should not be null");
		}
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}
		if (null == item)
		{
			throw new NullPointerException("item should not be null");
		}
		
		if(isModificationLocked())
		{
			throw new ContainerModificationLockedException("Container is locked by: "+containerLocker);
		}

		cvs.lockConfig();
		try
		{
			ItemKey key = ItemKey.createInstanceItemKey(lookupper, name);
			if (hasOverlappedItemKey(key))
			{
				throw new IllegalArgumentException(
						"if this item was put in with this name,"
								+ "it would happen that when a lookupper tried to look up an item with"
								+ "this name, there would be two matched items");
			}
			assert (!itemHolder.containsKey(key));

			itemHolder.put(key, item);
		} finally
		{
			cvs.unlockConfig();
		}
	}

	/**
	 * ����ItemKey��overlap��ʲô��˼����ItemKey.overlapped(ItemKey)
	 */
	private static boolean hasOverlappedItemKey(ItemKey key)
	{
		if (null == key)
		{
			throw new NullPointerException("key should not be null");
		}

		for (ItemKey k : itemHolder.keySet())
		{
			assert (k != null);
			if (k.overlapped(key))
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * ��������ɾ��һ��Global���ʼ����Item
	 * @param name Item������
	 * @return ��ɾ����Item����������ڣ��򷵻�null
	 * @throws ContainerModificationLockedException
	 */
	public static Object removeGlobalItem(String name) throws ContainerModificationLockedException
	{
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}

		if(isModificationLocked())
		{
			throw new ContainerModificationLockedException("Container is locked by: "+containerLocker);
		}
		
		cvs.lockConfig();
		try
		{
			ItemKey key = ItemKey.createGlobalItemKey(name);

			return itemHolder.remove(key);
		} finally
		{
			cvs.unlockConfig();
		}
	}

	/**
	 * ��������ɾ��һ��Class���ʼ����Item
	 * @param name Item������
	 * @param classOfLookupper ����ȡ�����Item��lookupper��������
	 * @return ��ɾ����Item����������ڣ��򷵻�null
	 * @throws ContainerModificationLockedException
	 */
	public static Object removeClassItem(Class<?> classOfLookupper, String name) 
	throws ContainerModificationLockedException
	{
		if (null == classOfLookupper)
		{
			throw new NullPointerException(
					"classOfLookupper should not be null");
		}
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}

		if(isModificationLocked())
		{
			throw new ContainerModificationLockedException("Container is locked by: "+containerLocker);
		}
		
		cvs.lockConfig();
		try
		{
			ItemKey key = ItemKey.createClassItemKey(classOfLookupper, name);

			return itemHolder.remove(key);
		} finally
		{
			cvs.unlockConfig();
		}
	}

	/**
	 * ��������ɾ��һ��Class���ʼ����Item
	 * @param name Item������
	 * @param lookupper ����ȡ�����Item��lookupper������
	 * @return ��ɾ����Item����������ڣ��򷵻�null
	 * @throws ContainerModificationLockedException
	 */
	public static Object removeInstanceItem(Object lookupper, String name) 
	throws ContainerModificationLockedException
	{
		if (null == lookupper)
		{
			throw new NullPointerException("lookupper should not be null");
		}
		if (null == name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		if(isModificationLocked())
		{
			throw new ContainerModificationLockedException("Container is locked by: "+containerLocker);
		}

		cvs.lockConfig();
		try
		{
			ItemKey key = ItemKey.createInstanceItemKey(lookupper, name);

			return itemHolder.remove(key);
		} finally
		{
			cvs.unlockConfig();
		}
	}
	
	/**
	 * ɾ��Container�����е�Item
	 */
	public static void clear()
	{
		if(isModificationLocked())
		{
			throw new ContainerModificationLockedException("Container is locked by: "+containerLocker);
		}
		
		cvs.lockConfig();
		try
		{
			itemHolder.clear();
		} finally
		{
			cvs.unlockConfig();
		}
	}
	
	/**
	 * ���Container�Ƿ���ȫ����
	 */
	public static boolean isEmpty()
	{
		return itemHolder.isEmpty();
	}

	/**
	 * �������ġ��������ĺ������������з����ɾ��Item�ķ���������ʱ���׳�
	 * ContainerModificationLockException�쳣��
	 * ��������ʱ��Ҫ�ṩ�����ߡ�ֻ�������߲��ܹ�������
	 * @param locker ˭����������
	 * @return �Ƿ�ɹ�������Ѿ��������Ϲ��������������ʧ�ܲ�����false��
	 */
	public static boolean lockModification(Object locker)
	{
		if (null == locker)
		{
			throw new NullPointerException("locker should not be null");
		}

		cvs.lockConfig();
		try
		{
			if (null == containerLocker)
			{
				containerLocker = locker;
				return true;
			} else
			{
				return false;
			}

		} finally
		{
			cvs.unlockConfig();
		}
	}

	/**
	 * ���������������󣬲�������ʹ���������з����ɾ��Item�ķ�����
	 * ��������ʱ��Ҫ�ṩ�����ߵ����ã��������ߵ����ú������ߵ�����
	 * ����==��֤��Ⱥ󣬲��ܳɹ�������
	 * @param unlocker �����ߵ�����
	 * @return �Ƿ�ɹ����������������û�����������ߴ���Ľ�����
	 * �������߾�==��֤����ȣ��ͻ�ʧ�ܲ�����false��
	 */
	public static boolean unlockModification(Object unlocker)
	{
		if (null == unlocker)
		{
			throw new NullPointerException("unlocker should not be null");
		}

		cvs.lockConfig();
		try
		{
			if (null == containerLocker)
			{
				return false;
			} else
			{
				if (unlocker == containerLocker)
				{
					containerLocker = null;
					return true;
				} else
				{
					return false;
				}
			}
		} finally
		{
			cvs.unlockConfig();
		}
	}

	/**
	 * @return ���������Ƿ�����
	 */
	public static boolean isModificationLocked()
	{
		return containerLocker!=null;
	}

	/**
	 * �������в��һ��Item�����Item�����ñ����ء��������ʵ����
	 * �Ǵ������а���Ҫ��ȡ��һ��Item����֮���Բ���retrieve�������
	 * ����Ϊ�����������ɾ���κ�Item��
	 * 
	 * �����������ַ��ʼ���Global��Class��Instance���κη��ʼ����Item��
	 * ֻҪlookupper�ܹ����ʵõ����ͻ����nameȡ�����ء����õ��Ļ���ڶ�
	 * ��Item�������㷵�ص�Ҫ�������ᱣ֤���ַ��ʼ����ж�û�г�ͻ��Item��
	 * 
	 * @param lookupper ˭Ҫȡ��Item��ʵ�����Ǳ���ȡ���ߵ����
	 * @param name ��Item������
	 * @return ��ȡ����Item�����á����û���򷵻�null��
	 */
	public static Object lookup(Object lookupper, String name)
	{
		cvs.lockView();
		try
		{
			ItemKey[] keys = ItemKey.createAllOverlappedKeys(lookupper, name);
			Object item = null;
			int matchCount = 0;
			for (ItemKey k : keys)
			{
				Object temp = itemHolder.get(k);
				if (temp != null)
				{
					matchCount++;
					if (null == item)
					{
						item = temp;
					}
				}

			}
			if (matchCount > 1)
			{
				assert (false);
			}

			return item;
		} finally
		{
			cvs.unlockView();
		}
	}

}
