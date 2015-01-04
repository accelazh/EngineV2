package com.accela.AuthorityCenter.ruleFilter;

import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseViewer;
import com.accela.AuthorityCenter.shared.CommandWithAuthority;

/**
 * 这个接口被AuthorityFilter实现，作为AuthorityFilter
 * 的访问接口通过这个接口访问AuthorityFilter时，只能使用
 * 它来过滤命令，而不能修改它。换句话说就是只能看而不能修
 * 改，所以叫Viewer。
 */
public interface IAuthorityFilterer
{
	public boolean shouldFilter(CommandWithAuthority command, IAuthorityBaseViewer authorityBaseViewer) throws AuthorityFilteringException;
}
