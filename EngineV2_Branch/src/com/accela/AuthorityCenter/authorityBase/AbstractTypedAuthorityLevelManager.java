package com.accela.AuthorityCenter.authorityBase;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * Ȩ�޵ȼ�(level��authority level)�Ĵ洢�͹�������֧�ֶ��̡߳�
 * ����������洢�͹������Ȩ�޵ȼ��Լ���Ȩ�޵ȼ���Ӧ�����롣
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
 * //Inheritance needed
 */
public abstract class AbstractTypedAuthorityLevelManager<TLevelName, TPassword>
{
	/**
	 * ����һ��Ȩ�޵ȼ������Ȩ�޵ȼ�����Ϊ��ߵ�Ȩ�޵ȼ����������
	 * levelName���Ѿ��е��ظ������������ʲôҲ������������false��
	 * 
	 * ��ʵ���������Ҫ��ʹ���ߴӵ͵���һ��һ���������µ�Ȩ�޵ȼ���
	 * 
	 * @param levelName �µ�Ȩ�޵ȼ������֣�����Ϊnull
	 * @param password ��Ȩ�޵ȼ������룬����Ϊnull
	 * @return ��ʾ�����Ƿ�ɹ���ʵ�������levelName�����Ѿ��е�Ȩ
	 * �޵ȼ��ظ����򷵻�true�����򷵻�false
	 */
	public synchronized boolean addHighestLevel(TLevelName levelName, TPassword password) throws Exception
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		if(null==password)
		{
			throw new NullPointerException("password should not be null");
		}
		
