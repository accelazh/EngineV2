package com.accela.AuthorityCenter.authorityBase;

import java.util.*;

/**
 * Ȩ�����Ͽ�(֧�ֶ��߳�)��
 * 
 * Ȩ�������漰����Ҫ��¼����Ϣ�У�
 * 1������ЩȨ�޵ȼ���ÿ���ȼ��������Ƕ��٣�
 * 2��������ߣ���CommanderID��ʾ����ӵ�е�Ȩ�޵ȼ���ʲô��
 * 3�������CommandHead��ʾ����ִ����Ҫ���Ȩ�޵ȼ���ʲô��
 * 
 * ��Щ��Ϣ���Ȩ�����Ͽ⡣����ฺ��洢��Щ��Ϣ���Լ��ṩ���úͷ�������
 * �ķ�����ע�������ֻ���ṩȨ�����ϣ����������κι���Ȩ�޵��߼����ݣ���
 * ���������ֻ��ִ��Ҫ���Ȩ�޵����Լ�ӵ�е�Ȩ�޵����
 * 
 * 1��Ȩ��ϵͳ�Ļ�������
 * 		1��TCommanderID����������ߣ�ʵ����������ID���ܹ�Ψһ�����ֿ�������ߡ�
 * 		   ������������ʾ������ߡ�
 * 		2��TCommandHead����һ�����ʵ����������ͷ����������"delete a123.txt"��
 * 		   "delete"��������ͷ��һ������������������ֳ�����������������ʾһ�����
 * 		3��TLevelName����һ��Ȩ�޵ȼ���Ȩ�޵ȼ���ȫ����AuthorityLevel�����ҳ�����
 *         д֮ΪLevel��LevelName��Ȩ�޵ȼ������֡�����"Normal"��"Middle"��"High"��
 *         ��ʹ���������Ա�ʾ������Ȩ�޵ȼ��ˣ�������������Ȩ�޵ȼ�����������ʾȨ�ޡ�
 * 		4��TPassword����˼��������롣
 * 		5���ҳ�����CommanderID��������߻�Ϊһ̸����CommandHead�������Ϊһ̸��
 * 		   ��LevelName��Ȩ�޵ȼ���Ϊһ̸
 * 
 * 2��ʹ�������ǰ��Ҫ֪���ģ�
 * 		1��ÿ��TCommanderID���Զ�Ӧһ��Ȩ�޵ȼ�(����һ��)����������������ӵ�е�Ȩ�޵ȼ���
 * 		2��ÿ��TCommandHead���Զ�Ӧһ��Ȩ�޵ȼ�(����һ��)�������������������Ҫ���Ȩ�޵ȼ���
 * 		3��Ȩ�ޣ�Authority�������˰���Ȩ�޵ȼ��⣨AughorityLevel��������һ��Ȩ�޵�״̬������
 *      "��Ȩ�޵ȼ�"��TCommanderID��TCommandHead��������Ȩ�޵ȼ�����Ȩ�޵ȼ���TCommanderID
 *      ֻ��������Ȩ�޵ȼ���ICommandHead����Ȩ�޵ȼ���TCommandHead���Ա��κ�TCommanderID��
 *      �С�����԰���Ȩ�޵ȼ�������͵�Ȩ�޵ȼ�������������̸��Ȩ�޵ȼ���ʱ�򲻰���������֮��
 *      Ȩ�ް�����Ȩ�޵ȼ���Ȩ�޵ȼ�����Ȩ�޵ȼ�����Ȩ�޵ȼ���Ȼ��������ĳ��Ȩ�ޣ��������ǲ�ͬ
 *      �ġ�
 * 		3��Ȩ�޵ȼ��иߵ�֮�֣���ͬ��Ȩ�޵ȼ��ĸߵ�һ����ͬ��ÿ��Ȩ�޵ȼ���һ������TLevelName
 * 		������ʶ����"��Ȩ�޵ȼ�"����һ��Ȩ�޵ȼ�����Ч���Ͽ��Կ�������͵�Ȩ�޵ȼ���TCommanderID
 * 		����ִ��Ҫ���Ȩ�޵ȼ����������TCommanderID��ӵ�е�Ȩ�޵ȼ������		
 * 		4��ÿ��Ȩ�޵ȼ�����һ�����룬���벻����null���߼��ϣ���TCommanderIDϣ�������Լ���
 *      Ȩ�޵ȼ���ʱ��Ӧ���ṩ��ӦȨ�޵ȼ������롣������ಢ��Ҫ����������Ϊ������������
 *      ���ϵ����⡣����౻�������ʹ�����ṩ���������Ȩ�޵�֧�֣���Ӧ��ֱ�ӱ�¶�����
 *      ���ߣ��������ǿ����������ṩ�ķ���"����"���ƻ�Ȩ�����ã���
 * 		5��Ĭ��״̬�£�Ҳ���������û���ֶ����ã����е�TCommanderID��TCommandHead��������
 * 		����ǰ��û�м��������ᴦ��"��Ȩ�޵ȼ�"��״̬�¡�Ҳ����˵���е�������߶��ᱻ��Ϊ
 * 		����"��Ȩ��״̬��"�����е�������º�Σ�գ�Ҳ���Ǳ���Ϊ��"��Ȩ�޵ȼ�"�����
 * 
 * 3������Ȩ�����Ͽ⡣���õĲ��裺
 * 		1���ڹ��췽���У�����Ҫ����һ��PasswordTank����Ϊ����������������û
 *      ��ʲô�ϸ�İ�ȫ��Ҫ������ʹ��SimplePasswordTank
 *      2������Ȩ�޵ȼ��������룬�ڴ������Ѿ�������Ӧ���������еķ�����������
 *      �˲�������
 *      3����Ҫ��Ȩ�޵ȼ�������ָ����Ȩ�޵ȼ�
 *      4����ӵ��Ȩ�޵ȼ����������ָ����Ȩ�޵ȼ�
 * 
 *
 * ==========================================================
 * ע�⣺
 * 1��TLevelName������к��ʵ�equals��hashCode��comareTo����
 * 2��TPassword������к��ʵ�equals����
 * 3��TCommanderID������к��ʵ�equals��hashCode��comareTo����
 * 4��TCommandHead������к��ʵ�equals��hashCode��comareTo����
 * ==========================================================
 * 
 */
