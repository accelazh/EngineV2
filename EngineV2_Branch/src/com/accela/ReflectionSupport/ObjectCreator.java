package com.accela.ReflectionSupport;

import java.lang.reflect.*;

import com.accela.ObjectPool.ObjectPool;
import java.util.*;
import java.util.concurrent.*;

/**
 * 
 * �����ʹ��ParameterGenerator�����ʹ�÷������һ����Ĺ��캯����
 * ���½�����Ĺ�����
 * 
 * ObjectCreator���Զ���ס���캯���б��ܹ��ɹ��½�����Ĺ��캯�����Լ�
 * �ܹ��ɹ��½�����Ĳ����б�
 * //TODO �¼��ϵĻ��������ܶ�û�о����꾡�Ĳ���
 * //TODO δ���꾡���ԣ�ֻ��ͨ��HPObjectStreams�����֤��
 */
public class ObjectCreator
{
	/**
	 * ������ȡ���캯��
	 */
	private static final ConstructorExtractor constructorExtractor=new ConstructorExtractor(); 
	/**
	 * ������¼��ǰ�ҵ��ģ��ܹ��ɹ��½�����Ĺ��캯����
	 * �´�����Ҫ�½������Ķ����ʱ�򣬾Ϳ���ֱ��ʹ���ˡ�
	 */
	private static final Map<Class<?>, Constructor<?>> hitConstructorMap=new ConcurrentHashMap<Class<?>, Constructor<?>>();
	/**
	 * ��¼һ����Գɹ�ʹ���캯���½�������Ĳ�����
	 * �´�������ͬ���Ĺ��캯����ʱ�򣬾Ϳ���ֱ��ʹ���ˡ�
	 */
	private static final Map<Constructor<?>, Object[]> hitParamMap=new ConcurrentHashMap<Constructor<?>, Object[]>();
	
	private static final CaseObjectCreator caseCreator=new CaseObjectCreator();
	
	/**
	 * ��������objectClass�Ĺ��캯������ͼ�ҵ�һ�������õĹ��캯�����½�����
	 * ���ʵ���Ҳ������򷵻�null��
	 * 
	 * //TODO ��һ��BUG��������캯���еĲ������ʹ���ѭ�����ͻ�ʹ��ObjectCreator����
	 * //TODO �����������Ӧ�ú��ټ����Ͼ���ʱӦ�����������캯������ʹ�ã�����Ӧ�ñ�����ѡ��
	 */
	public Object createObject(Class<?> objectClass)
	{
		if (null == objectClass)
		{
			throw new NullPointerException("objectClass should not be null");
		}
		
		//����ʹ��CaseObjectCreator����
		Object caseRet=caseCreator.createInstance(objectClass);
		if(caseRet!=null)
		{
			assert(caseRet.getClass()==objectClass);
			return caseRet;
		}
		
		//���ԴӶ������Ѱ��
		Object result=ObjectPool.retrieve(objectClass);
		if(result!=null)
		{
			assert(result.getClass()==objectClass);
			return result;
		}
		
		//���ԾɵĴ洢�Ĺ��캯��
		Constructor<?> oldConstructor=hitConstructorMap.get(objectClass);
		if(oldConstructor!=null)
		{
			Object newInstance=tryConstructor(oldConstructor);
			
			if(newInstance!=null)
			{
				//System.out.println("old constructor hit!");
				return newInstance;
			}
			else
			{
			    //���������Ӧ�ó��֣������assert(false)����������ص��쳣��
				//����ͨ����Ϳ��������ñ������캯���ķ��������ԣ����ӽ�׳�ԡ�
				assert(false);
			}
		}

		//�������캯�������γ���
		Constructor<?>[] constructors = constructorExtractor.getConstructors(objectClass);

		Object newInstance = null;
		for (int i = 0; i < constructors.length; i++)
		{
			try
			{
				newInstance = tryConstructor(constructors[i]);
				if (newInstance != null)
				{
					assert(newInstance.getClass()==objectClass);
					
					//��¼�ɹ��Գ�����Ĺ��캯��
					hitConstructorMap.put(objectClass, constructors[i]);
					
					break;
				}
			} catch (Exception ex)
			{
				ex.printStackTrace();
			}
		}

		return newInstance;
	}

	/**
	 * �ø��ֲ�����̽һ�����캯������ͼ�½�����������г���
	 * ��ʧ���ˣ��򷵻�null
	 * @param constructor
	 * @return
	 */
	private Object tryConstructor(Constructor<?> constructor)
	{
		Object result = null;
		constructor.setAccessible(true);
		
		//�Ӿ��еļ�¼�г����ܷ�ɹ�
		Object[] oldParams=hitParamMap.get(constructor);
		if(oldParams!=null)
		{
			try
			{
				result = constructor.newInstance(oldParams);
				assert(result!=null);
				
				if(result!=null)
				{
					return result;
				}
			} catch (Exception ex)
			{
				System.err.println("constructor is : "+constructor);
				ex.printStackTrace();
				assert(false);
			}
		}
		
		//���Ա������캯���Ĳ����ĸ��ֿ��ܣ��������µĶ���

		Class<?>[] parameterTypes = constructor.getParameterTypes();
		ParameterGenerator[] parameterGenerators = new ParameterGenerator[parameterTypes.length];
		for (int i = 0; i < parameterGenerators.length; i++)
		{
			parameterGenerators[i] = getParameterGenerator();
		}

		Object[] parameters = new Object[parameterTypes.length];

		int fillingIdx = 0;
		while (fillingIdx >= 0)
		{
			if (fillingIdx < parameters.length)
			{
				if (parameterGenerators[fillingIdx].hasNext())
				{
					parameters[fillingIdx] = parameterGenerators[fillingIdx]
							.createParameter(parameterTypes[fillingIdx], this);
					fillingIdx++;
				} else
				{
					parameterGenerators[fillingIdx].reset();
					fillingIdx--;
				}
			} else
			{
				try
				{
					result = constructor.newInstance(parameters);
					assert(result!=null);
					
					//��¼�ɹ��Գ�����Ĳ���
					hitParamMap.put(constructor, parameters);
					
					break;
				} catch (Exception ex)
				{
					fillingIdx--;
				}
			}
		}

		for (ParameterGenerator ppg : parameterGenerators)
		{
			disposeParameterGenerator(ppg);
		}

		return result;
	}

	/**
	 * ��ppgStack��ϣ�ȡ��һ��ParameterGenerator��ʹ�����
	 * ������ȡParameterGenerator����������ParameterGenerator
	 * ��ʹ���Ż���
	 * 
	 * @return һ��ParameterStack����
	 */
	private ParameterGenerator getParameterGenerator()
	{
		return ParameterGenerator.getParameterGenerator();
	}

	/**
	 * ��������һ��ParameterGenerator�����Ӧ��ͨ���������
	 * ���ͷţ���������ʹParameterGenererator�����ظ����á�
	 * @param ppg Ҫ�ͷŵ�ParameterGenerator����
	 */
	private void disposeParameterGenerator(ParameterGenerator ppg)
	{
		if(null==ppg)
		{
			throw new NullPointerException("ppg should not be null");
		}
		
		ParameterGenerator.disposeParameterGenerator(ppg);
	}

}
