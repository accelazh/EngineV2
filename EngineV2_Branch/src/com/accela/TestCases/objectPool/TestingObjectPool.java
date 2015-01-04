package com.accela.TestCases.objectPool;

import com.accela.ObjectPool.ObjectPool;

import junit.framework.TestCase;

public class TestingObjectPool extends TestCase
{

	protected void setUp() throws Exception
	{
		super.setUp();
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
	}

	@SuppressWarnings("static-access")
	public void testObjectPool()
	{
		ClassA classA = new ClassA();
		ClassAA classAA = new ClassAA();
		ClassAB classAB = new ClassAB();
		ClassABA classABA = new ClassABA();
		ClassB classB = new ClassB();
		ClassC classC = new ClassC();

		ClassA classA2 = new ClassA();
		ClassAA classAA2 = new ClassAA();
		ClassAB classAB2 = new ClassAB();
		ClassABA classABA2 = new ClassABA();
		ClassB classB2 = new ClassB();
		ClassC classC2 = new ClassC();

		ObjectPool.put(classA, true);

		assert (ObjectPool.retrieve(ClassA.class) == classA);
		assert (ObjectPool.retrieve(ClassB.class)!=null);
		assert (null == ObjectPool.retrieve(ClassABA.class));

		assert (classA.getClassAA() == null);
		assert (classA.getClassAB() == null);

		assert (classA.getSTR().equals("go to hell!"));
		assert (classA.getSTR2().equals("go to hell, too!!"));
		assert (classA.getStr3().equals("go to hell, either!!"));
		assert (ClassA.getStr3().equals("go to hell, either!!"));

		assert (classA.getObj1() != null);
		assert (classA.getObj2() != null);
		assert (classA.getObj3() != null);

		assert (ClassA.getObj2() != null);
		assert (ClassA.getObj3() != null);

		ObjectPool.put(classA, true);
		ObjectPool.put(classAA, true);
		ObjectPool.put(classAB, true);
		ObjectPool.put(classABA, true);
		ObjectPool.put(classB, true);
		ObjectPool.put(classC, true);

		ObjectPool.put(classA2, true);
		ObjectPool.put(classAA2, true);
		ObjectPool.put(classAB2, true);
		ObjectPool.put(classABA2, true);
		ObjectPool.put(classB2, true);
		ObjectPool.put(classC2, true);

		ClassAB cAB = new ClassAB();
		ObjectPool.put(cAB, true);

		for (int i = 0; i < 100; i++)
		{
			boolean hasException = false;
			try
			{
				ObjectPool.put(classB, true);
			} catch (Exception ex)
			{
				assert (ex instanceof IllegalArgumentException);
				hasException = true;
			}
			assert (hasException);
		}

		ClassA classA3 = ObjectPool.retrieve(ClassA.class);
		ClassAA classAA3 = ObjectPool.retrieve(ClassAA.class);
		ClassAB classAB3 = ObjectPool.retrieve(ClassAB.class);
		ClassABA classABA3 = ObjectPool.retrieve(ClassABA.class);
		ClassB classB3 = ObjectPool.retrieve(ClassB.class);
		ClassC classC3 = ObjectPool.retrieve(ClassC.class);

		ClassA classA4 = ObjectPool.retrieve(ClassA.class);
		ClassAA classAA4 = ObjectPool.retrieve(ClassAA.class);
		ClassAB classAB4 = ObjectPool.retrieve(ClassAB.class);
		ClassABA classABA4 = ObjectPool.retrieve(ClassABA.class);
		ClassB classB4 = ObjectPool.retrieve(ClassB.class);
		ClassC classC4 = ObjectPool.retrieve(ClassC.class);

		ClassAB cAB2 = ObjectPool.retrieve(ClassAB.class);
		assert (cAB2 != null);

		assert (classA3 != null);
		assert (classAA3 != null);
		assert (classAB3 != null);
		assert (classABA3 != null);
		assert (classB3 != null);
		assert (classC3 != null);

		assert (classA4 != null);
		assert (classAA4 != null);
		assert (classAB4 != null);
		assert (classABA4 != null);
		assert (classB4 != null);
		assert (classC4 != null);

		assert (classA3.getClassAA() == null);
		assert (classA3.getClassAB() == null);

		assert (classAA3.getClassAA() == null);
		assert (classAA3.getClassAB() == null);
		assert (classAB3.getClassAA() == null);
		assert (classAB3.getClassAB() == null);

		assert (classABA3.getClassA() == null);
		assert (classABA3.getClassAA() == null);
		assert (classABA3.getClassAB() == null);

		assert (classB3.getClassAA() == null);
		assert (classB3.getClassAB() == null);

		assert (classC3 != null);

		assert (classA4.getClassAA() == null);
		assert (classA4.getClassAB() == null);

		assert (classAA4.getClassAA() == null);
		assert (classAA4.getClassAB() == null);
		assert (classAB4.getClassAA() == null);
		assert (classAB4.getClassAB() == null);

		assert (classABA4.getClassA() == null);
		assert (classABA4.getClassAA() == null);
		assert (classABA4.getClassAB() == null);

		assert (classB4.getClassAA() == null);
		assert (classB4.getClassAB() == null);

		assert (classC4 != null);

		assert (cAB2.getClassAA() == null);
		assert (cAB2.getClassAB() == null);

		assert (ObjectPool.retrieve(ClassA.class)!=null);
		assert (ObjectPool.retrieve(ClassAA.class) == null);
		assert (ObjectPool.retrieve(ClassAB.class) == null);
		assert (ObjectPool.retrieve(ClassABA.class) == null);
		assert (ObjectPool.retrieve(ClassB.class) != null);
		assert (ObjectPool.retrieve(ClassC.class) != null);

		for (int i = 0; i < 100; i++)
		{
			ObjectPool.retrieve(ClassB.class);
		}
		for (int i = 0; i < 100; i++)
		{
			assert(ObjectPool.retrieve(ClassB.class)==null);
		}

		// test IObjectPoolSensitive
		ClassD classD = new ClassD();
		assert (!classD.isPutInvoked());
		assert (!classD.isRetrieveInvoked());

		ObjectPool.put(classD);
		assert (classD.isPutInvoked());
		assert (!classD.isRetrieveInvoked());

		assert (ObjectPool.retrieve(ClassD.class) == classD);
		assert (classD.isPutInvoked());
		assert (classD.isRetrieveInvoked());
		
		//test non clean put
		ClassA a=new ClassA();
		ObjectPool.put(a);
		assert(a.getClassAA()!=null);
		assert(a.getClassAB()!=null);

	}

}
