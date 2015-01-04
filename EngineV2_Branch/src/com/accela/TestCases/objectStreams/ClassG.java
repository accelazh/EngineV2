package com.accela.TestCases.objectStreams;

public class ClassG
{
	private int id;
	
	private ClassH classH;
	
	public ClassG()
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

	public ClassH getClassH()
	{
		return classH;
	}

	public void setClassH(ClassH classH)
	{
		this.classH = classH;
	}
	
	public boolean equals(Object obj)
	{
		ClassG other=(ClassG)obj;
		
		if(id==other.id
				&&classH.equals(other.classH)
				&&classH.getClassI().getClassG()==this
				&&other.classH.getClassI().getClassG()==other)
		{
			return true;
		}
		
		return false;
	}

}
