package com.accela.TestCases.authorityCenter;

public class TypeClassForTestA extends SuperTypeClassForTest
implements Comparable<TypeClassForTestA>
{

	public TypeClassForTestA(String value)
	{
		super(value);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof TypeClassForTestA))
		{
			return false;
		}
		
		return compareTo((TypeClassForTestA)obj)==0;
	}

	@Override
	public int hashCode()
	{
		return getValue().hashCode();
	}

	@Override
	public int compareTo(TypeClassForTestA o)
	{
		if(null==o)
		{
			return 1;
		}
		return getValue().compareTo(o.getValue());
	}

}
