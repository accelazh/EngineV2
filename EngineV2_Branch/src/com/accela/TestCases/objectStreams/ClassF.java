package com.accela.TestCases.objectStreams;

public class ClassF
{
	private int id;

	private ClassE classE;

	public ClassF()
	{
		id = (int) (Math.random() * 100);
	}

	public boolean equals(Object obj)
	{
		ClassF other = (ClassF) obj;
		if (id == other.id)
		{
			return classE.getId() == other.getClassE().getId();
		}

		return false;
	}

	public ClassE getClassE()
	{
		return classE;
	}

	public void setClassE(ClassE classE)
	{
		this.classE = classE;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

}
