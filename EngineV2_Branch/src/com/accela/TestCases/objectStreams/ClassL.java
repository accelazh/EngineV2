package com.accela.TestCases.objectStreams;

import java.util.ArrayList;
import java.util.List;

public class ClassL
{
	private int id;

	private List<ClassM> classMs = new ArrayList<ClassM>();

	public ClassL()
	{
		id = (int) (Math.random() * 100);
	}

	public boolean equals(Object obj)
	{
		ClassL other = (ClassL) obj;
		if (!(id == other.id))
		{
			return false;
		}

		if (!classMs.equals(other.classMs))
		{
			return false;
		}

		for (int i = 0; i < classMs.size(); i++)
		{
			if (classMs.get(i) != null && other.classMs.get(i) != null)
			{
				if (classMs.get(i) == other.classMs.get(i))
				{
					return false;
				}
			}
		}

		for (ClassM m : classMs)
		{
			if(null==m)
			{
				continue;
			}
			
			if (m.getClassL() != this)
			{
				return false;
			}
		}

		for (ClassM m : other.classMs)
		{
			if(null==m)
			{
				continue;
			}
			
			if (m.getClassL() != other)
			{
				return false;
			}
		}

		return true;

	}

	public int getId()
	{
		return id;
	}

	public void addClassM(ClassM m)
	{
		classMs.add(m);
		if (m != null)
		{
			m.setClassL(this);
		}
	}

	public List<ClassM> getClassMs()
	{
		return classMs;
	}

}
