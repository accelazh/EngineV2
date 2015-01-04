package com.accela.TestCases.connectionCenterUnionHPObjectStreams;

public class ClassG
{
	private char[] cs;
	
	public ClassG(String s)
	{
		assert(s!=null);
		cs=s.toCharArray();
	}

	public ClassG(String s, boolean b)
	{
		assert(s!=null);
		cs=s.toCharArray();
	}

	public char[] getCs()
	{
		return cs;
	}

	public void setCs(char[] cs)
	{
		this.cs = cs;
	}

}
