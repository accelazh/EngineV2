package com.accela.AuthorityCenter.ruleFilter;

/**
 * ����ӿڱ�AuthorityFilterʵ�֣���ΪAuthorityFilter
 * �ķ��ʽӿ�ͨ������ӿڷ���AuthorityFilterʱ������ȫ
 * Ȩ�ķ���Ȩ��
 */
public interface IAuthorityFilterConfigurer
extends IAuthorityFilterer
{
	public void addAuthorityRule(AuthorityRule rule);
	
	public boolean removeAuthorityRule(AuthorityRule rule);
	
	public void removeAllAuthorityRules();

}
