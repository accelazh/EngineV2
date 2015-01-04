package com.accela.AuthorityCenter.authorityBase;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 * 权限等级(level、authority level)的存储和管理器，支持多线程。
 * 这个类用来存储和管理各个权限等级以及与权限等级对应的密码。
 * 
 * 每个权限等级都具有其对应的名字TLevelName和密码TPassword，
 * 互相依靠TLevelName唯一地区分开。你可以逐级地加入和删除各
 * 个权限等级，以及核对密码。
 * 
 * TLevelName用来指明权限等级的名字，但实际上用TLevelName已
 * 经能够把权限等级唯一地区分开。权限等级的名字就可以代表权限
 * 等级，因此这里只需要权限等级的名字就够了。而权限等级这个对
 * 象在这个类里都没有出现过。
 * 
 * 这个类不维护密码安全，因此你可以看到一些直接暴露了密码的方
 * 法。它的使用者（不单指继承者）应该决定哪些方法可以暴露给外
 * 部，那些方法只能留给内部配置来用。
 * 
 * 这个类提供LevelTank的一些帮助和方便因此用Abstract开头
 *
 * @param TLevelName 权限等级的名字的类型，权限等级依靠它唯一
 * 地区分开。注意它必须具有合适的equals、hashCode和comareTo方法
 * @param <TPassword> 密码的类型，注意它必须具有合适的equals方法
 * 
 * ==========================================================
 * 注意：
 * 1、TLevelName必须具有合适的equals、hashCode和comareTo方法
 * 2、TPassword必须具有合适的equals方法
 * ==========================================================
 * 
 * //Inheritance needed
 */
public abstract class AbstractTypedAuthorityLevelManager<TLevelName, TPassword>
{
	/**
	 * 增加一个权限等级，这个权限等级将作为最高的权限等级。但是如果
	 * levelName和已经有的重复，则这个方法什么也不做，并返回false。
	 * 
	 * 事实上这个方法要求使用者从低到高一个一个地增加新的权限等级。
	 * 
	 * @param levelName 新的权限等级的名字，不能为null
	 * @param password 该权限等级的密码，不能为null
	 * @return 表示操作是否成功。实际上如果levelName不和已经有的权
	 * 限等级重复，则返回true，否则返回false
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
	 * addHighestLevel方法的实现，见addHighestLevel方法及其代码
	 */
	protected abstract void addHighestLevelImpl(TLevelName levelName, TPassword password) throws Exception;
	
	/**
	 * 删除指定的权限等级
	 * @param levelName 指定的权限等级的名字
	 * @return 如果不存在指定的权限等级，则返回false；如果存在，则删除之，并返回true
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
	 * removeLevel方法的实现，见removeLevel方法及其代码
	 */
	protected abstract void removeLevelImpl(TLevelName levelName) throws Exception;
	
	/**
	 * 得到指定权限等级的密码
	 * @param levelName 指定的权限等级，不能为null
	 * @return 如果存在指定的权限等级，则返回其密码，否则返回null
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
	 * getPassword方法的实现，见getPassword方法及其代码
	 */
	protected abstract TPassword getPasswordImpl(TLevelName levelName) throws Exception;
	
	/**
	 * 修改某个权限等级的对应密码
	 * @param levelName 指定的权限等级
	 * @param password 新的密码
	 * @return 这个权限等级过去的密码
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
	 * changePassword方法的实现，见changePassword方法及其代码
	 */
	protected abstract TPassword changePasswordImpl(TLevelName levelName, TPassword password) throws Exception;
	
	/**
	 * 检查是否LevelTank已经含有了levelName对应的权限等级
	 * @param levelName 将被检验的levelName
	 * @return 这个LevelTank是否已经含有了levelName对应的权限等级
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
	 * containsLevel方法的实现，见containsLevel方法及其代码，
	 * 注意在这个方法中不要调用getPassword方法，否则会造成循环
	 * 调用
	 */
	protected abstract boolean containsLevelImpl(TLevelName levelName) throws Exception;
	
	/**
	 * 
	 * 返回所有的权限等级levelName，有多少个不同的levelName，
	 * 表就有多大，即数组没有空余部分。
	 * 如果LevelManager是空的，那么应该返回size为零的表而不是null。
	 * 返回的List<TLevelName>没有对元素顺序要求。
	 * 另外，返回的表应该和LevelManager没有任何关系，即LevelManager不存储这个表的引用。
	 * 
	 */
	public synchronized List<TLevelName> getAllLevels() throws Exception
	{
		List<TLevelName> names=getAllLevelsImpl();
		
		if(null==names)
		{
			assert(false);    //getAllLevelsImpl()不应该返回null
		}
		
		//合法性检查
		for(TLevelName name : names)
		{
			if(null==name)
			{
				assert(false);    //有null元素
			}
			for(TLevelName name_inner: names)
			{
				if(name!=name_inner
						&&name.equals(name_inner))
				{
					assert(false);	//有相同元素
				}
			}
		}
		assert(names.size()==getSize());
		
		return names;
	}
	
	/**
	 * getAllLevels方法的实现，见getAllLevels方法及其代码，
	 */
	protected abstract List<TLevelName> getAllLevelsImpl() throws Exception;
	
