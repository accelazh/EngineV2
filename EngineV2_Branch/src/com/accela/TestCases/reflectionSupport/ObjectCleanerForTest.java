package com.accela.TestCases.reflectionSupport;

import com.accela.ReflectionSupport.ObjectCleaner;

public class ObjectCleanerForTest extends ObjectCleaner
{
	protected long cleanCount=0;
	
	@Override
	protected void onClean(Object object)
	{
		assert(object!=null);
		cleanCount++;
		
		if(object instanceof ClassA)
		{
			assert(((ClassA)object).hasNotBeCleaned());
		}
		
		//System.out.println("cleanCount: "+cleanCount);
	}

}
