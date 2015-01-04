package com.accela.AuthorityCenter.authorityBase;

import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 *  ʹ��CommanderID����TCommanderID��
 *  ʹ��String����TCommandHead��
 *  ʹ��String����TLevelName��
 *  ʹ��String����TPassword��
 *  �õ���AuthorityBase��
 */
public class AuthorityBase extends TypedAuthorityBase<CommanderIDWithAuthority, String, String, String>
implements IAuthorityBaseViewer, IAuthorityBaseConfigurer
{
	/**
	 * @param passwordManager ָ��ʹ�ú���PasswordTank���洢�͹�������
	 */
	public AuthorityBase(PasswordManager passwordManager)
	{
		super(passwordManager);
	}
	
}
