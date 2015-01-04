package com.accela.TestCases.connectionCenterUnionHPObjectStreams;

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
	
	private void init()
	{
		classC=new ClassC(classA);
		strBuf=new ClassG(Math.random()+"");
		
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
