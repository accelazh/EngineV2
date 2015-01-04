package com.accela.TestCases.authorityCenter;

import java.util.LinkedList;
import java.util.List;

import com.accela.AuthorityCenter.authorityBase.AuthorityBase;
import com.accela.AuthorityCenter.authorityBase.SimplePasswordManager;

import junit.framework.TestCase;

public class TestingAuthorityBase extends TestCase
{

	private AuthorityBase ab = new AuthorityBase(new SimplePasswordManager());

	private String[] levels = new String[] {
			"1", 
			"2",
			"3",

			"4", 
			"5",
			"6",

			"7", 
			"8",
			"9",

	};

	private String[] passwords = new String[] {
			"A", 
			"B",
			"C",

			"D", 
			"E",
			"F",

			"G", 
			"H",
			"I", 
	};

	private String levels2[] = new String[] {
			"1adsf", 
			"2adsf",
			"3ghj",

			"4sda", 
			"sdaf5",
			"6yui",

			"adf7", 
			"8uynm",
			"ati3jds",
	};

	private String[] passwords2 = new String[] {
			"go", 
			"now",
			"or",

			"I", 
			"will",
			"kick",

			"you", 
			"out",
			"!!",

	};

	private CommanderIDForTest[] commanders = new CommanderIDForTest[] {
			new CommanderIDForTest(1), 
			new CommanderIDForTest(2),
			new CommanderIDForTest(3),

			new CommanderIDForTest(4), 
			new CommanderIDForTest(5),
			new CommanderIDForTest(6),

			new CommanderIDForTest(7), 
			new CommanderIDForTest(8),
			new CommanderIDForTest(9), 
	};

	private CommanderIDForTest[] commanders2 = new CommanderIDForTest[] {
			new CommanderIDForTest(11), 
			new CommanderIDForTest(22),
			new CommanderIDForTest(33),

			new CommanderIDForTest(44), 
			new CommanderIDForTest(55),
			new CommanderIDForTest(66),

			new CommanderIDForTest(77), 
			new CommanderIDForTest(88),
			new CommanderIDForTest(99), 
	};

	private String[] commandHeads = new String[] {
			"today", 
			"is",
			"not",

			"such", 
			"a:",
			"good",

			"day", 
			"yes",
			"?", 
	};

	private String[] commandHeads2 = new String[] {
			"today3", 
			"is3",
			"not4",

			"such5", 
			"a6",
			"good7",

			"day8", 
			"yes9",
			"?9", 
	};

	protected void setUp() throws Exception
	{
		super.setUp();
		ab = new AuthorityBase(new SimplePasswordManager());
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
		ab = null;
	}

