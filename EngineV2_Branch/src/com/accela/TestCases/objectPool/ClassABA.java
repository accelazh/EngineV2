package com.accela.TestCases.objectPool;

public class ClassABA extends ClassAB
{
	private ClassA classA=new ClassA();
	private Object obj=new Object();
	
	private long l=1000;
	private byte b=8;
	
	public ClassA getClassA()
	{
		return classA;
	}
	public Object getObj()
	{
		return obj;
	}
	public long getL()
	{
		return l;
	}
	public byte getB()
	{
		return b;
	}

}
