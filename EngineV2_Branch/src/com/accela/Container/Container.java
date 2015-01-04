package com.accela.Container;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import com.accela.SynchronizeSupport.standard.ConfigViewSupport;

/**
 * 
 * 容器(Container)。 容器用来存储对象，被存储的对象叫作Item。你可以给被存储的对像起
 * 个名字(name)，当希望从容器中取出这个对象的时候，要用名字来查找(lookup)。
 * 
 * Container可以控制被存储的Item的访问级别，分别是Global、Class和Instance。
 * Global访问级别指任何对象都可以从Container中取出这个级别的Item；
 * Class访问级别的Item，只有指定的类的对象才可以从容器中取出这个Item；
 * Instance访问级别的Item，只有指定的实例才可以从容器中取出这个Item。
 * 
 * 你可以给容器上锁，从而禁止对容器的增删对象，即lock modification。此时仍可以从容器
 * 中查询并得到指定的对象的引用。给容器上锁后还可以给容器解锁，即unlockModification。
 * 但解锁者必须是上锁者。
 * 
 * 容器提供对多线程的支持。关于多线程的标准:
 * 1、所有向容器中加入和删除Item的方法，包括clear(),以及lockModification()、
 * unlockModification()方法，只能串行化执行。
 * 2、lookup方法可以并发执行
 * 3、所有容器中加入和删除Item的方法，包括clear()方法，是一组，lookup方法是另一组。
 * 两组方法同一时间只能有一组有线程运行。lockModification方法和unlockModification
 * 方法和上述两组在这个性质上不相干。
 * 4、isEmpty和isModificationLocked方法的执行没有任何同步要求。
 *    
 * 
 * 
 */
public class Container
{
	/**
	 * 装载所有存储在容器中的对象的Map
	 */
	private static Map<ItemKey, Object> itemHolder = new ConcurrentHashMap<ItemKey, Object>();
	/**
	 * 提供线程同步的支持，参看其注释
	 */
	private static ConfigViewSupport cvs=new ConfigViewSupport();
	/**
	 * 记录谁给容器上了锁，同时可以鉴别容器是否上锁了。
	 */
	private static Object containerLocker = null;

	/**
	 * 向容器中放入Global访问级别的Item。
	 * @param name Item的名字
	 * @param item 将放入容器的对象
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
	 * 向容器中放入Class访问级别的Item。
	 * 
	 * @param classOfLookupper 指定一个类，只有这个类的对象作为lookupper，
	 * 						   才能从容器中取出Item
	 * @param name Item的名字
	 * @param item 将放入容器的对象
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
	 * 向容器中放入Instance访问级别的Item。
	 * 
	 * @param lookupper 指定一个对象，只有这个对象作为lookupper，
	 * 		  才能从容器中取出Item
	 * @param name Item的名字
	 * @param item 将放入容器的对象
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
	 * 关于ItemKey的overlap是什么意思，见ItemKey.overlapped(ItemKey)
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
	 * 从容器中删除一个Global访问级别的Item
	 * @param name Item的名字
	 * @return 被删除的Item。如果不存在，则返回null
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
	 * 从容器中删除一个Class访问级别的Item
	 * @param name Item的名字
	 * @param classOfLookupper 可以取出这个Item的lookupper所属的类
	 * @return 被删除的Item。如果不存在，则返回null
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
	 * 从容器中删除一个Class访问级别的Item
	 * @param name Item的名字
	 * @param lookupper 可以取出这个Item的lookupper的引用
	 * @return 被删除的Item。如果不存在，则返回null
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
	 * 删除Container中所有的Item
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
	 * 检查Container是否完全空着
	 */
	public static boolean isEmpty()
	{
		return itemHolder.isEmpty();
	}

	/**
	 * 锁定更改。锁定更改后，所有向容器中放入或删除Item的方法被调用时将抛出
	 * ContainerModificationLockException异常。
	 * 锁定容器时需要提供锁定者。只有锁定者才能够解锁。
	 * @param locker 谁锁定了容器
	 * @return 是否成功。如果已经给容器上过锁，这个方法将失败并返回false。
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
	 * 解锁容器。解锁后，才能正常使用向容器中放入或删除Item的方法。
	 * 解锁容器时需要提供解锁者的引用，当解锁者的引用和上锁者的引用
	 * 经过==验证相等后，才能成功解锁。
	 * @param unlocker 解锁者的引用
	 * @return 是否成功解锁。如果本来就没有上锁，或者传入的解锁者
	 * 和上锁者经==验证不相等，就会失败并返回false。
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
	 * @return 返回容器是否被上锁
	 */
	public static boolean isModificationLocked()
	{
		return containerLocker!=null;
	}

	/**
	 * 从容器中查出一个Item，这个Item的引用被返回。这个方法实质上
	 * 是从容器中按照要求取出一个Item，但之所以不用retrieve这个名字
	 * 是因为这个方法不会删除任何Item。
	 * 
	 * 容器中有三种访问级别Global、Class、Instance。任何访问级别的Item，
	 * 只要lookupper能够访问得到，就会根据name取出返回。不用担心会存在多
	 * 个Item都能满足返回的要求，容器会保证三种访问级别中都没有冲突的Item。
	 * 
	 * @param lookupper 谁要取出Item，实质上是表明取出者的身份
	 * @param name 该Item的名字
	 * @return 被取出的Item的引用。如果没有则返回null。
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
