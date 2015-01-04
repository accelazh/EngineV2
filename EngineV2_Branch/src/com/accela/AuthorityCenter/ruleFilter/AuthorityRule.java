package com.accela.AuthorityCenter.ruleFilter;

import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseViewer;
import com.accela.AuthorityCenter.shared.CommandWithAuthority;

/**
 * Ȩ�޹���֧�ֶ��̣߳�
 * Ȩ�޹���涨��ʲô����������ñ����˵���
 * 
 * //Inheritance needed
 */
public abstract class AuthorityRule
{
	/**
	 * ��鴫�����������Ƿ�Ӧ�ù��˵��������
	 * �����˵������Ӧ�ñ�ִ�С�
	 * 
	 * @param command ����������
	 * @param authorityBaseViewer ��Ȩ�����Ͽ�AuthorityBase�ķ��ʽӿ�
	 * @return true���Ӧ�ù��˵���������
	 */
	public synchronized boolean shouldFilter(CommandWithAuthority command,
			IAuthorityBaseViewer authorityBaseViewer)
	throws Exception
	{
		if(null==command)
		{
			throw new NullPointerException("command should not be null");
		}
		if(null==authorityBaseViewer)
		{
			throw new NullPointerException("authorityBaseViewer should not be null");
		}
		
		return shouldFilterImpl(command, authorityBaseViewer);
	}
	
	/**
	 * ��鴫�����������Ƿ�Ӧ�ù��˵�������������˵������Ӧ�ñ�ִ�С�
	 * ���������shouldIntercept(Command command, IAuthorityBaseViewer authorityBaseViewer)
	 * ������ʵ�֡�
	 * 
	 * @param command ����������
	 * @param authorityBaseViewer ��Ȩ�����Ͽ�AuthorityBase�ķ��ʽӿ�
	 * @return true���Ӧ�ù��˵���������
	 */
	protected abstract boolean shouldFilterImpl(CommandWithAuthority command,
			IAuthorityBaseViewer authorityBaseViewer)
	throws Exception;

}
