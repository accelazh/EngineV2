package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.ruleFilter.AuthorityRule;

/**
 * 
 * ����ӿ���AuthorityCenterʵ�֣���������AuthorityCenter
 * ��"����Ȩ�޹�����"�Ĺ���
 *
 */
public interface IConfiguringAuthorityFilter
extends IAuthorityCenterFunctionInterface
{
	/**
	 * ����һ��AuthorityRule������൱�ڼ�����һ��������Ĺ��˹���
	 * ���ǲ������������������������˵���ͬһ��Ȩ�޹���ֻ�ܱ�����
	 * һ�Σ��ظ�����ֻ�൱�ڼ�����һ�Ρ�
	 * 
	 * @param rule �������Ȩ�޹���
	 */
	public void addAuthorityRule(AuthorityRule rule);
	/**
	 * ɾ��ĳ��AuthorityRule
	 * @param rule Ҫɾ����AuthorityRule
	 * @return AuthorityFilter�Ƿ����rule
	 */
	public boolean removeAuthorityRule(AuthorityRule rule);
	/**
	 * ɾ�����е�AuthorityRule
	 */
	public void removeAllAuthorityRules();

}
