package com.accela.TestCases.objectStreams;

import java.util.Random;

public class ClassAB extends ClassA
{
	public ClassAB()
	{
		super(1);
		randomlize2();
	}

	public ClassAB(int abc)
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
	
	public void randomlize2()
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
	
	public boolean equals(Object obj)
	{
		ClassAB other=(ClassAB)obj;
		
		if(super.equals(obj)
		&&bool==other.bool
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
