package com.accela.TestCases.objectStreams;

public class ClassI
{
	private int id;

	private ClassG classG;

	public ClassI()
	{
		id = (int) (Math.random() * 100);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public ClassG getClassG()
	{
		return classG;
	}

	public void setClassG(ClassG classG)
	{
		this.classG = classG;
	}
	
	public boolean equals(Object obj)
	{
		ClassI other=(ClassI)obj;
		
		if(id==other.id)
		{
			return true;
		}
		
		return false;
	}

}
