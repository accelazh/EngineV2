package com.accela.AuthorityCenter.ruleFilter;

import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseViewer;
import com.accela.AuthorityCenter.shared.CommandWithAuthority;

/**
 * ����ӿڱ�AuthorityFilterʵ�֣���ΪAuthorityFilter
 * �ķ��ʽӿ�ͨ������ӿڷ���AuthorityFilterʱ��ֻ��ʹ��
 * ������������������޸��������仰˵����ֻ�ܿ���������
 * �ģ����Խ�Viewer��
 */
public interface IAuthorityFilterer
{
	public boolean shouldFilter(CommandWithAuthority command, IAuthorityBaseViewer authorityBaseViewer) throws AuthorityFilteringException;
}
