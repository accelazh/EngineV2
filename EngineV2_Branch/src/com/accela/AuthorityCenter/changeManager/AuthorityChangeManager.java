package com.accela.AuthorityCenter.changeManager;

import com.accela.AuthorityCenter.authorityBase.AuthorityBaseOperatingException;
import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseConfigurer;
import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * 
 * 提供加以校验的变更权限的方法
 * 
 */
public class AuthorityChangeManager implements IAuthorityChanger
{

	/**
	 * 这个方法用于一个命令发布者commanderID希望更改某个权限等级的密码的时候。
	 * 当且仅当它的权限等级高于该levelName的时候，才能更改密码
	 * 
	 * @return 是否成功更改密码
	 * @throws AuthorityChangingException
	 */
	public synchronized boolean changeLevelPassword(CommanderIDWithAuthority commanderID,
			String levelName, String newPassword,
			IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException
	{
		if (null == commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		if (null == newPassword)
		{
			throw new NullPointerException("password should not be null");
		}
		if (null == authorityBaseConfigurer)
		{
			throw new NullPointerException(
					"authorityBaseConfigurer should not be null");
		}
		if (null == levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		try
		{
			if (!authorityBaseConfigurer.containsLevel(levelName))
			{
				throw new IllegalArgumentException(
						"levelName should be contained in AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}

		try
		{
			if (!(authorityBaseConfigurer
					.compareAuthority(authorityBaseConfigurer
							.findPermittedAuthority(commanderID), levelName) > 0))
			{
				return false;
			}

			authorityBaseConfigurer.changeLevelPassword(levelName, newPassword);
			return true;
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}

	}

	/**
	 * 这个方法用于一个命令发布者希望提升自己的权限等级的时候，它需要给出
	 * 自己的commanderID，自己希望的权限等级levelName和这个权限等级的密 码password。
	 * 
	 * 当且仅当这些条件满足的时候，这个命令发布者才能成功达到目的： 
	 * 1、传入参数都不为null
	 * 2、levelName曾经加入到AuthorityBase中，并且没有被删除 
	 * 3、levelName高于commanderID已有的权限等级
	 * 4、password和levelName对应的密码经equals方法测试相等
	 * 
	 * @return 是否成功提升权限
	 * @throws AuthorityChangingException
	 */
	private synchronized boolean heightenSelfLevel(CommanderIDWithAuthority commanderID,
			String levelName, String password,
			IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException
	{
		if (null == commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		if (null == password)
		{
			throw new NullPointerException("password should not be null");
		}
		if (null == authorityBaseConfigurer)
		{
			throw new NullPointerException(
					"authorityBaseConfigurer should not be null");
		}
		if (null == levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		try
		{
			if (!authorityBaseConfigurer.containsLevel(levelName))
			{
				throw new IllegalArgumentException(
						"levelName should be contained in AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}

		try
		{
			if (authorityBaseConfigurer
					.compareAuthority(levelName, authorityBaseConfigurer
							.findPermittedAuthority(commanderID)) <= 0)
			{
				return false;
			}

			if (!authorityBaseConfigurer.verifyLevelAndPassword(levelName,
					password))
			{
				return false;
			}

			authorityBaseConfigurer.setCommanderAuthority(commanderID,
					levelName);
			return true;
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}

	}

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
	 * @throws AuthorityChangingException
	 */
	public synchronized boolean heightenSelfAuthority(CommanderIDWithAuthority commanderID,
			String levelName, String password,
			IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException

	{
		if (null == commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		if (null == password)
		{
			throw new NullPointerException("password should not be null");
		}
		if (null == authorityBaseConfigurer)
		{
			throw new NullPointerException(
					"authorityBaseConfigurer should not be null");
		}
		if (null == levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		try
		{
			if (!authorityBaseConfigurer.containsLevel(levelName))
			{
				throw new IllegalArgumentException(
						"levelName should be contained in AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}

		return heightenSelfLevel(commanderID, levelName, password,
				authorityBaseConfigurer);
	}

	/**
	 * 这个方法用于一个命令发布者希望降低自己的权限等级的时候，它需要给出
	 * 自己的TCommanderID即commanderID和自己希望的权限等级levelName。
	 * 
	 * 当且仅当这些条件满足的时候，这个命令发布者才能成功达到目的： 
	 * 1、传入参数都不为null
	 * 2、levelName曾经加入到AuthorityBase中，并且没有被删除 
	 * 3、levelName低于commanderID已有的权限等级
	 * 
	 * @return 是否成功降低权限等级
	 * @throws AuthorityChangingException
	 */
	private synchronized boolean lowerSelfLevel(CommanderIDWithAuthority commanderID,
			String levelName, IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException
	{
		if (null == commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		if (null == authorityBaseConfigurer)
		{
			throw new NullPointerException(
					"authorityBaseConfigurer should not be null");
		}
		if (null == levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		try
		{
			if (!authorityBaseConfigurer.containsLevel(levelName))
			{
				throw new IllegalArgumentException(
						"levelName should be contained in AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}

		try
		{
			if (authorityBaseConfigurer
					.compareAuthority(levelName, authorityBaseConfigurer
							.findPermittedAuthority(commanderID)) >= 0)
			{
				return false;
			}

			authorityBaseConfigurer.setCommanderAuthority(commanderID,
					levelName);
			return true;
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}
	}

	/**
	 * 这个方法用于一个命令发布者希望将自己降低至无权限等级的时候， 它需要给出自己的TCommanderID即commanderID。
	 * 
	 * 当且仅当这些条件满足的时候，这个命令发布者才能成功达到目的： 
	 * 1、传入参数都不为null 
	 * 2、commanderID不是无权限等级
	 * 
	 * @return 是否成功降低至无权限等级
	 * @throws AuthorityChangingException
	 */
	private synchronized boolean removeSelfLevel(CommanderIDWithAuthority commanderID,
			IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException
	{
		if (null == commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		if (null == authorityBaseConfigurer)
		{
			throw new NullPointerException(
					"authorityBaseConfigurer should not be null");
		}

		if (authorityBaseConfigurer.findPermittedAuthority(commanderID) == null)
		{
			return false;
		}

		try
		{
			authorityBaseConfigurer.setCommanderAuthority(commanderID, null);
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}
		return true;

	}

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
	 * @throws AuthorityChangingException
	 */
	public synchronized boolean lowerSelfAuthority(CommanderIDWithAuthority commanderID,
			String levelName, IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException
	{
		if (null == commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		if (null == authorityBaseConfigurer)
		{
			throw new NullPointerException(
					"authorityBaseConfigurer should not be null");
		}
		try
		{
			if (levelName!=null&&!authorityBaseConfigurer.containsLevel(levelName))
			{
				throw new IllegalArgumentException(
						"levelName should be contained in AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}
		
		if (null == levelName)
		{
			return removeSelfLevel(commanderID, authorityBaseConfigurer);
		}
		return lowerSelfLevel(commanderID, levelName, authorityBaseConfigurer);

	}

	/**
	 * 这个方法用于一个命令发布者selfCommanderID，希望改变另一个命令发布者
	 * otherCommanderID的权限等级的时候。它需要给出自己的TCommanderID即
	 * selfCommanderID，被改变者的TCommanderID即otherCommanderID，以及
	 * 它希望将otherCommanderID改变到的权限等级levelName。
	 * 
	 * 当且仅当这些条件满足的时候，这个命令发布者才能成功达到目的： 
	 * 1、传入参数都不为null
	 * 2、levelName曾经加入到AuthorityVerifier中，并且没有被删除
	 * 3、levelName低于或等于selfCommanderID的权限等级
	 * 4、otherCommanderID的权限等级低于selfCommanderID的权限等级，或者
	 * otherCommanderID没有权限等级但selfCommanderID有
	 * 5、otherCommanderID的权限等级不等于levelName
	 * 
	 * @return selfCommanderID命令发布者是否成功达到目的
	 * @throws AuthorityChangingException
	 */
	private synchronized boolean setOtherLevel(CommanderIDWithAuthority selfCommanderID,
			CommanderIDWithAuthority otherCommanderID, String levelName,
			IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException
	{
		if (null == selfCommanderID)
		{
			throw new NullPointerException("selfCommanderID should not be null");
		}
		if (null == otherCommanderID)
		{
			throw new NullPointerException(
					"otherCommanderID should not be null");
		}
		if (null == authorityBaseConfigurer)
		{
			throw new NullPointerException(
					"authorityBaseConfigurer should not be null");
		}
		if (null == levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		try
		{
			if (!authorityBaseConfigurer.containsLevel(levelName))
			{
				throw new IllegalArgumentException(
						"levelName should be contained in AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}

		try
		{
			if (!(authorityBaseConfigurer
					.compareAuthority(authorityBaseConfigurer
							.findPermittedAuthority(selfCommanderID), levelName) >= 0))
			{
				return false;
			}

			if (!(authorityBaseConfigurer.compareAuthority(
					authorityBaseConfigurer
							.findPermittedAuthority(selfCommanderID),
					authorityBaseConfigurer
							.findPermittedAuthority(otherCommanderID)) > 0))
			{
				return false;
			}

			if (authorityBaseConfigurer.compareAuthority(
					authorityBaseConfigurer
							.findPermittedAuthority(otherCommanderID),
					levelName) == 0)
			{
				return false;
			}

			authorityBaseConfigurer.setCommanderAuthority(otherCommanderID,
					levelName);
			return true;
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}

	}

	/**
	 * 这个方法用于一个命令发布者selfCommanderID，希望将另一个命令发布者
	 * otherCommanderID降级至无权限等级的时候。它需要给出自己的TCommanderID
	 * 即selfCommanderID和被改变者的TCommanderID即otherCommanderID。
	 * 
	 * 当且仅当这些条件满足的时候，这个命令发布者才能成功达到目的： 
	 * 1、传入参数都不为null
	 * 2、otherCommanderID的权限等级低于selfCommanderID的权限等级，或者
	 * otherCommanderID没有权限等级但selfCommanderID有 
	 * 3、otherCommanderID不是无权限等级
	 * 
	 * @return selfCommanderID命令发布者是否成功达到目的
	 * @throws AuthorityChangingException
	 */
	private synchronized boolean removeOtherLevel(CommanderIDWithAuthority selfCommanderID,
			CommanderIDWithAuthority otherCommanderID,
			IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException
	{
		if (null == selfCommanderID)
		{
			throw new NullPointerException("selfCommanderID should not be null");
		}
		if (null == otherCommanderID)
		{
			throw new NullPointerException(
					"otherCommanderID should not be null");
		}
		if (null == authorityBaseConfigurer)
		{
			throw new NullPointerException(
					"authorityBaseConfigurer should not be null");
		}

		try
		{
			if (!(authorityBaseConfigurer.compareAuthority(
					authorityBaseConfigurer
							.findPermittedAuthority(selfCommanderID),
					authorityBaseConfigurer
							.findPermittedAuthority(otherCommanderID)) > 0))
			{
				return false;
			}

			if (authorityBaseConfigurer
					.findPermittedAuthority(otherCommanderID) == null)
			{
				return false;
			}

			authorityBaseConfigurer.setCommanderAuthority(otherCommanderID,
					null);
			return true;
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}

	}

	/**
	 * 这个方法用于一个命令发布者selfCommanderID，希望改变另一个命令发布者
	 * otherCommanderID的权限的时候。它需要给出自己的TCommanderID即
	 * selfCommanderID，被改变者的TCommanderID即otherCommanderID，以及
	 * 它希望将otherCommanderID改变到的权限等级levelName。如果levelName
	 * 为null，则表示将otherCommanderID变为无权限等级。
	 * 
	 * 当且仅当这些条件满足的时候，这个命令发布者才能成功达到目的： 
	 * 1、传入参数除levelName外都不为null
	 * 2、selfCommanderID不能和otherCommanderID相同
	 * 2、levelName曾经加入到AuthorityVerifier中，并且没有被删除；或者levelName是null
	 * 3、levelName低于或等于selfCommanderID的权限等级
	 * 4、otherCommanderID的权限等级低于selfCommanderID的权限等级，或者
	 * otherCommanderID没有权限等级但selfCommanderID有
	 * 5、otherCommanderID的权限等级不等于levelName；或者当levelName==null时，
	 * otherCommanderID不是无权限等级
	 * 
	 * @return selfCommanderID命令发布者是否成功达到目的
	 * @throws AuthorityChangingException
	 */
	public synchronized boolean setOtherAuthority(CommanderIDWithAuthority selfCommanderID,
			CommanderIDWithAuthority otherCommanderID, String levelName,
			IAuthorityBaseConfigurer authorityBaseConfigurer)
			throws AuthorityChangingException
	{

		if (null == selfCommanderID)
		{
			throw new NullPointerException("selfCommanderID should not be null");
		}
		if (null == otherCommanderID)
		{
			throw new NullPointerException(
					"otherCommanderID should not be null");
		}
		if(otherCommanderID.equals(selfCommanderID)
				||selfCommanderID.equals(otherCommanderID)
				||selfCommanderID==otherCommanderID)
		{
			throw new IllegalArgumentException(
					"self commander id should not equals to other commander id");
		}
		if (null == authorityBaseConfigurer)
		{
			throw new NullPointerException(
					"authorityBaseConfigurer should not be null");
		}
		try
		{
			if (levelName!=null&&!authorityBaseConfigurer.containsLevel(levelName))
			{
				throw new IllegalArgumentException(
						"levelName should be contained in AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityChangingException(ex);
		}
		
		if (null == levelName)
		{
			return removeOtherLevel(selfCommanderID, otherCommanderID,
					authorityBaseConfigurer);
		}
		return setOtherLevel(selfCommanderID, otherCommanderID, levelName,
				authorityBaseConfigurer);
	}

}
