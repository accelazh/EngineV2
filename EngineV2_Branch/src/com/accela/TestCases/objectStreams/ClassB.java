package com.accela.TestCases.objectStreams;

import java.util.Random;

public class ClassB
{
	private boolean bool;
	private byte b;
	private char c;
	private double d;
	
	private float f;
	private int i;
	private long l;
	private short s;
	
	private StringBuffer strBuffer=new StringBuffer();
	
	private ClassC classC;
	
	private ClassB()
	{
		randomlize();
	}
	
	public ClassB(double abc)
	{
		this();
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
		
		strBuffer=new StringBuffer();
		strBuffer.append(""+rand.nextGaussian()+rand.nextGaussian()+rand.nextGaussian());
		
		classC=new ClassC();
		classC.randomlize();
		
	}
	
	public boolean equals(Object obj)
	{
		ClassB other=(ClassB)obj;
		
		if(bool==other.bool
		&&b==other.b
		&&c==other.c
		&&d==other.d
		&&f==other.f
		&&i==other.i
		&&l==other.l
		&&s==other.s
		&&strBuffer.toString().equals(other.strBuffer.toString())
		&&classC.equals(other.classC))
		{
			return true;
		}
		
		return false;
	}

}
