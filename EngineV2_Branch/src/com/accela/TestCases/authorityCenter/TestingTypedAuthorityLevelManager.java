package com.accela.TestCases.authorityCenter;

import java.util.LinkedList;
import java.util.List;

import com.accela.AuthorityCenter.authorityBase.TypedAuthorityLevelManager;
import com.accela.AuthorityCenter.authorityBase.TypedSimplePasswordManager;

import junit.framework.TestCase;

public class TestingTypedAuthorityLevelManager extends TestCase
{
	private TypedAuthorityLevelManager<Integer, String> lm = new TypedAuthorityLevelManager<Integer, String>(
			new TypedSimplePasswordManager<Integer, String>());

	private Integer levels[] = new Integer[] { 1, 2, 3, 4, 5, 6, 7, 8, 9, };

	private String[] passwords = new String[] { "1", "2", "3", "4", "5", "6",
			"7", "8", "9", };

	private Integer levels2[] = new Integer[] { -1, -2, -3, -4, -5, -6, -7, -8,
			-9, };

	private String[] passwords2 = new String[] { "-1", "-2", "-3", "-4", "-5",
			"-6", "-7", "-8", "-9", };

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		lm = new TypedAuthorityLevelManager<Integer, String>(
				new TypedSimplePasswordManager<Integer, String>());
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		lm = null;
	}

	public void testTypedAuthorityLevelManager()
	{
		try
		{

			assert (lm.isEmpty());
			assert (lm.addHighestLevel(levels[0], passwords[0]));
			assert (!lm.isEmpty());
			lm.clear();
			assert (lm.isEmpty());

			assert (lm.addHighestLevel(levels[1], passwords[1]));
			assert (!lm.addHighestLevel(levels[1], passwords[2]));
			assert (lm.removeHighestLevel().equals(levels[1]));

			for (int i = 0; i < levels.length; i++)
			{
			    assert(lm.getSize()==i);
				assert (lm.addHighestLevel(levels[i], passwords[i]));
				assert(lm.getSize()==i+1);
			}
			for (int i = 0; i < levels.length; i++)
			{
			    assert(lm.getSize()==levels.length-i);
				assert (lm.removeLevel(levels[i]));
				assert(lm.getSize()==levels.length-1-i);
			}
			assert(lm.isEmpty());
			for (int i = 0; i < levels.length; i++)
			{
			    assert(lm.getSize()==i);
				assert (lm.addHighestLevel(levels[i], passwords[i]));
				assert(lm.getSize()==i+1);
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (!lm.removeLevel(levels2[i]));
				assert (lm.removeLevel(levels[i]));
				assert (!lm.removeLevel(levels[i]));
			}
			assert (lm.isEmpty());

			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.addHighestLevel(levels[i], passwords[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.removeHighestLevel().equals(levels[levels.length - 1
						- i]));
			}
			assert (lm.isEmpty());

			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.addHighestLevel(levels[i], passwords[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.getPassword(levels[i]).equals(passwords[i]));
				assert (lm.getPassword(levels2[i]) == null);
			}

			lm.clear();
			assert (lm.isEmpty());

			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.addHighestLevel(levels[i], passwords[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (!lm.addHighestLevel(levels[i], passwords2[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.containsLevel(levels[i]));
				assert (!lm.containsLevel(levels2[i]));
			}
			for (int i = 0; i < levels.length - 1; i++)
			{
				for (int j = 0; j < levels.length; j++)
				{
					if (i < j)
					{
						assert (lm.compareLevel(levels[i], levels[j]) < 0);

					} else if (i > j)
					{
						assert (lm.compareLevel(levels[i], levels[j]) > 0);
					} else
					{
						assert (lm.compareLevel(levels[i], levels[j]) == 0);
					}
				}

			}
			for (int i = 0; i < levels.length - 1; i++)
			{
				for (int j = 0; j < levels2.length; j++)
				{
					boolean hasException = false;
					try
					{
						lm.compareLevel(levels[i], levels2[j]);
					} catch (Exception ex)
					{
						hasException = true;
						assert (ex instanceof IllegalArgumentException);
					}
					assert (hasException);

				}
			}
			for (int i = 0; i < levels2.length - 1; i++)
			{
				for (int j = 0; j < levels.length; j++)
				{
					boolean hasException = false;
					try
					{
						lm.compareLevel(levels2[i], levels[j]);
					} catch (Exception ex)
					{
						hasException = true;
						assert (ex instanceof IllegalArgumentException);
					}
					assert (hasException);

				}
			}
			for (int i = 0; i < levels2.length - 1; i++)
			{
				for (int j = 0; j < levels2.length; j++)
				{
					boolean hasException = false;
					try
					{
						lm.compareLevel(levels2[i], levels2[levels2.length - 1
								- j]);
					} catch (Exception ex)
					{
						hasException = true;
						assert (ex instanceof IllegalArgumentException);
					}
					assert (hasException);

				}
			}

			lm.clear();
			assert (lm.isEmpty());
			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.addHighestLevel(levels[levels.length - 1 - i],
						passwords[i]));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (lm.addHighestLevel(levels2[i], passwords[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.changePassword(levels[i], "go to hell")
						.equals(passwords[passwords.length - 1 - i]));
			}
			for (int i = 0; i < 1; i++)
			{
				boolean hasException = false;
				try
				{
					lm.changePassword(100 + i, "go to hell, too");
				} catch (Exception ex)
				{
					hasException = true;
					assert (ex instanceof IllegalArgumentException);
				}
				assert (hasException);

			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.getPassword(levels[i]).equals("go to hell"));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (lm.getPassword(levels2[i]).equals(passwords[i]));
			}
			List<Integer> ls = lm.getAllLevels();
			assert (ls != null);
			assert (ls.size() == levels.length + levels2.length);
			assert(lm.getSize()==ls.size());
			for (int i = 0; i < levels.length; i++)
			{
				assert (ls.contains(levels[i]));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (ls.contains(levels2[i]));
			}

			LinkedList<Integer> list = new LinkedList<Integer>();
			for (int i = 0; i < levels.length; i++)
			{
				list.add(levels[levels.length - 1 - i]);
			}
			for (int i = 0; i < levels2.length; i++)
			{
				list.add(levels2[i]);
			}
			ls = lm.getIncSortedAllLevels();
			assert (ls.size() == levels.length + levels2.length);
			assert (ls.size() == list.size());
			for (int i = 0; i < ls.size(); i++)
			{
				assert (list.get(i).equals(ls.get(i)));
			}

			lm.clear();
			assert (lm.isEmpty());
			ls = lm.getAllLevels();
			assert (ls != null);
			assert (ls.size() == 0);
			ls = lm.getIncSortedAllLevels();
			assert (ls != null);
			assert (ls.size() == 0);

			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.addHighestLevel(levels[i], passwords[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.verify(levels[i], passwords[i]));
			}
			for (int i = 0; i < levels.length / 2; i++)
			{
				assert (!lm.verify(levels[i], passwords[passwords.length - 1
						- i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (!lm.verify(levels[i], passwords2[i]));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (!lm.verify(levels2[i], passwords[i]));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (!lm.verify(levels2[i], passwords2[i]));
			}

			for (int i = 0; i < levels.length; i++)
			{
				assert (!lm.isHighestLevel(levels2[i]));
				assert (lm.isHighestLevel(levels[levels.length - 1 - i]));
				assert (lm.getHighestLevel().equals(levels[levels.length - 1
						- i]));
				for (int j = 0; j < levels.length - 1 - i; j++)
				{
					assert (!lm.isHighestLevel(levels[j]));
				}

				assert (lm.removeHighestLevel() != null);
			}

			assert (lm.removeHighestLevel() == null);
			assert (lm.isEmpty());
			for (int i = 0; i < levels.length; i++)
			{
				assert (!lm.isHighestLevel(levels[i]));
				assert (lm.getHighestLevel() == null);
			}

			assert (lm.isEmpty());
			for (int i = 0; i < levels.length; i++)
			{
				assert (!lm.isMiddleLevel(levels[i]));
				assert (!lm.isLowestLevel(levels[i]));
				assert (lm.getLowestLevel() == null);
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.addHighestLevel(levels[i], passwords[i]));
			}
			for (int i = levels.length - 1; i >= 0; i--)
			{
				for (int j = 0; j <= i; j++)
				{
					if (j == i&&i!=0)
					{
						if(i!=0)
						{
							assert (lm.isHighestLevel(levels[j]));
							assert (!lm.isMiddleLevel(levels[j]));
							assert (!lm.isLowestLevel(levels[j]));
						}
						else
						{
							assert (lm.isHighestLevel(levels[j]));
							assert (!lm.isMiddleLevel(levels[j]));
							assert (lm.isLowestLevel(levels[j]));
						}
					}
					if (j < i && j > 0)
					{
						assert (!lm.isHighestLevel(levels[j]));
						assert (lm.isMiddleLevel(levels[j]));
						assert (!lm.isLowestLevel(levels[j]));
					}
					if (j == 0)
					{
						if(i!=0)
						{
							assert (!lm.isHighestLevel(levels[j]));
							assert (!lm.isMiddleLevel(levels[j]));
							assert (lm.isLowestLevel(levels[j]));
						}
						else
						{
							assert (lm.isHighestLevel(levels[j]));
							assert (!lm.isMiddleLevel(levels[j]));
							assert (lm.isLowestLevel(levels[j]));
						}
					}

				}

				assert (lm.getHighestLevel().equals(levels[i]));
				assert (lm.getLowestLevel().equals(levels[0]));

				for (int j = 0; j < levels2.length; j++)
				{
					assert (!lm.isHighestLevel(levels2[j]));
					assert (!lm.isMiddleLevel(levels2[j]));
					assert (!lm.isLowestLevel(levels2[j]));
				}

				lm.removeHighestLevel();
			}

			assert (lm.isEmpty());
			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.addHighestLevel(levels[i], passwords[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				for (int j = i; j <= levels.length; j++)
				{
					if (j == levels.length - 1)
					{
						if(i!=levels.length-1)
						{
							assert (lm.isHighestLevel(levels[j]));
							assert (!lm.isMiddleLevel(levels[j]));
							assert (!lm.isLowestLevel(levels[j]));
						}
						else
						{
							assert (lm.isHighestLevel(levels[j]));
							assert (!lm.isMiddleLevel(levels[j]));
							assert (lm.isLowestLevel(levels[j]));
						}
					}
					if (j > i && j < levels.length - 1)
					{
						assert (!lm.isHighestLevel(levels[j]));
						assert (lm.isMiddleLevel(levels[j]));
						assert (!lm.isLowestLevel(levels[j]));
					}
					if (j == i)
					{
						if(i!=levels.length-1)
						{
							assert (!lm.isHighestLevel(levels[j]));
							assert (!lm.isMiddleLevel(levels[j]));
							assert (lm.isLowestLevel(levels[j]));
						}
						else
						{
							assert (lm.isHighestLevel(levels[j]));
							assert (!lm.isMiddleLevel(levels[j]));
							assert (lm.isLowestLevel(levels[j]));
						}
					}

				}

				assert (lm.getHighestLevel().equals(levels[levels.length - 1]));
				assert (lm.getLowestLevel().equals(levels[i]));

				for (int j = 0; j < levels2.length; j++)
				{
					assert (!lm.isHighestLevel(levels2[j]));
					assert (!lm.isMiddleLevel(levels2[j]));
					assert (!lm.isLowestLevel(levels2[j]));
				}

				assert (lm.removeLevel(levels[i]));
			}

			assert (lm.isEmpty());
			for (int i = 0; i < levels.length; i++)
			{
				assert (lm.addHighestLevel(levels[i], passwords[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				if (i == levels.length - 1)
				{
					assert (lm.findAdjacentHigherLevel(levels[i]) == null);
				} else
				{
					assert (lm.findAdjacentHigherLevel(levels[i])
							.equals(levels[i + 1]));
				}

				if (i == 0)
				{
					assert (lm.findAdjacentLowerLevel(levels[i]) == null);
				} else
				{
					assert (lm.findAdjacentLowerLevel(levels[i])
							.equals(levels[i - 1]));
				}

				for (int j = 0; j < levels2.length; j++)
				{
					boolean hasException = false;
					try
					{
						lm.findAdjacentHigherLevel(levels2[i]);
					} catch (Exception ex)
					{
						hasException = true;
						assert (ex instanceof IllegalArgumentException);
					}
					assert (hasException);

					hasException = false;
					try
					{
						lm.findAdjacentLowerLevel(levels2[i]);
					} catch (Exception ex)
					{
						hasException = true;
						assert (ex instanceof IllegalArgumentException);
					}
					assert (hasException);

				}
			}

			lm.clear();
			assert (lm.isEmpty());
			assert (lm.getHighestLevel() == null);
			assert (lm.getLowestLevel() == null);

			lm.addHighestLevel(levels[0], passwords[0]);
			assert (lm.getHighestLevel().equals(levels[0]));
			assert (lm.getLowestLevel().equals(levels[0]));
			assert (lm.getHighestLevel().equals(lm.getLowestLevel()));

		} catch (Exception ex)
		{
			assert (false);
		}

	}

}
