package com.accela.ClassIDAndInstanceID;

/**
 *
 *ClassID�������ཨ��Ψһ�ı�ʶ������ͬ������������ClassID��equals��֤һ�����ȣ�
 *��ͬ������������ClassID��equals��֤ȫ�����
 *
 *
 * ClassID֧�ֶ��߳�
 */
public class ClassID implements Comparable<ClassID>
{
	private Class<?> aimClass;
	
	/**
	 * ����һ��ClassID����
	 * @param aimClass ���ClassID������ָ���Ŀ����
	 */
	protected ClassID(Class<?> aimClass)
	{
		if(null==aimClass)
		{
			throw new NullPointerException("aimClass should not be null");
		}
		
		this.aimClass=aimClass;
		assert(isIDOf(aimClass));
		
	}
	
	/**
	 * �ж����ClassID��ָ���Ŀ�����Ƿ���someClass
	 */
	public boolean isIDOf(Class<?> someClass)
	{
		if(null==someClass)
		{
			throw new NullPointerException("someClass should not be null");
		}
		
		return aimClass==someClass;
	}
	
	/**
	 * �ж����ClassID��ָ���Ŀ�����Ƿ���someInstance����
	 */
	public boolean isOfSameClass(Object someInstance)
	{
		if(null==someInstance)
		{
			throw new NullPointerException("someInstance should not be null");
		}
		
		return someInstance.getClass()==aimClass;
	}
	
	/**
	 * �ж����ClassID��ָ���Ŀ�����Ƿ���instanceID��
	 * ��ָ��Ķ������
	 * 
	 * @throws IllegalStateException ���instanceID�ڲ�
	 * �����������洢��Ŀ������Ѿ�����������
	 */
	public boolean isOfSameClass(InstanceID instanceID)
	{
		if(null==instanceID)
		{
			throw new NullPointerException("instanceID should not be null");
		}
		
		return instanceID.isOfSameClass(this);
	}
	
	/**
	 * �������ClassID��ָ���Ŀ����
	 */
	public Class<?> getAimClass()
	{
		assert(aimClass!=null);
		return aimClass;
	}

	/**
	 * ���ҽ�������ClassID��ָ���Ŀ������
	 * ==��֤��ȣ�������ClassID�����
	 */
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof ClassID))
		{
			return false;
		}
		
		ClassID other=(ClassID)obj;
		
		return compareTo(other)==0;
	}

	/**
	 * @return ���ClassID��ָ���Ŀ�����hashCode()���� 
	 */
	@Override
	public int hashCode()
	{
		return aimClass.hashCode();
	}

	/**
	 * �Ƚ����ClassID��ָ���Ŀ�����������
	 * other��ָ���Ŀ���������
	 */
	@Override
	public int compareTo(ClassID other)
	{
		if(null==other)
		{
			return 1;
		}
		
		int result = aimClass.getName().compareTo(other.aimClass.getName());
		assert(result!=0||this.aimClass==other.aimClass);
		assert(result!=0||this==other);		//IDDispatcher�ķ������Ӧ�ñ�֤����ȵ�ClassID��ʵ��һ��ֻ��һ��
		return result;
	}
	
	/**
	 * ����Ŀ�����Class<?>�����toString()�������ص��ַ���
	 */
	public String toString()
	{
		assert(aimClass!=null);
		return aimClass.toString();
	}
	
	

}
