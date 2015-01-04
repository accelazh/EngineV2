package com.accela.TestCases.objectStreams;

public class ClassCenter
{
	private int id;
	
	public ClassCenter()
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
	private ClassRightUp classRightUp;
	private ClassLeftDown classLeftDown;
	private ClassRightDown classRightDown;

	public ClassLeftUp getClassLeftUp()
	{
		return classLeftUp;
	}

	public void setClassLeftUp(ClassLeftUp classLeftUp)
	{
		this.classLeftUp = classLeftUp;
	}

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

	public ClassRightDown getClassRightDown()
	{
		return classRightDown;
	}

	public void setClassRightDown(ClassRightDown classRightDown)
	{
		this.classRightDown = classRightDown;
	}
	
	public boolean equals(Object obj)
	{
		ClassCenter other=(ClassCenter)obj;
		
		return id==other.id;
	}

}
