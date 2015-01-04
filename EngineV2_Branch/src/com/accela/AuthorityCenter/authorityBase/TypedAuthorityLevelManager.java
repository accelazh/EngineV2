package com.accela.AuthorityCenter.authorityBase;

import java.util.*;

/**
 * 
 * AbstractLevelTank的实现。
 * 
 * 权限等级(authority level, 常简写为level)的存储和管理器，
 * 支持多线程。这个类用来存储和管理各个权限等级以及与权限等
 * 级对应的密码。
 * 
 * 每个权限等级都具有其对应的名字TLevelName和密码TPassword，
 * 互相依靠TLevelName唯一地区分开。你可以逐级地加入和删除各
 * 个权限等级，以及核对密码。
 * 
 * TLevelName用来指明权限等级的名字，但实际上用TLevelName已
 * 经能够把权限等级唯一地区分开。权限等级的名字就可以代表权限
 * 等级，因此这里只需要权限等级的名字就够了。而权限等级这个对
 * 象在这个类里都没有出现过。
 * 
 * 这个类不维护密码安全，因此你可以看到一些直接暴露了密码的方
 * 法。它的使用者（不单指继承者）应该决定哪些方法可以暴露给外
 * 部，那些方法只能留给内部配置来用。
 * 
 * 这个类提供LevelTank的一些帮助和方便因此用Abstract开头
 *
 * @param TLevelName 权限等级的名字的类型，权限等级依靠它唯一
 * 地区分开。注意它必须具有合适的equals、hashCode和comareTo方法
 * @param <TPassword> 密码的类型，注意它必须具有合适的equals方法
 * 
 * ==========================================================
 * 注意：
 * 1、TLevelName必须具有合适的equals、hashCode和comareTo方法
 * 2、TPassword必须具有合适的equals方法
 * ==========================================================
 *
 */
public class TypedAuthorityLevelManager<TLevelName, TPassword> extends AbstractTypedAuthorityLevelManager<TLevelName, TPassword>
{
	private List<TLevelName> levelList=new LinkedList<TLevelName>();
	private TypedPasswordManager<TLevelName, TPassword> passwordTank;
	
	/**
	 * @param passwordTank 指定LevelTank使用什么PasswordTank
	 */
	public TypedAuthorityLevelManager(TypedPasswordManager<TLevelName, TPassword> passwordTank)
	{
		if(null==passwordTank)
		{
			throw new NullPointerException("passwordTank should not be null");
		}
		try
		{
			if (!passwordTank.isEmpty())
			{
				throw new IllegalArgumentException("passwordTank should be empty");
			}
		} catch (Exception ex)
		{
			throw new IllegalArgumentException("Something is wrong with passwordTank", ex);
		}
		
		this.passwordTank=passwordTank;
	}
	
	@Override
	protected void addHighestLevelImpl(TLevelName levelName, TPassword password) throws Exception
	{
		levelList.add(levelName);
		passwordTank.put(levelName, password);
	}
	@Override
	protected boolean containsLevelImpl(TLevelName levelName)
	{
		return levelList.contains(levelName);
	}
	
	@Override
	protected List<TLevelName> getAllLevelsImpl()
	{
		LinkedList<TLevelName> result=null;
		try
		{
			result=new LinkedList<TLevelName>();
			result.addAll(levelList);
		}catch(ClassCastException ex)
		{
			ex.printStackTrace();    //这个异常理论上不可能发生
			assert(false);
		}
		
		return result;
	}
	@Override
	protected TPassword getPasswordImpl(TLevelName levelName) throws Exception
	{
		return passwordTank.get(levelName);
	}
	@Override
	protected boolean isAHigherThanBImpl(TLevelName levelNameA,
			TLevelName levelNameB)
	{
		return levelList.indexOf(levelNameA)>levelList.indexOf(levelNameB);
	}
	@Override
	protected void removeLevelImpl(TLevelName levelName) throws Exception
	{
		levelList.remove(levelName);
		passwordTank.remove(levelName);
	}
	
	@Override
	protected boolean verifyImpl(TLevelName levelName, TPassword password) throws Exception
	{
		return passwordTank.verify(levelName, password);
	}

	@Override
	protected TPassword changePasswordImpl(TLevelName levelName,
			TPassword password) throws Exception
	{
		return passwordTank.put(levelName, password);
	}

	@Override
	protected int getSizeImpl()
	{
		return levelList.size();
	}
	
	

}
