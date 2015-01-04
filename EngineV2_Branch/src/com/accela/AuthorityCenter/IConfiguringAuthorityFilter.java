package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.ruleFilter.AuthorityRule;

/**
 * 
 * 这个接口由AuthorityCenter实现，用来访问AuthorityCenter
 * 的"配置权限过滤器"的功能
 *
 */
public interface IConfiguringAuthorityFilter
extends IAuthorityCenterFunctionInterface
{
	/**
	 * 加入一个AuthorityRule。这就相当于加入了一条对命令的过滤规则，
	 * 凡是不满足这条规则的命令都将被过滤掉。同一个权限规则只能被加入
	 * 一次，重复加入只相当于加入了一次。
	 * 
	 * @param rule 被加入的权限规则
	 */
	public void addAuthorityRule(AuthorityRule rule);
	/**
	 * 删除某个AuthorityRule
	 * @param rule 要删除的AuthorityRule
	 * @return AuthorityFilter是否包含rule
	 */
	public boolean removeAuthorityRule(AuthorityRule rule);
	/**
	 * 删除所有的AuthorityRule
	 */
	public void removeAllAuthorityRules();

}
