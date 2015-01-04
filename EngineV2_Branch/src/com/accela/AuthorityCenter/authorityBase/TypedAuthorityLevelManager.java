package com.accela.AuthorityCenter.authorityBase;

import java.util.*;

/**
 * 
 * AbstractLevelTank��ʵ�֡�
 * 
 * Ȩ�޵ȼ�(authority level, ����дΪlevel)�Ĵ洢�͹�������
 * ֧�ֶ��̡߳�����������洢�͹������Ȩ�޵ȼ��Լ���Ȩ�޵�
 * ����Ӧ�����롣
 * 
 * ÿ��Ȩ�޵ȼ����������Ӧ������TLevelName������TPassword��
 * ��������TLevelNameΨһ�����ֿ���������𼶵ؼ����ɾ����
 * ��Ȩ�޵ȼ����Լ��˶����롣
 * 
 * TLevelName����ָ��Ȩ�޵ȼ������֣���ʵ������TLevelName��
 * ���ܹ���Ȩ�޵ȼ�Ψһ�����ֿ���Ȩ�޵ȼ������־Ϳ��Դ���Ȩ��
 * �ȼ����������ֻ��ҪȨ�޵ȼ������־͹��ˡ���Ȩ�޵ȼ������
 * ����������ﶼû�г��ֹ���
 * 
 * ����಻ά�����밲ȫ���������Կ���һЩֱ�ӱ�¶������ķ�
 * ��������ʹ���ߣ�����ָ�̳��ߣ�Ӧ�þ�����Щ�������Ա�¶����
 * ������Щ����ֻ�������ڲ��������á�
 * 
 * ������ṩLevelTank��һЩ�����ͷ��������Abstract��ͷ
 *
 * @param TLevelName Ȩ�޵ȼ������ֵ����ͣ�Ȩ�޵ȼ�������Ψһ
 * �����ֿ���ע����������к��ʵ�equals��hashCode��comareTo����
 * @param <TPassword> ��������ͣ�ע����������к��ʵ�equals����
 * 
 * ==========================================================
 * ע�⣺
 * 1��TLevelName������к��ʵ�equals��hashCode��comareTo����
 * 2��TPassword������к��ʵ�equals����
 * ==========================================================
 *
 */
public class TypedAuthorityLevelManager<TLevelName, TPassword> extends AbstractTypedAuthorityLevelManager<TLevelName, TPassword>
{
	private List<TLevelName> levelList=new LinkedList<TLevelName>();
	private TypedPasswordManager<TLevelName, TPassword> passwordTank;
	
	/**
	 * @param passwordTank ָ��LevelTankʹ��ʲôPasswordTank
	 */
	public TypedAuthorityLevelManager(TypedPasswordManager<TLevelName, TPassword> passwordTank)
	{
		if(null==passwordTank)
		{
			throw new NullPointerException("passwordTank should not be null");
		}
		try
		{
			if (!passwordTank.isEmpty())
			{
				throw new IllegalArgumentException("passwordTank should be empty");
			}
		} catch (Exception ex)
		{
			throw new IllegalArgumentException("Something is wrong with passwordTank", ex);
		}
		
		this.passwordTank=passwordTank;
	}
	
	@Override
	protected void addHighestLevelImpl(TLevelName levelName, TPassword password) throws Exception
	{
		levelList.add(levelName);
		passwordTank.put(levelName, password);
	}
	@Override
	protected boolean containsLevelImpl(TLevelName levelName)
	{
		return levelList.contains(levelName);
	}
	
	@Override
	protected List<TLevelName> getAllLevelsImpl()
	{
		LinkedList<TLevelName> result=null;
		try
		{
			result=new LinkedList<TLevelName>();
			result.addAll(levelList);
		}catch(ClassCastException ex)
		{
			ex.printStackTrace();    //����쳣�����ϲ����ܷ���
			assert(false);
		}
		
		return result;
	}
	@Override
	protected TPassword getPasswordImpl(TLevelName levelName) throws Exception
	{
		return passwordTank.get(levelName);
	}
	@Override
	protected boolean isAHigherThanBImpl(TLevelName levelNameA,
			TLevelName levelNameB)
	{
		return levelList.indexOf(levelNameA)>levelList.indexOf(levelNameB);
	}
	@Override
	protected void removeLevelImpl(TLevelName levelName) throws Exception
	{
		levelList.remove(levelName);
		passwordTank.remove(levelName);
	}
	
	@Override
	protected boolean verifyImpl(TLevelName levelName, TPassword password) throws Exception
	{
		return passwordTank.verify(levelName, password);
	}

	@Override
	protected TPassword changePasswordImpl(TLevelName levelName,
			TPassword password) throws Exception
	{
		return passwordTank.put(levelName, password);
	}

	@Override
	protected int getSizeImpl()
	{
		return levelList.size();
	}
	
	

}
