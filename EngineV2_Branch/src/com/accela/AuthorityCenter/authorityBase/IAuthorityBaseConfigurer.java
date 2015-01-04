package com.accela.AuthorityCenter.authorityBase;

import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * 
 * 这个接口被AuthorityBase实现，作为AuthorityBase
 * 的访问接口通过这个接口访问AuthorityBase时，具有
 * 全权的访问权限，既可以看又可以修改
 *
 */
public interface IAuthorityBaseConfigurer 
extends ITypedAuthorityBaseConfigurer<CommanderIDWithAuthority, String, String, String>
{

}
