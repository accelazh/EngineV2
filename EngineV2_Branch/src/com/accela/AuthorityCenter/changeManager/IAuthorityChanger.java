package com.accela.AuthorityCenter.changeManager;

import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseConfigurer;
import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * 
 * ��AuthorityChangeManagerʵ�֣����ڷ���AuthorityChangeMananger
 * �Ľӿ�
 *
 */
public interface IAuthorityChanger
{
	public boolean changeLevelPassword(CommanderIDWithAuthority commanderID,
			String levelName, String newPassword,
			IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException;
	
	public boolean heightenSelfAuthority(CommanderIDWithAuthority commanderID,
			String levelName, String password,
			IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException;
	
	public boolean lowerSelfAuthority(CommanderIDWithAuthority commanderID,
			String levelName, IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException;
	
	public boolean setOtherAuthority(CommanderIDWithAuthority selfCommanderID,
			CommanderIDWithAuthority otherCommanderID, String levelName,
			IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException;

}
