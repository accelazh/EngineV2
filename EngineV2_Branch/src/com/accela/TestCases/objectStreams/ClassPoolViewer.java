package com.accela.TestCases.objectStreams;

public class ClassPoolViewer
{
	private static int createCount=0;
	
	private String s;
	
	public ClassPoolViewer()
	{
		s=Math.random()+"";
		createCount++;
	}
	
	public boolean equals(Object obj)
	{
		ClassPoolViewer other=(ClassPoolViewer)obj;
		if(s.equals(other.s))
		{
			return true;
		}
		
		return false;
	}

	public static int getCreateCount()
	{
		return createCount;
	}
	
	
	

}
