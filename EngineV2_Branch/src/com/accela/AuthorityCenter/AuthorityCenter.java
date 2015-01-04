package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.authorityBase.AuthorityBase;
import com.accela.AuthorityCenter.authorityBase.AuthorityBaseOperatingException;
import com.accela.AuthorityCenter.authorityBase.PasswordManager;
import com.accela.AuthorityCenter.authorityBase.SimplePasswordManager;
import com.accela.AuthorityCenter.changeManager.AuthorityChangeManager;
import com.accela.AuthorityCenter.changeManager.AuthorityChangingException;
import com.accela.AuthorityCenter.ruleFilter.AuthorityFilter;
import com.accela.AuthorityCenter.ruleFilter.AuthorityFilteringException;
import com.accela.AuthorityCenter.ruleFilter.AuthorityRule;
import com.accela.AuthorityCenter.shared.CommandWithAuthority;
import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * AuthorityCenter模块的入口
 * 
 * AuthorityCenter（支持多线程），
 * 用来管理权限问题。
 * 
 * 你可以：
 * 1、配置权限设置
 * 2、利用权限对命令(Command)进行过滤
 * 3、在需要验证的条件下变更权限设置
 * 
 * 1、权限系统的基本概念
 * 		0、CommandWithAuthority常常简写为Command，就是指命令的意思，只不过为了表
 * 		示这是在权限系统中的命令，因此加上了WithAuthority后缀。CommanderIDWithAuthority
 * 		常常简写为CommanderID，只不过为了表示这是在权限系统中的命令，因此加上了
 * 		WithAuthority后缀。
 * 		1、CommanderID代表命令发布者，实际上是它的ID，能够唯一地区分开命令发布者。
 * 		   这里用它来表示命令发布者。
 * 		2、CommandHead代表一种命令，实际上是命令头，比如命令"delete a123.txt"，
 * 		   "delete"就是命令头，一种命令可以用它来区分出来。这里用它来表示一种命令。
 * 		3、LevelName代表一种权限等级，权限等级的全称是AuthorityLevel，但我常常简
 *         写之为Level。LevelName是权限等级的名字。比如"Normal"、"Middle"、"High"。
 *         但使用它就足以表示和区分权限等级了，因此这里就是用权限等级的名字来表示权限。
 * 		4、Password顾名思义代表密码。
 * 		5、我常常将CommanderID和命令发布者混为一谈，将CommandHead和命令混为一谈，
 * 		   将LevelName和权限等级混为一谈
 * 
 * 2、使用这个类前需要知道的：
 * 		1、每个CommanderID可以对应一个权限等级(至多一个)，代表这个命令发布者拥有的权限等级。
 * 		2、每个CommandHead可以对应一个权限等级(至多一个)，代表运行这个命令所要求的权限等级。
 * 		3、权限（Authority），除了包括权限等级外（AughorityLevel），该有一种权限的状态，叫做
 *      "无权限等级"。CommanderID和CommandHead都可以无权限等级。无权限等级的CommanderID
 *      只能运行无权限等级的CommandHead；无权限等级的CommandHead可以被任何CommanderID运
 *      行。你可以把无权限等级看作最低的权限等级，但后面我们谈到权限等级的时候不包括它。总之，
 *      权限包括无权限等级和权限等级，但权限等级和无权限等级虽然都象征着某种权限，但它们是不同
 *      的。
 * 		3、权限等级有高低之分，不同的权限等级的高低一定不同。每个权限等级有一个名字LevelName
 * 		用来标识它。"无权限等级"不是一种权限等级，但效果上可以看作是最低的权限等级。CommanderID
 * 		可以执行要求的权限等级不高于这个CommanderID所拥有的权限等级的命令。		
 * 		4、每个权限等级都有一个密码，密码不能是null。逻辑上，当CommanderID希望提升自己的
 *      权限等级的时候，应该提供相应权限等级的密码。但这个类并不要求这样，因为它并不处理逻
 *      辑上的问题。这个类被设计来给使用者提供关于命令和权限的支持，不应该直接暴露给命令发
 *      布者（否则它们可以用这里提供的方法"方便"地破坏权限设置）。
 * 		5、默认状态下，也就是如果你没有手动设置，所有的CommanderID和CommandHead，不管这
 * 		个以前有没有见过，都会处在"无权限等级"的状态下。也就是说所有的命令发布者都会被认为
 * 		处在"无权限状态下"，所有的命令，哪怕很危险，也会是被认为是"无权限等级"的命令。
 * 
 * 3、配置AuthorityCenter
 *      1、配置权限资料库。配置的步骤：
 * 			1、调用getInterface(AuthorityCenter.ConfigureAuthorityData)方法获得
 * 			配置AuthorityData的接口
 *      	2、定义权限等级及其密码，在代码中已经划出对应区域，区域中的方法用来进行
 *      	此步的配置
 *      	3、给要求权限等级的命令指定其权限等级
 *      	4、给拥有权限等级的命令发布者指定其权限等级
 *      2、配置权限过滤机制
 *      	1、调用getInterface(AuthorityCenter.ConfigureAuthorityFilter)方法获得
 *      	配置权限过滤机制的接口
 *      	2、通过加入权限规则AuthorityRule来完成配置。
 *
 */
