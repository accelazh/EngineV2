package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * 
 * 这个接口由AuthorityCenter实现，用来访问AuthorityCenter
 * 的"查看权限数据"的功能
 *
 */
public interface IViewingAuthorityData 
extends IAuthorityCenterFunctionInterface
{
	/**
	 * 验证传入的levelName和传入的password是否匹配。
	 * 匹配是指：
	 * 1、levelName和password都不为null
	 * 2、levelName曾经通过addHighestLevel(TLevelName, TPassword)方法加入过，
	 * 并且没有通过removeHighestLevel()方法删除
	 * 3、通过addHighestLevel(TLevelName, TPassword)或
	 * changeLevelPassword(TLevelName, TPassword)给levelName指定的密码，与传入
	 * 参数的password经过equals方法验证相等。
	 * 
	 * @return 如果匹配则返回true，否则返回false
	 * @throws AuthorityCenterOperatingException 
	 */
    public boolean verifyLevelAndPassword(String levelName, String password) throws AuthorityCenterOperatingException;
    /**
	 * 检查是否加入过指定的权限等级并没有删除，即含有该权限等级，
	 * 不能传入null
	 * @throws AuthorityCenterOperatingException 
	 */
	public boolean containsLevel(String levelName) throws AuthorityCenterOperatingException;
	/**
	 * 比较权限的高低，你既可以传入TLevelName对象来比较
	 * 两个权限等级，又可以传入null来表示没有权限等级。无权限等级和无
	 * 权限等级相等。
	 * 
	 * 返回值和Java的Compare方法的规范相同。
	 * @throws AuthorityCenterOperatingException
	 */
	public int compareAuthority(String levelNameA, String levelNameB) throws AuthorityCenterOperatingException;
	/**
	 * 返回按升序排列的所有权限等级。数组中没有空余位置。
	 * 如果没有权限等级，则返回长度为零的数组而不是null。
	 * 另外，这个类中不会保留返回的数组的引用，因此你可
	 * 以修改这个数组。
	 * @throws AuthorityCenterOperatingException 
	 */
	public String[] getAllLevels() throws AuthorityCenterOperatingException;
	/**
	 * @return 当前AuthorityCenter中存在的权限等级的个数。
	 * （即加入过且没有被删除的权限等级的个数）
	 * @throws AuthorityCenterOperatingException
	 */
	public int getNumOfLevels() throws AuthorityCenterOperatingException;
	/**
	 * 查询传入的commandHead要求什么权限才可以被运行，如果commandHead无权限等级，
	 * 则返回null。
	 * @return commandHead要求的权限等级，如果commandHead无权限等级，则返回null
	 */
	public String findRequiredAuthority(String commandHead);
	/**
	 * 查询传入的commanderID拥有什么样的权限。如果commanderID无权限等级，则返回null。
	 * @return commanderID拥有的权限等级，如果commanderID无权限等级，则返回null
	 */
	public String findPermittedAuthority(CommanderIDWithAuthority commanderID);

}
