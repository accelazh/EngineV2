package com.accela.ExceptionCenter;

/**
 * 
 * ����ӿ����쳣��������ʵ�֣�ͨ�����������쳣��������
 * ���������쳣�ķ�����
 *
 */
public interface IExceptionPoster
{
	/**
	 * ��һ���쳣����exception���͵��쳣�������ġ��쳣�������Ļ��ҳ�����������쳣����
	 * exceptionƥ�����ͨ�쳣����������������쳣�����û����ͨ�쳣������ƥ�䣬��ô��
	 * ����Ĭ���쳣����������������쳣����exception��
	 */
	public void postException(Exception exception);
}