public class AuthorityCenter
implements IViewingAuthorityData, IConfiguringAuthorityData, 
IChangingAuthorityDataWithVerification, IConfiguringAuthorityFilter, 
IFilteringWithAuthorityFilter, IAuthorityCenterEntrance
{
	private AuthorityBase authorityBase;
	private AuthorityChangeManager authorityChangeManager;
	private AuthorityFilter authorityFilter;
	
	protected AuthorityCenter(PasswordManager passwordManager)
	{
		if(null==passwordManager)
		{
			throw new NullPointerException("passwordManager should not be null");
		}
		try
		{
			if(!passwordManager.isEmpty())
			{
				throw new IllegalArgumentException("passwordManager should be empty");
			}
		}catch(Exception ex)
		{
			throw new IllegalArgumentException("Something is wrong with passwordManager", ex);
		}
		
		this.authorityBase=new AuthorityBase(passwordManager);
		this.authorityChangeManager=new AuthorityChangeManager();
		this.authorityFilter=new AuthorityFilter();
	}
	
	//////////////////////////////////AuthorityCenter////////////////////////////////
	/**
	 * 工厂方法，使用SimplePasswordManager来管理密码
	 * @return 一个AuthorityCenter的实例，只不过转化成了IAuthorityCenterEntrance接口类型
	 */
	public static IAuthorityCenterEntrance createAuthorityCenter()
	{
		return createAuthorityCenter(new SimplePasswordManager());
	}
	
	/**
	 * 工厂方法
	 * @param passwordManager 指定使用哪中PasswordManager来管理密码。
	 * 
	 * @return 一个AuthorityCenter的实例，只不过转化成了IAuthorityCenterEntrance接口类型
	 */
	public static IAuthorityCenterEntrance createAuthorityCenter(PasswordManager passwordManager)
	{
		if(null==passwordManager)
		{
			throw new NullPointerException("passwordManager should not be null");
		}
		try
		{
			if(!passwordManager.isEmpty())
			{
				throw new IllegalArgumentException("passwordManager should be empty");
			}
		}catch(Exception ex)
		{
			throw new IllegalArgumentException("Something is wrong with passwordManager", ex);
		}
		
		return new AuthorityCenter(passwordManager);
	}
	
	
	public static enum AuthorityCenterFunctionInterfaces{
		CONFIGURE_AUTHORITY_DATA,
		CHANGE_AUTHORITY_DATA_WITH_VERIFCATION,
		CONFIGURE_AUTHORITY_FILTER,
		FILTER_WITH_AUTHORITY_FILTER,
		VIEW_AUTHORITY_DATA,
	};
	public IAuthorityCenterFunctionInterface getInstance(
			AuthorityCenterFunctionInterfaces face)
	{
		if(AuthorityCenterFunctionInterfaces.CHANGE_AUTHORITY_DATA_WITH_VERIFCATION==face)
		{
			return (IChangingAuthorityDataWithVerification)this;
		}
		else if(AuthorityCenterFunctionInterfaces.CONFIGURE_AUTHORITY_DATA==face)
		{
			return (IConfiguringAuthorityData)this;
		}
		else if(AuthorityCenterFunctionInterfaces.CONFIGURE_AUTHORITY_FILTER==face)
		{
			return (IConfiguringAuthorityFilter)this;
		}
		else if(AuthorityCenterFunctionInterfaces.FILTER_WITH_AUTHORITY_FILTER==face)
		{
			return (IFilteringWithAuthorityFilter)this;
		}
		else if(AuthorityCenterFunctionInterfaces.VIEW_AUTHORITY_DATA==face)
		{
			return (IViewingAuthorityData)this;
		}
		else
		{
			assert(false);
			throw new IllegalArgumentException("Illegal argument face");
		}
		
	}
	

	//////////////////////////////////AuthorityBase//////////////////////////////////
	
	@Override
	public boolean addHighestLevel(String levelName, String password)
			throws AuthorityCenterOperatingException
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		if(null==password)
		{
			throw new NullPointerException("password should not be null");
		}
		
		try
		{
			return authorityBase.addHighestLevel(levelName, password);
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public String changeLevelPassword(String levelName, String password)
			throws AuthorityCenterOperatingException
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		if(null==password)
		{
			throw new NullPointerException("password should not be null");
		}
		try
		{
			if(!authorityBase.containsLevel(levelName))
			{
				throw new IllegalArgumentException("levelName is not contained in the LevelManager");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
		
		try
		{
			return authorityBase.changeLevelPassword(levelName, password);
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public String removeHighestLevel() throws AuthorityCenterOperatingException
	{
		try
		{
			return authorityBase.removeHighestLevel();
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public String setCommandHeadAuthority(String commandHead, String levelName)
			throws AuthorityCenterOperatingException
	{
		if(null==commandHead)
		{
			throw new NullPointerException("commandHead should not be null");
		}
		try
		{
			if(levelName!=null&&!authorityBase.containsLevel(levelName))
			{
				throw new IllegalArgumentException("leveName should be contained by AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
		
		try
		{
			return authorityBase.setCommandHeadAuthority(commandHead, levelName);
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public String setCommanderAuthority(CommanderIDWithAuthority commanderID,
			String levelName) throws AuthorityCenterOperatingException
	{
		if(null==commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		try
		{
			if(levelName!=null&&!authorityBase.containsLevel(levelName))
			{
				throw new IllegalArgumentException("leveName should be contained by AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
		
		try
		{
			return authorityBase.setCommanderAuthority(commanderID, levelName);
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public int compareAuthority(String levelNameA, String levelNameB)
			throws AuthorityCenterOperatingException
	{
		try{
		if(levelNameA!=null&&!authorityBase.containsLevel(levelNameA))
		{
			throw new IllegalArgumentException(
			"levelNameA is not contained in the LevelManager");
		}
		if(levelNameB!=null&&!authorityBase.containsLevel(levelNameB))
		{
			throw new IllegalArgumentException(
			"levelNameB is not contained in the LevelManager");
		}
		}catch(AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
		
		try
		{
			return authorityBase.compareAuthority(levelNameA, levelNameB);
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public boolean containsLevel(String levelName)
			throws AuthorityCenterOperatingException
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		
		try
		{
			return authorityBase.containsLevel(levelName);
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public String findPermittedAuthority(CommanderIDWithAuthority commanderID)
	{
		if(null==commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		
		return authorityBase.findPermittedAuthority(commanderID);
	}

	@Override
	public String findRequiredAuthority(String commandHead)
	{
		if(null==commandHead)
		{
			throw new NullPointerException("commandHead should not be null");
		}
		
		return authorityBase.findRequiredAuthority(commandHead);
	}

	@Override
	public String[] getAllLevels() throws AuthorityCenterOperatingException
	{
		try
		{
			return authorityBase.getAllLevels().toArray(new String[0]);
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}
	
	@Override
	public int getNumOfLevels() throws AuthorityCenterOperatingException
	{
		try
		{
			return authorityBase.getNumOfLevels();
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public boolean verifyLevelAndPassword(String levelName, String password)
			throws AuthorityCenterOperatingException
	{
		if(null==levelName||null==password)
		{
			return false;
		}
		try
		{
			if(!authorityBase.containsLevel(levelName))
			{
				return false;
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
		
		try
		{
			return authorityBase.verifyLevelAndPassword(levelName, password);
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}
	
	/////////////////////////////AuthorityChangeManager//////////////////////////
	
	@Override
	public boolean changeLevelPasswordWithVerification(CommanderIDWithAuthority commanderID, 
			String levelName, 
			String newPassword)
			throws AuthorityCenterOperatingException
	{
		if (null == commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}

		if (null == newPassword)
		{
			throw new NullPointerException("password should not be null");
		}
		if (null == levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		try
		{
			if (!authorityBase.containsLevel(levelName))
			{
				throw new IllegalArgumentException(
						"levelName should be contained in AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
		
		try
		{
			return authorityChangeManager.changeLevelPassword(commanderID, levelName, newPassword, authorityBase);
		} catch (AuthorityChangingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public boolean heightenSelfAuthorityWithVerification(CommanderIDWithAuthority commanderID,
			String levelName, String password)
			throws AuthorityCenterOperatingException
	{
		if (null == commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		if (null == password)
		{
			throw new NullPointerException("password should not be null");
		}
		if (null == levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		try
		{
			if (!authorityBase.containsLevel(levelName))
			{
				throw new IllegalArgumentException(
						"levelName should be contained in AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
		
		try
		{
			return authorityChangeManager.heightenSelfAuthority(commanderID, levelName, password, authorityBase);
		} catch (AuthorityChangingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public boolean lowerSelfAuthorityWithVerification(CommanderIDWithAuthority commanderID,
			String levelName)
			throws AuthorityCenterOperatingException
	{
		if (null == commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		try
		{
			if (levelName!=null&&!authorityBase.containsLevel(levelName))
			{
				throw new IllegalArgumentException(
						"levelName should be contained in AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
		
		try
		{
			return authorityChangeManager.lowerSelfAuthority(commanderID, levelName, authorityBase);
		} catch (AuthorityChangingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public boolean setOtherAuthorityWithVerification(CommanderIDWithAuthority selfCommanderID,
			CommanderIDWithAuthority otherCommanderID, String levelName)
			throws AuthorityCenterOperatingException
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
		try
		{
			if (levelName!=null&&!authorityBase.containsLevel(levelName))
			{
				throw new IllegalArgumentException(
						"levelName should be contained in AuthorityVerifier");
			}
		} catch (AuthorityBaseOperatingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
		
		try
		{
			return authorityChangeManager.setOtherAuthority(selfCommanderID, otherCommanderID, levelName, authorityBase);
		} catch (AuthorityChangingException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	//////////////////////////////AuthorityFilter/////////////////////////////////////
	
	@Override
	public boolean shouldFilter(CommandWithAuthority command)
			throws AuthorityCenterOperatingException
	{
		if(null==command)
		{
			throw new NullPointerException("command should not be null");
		}
		
		try
		{
			return authorityFilter.shouldFilter(command, authorityBase);
		} catch (AuthorityFilteringException ex)
		{
			throw new AuthorityCenterOperatingException(ex);
		}
	}

	@Override
	public void addAuthorityRule(AuthorityRule rule)
	{
		if(null==rule)
		{
			throw new NullPointerException("rule should not be null");
		}
		
		authorityFilter.addAuthorityRule(rule);
	}

	@Override
	public boolean removeAuthorityRule(AuthorityRule rule)
	{
		if(null==rule)
		{
			throw new NullPointerException("rule should not be null");
		}
		
		return authorityFilter.removeAuthorityRule(rule);
	}

	@Override
	public void removeAllAuthorityRules()
	{
		authorityFilter.removeAllAuthorityRules();		
	}

}
