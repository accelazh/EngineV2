package com.accela.TestCases.objectStreams;

public class ClassSelf
{
	private int id;
	
	private ClassSelf classSelf;

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public ClassSelf getClassSelf()
	{
		return classSelf;
	}

	public void setClassSelf(ClassSelf classSelf)
	{
		this.classSelf = classSelf;
	}
	
	public boolean equals(Object obj)
	{
		ClassSelf other=(ClassSelf)obj;
		
		return id==other.id;
	}
	
}
