package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * 这个接口由AuthorityCenter实现，用来访问AuthorityCenter
 * 的"配置权限数据"的功能
 *
 */
public interface IConfiguringAuthorityData 
extends IAuthorityCenterFunctionInterface, IViewingAuthorityData
{
	/**
	 * 加入一个新的权限等级作为目前最高的权限等级，实际上这个方法是要求
	 * 使用者从低到高依次加入权限等级。
	 * 如果levelName和过去某个权限等级重复，则这个方法什么也不做，即操
	 * 作失败。
	 * 
	 * @param levelName 新的权限等级
	 * @param password 新的权限等级的密码
	 * @return 是否成功地加入了新的权限等级。
	 * @throws AuthorityCenterOperatingException 
	 */
	public boolean addHighestLevel(String levelName, String password) throws AuthorityCenterOperatingException;
	/**
	 * 删除最高的那个权限等级。所有拥有这个权限等级的TCommanderID和TCommandHead
	 * 都会被下降一个权限等级。但是如果被删除的权限等级就是最低权限等级，则上述
	 * TCommanderID和TCommandHead就会被删除权限等级，即变为无权限等级。
	 * 
	 * 如果没有最高权限等级，即此时还没有任何权限等级，则什么也不做
	 * 
	 * @return 被删除的最高权限等级，如果没有则返回null
	 * @throws AuthorityCenterOperatingException 
	 */
	public String removeHighestLevel() throws AuthorityCenterOperatingException;
	/**
	 * 修改某个权限等级的密码
	 * @throws AuthorityCenterOperatingException 
	 */
	public String changeLevelPassword(String levelName, String password) throws AuthorityCenterOperatingException;
	/**
	 * 重新设定commandHead这种命令的权限。如果levelName！=null，则将其设定为levelName
	 * 相应的权限等级，否则将其设定为无权限等级。
	 * @param commandHead 将要被设定的命令
	 * @param levelName commandHead被设定成的权限等级
	 * @return commandHead此前的权限等级，如果commandHead此前处于无权限等级状态，那么返回null
	 * @throws AuthorityCenterOperatingException 
	 */
	public String setCommandHeadAuthority(String commandHead, String levelName) throws AuthorityCenterOperatingException;
	/**
	 * 重新设定commanderID的权限。如果levelName！=null，则将其设定为levelName
	 * 相应的权限等级，否则将其设定为无权限等级。
	 * @param commanderID 将要被设定的命令
	 * @param levelName commandHead被设定成的权限等级
	 * @return commanderID此前的权限等级，如果commanderID此前处于无权限等级状态，那么返回null
	 * @throws AuthorityCenterOperatingException 
	 */
	public String setCommanderAuthority(CommanderIDWithAuthority commanderID, String levelName) throws AuthorityCenterOperatingException;

}
