package com.accela.TestCases.commandExecutionCenter;

import com.accela.CommandExecutionCenter.CommandExecutingException;
import com.accela.CommandExecutionCenter.CommandExecutionCenter;
import com.accela.CommandExecutionCenter.shared.ExecutionCommand;

import junit.framework.TestCase;

public class TestingCommandExecutionCenter extends TestCase
{
	private CommandExecutionCenter commandCenter=new CommandExecutionCenter();
	
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
	
	private final ExecutionCommand[][] commands=new ExecutionCommand[][]{
			new ExecutionCommand[]{
					new ExecutionCommand(commanders[0], commandHeads[0]),
					new ExecutionCommand(commanders[0], commandHeads[1]),
					new ExecutionCommand(commanders[0], commandHeads[2]),
			},
			new ExecutionCommand[]{
					new ExecutionCommand(commanders[1], commandHeads[0]),
					new ExecutionCommand(commanders[1], commandHeads[1]),
					new ExecutionCommand(commanders[1], commandHeads[2]),
			},
			new ExecutionCommand[]{
					new ExecutionCommand(commanders[2], commandHeads[0]),
					new ExecutionCommand(commanders[2], commandHeads[1]),
					new ExecutionCommand(commanders[2], commandHeads[2]),
			},
	};
	
