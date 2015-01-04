package com.accela.AuthorityCenter.authorityBase;

import java.util.*;

/**
 * 权限资料库(支持多线程)。
 * 
 * 权限问题涉及的需要记录的信息有：
 * 1、有哪些权限等级，每个等级的密码是多少；
 * 2、命令发布者（由CommanderID表示）所拥有的权限等级是什么；
 * 3、命令（由CommandHead表示）的执行所要求的权限等级是什么。
 * 
 * 这些信息组成权限资料库。这个类负责存储这些信息，以及提供配置和访问它们
 * 的方法。注意这个类只是提供权限资料，它不负责任何关于权限的逻辑内容，比
 * 如命令发布者只能执行要求的权限低于自己拥有的权限的命令。
 * 
 * 1、权限系统的基本概念
 * 		1、TCommanderID代表命令发布者，实际上是它的ID，能够唯一地区分开命令发布者。
 * 		   这里用它来表示命令发布者。
 * 		2、TCommandHead代表一种命令，实际上是命令头，比如命令"delete a123.txt"，
 * 		   "delete"就是命令头，一种命令可以用它来区分出来。这里用它来表示一种命令。
 * 		3、TLevelName代表一种权限等级，权限等级的全称是AuthorityLevel，但我常常简
 *         写之为Level。LevelName是权限等级的名字。比如"Normal"、"Middle"、"High"。
 *         但使用它就足以表示和区分权限等级了，因此这里就是用权限等级的名字来表示权限。
 * 		4、TPassword顾名思义代表密码。
 * 		5、我常常将CommanderID和命令发布者混为一谈，将CommandHead和命令混为一谈，
 * 		   将LevelName和权限等级混为一谈
 * 
 * 2、使用这个类前需要知道的：
 * 		1、每个TCommanderID可以对应一个权限等级(至多一个)，代表这个命令发布者拥有的权限等级。
 * 		2、每个TCommandHead可以对应一个权限等级(至多一个)，代表运行这个命令所要求的权限等级。
 * 		3、权限（Authority），除了包括权限等级外（AughorityLevel），该有一种权限的状态，叫做
 *      "无权限等级"。TCommanderID和TCommandHead都可以无权限等级。无权限等级的TCommanderID
 *      只能运行无权限等级的ICommandHead；无权限等级的TCommandHead可以被任何TCommanderID运
 *      行。你可以把无权限等级看作最低的权限等级，但后面我们谈到权限等级的时候不包括它。总之，
 *      权限包括无权限等级和权限等级，但权限等级和无权限等级虽然都象征着某种权限，但它们是不同
 *      的。
 * 		3、权限等级有高低之分，不同的权限等级的高低一定不同。每个权限等级有一个名字TLevelName
 * 		用来标识它。"无权限等级"不是一种权限等级，但效果上可以看作是最低的权限等级。TCommanderID
 * 		可以执行要求的权限等级不高于这个TCommanderID所拥有的权限等级的命令。		
 * 		4、每个权限等级都有一个密码，密码不能是null。逻辑上，当TCommanderID希望提升自己的
 *      权限等级的时候，应该提供相应权限等级的密码。但这个类并不要求这样，因为它并不处理逻
 *      辑上的问题。这个类被设计来给使用者提供关于命令和权限的支持，不应该直接暴露给命令发
 *      布者（否则它们可以用这里提供的方法"方便"地破坏权限设置）。
 * 		5、默认状态下，也就是如果你没有手动设置，所有的TCommanderID和TCommandHead，不管这
 * 		个以前有没有见过，都会处在"无权限等级"的状态下。也就是说所有的命令发布者都会被认为
 * 		处在"无权限状态下"，所有的命令，哪怕很危险，也会是被认为是"无权限等级"的命令。
 * 
 * 3、配置权限资料库。配置的步骤：
 * 		1、在构造方法中，你需要传入一个PasswordTank，作为密码的容器。如果你没
 *      有什么严格的安全需要，可以使用SimplePasswordTank
 *      2、定义权限等级及其密码，在代码中已经划出对应区域，区域中的方法用来进行
 *      此步的配置
 *      3、给要求权限等级的命令指定其权限等级
 *      4、给拥有权限等级的命令发布者指定其权限等级
 * 
 *
 * ==========================================================
 * 注意：
 * 1、TLevelName必须具有合适的equals、hashCode和comareTo方法
 * 2、TPassword必须具有合适的equals方法
 * 3、TCommanderID必须具有合适的equals、hashCode和comareTo方法
 * 4、TCommandHead必须具有合适的equals、hashCode和comareTo方法
 * ==========================================================
 * 
 */
