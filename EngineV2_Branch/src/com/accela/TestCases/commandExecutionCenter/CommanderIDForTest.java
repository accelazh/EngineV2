package com.accela.TestCases.commandExecutionCenter;

import com.accela.CommandExecutionCenter.shared.ExecutionCommanderID;

public class CommanderIDForTest extends ExecutionCommanderID
{
	private int id;
	
	public CommanderIDForTest(int id)
	{
		this.id=id;
	}

	@Override
	public int compareTo(ExecutionCommanderID o)
	{
		return id-((CommanderIDForTest)o).id;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(null==obj)
		{
			return false;
		}
		
		return 0==compareTo((ExecutionCommanderID)obj);
	}

	@Override
	public int hashCode()
	{
		return id;
	}

	public int getId()
	{
		return id;
	}

}
