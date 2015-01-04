package com.accela.TestCases.connectionCenterUnionHPObjectStreams;

import java.util.*;

public class ClassA
{
	private LinkedList<ClassB> listB=new LinkedList<ClassB>();
	
	private Map<Integer, ClassB> mapB=new HashMap<Integer, ClassB>();
	
	private ClassB classB;
	
	private int i;
	
	private boolean bool;
	
	private double d;
	
	private float f;
	
	private long l;
	
	private char c;
	
	private short s;
	
	private byte b;
	
	private Integer iWrap;
	
	private Boolean boolWrap;
	
	private Double dWrap;
	
	private Float fWrap;
	
	private Long lWrap;
	
	private Character cWrap;
	
	private Short sWrap;
	
	private Byte bWrap;
	
	private String str;
	
	public static enum ABC{
		A,B,C
	};
	
	private ABC abc;
	
	private static ClassB classBStatic;
	
	private static final StringBuffer strBufStaticFinal=new StringBuffer(Math.random()+"");
	
	private final String strFinal=new String(""+Math.random());
	
	public ClassA()
	{
		init();
	}
	
	private void init()
	{
		Random rand=new Random();
		
		final int SIZE=40;
		
		for(int i=0;i<SIZE;i++)
		{
			ClassB b=new ClassB(this);
			listB.add(b);
		}

		for(int i=0;i<SIZE;i++)
		{
			Integer key=rand.nextInt();
			ClassB val=new ClassB(this);
			mapB.put(key, val);
		}
		classB=new ClassB(this);
		
		i=rand.nextInt();
		bool=rand.nextBoolean();
		d=rand.nextDouble();
		f=rand.nextFloat();
		l=rand.nextLong();
		c=(char)rand.nextInt();
		s=(short)rand.nextInt();
		b=(byte)rand.nextInt();
		
		iWrap=rand.nextInt();
		boolWrap=rand.nextBoolean();
		dWrap=rand.nextDouble();
		fWrap=rand.nextFloat();
		lWrap=rand.nextLong();
		cWrap=(char)rand.nextInt();
		sWrap=(short)rand.nextInt();
		bWrap=(byte)rand.nextInt();
		
		str=rand.nextGaussian()+"";
		
		abc=ABC.values()[rand.nextInt(ABC.values().length)];
	}
	
	public boolean checkStaticAndFinalFields()
	{
		if(null==classBStatic)
		{
			return false;
		}
		if(null==strBufStaticFinal)
		{
			return false;
		}
		if(null==strFinal)
		{
			return false;
		}
		
		return true;
	}

	public LinkedList<ClassB> getListB()
	{
		return listB;
	}

	public void setListB(LinkedList<ClassB> listB)
	{
		this.listB = listB;
	}

	public Map<Integer, ClassB> getMapB()
	{
		return mapB;
	}

	public void setMapB(Map<Integer, ClassB> mapB)
	{
		this.mapB = mapB;
	}

	public ClassB getClassB()
	{
		return classB;
	}

	public void setClassB(ClassB classB)
	{
		this.classB = classB;
	}

	public int getI()
	{
		return i;
	}

	public void setI(int i)
	{
		this.i = i;
	}

	public boolean isBool()
	{
		return bool;
	}

	public void setBool(boolean bool)
	{
		this.bool = bool;
	}

	public double getD()
	{
		return d;
	}

	public void setD(double d)
	{
		this.d = d;
	}

	public float getF()
	{
		return f;
	}

	public void setF(float f)
	{
		this.f = f;
	}

	public long getL()
	{
		return l;
	}

	public void setL(long l)
	{
		this.l = l;
	}

	public char getC()
	{
		return c;
	}

	public void setC(char c)
	{
		this.c = c;
	}

	public short getS()
	{
		return s;
	}

	public void setS(short s)
	{
		this.s = s;
	}

	public byte getB()
	{
		return b;
	}

	public void setB(byte b)
	{
		this.b = b;
	}

	public Integer getIWrap()
	{
		return iWrap;
	}

	public void setIWrap(Integer wrap)
	{
		iWrap = wrap;
	}

	public Boolean getBoolWrap()
	{
		return boolWrap;
	}

	public void setBoolWrap(Boolean boolWrap)
	{
		this.boolWrap = boolWrap;
	}

	public Double getDWrap()
	{
		return dWrap;
	}

	public void setDWrap(Double wrap)
	{
		dWrap = wrap;
	}

	public Float getFWrap()
	{
		return fWrap;
	}

	public void setFWrap(Float wrap)
	{
		fWrap = wrap;
	}

	public Long getLWrap()
	{
		return lWrap;
	}

	public void setLWrap(Long wrap)
	{
		lWrap = wrap;
	}

	public Character getCWrap()
	{
		return cWrap;
	}

	public void setCWrap(Character wrap)
	{
		cWrap = wrap;
	}

	public Short getSWrap()
	{
		return sWrap;
	}

	public void setSWrap(Short wrap)
	{
		sWrap = wrap;
	}

	public Byte getBWrap()
	{
		return bWrap;
	}

	public void setBWrap(Byte wrap)
	{
		bWrap = wrap;
	}

	public String getStr()
	{
		return str;
	}

	public void setStr(String str)
	{
		this.str = str;
	}

	public ABC getAbc()
	{
		return abc;
	}

	public void setAbc(ABC abc)
	{
		this.abc = abc;
	}

	public static ClassB getClassBStatic()
	{
		return classBStatic;
	}

	public static void setClassBStatic(ClassB classBStatic)
	{
		ClassA.classBStatic = classBStatic;
	}

	public static StringBuffer getStrBufStaticFinal()
	{
		return strBufStaticFinal;
	}

	public String getStrFinal()
	{
		return strFinal;
	}

	public boolean hasNotBeCleaned()
	{
		if(null==iWrap)
		{
			return false;
		}
		if(null==boolWrap)
		{
			return false;
		}
		if(null==dWrap)
		{
			return false;
		}
		if(null==fWrap)
		{
			return false;
		}
		if(null==lWrap)
		{
			return false;
		}
		if(null==cWrap)
		{
			return false;
		}
		if(null==sWrap)
		{
			return false;
		}
		if(null==bWrap)
		{
			return false;
		}
		if(null==str)
		{
			return false;
		}
		
		return true;
	}
	

}
