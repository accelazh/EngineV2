package com.accela.ExceptionCenter;

/**
 * 
 * ����ӿ����쳣��������ʵ�֣�ͨ�����������쳣��������
 * �������õķ�����
 *
 */
public interface IExceptionCenterConfigurer
{
	/**
	 * �����ͨ�쳣��������
	 * ������Ӷ������ͬ���쳣���쳣�����������Ƕ�����Ч�ġ�
	 * 
	 */
	public void addExceptionHandler(ExceptionHandler handler);
	
	/**
	 * ɾ����ͨ�쳣������
	 * @return true����쳣���������д洢��ָ�����쳣������handler
	 */
	public boolean removeExceptionHandler(ExceptionHandler handler);
	
	/**
	 * ����ָ��һ��Ĭ���쳣������
	 * @return ��ȥ��Ĭ���쳣������
	 */
	public ExceptionHandler replaceDefaultExceptionHandler(
			ExceptionHandler handler);
}
