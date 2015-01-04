package com.accela.Container;

import com.accela.ClassIDAndInstanceID.*;

/**
 * 
 * 存储在Container中的Item的键
 *
 */
class ItemKey
{
	/**
	 * aimClassID!=null&&aimInstanceID!=null: ItemKey对应的Item为Instance访问级别，只有aimInstance对应的实例可以从Container中取出该Item。此时aimClassID对应的类应该是aimInstanceID对应的实例的类。 
	 * aimClassID!=null&&aimInstanceID==null: ItemKey对应的Item为Class访问级别，只有aimClassID对应的类的对象可以从Container中取出该Item。
	 * aimClassID==null&&aimInstanceID==null: ItemKey对应的Item为Global访问级别，任何对象都可以从Container中取出该Item。
	 */
	private ClassID aimClassID;
	/**
	 * 同aimClassID的注释
	 */
	private InstanceID aimInstanceID;
	/**
	 * ItemKey所对应的Item的名字
	 */
	private String name;

	protected ItemKey(ClassID aimClassID, InstanceID aimInstanceID, String name)
	{
		if (null == aimClassID)
		{
			if (aimInstanceID != null)
			{
				throw new IllegalArgumentException("invalid aimInstanceID");
			}
		} else
		{
			if (aimInstanceID != null
					&& !aimInstanceID.isOfSameClass(aimClassID))
			{
				throw new IllegalArgumentException("invalid aimInstanceID");
			}
		}
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}

		this.aimClassID = aimClassID;
		this.aimInstanceID = aimInstanceID;
		this.name=name;
	}
	
	/**
	 * 两个键overlap是指，它们的name属性相同，并且当某个lookupper
	 * 通过这个name查找(lookup)Item的时候，这两个键都满足查找的要
	 * 求。这会引起Container的混乱，是Container所禁止的。
	 * 
	 * @param key
	 * @return
	 */
	public boolean overlapped(ItemKey key)
	{
		if(null==key)
		{
			throw new NullPointerException("key should not be null");
		}
		
		if(!name.equals(key.name))
		{
			return false;
		}
		
		if(aimClassID!=null
				&&key.aimClassID!=null
				&&!aimClassID.equals(key.aimClassID))
		{
			return false;
		}
		
		if(aimInstanceID!=null
				&&key.aimInstanceID!=null
				&&!aimInstanceID.equals(key.aimInstanceID))
		{
			return false;
		}
		
		return true;
	}
	
	/**
	 * 该方法构造所有满足下述要求的键，不论在Container中存不存在，并返回。
	 * 这些键都满足：
	 * 如果Container中以aimInstance作为lookupper，以name作为名字，
	 * 调用lookup方法来查出Item，这些键中的每一个如果存在于Container
	 * 中，都应该被查询出。并且不存在一个键，它不在上述的那些键中，而
	 * 且它也应该被查询出。
	 * 
	 * @param aimInstance 见上文
	 * @param name 见上文
	 * @return 见上文
	 */
	public static ItemKey[] createAllOverlappedKeys(Object aimInstance, String name)
	{
		if(null==aimInstance)
		{
			throw new NullPointerException("aimInstance should not be null");
		}
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		return new ItemKey[]{
				createInstanceItemKey(aimInstance, name),
				createClassItemKey(aimInstance.getClass(), name),
				createGlobalItemKey(name)};
	}
	
	/**
	 * 创建Instance访问级别的键
	 */ 
	public static ItemKey createInstanceItemKey(Object aimInstance, String name)
	{
		if(null==aimInstance)
		{
			throw new NullPointerException("aimInstance should not be null");
		}
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		ClassID classID=IDDispatcher.createClassID(aimInstance.getClass());
		InstanceID instanceID=IDDispatcher.createInstanceID(aimInstance);
		return new ItemKey(classID, instanceID, name); 
	}
	/**
	 * 创建Class访问级别的键
	 */ 
	public static ItemKey createClassItemKey(Class<?> aimClass, String name)
	{
		if(null==aimClass)
		{
			throw new NullPointerException("aimClass should not be null");
		}
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		ClassID classID=IDDispatcher.createClassID(aimClass);
		return new ItemKey(classID, null, name);
	}
	/**
	 * 创建Global访问级别的键
	 */ 
	public static ItemKey createGlobalItemKey(String name)
	{
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		return new ItemKey(null, null, name);
	}

	@Override
	public boolean equals(Object obj)
	{
		if (!(obj instanceof ItemKey))
		{
			return false;
		}
		
		ItemKey other=(ItemKey)obj;

		assert(name!=null);
		assert(other.name!=null);
		
		if(!name.equals(other.name))
		{
			return false;
		}
		
		if(aimClassID!=null&&other.aimClassID!=null)
		{
			if(!aimClassID.equals(other.aimClassID))
			{
				return false;
			}
		}
		else if(aimClassID!=null&&null==other.aimClassID)
		{
			return false;
		}
		else if(null==aimClassID&&other.aimClassID!=null)
		{
			return false;
		}
		
		if(aimInstanceID!=null&&other.aimInstanceID!=null)
		{
			if(!aimInstanceID.equals(other.aimInstanceID))
			{
				return false;
			}
		}
		else if(aimInstanceID!=null&&null==other.aimInstanceID)
		{
			return false;
		}
		else if(null==aimInstanceID&&other.aimInstanceID!=null)
		{
			return false;
		}
		
		return true;
	}

	@Override
	public int hashCode()
	{
		assert(name!=null);
		
		if (null == aimClassID)
		{
			if (null == aimInstanceID)
			{
				return name.hashCode();
			} else
			{
				assert (false);
				throw new IllegalStateException(
						"aimClassID is null but aimInstanceID is not null");
			}
		} else
		{
			if (null == aimInstanceID)
			{
				return aimClassID.hashCode()^name.hashCode();
			} else
			{
				if (aimClassID.isOfSameClass(aimInstanceID))
				{
					return aimClassID.hashCode()
					^aimInstanceID.hashCode()
					^name.hashCode();
				} else
				{
					assert (false);
					throw new IllegalStateException(
							"aimInstanceID and aimClassID are all non-null, "
									+ "but the class of the instance aimInstanceID " 
									+ "represents is not the class aimClassID represents");
				}
			}
		}

	}

}
