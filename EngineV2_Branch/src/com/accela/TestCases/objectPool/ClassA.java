package com.accela.TestCases.objectPool;

public class ClassA
{
	private int a=10;
	private ClassB classAA=new ClassB();
	private ClassC classAB=new ClassC();
	
	private final String STR="go to hell!";
	
	private final String STR2="go to hell, too!!";
	
	private static String str3="go to hell, either!!";
	
	private final Object obj1=new Object();
	private final static Object obj2=new Object();
	private static Object obj3=new Object();
	
	public int getA()
	{
		return a;
	}
	public ClassB getClassAA()
	{
		return classAA;
	}
	public ClassC getClassAB()
	{
		return classAB;
	}
	public String getSTR()
	{
		return STR;
	}
	public Object getObj1()
	{
		return obj1;
	}
	public static Object getObj2()
	{
		return obj2;
	}
	public static Object getObj3()
	{
		return obj3;
	}
	public static void setObj3(Object obj3)
	{
		ClassA.obj3 = obj3;
	}
	public String getSTR2()
	{
		return STR2;
	}
	public static String getStr3()
	{
		return str3;
	}

}