public class TypedAuthorityBase<TCommanderID, TCommandHead, TLevelName, TPassword>
implements ITypedAuthorityBaseViewer<TCommanderID, TCommandHead, TLevelName, TPassword>, 
ITypedAuthorityBaseConfigurer<TCommanderID, TCommandHead, TLevelName, TPassword>
{
	/**
	 * ��¼����ӵ��ĳ��Ȩ�޵ȼ���TCommanderID���Լ����Ӧ��Ȩ�޵ȼ�
	 */
	private Map<TCommanderID, TLevelName> commanderMap;
	/**
	 * ��¼����Ҫ��ĳ��Ȩ�޵ȼ���TCommandHead���Լ����Ӧ��Ȩ�޵ȼ�
	 */
	private Map<TCommandHead, TLevelName> commandMap;
	/**
	 * ��¼�͹���Ȩ�޵ȼ������Ӧ�����롣
	 */
	private TypedAuthorityLevelManager<TLevelName, TPassword> levelManager;
	
	/**
	 * @param passwordManager ָ��ʹ�ú���PasswordTank���洢�͹�������
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
	
	///////////////////////////////����Ȩ�޵ȼ���������////////////////////////////////
	
	/**
	 * ����һ���µ�Ȩ�޵ȼ���ΪĿǰ��ߵ�Ȩ�޵ȼ���ʵ�������������Ҫ��
	 * ʹ���ߴӵ͵������μ���Ȩ�޵ȼ���
	 * ���levelName�͹�ȥĳ��Ȩ�޵ȼ��ظ������������ʲôҲ����������
	 * ��ʧ�ܡ�
	 * 
	 * @param levelName �µ�Ȩ�޵ȼ�
	 * @param password �µ�Ȩ�޵ȼ�������
	 * @return �Ƿ�ɹ��ؼ������µ�Ȩ�޵ȼ���
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
	 * ɾ����ߵ��Ǹ�Ȩ�޵ȼ�������ӵ�����Ȩ�޵ȼ���TCommanderID��TCommandHead
	 * ���ᱻ�½�һ��Ȩ�޵ȼ������������ɾ����Ȩ�޵ȼ��������Ȩ�޵ȼ���������
	 * TCommanderID��TCommandHead�ͻᱻɾ��Ȩ�޵ȼ�������Ϊ��Ȩ�޵ȼ���
	 * 
	 * ���û�����Ȩ�޵ȼ�������ʱ��û���κ�Ȩ�޵ȼ�����ʲôҲ����
	 * 
	 * @return ��ɾ�������Ȩ�޵ȼ������û���򷵻�null
	 * @throws AuthorityBaseOperatingException 
	 */
	public synchronized TLevelName removeHighestLevel() throws AuthorityBaseOperatingException
	{		
		try
		{
			// ����Ƿ�����ߵȼ���Ȩ�ޣ���ɾ�����Ȩ�޵ȼ�
			final TLevelName highest = levelManager.removeHighestLevel();
			if (null == highest)
			{
				return null;
			}

			// �ҳ�Ӧ�ý�����ʲôȨ�޵ȼ���������Ȩ�޵ȼ�
			TLevelName newLevel = levelManager.getHighestLevel();
			
			// ����CommandMap���ҳ���Ҫ�ı��TCommandHead������֮
			LinkedList<TCommandHead> commandHeadsToModify=new LinkedList<TCommandHead>();
			for (TCommandHead command : commandMap.keySet())
			{
				assert (command != null); // �����ϲ�Ӧ�÷���������
				if (commandMap.get(command).equals(highest))
				{
					commandHeadsToModify.add(command);
				}
			}
			
			//�޸���Ҫ���ĵ�TCommandHead
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

			// ����CommanderMap���ҳ���Ҫ�ı��TCommanderID������֮
			LinkedList<TCommanderID> commandersToModify=new LinkedList<TCommanderID>();
			for (TCommanderID commander : commanderMap.keySet())
			{
				assert (commander != null); // �����ϲ�Ӧ�÷���������
				if (commanderMap.get(commander).equals(highest))
				{
					commandersToModify.add(commander);
				}
			}
			
			//�޸���Ҫ���ĵ�TCommandHead
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
	 * �޸�ĳ��Ȩ�޵ȼ�������
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
	 * ��֤�����levelName�ʹ����password�Ƿ�ƥ�䡣
	 * ƥ����ָ��
	 * 1��levelName��password����Ϊnull
	 * 2��levelName����ͨ��addHighestLevel(TLevelName, TPassword)�����������
	 * ����û��ͨ��removeHighestLevel()����ɾ��
	 * 3��ͨ��addHighestLevel(TLevelName, TPassword)��
	 * changeLevelPassword(TLevelName, TPassword)��levelNameָ�������룬�봫��
	 * ������password����equals������֤��ȡ�
	 * 
	 * @return ���ƥ���򷵻�true�����򷵻�false
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
	 * ����Ƿ�����ָ����Ȩ�޵ȼ���û��ɾ���������и�Ȩ�޵ȼ���
	 * ���ܴ���null
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
	 * �Ƚ�����Ȩ�޵ȼ��ĵȼ��ߵͣ����ܴ���null��
	 * ��Java��compare����������ͬ
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
	 * �Ƚ�Ȩ�޵ĸߵͣ���ȿ��Դ���TLevelName�������Ƚ�
	 * ����Ȩ�޵ȼ����ֿ��Դ���null����ʾû��Ȩ�޵ȼ�����Ȩ�޵ȼ�����
	 * Ȩ�޵ȼ���ȡ�
	 * 
	 * ����ֵ��Java��Compare�����Ĺ淶��ͬ��
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
	 * ���ذ��������е�����Ȩ�޵ȼ���������û�п���λ�á�
	 * ���û��Ȩ�޵ȼ����򷵻س���Ϊ������������null��
	 * ���⣬������в��ᱣ�����ص���������ã�������
	 * ���޸�������顣
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
	 * ����һ���ж��ٸ�Ȩ�޵ȼ� 
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
	
	//////////////////////////��Ҫ��Ȩ�޵ȼ�������ָ����Ȩ�޵ȼ�///////////////////////////
	
	/**
	 * ��commandHead���������Ȩ�޵ȼ��趨ΪlevelName
	 * @param commandHead ��Ҫ���趨������
	 * @param levelName commandHead���趨�ɵ�Ȩ�޵ȼ�
	 * @return commandHead��ǰ��Ȩ�޵ȼ������commandHead��ǰ������Ȩ�޵ȼ�״̬����ô����null
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
	 * ��commandHead��Ȩ�޵ȼ�ɾ��������������Ϊ��Ȩ�޵ȼ�
	 * @return commandHead��ǰ��Ȩ�޵ȼ������commandHead��ǰ������Ȩ�޵ȼ�״̬����ô����null
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
	 * �����趨commandHead���������Ȩ�ޡ����levelName��=null�������趨ΪlevelName
	 * ��Ӧ��Ȩ�޵ȼ����������趨Ϊ��Ȩ�޵ȼ���
	 * @param commandHead ��Ҫ���趨������
	 * @param levelName commandHead���趨�ɵ�Ȩ�޵ȼ�
	 * @return commandHead��ǰ��Ȩ�޵ȼ������commandHead��ǰ������Ȩ�޵ȼ�״̬����ô����null
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
	 * ��ѯ�����commandHeadҪ��ʲôȨ�޲ſ��Ա����У����commandHead��Ȩ�޵ȼ���
	 * �򷵻�null��
	 * @return commandHeadҪ���Ȩ�޵ȼ������commandHead��Ȩ�޵ȼ����򷵻�null
	 */
	public synchronized TLevelName findRequiredAuthority(TCommandHead commandHead)
	{
		if(null==commandHead)
		{
			throw new NullPointerException("commandHead should not be null");
		}
		
		return commandMap.get(commandHead);
	}
	
	///////////////////��ӵ��Ȩ�޵ȼ����������ָ����Ȩ�޵ȼ�///////////////////////////
	
	/**
	 * ��commanderID��Ȩ�޵ȼ��趨ΪlevelName
	 * @return commanderID��ǰ��Ȩ�޵ȼ������commanderID��ǰ������Ȩ�޵ȼ�״̬����ô����null
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
	 * ��commanderID��Ȩ�޵ȼ�ɾ��������������Ϊ��Ȩ�޵ȼ�
	 * @return commanderID��ǰ��Ȩ�޵ȼ������commanderID��ǰ������Ȩ�޵ȼ�״̬����ô����null
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
	 * �����趨commanderID��Ȩ�ޡ����levelName��=null�������趨ΪlevelName
	 * ��Ӧ��Ȩ�޵ȼ����������趨Ϊ��Ȩ�޵ȼ���
	 * @param commanderID ��Ҫ���趨������
	 * @param levelName commandHead���趨�ɵ�Ȩ�޵ȼ�
	 * @return commanderID��ǰ��Ȩ�޵ȼ������commanderID��ǰ������Ȩ�޵ȼ�״̬����ô����null
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
	 * ��ѯ�����commanderIDӵ��ʲô����Ȩ�ޡ����commanderID��Ȩ�޵ȼ����򷵻�null��
	 * @return commanderIDӵ�е�Ȩ�޵ȼ������commanderID��Ȩ�޵ȼ����򷵻�null
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
