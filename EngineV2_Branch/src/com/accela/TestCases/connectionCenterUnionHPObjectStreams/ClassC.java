package com.accela.TestCases.connectionCenterUnionHPObjectStreams;

public class ClassC
{
	private ClassA classA;
	
	private ClassG strBuf;
	
	public ClassC(ClassA classA)
	{
		this.classA=classA;
		init();
	}

	private void init()
	{
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

	public ClassG getStrBuf()
	{
		return strBuf;
	}

	public void setStrBuf(ClassG strBuf)
	{
		this.strBuf = strBuf;
	}
}
