package com.accela.TestCases.reflectionSupport;

public class ClassAA
{
	private ClassA classA;
	
	private ClassAA next;
	
	public ClassAA()
	{
		init();
	}
	
	private void init()
	{
		classA=new ClassA();
		ClassAB.addToSet(classA);
	}

	public ClassAA getNext()
	{
		return next;
	}

	public void setNext(ClassAA next)
	{
		this.next = next;
	}

	public ClassA getClassA()
	{
		return classA;
	}

	public void setClassA(ClassA classA)
	{
		this.classA = classA;
	}

}
