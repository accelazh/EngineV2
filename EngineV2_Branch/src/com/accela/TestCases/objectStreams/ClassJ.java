package com.accela.TestCases.objectStreams;

import java.util.*;

public class ClassJ
{
	private int id;

	private List<ClassK> classKs = new LinkedList<ClassK>();

	public ClassJ()
	{
		id = (int) (Math.random() * 100);
	}

	public boolean equals(Object obj)
	{
		ClassJ other = (ClassJ) obj;
		if (!(id == other.id))
		{
			return false;
		}

		for (ClassK k : classKs)
		{
			if (null == k)
			{
				return false;
			}
		}

		for (ClassK k : other.classKs)
		{
			if (null == k)
			{
				return false;
			}
		}

		if (!classKs.equals(other.classKs))
		{
			return false;
		}

		for (int i = 0; i < classKs.size(); i++)
		{
			if (classKs.get(i) == other.classKs.get(i))
			{
				return false;
			}
		}

		for (ClassK k : classKs)
		{
			if (k.getClassJ() != this)
			{
				return false;
			}
		}

		for (ClassK k : other.classKs)
		{
			if (k.getClassJ() != other)
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

	public void addClassK(ClassK k)
	{
		if (null == k)
		{
			throw new NullPointerException("k should not be null");
		}

		classKs.add(k);
		k.setClassJ(this);
	}

	public List<ClassK> getClassKs()
	{
		return classKs;
	}

}
