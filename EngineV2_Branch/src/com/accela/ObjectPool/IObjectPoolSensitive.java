package com.accela.ObjectPool;

/**
 * 
 * ʵ��������ӿڵĶ��󣬵�����ObjectPool�е�ʱ��
 * ObjectPool�����onPutIntoObjectPool()��������
 * ��ObjectPool��ȡ����ʱ��ObjectPool�����
 * onRetrievedFromObjectPool()����
 *
 */
public interface IObjectPoolSensitive
{
	/**
	 * �ڴӶ������ȡ�ض����ʱ��ִ��
	 */
	public void onPut();
	
	/**
	 * ���������з�������ʱ��ִ��
	 */
	public void onRetrieve();
}
