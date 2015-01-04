package com.accela.TestCases.objectStreams;

public class ClassUseless
{
	public boolean equals(Object obj)
	{
		if(!(obj instanceof ClassUseless))
		{
			return false;
		}
		
		return true;
	}

}
