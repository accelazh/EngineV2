package com.accela.TestCases.ClassIDAndInstanceID;

import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;
import com.accela.ClassIDAndInstanceID.*;

public class TestingClassAndInstanceID extends TestCase
{
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	private ClassID createClassID(Class<?> aimClass)
			throws ClassNotFoundException, SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException
	{
		assert (aimClass != null);

		ClassID lastClassID = IDDispatcher.createClassID(aimClass);
		ClassID newClassID = null;
		for (int i = 0; i < 1000; i++)
		{
			if (i % 2 == 0)
			{
				newClassID = IDDispatcher.createClassID(aimClass);
			} else
			{
				newClassID = IDDispatcher.createClassID(aimClass.getName());
			}
			assert (newClassID != null);
			assert (lastClassID != null);
			assert (newClassID == lastClassID);
			assert (newClassID.equals(lastClassID));
			lastClassID = newClassID;

			checkClassIDAccuracy(newClassID, aimClass);

			Constructor<ClassID> constructor = ClassID.class
					.getDeclaredConstructor(Class.class);
			constructor.setAccessible(true);
			ClassID reflectClassID = constructor.newInstance(aimClass);
			assert (reflectClassID != null);
			assert (reflectClassID != newClassID);
			// assert (reflectClassID.equals(newClassID));
			// checkClassIDAccuracy(reflectClassID, aimClass);

			boolean hasException = false;
			try
			{
				newClassID = IDDispatcher.createClassID("" + Math.random());
			} catch (Exception ex)
			{
				assert (ex instanceof ClassNotFoundException);
				hasException = true;
			}
			assert (hasException);
			assert (newClassID != null);
		}

		assert (newClassID != null);
		return newClassID;
	}

	private void checkClassIDAccuracy(ClassID classID, Class<?> aimClass)
	{
		assert (classID != null);
		assert (aimClass != null);

		assert (classID.compareTo(IDDispatcher.createClassID(aimClass)) == 0);
		assert (classID.getAimClass() == aimClass);
		assert (classID.isIDOf(aimClass));
		assert (classID.getAimClass() == aimClass);
	}

	private InstanceID createInstanceID(Object aimInstance)
			throws SecurityException, NoSuchMethodException,
			IllegalArgumentException, InstantiationException,
			IllegalAccessException, InvocationTargetException
	{
		assert (aimInstance != null);

		InstanceID lastInstanceID = IDDispatcher.createInstanceID(aimInstance);
		InstanceID newInstanceID = null;
		for (int i = 0; i < 1000; i++)
		{
			newInstanceID = IDDispatcher.createInstanceID(aimInstance);

			assert (newInstanceID != null);
			assert (lastInstanceID != null);
			assert (newInstanceID == lastInstanceID);
			assert (newInstanceID.equals(lastInstanceID));
			lastInstanceID = newInstanceID;
			checkInstanceIDAccuracy(newInstanceID, aimInstance);

			Constructor<InstanceID> constructor = InstanceID.class
					.getDeclaredConstructor(Object.class);
			constructor.setAccessible(true);
			InstanceID reflectInstanceID = constructor.newInstance(aimInstance);
			assert (reflectInstanceID != null);
			assert (reflectInstanceID != newInstanceID);
			// assert (reflectInstanceID.equals(newInstanceID));
			// checkInstanceIDAccuracy(reflectInstanceID, aimInstance);

			assert (newInstanceID != null);
		}

		assert (newInstanceID != null);
		return newInstanceID;
	}

	private void checkInstanceIDAccuracy(InstanceID instanceID,
			Object aimInstance) throws SecurityException,
			NoSuchMethodException, IllegalArgumentException,
			InstantiationException, IllegalAccessException,
			InvocationTargetException
	{
		assert (instanceID != null);
		assert (aimInstance != null);

		assert (instanceID.getAimInstance() == aimInstance);
		assert (instanceID.isIDOf(aimInstance));
		assert (instanceID.equals(IDDispatcher.createInstanceID(aimInstance)));
		assert (instanceID.getClassIDOfSameClass().getAimClass() == aimInstance
				.getClass());
		assert (instanceID.getClassIDOfSameClass() == IDDispatcher
				.createClassID(aimInstance.getClass()));
		assert (instanceID.getClassIDOfSameClass().equals(IDDispatcher
				.createClassID(aimInstance.getClass())));

		instanceID.isOfSameClass(aimInstance.getClass());
		instanceID.isOfSameClass(IDDispatcher.createClassID(aimInstance
				.getClass()));

		Constructor<ClassID> constructor = ClassID.class
				.getDeclaredConstructor(Class.class);
		constructor.setAccessible(true);
		ClassID reflectClassID = constructor
				.newInstance(aimInstance.getClass());
		assert (reflectClassID != null);
		instanceID.isOfSameClass(reflectClassID);

	}

