package com.accela.TestCases.authorityCenter;

import java.util.List;

import com.accela.AuthorityCenter.authorityBase.AuthorityBase;
import com.accela.AuthorityCenter.authorityBase.AuthorityBaseOperatingException;
import com.accela.AuthorityCenter.authorityBase.SimplePasswordManager;
import com.accela.AuthorityCenter.ruleFilter.AuthorityFilter;
import com.accela.AuthorityCenter.ruleFilter.AuthorityFilteringException;
import com.accela.AuthorityCenter.shared.CommandWithAuthority;

import junit.framework.TestCase;

public class TestingAuthorityFilter extends TestCase
{
	private AuthorityFilter authorityFilter=new AuthorityFilter();
	
	private final String[] commandHeads=new String[]{
			"command1",
			"command2",
			"command3",
	};
	
	private final CommanderIDForTest[] commanders=new CommanderIDForTest[]{
		new CommanderIDForTest(1),
		new CommanderIDForTest(2),
		new CommanderIDForTest(3),
	};
	
	private final CommandWithAuthority[][] commands=new CommandWithAuthority[][]{
			new CommandWithAuthority[]{
					new CommandWithAuthority(commanders[0], commandHeads[0]),
					new CommandWithAuthority(commanders[0], commandHeads[1]),
					new CommandWithAuthority(commanders[0], commandHeads[2]),
			},
			new CommandWithAuthority[]{
					new CommandWithAuthority(commanders[1], commandHeads[0]),
					new CommandWithAuthority(commanders[1], commandHeads[1]),
					new CommandWithAuthority(commanders[1], commandHeads[2]),
			},
			new CommandWithAuthority[]{
					new CommandWithAuthority(commanders[2], commandHeads[0]),
					new CommandWithAuthority(commanders[2], commandHeads[1]),
					new CommandWithAuthority(commanders[2], commandHeads[2]),
			},
	};
	
	private RuleForTest[][] rules=new RuleForTest[][]{
			new RuleForTest[]{
				new RuleForTest(true),
				new RuleForTest(true),
				new RuleForTest(true, new Runnable(){
					public void run()
					{
						throw new NullPointerException();
					}
				}),
				new RuleForTest(true, new Runnable(){
					public void run()
					{
						throw new IllegalArgumentException();
					}
				})
			},
			
			new RuleForTest[]{
					new RuleForTest(false),
					new RuleForTest(false),
					new RuleForTest(false, new Runnable(){
						public void run()
						{
							throw new NullPointerException();
						}
					}),
					new RuleForTest(false, new Runnable(){
						public void run()
						{
							throw new IllegalArgumentException();
						}
					})
				},
	};
	
	protected void setUp() throws Exception
	{
		super.setUp();
		
		authorityFilter=new AuthorityFilter();
		clearInvokeCount();
	}

	private void clearInvokeCount()
	{
		for(int i=0;i<rules.length;i++)
		{
			for(int j=0;j<rules[i].length;j++)
			{
				rules[i][j].clearInvokeCount();
			}
		}
		
	}

	protected void tearDown() throws Exception
	{
		super.tearDown();
		
		authorityFilter=null;
	}
	
	public void testDefaultRule()
	{
		AuthorityBase base = new AuthorityBase(new SimplePasswordManager());

		try
		{
			base.addHighestLevel("l1", "l1");
			base.addHighestLevel("l2", "l2");
			base.addHighestLevel("l3", "l3");

			base.setCommandHeadAuthority(commandHeads[1], "l1");
			base.setCommandHeadAuthority(commandHeads[2], "l2");
			base.setCommanderAuthority(commanders[1], "l1");
			base.setCommanderAuthority(commanders[2], "l2");

			assert (authorityFilter.shouldFilter(commands[0][0], base) == false);
			assert (authorityFilter.shouldFilter(commands[0][1], base) == true);
			assert (authorityFilter.shouldFilter(commands[0][2], base) == true);

			assert (authorityFilter.shouldFilter(commands[1][0], base) == false);
			assert (authorityFilter.shouldFilter(commands[1][1], base) == false);
			assert (authorityFilter.shouldFilter(commands[1][2], base) == true);

			assert (authorityFilter.shouldFilter(commands[2][0], base) == false);
			assert (authorityFilter.shouldFilter(commands[2][1], base) == false);
			assert (authorityFilter.shouldFilter(commands[2][2], base) == false);

			authorityFilter.removeAllAuthorityRules();
			for (int i = 0; i < commands.length; i++)
			{
				for (int j = 0; j < commands[i].length; j++)
				{
					assert (authorityFilter.shouldFilter(commands[i][j], base) == false);
				}
			}

		} catch (AuthorityFilteringException ex)
		{
			assert (false);
		} catch (AuthorityBaseOperatingException ex)
		{
			assert (false);
		}

	}
	
	public void testRules()
	{
		AuthorityBase base=new AuthorityBase(new SimplePasswordManager());
		
		for(int i=0;i<rules.length;i++)
		{
			for(int j=0;j<rules[i].length;j++)
			{
				authorityFilter.addAuthorityRule(rules[i][j]);
			}
		}
		
		for(int i=0;i<commands.length;i++)
		{
			for(int j=0;j<commands[i].length;j++)
			{
				try
				{
					assert(authorityFilter.shouldFilter(commands[i][j], base)==true);
				} catch (AuthorityFilteringException ex)
				{
					assert(false);
				}
				
				boolean findActivedRule=false;
				for(int inner_i=0;inner_i<rules.length;inner_i++)
				{
					for(int inner_j=0;inner_j<rules[inner_i].length;inner_j++)
					{
						if(rules[inner_i][inner_j].getInvokeCount()==1)
						{
							try
							{
								if(rules[inner_i][inner_j].shouldFilter(commands[0][0], base))
								{
									findActivedRule=true;
								}
							} catch (Exception e)
							{
								
							}
						}
					}
				}
				
				assert(findActivedRule);
				clearInvokeCount();
			}
		}
		
		authorityFilter.removeAllAuthorityRules();
		
		
		//////////////
		
		for(int i=0;i<rules[1].length;i++)
		{
			authorityFilter.addAuthorityRule(rules[1][i]);
		}
		
		
		for(int i=0;i<commands.length;i++)
		{
			for(int j=0;j<commands[i].length;j++)
			{
				List<Throwable> exceptions=null;
				
				try
				{
					assert(authorityFilter.shouldFilter(commands[i][j], base)==false);
				} catch (AuthorityFilteringException ex)
				{
					exceptions=ex.getCauseList();
				}
				
				assert(exceptions!=null);
				int nullPointerExceptionCount=0;
				int illegalArgumentExceptionCount=0;
				for(Throwable t : exceptions)
				{
					if(t instanceof NullPointerException)
					{
						nullPointerExceptionCount++;
					}
					else if(t instanceof IllegalArgumentException)
					{
						illegalArgumentExceptionCount++;
					}
					else
					{
						assert(false);
					}
				}
				
				assert(1==nullPointerExceptionCount);
				assert(1==illegalArgumentExceptionCount);
				
				clearInvokeCount();
			}
		}
		
		
		/////////////////////
		
		authorityFilter.addAuthorityRule(rules[0][0]);
		
		for(int i=0;i<commands.length;i++)
		{
			for(int j=0;j<commands[i].length;j++)
			{
				try
				{
					assert(authorityFilter.shouldFilter(commands[i][j], base)==true);
				} catch (AuthorityFilteringException ex)
				{
					assert(false);
				}
				
				clearInvokeCount();
			}
		}
		
	}

}
