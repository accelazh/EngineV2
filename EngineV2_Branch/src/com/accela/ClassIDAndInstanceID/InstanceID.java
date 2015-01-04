package com.accela.ClassIDAndInstanceID;

import java.lang.ref.WeakReference;

/**
 * 
 * InstanceID���ڸ�һ������ʵ������һ��Ψһ�ı�ʶ������==��֤��ȵĶ�������
 * InstanceID����equals����==��֤����ȶ�������InstanceID���ǲ���ȡ�
 * 
 * InstanceID֧�ֶ��߳�
 * 
 * Note: InstanceID ʹ��WeakReference����Ŀ����񣬲�����ɶ����޷�����
 * Note����ΪSUN JDK BUG ID=6321873��InstanceID�޷�ʵ��Comparable�ӿ�
 */
public class InstanceID
{
	private WeakReference<?> aimInstance;
	/**
	 * ��¼�¶���Ĺ�ϣ���Ա�ʧЧ��ʹ��
	 */
	private int oldHashCode=-1;
	/**
	 * ��¼�¶�������Ա�InstanceIDʧЧ�����ܹ���
	 */
	private Class<?> oldClass=null;
	
	private String oldString=null;
	
	/**
	 * �½�һ��InstanceID
	 * @param aimInstance ���InstanceID��ָ���Ŀ�����InstanceID
	 * �ڲ���������ָ������
	 */
	protected <T> InstanceID(T aimInstance)
	{
		if(null==aimInstance)
		{
			throw new NullPointerException("aimInstance should not be null");
		}
		
		this.aimInstance=new WeakReference<T>(aimInstance);
		oldHashCode=hashCode();
		this.oldClass=aimInstance.getClass();
		oldString=aimInstance.toString();
		
		assert(oldHashCode>=0);
		assert(oldClass!=null);
		assert(oldString!=null);
		assert(this.isIDOf(aimInstance));
		assert(isValid());
		
		//����ڹ��췽���о�ʧЧ����ᵼ���鷳�Ĵ�����������Ҫ��ס
		testWeakRefValid();
	}
	
	/**
	 * @return ���InstanceID��ָ���Ŀ������Ƿ���someInstance��
	 * ���InstanceID�ڲ������������洢��Ŀ������Ѿ����������գ�
	 * ��ôһ������false
	 */
	public boolean isIDOf(Object someInstance)
	{
		if(null==someInstance)
		{
			throw new NullPointerException("someInstance should not be null");
		}
		if(!isValid())
		{
			return false;
		}
		
		return aimInstance.get()==someInstance;
	}
	
	/**
	 * @return �õ����InstanceID��ָ���Ŀ��������ID
	 * ��ʹInstanceID�ڲ������������洢��Ŀ������Ѿ����������գ�
	 * ����������ܷ��غ�Ŀ�������ǰ��ͬ�Ľ��
	 */
	public ClassID getClassIDOfSameClass()
	{
		return IDDispatcher.createClassID(oldClass);
	}
	
	/**
	 * �жϴ����someClass�������������InstanceID��ָ���Ŀ�������࣬�Ƿ���
	 * ͬһ���ࡣ
	 * 
	 * ���InstanceID�ڲ������������洢��Ŀ������Ѿ����������գ�����������ܷ���
	 * �Ͷ�����ǰ��ͬ�Ľ��
	 */
	public boolean isOfSameClass(Class<?> someClass)
	{
		if(null==someClass)
		{
			throw new NullPointerException("someClass should not be null");
		}
		return someClass==oldClass;
	}
	
	/**
	 * �жϴ����classID�������������InstanceID��ָ���Ŀ�������࣬
	 * �Ƿ���ͬһ����
	 * 
	 * ���InstanceID�ڲ������������洢��Ŀ������Ѿ����������գ������
	 * �����ܷ��غͶ�����ǰ��ͬ�Ľ��
	 */
	public boolean isOfSameClass(ClassID classID)
	{
		if(null==classID)
		{
			throw new NullPointerException("classID should not be null");
		}
		return classID.isIDOf(oldClass);
	}
	
	/**
	 * @return ���InstanceID��ָ��Ķ�������ã�����
	 * ����ǳ������ɣ��պ������InstanceID������ڲ�
	 * �洢�������õ���Ч�Ժ�������������������������
	 * ָ�Ķ�����᷵��null
	 * 
	 * ���InstanceID�ڲ������������洢��Ŀ������Ѿ���
	 * �������գ����������һ������null��
	 */
	public Object getAimInstance()
	{
		if(!isValid())
		{
			return null;
		}
		
		assert(aimInstance!=null);
		assert(aimInstance.get()!=null);
		return aimInstance.get();
	}
	
	/**
	 * ���������õ���Ч�ԣ�����Ѿ�ʧЧ�����׳�IllegalStateException�쳣��
	 * 
	 * @throws IllegalStateException ���InstanceID�ڲ�
	 * �����������洢��Ŀ������Ѿ�����������
	 */
	private void testWeakRefValid()
	{
		if(!isValid())
		{
			throw new IllegalStateException("the object to which this instanceID points to is already collected by gc!");
		}
	}
	
	/**
	 * ����InstanceID��ָ��Ķ����Ƿ��Ѿ����ͷţ���InstanceID�Ѿ���Ч 
	 */
	public boolean isValid()
	{
		return aimInstance.get()!=null;
	}

	/**
	 * ����InstanceID��ȣ����ҽ�����ָ���Ŀ�������==
	 * ��֤��ȡ�
	 * 
	 * ���������Ƚϵ�InstanceID�е��κ�һ��ʧЧ�������
	 * other==this������£����᷵��false
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof InstanceID))
		{
			return false;
		}
		
		InstanceID other=(InstanceID)obj;
		if(isValid()&&other.isValid())
		{
			boolean result = aimInstance.get()==other.aimInstance.get();
			assert(!result||this==other);
			return result;
		}
		else
		{
			if(this==other)
			{
				return true;
			}
			else
			{
				return false;
			}
		}
	}

	/**
	 * @return ����System.identityHashCode(InstanceID��ָ���Ŀ�����)
	 * 
	 * ���InstanceID�Ѿ�ʧЧ����������������ظö�����ǰ�Ĺ�ϣ�룬��ά��
	 * ��ϣ�벻�䡣
	 */
	@Override
	public int hashCode()
	{
		if(!isValid())
		{
			return oldHashCode;
		}
		
		//����ȡ��hashCode�İ취��ֻ֤Ҫʵ����ͬ��
		//��������equals��ȣ�Ҳ������Ϸ��ز�ͬ��hashCode
		return System.identityHashCode(aimInstance.get());	
	}
	
	/**
	 * ����Ŀ������toString()�����ص��ַ�������֤InstanceIDʧЧ��
	 * ���ܷ�����ͬ�Ľ������Ȼ�������ָͬ��equals()������ͬ
	 */
	public String toString()
	{
		assert(oldString!=null);
		return oldString;
	}
	
}
