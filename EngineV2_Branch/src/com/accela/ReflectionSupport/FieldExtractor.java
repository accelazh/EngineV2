package com.accela.ReflectionSupport;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.lang.reflect.*;

/**
 * 
 * �����������һ��Class<?>�����г�ȡ�������Լ������и�����ֶΡ�
 * �ֶ��Թ̶���������ÿ������װ��һ����������ֶΣ���������static
 * �ֶη��ڷ�static�ֶκ�ͷ��
 *
 * //TODO δ���꾡���ԣ�ֻ��ͨ��HPObjectStream�����֤��
 * 
 */
public class FieldExtractor
{
	private static final Map<Class<?>, List<Field[]>> fieldMap = new ConcurrentHashMap<Class<?>, List<Field[]>>();
	
	private static final FieldComparator fieldComparator=new FieldComparator();
	
	/**
	 * 
	 * ������򣺾�̬�ֶαȷǾ�̬�ֶδ󣬾�̬�ֶκ;�̬�ֶζ���ȣ�
	 * �Ǿ�̬�ֶκͷǾ�̬�ֶ�֮�������������ֵ���Ƚ�
	 *
	 */
	private static class FieldComparator implements Comparator<Field>
	{
		@Override
		public int compare(Field left, Field right)
		{
			boolean isLeftStatic=Modifier.isStatic(left.getModifiers());
			boolean isRightStatic=Modifier.isStatic(right.getModifiers());
			
			if(!isLeftStatic&&isRightStatic)
			{
				return -1;
			}
			else if(isLeftStatic&&!isRightStatic)
			{
				return 1;
			}
			else if(isLeftStatic&&isRightStatic)
			{
				return 0;
			}
			else
			{
				return left.getName().compareTo(right.getName());
			}
			
		}
	}
	
	/**
	 * ��ȡһ���༰�丸�࣬�Լ�����ĸ���������ֶΡ����ص������У�ÿ������
	 * ��װ��һ����������ֶΡ���һ��������ǰһ������ĸ��ࡣ��һ�������ڣ�
	 * ���о�̬�ֶζ����ڷǾ�̬�ֶκ��棬�Ǿ�̬�ֶΰ��������ֵ��ֵ������У�
	 * ��̬�ֶε�˳��û�б�֤��
	 * 
	 * ���⣬���ص������Լ����е����鶼�Ǵ洢��������еģ��������˹������
	 * ���ģ�����������
	 */
	public List<Field[]> getSortedFieldsList(final Class<?> objectClass)
	{
		if(null==objectClass)
		{
			throw new NullPointerException("objectClass should not be null");
		}
		
		List<Field[]> fieldList=fieldMap.get(objectClass);
		if(fieldList!=null)
		{
			assert(checkReturnListValid(fieldList));
			return fieldList;
		}
		else
		{
			//���㲢�õ�fieldList
			fieldList=new LinkedList<Field[]>();
			Class<?> aimClass = objectClass;
			while (aimClass != null)
			{
				Field[] fields = aimClass.getDeclaredFields();
				Arrays.sort(fields, fieldComparator);
				fieldList.add(fields);
				
				aimClass=aimClass.getSuperclass();
			}
			
			//�õ����ɸı�ı���ͼ
			fieldList=Collections.unmodifiableList(fieldList);
			assert(fieldList!=null);
			
			//���µõ���fieldList��¼��fieldMap�����ʹ�ö��̣߳��п����ظ�
			//������ͬ��fieldList�����������������
			fieldMap.put(objectClass, fieldList);
			
			//���õ���fieldList����ȷ��
			assert(checkReturnListValid(fieldList));
			
			return fieldList;
		}
		
	}

	private static boolean checkReturnListValid(List<Field[]> fieldList)
	{
		if(null==fieldList)
		{
			return false;
		}
		
		
		for(Field[] fields : fieldList)
		{
			if(null==fields)
			{
				return false;
			}
			
			boolean hasMetStatic=false;
			for(int i=0;i<fields.length;i++)
			{
				final Field f=fields[i];
				if(null==f)
				{
					return false;
				}
				
				if(hasMetStatic)
				{
					if(!Modifier.isStatic(f.getModifiers()))
					{
						return false;
					}
				}
				else
				{
					if(Modifier.isStatic(f.getModifiers()))
					{
						hasMetStatic=true;
					}
					else
					{
						if(i>0)
						{
							final Field lastF=fields[i-1];
							if(lastF==null)
							{
								return false;
							}
							if(Modifier.isStatic(lastF.getModifiers()))
							{
								return false;
							}
							if(lastF.getName().compareTo(f.getName())>=0)
							{
								return false;
							}
						}
						else
						{
							if(f==null)
							{
								return false;
							}
							if(hasMetStatic)
							{
								return false;
							}
						}
					}
				}
			} 
		}// outer for
		
		return true;
	}
	
	/**
	 * FieldExtractor�Ĵ���ʹ���˻��������������Ӧ��ͨ����̬��������
	 */
	protected FieldExtractor()
	{
		
	}
	
	/**
	 * װ���ֶ���ȡ��FieldExtractor�����ջ������Ϊ���Ż�FieldExtractor����
	 * ��ʹ�ã�����Ƶ���������ͷ�FieldExtractor�������ơ�
	 */
	private static Stack<FieldExtractor> fieldExtractorStack = new Stack<FieldExtractor>();

	/**
	 * ���һ��FieldExtractor����ͨ��������������FieldExtractor
	 * ������ܹ��Ż������½��� 
	 */
	public synchronized static FieldExtractor getFieldExtractor()
	{
		if (fieldExtractorStack.isEmpty())
		{
			return new FieldExtractor();
		} else
		{
			return fieldExtractorStack.pop();
		}
	}
	
	/**
	 * �ͷ�һ��FieldExtractor����ͨ������������ͷ�FieldExtractor
	 * ������ܹ��Ż������ͷš� 
	 */
	public synchronized static void disposeFieldExtractor(FieldExtractor fieldExtractor)
	{
		if(null==fieldExtractor)
		{
			throw new NullPointerException("fieldExtractor should not be null");
		}
		fieldExtractorStack.push(fieldExtractor);
	}
	
}
