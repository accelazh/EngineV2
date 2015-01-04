package com.accela.TestCases.reflectionSupport;

public class ClassG
{
	private char[] cs;
	
	public ClassG(String s)
	{
		assert(s!=null);
		cs=s.toCharArray();
		ClassAB.addToSet(cs);
	}

	public ClassG(String s, boolean b)
	{
		assert(s!=null);
		cs=s.toCharArray();
	}

}
