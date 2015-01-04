package com.accela.AuthorityCenter.authorityBase;

/**
 * 
 * ����ӿڱ�TypedAuthorityBaseʵ�֣���ΪTypedAuthorityBase
 * �ķ��ʽӿ�ͨ������ӿڷ���TypedAuthorityBaseʱ������ȫȨ��
 * ����Ȩ�ޣ��ȿ��Կ��ֿ����޸�
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
