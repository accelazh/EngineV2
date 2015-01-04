package com.accela.TestCases.objectStreams;

public class ClassK
{
	private int id;

	private ClassJ ClassJ;

	public ClassK()
	{
		id = (int) (Math.random() * 100);
	}

	public boolean equals(Object obj)
	{
		ClassK other = (ClassK) obj;
		if (id == other.id)
		{
			return ClassJ.getId() == other.ClassJ.getId();
		}

		return false;
	}

	public ClassJ getClassJ()
	{
		return ClassJ;
	}

	public void setClassJ(ClassJ classJ)
	{
		ClassJ = classJ;
	}

	public int getId()
	{
		return id;
	}
	
}
