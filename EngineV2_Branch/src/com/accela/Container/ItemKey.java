package com.accela.Container;

import com.accela.ClassIDAndInstanceID.*;

/**
 * 
 * �洢��Container�е�Item�ļ�
 *
 */
class ItemKey
{
	/**
	 * aimClassID!=null&&aimInstanceID!=null: ItemKey��Ӧ��ItemΪInstance���ʼ���ֻ��aimInstance��Ӧ��ʵ�����Դ�Container��ȡ����Item����ʱaimClassID��Ӧ����Ӧ����aimInstanceID��Ӧ��ʵ�����ࡣ 
	 * aimClassID!=null&&aimInstanceID==null: ItemKey��Ӧ��ItemΪClass���ʼ���ֻ��aimClassID��Ӧ����Ķ�����Դ�Container��ȡ����Item��
	 * aimClassID==null&&aimInstanceID==null: ItemKey��Ӧ��ItemΪGlobal���ʼ����κζ��󶼿��Դ�Container��ȡ����Item��
	 */
	private ClassID aimClassID;
	/**
	 * ͬaimClassID��ע��
	 */
	private InstanceID aimInstanceID;
	/**
	 * ItemKey����Ӧ��Item������
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
	 * ������overlap��ָ�����ǵ�name������ͬ�����ҵ�ĳ��lookupper
	 * ͨ�����name����(lookup)Item��ʱ������������������ҵ�Ҫ
	 * ���������Container�Ļ��ң���Container����ֹ�ġ�
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
	 * �÷�������������������Ҫ��ļ���������Container�д治���ڣ������ء�
	 * ��Щ�������㣺
	 * ���Container����aimInstance��Ϊlookupper����name��Ϊ���֣�
	 * ����lookup���������Item����Щ���е�ÿһ�����������Container
	 * �У���Ӧ�ñ���ѯ�������Ҳ�����һ��������������������Щ���У���
	 * ����ҲӦ�ñ���ѯ����
	 * 
	 * @param aimInstance ������
	 * @param name ������
	 * @return ������
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
	 * ����Instance���ʼ���ļ�
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
	 * ����Class���ʼ���ļ�
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
	 * ����Global���ʼ���ļ�
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
