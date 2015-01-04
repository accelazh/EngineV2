package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.shared.CommandWithAuthority;

/**
 * 
 * ����ӿ���AuthorityCenterʵ�֣���������AuthorityCenter
 * ��"��Ȩ�޹�������������"�Ĺ���
 *
 */
public interface IFilteringWithAuthorityFilter
extends IAuthorityCenterFunctionInterface
{
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
	 * @throws AuthorityCenterOperatingException �����������ʱ����������쳣������¼�쳣
	 * ��������������AuthorityRule�����ˡ�������κ�һ��AuthorityRule����true����
	 * �׳��쳣������true��������е�AuthorityRule������false�����׳��쳣�����������ʱ����
	 * ���쳣�ᱻ��¼��AuthorityFilteringException�С���AuthorityFilteringException�ᱻ��¼��
	 * �׳����쳣AuthorityCenterOperatingException�С�
	 */
	public boolean shouldFilter(CommandWithAuthority command) throws AuthorityCenterOperatingException;
}
