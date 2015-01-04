package com.accela.AuthorityCenter.authorityBase;



/**
 * 
 * ����Ĵ洢�͹�������֧�ֶ��̡߳�
 * ����������洢�͹������롣���ڸ߰�ȫ�Ե���Ҫ���ⲻ�Ǹ��򵥵��¡�
 * ʵ���ϣ�ÿ���ἰ���룬��Ȼ��������ʲô�����룬��������
 * ��ʲô��Ϊname����ÿ��������Ҫһ��Ψһ��name����ʶ����
 * 
 * ����洢�������ǲ��ܹ�ά�����밲ȫ�ģ���ֻ��Ϊʵ�����밲ȫ
 * �����ṩһ�������֧�֣���������кܶ෽����ֱ�ӱ�¶���롣
 * ������ʹ���ߣ�����ָ�̳��ߣ�Ӧ�þ�����Щ����ֻ�ܸ��ڲ�ʹ
 * �ã���Щ����������verify(TName, TPassword)���Ը��ⲿʹ�ã���
 *
 * @param <TName> ������ʶÿһ������Ķ��󣬿����Ǹ������͡�
 * ע��TName������к��ʵ�equals��hashCode��comareTo����
 * @param <TPassword> ��������ͣ�ע����������к��ʵ�equals����
 * 
 * ==========================================================
 * ע�⣺
 * 1��TName������к��ʵ�equals��hashCode��comareTo����
 * 2��TPassword������к��ʵ�equals����
 * ==========================================================
 * 
 * //Inheritance needed
 */
public abstract class TypedPasswordManager<TName, TPassword>
{
	/**
	 * ����һ������
	 * 
	 * @param name ���������ʲô�ĵ����룬����Ϊnull
	 * @param password ���룬����Ϊnull
	 * @return �����ȥ�к����name�󶨵����룬��ô����֮�����û�У��򷵻�null
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
	 * put������ʵ�֣���put����������� 
	 */
	protected abstract TPassword putImpl(TName name, TPassword password) throws Exception;
	
	/**
	 * ɾ��һ������
	 * 
	 * @param name ��������name������Ϊnull 
	 * @return �����ȥ�к�name�󶨵����룬�򷵻�֮�����û�У��򷵻�null
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
	 * remove������ʵ�֣���remove����������� 
	 */
	protected abstract TPassword removeImpl(TName name) throws Exception;
	
	/**
	 * ȡ��name��Ӧ������
	 * 
	 * @param name ���������ʲô�ĵ����룬����Ϊnull
	 * @return ����ж�Ӧ�����򷵻�֮�����򷵻�null
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
	 * get������ʵ�֣���get����������� 
	 */
	protected abstract TPassword getImpl(TName name) throws Exception;
	
	/**
	 * ����Ƿ����name��Ӧ������
	 * 
	 * @param name ���������ʲô�ĵ����룬����Ϊnull
	 * @return ����name��Ӧ�������򷵻�true����֮����false
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
	 * ��֤�����name-password�Ժ��Ѿ��洢��name-password���Ƿ�һ��(��equals����)��
	 * ���һ���򷵻�true�����򷵻�false�����nameָ�������벻���ڣ�Ҳ����null��
	 * �������������ҪһЩ��ȫ�������粻���ڱȽϺ󣬰����������ڴ��еȵȡ�
	 * 
	 * @param name ���������ʲô�ĵ�����
	 * @param password ��Ҫ��֤�����룬���ܲ���name��Ӧ������
	 * @return �����Ƿ�ƥ�䣬�������name��password��������һ����nullʱ���򷵻�false��
	 * ���nameָ�������벻���ڣ�Ҳ����false��
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
	 * verify������ʵ�֣���verify����������� 
	 */
	protected abstract boolean verifyImpl(TName name, TPassword password) throws Exception;

	/**
	 * ��մ洢������password��name
	 */
	public synchronized void clear() throws Exception
	{
		clearImpl();
	}
	
	protected abstract void clearImpl() throws Exception;
	
	/**
	 * ����Ƿ�Ϊ�գ���û�м����κ�name��password 
	 */
	public synchronized boolean isEmpty() throws Exception
	{
		return isEmptyImpl();
	}
	
	protected abstract boolean isEmptyImpl() throws Exception;
	
	/**
	 * @return ��ǰ�洢�е�password������
	 */
	public synchronized int getSize() throws Exception
	{
		return getSizeImpl();
	}
	
	protected abstract int getSizeImpl() throws Exception;
	
	
	
	
}