	private final CommandHandlerForTest[][] commandHandlers=new CommandHandlerForTest[][]{
		new CommandHandlerForTest[]{
				new CommandHandlerForTest(commandHeads[0]),
				new CommandHandlerForTest(commandHeads[1]),
				new CommandHandlerForTest(commandHeads[2]),
		},
		new CommandHandlerForTest[]{
				new CommandHandlerForTest(commandHeads[0]),
				new CommandHandlerForTest(commandHeads[1]),
				new CommandHandlerForTest(commandHeads[2]),
		},
		new CommandHandlerForTest[]{
				new CommandHandlerForTest(commandHeads[0], new Runnable(){
					public void run()
					{
						throw new NullPointerException();
					}
				}),
				new CommandHandlerForTest(commandHeads[1], new Runnable(){
					public void run()
					{
						throw new NullPointerException();
					}
				}),
				new CommandHandlerForTest(commandHeads[2], new Runnable(){
					public void run()
					{
						throw new NullPointerException();
					}
				}),
		},
		new CommandHandlerForTest[]{
				new CommandHandlerForTest(commandHeads[0], new Runnable(){
					public void run()
					{
						throw new IllegalArgumentException();
					}
				}),
				new CommandHandlerForTest(commandHeads[1], new Runnable(){
					public void run()
					{
						throw new IllegalArgumentException();
					}
				}),
				new CommandHandlerForTest(commandHeads[2], new Runnable(){
					public void run()
					{
						throw new IllegalArgumentException();
					}
				}),
		},
		
	};
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		
		commandCenter=new CommandExecutionCenter();
		clearInvokeCount();
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		commandCenter=null;
	}
	
	private void clearInvokeCount()
	{
		for(int i=0;i<commandHandlers.length;i++)
		{
			for(int j=0;j<commandHandlers[i].length;j++)
			{
				commandHandlers[i][j].clearInvokeCount();
			}
		}
	}
	
	private String generateRandomCommandHead()
	{
		StringBuffer buffer=new StringBuffer();
		
		int count=(int)(16*Math.random());
		for(int i=0;i<count;i++)
		{
			buffer.append((char)('0'+(int)(('z'-'0'+1)*Math.random())));
		}
		
		return buffer.toString();
	}
	
	private String generateRandomCommandHead2()
	{
		boolean random1=Math.random()>=0.5;
		boolean random2=Math.random()>=0.5;
		int count=(int)(16*Math.random());
		
		StringBuffer buffer=new StringBuffer();
		
		for(int i=0;i<count;i++)
		{
			if(random1&&random2)
			{
			    if(Math.random()>=0.5)
			    {
			    	buffer.append((char)('a'+(int)(('z'-'a'+1)*Math.random())));
			    }
			    else
			    {
			    	buffer.append((char)('A'+(int)(('Z'-'A'+1)*Math.random())));
			    }
			}
			else if(random1&&!random2)
			{
				buffer.append((char)('0'+(int)(('9'-'0'+1)*Math.random())));
			}
			else if(!random1&&random2)
			{
				buffer.append('-');
			}
			else if(!random1&&!random2)
			{
				buffer.append('_');
			}
			else
			{
				assert(false);
			}
				
		}
		
		return buffer.toString();
	}
	
	private String generateValidCommandHead()
	{
		boolean random1=Math.random()>=0.5;
		boolean random2=Math.random()>=0.5;
		int count=(int)(16*Math.random())+1;
		
		StringBuffer buffer=new StringBuffer();
		
		if(Math.random()>=0.5)
	    {
	    	buffer.append((char)('a'+(int)(('z'-'a'+1)*Math.random())));
	    }
	    else
	    {
	    	buffer.append((char)('A'+(int)(('Z'-'A'+1)*Math.random())));
	    }
		
		for(int i=0;i<count;i++)
		{
			if(random1&&random2)
			{
			    if(Math.random()>=0.5)
			    {
			    	buffer.append((char)('a'+(int)(('z'-'a'+1)*Math.random())));
			    }
			    else
			    {
			    	buffer.append((char)('A'+(int)(('Z'-'A'+1)*Math.random())));
			    }
			}
			else if(random1&&!random2)
			{
				buffer.append((char)('0'+(int)(('9'-'0'+1)*Math.random())));
			}
			else if(!random1&&random2)
			{
				buffer.append('-');
			}
			else if(!random1&&!random2)
			{
				buffer.append('_');
			}
			else
			{
				assert(false);
			}
				
		}
		
		return buffer.toString();
	}
	
	private boolean generateAndTestCommandHead()
	{
		String temp=null;
		if(!ExecutionCommand.
				checkCommandHeadValid(temp=
						generateValidCommandHead()))
		{
			System.out.println(temp);
			assert(false);
		}
		
		boolean random1=Math.random()>=0.5;
		boolean random2=Math.random()>=0.5;
		String str=null;
		
		if(random1&&random2)
		{
		   str=generateRandomCommandHead();
		}
		else if(random1&&!random2)
		{
			str=generateRandomCommandHead2();
		}
		else if(!random1&&random2)
		{
			str=generateValidCommandHead();
		}
		else if(!random1&&!random2)
		{
			if(Math.random()>=0.5)
			{
				str=new String("");
				assert(str.length()==0);
			}
			else
			{
				str=null;
			}
		}
		else
		{
			assert(false);
		}
		
		boolean valid=true;
		if(null==str||str.length()<1)
		{
			valid=false;
		}
		else
		{
			if(!((str.charAt(0)>='A'&&str.charAt(0)<='Z')
					||(str.charAt(0)>='a'&&str.charAt(0)<='z')))
			{
				valid=false;
			}
		}
		
		if (str != null)
		{
			for (char c : str.toCharArray())
			{
				if (!((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')
						|| (c >= '0' && c <= '9') || ('-' == c) || ('_' == c)))
				{
					valid = false;
				}
			}
		}
		
		if(valid==ExecutionCommand.checkCommandHeadValid(str))
		{
			return true;
		}
		else
		{
			System.out.println("str: "+str);
			System.out.println("local check: "+ valid);
			System.out.println("ExecutionCommand check: "+
					ExecutionCommand.checkCommandHeadValid(str));
			
			return false;
		}
		
	}
	
	public void testCommandHeadDValidation()
	{
		for(int i=0;i<1000;i++)
		{
			assert(generateAndTestCommandHead());
		}
		
	}
	
	public void testCommandCenter()
	{
		for(int i=0;i<commandHandlers.length;i++)
		{
			for(int j=0;j<commandHandlers[i].length;j++)
			{
				commandCenter.addCommandHandler(commandHandlers[i][j]);
			}
		}
		
		commandCenter.addCommandHandler(commandHandlers[0][0]);
		commandCenter.addCommandHandler(commandHandlers[0][0]);
		
		commandCenter.addCommandHandler(commandHandlers[2][0]);
		commandCenter.addCommandHandler(commandHandlers[2][0]);
		
		//======================
		
		for(int i=0;i<commands.length;i++)
		{
			for(int j=0;j<commands[i].length;j++)
			{
				commandExecutionTest(i, j);
				clearInvokeCount();
			}
		}
		
		//=======================
		int removeLength=commandCenter.removeCommandHandler(commandHeads[0]).length;
		assert(8==removeLength);

		
		for(int i=0;i<commands.length;i++)
		{
			for(int j=0;j<commands[i].length;j++)
			{
				commandExecutionTest2(i, j);
				clearInvokeCount();
			}
		}
		
	}
	
	private void commandExecutionTest(int row, int column)
	{
		CommandExecutingException exception=null;
		try
		{
			commandCenter.executeCommand(commands[row][column]);
		} catch (CommandExecutingException ex)
		{
			exception=ex;
		}
		assert(exception!=null);
		
		for(int i=0;i<commandHandlers.length;i++)
		{
			for(int j=0;j<commandHandlers[i].length;j++)
			{
				if(j==column)
				{
					if(0==i&&0==j)
					{
						assert(commandHandlers[i][j].getInvokeCount()==3);
					}
					else if(2==i&&0==j)
					{
						assert(commandHandlers[i][j].getInvokeCount()==3);
					}
					else
					{
						assert(commandHandlers[i][j].getInvokeCount()==1);
					}
				}
				else
				{
					assert(commandHandlers[i][j].getInvokeCount()==0);
				}
			}
		}
		
		//===================
		
		int nullPointerExceptionCount=0;
		int illegalArgumentExceptionCount=0;
		for(Throwable ex : exception.getCauseList())
		{
			if(ex instanceof NullPointerException)
			{
				nullPointerExceptionCount++;
			}
			else if(ex instanceof IllegalArgumentException)
			{
				illegalArgumentExceptionCount++;
			}
			else
			{
				assert(false);
			}
		}
		
		if(0==column)
		{
			assert(3==nullPointerExceptionCount);
		}
		else
		{
			assert(1==nullPointerExceptionCount);
		}
		assert(1==illegalArgumentExceptionCount);
		
	}
	
	private void commandExecutionTest2(int row, int column)
	{
		CommandExecutingException exception=null;
		try
		{
			commandCenter.executeCommand(commands[row][column]);
		} catch (CommandExecutingException ex)
		{
			exception=ex;
		}
		
		for(int i=0;i<commandHandlers.length;i++)
		{
			for(int j=0;j<commandHandlers[i].length;j++)
			{
				if(0==column)
				{
					assert(commandHandlers[i][j].getInvokeCount()==0);
					continue;
				}
				
				if(j==column)
				{
					if(0==i&&0==j)
					{
						assert(commandHandlers[i][j].getInvokeCount()==3);
					}
					else if(2==i&&0==j)
					{
						assert(commandHandlers[i][j].getInvokeCount()==3);
					}
					else
					{
						assert(commandHandlers[i][j].getInvokeCount()==1);
					}
				}
				else
				{
					assert(commandHandlers[i][j].getInvokeCount()==0);
				}
			}
		}
		
		//===================
		
		if(0==column)
		{
			exception=null;
		}
		else
		{
			int nullPointerExceptionCount=0;
			int illegalArgumentExceptionCount=0;
			for(Throwable ex : exception.getCauseList())
			{
				if(ex instanceof NullPointerException)
				{
					nullPointerExceptionCount++;
				}
				else if(ex instanceof IllegalArgumentException)
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

		}
		
	}

}
