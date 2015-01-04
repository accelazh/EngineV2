package com.accela.AuthorityCenter.ruleFilter;

/**
 * 这个接口被AuthorityFilter实现，作为AuthorityFilter
 * 的访问接口通过这个接口访问AuthorityFilter时，具有全
 * 权的访问权限
 */
public interface IAuthorityFilterConfigurer
extends IAuthorityFilterer
{
	public void addAuthorityRule(AuthorityRule rule);
	
	public boolean removeAuthorityRule(AuthorityRule rule);
	
	public void removeAllAuthorityRules();

}
