package com.accela.TestCases.IDSupport;

import com.accela.IDSupport.DigitIdPool;
import java.util.*;

import junit.framework.TestCase;

public class TestingDigitIdPool extends TestCase
{
	private DigitIdPool pool;

	private static final int ITER_SIZE = 1000;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();

		pool = new DigitIdPool();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();

		pool = null;
	}

	public void testMessAlloc()
	{
		Set<Long> set = new HashSet<Long>();

		for (int i = 0; i < ITER_SIZE; i++)
		{
			for (int j = 0; j < ITER_SIZE; j++)
			{
				//long id = -1;
				boolean result = set.add(/*id =*/ pool.allocId());
				assert (result);

				//System.out.println("i = " + i + ", j = " + j + ", id = " + id);
			}
		}
	}

	public void testRandom()
	{
		Set<Long> set = new HashSet<Long>();

		int[] addArray = new int[(int) (Math.random() * ITER_SIZE)];
		for (int i = 0; i < addArray.length; i++)
		{
			addArray[i] = (int) (Math.random() * ITER_SIZE);
		}

		int[] removeArray = addArray.clone();
		Arrays.sort(removeArray);

		assert (addArray.length == removeArray.length);

		for (int i = 0; i < addArray.length; i++)
		{
			for (int j = 0; j < addArray[i]; j++)
			{
				//long id = -1;
				boolean result = set.add(/*id =*/ pool.allocId());
				assert (result);

				//System.out.println("i = " + i + ", id = " + id);
			}

			Long[] ids = set.toArray(new Long[0]);
			int removeIdx = (int) (ids.length * Math.random());
			assert (removeArray[i] <= set.size());

			for (int j = 0; j < removeArray[i]; j++)
			{
				pool.freeId(ids[removeIdx]);
				boolean result = set.remove(ids[removeIdx]);
				assert (result);

				removeIdx = (removeIdx + 1) % ids.length;
			}
		}
	}

	public void testMemory()
	{
		final long TIMES = 200000000L;

		for (long i = 0; i < TIMES; i++)
		{
			/*long id =*/ pool.allocId();
			//System.out.println(id);
		}
	}

	/*public static void main(String[] args) throws Exception
	{
		TestingDigitIdPool t = new TestingDigitIdPool();
		t.setUp();
		t.testMemory();
		t.tearDown();
	}*/

}
