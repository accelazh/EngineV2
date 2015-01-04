package com.accela.ConnectionCenter.connectionLauncher;

import java.util.concurrent.locks.ReentrantLock;

import com.accela.ConnectionCenter.shared.ClientID;
import com.accela.ConnectionCenter.shared.NewConnectionInfo;

/**
 * 
 * ConnectionLauncher������ָ����Client�������ӡ�
 * 
 * ���ݾ�������缼��������ཫ�ᱻ�̳к�ʵ�֡�
 *
 */
public abstract class ConnectionLauncher
{
	private ReentrantLock lock = new ReentrantLock();

	/**
	 * �½�һ�����ӣ��������ʵ���ϻ����launchConnectionImpl(ClientID)
	 * ��������ӵ��½����������ֻ���һЩ���滯�Ĳ���
	 * 
	 * @param clientID Զ�˵�������ClientID��ָ����˭�½�����
	 * @return ������Client���棬�õ��Ľ�����Client���������������Ϣ
	 * @throws FailedToLaunchConnectionException
	 */
	public NewConnectionInfo launchConnection(ClientID clientID) throws FailedToLaunchConnectionException
	{
		if (null == clientID)
		{
			throw new NullPointerException("clientID should not be null");
		}

		NewConnectionInfo info = null;
		lock.lock();

		try
		{
			info = launchConnectionImpl(clientID);
		} catch (Exception ex)
		{
			throw new FailedToLaunchConnectionException(ex);
		} finally
		{
			lock.unlock();
		}

		if (null == info)
		{
			assert(false);
			throw new NullPointerException(
					"launchConnectionImpl should not return null!");
		}

		return info;
	}

	/**
	 * ���ݾ�������缼����ʵ�ֵ��½����ӵķ�����
	 * 
	 * @param clientID ָ����˭�½�����
	 * @return ������Client���棬�õ��Ľ�����Client���������������Ϣ
	 * @throws Exception
	 */
	protected abstract NewConnectionInfo launchConnectionImpl(ClientID clientID)
			throws Exception;
}