		if(containsLevel(levelName))
		{
			return false;
		}
		else
		{
			addHighestLevelImpl(levelName, password);
			return true;
		}
		
	}
	
	/**
	 * addHighestLevel������ʵ�֣���addHighestLevel�����������
	 */
	protected abstract void addHighestLevelImpl(TLevelName levelName, TPassword password) throws Exception;
	
	/**
	 * ɾ��ָ����Ȩ�޵ȼ�
	 * @param levelName ָ����Ȩ�޵ȼ�������
	 * @return ���������ָ����Ȩ�޵ȼ����򷵻�false��������ڣ���ɾ��֮��������true
	 */
	public synchronized boolean removeLevel(TLevelName levelName) throws Exception
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		
		if(!containsLevel(levelName))
		{
			return false;
		}
		else
		{
			removeLevelImpl(levelName);
			return true;
		}
	}
	
	/**
	 * removeLevel������ʵ�֣���removeLevel�����������
	 */
	protected abstract void removeLevelImpl(TLevelName levelName) throws Exception;
	
	/**
	 * �õ�ָ��Ȩ�޵ȼ�������
	 * @param levelName ָ����Ȩ�޵ȼ�������Ϊnull
	 * @return �������ָ����Ȩ�޵ȼ����򷵻������룬���򷵻�null
	 */
	public synchronized TPassword getPassword(TLevelName levelName) throws Exception
	{
		if(null==levelName)
		{
			throw new NullPointerException("leveName should not be null");
		}
		
		if(!containsLevel(levelName))
		{
			return null;
		}
		else
		{
			return getPasswordImpl(levelName);
		}
	}
	
	/**
	 * getPassword������ʵ�֣���getPassword�����������
	 */
	protected abstract TPassword getPasswordImpl(TLevelName levelName) throws Exception;
	
	/**
	 * �޸�ĳ��Ȩ�޵ȼ��Ķ�Ӧ����
	 * @param levelName ָ����Ȩ�޵ȼ�
	 * @param password �µ�����
	 * @return ���Ȩ�޵ȼ���ȥ������
	 */
	public synchronized TPassword changePassword(TLevelName levelName, TPassword password) throws Exception
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
			throw new IllegalArgumentException("levelName is not contained in the LevelTank");
		}
		
		return changePasswordImpl(levelName, password);
	}
	
	/**
	 * changePassword������ʵ�֣���changePassword�����������
	 */
	protected abstract TPassword changePasswordImpl(TLevelName levelName, TPassword password) throws Exception;
	
	/**
	 * ����Ƿ�LevelTank�Ѿ�������levelName��Ӧ��Ȩ�޵ȼ�
	 * @param levelName ���������levelName
	 * @return ���LevelTank�Ƿ��Ѿ�������levelName��Ӧ��Ȩ�޵ȼ�
	 */
	public synchronized boolean containsLevel(TLevelName levelName) throws Exception
	{
		if(null==levelName)
		{
			throw new NullPointerException("leveName should not be null");
		}
		
		return containsLevelImpl(levelName);
	}
	
	/**
	 * containsLevel������ʵ�֣���containsLevel����������룬
	 * ע������������в�Ҫ����getPassword��������������ѭ��
	 * ����
	 */
	protected abstract boolean containsLevelImpl(TLevelName levelName) throws Exception;
	
	/**
	 * 
	 * �������е�Ȩ�޵ȼ�levelName���ж��ٸ���ͬ��levelName��
	 * ����ж�󣬼�����û�п��ಿ�֡�
	 * ���LevelManager�ǿյģ���ôӦ�÷���sizeΪ��ı������null��
	 * ���ص�List<TLevelName>û�ж�Ԫ��˳��Ҫ��
	 * ���⣬���صı�Ӧ�ú�LevelManagerû���κι�ϵ����LevelManager���洢���������á�
	 * 
	 */
	public synchronized List<TLevelName> getAllLevels() throws Exception
	{
		List<TLevelName> names=getAllLevelsImpl();
		
		if(null==names)
		{
			assert(false);    //getAllLevelsImpl()��Ӧ�÷���null
		}
		
		//�Ϸ��Լ��
		for(TLevelName name : names)
		{
			if(null==name)
			{
				assert(false);    //��nullԪ��
			}
			for(TLevelName name_inner: names)
			{
				if(name!=name_inner
						&&name.equals(name_inner))
				{
					assert(false);	//����ͬԪ��
				}
			}
		}
		assert(names.size()==getSize());
		
		return names;
	}
	
	/**
	 * getAllLevels������ʵ�֣���getAllLevels����������룬
	 */
	protected abstract List<TLevelName> getAllLevelsImpl() throws Exception;
	
	/**
	 * �Ƚ�����Ȩ�޵ȼ��ĸߵ͡�
	 * @param leveNameA һ��Ȩ�޵ȼ���levelName������Ϊnull������Ӧ�ô�����LevelTank��
	 * @param levelNameB ��һ��Ȩ�޵ȼ���levelName������Ϊnull������Ӧ�ô�����LevelTank��
	 * 
	 * @return ���levelNameA�ĵȼ�����levelNameB���򷵻ش����������
	 * ���levelNameA�ĵȼ�����levelNameB���򷵻�С������������
	 * leveNameA�ĵȼ�����levelNameB�ĵȼ����򷵻��㡣
	 */
	public synchronized int compareLevel(TLevelName levelNameA, TLevelName levelNameB) throws Exception
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
					"levelNameA is not contained in the LevelTank");
		}
		if (!containsLevel(levelNameB))
		{
			throw new IllegalArgumentException(
					"levelNameB is not contained in the LevelTank");
		}
		
		if(levelNameA.equals(levelNameB))
		{
			return 0;
		}
		
		if(isAHigherThanBImpl(levelNameA, levelNameB))
		{
			return 1;
		}
		else if(isAHigherThanBImpl(levelNameB, levelNameA))
		{
			return -1;
		}
		else
		{
			//�����������Ӧ�÷�������ΪLevelTank�в�Ӧ��������Ȩ�޵ȼ���ȵ�,
			//���ǻ�����ͬ������Ȩ�޵ȼ�
			assert(false);  
			return 0;
		}
	}
	
	/**
	 * compareLevel������ʵ�֣���compareLevel����������롣
	 * ��������Ƚ�levelNameA��levelNameB�ĸ�Ȩ�޵ȼ����ߡ�
	 * levelNameA����levelNameB�򷵻�true�����ڻ���С���򷵻�false��
	 */
	protected abstract boolean isAHigherThanBImpl(TLevelName levelNameA, TLevelName levelNameB) throws Exception;
	
	/**
	 * ��֤�����levelName��password�����Ѿ��洢��levelName�����Ӧ��password
	 * �Ƿ�һ��(��equals����)�����⣬���levelName��password���κ�һ��Ϊnull��
	 * ��᷵��false�����levelNameָ���ĵȼ������ڣ��򷵻�false��
	 */
	public synchronized boolean verify(TLevelName levelName, TPassword password) throws Exception
	{
		if(null==levelName||null==password)
		{
			return false;
		}
		if(!containsLevel(levelName))
		{
			return false;
		}
		
		return verifyImpl(levelName, password);
	}
	
	/**
	 * verify������ʵ�֣���verify����������룬
	 */
	protected abstract boolean verifyImpl(TLevelName levelName, TPassword password) throws Exception;

	/**
	 * ���levelName�Ƿ������Ȩ�޵ȼ��ġ�	
	 * @param levelName �������Ȩ�޵ȼ������֣�����Ϊnull
	 * @return levelName�Ƿ������Ȩ�޵ȼ��������leveName����LevelTank�У���һ������false
	 */
	public synchronized boolean isHighestLevel(TLevelName levelName) throws Exception
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		if(!containsLevel(levelName))
		{
			return false;
		}
		
		return levelName.equals(getHighestLevel());
	}
	
	/**
	 * ���levelName�Ƿ������Ȩ�޵ȼ��ġ�	
	 * @param levelName �������Ȩ�޵ȼ������֣�����Ϊnull
	 * @return levelName�Ƿ������Ȩ�޵ȼ��������leveName����LevelTank�У���һ������false
	 */
	public synchronized boolean isLowestLevel(TLevelName levelName) throws Exception
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		if(!containsLevel(levelName))
		{
			return false;
		}
		
		return levelName.equals(getLowestLevel());
	}
	
	/**
	 * ���levelName�Ƿ񼴲������Ȩ�޵ȼ���Ҳ�������Ȩ�޵ȼ������е�Ȩ�޵ȼ���
	 * @param levelName �������Ȩ�޵ȼ������֣�����Ϊnull
	 * @return levelName�Ƿ񼴲������Ȩ�޵ȼ���Ҳ�������Ȩ�޵ȼ��������leveName����LevelTank�У���һ������false
	 */
	public synchronized boolean isMiddleLevel(TLevelName levelName) throws Exception
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		if(!containsLevel(levelName))
		{
			return false;
		}
		
		return !levelName.equals(getHighestLevel())&&!levelName.equals(getLowestLevel());
	}
	
	/**
	 * ���ؾ������Ȩ�޵ȼ���TLevelName���������LevelTank�ǿյģ�
	 * �򷵻�null��
	 */
	public synchronized TLevelName getHighestLevel() throws Exception
	{
		List<TLevelName> names=getAllLevels();
		if(0==names.size())
		{
			return null;
		}
		
		TLevelName highest=names.get(0);
		for(TLevelName name : names)
		{
			if(compareLevel(name, highest)>0)
			{
				highest=name;
			}
		}
		
		assert(highest!=null);
		return highest;
	}
	
	/**
	 * ���ؾ������Ȩ�޵ȼ���TLevelName���������LevelTank�ǿյģ�
	 * �򷵻�null��
	 */
	public synchronized TLevelName getLowestLevel() throws Exception
	{
		List<TLevelName> names=getAllLevels();
		assert(names!=null);
		if(0==names.size())
		{
			return null;
		}
		
		TLevelName lowest=names.get(0);
		for(TLevelName name : names)
		{
			if(compareLevel(name, lowest)<0)
			{
				lowest=name;
			}
		}
		
		assert(lowest!=null);
		return lowest;
	}
	
	/**
	 * ɾ����ߵ�Ȩ�޵ȼ���������LevelTank�ǿյģ���û�����Ȩ�޵ȼ���
	 * ��ôʲôҲ������
	 * 
	 * @return ��ɾ�������Ȩ�޵ȼ������û���򷵻�null
	 */
	public synchronized TLevelName removeHighestLevel() throws Exception
	{
		TLevelName highest=getHighestLevel();
		if(highest!=null)
		{
			removeLevel(highest);
		}
		
		return highest;
	}
	
	/**
	 * ���LevelTank
	 */
	public synchronized void clear() throws Exception
	{
		for(TLevelName name : getAllLevels())
		{
			removeLevel(name);
		}
	}
	
	/**
	 * ���LevelTank�Ƿ��ǿյģ�������û�м����κ�Ȩ�޵ȼ� 
	 */
	public synchronized boolean isEmpty() throws Exception
	{
		return 0==getAllLevels().size();
	}
	
	/**
	 * @return ���������е�����Ȩ�޵ȼ������顣�ж��ٸ���ͬ��levelName��
	 * ������ж�󣬼�����û�п��ಿ�֡����LevelTank�ǿյģ���ôӦ��
	 * ���س���Ϊ������������null�����⣬���ص�����Ӧ�ú�LevelTankû
	 * �й�ϵ����LevelTank���洢�����������á�
	 */
	public synchronized List<TLevelName> getIncSortedAllLevels() throws Exception
	{
		List<TLevelName> names=getAllLevels();
		ArrayList<TLevelName> array=new ArrayList<TLevelName>();
		array.addAll(names);
		selectionSort(array);
		
		//�Ϸ��Լ��
		for(TLevelName name : array)
		{
			if(null==name)
			{
				assert(false);    //��nullԪ��
			}
			for(TLevelName name_inner: array)
			{
				if(name!=name_inner
						&&name.equals(name_inner))
				{
					assert(false);	//����ͬԪ��
				}
			}
		}
		assert(array.size()==getSize());
		
		for(int i=0;i<array.size();i++)
		{
			for(int i_inner=0;i_inner<array.size();i_inner++)
			{
				if(i_inner==i)
				{
					assert(compareLevel(array.get(i), array.get(i_inner))==0);
				}
				else if(i_inner<i)
				{
					assert(compareLevel(array.get(i), array.get(i_inner))>0);
				}
				else if(i_inner>i)
				{
					assert(compareLevel(array.get(i), array.get(i_inner))<0);
				}
				else
				{
					assert(false);
				}
					
			}
		}
		
		return array;
	}
	
	/**
	 * �����鰴�������� 
	 */
	private void selectionSort(ArrayList<TLevelName> array) throws Exception
	{
		for(int i=0;i<array.size();i++)
		{
			int minIdx=i;
			for(int j=i+1;j<array.size();j++)
			{
				if(compareLevel(array.get(j), array.get(minIdx))<0)
				{
					minIdx=j;
				}
			}
			TLevelName temp=array.set(i, array.get(minIdx));
			array.set(minIdx, temp);
		}
	}
	
	/**
	 * ���ұ�ָ����levelNameֻ��һ����Ȩ�޵ȼ���Ҳ����˵�����ص�TLevelName�������㣺
	 * 1�����ĵȼ����ڴ������levelName
	 * 2��LevelTank�в�����һ��Ȩ�޵ȼ�����������1���ұȷ��ص�TLevelNameȨ�޵ȼ�����
	 * 
	 * @param levelName ָ����levelName
	 * @return ��levelNameֻ��һ���ȼ���Ȩ�޵ȼ������û���򷵻�null
	 */
	public synchronized TLevelName findAdjacentHigherLevel(TLevelName levelName) throws Exception
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		if(!containsLevel(levelName))
		{
			throw new IllegalArgumentException("levelName should be contained by the LevelTank");
		}
		
	    List<TLevelName> names=getAllLevels();
	    
	    TLevelName result=null;
	    for(TLevelName name : names)
	    {
	    	if(compareLevel(name, levelName)<=0)
	    	{
	    		continue;
	    	}
	    	
	    	if(null==result)
	    	{
	    		result=name;
	    	}
	    	else
	    	{
	    		if(compareLevel(name, result)<0)
	    		{
	    			result=name;
	    		}
	    		else if(compareLevel(name, result)==0)
	    		{
	    			assert(false);		//getAllLevels()�в�Ӧ������equals()��ȵ�Ԫ��
	    		}
	    	}
	    }
	    
	    return result;
	}
	
	/**
	 * ���ұ�ָ����levelNameֻ��һ����Ȩ�޵ȼ���Ҳ����˵�����ص�TLevelName�������㣺
	 * 1�����ĵȼ����ڴ������levelName
	 * 2��LevelTank�в�����һ��Ȩ�޵ȼ�����������1���ұȷ��ص�TLevelNameȨ�޵ȼ�����
	 * 
	 * @param levelName ָ����levelName
	 * @return ��levelNameֻ��һ���ȼ���Ȩ�޵ȼ������û���򷵻�null
	 */
	public synchronized TLevelName findAdjacentLowerLevel(TLevelName levelName) throws Exception
	{
		if(null==levelName)
		{
			throw new NullPointerException("levelName should not be null");
		}
		if(!containsLevel(levelName))
		{
			throw new IllegalArgumentException("levelName should be contained by the LevelTank");
		}
		
		List<TLevelName> names=getAllLevels();
	    
	    TLevelName result=null;
	    for(TLevelName name : names)
	    {
	    	if(compareLevel(name, levelName)>=0)
	    	{
	    		continue;
	    	}
	    	
	    	if(null==result)
	    	{
	    		result=name;
	    	}
	    	else
	    	{
	    		if(compareLevel(name, result)>0)
	    		{
	    			result=name;
	    		}
	    		else if(compareLevel(name, result)==0)
	    		{
	    			assert(false);		//getAllLevels()�в�Ӧ������equals()��ȵ�Ԫ��
	    		}
	    	}
	    }
	    
	    return result;
	}
	
	/**
	 * @return ���ص�ǰ�洢��levelName��
	 */
	public synchronized int getSize() throws Exception
	{
		return getSizeImpl();
	}
	
	/**
	 * getSize������ʵ�֣���getSize����������룬
	 * ע�ⲻҪ����getAllLevels()��������������ѭ������
	 */
	protected abstract int getSizeImpl() throws Exception;
	
}
