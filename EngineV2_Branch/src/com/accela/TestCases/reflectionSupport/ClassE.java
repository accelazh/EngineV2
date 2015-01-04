package com.accela.TestCases.reflectionSupport;

public class ClassE
{
	private int id;
	
	private ClassF classF;
	
	public ClassE()
	{
		id=(int)(Math.random()*100);
	}
	
	public boolean equals(Object obj)
	{
		ClassE other=(ClassE)obj;
		if(id==other.id
				&&classF.equals(other.classF))
		{
			return true;
		}
		
		return false;
	}

	public ClassF getClassF()
	{
		return classF;
	}

	public void setClassF(ClassF classF)
	{
		this.classF = classF;
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
