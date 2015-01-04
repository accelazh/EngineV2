package com.accela.ReflectionSupport;

import java.util.*;
import java.util.concurrent.*;
import java.lang.reflect.*;

/**
 * 
 * �����������ȡһ����Ĺ��캯������ȡ�����Ĺ��캯���ᰴ��
 * һ����˳�����С������ܱ�֤ÿ�ε�˳����ȫ��ͬ��
 *
 */
public class ConstructorExtractor
{
	/**
	 * �����캯�����򣬺��ж������͵Ĳ���Խ�٣��ͻ���ΪԽС�����������������
	 * �ĸ���һ���࣬�����Խ�ٵĹ��캯������ԽС��
	 */
	private static final Comparator<Constructor<?>> constructorComparator = new Comparator<Constructor<?>>()
	{
		@Override
		public int compare(Constructor<?> left, Constructor<?> right)
		{
			int leftCount=0;
			int rightCount=0;
			Class<?>[] leftTypes=left.getParameterTypes();
			Class<?>[] rightTypes=right.getParameterTypes();
			
			//ͳ�Ʒǻ������͵Ĳ����ĸ���
			for(int i=0;i<leftTypes.length;i++)
			{
				if(!leftTypes[i].isPrimitive())
				{
					leftCount++;
				}
			}
			for(int i=0;i<rightTypes.length;i++)
			{
				if(!rightTypes[i].isPrimitive())
				{
					rightCount++;
				}
			}
			
			//��һ�αȽϣ��ҳ����ǻ������͵Ĳ������ٵĹ��캯��
			if(leftCount!=rightCount)
			{
				return leftCount-rightCount;
			}
			
			//�ڶ��αȽϣ��ҳ��������������ٵĹ��캯��
			return leftTypes.length-rightTypes.length;
		}

	};

	private static final Map<Class<?>, Constructor<?>[]> constructorMap=new ConcurrentHashMap<Class<?>, Constructor<?>[]>();

	/**
	 * ���ù�ȥ�ĵõ��Ĺ��캯�����飬�õ��������objectClass�Ĺ��캯���б�
	 * ���캯��������˳��
	 * ��һ�ο������ж������͵Ĳ���Խ�٣��ͻ����е�Խ��ǰ
	 * �ڶ��ο��������ĸ���Խ�٣��ͻ����е�Խ��ǰ
	 * ע���ⲻ�ܱ�֤ÿ�ε�˳����ȫ��ͬ��
	 * 
	 * ���⣬��Ҫ���ķ��ص����飬�������洢��������У��������˹���������ģ�
	 * ������������
	 */
	public Constructor<?>[] getConstructors(Class<?> objectClass)
	{
		assert(objectClass!=null);
		
		Constructor<?>[] constructors=constructorMap.get(objectClass);
		if(constructors!=null)
		{
			assert(checkReturnConstructorsValid(constructors));
			return constructors;
		}
		else
		{
			constructors = objectClass.getDeclaredConstructors();
			
			Arrays.sort(constructors, constructorComparator);
			assert(checkReturnConstructorsValid(constructors));
			
			constructorMap.put(objectClass, constructors);
			
			return constructors;
		}
	
	}

	/**
	 * �˷����������õ��Ĺ��캯���б����ȷ�ԡ�
	 */
	private boolean checkReturnConstructorsValid(Constructor<?>[] constructors)
	{
		if(null==constructors)
		{
			return false;
		}
		
		//����Ƿ���nullԪ�ػ���������ȵ�Ԫ��
		for(Constructor<?> c : constructors)
		{
			if(null==c)
			{
				return false;
			}
			
			for(Constructor<?> inner_c : constructors)
			{
				if(inner_c!=c&&inner_c.equals(c))
				{
					return false;
				}
			}
		}
		
		//�������˳��
		int outerIdx=0;
		for(Constructor<?> outer : constructors)
		{
			//�ҳ�outer�Ĵ�������л������Ͳ����ĸ���
			int outerPrimitiveCount=0;
			for(Class<?> param : outer.getParameterTypes())
			{
				if(param.isPrimitive())
				{
					outerPrimitiveCount++;
				}
			}
			
			int innerIdx=0;
			for(Constructor<?> inner : constructors)
			{
				if(innerIdx<=outerIdx)
				{
					continue;
				}
				
				//�ҳ�inner�Ĵ�������л������Ͳ����ĸ���
				int innerPrimitiveCount=0;
				for(Class<?> param : inner.getParameterTypes())
				{
					if(param.isPrimitive())
					{
						innerPrimitiveCount++;
					}
				}
				
				if(outer.getParameterTypes().length-outerPrimitiveCount
						>inner.getParameterTypes().length-innerPrimitiveCount)
				{
					return false;
				}
				else if(outer.getParameterTypes().length-outerPrimitiveCount
						<inner.getParameterTypes().length-innerPrimitiveCount)
				{
					// pass
				}
				else
				{
					if(outer.getParameterTypes().length>inner.getParameterTypes().length)
					{
						return false;
					}
					else
					{
						// pass
					}
				}
				
				innerIdx++;
			}
			
			outerIdx++;
		}
		
		//����
		return true;
		
	}
	
}
