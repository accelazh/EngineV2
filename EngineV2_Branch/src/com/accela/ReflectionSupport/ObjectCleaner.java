package com.accela.ReflectionSupport;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;

/**
 * 
 * ������ܹ����һ������������������͵��ֶΣ� �Լ������ݹ����ո��ֶ������õĶ���
 * 
 * ����Զ���һ���������涨һ�������ڱ����֮ǰ��ʲô
 * 
 */
public class ObjectCleaner
{
	private final ObjectCleanerObjectRecorder objectRecorder = new ObjectCleanerObjectRecorder();

	private final List<Object> cleanedObjects = new LinkedList<Object>();

	private void addCleanedObject(Object object)
	{
		assert (object != null);
		assert (!containsCleanedObject(object));
		if (null == object)
		{
			throw new NullPointerException("object should not be null");
		}

		cleanedObjects.add(object);
	}

	private void clearCleanedObjects()
	{
		assert (cleanedObjects != null);
		cleanedObjects.clear();
	}

	//����������Ĵ�����Դ��ֻ���ڶ�����
	private boolean containsCleanedObject(Object object)
	{
		assert (object != null);

		for(Object element : cleanedObjects)
		{
			assert(element!=null);
			
			if(element==object)
			{
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * �ݹ���������ķ�������Ѷ�������з�static�ҷ�final���������͵ĳ�Ա������Ϊnull�����һ�ݹ�ذ����б�����������õĶ���Ҳ��ͬ
	 * ���ķ�ʽ��������ܹ��Ը�ѭ�����á�
	 * 
	 * ���⣬����������Զ�����ö�����͡���װ�����͡�String���͵Ķ��󣬲����������ǵ��ڲ���ֻ�ǰ����Ƿŵ����صı��С�
	 * ���������java�������������� ���Ż���
	 * 
	 * @return �������б�������Ķ��󣬰��������object��ObjectCleaner
	 *         ÿ�η��صı���ͬһ�������������洢��ObjectCleaner�У�����ʹ
	 *         �á���Ҫ�޸ķ���ֵ����������һ�ε���cleanObject(Object)������ʱ�� �Ա�������ֵ��
	 * 
	 */
	public synchronized List<Object> cleanObject(Object object)
	{
		if (null == object)
		{
			throw new NullPointerException("object should not be null");
		}
		
		clearCleanedObjects();
		cleanObjectImpl(object);
		objectRecorder.clearObjectRecord();

		assert (containsCleanedObject(object));
		assert (checkReturnListValid(cleanedObjects));
		return cleanedObjects;
	}

	private void cleanObjectImpl(Object object)
	{
		assert (object != null);
		
		if (object != null && !objectRecorder.hasRecordOf(object))
		{
			onClean(object);
			
			objectRecorder.recordObject(object);
			addCleanedObject(object);
			cleanNormalObject(object);
		}

	}

	// normal object ָ������null��Ҳ����objectRecorder����¼�����Ѿ�������Ķ���
	private void cleanNormalObject(Object object)
	{
		assert (object != null);
		assert (objectRecorder.hasRecordOf(object));
		assert (containsCleanedObject(object));

		if (object.getClass().isArray())
		{
			// ��������
			Class<?> componentType = object.getClass().getComponentType();
			assert (componentType != null);

			if (!componentType.isPrimitive())
			{
				cleanObjectArray((Object[]) object);
			}

		} else if (object.getClass().isEnum())
		{
			// do nothing
		} else if (object.getClass() == Integer.class)
		{
			// do nothing
		} else if (object.getClass() == Boolean.class)
		{
			// do nothing
		} else if (object.getClass() == Double.class)
		{
			// do nothing
		} else if (object.getClass() == Float.class)
		{
			// do nothing
		} else if (object.getClass() == Long.class)
		{
			// do nothing
		} else if (object.getClass() == Character.class)
		{
			// do nothing
		} else if (object.getClass() == Short.class)
		{
			// do nothing
		} else if (object.getClass() == Byte.class)
		{
			// do nothing
		} else if (object.getClass() == String.class)
		{
			// do nothing
		} else
		{
			if (object instanceof Collection)
			{
				// �Լ�����ķ�ʽ�������
				cleanCollection((Collection<?>) object);
			} else if (object instanceof Map)
			{
				// ��Map�ķ�ʽ�������
				cleanMap((Map<?, ?>) object);
			} else
			{
				// �������������ֶ�
				cleanFields(object);
			}

		}

	}

	private void cleanObjectArray(Object[] objects)
	{
		assert (objects != null);

		for (int i = 0; i < objects.length; i++)
		{
			objects[i] = null;
			cleanObjectImpl(objects[i]);
		}
	}

	private void cleanCollection(Collection<?> collection)
	{
		assert (collection != null);

		int idx=0;
		for (Object element : collection)
		{
			cleanObjectImpl(element);
			idx++;
		}

		collection.clear();
	}

	private void cleanMap(Map<?, ?> map)
	{
		assert (map != null);

		int idx=0;
		for (Entry<?, ?> element : map.entrySet())
		{
			cleanObjectImpl(element.getKey());
			cleanObjectImpl(element.getValue());
			idx++;
		}

		map.clear();
	}

	private void cleanFields(Object object)
	{
		assert (object != null);
		assert (!object.getClass().isArray());
		assert (!object.getClass().isEnum());
		assert (!(object instanceof Collection));
		assert (!(object instanceof Map));

		FieldExtractor fieldExtractor = FieldExtractor.getFieldExtractor();
		List<Field[]> fieldList = fieldExtractor.getSortedFieldsList(object
				.getClass());

		for (Field[] fields : fieldList)
		{
			for (Field f : fields)
			{
				if (Modifier.isStatic(f.getModifiers()))
				{
					break;
				}
				if (Modifier.isFinal(f.getModifiers()))
				{
					continue;
				}

				cleanField(object, f);
			}
		}

		FieldExtractor.disposeFieldExtractor(fieldExtractor);
	}

	private void cleanField(Object object, Field field)
	{
		assert (object != null);
		assert (field != null);
		assert (!Modifier.isStatic(field.getModifiers()));
		assert (!Modifier.isFinal(field.getModifiers()));

		Class<?> fieldType = field.getType();
		field.setAccessible(true);
		if (fieldType.isPrimitive())
		{
			return;
		}

		try
		{
			Object fieldVal = field.get(object);
			if (fieldVal != null)
			{
				field.set(object, null);
				cleanObjectImpl(fieldVal);
			}
		} catch (IllegalArgumentException ex)
		{
			ex.printStackTrace();
			assert (false);
		} catch (IllegalAccessException ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	// //////////////////////////////////////////////////////////////////
	
	private static boolean checkReturnListValid(List<Object> list)
	{
		if (null == list)
		{
			return false;
		}
		if (list.size() < 1)
		{
			return false;
		}

		int outerIdx=0;
		for(Object outer : list)
		{
			if(null==outer)
			{
				return false;
			}
			
			int innerIdx=0;
			for(Object inner : list)
			{
				if(innerIdx>=outerIdx)
				{
					break;
				}
				
				if(inner==outer)
				{
					return false;
				}
				
				innerIdx++;
			}
			
			if(!checkFields(outer))
			{
				return false;
			}
			
			outerIdx++;
		}

		return true;
	}
	
	private static boolean checkFields(Object object)
	{
		assert(object!=null);
		
		if (object.getClass().isArray())
		{
			Class<?> componentType = object.getClass().getComponentType();
			assert (componentType != null);

			if (!componentType.isPrimitive())
			{
				for (Object element : (Object[]) object)
				{
					if (element != null)
					{
						return false;
					}
				}
			}

		} else if (object.getClass().isEnum())
		{
			// do nothing
		} else
		{
			if (object instanceof Collection)
			{
				if (((Collection<?>) object).size() != 0)
				{
					return false;
				}

			} else if (object instanceof Map)
			{
				if (((Map<?, ?>) object).size() != 0)
				{
					return false;
				}
			} else
			{
				FieldExtractor fieldExtractor = FieldExtractor
						.getFieldExtractor();
				List<Field[]> fieldList = fieldExtractor
						.getSortedFieldsList(object.getClass());

				for (Field[] fields : fieldList)
				{
					for (Field f : fields)
					{
						if (Modifier.isStatic(f.getModifiers()))
						{
							continue;
						}
						if (Modifier.isFinal(f.getModifiers()))
						{
							continue;
						}

						Class<?> fieldType = f.getType();
						f.setAccessible(true);
						if (fieldType.isPrimitive())
						{
							continue;
						}

						try
						{
							Object fieldVal = f.get(object);
							if (fieldVal != null)
							{
								return false;
							}
						} catch (IllegalArgumentException ex)
						{
							ex.printStackTrace();
							return false;
						} catch (IllegalAccessException ex)
						{
							ex.printStackTrace();
							return false;
						}
					}
				}

				FieldExtractor.disposeFieldExtractor(fieldExtractor);
			}

		}
		
		return true;
	}

	/**
	 * 
	 * ������������̳У����Զ����ڶ����ڱ�����
	 * ֮ǰ��ʲô
	 */
	protected void onClean(Object object)
	{
		
	}

}
