package com.accela.AuthorityCenter.ruleFilter;

import java.util.*;

import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseViewer;
import com.accela.AuthorityCenter.shared.CommandWithAuthority;

/**
 * Ȩ�޹�������֧�ֶ��̣߳���
 * 
 * Ȩ�޹�������װ�и����AuthorityRule������
 * �����������Ĺ���Ȩ�޹�����ʹ��AuthorityRule
 * ���������
 *
 */
public class AuthorityFilter 
implements IAuthorityFilterer, IAuthorityFilterConfigurer
{
	/**
	 * װ��AuthorityRule������
	 */
	private List<AuthorityRule> ruleHolder=new LinkedList<AuthorityRule>();
    
	/**
	 * �½���ʱ�򣬻��Զ�����AuthorityVerificationRule���Ȩ�޹���
	 * @see AuthorityVerificationRule
	 */
	public AuthorityFilter()
	{
		addAuthorityRule(new AuthorityVerificationRule());
	}
	
	/**
	 * ����һ��AuthorityRule������൱�ڼ�����һ��������Ĺ��˹���
	 * ���ǲ������������������������˵���ͬһ��Ȩ�޹���ֻ�ܱ�����
	 * һ�Σ��ظ�����ֻ�൱�ڼ�����һ�Ρ�
	 * 
	 * @param rule �������Ȩ�޹���
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
	 * ɾ��ĳ��AuthorityRule
	 * @param rule Ҫɾ����AuthorityRule
	 * @return AuthorityFilter�Ƿ����rule
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
	 * ɾ�����е�AuthorityRule
	 */
	public synchronized void removeAllAuthorityRules()
	{
		ruleHolder.clear();
	}
	
	/**
	 * ��鴫�����������Ƿ�Ӧ�ù��˵��������
	 * �����˵������Ӧ�ñ�ִ�С�
	 * 
	 * AuthorityFilter��ʹ���Ѿ����������AuthorityRule��command���м�飬
	 * ֻҪ��һ��AuthorityRule��shouldFilter��������true���ͻ᷵��true��
	 * Ҳ���ǹ��˵������ֻ�����е�AuthorityRule��shouldFilter��������
	 * falseʱ���Ż᷵��false��Ҳ�������������ͨ����
	 * 
	 * @param command ����������
	 * @param authorityBaseViewer ��Ȩ�����Ͽ�AuthorityBase�ķ��ʽӿ�
	 * @return true���Ӧ�ù��˵���������
	 * @throws AuthorityFilteringException �����������ʱ����������쳣������¼�쳣
	 * ��������������AuthorityRule�����ˡ�������κ�һ��AuthorityRule����true����
	 * �׳��쳣������true��������е�AuthorityRule������false�����׳��쳣
	 * AuthorityFilteringException�����������ʱ�������쳣�ᱻ��¼��
	 * AuthorityFilteringException�С�
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
