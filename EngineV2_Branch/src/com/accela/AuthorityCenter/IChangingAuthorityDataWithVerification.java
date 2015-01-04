package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * 
 * 这个接口由AuthorityCenter实现，用来访问AuthorityCenter
 * 的"在需要验证的条件下修改权限数据"的功能
 *
 */
public interface IChangingAuthorityDataWithVerification
extends IAuthorityCenterFunctionInterface
{
	/**
	 * 这个方法用于一个命令发布者commanderID希望更改某个权限等级的密码的时候。
	 * 当且仅当它的权限等级高于该levelName的时候，才能更改密码
	 * 
	 * @return 是否成功更改密码
	 * @throws AuthorityCenterOperatingException
	 */
	public boolean changeLevelPasswordWithVerification(CommanderIDWithAuthority commanderID,
			String levelName, String newPassword)
			throws AuthorityCenterOperatingException;
	/**
	 * 这个方法用于一个命令发布者希望提升自己的权限的时候，它需要给出
	 * 自己的commanderID，自己希望的权限等级levelName和这个权限等级的密 码password。
	 * 
	 * 当且仅当这些条件满足的时候，这个命令发布者才能成功达到目的： 
	 * 1、传入参数都不为null
	 * 2、levelName曾经加入到AuthorityBase中，并且没有被删除 
	 * 3、levelName高于commanderID已有的权限等级
	 * 4、password和levelName对应的密码经equals方法测试相等
	 * 
	 * @return 是否成功提升权限
	 * @throws AuthorityCenterOperatingException
	 */
	public boolean heightenSelfAuthorityWithVerification(CommanderIDWithAuthority commanderID,
			String levelName, String password)
			throws AuthorityCenterOperatingException;
	/**
	 * 这个方法用于一个命令发布者希望降低自己的权限的时候，它需要给出
	 * 自己的TCommanderID即commanderID和自己希望的权限等级levelName。
	 * 当levelName为null时，将删除commanderID的权限等级，即将其置于 无权限等级的状态
	 * 
	 * 当且仅当这些条件满足的时候，这个命令发布者才能成功达到目的： 
	 * 1、传入参数都除了levelName外不为null
	 * 2、levelName曾经加入到AuthorityBase中，并且没有被删除 ；或者levelName==null
	 * 3、levelName低于commanderID已有的权限等级；或者levelName==null时，commanderID
	 * 不是无权限的等级
	 * 
	 * @return 是否成功降低权限等级
	 * @throws AuthorityCenterOperatingException
	 */
	public boolean lowerSelfAuthorityWithVerification(CommanderIDWithAuthority commanderID,
			String levelName)
			throws AuthorityCenterOperatingException;
	/**
	 * 这个方法用于一个命令发布者selfCommanderID，希望改变另一个命令发布者
	 * otherCommanderID的权限的时候。它需要给出自己的TCommanderID即
	 * selfCommanderID，被改变者的TCommanderID即otherCommanderID，以及
	 * 它希望将otherCommanderID改变到的权限等级levelName。如果levelName
	 * 为null，则表示将otherCommanderID变为无权限等级。
	 * 
	 * 当且仅当这些条件满足的时候，这个命令发布者才能成功达到目的： 
	 * 1、传入参数除levelName外都不为null
	 * 2、levelName曾经加入到AuthorityVerifier中，并且没有被删除；或者levelName是null
	 * 3、levelName低于或等于selfCommanderID的权限等级
	 * 4、otherCommanderID的权限等级低于selfCommanderID的权限等级，或者
	 * otherCommanderID没有权限等级但selfCommanderID有
	 * 5、otherCommanderID的权限等级不等于levelName；或者当levelName==null时，
	 * otherCommanderID不是无权限等级
	 * 
	 * @return selfCommanderID命令发布者是否成功达到目的
	 * @throws AuthorityCenterOperatingException
	 */
	public boolean setOtherAuthorityWithVerification(CommanderIDWithAuthority selfCommanderID,
			CommanderIDWithAuthority otherCommanderID, String levelName)
			throws AuthorityCenterOperatingException;

}
