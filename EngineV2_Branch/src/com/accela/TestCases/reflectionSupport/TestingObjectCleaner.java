package com.accela.TestCases.reflectionSupport;

import com.accela.ReflectionSupport.FieldExtractor;
import java.lang.reflect.*;
import java.util.*;

import junit.framework.TestCase;

public class TestingObjectCleaner extends TestCase
{
	private ObjectCleanerForTest oc;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		oc = new ObjectCleanerForTest();
		ClassAB.cleanSet();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();

		oc = null;
		ClassAB.cleanSet();
	}

	public void testObjectCleaner()
	{
		for (int i = 0; i < 3; i++)
		{
			oc.cleanCount=0;
			innerTestObjectCleaner();
		}
	}

	private void innerTestObjectCleaner()
	{
		// ================================

		ClassAB.cleanSet();

		ClassAB ab = new ClassAB();
		ClassAB.addToSet(ab);

		List<Object> result = oc.cleanObject(ab);
		//System.out.println("checking return list valid");
		assert (checkReturnListValid(result));

		//System.out.println("check static and final fields");
		int idx = 0;
		for (Object element : result)
		{
			//System.out.println("checking idx: " + idx);

			assert (element != null);
			if (element.getClass() == ClassA.class)
			{
				assert (((ClassA) element).checkStaticAndFinalFields());
			}
			idx++;
		}

		List<Object> creationList = ClassAB.getAddedObjects();
		//System.out.println(creationList.size());
		//System.out.println(result.size());
		assert (creationList.size() == result.size());
		assert (oc.cleanCount == result.size());
		assert (oc.cleanCount == creationList.size());

		//System.out.println("checking lists equal");
		idx = 0;
		for (Object element : result)
		{
			//System.out.println("checking idx: " + idx);

			boolean find = false;
			for (Object finder : creationList)
			{
				if (finder == element)
				{
					find = true;
					break;
				}
			}
			assert (find);
			idx++;
		}
		idx = 0;
		for (Object element : creationList)
		{
			//System.out.println("checking idx: " + idx);

			boolean find = false;
			for (Object finder : result)
			{
				if (finder == element)
				{
					find = true;
					break;
				}
			}
			assert (find);
			idx++;
		}

	}

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

		int outerIdx = 0;
		for (Object outer : list)
		{
			//System.out.println("checkingRet idx: " + outerIdx);

			if (null == outer)
			{
				return false;
			}

			int innerIdx = 0;
			for (Object inner : list)
			{
				if (innerIdx >= outerIdx)
				{
					break;
				}

				if (inner == outer)
				{
					return false;
				}

				innerIdx++;
			}

			if (!checkFields(outer))
			{
				return false;
			}

			outerIdx++;
		}

		return true;
	}

	private static boolean checkFields(Object object)
	{
		assert (object != null);

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
	
	/*public static void main(String[] args)
	{
		ClassE classE=new ClassE();
		ClassF classF=new ClassF();
		
		classE.setClassF(classF);
		classF.setClassE(classE);
		
		ObjectCleanerForTest oc=new ObjectCleanerForTest();
		oc.cleanObject(classE);
		
		System.out.println(oc.cleanCount);
		
	}*/

}
