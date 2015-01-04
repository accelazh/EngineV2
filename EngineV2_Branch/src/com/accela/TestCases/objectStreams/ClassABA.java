package com.accela.TestCases.objectStreams;

import java.util.Random;

public class ClassABA extends ClassAB
{
	protected ClassABA()
	{
		super(2);
		randomlize3();
	}
	
	public ClassABA(int abc)
	{
		super(abc);
		randomlize3();
	}
	
	private boolean bool;
	private byte b;
	private char c;
	private double d;
	
	private float f;
	private int i;
	private long l;
	private short s;
	
	private Object nullValue=null;
	
	public void randomlize3()
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
		ClassABA other=(ClassABA)obj;
		
		if(super.equals(obj)
		&&bool==other.bool
		&&b==other.b
		&&c==other.c
		&&d==other.d
		&&f==other.f
		&&i==other.i
		&&l==other.l
		&&s==other.s
		&&nullValue==null
		&&other.nullValue==null)
		{
			return true;
		}
		
		return false;
	}

}
