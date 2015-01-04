package com.accela.AuthorityCenter.changeManager;

import com.accela.AuthorityCenter.authorityBase.AuthorityBaseOperatingException;
import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseConfigurer;
import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * 
 * �ṩ����У��ı��Ȩ�޵ķ���
 * 
 */
public class AuthorityChangeManager implements IAuthorityChanger
{

	/**
	 * �����������һ���������commanderIDϣ������ĳ��Ȩ�޵ȼ��������ʱ��
	 * ���ҽ�������Ȩ�޵ȼ����ڸ�levelName��ʱ�򣬲��ܸ�������
	 * 
	 * @return �Ƿ�ɹ���������
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
	 * �����������һ���������ϣ�������Լ���Ȩ�޵ȼ���ʱ������Ҫ����
	 * �Լ���commanderID���Լ�ϣ����Ȩ�޵ȼ�levelName�����Ȩ�޵ȼ����� ��password��
	 * 
	 * ���ҽ�����Щ���������ʱ�����������߲��ܳɹ��ﵽĿ�ģ� 
	 * 1�������������Ϊnull
	 * 2��levelName�������뵽AuthorityBase�У�����û�б�ɾ�� 
	 * 3��levelName����commanderID���е�Ȩ�޵ȼ�
	 * 4��password��levelName��Ӧ�����뾭equals�����������
	 * 
	 * @return �Ƿ�ɹ�����Ȩ��
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
	 * �����������һ���������ϣ�������Լ���Ȩ�޵�ʱ������Ҫ����
	 * �Լ���commanderID���Լ�ϣ����Ȩ�޵ȼ�levelName�����Ȩ�޵ȼ����� ��password��
	 * 
	 * ���ҽ�����Щ���������ʱ�����������߲��ܳɹ��ﵽĿ�ģ� 
	 * 1�������������Ϊnull
	 * 2��levelName�������뵽AuthorityBase�У�����û�б�ɾ�� 
	 * 3��levelName����commanderID���е�Ȩ�޵ȼ�
	 * 4��password��levelName��Ӧ�����뾭equals�����������
	 * 
	 * @return �Ƿ�ɹ�����Ȩ��
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
	 * �����������һ���������ϣ�������Լ���Ȩ�޵ȼ���ʱ������Ҫ����
	 * �Լ���TCommanderID��commanderID���Լ�ϣ����Ȩ�޵ȼ�levelName��
	 * 
	 * ���ҽ�����Щ���������ʱ�����������߲��ܳɹ��ﵽĿ�ģ� 
	 * 1�������������Ϊnull
	 * 2��levelName�������뵽AuthorityBase�У�����û�б�ɾ�� 
	 * 3��levelName����commanderID���е�Ȩ�޵ȼ�
	 * 
	 * @return �Ƿ�ɹ�����Ȩ�޵ȼ�
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
	 * �����������һ���������ϣ�����Լ���������Ȩ�޵ȼ���ʱ�� ����Ҫ�����Լ���TCommanderID��commanderID��
	 * 
	 * ���ҽ�����Щ���������ʱ�����������߲��ܳɹ��ﵽĿ�ģ� 
	 * 1�������������Ϊnull 
	 * 2��commanderID������Ȩ�޵ȼ�
	 * 
	 * @return �Ƿ�ɹ���������Ȩ�޵ȼ�
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
	 * �����������һ���������ϣ�������Լ���Ȩ�޵�ʱ������Ҫ����
	 * �Լ���TCommanderID��commanderID���Լ�ϣ����Ȩ�޵ȼ�levelName��
	 * ��levelNameΪnullʱ����ɾ��commanderID��Ȩ�޵ȼ������������� ��Ȩ�޵ȼ���״̬
	 * 
	 * ���ҽ�����Щ���������ʱ�����������߲��ܳɹ��ﵽĿ�ģ� 
	 * 1���������������levelName�ⲻΪnull
	 * 2��levelName�������뵽AuthorityBase�У�����û�б�ɾ�� ������levelName==null
	 * 3��levelName����commanderID���е�Ȩ�޵ȼ�������levelName==nullʱ��commanderID
	 * ������Ȩ�޵ĵȼ�
	 * 
	 * @return �Ƿ�ɹ�����Ȩ�޵ȼ�
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
	 * �����������һ���������selfCommanderID��ϣ���ı���һ���������
	 * otherCommanderID��Ȩ�޵ȼ���ʱ������Ҫ�����Լ���TCommanderID��
	 * selfCommanderID�����ı��ߵ�TCommanderID��otherCommanderID���Լ�
	 * ��ϣ����otherCommanderID�ı䵽��Ȩ�޵ȼ�levelName��
	 * 
	 * ���ҽ�����Щ���������ʱ�����������߲��ܳɹ��ﵽĿ�ģ� 
	 * 1�������������Ϊnull
	 * 2��levelName�������뵽AuthorityVerifier�У�����û�б�ɾ��
	 * 3��levelName���ڻ����selfCommanderID��Ȩ�޵ȼ�
	 * 4��otherCommanderID��Ȩ�޵ȼ�����selfCommanderID��Ȩ�޵ȼ�������
	 * otherCommanderIDû��Ȩ�޵ȼ���selfCommanderID��
	 * 5��otherCommanderID��Ȩ�޵ȼ�������levelName
	 * 
	 * @return selfCommanderID��������Ƿ�ɹ��ﵽĿ��
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
	 * �����������һ���������selfCommanderID��ϣ������һ���������
	 * otherCommanderID��������Ȩ�޵ȼ���ʱ������Ҫ�����Լ���TCommanderID
	 * ��selfCommanderID�ͱ��ı��ߵ�TCommanderID��otherCommanderID��
	 * 
	 * ���ҽ�����Щ���������ʱ�����������߲��ܳɹ��ﵽĿ�ģ� 
	 * 1�������������Ϊnull
	 * 2��otherCommanderID��Ȩ�޵ȼ�����selfCommanderID��Ȩ�޵ȼ�������
	 * otherCommanderIDû��Ȩ�޵ȼ���selfCommanderID�� 
	 * 3��otherCommanderID������Ȩ�޵ȼ�
	 * 
	 * @return selfCommanderID��������Ƿ�ɹ��ﵽĿ��
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
	 * �����������һ���������selfCommanderID��ϣ���ı���һ���������
	 * otherCommanderID��Ȩ�޵�ʱ������Ҫ�����Լ���TCommanderID��
	 * selfCommanderID�����ı��ߵ�TCommanderID��otherCommanderID���Լ�
	 * ��ϣ����otherCommanderID�ı䵽��Ȩ�޵ȼ�levelName�����levelName
	 * Ϊnull�����ʾ��otherCommanderID��Ϊ��Ȩ�޵ȼ���
	 * 
	 * ���ҽ�����Щ���������ʱ�����������߲��ܳɹ��ﵽĿ�ģ� 
	 * 1�����������levelName�ⶼ��Ϊnull
	 * 2��selfCommanderID���ܺ�otherCommanderID��ͬ
	 * 2��levelName�������뵽AuthorityVerifier�У�����û�б�ɾ��������levelName��null
	 * 3��levelName���ڻ����selfCommanderID��Ȩ�޵ȼ�
	 * 4��otherCommanderID��Ȩ�޵ȼ�����selfCommanderID��Ȩ�޵ȼ�������
	 * otherCommanderIDû��Ȩ�޵ȼ���selfCommanderID��
	 * 5��otherCommanderID��Ȩ�޵ȼ�������levelName�����ߵ�levelName==nullʱ��
	 * otherCommanderID������Ȩ�޵ȼ�
	 * 
	 * @return selfCommanderID��������Ƿ�ɹ��ﵽĿ��
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
