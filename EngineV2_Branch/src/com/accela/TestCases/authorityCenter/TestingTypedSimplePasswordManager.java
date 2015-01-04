package com.accela.TestCases.authorityCenter;

import com.accela.AuthorityCenter.authorityBase.TypedSimplePasswordManager;

import junit.framework.TestCase;

public class TestingTypedSimplePasswordManager extends TestCase
{

	private TypedSimplePasswordManager<Integer, String> passwordManager = new TypedSimplePasswordManager<Integer, String>();

	private Integer names[] = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, };

	private String[] passwords = new String[] { "1", "2", "3", "4", "5", "6",
			"7", "8", "9", };

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		passwordManager = new TypedSimplePasswordManager<Integer, String>();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		passwordManager = null;
	}

	public void testPasswordManager()
	{
		try
		{
			assert (passwordManager.isEmpty());
			assert (passwordManager.put(names[0], passwords[0]) == null);
			assert (!passwordManager.isEmpty());
			assert (passwordManager.put(names[0], passwords[1])
					.equals(passwords[0]));
			passwordManager.clear();
			assert (passwordManager.isEmpty());

			assert (passwordManager.put(names[1], passwords[1]) == null);
			assert (passwordManager.remove(names[0]) == null);
			assert (passwordManager.put(names[2], passwords[2]) == null);
			assert (passwordManager.remove(names[1]).equals(passwords[1]));
			assert (passwordManager.remove(names[2]).equals(passwords[2]));
			assert (passwordManager.isEmpty());

			for (int i = 0; i < names.length; i++)
			{
				assert(passwordManager.getSize()==i);
				assert (passwordManager.put(names[i], passwords[i]) == null);
				assert(passwordManager.getSize()==i+1);
			}
			for (int i = 0; i < names.length; i++)
			{
				assert (passwordManager.get(names[i]).equals(passwords[i]));
				assert (passwordManager.containsName(names[i]));
			}
			for (int i = 0; i < 10; i++)
			{
				assert (passwordManager.get(-i) == null);
				assert (!passwordManager.containsName(-i));
			}
			passwordManager.clear();
			for (int i = 0; i < names.length; i++)
			{
				assert (passwordManager.get(names[i]) == null);
				assert (!passwordManager.containsName(names[i]));
			}
			for (int i = 0; i < 10; i++)
			{
				assert (passwordManager.get(-i) == null);
				assert (!passwordManager.containsName(-i));
			}
			assert (passwordManager.isEmpty());

			for (int i = 0; i < names.length; i++)
			{
				assert (passwordManager.put(names[i], passwords[i]) == null);
			}
			for (int i = 0; i < names.length; i++)
			{
				for (int j = 0; j < names.length; j++)
				{
					if (i == j)
					{
						assert (passwordManager.verify(names[i], passwords[j]));
					} else
					{
						assert (!passwordManager.verify(names[i], passwords[j]));
					}
				}
			}
			for (int i = 0; i < names.length; i++)
			{
				for (int j = 0; j < 10; j++)
				{
					assert (!passwordManager.verify(names[i], "" + (-j)));
				}
			}
			for (int i = 0; i < 10; i++)
			{
				for (int j = 0; j < names.length; j++)
				{
					assert (!passwordManager.verify(-i, passwords[j]));
				}
			}
			for (int i = 0; i < 10; i++)
			{
				for (int j = 0; j < 10; j++)
				{
					assert (!passwordManager.verify(-i, "" + (-j)));
				}
			}
			for (int i = 0; i < names.length; i++)
			{
				for (int j = 0; j < 10; j++)
				{
					assert (!passwordManager.verify(names[i], null));
				}
			}
			for (int i = 0; i < 10; i++)
			{
				for (int j = 0; j < names.length; j++)
				{
					assert (!passwordManager.verify(null, passwords[j]));
				}
			}
			for (int i = 0; i < 10; i++)
			{
				for (int j = 0; j < 10; j++)
				{
					assert (!passwordManager.verify(null, null));
				}
			}

			boolean hasException = false;
			try
			{
				passwordManager.put(null, "123");
			} catch (Exception ex)
			{
				hasException = true;
				assert ((ex instanceof NullPointerException) || (ex instanceof IllegalArgumentException));
			}
			assert (hasException);
			hasException = false;

			try
			{
				passwordManager.put(1, null);
			} catch (Exception ex)
			{
				hasException = true;
				assert ((ex instanceof NullPointerException) || (ex instanceof IllegalArgumentException));
			}
			assert (hasException);
			hasException = false;

			try
			{
				passwordManager.put(null, null);
			} catch (Exception ex)
			{
				hasException = true;
				assert ((ex instanceof NullPointerException) || (ex instanceof IllegalArgumentException));
			}
			assert (hasException);
			hasException = false;

			try
			{
				passwordManager.get(null);
			} catch (Exception ex)
			{
				hasException = true;
				assert ((ex instanceof NullPointerException) || (ex instanceof IllegalArgumentException));
			}
			assert (hasException);
			hasException = false;

			try
			{
				passwordManager.containsName(null);
			} catch (Exception ex)
			{
				hasException = true;
				assert ((ex instanceof NullPointerException) || (ex instanceof IllegalArgumentException));
			}
			assert (hasException);
			hasException = false;

			try
			{
				passwordManager.remove(null);
			} catch (Exception ex)
			{
				hasException = true;
				assert ((ex instanceof NullPointerException) || (ex instanceof IllegalArgumentException));
			}
			assert (hasException);
			hasException = false;
			
			//////////////////
			passwordManager.clear();
			for (int i = 0; i < names.length; i++)
			{
				assert(passwordManager.getSize()==i);
				assert (passwordManager.put(names[i], passwords[i]) == null);
				assert(passwordManager.getSize()==i+1);
			}
			for (int i = 0; i < names.length; i++)
			{
				assert(passwordManager.getSize()==names.length-i);
				assert (passwordManager.remove(names[i]).equals(passwords[i]));
				assert(passwordManager.getSize()==names.length-1-i);
			}

		} catch (Exception ex)
		{
			assert (false);
		}
	}

}
