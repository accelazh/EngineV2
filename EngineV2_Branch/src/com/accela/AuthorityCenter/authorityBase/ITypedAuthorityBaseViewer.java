package com.accela.AuthorityCenter.authorityBase;

import java.util.List;

/**
 * 
 * 这个接口被TypedAuthorityBase实现，作为TypedAuthorityBase
 * 的访问接口通过这个接口访问TypedAuthorityBase时，只能查看而
 * 不能修改
 *
 */
public interface ITypedAuthorityBaseViewer<TCommanderID, TCommandHead, TLevelName, TPassword>
{
	public boolean verifyLevelAndPassword(TLevelName levelName, TPassword password) throws AuthorityBaseOperatingException;
	
	public boolean containsLevel(TLevelName levelName) throws AuthorityBaseOperatingException;

	public int compareAuthority(TLevelName levelNameA, TLevelName levelNameB) throws AuthorityBaseOperatingException;
	
	public List<TLevelName> getAllLevels() throws AuthorityBaseOperatingException;
	
	public int getNumOfLevels() throws AuthorityBaseOperatingException;
	
	public TLevelName findRequiredAuthority(TCommandHead commandHead);
	
	public TLevelName findPermittedAuthority(TCommanderID commanderID);
}
