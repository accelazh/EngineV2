package com.accela.TestCases.objectStreams;

import java.util.Random;

public class ClassD
{
	private boolean bool;
	private static byte b;
	private char c;
	private static double d;
	
	private float f;
	private int i;
	private static long l;
	private short s;
	
	private String str;
	
	private static ClassB classB;
	
	private ClassC classC;
	
	private ClassD()
	{
		randomlize();
	}
	
	public ClassD(String abc)
	{
		this();
		abc.trim();
	}
	
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
		ClassD other=(ClassD)obj;
		
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
		&&classB.equals(other.classB))
		{
			return true;
		}
		
		return false;
	}

}
