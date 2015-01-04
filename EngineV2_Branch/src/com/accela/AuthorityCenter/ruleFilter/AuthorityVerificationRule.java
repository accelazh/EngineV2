package com.accela.AuthorityCenter.ruleFilter;

import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseViewer;
import com.accela.AuthorityCenter.shared.CommandWithAuthority;

/**
 * 
 * 这个规则用于过滤这样的命令：
 * 发令者的权限低于命令的权限。
 * 
 * 这个规则是一个非常普通的规则，所以这个这条规则会
 * 被自动地加入到AuthorityFilter中。
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
