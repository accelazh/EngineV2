package com.accela.AuthorityCenter.authorityBase;

/**
 * 
 * 这个接口被TypedAuthorityBase实现，作为TypedAuthorityBase
 * 的访问接口通过这个接口访问TypedAuthorityBase时，具有全权的
 * 访问权限，既可以看又可以修改
 *
 */
public interface ITypedAuthorityBaseConfigurer<TCommanderID, TCommandHead, TLevelName, TPassword>
extends ITypedAuthorityBaseViewer<TCommanderID, TCommandHead, TLevelName, TPassword>
{
	public boolean addHighestLevel(TLevelName levelName, TPassword password) throws AuthorityBaseOperatingException;
	
	public TLevelName removeHighestLevel() throws AuthorityBaseOperatingException;
	
	public TPassword changeLevelPassword(TLevelName levelName, TPassword password) throws AuthorityBaseOperatingException;
	
	public TLevelName setCommandHeadAuthority(TCommandHead commandHead, TLevelName levelName) throws AuthorityBaseOperatingException;
	
	public TLevelName setCommanderAuthority(TCommanderID commanderID, TLevelName levelName) throws AuthorityBaseOperatingException;
	
}
