package com.accela.TestCases.objectStreams;

import java.util.Random;

public class ClassC
{
	private boolean bool;
	private byte b;
	private char c;
	private double d;
	
	private float f;
	private int i;
	private long l;
	private short s;
	
	public enum BAD{
		A,
		B,
		C,
	}
	
	private BAD bad=BAD.A;
	
	public ClassC(double abc)
	{
		this();
	}
	
	public ClassC()
	{
		randomlize();
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
		
	}
	
	public boolean equals(Object obj)
	{
		ClassC other=(ClassC)obj;
		
		if(bool==other.bool
		&&b==other.b
		&&c==other.c
		&&d==other.d
		&&f==other.f
		&&i==other.i
		&&l==other.l
		&&s==other.s
		&&bad==other.bad)
		{
			return true;
		}
		
		return false;
	}

}