	private void innerTestClassIDAndInstanceID()
	{
		try
		{
			// test class id
			ClassID classAID = createClassID(ClassA.class);
			ClassID classBID = createClassID(ClassB.class);
			ClassID classCID = createClassID(ClassC.class);

			ClassID classAAID = createClassID(ClassAA.class);
			ClassID classABID = createClassID(ClassAB.class);
			ClassID classABAID = createClassID(ClassABA.class);

			ClassID classAID2 = createClassID(ClassA.class);

			assert (!classAID.equals(classBID));
			assert (classAID.equals(classAID2));
			assert (classAID.hashCode() == classAID2.hashCode());

			assert (classAID.isIDOf(ClassA.class));
			assert (!classAID.isIDOf(ClassB.class));
			assert (classAID.isIDOf(new ClassA().getClass()));
			assert (!classAID.isIDOf(new ClassB().getClass()));

			ClassID[] classIDs = { classAID, classBID, classCID, classAAID,
					classABID, classABAID, };
			Arrays.sort(classIDs);
			for (int i = 0; i < classIDs.length - 1; i++)
			{
				assert (!classIDs[i].equals(classIDs[i + 1]));
				assert (classIDs[i].compareTo(classIDs[i + 1]) < 0);
			}

			// test instance id
			ClassA classA = new ClassA();
			ClassB classB = new ClassB();
			ClassC classC = new ClassC();

			ClassAA classAA = new ClassAA();
			ClassAB classAB = new ClassAB();
			ClassABA classABA = new ClassABA();

			InstanceID classAInstanceID = createInstanceID(classA);
			InstanceID classBInstanceID = createInstanceID(classB);
			InstanceID classCInstanceID = createInstanceID(classC);

			InstanceID classAAInstanceID = createInstanceID(classAA);
			InstanceID classABInstanceID = createInstanceID(classAB);
			InstanceID classABAInstanceID = createInstanceID(classABA);

			InstanceID classAInstanceID2 = createInstanceID(classA);

			InstanceID classAInstanceID3 = createInstanceID(new ClassA());
			InstanceID classBInstanceID3 = createInstanceID(new ClassB());
			InstanceID classCInstanceID3 = createInstanceID(new ClassC());

			InstanceID classAAInstanceID3 = createInstanceID(new ClassAA());
			InstanceID classABInstanceID3 = createInstanceID(new ClassAB());
			InstanceID classABAInstanceID3 = createInstanceID(new ClassABA());

			List<InstanceID> list = new LinkedList<InstanceID>();
			list.add(classCInstanceID);
			list.add(classABAInstanceID);
			list.add(classAAInstanceID);
			list.add(classABInstanceID);
			list.add(classBInstanceID3);
			list.add(classCInstanceID3);
			list.add(classAAInstanceID3);
			list.add(classABInstanceID3);
			list.add(classABAInstanceID3);
			list.clear();
			list=null;

			assert (classAInstanceID.equals(classAInstanceID2));
			assert ((!classAInstanceID3.isValid())
					||(!classAInstanceID.equals(classAInstanceID3)));
			assert (!classAInstanceID.equals(classBInstanceID));
			assert (classAInstanceID.hashCode() == classAInstanceID2.hashCode());

			assert (classAInstanceID.getClassIDOfSameClass()
					.equals(createClassID(ClassA.class)));
			assert (!classAInstanceID.isIDOf(new ClassA()));
			assert (classAInstanceID.isIDOf(classA));
			assert (!classAInstanceID.isIDOf(classB));
			assert (classAInstanceID.isOfSameClass(ClassA.class));
			assert (!classAInstanceID.isOfSameClass(ClassB.class));
			assert (classAInstanceID.isOfSameClass(createClassID(ClassA.class)));
			assert (!classAInstanceID
					.isOfSameClass(createClassID(ClassB.class)));

			// test class id and instance id
			assert (classAID.isOfSameClass(classAInstanceID));
			assert (!classAID.isOfSameClass(classBInstanceID));
			assert (classBInstanceID.isOfSameClass(classBID));
			assert (!classBInstanceID.isOfSameClass(classCID));

			assert (classAID.isOfSameClass(classA));
			assert (!classAID.isOfSameClass(classB));

			// test 4 test for garbage collection
			InstanceID classABAInstanceID2 = IDDispatcher
					.createInstanceID(classABA);
			assert (classABAInstanceID2 != null);
			int oldHashCode=classABAInstanceID2.hashCode();
			String oldString=classABAInstanceID2.toString();
			assert(oldString.equals(classABA.toString()));
			
			classABA = null;
			System.gc();
			System.gc();
			
			while(classABAInstanceID2.isValid())
			{
				Thread.sleep(10);
			}
			
			assert(classABAInstanceID2.equals(classABAInstanceID2));
			assert(!classABAInstanceID2.equals(classAAInstanceID3));
			assert(classABAInstanceID2.hashCode()==oldHashCode);
			assert(classABAInstanceID2.getAimInstance()==null);
			assert(classABAInstanceID2.getClassIDOfSameClass()==IDDispatcher.createClassID(ClassABA.class));
			assert(!classABAInstanceID2.isIDOf(new ClassABA()));
			assert(classABAInstanceID2.isOfSameClass(ClassABA.class));
			assert(!classABAInstanceID2.isOfSameClass(ClassAB.class));
			assert(classABAInstanceID2.isOfSameClass(IDDispatcher.createClassID(ClassABA.class)));
			assert(!classABAInstanceID2.isOfSameClass(IDDispatcher.createClassID(ClassAB.class)));
			assert(classABAInstanceID2.getClassIDOfSameClass()==IDDispatcher.createClassID(ClassABA.class));
			assert(classABAInstanceID2.toString().equals(oldString));
			
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}

	}