public class TypedAuthorityBase<TCommanderID, TCommandHead, TLevelName, TPassword>
implements ITypedAuthorityBaseViewer<TCommanderID, TCommandHead, TLevelName, TPassword>, 
ITypedAuthorityBaseConfigurer<TCommanderID, TCommandHead, TLevelName, TPassword>
{
	/**
	 * 记录所有拥有某种权限等级的TCommanderID，以及其对应的权限等级
	 */
	private Map<TCommanderID, TLevelName> commanderMap;
	/**
	 * 记录所有要求某种权限等级的TCommandHead，以及其对应的权限等级
	 */
	private Map<TCommandHead, TLevelName> commandMap;
	/**
	 * 记录和管理权限等级及其对应的密码。
	 */
	private TypedAuthorityLevelManager<TLevelName, TPassword> levelManager;
	
	/**
	 * @param passwordManager 指定使用何种PasswordTank来存储和管理密码
	 */
	public TypedAuthorityBase(TypedPasswordManager<TLevelName, TPassword> passwordManager)
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
		
		this.commanderMap=new HashMap<TCommanderID, TLevelName>();
		this.commandMap=new HashMap<TCommandHead, TLevelName>();
		this.levelManager=new TypedAuthorityLevelManager<TLevelName, TPassword>(passwordManager);
	}
	
	///////////////////////////////定义权限等级及其密码////////////////////////////////
	
	/**
	 * 加入一个新的权限等级作为目前最高的权限等级，实际上这个方法是要求
	 * 使用者从低到高依次加入权限等级。
	 * 如果levelName和过去某个权限等级重复，则这个方法什么也不做，即操
	 * 作失败。
	 * 
	 * @param levelName 新的权限等级
	 * @param password 新的权限等级的密码
	 * @return 是否成功地加入了新的权限等级。
	 * @throws AuthorityBaseOperatingException 
	 */
	public synchronized boolean addHighestLevel(TLevelName levelName, TPassword password) throws AuthorityBaseOperatingException
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
			if(levelManager.containsLevel(levelName))
			{
				return false;
			}
			else
			{
				boolean ret=levelManager.addHighestLevel(levelName, password);
				assert(ret);
				return true;
			}
		} catch (Exception ex)
		{
			throw new AuthorityBaseOperatingException(ex);
		}
		
	}
	
	/**
	 * 删除最高的那个权限等级。所有拥有这个权限等级的TCommanderID和TCommandHead
	 * 都会被下降一个权限等级。但是如果被删除的权限等级就是最低权限等级，则上述
	 * TCommanderID和TCommandHead就会被删除权限等级，即变为无权限等级。
	 * 
	 * 如果没有最高权限等级，即此时还没有任何权限等级，则什么也不做
	 * 
	 * @return 被删除的最高权限等级，如果没有则返回null
	 * @throws AuthorityBaseOperatingException 
	 */
	public synchronized TLevelName removeHighestLevel() throws AuthorityBaseOperatingException
	{		
		try
		{
			// 检查是否有最高等级的权限，并删除最高权限等级
			final TLevelName highest = levelManager.removeHighestLevel();
			if (null == highest)
			{
				return null;
			}

			// 找出应该降至的什么权限等级，或者无权限等级
			TLevelName newLevel = levelManager.getHighestLevel();
			
			// 迭代CommandMap，找出需要改变的TCommandHead并更改之
			LinkedList<TCommandHead> commandHeadsToModify=new LinkedList<TCommandHead>();
			for (TCommandHead command : commandMap.keySet())
			{
				assert (command != null); // 理论上不应该发生这种事
				if (commandMap.get(command).equals(highest))
				{
					commandHeadsToModify.add(command);
				}
			}
			
			//修改需要更改的TCommandHead
			for(TCommandHead command : commandHeadsToModify)
			{
				assert(command!=null);
				if (null == newLevel)
				{
					commandMap.remove(command);
				} else
				{
					commandMap.put(command, newLevel);
				}
			}

			// 迭代CommanderMap，找出需要改变的TCommanderID并更改之
			LinkedList<TCommanderID> commandersToModify=new LinkedList<TCommanderID>();
			for (TCommanderID commander : commanderMap.keySet())
			{
				assert (commander != null); // 理论上不应该发生这种事
				if (commanderMap.get(commander).equals(highest))
				{
					commandersToModify.add(commander);
				}
			}
			
			//修改需要更改的TCommandHead
			for(TCommanderID commander : commandersToModify)
			{
				assert(commander!=null);
				if (null == newLevel)
				{
					commanderMap.remove(commander);
				} else
				{
					commanderMap.put(commander, newLevel);
				}
			}
			
			return highest;

		} catch (Exception ex)
		{
			throw new AuthorityBaseOperatingException(ex);
		}
		
	}
	
	/**
	 * 修改某个权限等级的密码
	 */
	public synchronized TPassword changeLevelPassword(TLevelName levelName, TPassword password) throws AuthorityBaseOperatingException
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		if(null==password)
		{
			throw new NullPointerException("password should not be null");
		}
		if(!containsLevel(levelName))
		{
			throw new IllegalArgumentException("levelName is not contained in the LevelManager");
		}
		
		try
		{
			return levelManager.changePassword(levelName, password);
		} catch (Exception ex)
		{
			throw new AuthorityBaseOperatingException(ex);
		}
	}
	
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
	 * @throws AuthorityBaseOperatingException 
	 */
	public synchronized boolean verifyLevelAndPassword(TLevelName levelName, TPassword password) throws AuthorityBaseOperatingException
	{
		if(null==levelName||null==password)
		{
			return false;
		}
		if(!containsLevel(levelName))
		{
			return false;
		}
		
		try
		{
			return levelManager.verify(levelName, password);
		} catch (Exception ex)
		{
			throw new AuthorityBaseOperatingException(ex);
		}
	}


	/**
	 * 检查是否加入过指定的权限等级并没有删除，即含有该权限等级，
	 * 不能传入null
	 * @throws AuthorityBaseOperatingException 
	 */
	public synchronized boolean containsLevel(TLevelName levelName) throws AuthorityBaseOperatingException
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		
		try
		{
			return levelManager.containsLevel(levelName);
		} catch (Exception ex)
		{
			throw new AuthorityBaseOperatingException(ex);
		}
	}
	
	/**
	 * 比较两个权限等级的等级高低，不能传入null。
	 * 和Java的compare方法规则相同
	 * 
	 * @throws AuthorityBaseOperatingException
	 */
	private synchronized int compareLevel(TLevelName levelNameA, TLevelName levelNameB) throws AuthorityBaseOperatingException
	{
		if(null==levelNameA)
		{
			throw new NullPointerException("levelNameA should not be null");
		}
		if(null==levelNameB)
		{
			throw new NullPointerException("levelNameB should not be null");
		}
		
		if (!containsLevel(levelNameA))
		{
			throw new IllegalArgumentException(
					"levelNameA is not contained in the LevelManager");
		}
		if (!containsLevel(levelNameB))
		{
			throw new IllegalArgumentException(
					"levelNameB is not contained in the LevelManager");
		}
		
		try
		{
			return levelManager.compareLevel(levelNameA, levelNameB);
		} catch (Exception ex)
		{
			throw new AuthorityBaseOperatingException(ex);
		}
	}

	/**
	 * 比较权限的高低，你既可以传入TLevelName对象来比较
	 * 两个权限等级，又可以传入null来表示没有权限等级。无权限等级和无
	 * 权限等级相等。
	 * 
	 * 返回值和Java的Compare方法的规范相同。
	 * @throws AuthorityBaseOperatingException
	 */
	public synchronized int compareAuthority(TLevelName levelNameA, TLevelName levelNameB) throws AuthorityBaseOperatingException
	{
		if(levelNameA!=null&&!containsLevel(levelNameA))
		{
			throw new IllegalArgumentException(
			"levelNameA is not contained in the LevelManager");
		}
		if(levelNameB!=null&&!containsLevel(levelNameB))
		{
			throw new IllegalArgumentException(
			"levelNameB is not contained in the LevelManager");
		}
		
		if(null==levelNameA&&null==levelNameB)
		{
			return 0;
		}
		if(null==levelNameA)
		{
			return -1;
		}
		if(null==levelNameB)
		{
			return 1;
		}
		return compareLevel(levelNameA, levelNameB);
	}
	
	/**
	 * 返回按升序排列的所有权限等级。数组中没有空余位置。
	 * 如果没有权限等级，则返回长度为零的数组而不是null。
	 * 另外，这个类中不会保留返回的数组的引用，因此你可
	 * 以修改这个数组。
	 * @throws AuthorityBaseOperatingException 
	 */
	public synchronized List<TLevelName> getAllLevels() throws AuthorityBaseOperatingException
	{
		try
		{
			return levelManager.getIncSortedAllLevels();
		} catch (Exception ex)
		{
			throw new AuthorityBaseOperatingException(ex);
		}
	}
	
	/**
	 * 返回一共有多少个权限等级 
	 */
	public synchronized int getNumOfLevels() throws AuthorityBaseOperatingException
	{
		try
		{
			return levelManager.getSize();
		} catch (Exception ex)
		{
			throw new AuthorityBaseOperatingException(ex);
		}
	}
	
	//////////////////////////给要求权限等级的命令指定其权限等级///////////////////////////
	
	/**
	 * 将commandHead这种命令的权限等级设定为levelName
	 * @param commandHead 将要被设定的命令
	 * @param levelName commandHead被设定成的权限等级
	 * @return commandHead此前的权限等级，如果commandHead此前处于无权限等级状态，那么返回null
	 * @throws AuthorityBaseOperatingException 
	 */
	private synchronized TLevelName setCommandHeadLevel(TCommandHead commandHead, TLevelName levelName) throws AuthorityBaseOperatingException
	{
		if(null==commandHead)
		{
			throw new NullPointerException("commandHead should not be null");
		}
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		if(!containsLevel(levelName))
		{
			throw new IllegalArgumentException("leveName should be contained by AuthorityVerifier");
		}
		
		return commandMap.put(commandHead, levelName);
	}
	
	/**
	 * 将commandHead的权限等级删除，即将其设置为无权限等级
	 * @return commandHead此前的权限等级，如果commandHead此前处于无权限等级状态，那么返回null
	 */
	private synchronized TLevelName removeCommandHeadLevel(TCommandHead commandHead)
	{
		if(null==commandHead)
		{
			throw new NullPointerException("commandHead should not be null");
		}
		
		return commandMap.remove(commandHead);
	}
	
	/**
	 * 重新设定commandHead这种命令的权限。如果levelName！=null，则将其设定为levelName
	 * 相应的权限等级，否则将其设定为无权限等级。
	 * @param commandHead 将要被设定的命令
	 * @param levelName commandHead被设定成的权限等级
	 * @return commandHead此前的权限等级，如果commandHead此前处于无权限等级状态，那么返回null
	 * @throws AuthorityBaseOperatingException 
	 */
	public synchronized TLevelName setCommandHeadAuthority(TCommandHead commandHead, TLevelName levelName) throws AuthorityBaseOperatingException
	{
		if(null==commandHead)
		{
			throw new NullPointerException("commandHead should not be null");
		}
		if(levelName!=null&&!containsLevel(levelName))
		{
			throw new IllegalArgumentException("leveName should be contained by AuthorityVerifier");
		}
		
		if(null==levelName)
		{
			return removeCommandHeadLevel(commandHead);
		}
		return setCommandHeadLevel(commandHead, levelName);
	}
	
	/**
	 * 查询传入的commandHead要求什么权限才可以被运行，如果commandHead无权限等级，
	 * 则返回null。
	 * @return commandHead要求的权限等级，如果commandHead无权限等级，则返回null
	 */
	public synchronized TLevelName findRequiredAuthority(TCommandHead commandHead)
	{
		if(null==commandHead)
		{
			throw new NullPointerException("commandHead should not be null");
		}
		
		return commandMap.get(commandHead);
	}
	
	///////////////////给拥有权限等级的命令发布者指定其权限等级///////////////////////////
	
	/**
	 * 将commanderID的权限等级设定为levelName
	 * @return commanderID此前的权限等级，如果commanderID此前处于无权限等级状态，那么返回null
	 * @throws AuthorityBaseOperatingException 
	 */
	private synchronized TLevelName setCommanderLevel(TCommanderID commanderID, TLevelName levelName) throws AuthorityBaseOperatingException
	{
		if(null==commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		if(!containsLevel(levelName))
		{
			throw new IllegalArgumentException("leveName should be contained by AuthorityVerifier");
		}
		
		return commanderMap.put(commanderID, levelName);
	}
	
	/**
	 * 将commanderID的权限等级删除，即将其设置为无权限等级
	 * @return commanderID此前的权限等级，如果commanderID此前处于无权限等级状态，那么返回null
	 */
	private synchronized TLevelName removeCommanderLevel(TCommanderID commanderID)
	{
		if(null==commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		
		return commanderMap.remove(commanderID);
    }
	
	/**
	 * 重新设定commanderID的权限。如果levelName！=null，则将其设定为levelName
	 * 相应的权限等级，否则将其设定为无权限等级。
	 * @param commanderID 将要被设定的命令
	 * @param levelName commandHead被设定成的权限等级
	 * @return commanderID此前的权限等级，如果commanderID此前处于无权限等级状态，那么返回null
	 * @throws AuthorityBaseOperatingException 
	 */
	public synchronized TLevelName setCommanderAuthority(TCommanderID commanderID, TLevelName levelName) throws AuthorityBaseOperatingException
	{
		if(null==commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		if(levelName!=null&&!containsLevel(levelName))
		{
			throw new IllegalArgumentException("leveName should be contained by AuthorityVerifier");
		}
		
		if(null==levelName)
		{
			return removeCommanderLevel(commanderID);
		}
		return setCommanderLevel(commanderID, levelName);
	}
	
	/**
	 * 查询传入的commanderID拥有什么样的权限。如果commanderID无权限等级，则返回null。
	 * @return commanderID拥有的权限等级，如果commanderID无权限等级，则返回null
	 */
	public synchronized TLevelName findPermittedAuthority(TCommanderID commanderID)
	{
		if(null==commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		
		return commanderMap.get(commanderID);
    }
	
}
