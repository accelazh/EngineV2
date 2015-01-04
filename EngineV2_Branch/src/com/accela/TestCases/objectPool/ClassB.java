package com.accela.TestCases.objectPool;

import java.util.LinkedList;

public class ClassB
{
	private int a=10;
	private String s="go to hell!";
	private LinkedList<String> classAA=new LinkedList<String>();
	private ClassC classAB=new ClassC();
	
	public int getA()
	{
		return a;
	}
	public String getS()
	{
		return s;
	}
	public LinkedList<String> getClassAA()
	{
		return classAA;
	}
	public ClassC getClassAB()
	{
		return classAB;
	}

}
