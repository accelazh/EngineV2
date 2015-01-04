package com.accela.AuthorityCenter.ruleFilter;

import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseViewer;
import com.accela.AuthorityCenter.shared.CommandWithAuthority;

/**
 * 
 * ����������ڹ������������
 * �����ߵ�Ȩ�޵��������Ȩ�ޡ�
 * 
 * ���������һ���ǳ���ͨ�Ĺ�������������������
 * ���Զ��ؼ��뵽AuthorityFilter�С�
 *
 */
public class AuthorityVerificationRule extends AuthorityRule
{
	@Override
	protected boolean shouldFilterImpl(CommandWithAuthority command,
			IAuthorityBaseViewer authorityBaseViewer) throws Exception
	{
		if(authorityBaseViewer.compareAuthority(
				authorityBaseViewer.findPermittedAuthority(command.getCommanderID()),
				authorityBaseViewer.findRequiredAuthority(command.getCommandHead()))
				<0)
		{
			return true;
		}
		
		return false;
	}

}
