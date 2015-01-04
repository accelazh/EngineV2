package com.accela.TestCases.container;


import com.accela.Container.Container;
import com.accela.Container.ContainerModificationLockedException;
import com.accela.Container.TestingOverlappedKeysRandom;

import junit.framework.TestCase;

public class TestingContainerSingleThread extends TestCase
{
	public String[] items;

	public String[] items2;

	protected void setUp() throws Exception
	{
		super.setUp();
		
		if(Container.isModificationLocked())
		{
			throw new IllegalStateException("Container is locked!");
		}
		
		items = new String[100];
		for (int i = 0; i < items.length; i++)
		{
			items[i] = (i + 1) + ""+"a";
		}

		items2 = new String[100];
		for (int i = 0; i < items2.length; i++)
		{
			items2[i] = (i + 1) + "" + items.length+"a";
		}
		
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();

		items = null;
		items2 = null;
		
	}

	public void testLookup()
	{
		Container.clear();
		assert(Container.isEmpty());
		
		// test global item
		for (int i = 0; i < items.length; i++)
		{
			Container.putGlobalItem(items[i], items[i]);
		}
		for (int i = 0; i < items.length; i++)
		{
			Container.putGlobalItem(items2[i], items[i]);
		}
		for (int i = 0; i < items.length; i++)
		{
			for (int j = 0; j < items.length; j++)
			{
				assert (Container.lookup(items[j], items[i]) == items[i]);
				assert (Container
						.lookup(items[items.length - 1 - j], items2[i]) == (items[i]));
				assert (Container.lookup("g" + i, "good" + i) == null);
			}
		}

		assert (Container.removeGlobalItem("gogogo") == null);
		for (int i = 0; i < items2.length; i++)
		{
			assert (Container.removeGlobalItem(items2[i]) == (items[i]));
		}
		for (int i = 0; i < items.length; i++)
		{
			for (int j = 0; j < items.length; j++)
			{
				assert (Container.lookup(items[j], items[i]) == (items[i]));
				assert (Container
						.lookup(items[items.length - 1 - j], items2[i]) == null);
			}
		}

		Container.clear();
		assert (Container.isEmpty());
		for (int i = 0; i < items.length; i++)
		{
			for (int j = 0; j < items.length; j++)
			{
				assert (Container.lookup(items[j], items[i]) == null);
				assert (Container.lookup(items[j], items2[i]) == null);
			}
		}

		// test class item
		for (int i = 0; i < items.length; i++)
		{
			Container.putGlobalItem(items2[items2.length - 1 - i], items2[i]);
		}

		Class<?> c1 = ClassA.class;
		Class<?> c2 = ClassAA.class;
		assert (c1 != c2);
		assert (c2 != c1);
		assert (!(c1.isInstance(c2)));
		assert (!(c2.isInstance(c1)));

		for (int i = 0; i < 10; i++)
		{
			Container.putClassItem(ClassA.class, items[i], items[i]);
		}
		for (int i = 10; i < 20; i++)
		{
			Container.putClassItem(ClassAA.class, items[i], items[i]);
		}
		for (int i = 20; i < 30; i++)
		{
			Container.putClassItem(ClassAB.class, items[i], items[i]);
		}
		for (int i = 30; i < 40; i++)
		{
			Container.putClassItem(ClassABA.class, items[i], items[i]);
		}
		for (int i = 40; i < 50; i++)
		{
			Container.putClassItem(ClassB.class, items[i], items[i]);
		}

		for (int i = 0; i < items2.length; i++)
		{
			assert (Container.lookup(ClassABA.class, items2[items2.length - 1
					- i]) == (items2[i]));
		}

		for (int i = 0; i < 10; i++)
		{
			ClassA a = new ClassA();
			assert (Container.lookup(new ClassA(), items[i]) == (items[i]));
			assert (Container.lookup(a, items[i]) == (items[i]));
			assert (Container.lookup(a, items[i]) == (items[i]));
			assert (Container.lookup(new ClassA(), items2[i]) == (items2[items2.length
					- 1 - i]));
			assert (Container.lookup(new ClassA(), items[i + 10]) == null);
			assert (Container.lookup(new ClassA(), items[i + 20]) == null);
			assert (Container.lookup(new ClassA(), items[i + 30]) == null);
			assert (Container.lookup(new ClassA(), items[i + 40]) == null);

			assert (Container.lookup(ClassA.class, items[i]) == null);
		}
		for (int i = 10; i < 20; i++)
		{
			ClassAA a = new ClassAA();
			assert (Container.lookup(new ClassAA(), items[i]) == (items[i]));
			assert (Container.lookup(a, items[i]) == (items[i]));
			assert (Container.lookup(a, items[i]) == (items[i]));
			assert (Container.lookup(new ClassAA(), items2[i]) == (items2[items2.length
					- 1 - i]));
			assert (Container.lookup(new ClassAA(), items[i - 10]) == null);
			assert (Container.lookup(new ClassAA(), items[i + 10]) == null);
			assert (Container.lookup(new ClassAA(), items[i + 20]) == null);
			assert (Container.lookup(new ClassAA(), items[i + 30]) == null);

			assert (Container.lookup(ClassAA.class, items[i]) == null);
		}
		for (int i = 20; i < 30; i++)
		{
			ClassAB a = new ClassAB();
			assert (Container.lookup(new ClassAB(), items[i]) == (items[i]));
			assert (Container.lookup(a, items[i]) == (items[i]));
			assert (Container.lookup(a, items[i]) == (items[i]));
			assert (Container.lookup(new ClassAB(), items2[i]) == (items2[items2.length
					- 1 - i]));
			assert (Container.lookup(new ClassAB(), items[i - 20]) == null);
			assert (Container.lookup(new ClassAB(), items[i - 10]) == null);
			assert (Container.lookup(new ClassAB(), items[i + 10]) == null);
			assert (Container.lookup(new ClassAB(), items[i + 20]) == null);

			assert (Container.lookup(ClassAB.class, items[i]) == null);
		}
		for (int i = 30; i < 40; i++)
		{
			ClassABA a = new ClassABA();
			assert (Container.lookup(new ClassABA(), items[i]) == (items[i]));
			assert (Container.lookup(a, items[i]) == (items[i]));
			assert (Container.lookup(a, items[i]) == (items[i]));
			assert (Container.lookup(new ClassABA(), items2[i]) == (items2[items2.length
					- 1 - i]));
			assert (Container.lookup(new ClassABA(), items[i - 30]) == null);
			assert (Container.lookup(new ClassABA(), items[i - 20]) == null);
			assert (Container.lookup(new ClassABA(), items[i - 10]) == null);
			assert (Container.lookup(new ClassABA(), items[i + 10]) == null);

			assert (Container.lookup(ClassABA.class, items[i]) == null);
		}
		for (int i = 40; i < 50; i++)
		{
			ClassB a = new ClassB();
			assert (Container.lookup(new ClassB(), items[i]) == (items[i]));
			assert (Container.lookup(a, items[i]) == (items[i]));
			assert (Container.lookup(a, items[i]) == (items[i]));
			assert (Container.lookup(new ClassB(), items2[i]) == (items2[items2.length
					- 1 - i]));
			assert (Container.lookup(new ClassB(), items[i - 40]) == null);
			assert (Container.lookup(new ClassB(), items[i - 30]) == null);
			assert (Container.lookup(new ClassB(), items[i - 20]) == null);
			assert (Container.lookup(new ClassB(), items[i - 10]) == null);

			assert (Container.lookup(ClassB.class, items[i]) == null);
		}

		// test instance item

		ClassA[] classAs = new ClassA[10];
		ClassB[] classBs = new ClassB[classAs.length];
		ClassC[] classCs = new ClassC[classAs.length];
		ClassAB[] classABs = new ClassAB[classAs.length];
		ClassABA[] classABAs = new ClassABA[classAs.length];

		for (int i = 0; i < classAs.length; i++)
		{
			classAs[i] = new ClassA();
			classBs[i] = new ClassB();
			classCs[i] = new ClassC();
			classABs[i] = new ClassAB();
			classABAs[i] = new ClassABA();
		}
		assert (10 == classAs.length);

		for (int i = 0; i < 10; i++)
		{
			Container.putInstanceItem(classAs[i], items[i + 50], items[i + 50]);
			Container.putInstanceItem(classBs[i], items[i + 60], items[i + 60]);
			Container.putInstanceItem(classCs[i], items[i + 70], items[i + 70]);
			Container
					.putInstanceItem(classABs[i], items[i + 80], items[i + 80]);
			Container.putInstanceItem(classABAs[i], items[i + 90],
					items[i + 90]);
		}

		Object[][] classes = new Object[][] { classAs, classBs, classCs,
				classABs, classABAs, };

		for (int i = 0; i < classes.length; i++)
		{
			for (int j = 0; j < classAs.length; j++)
			{
				assert (Container.lookup(classes[i][j], items[50 + i * 10 + j]) == (items[50
						+ i * 10 + j]));
				assert (Container.lookup(classes[i], items[50 + i * 10 + j]) == null);

				for (int k = 0; k < classAs.length / 2; k++)
				{
					assert (Container.lookup(classes[i][k], items[50 + i * 10
							+ classAs.length - 1 - k]) == null);
				}

				for (int k = 0; k < classes.length; k++)
				{
					if (i == k)
					{
						assert (Container.lookup(classes[i][j], items[50 + k
								* 10 + j]) == (items[50 + i * 10 + j]));
					} else
					{
						assert (Container.lookup(classes[i][j], items[50 + k
								* 10 + j]) == null);
					}
				}

				for (int k = 0; k < items2.length; k++)
				{
					assert (Container.lookup(classes[i][j], items2[k]) == (items2[items2.length
							- 1 - k]));
				}

				for (int k = 0; k < classes.length; k++)
				{
					assert (Container.lookup(classes[0][k], items[k]) == (items[k]));
					assert (Container.lookup(classes[0][k], items[k + 10]) == null);
					assert (Container.lookup(classes[0][k], items[k + 20]) == null);
					assert (Container.lookup(classes[0][k], items[k + 30]) == null);
					assert (Container.lookup(classes[0][k], items[k + 40]) == null);

					assert (Container.lookup(classes[3][k], items[k + 20]) == (items[k + 20]));
					assert (Container.lookup(classes[3][k], items[k + 10]) == null);
					assert (Container.lookup(classes[3][k], items[k]) == null);
					assert (Container.lookup(classes[3][k], items[k + 30]) == null);
					assert (Container.lookup(classes[3][k], items[k + 40]) == null);

					assert (Container.lookup(classes[4][k], items[k + 30]) == (items[k + 30]));
					assert (Container.lookup(classes[4][k], items[k + 20]) == null);

					assert (Container.lookup(classes[1][k], items[k + 40]) == (items[k + 40]));
					assert (Container.lookup(classes[1][k], items[k + 10]) == null);
					assert (Container.lookup(classes[1][k], items[k]) == null);
					
					assert (Container.lookup(classes[2][k], items[k + 40]) == null);
					assert (Container.lookup(classes[2][k], items[k + 20]) == null);
					assert (Container.lookup(classes[2][k], items[k]) == null);
				}

				for (int inner_i = 0; inner_i < 10; inner_i++)
				{
					ClassA a = new ClassA();
					assert (Container.lookup(new ClassA(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassA(), items2[inner_i]) == (items2[items2.length
							- 1 - inner_i]));
					assert (Container.lookup(new ClassA(), items[inner_i + 10]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 20]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 30]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 40]) == null);
				}
				for (int inner_i = 10; inner_i < 20; inner_i++)
				{
					ClassAA a = new ClassAA();
					assert (Container.lookup(new ClassAA(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassAA(), items2[inner_i]) == (items2[items2.length
							- 1 - inner_i]));
					assert (Container
							.lookup(new ClassAA(), items[inner_i - 10]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 10]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 20]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 30]) == null);
				}
				for (int inner_i = 20; inner_i < 30; inner_i++)
				{
					ClassAB a = new ClassAB();
					assert (Container.lookup(new ClassAB(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassAB(), items2[inner_i]) == (items2[items2.length
							- 1 - inner_i]));
					assert (Container
							.lookup(new ClassAB(), items[inner_i - 20]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i - 10]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i + 10]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i + 20]) == null);
				}
				for (int inner_i = 30; inner_i < 40; inner_i++)
				{
					ClassABA a = new ClassABA();
					assert (Container.lookup(new ClassABA(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassABA(), items2[inner_i]) == (items2[items2.length
							- 1 - inner_i]));
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 30]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 20]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 10]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i + 10]) == null);
				}
				for (int inner_i = 40; inner_i < 50; inner_i++)
				{
					ClassB a = new ClassB();
					assert (Container.lookup(new ClassB(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassB(), items2[inner_i]) == (items2[items2.length
							- 1 - inner_i]));
					assert (Container.lookup(new ClassB(), items[inner_i - 40]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 30]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 20]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 10]) == null);
				}

			}
		}

		// test remove global items
		for (int i = 0; i < items2.length; i++)
		{
			assert (Container.removeGlobalItem(items2[i]) == (items2[items2.length
					- i - 1]));
		}
		for (int i = 0; i < items2.length; i++)
		{
			assert (Container.removeGlobalItem(items2[i]) == null);
		}

		for (int i = 0; i < classes.length; i++)
		{
			for (int j = 0; j < classAs.length; j++)
			{
				assert (Container.lookup(classes[i][j], items[50 + i * 10 + j]) == (items[50
						+ i * 10 + j]));
				assert (Container.lookup(classes[i], items[50 + i * 10 + j]) == null);

				for (int k = 0; k < classAs.length / 2; k++)
				{
					assert (Container.lookup(classes[i][k], items[50 + i * 10
							+ classAs.length - 1 - k]) == null);
				}

				for (int k = 0; k < classes.length; k++)
				{
					if (i == k)
					{
						assert (Container.lookup(classes[i][j], items[50 + k
								* 10 + j]) == (items[50 + i * 10 + j]));
					} else
					{
						assert (Container.lookup(classes[i][j], items[50 + k
								* 10 + j]) == null);
					}
				}

				for (int k = 0; k < items2.length; k++)
				{
					assert (Container.lookup(classes[i][j], items2[k]) == null);
				}

				for (int k = 0; k < classes.length; k++)
				{
					assert (Container.lookup(classes[0][k], items[k]) == (items[k]));
					assert (Container.lookup(classes[0][k], items[k + 10]) == null);
					assert (Container.lookup(classes[0][k], items[k + 20]) == null);
					assert (Container.lookup(classes[0][k], items[k + 30]) == null);
					assert (Container.lookup(classes[0][k], items[k + 40]) == null);

					assert (Container.lookup(classes[3][k], items[k + 20]) == (items[k + 20]));
					assert (Container.lookup(classes[3][k], items[k + 10]) == null);
					assert (Container.lookup(classes[3][k], items[k]) == null);
					assert (Container.lookup(classes[3][k], items[k + 30]) == null);
					assert (Container.lookup(classes[3][k], items[k + 40]) == null);

					assert (Container.lookup(classes[4][k], items[k + 30]) == (items[k + 30]));
					assert (Container.lookup(classes[4][k], items[k + 20]) == null);

					assert (Container.lookup(classes[1][k], items[k + 40]) == (items[k + 40]));
					assert (Container.lookup(classes[1][k], items[k + 10]) == null);
					assert (Container.lookup(classes[1][k], items[k]) == null);
					
					assert (Container.lookup(classes[2][k], items[k + 40]) == null);
					assert (Container.lookup(classes[2][k], items[k + 20]) == null);
					assert (Container.lookup(classes[2][k], items[k]) == null);
				}

				for (int inner_i = 0; inner_i < 10; inner_i++)
				{
					ClassA a = new ClassA();
					assert (Container.lookup(new ClassA(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassA(), items2[inner_i]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 10]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 20]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 30]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 40]) == null);
				}
				for (int inner_i = 10; inner_i < 20; inner_i++)
				{
					ClassAA a = new ClassAA();
					assert (Container.lookup(new ClassAA(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassAA(), items2[inner_i]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i - 10]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 10]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 20]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 30]) == null);
				}
				for (int inner_i = 20; inner_i < 30; inner_i++)
				{
					ClassAB a = new ClassAB();
					assert (Container.lookup(new ClassAB(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassAB(), items2[inner_i]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i - 20]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i - 10]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i + 10]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i + 20]) == null);
				}
				for (int inner_i = 30; inner_i < 40; inner_i++)
				{
					ClassABA a = new ClassABA();
					assert (Container.lookup(new ClassABA(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassABA(), items2[inner_i]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 30]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 20]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 10]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i + 10]) == null);
				}
				for (int inner_i = 40; inner_i < 50; inner_i++)
				{
					ClassB a = new ClassB();
					assert (Container.lookup(new ClassB(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassB(), items2[inner_i]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 40]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 30]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 20]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 10]) == null);
				}

			}
		}

		// test remove instance items
		for (int i = 0; i < 10; i++)
		{
			assert (Container.removeInstanceItem(classAs[i], items[i + 50]) == (items[i + 50]));
			assert (Container.removeInstanceItem(classBs[i], items[i + 60]) == (items[i + 60]));
			assert (Container.removeInstanceItem(classCs[i], items[i + 70]) == (items[i + 70]));
			assert (Container.removeInstanceItem(classABs[i], items[i + 80]) == (items[i + 80]));
			assert (Container.removeInstanceItem(classABAs[i], items[i + 90]) == (items[i + 90]));
		}
		for (int i = 0; i < 10; i++)
		{
			assert (Container.removeInstanceItem(classAs[i], items[i + 50]) == null);
			assert (Container.removeInstanceItem(classBs[i], items[i + 60]) == null);
			assert (Container.removeInstanceItem(classCs[i], items[i + 70]) == null);
			assert (Container.removeInstanceItem(classABs[i], items[i + 80]) == null);
			assert (Container.removeInstanceItem(classABAs[i], items[i + 90]) == null);
		}

		for (int i = 0; i < classes.length; i++)
		{
			for (int j = 0; j < classAs.length; j++)
			{
				assert (Container.lookup(classes[i][j], items[50 + i * 10 + j]) == null);
				assert (Container.lookup(classes[i], items[50 + i * 10 + j]) == null);

				for (int k = 0; k < classAs.length / 2; k++)
				{
					assert (Container.lookup(classes[i][k], items[50 + i * 10
							+ classAs.length - 1 - k]) == null);
				}

				for (int k = 0; k < classes.length; k++)
				{
					if (i == k)
					{
						assert (Container.lookup(classes[i][j], items[50 + k
								* 10 + j]) == null);
					} else
					{
						assert (Container.lookup(classes[i][j], items[50 + k
								* 10 + j]) == null);
					}
				}

				for (int k = 0; k < items2.length; k++)
				{
					assert (Container.lookup(classes[i][j], items2[k]) == null);
				}

				for (int k = 0; k < classes.length; k++)
				{
					assert (Container.lookup(classes[0][k], items[k]) == (items[k]));
					assert (Container.lookup(classes[0][k], items[k + 10]) == null);
					assert (Container.lookup(classes[0][k], items[k + 20]) == null);
					assert (Container.lookup(classes[0][k], items[k + 30]) == null);
					assert (Container.lookup(classes[0][k], items[k + 40]) == null);

					assert (Container.lookup(classes[3][k], items[k + 20]) == (items[k + 20]));
					assert (Container.lookup(classes[3][k], items[k + 10]) == null);
					assert (Container.lookup(classes[3][k], items[k]) == null);
					assert (Container.lookup(classes[3][k], items[k + 30]) == null);
					assert (Container.lookup(classes[3][k], items[k + 40]) == null);

					assert (Container.lookup(classes[4][k], items[k + 30]) == (items[k + 30]));
					assert (Container.lookup(classes[4][k], items[k + 20]) == null);

					assert (Container.lookup(classes[1][k], items[k + 40]) == (items[k + 40]));
					assert (Container.lookup(classes[1][k], items[k + 10]) == null);
					assert (Container.lookup(classes[1][k], items[k]) == null);
					
					assert (Container.lookup(classes[2][k], items[k + 40]) == null);
					assert (Container.lookup(classes[2][k], items[k + 20]) == null);
					assert (Container.lookup(classes[2][k], items[k]) == null);
				}

				for (int inner_i = 0; inner_i < 10; inner_i++)
				{
					ClassA a = new ClassA();
					assert (Container.lookup(new ClassA(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassA(), items2[inner_i]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 10]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 20]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 30]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 40]) == null);
				}
				for (int inner_i = 10; inner_i < 20; inner_i++)
				{
					ClassAA a = new ClassAA();
					assert (Container.lookup(new ClassAA(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassAA(), items2[inner_i]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i - 10]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 10]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 20]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 30]) == null);
				}
				for (int inner_i = 20; inner_i < 30; inner_i++)
				{
					ClassAB a = new ClassAB();
					assert (Container.lookup(new ClassAB(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassAB(), items2[inner_i]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i - 20]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i - 10]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i + 10]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i + 20]) == null);
				}
				for (int inner_i = 30; inner_i < 40; inner_i++)
				{
					ClassABA a = new ClassABA();
					assert (Container.lookup(new ClassABA(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassABA(), items2[inner_i]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 30]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 20]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 10]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i + 10]) == null);
				}
				for (int inner_i = 40; inner_i < 50; inner_i++)
				{
					ClassB a = new ClassB();
					assert (Container.lookup(new ClassB(), items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(a, items[inner_i]) == (items[inner_i]));
					assert (Container.lookup(new ClassB(), items2[inner_i]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 40]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 30]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 20]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 10]) == null);
				}

			}
		}

		// test remove class items
		for (int i = 0; i < 10; i++)
		{
			assert (Container.removeClassItem(ClassA.class, items[i]) == (items[i]));
		}
		for (int i = 10; i < 20; i++)
		{
			assert (Container.removeClassItem(ClassAA.class, items[i]) == (items[i]));
		}
		for (int i = 20; i < 30; i++)
		{
			assert (Container.removeClassItem(ClassAB.class, items[i]) == (items[i]));
		}
		for (int i = 30; i < 40; i++)
		{
			assert (Container.removeClassItem(ClassABA.class, items[i]) == (items[i]));
		}
		for (int i = 40; i < 50; i++)
		{
			assert (Container.removeClassItem(ClassB.class, items[i]) == items[i]);
		}

		for (int i = 0; i < 10; i++)
		{
			assert (Container.removeClassItem(ClassA.class, items[i]) == null);
		}
		for (int i = 10; i < 20; i++)
		{
			assert (Container.removeClassItem(ClassAA.class, items[i]) == null);
		}
		for (int i = 20; i < 30; i++)
		{
			assert (Container.removeClassItem(ClassAB.class, items[i]) == null);
		}
		for (int i = 30; i < 40; i++)
		{
			assert (Container.removeClassItem(ClassABA.class, items[i]) == null);
		}
		for (int i = 40; i < 50; i++)
		{
			assert (Container.removeClassItem(ClassB.class, items[i]) == null);
		}

		assert(Container.isEmpty());
		
		for (int i = 0; i < classes.length; i++)
		{
			for (int j = 0; j < classAs.length; j++)
			{
				assert (Container.lookup(classes[i][j], items[50 + i * 10 + j]) == null);
				assert (Container.lookup(classes[i], items[50 + i * 10 + j]) == null);

				for (int k = 0; k < classAs.length / 2; k++)
				{
					assert (Container.lookup(classes[i][k], items[50 + i * 10
							+ classAs.length - 1 - k]) == null);
				}

				for (int k = 0; k < classes.length; k++)
				{
					if (i == k)
					{
						assert (Container.lookup(classes[i][j], items[50 + k
								* 10 + j]) == null);
					} else
					{
						assert (Container.lookup(classes[i][j], items[50 + k
								* 10 + j]) == null);
					}
				}

				for (int k = 0; k < items2.length; k++)
				{
					assert (Container.lookup(classes[i][j], items2[k]) == null);
				}

				for (int k = 0; k < classes.length; k++)
				{
					assert (Container.lookup(classes[0][k], items[k]) == null);
					assert (Container.lookup(classes[0][k], items[k + 10]) == null);
					assert (Container.lookup(classes[0][k], items[k + 20]) == null);
					assert (Container.lookup(classes[0][k], items[k + 30]) == null);
					assert (Container.lookup(classes[0][k], items[k + 40]) == null);

					assert (Container.lookup(classes[3][k], items[k + 20]) == null);
					assert (Container.lookup(classes[3][k], items[k + 10]) == null);
					assert (Container.lookup(classes[3][k], items[k]) == null);
					assert (Container.lookup(classes[3][k], items[k + 30]) == null);
					assert (Container.lookup(classes[3][k], items[k + 40]) == null);

					assert (Container.lookup(classes[4][k], items[k + 30]) == null);
					assert (Container.lookup(classes[4][k], items[k + 20]) == null);

					assert (Container.lookup(classes[1][k], items[k + 40]) == null);
					assert (Container.lookup(classes[1][k], items[k + 10]) == null);
					assert (Container.lookup(classes[1][k], items[k]) == null);
					
					assert (Container.lookup(classes[2][k], items[k + 40]) == null);
					assert (Container.lookup(classes[2][k], items[k + 20]) == null);
					assert (Container.lookup(classes[2][k], items[k]) == null);
				}

				for (int inner_i = 0; inner_i < 10; inner_i++)
				{
					ClassA a = new ClassA();
					assert (Container.lookup(new ClassA(), items[inner_i]) == null);
					assert (Container.lookup(a, items[inner_i]) == null);
					assert (Container.lookup(a, items[inner_i]) == null);
					assert (Container.lookup(new ClassA(), items2[inner_i]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 10]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 20]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 30]) == null);
					assert (Container.lookup(new ClassA(), items[inner_i + 40]) == null);
				}
				for (int inner_i = 10; inner_i < 20; inner_i++)
				{
					ClassAA a = new ClassAA();
					assert (Container.lookup(new ClassAA(), items[inner_i]) == null);
					assert (Container.lookup(a, items[inner_i]) == null);
					assert (Container.lookup(a, items[inner_i]) == null);
					assert (Container.lookup(new ClassAA(), items2[inner_i]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i - 10]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 10]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 20]) == null);
					assert (Container
							.lookup(new ClassAA(), items[inner_i + 30]) == null);
				}
				for (int inner_i = 20; inner_i < 30; inner_i++)
				{
					ClassAB a = new ClassAB();
					assert (Container.lookup(new ClassAB(), items[inner_i]) == null);
					assert (Container.lookup(a, items[inner_i]) == null);
					assert (Container.lookup(a, items[inner_i]) == null);
					assert (Container.lookup(new ClassAB(), items2[inner_i]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i - 20]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i - 10]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i + 10]) == null);
					assert (Container
							.lookup(new ClassAB(), items[inner_i + 20]) == null);
				}
				for (int inner_i = 30; inner_i < 40; inner_i++)
				{
					ClassABA a = new ClassABA();
					assert (Container.lookup(new ClassABA(), items[inner_i]) == null);
					assert (Container.lookup(a, items[inner_i]) == null);
					assert (Container.lookup(a, items[inner_i]) == null);
					assert (Container.lookup(new ClassABA(), items2[inner_i]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 30]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 20]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i - 10]) == null);
					assert (Container.lookup(new ClassABA(),
							items[inner_i + 10]) == null);
				}
				for (int inner_i = 40; inner_i < 50; inner_i++)
				{
					ClassB a = new ClassB();
					assert (Container.lookup(new ClassB(), items[inner_i]) == null);
					assert (Container.lookup(a, items[inner_i]) == null);
					assert (Container.lookup(a, items[inner_i]) == null);
					assert (Container.lookup(new ClassB(), items2[inner_i]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 40]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 30]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 20]) == null);
					assert (Container.lookup(new ClassB(), items[inner_i - 10]) == null);
				}

			}
		}

		// at last
		assert (Container.isEmpty());
		Container.putGlobalItem("go", "no!");
		assert (!Container.isEmpty());
		Container.clear();
		assert (Container.isEmpty());

	}

	private void addSomethingToContainer()
	{
		ClassA[] classAs = new ClassA[10];
		ClassB[] classBs = new ClassB[classAs.length];
		ClassC[] classCs = new ClassC[classAs.length];
		ClassAB[] classABs = new ClassAB[classAs.length];
		ClassABA[] classABAs = new ClassABA[classAs.length];

		for (int i = 0; i < classAs.length; i++)
		{
			classAs[i] = new ClassA();
			classBs[i] = new ClassB();
			classCs[i] = new ClassC();
			classABs[i] = new ClassAB();
			classABAs[i] = new ClassABA();
		}
		assert (10 == classAs.length);

		for (int i = 0; i < 10; i++)
		{
			Container.putInstanceItem(classAs[i], items[i + 50], items[i + 50]);
			Container.putInstanceItem(classBs[i], items[i + 60], items[i + 60]);
			Container.putInstanceItem(classCs[i], items[i + 70], items[i + 70]);
			Container
					.putInstanceItem(classABs[i], items[i + 80], items[i + 80]);
			Container.putInstanceItem(classABAs[i], items[i + 90],
					items[i + 90]);
		}

		for (int i = 0; i < 10; i++)
		{
			Container.putClassItem(ClassA.class, items[i], items[i]);
		}
		for (int i = 10; i < 20; i++)
		{
			Container.putClassItem(ClassAA.class, items[i], items[i]);
		}
		for (int i = 20; i < 30; i++)
		{
			Container.putClassItem(ClassAB.class, items[i], items[i]);
		}
		for (int i = 30; i < 40; i++)
		{
			Container.putClassItem(ClassABA.class, items[i], items[i]);
		}
		for (int i = 40; i < 50; i++)
		{
			Container.putClassItem(ClassB.class, items[i], items[i]);
		}

		for (int i = 0; i < items.length; i++)
		{
			Container.putGlobalItem(items2[items2.length - 1 - i], items2[i]);
		}
	}

	public void testOverlappedKey()
	{
		Container.clear();
		assert(Container.isEmpty());
		
		addSomethingToContainer();

		// test 1
		ClassA temp = null;
		Container.putGlobalItem("1", temp = new ClassA());

		boolean hasException = false;
		try
		{
			Container.putGlobalItem("1", new ClassC());
		} catch (Exception ex)
		{
			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.putGlobalItem("1", new ClassA());
		} catch (Exception ex)
		{
			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);

		hasException = false;
		try
		{
			Container.putClassItem(ClassA.class, "1", new ClassB());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);

		hasException = false;
		
		try
		{
			Container.putInstanceItem(ClassC.class, "1", new ClassA());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		assert (Container.lookup("123", "1") == temp);

		// test 2
		ClassB temp2 = null;
		Container.putClassItem(ClassC.class, "2", temp2 = new ClassB());
		Container.putClassItem(ClassA.class, "2", new ClassC());
		
		hasException = false;
		try
		{
			Container.putClassItem(ClassA.class, "2", new ClassA());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.putClassItem(ClassC.class, "2", new ClassA());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.putGlobalItem("2", new ClassA());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		Container.putGlobalItem("3", new ClassA());
		hasException = false;
		try
		{
			Container.putClassItem(ClassABA.class ,"3", new ClassABA());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.putInstanceItem(new ClassA() ,"2", new ClassA());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.putInstanceItem(new ClassC() ,"2", new ClassA());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
	
		Container.putInstanceItem(new ClassAA() ,"2", new ClassA());
		hasException = false;
		try
		{
			Container.putClassItem(ClassAA.class ,"2", new ClassA());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		assert(Container.lookup(new ClassC(), "2")==temp2);
		
		//test 3
		Container.clear();
		assert(Container.isEmpty());
		
		ClassA inst=new ClassA();
		ClassC temp3=null;
		Container.putInstanceItem(inst, "3", temp3=new ClassC());
		Container.putInstanceItem(new ClassA(), "3", temp3);
		
		hasException = false;
		try
		{
			Container.putGlobalItem("3", new ClassA());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.putClassItem(inst.getClass() ,"3", new ClassA());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		Container.putClassItem(ClassC.class ,"3", new ClassA());
		
		hasException = false;
		try
		{
			Container.putInstanceItem(new ClassC() ,"3", new ClassB());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.putInstanceItem(inst ,"3", new ClassA());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		Container.putGlobalItem("4", new ClassAB());
		
		hasException = false;
		try
		{
			Container.putInstanceItem(new ClassAA(),"4", new ClassB());
		} catch (Exception ex)
		{

			assert (ex instanceof IllegalArgumentException);
			hasException = true;
		}
		assert (hasException);
		
		assert(Container.lookup(inst, "3")==temp3);
		
	}

	public void testLockModification()
	{
		testLookup();
		
		assert(!Container.isModificationLocked());
		assert(!Container.unlockModification(new ClassC()));
		
		ClassA temp=null;
		ClassB temp2=null;
		assert(Container.lockModification(temp=new ClassA()));
		assert(!Container.lockModification(temp2=new ClassB()));
		
		assert(Container.isModificationLocked());
		
		assert(!Container.unlockModification(new ClassB()));
		assert(!Container.unlockModification(temp2));
		assert(Container.isModificationLocked());
		
		boolean hasException = false;
		try
		{
			testLookup();
		}catch(Exception ex)
		{
			assert(ex instanceof ContainerModificationLockedException);
			hasException=true;
		}
		assert(hasException);
		
		TestingOverlappedKeysRandom test=new TestingOverlappedKeysRandom();
		for(int i=0;i<100;i++)
		{
			test.randomLookup();
		}
		
		hasException = false;
		try
		{
			Container.putGlobalItem("", "");
		} catch (Exception ex)
		{
			assert (ex instanceof ContainerModificationLockedException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.putClassItem(ClassA.class, "", "");
		} catch (Exception ex)
		{
			assert (ex instanceof ContainerModificationLockedException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.putInstanceItem(new ClassB(), "", "");
		} catch (Exception ex)
		{
			assert (ex instanceof ContainerModificationLockedException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.removeGlobalItem("");
		} catch (Exception ex)
		{
			assert (ex instanceof ContainerModificationLockedException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.removeClassItem(ClassA.class, "");
		} catch (Exception ex)
		{
			assert (ex instanceof ContainerModificationLockedException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.removeInstanceItem(new ClassB(), "");
		} catch (Exception ex)
		{
			assert (ex instanceof ContainerModificationLockedException);
			hasException = true;
		}
		assert (hasException);
		
		hasException = false;
		try
		{
			Container.clear();
		} catch (Exception ex)
		{
			assert (ex instanceof ContainerModificationLockedException);
			hasException = true;
		}
		assert (hasException);
		
		assert(Container.unlockModification(temp));
		assert(!Container.isModificationLocked());
		assert(!Container.unlockModification(temp));
		assert(!Container.unlockModification(temp2));
		assert(!Container.isModificationLocked());
		
		testLookup();
		
		try
		{
			Container.putGlobalItem("", "");
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		try
		{
			Container.putClassItem(ClassA.class, "1234", "");
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		try
		{
			Container.putInstanceItem(new ClassB(), "1234", "");
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		try
		{
			assert(Container.removeGlobalItem("").equals(""));
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		try
		{
			assert(Container.removeClassItem(ClassA.class, "1234").equals(""));
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		try
		{
			assert(Container.removeInstanceItem(new ClassB(), "1234")==null);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		try
		{
			Container.clear();
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		assert(Container.isEmpty());
		
		assert(!Container.isModificationLocked());
		String a1="123";
		String a2=new String("123");
		assert(a1!=a2&&a1.equals(a2));
		assert(Container.lockModification(a1));
		assert(Container.isModificationLocked());
		assert(!Container.unlockModification(a2));
		assert(Container.isModificationLocked());
		assert(Container.unlockModification(a1));
		assert(!Container.isModificationLocked());
		
	}
	
	public void testOverlappedKeyRandom()
	{
		Container.clear();
		assert(Container.isEmpty());
		
		TestingOverlappedKeysRandom t=new TestingOverlappedKeysRandom(); 
		t.testOverlappedKeyRandom();
		
		assertEquals(60, 1.0, 0 );
	}

}
