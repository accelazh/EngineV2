package com.accela.TestCases.reflectionSupport;

public class ClassB
{
	private ClassA classA;

	private ClassC classC;

	private ClassG strBuf;
	
	public ClassB(ClassA classA)
	{
		this.classA=classA;
		init();
	}
	
	public ClassB(ClassA classA, boolean b)
	{
		this.classA=classA;
		init(false);
	}

	private void init(boolean b)
	{
		classC=new ClassC(classA, false);
		strBuf=new ClassG(Math.random()+"", false);
	}

	private void init()
	{
		classC=new ClassC(classA);
		strBuf=new ClassG(Math.random()+"");
		
		ClassAB.addToSet(classC);
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

	public ClassC getClassC()
	{
		return classC;
	}

	public void setClassC(ClassC classC)
	{
		this.classC = classC;
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
