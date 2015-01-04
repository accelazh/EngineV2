package com.accela.TestCases.objectStreams;

public class ClassM
{
	private int id;

	private ClassL ClassL;

	public ClassM()
	{
		id = (int) (Math.random() * 100);
	}

	public boolean equals(Object obj)
	{
		ClassM other = (ClassM) obj;
		if (id == other.id)
		{
			return ClassL.getId() == other.ClassL.getId();
		}

		return false;
	}

	public ClassL getClassL()
	{
		return ClassL;
	}

	public void setClassL(ClassL classL)
	{
		ClassL = classL;
	}

	public int getId()
	{
		return id;
	}
	

}
