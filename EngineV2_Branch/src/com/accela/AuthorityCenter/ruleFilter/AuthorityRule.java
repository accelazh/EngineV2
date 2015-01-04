package com.accela.AuthorityCenter.ruleFilter;

import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseViewer;
import com.accela.AuthorityCenter.shared.CommandWithAuthority;

/**
 * 权限规则（支持多线程）
 * 权限规则规定了什么样的命令因该被过滤掉。
 * 
 * //Inheritance needed
 */
public abstract class AuthorityRule
{
	/**
	 * 检查传入的命令，返回是否应该过滤掉这条命令。
	 * 被过滤掉的命令不应该被执行。
	 * 
	 * @param command 被检查的命令
	 * @param authorityBaseViewer 对权限资料库AuthorityBase的访问接口
	 * @return true如果应该过滤掉这条命令
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
	 * 检查传入的命令，返回是否应该过滤掉这条命令。被过滤掉的命令不应该被执行。
	 * 这个方法是shouldIntercept(Command command, IAuthorityBaseViewer authorityBaseViewer)
	 * 方法的实现。
	 * 
	 * @param command 被检查的命令
	 * @param authorityBaseViewer 对权限资料库AuthorityBase的访问接口
	 * @return true如果应该过滤掉这条命令
	 */
	protected abstract boolean shouldFilterImpl(CommandWithAuthority command,
			IAuthorityBaseViewer authorityBaseViewer)
	throws Exception;

}
