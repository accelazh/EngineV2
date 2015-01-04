package com.accela.TestCases.authorityCenter;

import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

public class CommanderIDForTest extends CommanderIDWithAuthority
{
	private int id;

	public CommanderIDForTest(int id)
	{
		this.id=id;
	}
	
	@Override
	public int compareTo(CommanderIDWithAuthority o)
	{
		return id-((CommanderIDForTest)o).id;
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof CommanderIDForTest))
		{
			return false;
		}
		
		return 0==compareTo((CommanderIDWithAuthority)obj);
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
