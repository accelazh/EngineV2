package com.accela.ConnectionCenter.connectorCreator;

import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.connector.Connector;
import com.accela.ConnectionCenter.shared.NewConnectionInfo;

/**
 * 
 * ConnectionCenterֻ��ͨ����������½�
 * Connector����
 * 
 * //Inheritance needed
 *
 */
public abstract class ConnectorCreator
{
	private ReentrantLock lock=new ReentrantLock();
	
	/**
	 * 
	 * ����NewConnectionInfo�½�һ��Connectorʵ��
	 * @param info �����½�Connector�ı�Ҫ��Ϣ
	 * @return һ���µ�Connectorʵ��
	 * @throws FailedToCreateConnectorException
	 */
	public Connector createConnector(NewConnectionInfo info) throws FailedToCreateConnectorException
	{
		if(null==info)
		{
			throw new NullPointerException();
		}
		Connector connector=null;
		lock.lock();
		try
		{
			connector = createConnectorImpl(info);
		} catch (Exception ex)
		{
			throw new FailedToCreateConnectorException(ex);
		} finally
		{
			lock.unlock();
		}
		
		if (null == connector)
		{
			assert(false);
			throw new NullPointerException(
					"createConnectorImpl should not return null!");
		}
		
		return connector;
	}

	/**
	 * ���ݾ������缼��ʵ�ֵ��½�Connector�ķ�����
	 * 
	 * ע�⣺��Ҫ��ͼ���ñ��ͷŵ�Connector��������󻺳�������Ϊ
	 * Connector��ʹ���رգ�����������Ȼ���ܴ洢��������Ϣ������
	 * �����Ϣ�п������ڱ�ʹ�á�
	 */
	protected abstract Connector createConnectorImpl(NewConnectionInfo info) throws Exception;

}