	private void innerTestMessCreateAndRelease()
	{
		try
		{
			for (int i = 0; i < 1000000; i++)
			{
				ClassID cid = IDDispatcher.createClassID(String.class);
				cid = IDDispatcher
						.createClassID("com.accela.TestCases.ClassIDAndInstanceID.ClassA");
				assert (cid.isIDOf(ClassA.class));
				assert (!cid.isIDOf(String.class));
				InstanceID iid = IDDispatcher.createInstanceID(new Object());
				assert (!iid.isValid()||iid.isOfSameClass(Object.class));
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	/**
	 * This test consumes huge amount of time
	 */
	private void innerTestMassCreation()
	{
		try
		{
			// test System.identityHashCode() fault
			int faultCount = 0;

			List<List<String>> strListList = new LinkedList<List<String>>();
			for (int i = 0; i < 100; i++)
			{
				List<String> list = new LinkedList<String>();
				for (int j = 0; j < 100; j++)
				{
					list.add("" + Math.random());
				}

				strListList.add(list);
			}

			for (List<String> list1 : strListList)
			{
				for (String str1 : list1)
				{
					for (List<String> list2 : strListList)
					{
						for (String str2 : list2)
						{
							if (str1 == str2)
							{
								continue;
							}

							int str1Code = System.identityHashCode(str1);
							int str2Code = System.identityHashCode(str2);

							Field field = String.class
									.getDeclaredField("value");
							field.setAccessible(true);

							int str1ArrayCode = System.identityHashCode(field
									.get(str1));
							int str2ArrayCode = System.identityHashCode(field
									.get(str2));

							Set<Integer> set = new HashSet<Integer>();
							set.add(str1Code);
							set.add(str2Code);
							set.add(str1ArrayCode);
							set.add(str2ArrayCode);

							if (set.size() < 4)
							{
								faultCount++;
							}

							// ==

							InstanceID str1ID = IDDispatcher
									.createInstanceID(str1);
							InstanceID str2ID = IDDispatcher
									.createInstanceID(str2);

							InstanceID str1ArrayID = IDDispatcher
									.createInstanceID(field.get(str1));
							InstanceID str2ArrayID = IDDispatcher
									.createInstanceID(field.get(str2));

							Set<InstanceID> set2 = new HashSet<InstanceID>();
							set2.add(str1ID);
							set2.add(str2ID);
							set2.add(str1ArrayID);
							set2.add(str2ArrayID);

							if (set2.size() < 4)
							{
								assert (false);
							}
						}
					}
				}
			}// outer for

			assert (faultCount > 0);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	public void testExtreme()
	{
		this.innerTestClassIDAndInstanceID();
		System.out.println("10%...");
		this.innerTestClassIDAndInstanceID();
		System.out.println("20%...");

		this.innerTestMessCreateAndRelease();
		System.out.println("30%...");
		this.innerTestMassCreation();
		System.out.println("40%...");
		this.innerTestMessCreateAndRelease();
		System.out.println("50%...");

		this.innerTestClassIDAndInstanceID();
		System.out.println("60%...");
		this.innerTestClassIDAndInstanceID();
		System.out.println("70%...");

		this.innerTestMessCreateAndRelease();
		System.out.println("80%...");
		this.innerTestMassCreation();
		System.out.println("90%...");
		this.innerTestMessCreateAndRelease();
		System.out.println("100%...");
	}

}
