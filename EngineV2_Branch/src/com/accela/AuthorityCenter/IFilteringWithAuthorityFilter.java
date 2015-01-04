package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.shared.CommandWithAuthority;

/**
 * 
 * 这个接口由AuthorityCenter实现，用来访问AuthorityCenter
 * 的"用权限过滤器过滤命令"的功能
 *
 */
public interface IFilteringWithAuthorityFilter
extends IAuthorityCenterFunctionInterface
{
	/**
	 * 检查传入的命令，返回是否应该过滤掉这条命令。
	 * 被过滤掉的命令不应该被执行。
	 * 
	 * AuthorityFilter会使用已经加入的所有AuthorityRule对command进行检查，
	 * 只要有一个AuthorityRule的shouldFilter方法返回true，就会返回true。
	 * 也就是过滤掉该命令。只有所有的AuthorityRule的shouldFilter方法返回
	 * false时，才会返回false，也就是允许该命令通过。
	 * 
	 * @param command 被检查的命令
	 * @param authorityBaseViewer 对权限资料库AuthorityBase的访问接口
	 * @return true如果应该过滤掉这条命令
	 * @throws AuthorityCenterOperatingException 当过滤命令的时候，如果发生异常，则会记录异常
	 * 并继续调用其它AuthorityRule来过滤。如果有任何一个AuthorityRule返回true，则不
	 * 抛出异常并返回true，如果所有的AuthorityRule都返回false，才抛出异常。过滤命令的时候发生
	 * 的异常会被记录在AuthorityFilteringException中。而AuthorityFilteringException会被记录在
	 * 抛出的异常AuthorityCenterOperatingException中。
	 */
	public boolean shouldFilter(CommandWithAuthority command) throws AuthorityCenterOperatingException;
}
