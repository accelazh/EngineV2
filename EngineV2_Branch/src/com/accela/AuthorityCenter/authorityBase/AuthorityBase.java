package com.accela.AuthorityCenter.authorityBase;

import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 *  使用CommanderID代替TCommanderID，
 *  使用String代替TCommandHead，
 *  使用String代替TLevelName，
 *  使用String代替TPassword后，
 *  得到的AuthorityBase。
 */
public class AuthorityBase extends TypedAuthorityBase<CommanderIDWithAuthority, String, String, String>
implements IAuthorityBaseViewer, IAuthorityBaseConfigurer
{
	/**
	 * @param passwordManager 指定使用何种PasswordTank来存储和管理密码
	 */
	public AuthorityBase(PasswordManager passwordManager)
	{
		super(passwordManager);
	}
	
}
