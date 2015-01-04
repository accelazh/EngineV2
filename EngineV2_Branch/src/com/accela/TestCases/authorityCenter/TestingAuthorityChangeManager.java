package com.accela.TestCases.authorityCenter;

import java.util.LinkedList;
import java.util.List;

import com.accela.AuthorityCenter.authorityBase.AuthorityBase;
import com.accela.AuthorityCenter.authorityBase.SimplePasswordManager;
import com.accela.AuthorityCenter.changeManager.AuthorityChangeManager;

import junit.framework.TestCase;

public class TestingAuthorityChangeManager extends TestCase
{
	private AuthorityChangeManager acm = new AuthorityChangeManager();

	private AuthorityBase ab1 = new AuthorityBase(new SimplePasswordManager());

	private AuthorityBase ab2 = new AuthorityBase(new SimplePasswordManager());

	private AuthorityBase ab3 = new AuthorityBase(new SimplePasswordManager());

	private AuthorityBase ab4 = new AuthorityBase(new SimplePasswordManager());

	private String[] levels = new String[] { "1", "2", "3",

	"4", "5", "6",

	"7", "8", "9",

	};

	private String[] passwords = new String[] { "A", "B", "C",

	"D", "E", "F",

	"G", "H", "I", };

	private String levels2[] = new String[] { "1adsf", "2adsf", "3ghj",

	"4sda", "sdaf5", "6yui",

	"adf7", "8uynm", "ati3jds", };

	private String[] passwords2 = new String[] { "go", "now", "or",

	"III", "will", "kick",

	"you", "out", "!!",

	};

	private CommanderIDForTest[] commanders = new CommanderIDForTest[] {
			new CommanderIDForTest(1), new CommanderIDForTest(2),
			new CommanderIDForTest(3),

			new CommanderIDForTest(4), new CommanderIDForTest(5),
			new CommanderIDForTest(6),

			new CommanderIDForTest(7), new CommanderIDForTest(8),
			new CommanderIDForTest(9), };

	private CommanderIDForTest[] commanders2 = new CommanderIDForTest[] {
			new CommanderIDForTest(11), new CommanderIDForTest(22),
			new CommanderIDForTest(33),

			new CommanderIDForTest(44), new CommanderIDForTest(55),
			new CommanderIDForTest(66),

			new CommanderIDForTest(77), new CommanderIDForTest(88),
			new CommanderIDForTest(99), };

	private String[] commandHeads = new String[] { "today", "is", "not",

	"such", "a:", "good",

	"day", "yes", "?", };

	private String[] commandHeads2 = new String[] { "today3", "is3", "not4",

	"such5", "a6", "good7",

	"day8", "yes9", "?9", };

	protected void setUp() throws Exception
	{
		super.setUp();

		acm = new AuthorityChangeManager();
		ab1 = new AuthorityBase(new SimplePasswordManager());
		ab2 = new AuthorityBase(new SimplePasswordManager());
		ab3 = new AuthorityBase(new SimplePasswordManager());
		ab4 = new AuthorityBase(new SimplePasswordManager());

		for (int i = 0; i < levels2.length; i++)
		{
			ab4.addHighestLevel(levels2[i], passwords2[i]);
		}

		for (int i = 0; i < levels.length; i++)
		{
			ab2.addHighestLevel(levels[i], passwords[i]);
			ab3.addHighestLevel(levels[i], passwords[i]);
			ab4.addHighestLevel(levels[i], passwords[i]);
		}
		for (int i = 0; i < commanders.length; i++)
		{
			ab2.setCommanderAuthority(commanders[i], levels[i]);
			ab3.setCommanderAuthority(commanders[i], levels[i]);
			ab4.setCommanderAuthority(commanders[i], levels[i]);
		}
		for (int i = 0; i < commandHeads.length; i++)
		{
			ab2.setCommandHeadAuthority(commandHeads[i], levels[i]);
			ab3.setCommandHeadAuthority(commandHeads[i], levels[i]);
			ab4.setCommandHeadAuthority(commandHeads[i], levels[i]);
		}

		for (int i = 0; i < levels2.length; i++)
		{
			ab3.addHighestLevel(levels2[i], passwords2[i]);
		}
		for (int i = 0; i < commanders2.length; i++)
		{
			ab3.setCommanderAuthority(commanders2[i], levels2[i]);
		}
		for (int i = 0; i < commandHeads2.length; i++)
		{
			ab3.setCommandHeadAuthority(commandHeads2[i], levels2[i]);
		}
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();

		acm = null;
		ab1 = null;
		ab2 = null;
		ab3 = null;
	}

