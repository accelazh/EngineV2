package com.accela.AuthorityCenter.authorityBase;

import java.util.List;

/**
 * 
 * ����ӿڱ�TypedAuthorityBaseʵ�֣���ΪTypedAuthorityBase
 * �ķ��ʽӿ�ͨ������ӿڷ���TypedAuthorityBaseʱ��ֻ�ܲ鿴��
 * �����޸�
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
