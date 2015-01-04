package com.accela.TestCases.authorityCenter;

public class TypeClassForTestC extends SuperTypeClassForTest
implements Comparable<TypeClassForTestC>
{

	public TypeClassForTestC(String value)
	{
		super(value);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof TypeClassForTestC))
		{
			return false;
		}
		
		return compareTo((TypeClassForTestC)obj)==0;
	}

	@Override
	public int hashCode()
	{
		return getValue().hashCode();
	}

	@Override
	public int compareTo(TypeClassForTestC o)
	{
		if(null==o)
		{
			return 1;
		}
		return getValue().compareTo(o.getValue());
	}

}
