package com.accela.AuthorityCenter.ruleFilter;

import java.util.*;

import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseViewer;
import com.accela.AuthorityCenter.shared.CommandWithAuthority;

/**
 * 权限过滤器（支持多线程）。
 * 
 * 权限过滤器中装有个许多AuthorityRule，它们
 * 代表过滤命令的规则。权限过滤器使用AuthorityRule
 * 来过滤命令。
 *
 */
public class AuthorityFilter 
implements IAuthorityFilterer, IAuthorityFilterConfigurer
{
	/**
	 * 装载AuthorityRule的容器
	 */
	private List<AuthorityRule> ruleHolder=new LinkedList<AuthorityRule>();
    
	/**
	 * 新建的时候，会自动加入AuthorityVerificationRule这个权限规则。
	 * @see AuthorityVerificationRule
	 */
	public AuthorityFilter()
	{
		addAuthorityRule(new AuthorityVerificationRule());
	}
	
	/**
	 * 加入一个AuthorityRule。这就相当于加入了一条对命令的过滤规则，
	 * 凡是不满足这条规则的命令都将被过滤掉。同一个权限规则只能被加入
	 * 一次，重复加入只相当于加入了一次。
	 * 
	 * @param rule 被加入的权限规则
	 */
	public synchronized void addAuthorityRule(AuthorityRule rule)
	{
		if(null==rule)
		{
			throw new NullPointerException("rule should not be null");
		}
		
	    if(!ruleHolder.contains(rule))
	    {
	    	ruleHolder.add(rule);
	    }
	}
	
	/**
	 * 删除某个AuthorityRule
	 * @param rule 要删除的AuthorityRule
	 * @return AuthorityFilter是否包含rule
	 */
	public synchronized boolean removeAuthorityRule(AuthorityRule rule)
	{
		if(null==rule)
		{
			throw new NullPointerException("rule should not be null");
		}
		
		return ruleHolder.remove(rule);
	}
	
	/**
	 * 删除所有的AuthorityRule
	 */
	public synchronized void removeAllAuthorityRules()
	{
		ruleHolder.clear();
	}
	
	/**
	 * 检查传入的命令，返回是否应该过滤掉这条命令。
	 * 被过滤掉的命令不应该被执行。
	 * 
	 * AuthorityFilter会使用已经加入的所有AuthorityRule对command进行检查，
	 * 只要有一个AuthorityRule的shouldFilter方法返回true，就会返回true。
	 * 也就是过滤掉该命令。只有所有的AuthorityRule的shouldFilter方法返回
	 * false时，才会返回false，也就是允许该命令通过。
	 * 
	 * @param command 被检查的命令
	 * @param authorityBaseViewer 对权限资料库AuthorityBase的访问接口
	 * @return true如果应该过滤掉这条命令
	 * @throws AuthorityFilteringException 当过滤命令的时候，如果发生异常，则会记录异常
	 * 并继续调用其它AuthorityRule来过滤。如果有任何一个AuthorityRule返回true，则不
	 * 抛出异常并返回true，如果所有的AuthorityRule都返回false，才抛出异常
	 * AuthorityFilteringException。过滤命令的时候发生的异常会被记录在
	 * AuthorityFilteringException中。
	 */
	public synchronized boolean shouldFilter(CommandWithAuthority command, IAuthorityBaseViewer authorityBaseViewer) throws AuthorityFilteringException
	{
		if(null==command)
		{
			throw new NullPointerException("command should not be null");
		}
		if(null==authorityBaseViewer)
		{
			throw new NullPointerException("authorityBaseViewer should not be null");
		}
		
		boolean hasException=false;
		List<Throwable> exceptionList=new LinkedList<Throwable>();
		
		for(AuthorityRule rule : ruleHolder)
		{
			assert(rule!=null);
			try
			{
				if(rule.shouldFilter(command, authorityBaseViewer))
				{
					return true;
				}
			} catch (Exception ex)
			{
				hasException=true;
				exceptionList.add(ex);
			}
		}
		
		if(hasException)
		{
			throw new AuthorityFilteringException(exceptionList);
		}
		
		return false;
		
	}

}
