package com.accela.TestCases.objectStreams;

public class ClassRightDown
{
	private int id;
	
	public ClassRightDown()
	{
		id=(int)(Math.random()*100);
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}
	
	private ClassRightUp classRightUp;
	private ClassLeftDown classLeftDown;
	
	private ClassCenter classCenter;

	public ClassRightUp getClassRightUp()
	{
		return classRightUp;
	}

	public void setClassRightUp(ClassRightUp classRightUp)
	{
		this.classRightUp = classRightUp;
	}

	public ClassLeftDown getClassLeftDown()
	{
		return classLeftDown;
	}

	public void setClassLeftDown(ClassLeftDown classLeftDown)
	{
		this.classLeftDown = classLeftDown;
	}

	public ClassCenter getClassCenter()
	{
		return classCenter;
	}

	public void setClassCenter(ClassCenter classCenter)
	{
		this.classCenter = classCenter;
	}
	
	public boolean equals(Object obj)
	{
		ClassRightDown other=(ClassRightDown)obj;
		
		return id==other.id;
	}
}
