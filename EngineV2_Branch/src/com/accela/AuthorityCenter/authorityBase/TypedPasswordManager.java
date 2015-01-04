package com.accela.AuthorityCenter.authorityBase;



/**
 * 
 * 密码的存储和管理器，支持多线程。
 * 这个类用来存储和管理密码。对于高安全性的需要，这不是个简单的事。
 * 实际上，每当提及密码，自然会问这是什么的密码，这里称这个
 * “什么”为name。即每个密码需要一个唯一的name来标识它。
 * 
 * 密码存储器自身是不能够维护密码安全的，它只是为实现密码安全
 * 机制提供一个方便的支持，因此里面有很多方法会直接暴露密码。
 * 这个类的使用者（不单指继承者）应该决定哪些方法只能给内部使
 * 用，哪些方法（比如verify(TName, TPassword)可以给外部使用）。
 *
 * @param <TName> 用来标识每一个密码的对象，可以是各种类型。
 * 注意TName必须具有合适的equals、hashCode和comareTo方法
 * @param <TPassword> 密码的类型，注意它必须具有合适的equals方法
 * 
 * ==========================================================
 * 注意：
 * 1、TName必须具有合适的equals、hashCode和comareTo方法
 * 2、TPassword必须具有合适的equals方法
 * ==========================================================
 * 
 * //Inheritance needed
 */
public abstract class TypedPasswordManager<TName, TPassword>
{
	/**
	 * 加入一条密码
	 * 
	 * @param name 这个密码是什么的的密码，不能为null
	 * @param password 密码，不能为null
	 * @return 如果过去有和这个name绑定的密码，那么返回之；如果没有，则返回null
	 */
	public synchronized TPassword put(TName name, TPassword password) throws Exception
	{
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		if(null==password)
		{
			throw new NullPointerException("password should not be null");
		}
		
		return putImpl(name, password);
	}
	
	/**
	 * put方法的实现，见put方法及其代码 
	 */
	protected abstract TPassword putImpl(TName name, TPassword password) throws Exception;
	
	/**
	 * 删除一条密码
	 * 
	 * @param name 这个密码的name，不能为null 
	 * @return 如果过去有和name绑定的密码，则返回之，如果没有，则返回null
	 */
	public synchronized TPassword remove(TName name) throws Exception
	{
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		return removeImpl(name);
	}
	
	/**
	 * remove方法的实现，见remove方法及其代码 
	 */
	protected abstract TPassword removeImpl(TName name) throws Exception;
	
	/**
	 * 取出name对应的密码
	 * 
	 * @param name 这个密码是什么的的密码，不能为null
	 * @return 如果有对应密码则返回之，否则返回null
	 */
	public synchronized TPassword get(TName name) throws Exception
	{
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		return getImpl(name);
	}
	
	/**
	 * get方法的实现，见get方法及其代码 
	 */
	protected abstract TPassword getImpl(TName name) throws Exception;
	
	/**
	 * 检查是否存在name对应的密码
	 * 
	 * @param name 这个密码是什么的的密码，不能为null
	 * @return 存在name对应的密码则返回true，反之返回false
	 */
	public synchronized boolean containsName(TName name) throws Exception
	{
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		return get(name)!=null;
	}
	
	/**
	 * 验证传入的name-password对和已经存储的name-password对是否一致(用equals方法)，
	 * 如果一致则返回true，否则返回false。如果name指定的密码不存在，也返回null。
	 * 这个方法可能需要一些安全处理，比如不能在比较后，把密码留在内存中等等。
	 * 
	 * @param name 这个密码是什么的的密码
	 * @param password 需要验证的密码，可能不是name对应的密码
	 * @return 密码是否匹配，但是如果name和password中至少有一个是null时，则返回false。
	 * 如果name指定的密码不存在，也返回false。
	 */
	public synchronized boolean verify(TName name, TPassword password) throws Exception
	{
		if(null==name||null==password)
		{
			return false;
		}
		if(!containsName(name))
		{
			return false;
		}
		
		return verifyImpl(name, password);
	}
	
	/**
	 * verify方法的实现，见verify方法及其代码 
	 */
	protected abstract boolean verifyImpl(TName name, TPassword password) throws Exception;

	/**
	 * 清空存储的所有password及name
	 */
	public synchronized void clear() throws Exception
	{
		clearImpl();
	}
	
	protected abstract void clearImpl() throws Exception;
	
	/**
	 * 检查是否为空，即没有加入任何name和password 
	 */
	public synchronized boolean isEmpty() throws Exception
	{
		return isEmptyImpl();
	}
	
	protected abstract boolean isEmptyImpl() throws Exception;
	
	/**
	 * @return 当前存储有的password的数量
	 */
	public synchronized int getSize() throws Exception
	{
		return getSizeImpl();
	}
	
	protected abstract int getSizeImpl() throws Exception;
	
	
	
	
}
