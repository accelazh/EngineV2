package com.accela.TestCases.objectStreams;

public class ClassLeftDown
{

	private int id;
	
	public ClassLeftDown()
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
	
	private ClassLeftUp classLeftUp;
	private ClassRightDown classRightDown;
	
	private ClassCenter classCenter;

	public ClassLeftUp getClassLeftUp()
	{
		return classLeftUp;
	}

	public void setClassLeftUp(ClassLeftUp classLeftUp)
	{
		this.classLeftUp = classLeftUp;
	}

	public ClassRightDown getClassRightDown()
	{
		return classRightDown;
	}

	public void setClassRightDown(ClassRightDown classRightDown)
	{
		this.classRightDown = classRightDown;
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
		ClassLeftDown other=(ClassLeftDown)obj;
		
		return id==other.id;
	}
}
