package com.accela.ReflectionSupport;

import java.net.Inet4Address;
import java.net.UnknownHostException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 
 * ������ObjectCreatorʹ�á��ܶ��ض����಻��Ҫ�鷳��ȥƥ�����Ĺ��캯����
 * ֻ��Ҫ��������У�һһָ����θ�һ���ض�����Ӧ�����ʹ�����Ĺ��캯��ȥ�� ������Ϳ����ˡ�������������������µġ�
 * 
 */
public class CaseObjectCreator
{
	private Map<Class<?>, Creator> creatorMap;

	public CaseObjectCreator()
	{
		creatorMap = new ConcurrentHashMap<Class<?>, Creator>();
		initCreators();
		creatorMap = Collections.unmodifiableMap(creatorMap);

		assert (creatorMap != null);
	}

	private void initCreators()
	{
		addCreator(new Inet4AddressCreator());
	}

	private void addCreator(Creator creator)
	{
		if (null == creator)
		{
			throw new NullPointerException("creator should not be null");
		}

		assert (creator.getAimClass() != null);
		creatorMap.put(creator.getAimClass(), creator);
	}

	//TODO ע�������Ŀǰ��ΪcreatorMap��ֻ���ģ�����û��ͬ��Լ��
	public Object createInstance(Class<?> objectClass)
	{
		if (null == objectClass)
		{
			throw new NullPointerException("objectClass should not be null");
		}

		Creator creator = creatorMap.get(objectClass);
		if (creator != null)
		{
			Object ret = creator.createInstance(objectClass);

			assert (ret != null);
			assert (ret.getClass() == objectClass);

			System.out.println("CaseObjectCreator hit!");
			
			return ret;
		} else
		{
			return null;
		}

	}

	// ���������ָ��һ���ض�������δ�������
	private static abstract class Creator
	{
		private Class<?> aimClass;

		public Creator(Class<?> aimClass)
		{
			if (null == aimClass)
			{
				throw new NullPointerException("aimClass should not be null");
			}

			this.aimClass = aimClass;
		}

		public Object createInstance(Class<?> objectClass)
		{
			if (null == objectClass)
			{
				throw new NullPointerException("objectClass should not be null");
			}
			assert (aimClass != null);
			if (aimClass != objectClass)
			{
				throw new IllegalArgumentException(
						"objectClass does match the kind of class this Creator handles");
			}

			Object ret = createInstanceImpl();
			if (null == ret)
			{
				throw new IllegalStateException(
						"createInstanceImpl() should not return null");
			}
			if (ret.getClass() != objectClass)
			{
				throw new IllegalStateException(
						"createInstanceImpl() return an object that does not match the aimClass");
			}

			return ret;
		}

		protected abstract Object createInstanceImpl();

		public Class<?> getAimClass()
		{
			return aimClass;
		}

	}

	private static class Inet4AddressCreator extends Creator
	{
		public Inet4AddressCreator()
		{
			super(Inet4Address.class);
		}

		@Override
		protected Object createInstanceImpl()
		{
			Object ret = null;
			try
			{
				ret = Inet4Address.getByName("192.168.1.1");
			} catch (UnknownHostException ex)
			{
				ex.printStackTrace();
				assert (false);
			}

			assert (ret != null);
			return ret;
		}

	}
}
