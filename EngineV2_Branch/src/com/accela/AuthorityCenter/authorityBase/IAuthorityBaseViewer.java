package com.accela.AuthorityCenter.authorityBase;

import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * 这个接口被AuthorityBase实现，作为AuthorityBase
 * 的访问接口通过这个接口访问AuthorityBase时，只能
 * 查看而不能修改
 */
public interface IAuthorityBaseViewer 
extends ITypedAuthorityBaseViewer<CommanderIDWithAuthority, String, String, String>
{

}
