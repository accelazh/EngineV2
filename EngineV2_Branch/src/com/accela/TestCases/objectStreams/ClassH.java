package com.accela.TestCases.objectStreams;

public class ClassH
{
	private int id;

	private ClassI classI;

	public ClassH()
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

	public ClassI getClassI()
	{
		return classI;
	}

	public void setClassI(ClassI classI)
	{
		this.classI = classI;
	}
	
	public boolean equals(Object obj)
	{
		ClassH other=(ClassH)obj;
		
		if(id==other.id
				&&classI.equals(other.classI))
		{
			return true;
		}
		
		return false;
	}

}
