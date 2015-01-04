package com.accela.TestCases.reflectionSupport;

public class ClassC
{
	private ClassA classA;
	
	private ClassG strBuf;
	
	public ClassC(ClassA classA)
	{
		this.classA=classA;
		init();
	}

	public ClassC(ClassA classA, boolean b)
	{
		this.classA=classA;
		init(false);
	}

	private void init(boolean b)
	{
		strBuf=new ClassG(Math.random()+"", false);
	}

	private void init()
	{
		strBuf=new ClassG(Math.random()+"");
		
		ClassAB.addToSet(strBuf);
	}

	public ClassA getClassA()
	{
		return classA;
	}

	public void setClassA(ClassA classA)
	{
		this.classA = classA;
	}

	public ClassG getStrBuf()
	{
		return strBuf;
	}

	public void setStrBuf(ClassG strBuf)
	{
		this.strBuf = strBuf;
	}
}
