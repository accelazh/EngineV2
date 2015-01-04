package com.accela.TestCases.authorityCenter;

public class SuperTypeClassForTest
{
	private String value;
	
	public SuperTypeClassForTest(String value)
	{
		if(null==value)
		{
			throw new NullPointerException("value should not be null");
		}
		
		this.value=value;
	}

	public String getValue()
	{
		return value;
	}

}
