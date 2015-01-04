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
 * AuthorityCenterģ������
 * 
 * AuthorityCenter��֧�ֶ��̣߳���
 * ��������Ȩ�����⡣
 * 
 * ����ԣ�
 * 1������Ȩ������
 * 2������Ȩ�޶�����(Command)���й���
 * 3������Ҫ��֤�������±��Ȩ������
 * 
 * 1��Ȩ��ϵͳ�Ļ�������
 * 		0��CommandWithAuthority������дΪCommand������ָ�������˼��ֻ����Ϊ�˱�
 * 		ʾ������Ȩ��ϵͳ�е������˼�����WithAuthority��׺��CommanderIDWithAuthority
 * 		������дΪCommanderID��ֻ����Ϊ�˱�ʾ������Ȩ��ϵͳ�е������˼�����
 * 		WithAuthority��׺��
 * 		1��CommanderID����������ߣ�ʵ����������ID���ܹ�Ψһ�����ֿ�������ߡ�
 * 		   ������������ʾ������ߡ�
 * 		2��CommandHead����һ�����ʵ����������ͷ����������"delete a123.txt"��
 * 		   "delete"��������ͷ��һ������������������ֳ�����������������ʾһ�����
 * 		3��LevelName����һ��Ȩ�޵ȼ���Ȩ�޵ȼ���ȫ����AuthorityLevel�����ҳ�����
 *         д֮ΪLevel��LevelName��Ȩ�޵ȼ������֡�����"Normal"��"Middle"��"High"��
 *         ��ʹ���������Ա�ʾ������Ȩ�޵ȼ��ˣ�������������Ȩ�޵ȼ�����������ʾȨ�ޡ�
 * 		4��Password����˼��������롣
 * 		5���ҳ�����CommanderID��������߻�Ϊһ̸����CommandHead�������Ϊһ̸��
 * 		   ��LevelName��Ȩ�޵ȼ���Ϊһ̸
 * 
 * 2��ʹ�������ǰ��Ҫ֪���ģ�
 * 		1��ÿ��CommanderID���Զ�Ӧһ��Ȩ�޵ȼ�(����һ��)����������������ӵ�е�Ȩ�޵ȼ���
 * 		2��ÿ��CommandHead���Զ�Ӧһ��Ȩ�޵ȼ�(����һ��)�������������������Ҫ���Ȩ�޵ȼ���
 * 		3��Ȩ�ޣ�Authority�������˰���Ȩ�޵ȼ��⣨AughorityLevel��������һ��Ȩ�޵�״̬������
 *      "��Ȩ�޵ȼ�"��CommanderID��CommandHead��������Ȩ�޵ȼ�����Ȩ�޵ȼ���CommanderID
 *      ֻ��������Ȩ�޵ȼ���CommandHead����Ȩ�޵ȼ���CommandHead���Ա��κ�CommanderID��
 *      �С�����԰���Ȩ�޵ȼ�������͵�Ȩ�޵ȼ�������������̸��Ȩ�޵ȼ���ʱ�򲻰���������֮��
 *      Ȩ�ް�����Ȩ�޵ȼ���Ȩ�޵ȼ�����Ȩ�޵ȼ�����Ȩ�޵ȼ���Ȼ��������ĳ��Ȩ�ޣ��������ǲ�ͬ
 *      �ġ�
 * 		3��Ȩ�޵ȼ��иߵ�֮�֣���ͬ��Ȩ�޵ȼ��ĸߵ�һ����ͬ��ÿ��Ȩ�޵ȼ���һ������LevelName
 * 		������ʶ����"��Ȩ�޵ȼ�"����һ��Ȩ�޵ȼ�����Ч���Ͽ��Կ�������͵�Ȩ�޵ȼ���CommanderID
 * 		����ִ��Ҫ���Ȩ�޵ȼ����������CommanderID��ӵ�е�Ȩ�޵ȼ������		
 * 		4��ÿ��Ȩ�޵ȼ�����һ�����룬���벻����null���߼��ϣ���CommanderIDϣ�������Լ���
 *      Ȩ�޵ȼ���ʱ��Ӧ���ṩ��ӦȨ�޵ȼ������롣������ಢ��Ҫ����������Ϊ������������
 *      ���ϵ����⡣����౻�������ʹ�����ṩ���������Ȩ�޵�֧�֣���Ӧ��ֱ�ӱ�¶�����
 *      ���ߣ��������ǿ����������ṩ�ķ���"����"���ƻ�Ȩ�����ã���
 * 		5��Ĭ��״̬�£�Ҳ���������û���ֶ����ã����е�CommanderID��CommandHead��������
 * 		����ǰ��û�м��������ᴦ��"��Ȩ�޵ȼ�"��״̬�¡�Ҳ����˵���е�������߶��ᱻ��Ϊ
 * 		����"��Ȩ��״̬��"�����е�������º�Σ�գ�Ҳ���Ǳ���Ϊ��"��Ȩ�޵ȼ�"�����
 * 
 * 3������AuthorityCenter
 *      1������Ȩ�����Ͽ⡣���õĲ��裺
 * 			1������getInterface(AuthorityCenter.ConfigureAuthorityData)�������
 * 			����AuthorityData�Ľӿ�
 *      	2������Ȩ�޵ȼ��������룬�ڴ������Ѿ�������Ӧ���������еķ�����������
 *      	�˲�������
 *      	3����Ҫ��Ȩ�޵ȼ�������ָ����Ȩ�޵ȼ�
 *      	4����ӵ��Ȩ�޵ȼ����������ָ����Ȩ�޵ȼ�
 *      2������Ȩ�޹��˻���
 *      	1������getInterface(AuthorityCenter.ConfigureAuthorityFilter)�������
 *      	����Ȩ�޹��˻��ƵĽӿ�
 *      	2��ͨ������Ȩ�޹���AuthorityRule��������á�
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
	 * ����������ʹ��SimplePasswordManager����������
	 * @return һ��AuthorityCenter��ʵ����ֻ����ת������IAuthorityCenterEntrance�ӿ�����
	 */
	public static IAuthorityCenterEntrance createAuthorityCenter()
	{
		return createAuthorityCenter(new SimplePasswordManager());
	}
	
	/**
	 * ��������
	 * @param passwordManager ָ��ʹ������PasswordManager���������롣
	 * 
	 * @return һ��AuthorityCenter��ʵ����ֻ����ת������IAuthorityCenterEntrance�ӿ�����
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
