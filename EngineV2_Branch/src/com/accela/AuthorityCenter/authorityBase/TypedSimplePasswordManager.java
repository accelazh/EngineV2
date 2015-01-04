package com.accela.AuthorityCenter.authorityBase;

import java.util.*;

/**
 * 
 * �򵥵�����洢����û��ʲô��ȫ��ʩ
 *
 * ==========================================================
 * ע�⣺
 * 1��TName������к��ʵ�equals��hashCode��comareTo����
 * 2��TPassword������к��ʵ�equals����
 * ==========================================================
 */
public class TypedSimplePasswordManager<TName, TPassword> extends TypedPasswordManager<TName, TPassword>
{

	private Map<TName, TPassword> map=new HashMap<TName, TPassword>();

	@Override
	protected TPassword getImpl(TName name)
	{
		return map.get(name);
	}

	@Override
	protected TPassword putImpl(TName name, TPassword password)
	{
		return map.put(name, password);
	}

	@Override
	protected TPassword removeImpl(TName name)
	{
		return map.remove(name);
	}

	@Override
	protected boolean verifyImpl(TName name, TPassword password)
	{
		return map.get(name).equals(password);
	}

	@Override
	protected void clearImpl()
	{
		map.clear();
	}

	@Override
	protected boolean isEmptyImpl()
	{
		return map.isEmpty();
	}

	@Override
	protected int getSizeImpl() throws Exception
	{
		return map.size();
	}

}
