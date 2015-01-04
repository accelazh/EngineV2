package com.accela.AuthorityCenter.authorityBase;

public class SimplePasswordManager extends PasswordManager
{

	private TypedSimplePasswordManager<String, String> passwordManager=new TypedSimplePasswordManager<String, String>(); 
	
	@Override
	protected void clearImpl() throws Exception
	{
		passwordManager.clear();
	}

	@Override
	protected String getImpl(String name) throws Exception
	{
		return passwordManager.get(name);
	}

	@Override
	protected boolean isEmptyImpl() throws Exception
	{
		return passwordManager.isEmpty();
	}

	@Override
	protected String putImpl(String name, String password) throws Exception
	{
		return passwordManager.put(name, password);
	}

	@Override
	protected String removeImpl(String name) throws Exception
	{
		return passwordManager.remove(name);
	}

	@Override
	protected boolean verifyImpl(String name, String password) throws Exception
	{
		return passwordManager.verify(name, password);
	}

	@Override
	protected int getSizeImpl() throws Exception
	{
		return passwordManager.getSize();
	}

}