	public void testChangeLevelPassword()
	{
		try
		{
			// test empty authority base condition
			for (CommanderIDForTest commander : commanders)
			{
				for (String level : levels)
				{
					for (String password : passwords)
					{
						boolean hasException = false;
						try
						{
							acm.changeLevelPassword(commander, level, password,
									ab1);
						} catch (Exception ex)
						{
							assert (ex instanceof IllegalArgumentException);
							hasException = true;
						}
						assert (hasException);
					}
				}
			}

			// test non-empty authority base condition 1
			for (int i = 0; i < commanders.length; i++)
			{
				for (int j = 0; j < levels.length; j++)
				{
					for (int k = 0; k < passwords2.length; k++)
					{
						if (i <= j)
						{
							assert (!acm.changeLevelPassword(commanders[i],
									levels[j], passwords2[k], ab2));

							for (int inner_i = 0; inner_i < levels.length; inner_i++)
							{
								assert (ab2.verifyLevelAndPassword(
										levels[inner_i], passwords[inner_i]));
							}

						} else
						{
							assert (acm.changeLevelPassword(commanders[i],
									levels[j], passwords2[k], ab2));
							for (int inner_i = 0; inner_i < levels.length; inner_i++)
							{
								if (inner_i != j)
								{
									assert (ab2
											.verifyLevelAndPassword(
													levels[inner_i],
													passwords[inner_i]));
								} else
								{
									assert (ab2.verifyLevelAndPassword(
											levels[inner_i], passwords2[k]));
								}
							}
						}

						ab2 = new AuthorityBase(new SimplePasswordManager());
						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							ab2.addHighestLevel(levels[inner_i],
									passwords[inner_i]);
						}
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							ab2.setCommanderAuthority(commanders[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
						{
							ab2.setCommandHeadAuthority(commandHeads[inner_i],
									levels[inner_i]);
						}

					}// for 3rd
				}
			}

			// test non-empty authority base condition 2
			for (int i = 0; i < commanders2.length; i++)
			{
				for (int j = 0; j < levels.length; j++)
				{
					for (int k = 0; k < passwords2.length; k++)
					{
						assert (!acm.changeLevelPassword(commanders2[i],
								levels[j], passwords2[k], ab2));

						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							assert (ab2.verifyLevelAndPassword(levels[inner_i],
									passwords[inner_i]));
						}

						ab2 = new AuthorityBase(new SimplePasswordManager());
						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							ab2.addHighestLevel(levels[inner_i],
									passwords[inner_i]);
						}
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							ab2.setCommanderAuthority(commanders[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
						{
							ab2.setCommandHeadAuthority(commandHeads[inner_i],
									levels[inner_i]);
						}

					}// for 3rd
				}
			}

			// test non-empty authority base condition 3
			for (int i = 0; i < commanders.length; i++)
			{
				for (int j = 0; j < levels.length; j++)
				{
					for (int k = 0; k < passwords.length; k++)
					{
						if (i <= j)
						{
							assert (!acm.changeLevelPassword(commanders[i],
									levels[j], passwords[k], ab2));

							for (int inner_i = 0; inner_i < levels.length; inner_i++)
							{
								assert (ab2.verifyLevelAndPassword(
										levels[inner_i], passwords[inner_i]));
							}

						} else
						{
							assert (acm.changeLevelPassword(commanders[i],
									levels[j], passwords[k], ab2));
							for (int inner_i = 0; inner_i < levels.length; inner_i++)
							{
								if (inner_i != j)
								{
									assert (ab2
											.verifyLevelAndPassword(
													levels[inner_i],
													passwords[inner_i]));
								} else
								{
									assert (ab2.verifyLevelAndPassword(
											levels[inner_i], passwords[k]));
								}
							}
						}

						ab2 = new AuthorityBase(new SimplePasswordManager());
						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							ab2.addHighestLevel(levels[inner_i],
									passwords[inner_i]);
						}
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							ab2.setCommanderAuthority(commanders[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
						{
							ab2.setCommandHeadAuthority(commandHeads[inner_i],
									levels[inner_i]);
						}

					}// for 3rd
				}
			}

			// test non-empty authority base condition 4
			for (int i = 0; i < commanders.length; i++)
			{
				for (int j = 0; j < levels.length; j++)
				{
					for (int k = passwords.length - 1; k >= 0; k--)
					{
						if (i <= j)
						{
							assert (!acm.changeLevelPassword(commanders[i],
									levels[j], passwords[k], ab2));

							for (int inner_i = 0; inner_i < levels.length; inner_i++)
							{
								assert (ab2.verifyLevelAndPassword(
										levels[inner_i], passwords[inner_i]));
							}

						} else
						{
							assert (acm.changeLevelPassword(commanders[i],
									levels[j], passwords[k], ab2));
							for (int inner_i = 0; inner_i < levels.length; inner_i++)
							{
								if (inner_i != j)
								{
									assert (ab2
											.verifyLevelAndPassword(
													levels[inner_i],
													passwords[inner_i]));
								} else
								{
									assert (ab2.verifyLevelAndPassword(
											levels[inner_i], passwords[k]));
								}
							}
						}

						ab2 = new AuthorityBase(new SimplePasswordManager());
						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							ab2.addHighestLevel(levels[inner_i],
									passwords[inner_i]);
						}
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							ab2.setCommanderAuthority(commanders[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
						{
							ab2.setCommandHeadAuthority(commandHeads[inner_i],
									levels[inner_i]);
						}

					}// for 3rd
				}
			}

			// test non-empty authority base condition 5
			for (int i = 0; i < commanders.length; i++)
			{
				for (int j = 0; j < levels2.length; j++)
				{
					for (int k = 0; k < passwords2.length; k++)
					{
						boolean hasException = false;
						try
						{
							acm.changeLevelPassword(commanders[i], levels2[j],
									passwords2[k], ab2);
						} catch (Exception ex)
						{
							assert (ex instanceof IllegalArgumentException);
							hasException = true;
						}
						assert (hasException);

					}// for 3rd
				}
			}

		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}

	}

	public void testHeightenSelfAuthority()
	{
		try
		{
			// test 1
			for (int i = 0; i < commanders.length; i++)
			{
				for (int j = 0; j < levels.length; j++)
				{
					for (int k = 0; k < passwords.length; k++)
					{
						if (i < j&&k==j)
						{
							assert (acm.heightenSelfAuthority(commanders[i],
									levels[j], passwords[k], ab2));
							for (int inner_i = 0; inner_i < commanders.length; inner_i++)
							{
								if (inner_i != i)
								{
									assert (ab2
											.findPermittedAuthority(commanders[inner_i])
											.equals(levels[inner_i]));
								} else
								{
									assert (ab2
											.findPermittedAuthority(commanders[inner_i])
											.equals(levels[j]));
								}
							}

						} else
						{
							assert (!acm.heightenSelfAuthority(commanders[i],
									levels[j], passwords[k], ab2));
							for (int inner_i = 0; inner_i < commanders.length; inner_i++)
							{
								assert (ab2
										.findPermittedAuthority(commanders[inner_i])
										.equals(levels[inner_i]));
							}
						}

						ab2 = new AuthorityBase(new SimplePasswordManager());
						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							ab2.addHighestLevel(levels[inner_i],
									passwords[inner_i]);
						}
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							ab2.setCommanderAuthority(commanders[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
						{
							ab2.setCommandHeadAuthority(commandHeads[inner_i],
									levels[inner_i]);
						}
					}

					for (int k = 0; k < passwords2.length; k++)
					{
						assert (!acm.heightenSelfAuthority(commanders[i],
								levels[j], passwords2[k], ab2));
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							assert (ab2
									.findPermittedAuthority(commanders[inner_i])
									.equals(levels[inner_i]));
						}

						ab2 = new AuthorityBase(new SimplePasswordManager());
						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							ab2.addHighestLevel(levels[inner_i],
									passwords[inner_i]);
						}
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							ab2.setCommanderAuthority(commanders[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
						{
							ab2.setCommandHeadAuthority(commandHeads[inner_i],
									levels[inner_i]);
						}

					}
				}
			}

			// test 2
			for (int i = 0; i < levels2.length; i++)
			{
				assert (ab2.addHighestLevel(levels2[i], passwords2[i]));
			}
			for (int i = 0; i < commanders.length; i++)
			{
				for (int j = 0; j < levels2.length; j++)
				{
					for (int k = 0; k < passwords2.length; k++)
					{
						if(j==k)
						{assert (acm.heightenSelfAuthority(commanders[i],
								levels2[j], passwords2[k], ab2));
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							if (inner_i != i)
							{
								assert (ab2
										.findPermittedAuthority(commanders[inner_i])
										.equals(levels[inner_i]));
							} else
							{
								assert (ab2
										.findPermittedAuthority(commanders[inner_i])
										.equals(levels2[j]));
							}
						}
						}
						else
						{
							assert (!acm.heightenSelfAuthority(commanders[i],
									levels2[j], passwords2[k], ab2));
							for (int inner_i = 0; inner_i < commanders.length; inner_i++)
							{
								assert (ab2
										.findPermittedAuthority(commanders[inner_i])
										.equals(levels[inner_i]));
							}
						}

						ab2 = new AuthorityBase(new SimplePasswordManager());
						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							ab2.addHighestLevel(levels[inner_i],
									passwords[inner_i]);
						}
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							ab2.setCommanderAuthority(commanders[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
						{
							ab2.setCommandHeadAuthority(commandHeads[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < levels2.length; inner_i++)
						{
							assert (ab2.addHighestLevel(levels2[inner_i],
									passwords2[inner_i]));
						}
					}

					for (int k = 0; k < passwords.length; k++)
					{
						assert (!acm.heightenSelfAuthority(commanders[i],
								levels2[j], passwords[k], ab2));
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							assert (ab2
									.findPermittedAuthority(commanders[inner_i])
									.equals(levels[inner_i]));
						}

						ab2 = new AuthorityBase(new SimplePasswordManager());
						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							ab2.addHighestLevel(levels[inner_i],
									passwords[inner_i]);
						}
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							ab2.setCommanderAuthority(commanders[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
						{
							ab2.setCommandHeadAuthority(commandHeads[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < levels2.length; inner_i++)
						{
							assert (ab2.addHighestLevel(levels2[inner_i],
									passwords2[inner_i]));
						}

					}

				}
			}

			// test 3
			ab2 = new AuthorityBase(new SimplePasswordManager());
			for (int inner_i = 0; inner_i < levels.length; inner_i++)
			{
				ab2.addHighestLevel(levels[inner_i], passwords[inner_i]);
			}
			for (int inner_i = 0; inner_i < commanders.length; inner_i++)
			{
				ab2.setCommanderAuthority(commanders[inner_i], levels[inner_i]);
			}
			for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
			{
				ab2.setCommandHeadAuthority(commandHeads[inner_i],
						levels[inner_i]);
			}
			for (int i = 0; i < commanders2.length; i++)
			{
				for (int j = 0; j < levels.length; j++)
				{
					for (int k = 0; k < passwords.length; k++)
					{
						if(j==k)
						{assert (acm.heightenSelfAuthority(commanders2[i],
								levels[j], passwords[k], ab2));
						for (int inner_i = 0; inner_i < commanders2.length; inner_i++)
						{
							if (inner_i != i)
							{
								assert (ab2
										.findPermittedAuthority(commanders2[inner_i]) == null);
							} else
							{
								assert (ab2
										.findPermittedAuthority(commanders2[inner_i])
										.equals(levels[j]));
							}
						}
						}else
						{
							assert (!acm.heightenSelfAuthority(commanders2[i],
									levels[j], passwords[k], ab2));
							for (int inner_i = 0; inner_i < commanders2.length; inner_i++)
							{
									assert (ab2
											.findPermittedAuthority(commanders2[inner_i]) == null);
							}
						}

						ab2 = new AuthorityBase(new SimplePasswordManager());
						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							ab2.addHighestLevel(levels[inner_i],
									passwords[inner_i]);
						}
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							ab2.setCommanderAuthority(commanders[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
						{
							ab2.setCommandHeadAuthority(commandHeads[inner_i],
									levels[inner_i]);
						}
					}

					for (int k = 0; k < passwords2.length; k++)
					{
						assert (!acm.heightenSelfAuthority(commanders[i],
								levels[j], passwords2[k], ab2));
						for (int inner_i = 0; inner_i < commanders2.length; inner_i++)
						{
							assert (ab2
									.findPermittedAuthority(commanders2[inner_i]) == null);
						}

						ab2 = new AuthorityBase(new SimplePasswordManager());
						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							ab2.addHighestLevel(levels[inner_i],
									passwords[inner_i]);
						}
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							ab2.setCommanderAuthority(commanders[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
						{
							ab2.setCommandHeadAuthority(commandHeads[inner_i],
									levels[inner_i]);
						}

					}
				}
			}

			// test 4
			ab2 = new AuthorityBase(new SimplePasswordManager());
			for (int inner_i = 0; inner_i < levels.length; inner_i++)
			{
				ab2.addHighestLevel(levels[inner_i], passwords[inner_i]);
			}
			for (int inner_i = 0; inner_i < commanders.length; inner_i++)
			{
				ab2.setCommanderAuthority(commanders[inner_i], levels[inner_i]);
			}
			for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
			{
				ab2.setCommandHeadAuthority(commandHeads[inner_i],
						levels[inner_i]);
			}
			for (int i = 0; i < commanders.length; i++)
			{
				for (int j = 0; j < levels2.length; j++)
				{
					for (int k = 0; k < passwords.length; k++)
					{
						boolean hasException = false;
						try
						{
							acm.heightenSelfAuthority(commanders[i],
									levels2[j], passwords[k], ab2);
						} catch (Exception ex)
						{
							assert (ex instanceof IllegalArgumentException);
							hasException = true;
						}
						assert (hasException);

					}
				}
			}
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	public void testLowerSelfAuthority()
	{
		try
		{
			// test 1
			for (int i = 0; i < commanders.length; i++)
			{
				for (int j = 0; j < levels.length; j++)
				{
					if (i <= j)
					{
						assert (!acm.lowerSelfAuthority(commanders[i],
								levels[j], ab2));
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							assert (ab2
									.findPermittedAuthority(commanders[inner_i])
									.equals(levels[inner_i]));
						}
					} else
					{
						assert (acm.lowerSelfAuthority(commanders[i],
								levels[j], ab2));
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							if (inner_i != i)
							{
								assert (ab2
										.findPermittedAuthority(commanders[inner_i])
										.equals(levels[inner_i]));
							} else
							{
								assert (ab2
										.findPermittedAuthority(commanders[inner_i])
										.equals(levels[j]));
							}
						}
					}

					ab2 = new AuthorityBase(new SimplePasswordManager());
					for (int inner_i = 0; inner_i < levels.length; inner_i++)
					{
						ab2
								.addHighestLevel(levels[inner_i],
										passwords[inner_i]);
					}
					for (int inner_i = 0; inner_i < commanders.length; inner_i++)
					{
						ab2.setCommanderAuthority(commanders[inner_i],
								levels[inner_i]);
					}
					for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
					{
						ab2.setCommandHeadAuthority(commandHeads[inner_i],
								levels[inner_i]);
					}

				}

				assert (acm.lowerSelfAuthority(commanders[i], null, ab2));
				for (int inner_i = 0; inner_i < commanders.length; inner_i++)
				{
					if (inner_i != i)
					{
						assert (ab2.findPermittedAuthority(commanders[inner_i])
								.equals(levels[inner_i]));
					} else
					{
						assert (ab2.findPermittedAuthority(commanders[inner_i]) == null);
					}
				}

				ab2 = new AuthorityBase(new SimplePasswordManager());
				for (int inner_i = 0; inner_i < levels.length; inner_i++)
				{
					ab2.addHighestLevel(levels[inner_i], passwords[inner_i]);
				}
				for (int inner_i = 0; inner_i < commanders.length; inner_i++)
				{
					ab2.setCommanderAuthority(commanders[inner_i],
							levels[inner_i]);
				}
				for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
				{
					ab2.setCommandHeadAuthority(commandHeads[inner_i],
							levels[inner_i]);
				}
			}

			// test 2 null authority condition
			for (int i = 0; i < commanders2.length; i++)
			{
				for (int j = 0; j < levels.length; j++)
				{
					assert (!acm.lowerSelfAuthority(commanders2[i], levels[j],
							ab2));
					for (int inner_i = 0; inner_i < commanders.length; inner_i++)
					{
						assert (ab2
								.findPermittedAuthority(commanders2[inner_i]) == null);
					}

					ab2 = new AuthorityBase(new SimplePasswordManager());
					for (int inner_i = 0; inner_i < levels.length; inner_i++)
					{
						ab2.addHighestLevel(levels[inner_i],
								passwords[inner_i]);
					}
					for (int inner_i = 0; inner_i < commanders.length; inner_i++)
					{
						ab2.setCommanderAuthority(commanders[inner_i],
								levels[inner_i]);
					}
					for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
					{
						ab2.setCommandHeadAuthority(commandHeads[inner_i],
								levels[inner_i]);
					}

				}

				assert (!acm.lowerSelfAuthority(commanders2[i], null, ab2));
				for (int inner_i = 0; inner_i < commanders.length; inner_i++)
				{
					assert (ab2.findPermittedAuthority(commanders2[inner_i]) == null);
				}

				ab2 = new AuthorityBase(new SimplePasswordManager());
				for (int inner_i = 0; inner_i < levels.length; inner_i++)
				{
					ab2.addHighestLevel(levels[inner_i], passwords[inner_i]);
				}
				for (int inner_i = 0; inner_i < commanders.length; inner_i++)
				{
					ab2.setCommanderAuthority(commanders[inner_i],
							levels[inner_i]);
				}
				for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
				{
					ab2.setCommandHeadAuthority(commandHeads[inner_i],
							levels[inner_i]);
				}
			}

			// test 3 levels2 - levels1 condition
			for (int i = 0; i < commanders.length; i++)
			{
				for (int j = 0; j < levels2.length; j++)
				{
					assert (acm.lowerSelfAuthority(commanders[i], levels2[j],
							ab4));
					for (int inner_i = 0; inner_i < commanders.length; inner_i++)
					{
						if (inner_i != i)
						{
							assert (ab4
									.findPermittedAuthority(commanders[inner_i])
									.equals(levels[inner_i]));
						} else
						{
							assert (ab4
									.findPermittedAuthority(commanders[inner_i])
									.endsWith(levels2[j]));
						}
					}

					for (int inner_i = 0; inner_i < levels2.length; inner_i++)
					{
						ab4.addHighestLevel(levels2[inner_i],
								passwords2[inner_i]);
					}
					for (int inner_i = 0; inner_i < levels.length; inner_i++)
					{
						ab4
								.addHighestLevel(levels[inner_i],
										passwords[inner_i]);
					}
					for (int inner_i = 0; inner_i < commanders.length; inner_i++)
					{
						ab4.setCommanderAuthority(commanders[inner_i],
								levels[inner_i]);
					}
					for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
					{
						ab4.setCommandHeadAuthority(commandHeads[inner_i],
								levels[inner_i]);
					}

				}
			}

			// test 4 not contained condition
			ab2 = new AuthorityBase(new SimplePasswordManager());
			for (int inner_i = 0; inner_i < levels.length; inner_i++)
			{
				ab2.addHighestLevel(levels[inner_i], passwords[inner_i]);
			}
			for (int inner_i = 0; inner_i < commanders.length; inner_i++)
			{
				ab2.setCommanderAuthority(commanders[inner_i], levels[inner_i]);
			}
			for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
			{
				ab2.setCommandHeadAuthority(commandHeads[inner_i],
						levels[inner_i]);
			}

			for (int i = 0; i < commanders.length; i++)
			{
				for (int j = 0; j < levels2.length; j++)
				{
					boolean hasException = false;
					try
					{
						acm.lowerSelfAuthority(commanders[i], levels2[j], ab2);
					} catch (Exception ex)
					{
						assert (ex instanceof IllegalArgumentException);
						hasException = true;
					}
					assert (hasException);
				}
			}

		} catch (Exception ex)
		{
			assert (false);
		}

	}

	public void testSetOtherAuthority()
	{
		final List<CommanderIDForTest> commanderList = new LinkedList<CommanderIDForTest>();
		for (int i = 0; i < commanders.length; i++)
		{
			commanderList.add(commanders[i]);
		}
		for (int i = 0; i < commanders2.length; i++)
		{
			commanderList.add(commanders2[i]);
		}
		commanderList.add(null);

		final List<String> levelList = new LinkedList<String>();
		for (int i = 0; i < levels.length; i++)
		{
			levelList.add(levels[i]);
		}
		for (int i = 0; i < levels2.length; i++)
		{
			levelList.add(levels2[i]);
		}
		levelList.add(null);

		AuthorityBase base = ab2;

		try
		{
			// test 1
			for (CommanderIDForTest selfCommander : commanderList)
			{
				for (CommanderIDForTest otherCommander : commanderList)
				{
					for (String level : levelList)
					{
						if(selfCommander!=null
								&&otherCommander!=null
								&&!selfCommander.equals(otherCommander)
								&&(null==level||base.containsLevel(level))
								&&base.compareAuthority(level, 
										base.findPermittedAuthority(selfCommander))
										<=0
								&&base.compareAuthority(
										base.findPermittedAuthority(otherCommander), 
										base.findPermittedAuthority(selfCommander))
										<0
								&&base.compareAuthority(level, 
										base.findPermittedAuthority(otherCommander))!=0)
						{
							assert(acm.setOtherAuthority(selfCommander, otherCommander,
									level, base));
							
							for(CommanderIDForTest c : commanderList)
							{
								if(c!=otherCommander)
								{
									if(c!=null)
									{
										assert(authorityStayUnchanged(c, base));
									}
								}
								else
								{
									assert(base.compareAuthority(level, 
											base.findPermittedAuthority(c))==0);
								}
							}
						}
						
						else if(selfCommander==null||otherCommander==null
								||selfCommander.equals(otherCommander)
								||(level!=null&&!base.containsLevel(level)))
						{
							boolean hasException=false;
							try
							{
								acm.setOtherAuthority(selfCommander, otherCommander,
										level, base);
							}
							catch(Exception ex)
							{
								assert(ex instanceof NullPointerException
										||ex instanceof IllegalArgumentException);
								hasException=true;
							}
							assert(hasException);
						}
						else
						{
							assert(!acm.setOtherAuthority(selfCommander, otherCommander,
									level, base));
							
							for(CommanderIDForTest c : commanderList)
							{
								if(c!=null)
								{
									assert(authorityStayUnchanged(c, base));
								}
							}
							
						}
						
						base = new AuthorityBase(new SimplePasswordManager());
						for (int inner_i = 0; inner_i < levels.length; inner_i++)
						{
							base.addHighestLevel(levels[inner_i],
									passwords[inner_i]);
						}
						for (int inner_i = 0; inner_i < commanders.length; inner_i++)
						{
							base.setCommanderAuthority(commanders[inner_i],
									levels[inner_i]);
						}
						for (int inner_i = 0; inner_i < commandHeads.length; inner_i++)
						{
							base.setCommandHeadAuthority(commandHeads[inner_i],
									levels[inner_i]);
						}
						
					}// for 3rd
					
				}
			}
			
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}

	}
	
	private boolean authorityStayUnchanged(CommanderIDForTest commander, AuthorityBase base)
	{
		for(int i=0;i<commanders.length;i++)
		{
			if(commanders[i]==commander)
			{
				return base.findPermittedAuthority(commander).equals(levels[i]); 
			}
		}
		
		for(CommanderIDForTest c : commanders2)
		{
			if(c==commander)
			{
				return base.findPermittedAuthority(commander)==null; 
			}
		}
		
		throw new IllegalArgumentException();
	}
	

}
