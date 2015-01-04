package com.accela.TestCases.objectStreams;

import java.util.Random;

public class ClassA
{
	private ClassA()
	{
		randomlize();
	}
	
	public ClassA(int abc)
	{
		this();
	}
	
	private boolean bool;
	private byte b;
	private char c;
	private double d;
	
	private float f;
	private int i;
	private long l;
	private short s;
	
	private String str;
	
	private ClassB classB;
	
	private ClassC classC;
	
	private final String STR="go to hell!";
	
	private final static String STR2="go to hell, too!!";
	
	private static String str3="go to hell, either!!";
	
	private final Object obj1=new Object();
	private final static Object obj2=new Object();
	private static Object obj3=new Object();
	
	public void randomlize()
	{
		Random rand=new Random();
		
		bool=rand.nextBoolean();
		b=(byte)rand.nextInt();
		c=(char)rand.nextInt();
		d=rand.nextDouble();
		
		f=rand.nextFloat();
		i=rand.nextInt();
		l=rand.nextLong();
		s=(short)rand.nextInt();
		
		str=""+rand.nextGaussian()+rand.nextGaussian()+rand.nextGaussian();
		
		classB=new ClassB(5);
		classB.randomlize();
		
		classC=new ClassC();
		classC.randomlize();
		
	}
	
	@SuppressWarnings("static-access")
	public boolean equals(Object obj)
	{
		ClassA other=(ClassA)obj;
		
		if(bool==other.bool
		&&b==other.b
		&&c==other.c
		&&d==other.d
		&&f==other.f
		&&i==other.i
		&&l==other.l
		&&s==other.s
		&&str.equals(other.str)
		&&classC.equals(other.classC)
		&&classB.equals(other.classB)
		&&STR.equals(other.STR)
		&&STR2.equals(other.STR2)
		&&STR2.equals(ClassA.STR2)
		&&str3.equals(other.str3)
		&&str3.equals(ClassA.str3)
		&&obj1!=null
		&&other.obj1!=null
		&&obj2!=null
		&&other.obj2!=null
		&&obj3!=null
		&&other.obj3!=null
		//&&obj1==other.obj1
		&&obj2==other.obj2
		&&obj3==other.obj3)
		{
			return true;
		}
		
		return false;
	}

}