	public void testLevelFunction()
	{
		try
		{

			assert (ab.getNumOfLevels() == 0);
			assert (ab.addHighestLevel(levels[0], passwords[0]));
			assert (ab.getNumOfLevels() > 0);
			assert(ab.removeHighestLevel().equals(levels[0]));
			assert (ab.getNumOfLevels() == 0);

			assert (ab.addHighestLevel(levels[1], passwords[1]));
			assert (!ab.addHighestLevel(levels[1], passwords[2]));
			assert (ab.removeHighestLevel().equals(levels[1]));

			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.getNumOfLevels() == i);
				assert (ab.addHighestLevel(levels[i], passwords[i]));
				assert (ab.getNumOfLevels() == i + 1);
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.getNumOfLevels() == levels.length - i);
				assert (ab.removeHighestLevel().equals(levels[levels.length - 1
						- i]));
				assert (ab.getNumOfLevels() == levels.length - 1 - i);
			}
			assert (ab.getNumOfLevels() == 0);

			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.addHighestLevel(levels[i], passwords[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.verifyLevelAndPassword(levels[i], passwords[i]));
				assert (!ab.verifyLevelAndPassword(levels2[i], passwords[i]));
				assert (!ab.verifyLevelAndPassword(levels2[i], passwords2[i]));
				assert (!ab.verifyLevelAndPassword(levels[i], passwords2[i]));
			}
			for(int i=levels.length-1;i>=0;i--)
			{
				assert(ab.removeHighestLevel().equals(levels[i]));
				
				assert(ab.getAllLevels().size()==i);
				for(int j=0;j<i;j++)
				{
					assert(ab.getAllLevels().contains(levels[j]));
					assert(ab.containsLevel(levels[j]));
				}
				for(int j=i;j<levels.length;j++)
				{
					assert(!ab.getAllLevels().contains(levels[j]));
					assert(!ab.containsLevel(levels[j]));
				}
			}
			assert(ab.getAllLevels().size()==0);
			assert(ab.getNumOfLevels()==0);
			
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.addHighestLevel(levels[i], passwords[i]));
			}

			while (ab.getNumOfLevels() != 0)
			{
				assert (ab.removeHighestLevel() != null);
			}
			assert (ab.getNumOfLevels() == 0);

			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.addHighestLevel(levels[i], passwords[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (!ab.addHighestLevel(levels[i], passwords2[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.containsLevel(levels[i]));
				assert (!ab.containsLevel(levels2[i]));
			}
			for (int i = 0; i < levels.length - 1; i++)
			{
				for (int j = 0; j < levels.length; j++)
				{
					if (i < j)
					{
						assert (ab.compareAuthority(levels[i], levels[j]) < 0);

					} else if (i > j)
					{
						assert (ab.compareAuthority(levels[i], levels[j]) > 0);
					} else
					{
						assert (ab.compareAuthority(levels[i], levels[j]) == 0);
					}
				}

			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.compareAuthority(null, levels[i]) < 0);
				assert (ab.compareAuthority(levels[i], null) > 0);
				assert (ab.compareAuthority(null, null) == 0);
			}

			boolean hasException2 = false;
			try
			{
				ab.compareAuthority(levels2[0], null);
			} catch (Exception ex)
			{
				hasException2 = true;
				assert (ex instanceof IllegalArgumentException);
			}
			assert (hasException2);

			hasException2 = false;
			try
			{
				ab.compareAuthority(null, levels2[0]);
			} catch (Exception ex)
			{
				hasException2 = true;
				assert (ex instanceof IllegalArgumentException);
			}
			assert (hasException2);

			hasException2 = false;
			try
			{
				assert(ab.compareAuthority(null, null)==0);
			} catch (Exception ex)
			{
				hasException2 = true;
				assert (ex instanceof IllegalArgumentException);
			}
			assert (!hasException2);

			for (int i = 0; i < levels.length - 1; i++)
			{
				for (int j = 0; j < levels2.length; j++)
				{
					boolean hasException = false;
					try
					{
						ab.compareAuthority(levels[i], levels2[j]);
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
						ab.compareAuthority(levels2[i], levels[j]);
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
						ab.compareAuthority(levels2[i], levels2[levels2.length
								- 1 - j]);
					} catch (Exception ex)
					{
						hasException = true;
						assert (ex instanceof IllegalArgumentException);
					}
					assert (hasException);

				}
			}

			while (ab.getNumOfLevels() != 0)
			{
				assert (ab.removeHighestLevel() != null);
			}

			assert (ab.getNumOfLevels() == 0);
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.addHighestLevel(levels[levels.length - 1 - i],
						passwords[i]));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (ab.addHighestLevel(levels2[i], passwords[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.changeLevelPassword(levels[i],
						new String("go to hell"))
						.equals(passwords[passwords.length - 1 - i]));
			}
			for (int i = 0; i < 1; i++)
			{
				boolean hasException = false;
				try
				{
					ab.changeLevelPassword(new String(100 + i + ""),
							new String("go to hell, too"));
				} catch (Exception ex)
				{
					hasException = true;
					assert (ex instanceof IllegalArgumentException);
				}
				assert (hasException);

			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.verifyLevelAndPassword(levels[i],
						new String("go to hell")));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (ab.verifyLevelAndPassword(levels2[i], (passwords[i])));
			}
			List<String> ls = ab.getAllLevels();
			assert (ls != null);
			assert (ls.size() == levels.length + levels2.length);
			assert (ab.getNumOfLevels() == ls.size());
			for (int i = 0; i < levels.length; i++)
			{
				assert (ls.contains(levels[i]));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (ls.contains(levels2[i]));
			}

			LinkedList<String> list = new LinkedList<String>();
			for (int i = 0; i < levels.length; i++)
			{
				list.add(levels[levels.length - 1 - i]);
			}
			for (int i = 0; i < levels2.length; i++)
			{
				list.add(levels2[i]);
			}
			ls = ab.getAllLevels();
			assert (ls.size() == levels.length + levels2.length);
			assert (ls.size() == list.size());
			for (int i = 0; i < ls.size(); i++)
			{
				assert (list.get(i).equals(ls.get(i)));
			}

			while (ab.getNumOfLevels() != 0)
			{
				assert (ab.removeHighestLevel() != null);
			}
			assert (ab.getNumOfLevels() == 0);
			ls = ab.getAllLevels();
			assert (ls != null);
			assert (ls.size() == 0);

			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.addHighestLevel(levels[i], passwords[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.verifyLevelAndPassword(levels[i], passwords[i]));
			}
			for (int i = 0; i < levels.length / 2; i++)
			{
				assert (!ab.verifyLevelAndPassword(levels[i],
						passwords[passwords.length - 1 - i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (!ab.verifyLevelAndPassword(levels[i], passwords2[i]));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (!ab.verifyLevelAndPassword(levels2[i], passwords[i]));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (!ab.verifyLevelAndPassword(levels2[i], passwords2[i]));
			}

		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	public void testCommandHeadFunction()
	{
		try
		{
			for (int i = 0; i < commandHeads.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads[i]) == null);
			}

			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.addHighestLevel(levels[i], passwords[i]));
			}

			for (int i = 0; i < commandHeads.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads[i]) == null);
			}
			for (int i = 0; i < commandHeads2.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads2[i]) == null);
			}

			for (int i = 0; i < commandHeads.length; i++)
			{
				assert (ab.setCommandHeadAuthority(commandHeads[i], levels[i]) == null);
			}
			for (int i = 0; i < commandHeads.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads[i])
						.equals(levels[i]));
			}
			for (int i = 0; i < commandHeads2.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads2[i]) == null);
			}

			for (int i = 0; i < commandHeads2.length; i++)
			{
				assert (ab.setCommandHeadAuthority(commandHeads2[i],
						levels[levels.length - 1 - i]) == null);
			}
			for (int i = 0; i < commandHeads.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads[i])
						.equals(levels[i]));
			}
			for (int i = 0; i < commandHeads2.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads2[i])
						.equals(levels[levels.length - i - 1]));
			}

			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.setCommandHeadAuthority(commandHeads[i], null) == levels[i]);
			}
			for (int i = 0; i < commandHeads.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads[i])==null);
				assert (ab.findRequiredAuthority(commandHeads2[i])
						.equals(levels[levels.length - i - 1]));
			}
			for (int i = 0; i < commandHeads2.length; i++)
			{
				assert (ab.setCommandHeadAuthority(commandHeads2[i], null)
						.equals(levels[levels.length - 1 - i]));
			}
			for (int i = 0; i < commandHeads.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads[i]) == null);
			}
			for (int i = 0; i < commandHeads2.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads2[i]) == null);
			}

			for (int i = 0; i < levels.length; i++)
			{
				boolean hasException = false;
				try
				{
					ab.setCommandHeadAuthority(commandHeads[i], levels2[i]);
				} catch (Exception ex)
				{
					assert (ex instanceof IllegalArgumentException);
					hasException = true;
				}
				assert (hasException);
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads[i]) == null);
			}

			for (int i = 0; i < levels.length; i++)
			{
				ab.setCommandHeadAuthority(commandHeads[i], levels[i]);
			}
			for (int i = 0; i < levels.length; i++)
			{
				boolean hasException = false;
				try
				{
					ab.setCommandHeadAuthority(commandHeads[i], levels2[i]);
				} catch (Exception ex)
				{
					assert (ex instanceof IllegalArgumentException);
					hasException = true;
				}
				assert (hasException);
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads[i])
						.equals(levels[i]));
			}

			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.setCommandHeadAuthority(commandHeads[i],
						levels[levels.length - 1 - i]).equals(levels[i]));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (ab.addHighestLevel(levels2[i], passwords2[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.setCommandHeadAuthority(commandHeads[i], levels2[i])
						.equals(levels[levels.length - 1 - i]));
			}
			for (int i = 0; i < commandHeads.length; i++)
			{
				assert (ab.findRequiredAuthority(commandHeads[i])
						.equals(levels2[i]));
			}

		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}

	}

	public void testCommanderFunction()
	{
		try
		{
			for (int i = 0; i < commanders.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders[i]) == null);
			}

			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.addHighestLevel(levels[i], passwords[i]));
			}

			for (int i = 0; i < commanders.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders[i]) == null);
			}
			for (int i = 0; i < commanders2.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders2[i]) == null);
			}

			for (int i = 0; i < commanders.length; i++)
			{
				assert (ab.setCommanderAuthority(commanders[i], levels[i]) == null);
			}
			for (int i = 0; i < commanders.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders[i])
						.equals(levels[i]));
			}
			for (int i = 0; i < commanders2.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders2[i]) == null);
			}

			for (int i = 0; i < commanders2.length; i++)
			{
				assert (ab.setCommanderAuthority(commanders2[i],
						levels[levels.length - 1 - i]) == null);
			}
			for (int i = 0; i < commanders.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders[i])
						.equals(levels[i]));
			}
			for (int i = 0; i < commanders2.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders2[i])
						.equals(levels[levels.length - i - 1]));
			}

			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.setCommanderAuthority(commanders[i], null) == levels[i]);
			}
			for (int i = 0; i < commanders.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders[i])==null);
				assert(ab.findPermittedAuthority(commanders2[i])
						.equals(levels[levels.length-1-i]));
			}
			for (int i = 0; i < commanders2.length; i++)
			{
				assert (ab.setCommanderAuthority(commanders2[i], null)
						.equals(levels[levels.length - 1 - i]));
			}
			for (int i = 0; i < commanders.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders[i]) == null);
			}
			for (int i = 0; i < commanders2.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders2[i]) == null);
			}

			for (int i = 0; i < levels.length; i++)
			{
				boolean hasException = false;
				try
				{
					ab.setCommanderAuthority(commanders[i], levels2[i]);
				} catch (Exception ex)
				{
					assert (ex instanceof IllegalArgumentException);
					hasException = true;
				}
				assert (hasException);
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders[i]) == null);
			}

			for (int i = 0; i < levels.length; i++)
			{
				ab.setCommanderAuthority(commanders[i], levels[i]);
			}
			for (int i = 0; i < levels.length; i++)
			{
				boolean hasException = false;
				try
				{
					ab.setCommanderAuthority(commanders[i], levels2[i]);
				} catch (Exception ex)
				{
					assert (ex instanceof IllegalArgumentException);
					hasException = true;
				}
				assert (hasException);
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders[i])
						.equals(levels[i]));
			}

			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.setCommanderAuthority(commanders[i],
						levels[levels.length - 1 - i]).equals(levels[i]));
			}
			for (int i = 0; i < levels2.length; i++)
			{
				assert (ab.addHighestLevel(levels2[i], passwords2[i]));
			}
			for (int i = 0; i < levels.length; i++)
			{
				assert (ab.setCommanderAuthority(commanders[i], levels2[i])
						.equals(levels[levels.length - 1 - i]));
			}
			for (int i = 0; i < commanders.length; i++)
			{
				assert (ab.findPermittedAuthority(commanders[i])
						.equals(levels2[i]));
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	public void testWhenUnionLevelCommandHeadAndCommander()
	{
		try
		{
			assert (ab.getNumOfLevels() == 0);
			for (int i = 0; i < levels.length; i++)
			{
				assert(ab.addHighestLevel(levels[i], passwords[i]));
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommandHeadAuthority(commandHeads[i], levels[i])==null);
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommanderAuthority(commanders[i], levels[i])==null);
			}
			
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.changeLevelPassword(levels[i], passwords2[i]).equals(passwords[i]));
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.verifyLevelAndPassword(levels[i], passwords2[i]));
				assert(!ab.verifyLevelAndPassword(levels[i], passwords[i]));
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.changeLevelPassword(levels[i], passwords[i]).equals(passwords2[i]));
			}
			for(int i=0;i<levels.length;i++)
			{
				for(int j=0;j<levels.length;j++)
				{
					if(i!=j)
					{
						assert(ab.compareAuthority(levels[i], levels[j])*(i-j)>0);
					}
					else
					{
						assert(ab.compareAuthority(levels[i], levels[j])==0);
					}
				}
			}
			
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.containsLevel(levels[i]));
			}
			for(int i=0;i<levels2.length;i++)
			{
				assert(!ab.containsLevel(levels2[i]));
			}
			assert(ab.getAllLevels().size()==levels.length);
			int temp_idx=0;
			for(String level : ab.getAllLevels())
			{
				assert(level!=null);
				level.equals(levels[temp_idx]);
				temp_idx++;
			}
			
			assert(ab.getNumOfLevels()==levels.length);
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.verifyLevelAndPassword(levels[i], passwords[i]));
				assert(!ab.verifyLevelAndPassword(levels[i], passwords2[i]));
				assert(!ab.verifyLevelAndPassword(levels2[i], passwords[i]));
				assert(!ab.verifyLevelAndPassword(levels2[i], passwords2[i]));
			}
			
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.findPermittedAuthority(commanders[i]).equals(levels[i]));
				assert(ab.findRequiredAuthority(commandHeads[i]).equals(levels[i]));
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommanderAuthority(commanders[i], levels[levels.length-1-i]).equals(levels[i]));
				assert(ab.setCommandHeadAuthority(commandHeads[i], levels[levels.length-1-i]).equals(levels[i]));
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.findPermittedAuthority(commanders[i]).equals(levels[levels.length-1-i]));
				assert(ab.findRequiredAuthority(commandHeads[i]).equals(levels[levels.length-1-i]));
			}
			
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommanderAuthority(commanders[i], null).equals(levels[levels.length-1-i]));
				assert(ab.setCommandHeadAuthority(commandHeads[i], null).equals(levels[levels.length-1-i]));
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.findPermittedAuthority(commanders[i])==null);
				assert(ab.findRequiredAuthority(commandHeads[i])==null);
			}
			
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommandHeadAuthority(commandHeads[i], levels[i])==null);
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommanderAuthority(commanders[i], levels[i])==null);
			}
			for(int i=0;i<levels2.length;i++)
			{
				assert(ab.addHighestLevel(levels2[i], passwords2[i]));
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommanderAuthority(commanders[i], levels2[i]).equals(levels[i]));
				assert(ab.setCommandHeadAuthority(commandHeads[i], levels2[i]).equals(levels[i]));
			}
			for(int i=levels2.length-1;i>=0;i--)
			{
				assert(ab.removeHighestLevel().equals(levels2[i]));

				assert(ab.addHighestLevel(levels2[i], passwords2[i]));
				assert(ab.removeHighestLevel().equals(levels2[i]));
				
				for(int j=0;j<i;j++)
				{
					assert(ab.verifyLevelAndPassword(levels2[j], passwords2[j]));
				}
				for(int j=0;j<levels.length;j++)
				{
					assert(ab.verifyLevelAndPassword(levels[j], passwords[j]));
				}
				
				boolean hasException=false;
				try
				{
					ab.changeLevelPassword(levels2[i], passwords[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				assert(!ab.getAllLevels().contains(levels2[i]));
				assert(ab.getNumOfLevels()==levels.length+i);
				
				if (i > 0)
				{
					for (int j = i; j < levels2.length; j++)
					{
						ab.findPermittedAuthority(commanders[j]).equals(
								levels2[i - 1]);
						ab.findRequiredAuthority(commandHeads[j]).equals(
								levels2[i - 1]);
					}
					for (int j = 0; j < i; j++)
					{
						ab.findPermittedAuthority(commanders[j]).equals(
								levels2[j]);
						ab.findRequiredAuthority(commandHeads[j]).equals(
								levels2[j]);
					}
				}
				else if(0==i)
				{
					for (int j = i; j < levels2.length; j++)
					{
						ab.findPermittedAuthority(commanders[j]).equals(
								levels[levels.length-1]);
						ab.findRequiredAuthority(commandHeads[j]).equals(
								levels[levels.length-1]);
					}
				}
				else
				{
					assert(false);
				}
				
			}
			
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.findPermittedAuthority(commanders[i]).equals(levels[levels.length-1]));
				assert(ab.findRequiredAuthority(commandHeads[i]).equals(levels[levels.length-1]));
			}
			
			for(int i=levels.length-1;i>=0;i--)
			{
				assert(ab.removeHighestLevel().equals(levels[i]));
				
				if(i>0)
				{
					for(int j=0;j<levels.length;j++)
					{
						assert(ab.findRequiredAuthority(commandHeads[j]).equals(levels[i-1]));
						assert(ab.findPermittedAuthority(commanders[j]).equals(levels[i-1]));
					}
				}
				else if(i==0)
				{
					for(int j=0;j<levels.length;j++)
					{
						assert(ab.findRequiredAuthority(commandHeads[j])==null);
						assert(ab.findPermittedAuthority(commanders[j])==null);
					}
				}
				else
				{
					assert(false);
				}
				
			}
			
			assert(ab.getAllLevels().size()==0);
			assert(ab.getNumOfLevels()==0);
			for(int j=0;j<levels.length;j++)
			{
				assert(ab.findRequiredAuthority(commandHeads[j])==null);
				assert(ab.findPermittedAuthority(commanders[j])==null);
			}
			
			for (int i = 0; i < levels.length; i++)
			{
				assert(ab.addHighestLevel(levels[i], passwords[i]));
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommandHeadAuthority(commandHeads[i], levels[i])==null);
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommanderAuthority(commanders[i], levels[i])==null);
			}
			
			/////////////////////////
			
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommandHeadAuthority(commandHeads[i], null).equals(levels[i]));
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommanderAuthority(commanders[i], null).equals(levels[i]));
			}
			for(int i=levels.length-1;i>=0;i--)
			{
				assert(ab.removeHighestLevel().equals(levels[i]));
				assert(!ab.getAllLevels().contains(levels[i]));
				assert(!ab.containsLevel(levels[i]));
				
				boolean hasException=false;
				try
				{
					ab.changeLevelPassword(levels[i], passwords[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.compareAuthority(levels[i], levels[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.changeLevelPassword(levels[i], passwords[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.setCommanderAuthority(commanders[i], levels[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.setCommandHeadAuthority(commandHeads[i], levels[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					assert(!ab.verifyLevelAndPassword(levels[i], passwords[i]));
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(!hasException);
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.findPermittedAuthority(commanders[i])==null);
				assert(ab.findRequiredAuthority(commandHeads[i])==null);
				assert(ab.getAllLevels().size()==0);
				assert(ab.getNumOfLevels()==0);
				
				assert(!ab.containsLevel(levels[i]));
				
				boolean hasException=false;
				try
				{
					ab.changeLevelPassword(levels[i], passwords[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.compareAuthority(levels[i], levels[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.changeLevelPassword(levels[i], passwords[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.setCommanderAuthority(commanders[i], levels[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.setCommandHeadAuthority(commandHeads[i], levels[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					assert(!ab.verifyLevelAndPassword(levels[i], passwords[i]));
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(!hasException);
				
			}
			
			
			/////////////////////////////////////////////
			
			for (int i = 0; i < levels.length; i++)
			{
				assert(ab.addHighestLevel(levels[i], passwords[i]));
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommandHeadAuthority(commandHeads[i], levels[i])==null);
			}
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.setCommanderAuthority(commanders[i], levels[i])==null);
			}
			
			for(int i=levels.length-1;i>=0;i--)
			{
				assert(ab.removeHighestLevel().equals(levels[i]));
				
				assert(!ab.containsLevel(levels[i]));
				
				boolean hasException=false;
				try
				{
					ab.changeLevelPassword(levels[i], passwords[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.compareAuthority(levels[i], levels[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.changeLevelPassword(levels[i], passwords[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.setCommanderAuthority(commanders[i], levels[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					ab.setCommandHeadAuthority(commandHeads[i], levels[i]);
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(hasException);
				
				hasException=false;
				try
				{
					assert(!ab.verifyLevelAndPassword(levels[i], passwords[i]));
				}catch(Exception ex)
				{
					assert(ex instanceof IllegalArgumentException);
					hasException=true;
				}
				assert(!hasException);
				
				////////////
				
				if(i>0)
				{
					for(int j=0;j<i;j++)
					{
						assert(ab.findPermittedAuthority(commanders[j]).equals(levels[j]));
						assert(ab.findRequiredAuthority(commandHeads[j]).equals(levels[j]));
					}
					for(int j=i;j<levels.length;j++)
					{
						assert(ab.findPermittedAuthority(commanders[j]).equals(levels[i-1]));
						assert(ab.findRequiredAuthority(commandHeads[j]).equals(levels[i-1]));
					}
				}
				else if(0==i)
				{
					for(int j=0;j<levels.length;j++)
					{
						assert(ab.findPermittedAuthority(commanders[j])==null);
						assert(ab.findRequiredAuthority(commandHeads[j])==null);
					}
				}
				else
				{
					assert(false);
				}
			}
			
			for(int i=0;i<levels.length;i++)
			{
				assert(ab.findPermittedAuthority(commanders[i])==null);
				assert(ab.findRequiredAuthority(commandHeads[i])==null);
				assert(ab.getAllLevels().size()==0);
				assert(ab.getNumOfLevels()==0);
				
				assert(!ab.containsLevel(levels[i]));
				
				assert(ab.removeHighestLevel()==null);
			}
			
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

}
