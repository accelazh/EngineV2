package com.accela.TestCases.objectPool;

import com.accela.ObjectPool.IObjectPoolSensitive;

public class ClassD implements IObjectPoolSensitive
{
	private boolean putInvoked=false;
	
	private boolean retrieveInvoked=false;

	public boolean isPutInvoked()
	{
		return putInvoked;
	}

	public boolean isRetrieveInvoked()
	{
		return retrieveInvoked;
	}

	@Override
	public void onPut()
	{
		putInvoked=true;
	}

	@Override
	public void onRetrieve()
	{
		retrieveInvoked=true;
	}
	
	

}
