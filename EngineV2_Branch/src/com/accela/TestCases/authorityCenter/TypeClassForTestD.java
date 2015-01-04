package com.accela.TestCases.authorityCenter;

public class TypeClassForTestD extends SuperTypeClassForTest
implements Comparable<TypeClassForTestD>
{

	public TypeClassForTestD(String value)
	{
		super(value);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof TypeClassForTestD))
		{
			return false;
		}
		
		return compareTo((TypeClassForTestD)obj)==0;
	}

	@Override
	public int hashCode()
	{
		return getValue().hashCode();
	}

	@Override
	public int compareTo(TypeClassForTestD o)
	{
		if(null==o)
		{
			return 1;
		}
		return getValue().compareTo(o.getValue());
	}

}