	/**
	 * 比较两个权限等级的高低。
	 * @param leveNameA 一个权限等级的levelName，不能为null，并且应该存在于LevelTank中
	 * @param levelNameB 另一个权限等级的levelName，不能为null，并且应该存在于LevelTank中
	 * 
	 * @return 如果levelNameA的等级高于levelNameB，则返回大于零的数，
	 * 如果levelNameA的等级低于levelNameB，则返回小于零的数，如果
	 * leveNameA的等级等于levelNameB的等级，则返回零。
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
			//这种情况绝不应该发生，因为LevelTank中不应该有两个权限等级相等的,
			//但是互不相同的两个权限等级
			assert(false);  
			return 0;
		}
	}
	
	/**
	 * compareLevel方法的实现，见compareLevel方法及其代码。
	 * 这个方法比较levelNameA和levelNameB哪个权限等级更高。
	 * levelNameA高于levelNameB则返回true，等于或者小于则返回false。
	 */
	protected abstract boolean isAHigherThanBImpl(TLevelName levelNameA, TLevelName levelNameB) throws Exception;
	
	/**
	 * 验证传入的levelName和password，与已经存储的levelName和其对应的password
	 * 是否一致(用equals方法)。另外，如果levelName和password中任何一个为null，
	 * 则会返回false；如果levelName指定的等级不存在，则返回false。
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
	 * verify方法的实现，见verify方法及其代码，
	 */
	protected abstract boolean verifyImpl(TLevelName levelName, TPassword password) throws Exception;

	/**
	 * 检查levelName是否是最高权限等级的。	
	 * @param levelName 被检验的权限等级的名字，不能为null
	 * @return levelName是否是最高权限等级。但如果leveName不在LevelTank中，则一定返回false
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
	 * 检查levelName是否是最低权限等级的。	
	 * @param levelName 被检验的权限等级的名字，不能为null
	 * @return levelName是否是最高权限等级。但如果leveName不在LevelTank中，则一定返回false
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
	 * 检查levelName是否即不是最高权限等级，也不是最低权限等级，即中等权限等级。
	 * @param levelName 被检验的权限等级的名字，不能为null
	 * @return levelName是否即不是最高权限等级，也不是最低权限等级。但如果leveName不在LevelTank中，则一定返回false
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
	 * 返回具有最高权限等级的TLevelName。但是如果LevelTank是空的，
	 * 则返回null。
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
	 * 返回具有最低权限等级的TLevelName。但是如果LevelTank是空的，
	 * 则返回null。
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
	 * 删除最高的权限等级，如果这个LevelTank是空的，即没有最高权限等级，
	 * 那么什么也不做。
	 * 
	 * @return 被删除的最高权限等级，如果没有则返回null
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
	 * 清空LevelTank
	 */
	public synchronized void clear() throws Exception
	{
		for(TLevelName name : getAllLevels())
		{
			removeLevel(name);
		}
	}
	
	/**
	 * 检查LevelTank是否是空的，即里面没有加入任何权限等级 
	 */
	public synchronized boolean isEmpty() throws Exception
	{
		return 0==getAllLevels().size();
	}
	
	/**
	 * @return 按升序排列的所有权限等级的数组。有多少个不同的levelName，
	 * 数组就有多大，即数组没有空余部分。如果LevelTank是空的，那么应该
	 * 返回长度为零的数组而不是null。另外，返回的数组应该和LevelTank没
	 * 有关系，即LevelTank不存储这个数组的引用。
	 */
	public synchronized List<TLevelName> getIncSortedAllLevels() throws Exception
	{
		List<TLevelName> names=getAllLevels();
		ArrayList<TLevelName> array=new ArrayList<TLevelName>();
		array.addAll(names);
		selectionSort(array);
		
		//合法性检查
		for(TLevelName name : array)
		{
			if(null==name)
			{
				assert(false);    //有null元素
			}
			for(TLevelName name_inner: array)
			{
				if(name!=name_inner
						&&name.equals(name_inner))
				{
					assert(false);	//有相同元素
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
	 * 将数组按升序排列 
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
	 * 查找比指定的levelName只高一级的权限等级。也就是说，返回的TLevelName对象满足：
	 * 1、它的等级高于传入参数levelName
	 * 2、LevelTank中不存在一个权限等级，满足条件1，且比返回的TLevelName权限等级更低
	 * 
	 * @param levelName 指定的levelName
	 * @return 比levelName只高一个等级的权限等级，如果没有则返回null
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
	    			assert(false);		//getAllLevels()中不应该有用equals()相等的元素
	    		}
	    	}
	    }
	    
	    return result;
	}
	
	/**
	 * 查找比指定的levelName只低一级的权限等级。也就是说，返回的TLevelName对象满足：
	 * 1、它的等级低于传入参数levelName
	 * 2、LevelTank中不存在一个权限等级，满足条件1，且比返回的TLevelName权限等级更高
	 * 
	 * @param levelName 指定的levelName
	 * @return 比levelName只低一个等级的权限等级，如果没有则返回null
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
	    			assert(false);		//getAllLevels()中不应该有用equals()相等的元素
	    		}
	    	}
	    }
	    
	    return result;
	}
	
	/**
	 * @return 返回当前存储的levelName数
	 */
	public synchronized int getSize() throws Exception
	{
		return getSizeImpl();
	}
	
	/**
	 * getSize方法的实现，见getSize方法及其代码，
	 * 注意不要调用getAllLevels()方法，否则会造成循环调用
	 */
	protected abstract int getSizeImpl() throws Exception;
	
}
