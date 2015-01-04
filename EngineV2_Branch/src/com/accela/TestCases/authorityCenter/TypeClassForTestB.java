package com.accela.TestCases.authorityCenter;

public class TypeClassForTestB extends SuperTypeClassForTest
implements Comparable<TypeClassForTestB>
{

	public TypeClassForTestB(String value)
	{
		super(value);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof TypeClassForTestB))
		{
			return false;
		}
		
		return compareTo((TypeClassForTestB)obj)==0;
	}

	@Override
	public int hashCode()
	{
		return getValue().hashCode();
	}

	@Override
	public int compareTo(TypeClassForTestB o)
	{
		if(null==o)
		{
			return 1;
		}
		return getValue().compareTo(o.getValue());
	}

}
