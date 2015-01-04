package com.accela.SocketConnectionCenter;

import java.net.InetAddress;

import com.accela.ConnectionCenter.ConnectionCenter;
import com.accela.ConnectionCenter.ConnectionCenterInitializer;

/**
 * 
 * ʹ��Socket����ʵ�ֵ�ConnectionCenter
 * 
 */
public class SocketConnectionCenter extends ConnectionCenter
{
	private boolean useHPObjectStreams = false;

	protected SocketConnectionCenter(ConnectionCenterInitializer initializer)
	{
		super(initializer);
	}

	/**
	 * �½�һ��SocketConnectionCenter�����д�������ʹ�õ��� java�Դ��Ķ������������
	 */
	public static SocketConnectionCenter createInstance()
	{
		SocketConnectionCenterInitializer initializer = new SocketConnectionCenterInitializer();
		SocketConnectionCenter connectionCenter = null;

		connectionCenter = new SocketConnectionCenter(initializer);

		return connectionCenter;
	}

	/**
	 * �½�һ��SocketConnectionCenter�����д��������õĶ��������������
	 * ��������java�Դ��Ķ��������������Ҳ������HPObjectInputStream��
	 * HPObjectOutputStream����ͨ��useHPObjectStreams��������
	 * 
	 * @param useHPObjectStreams
	 *            �Ƿ�ʹ��HPObjectOutputStream��HPObjectInputStream�� ��Ϊ��������ʱ�Ķ������������
	 */
	public static SocketConnectionCenter createInstance(
			boolean useHPObjectStreams)
	{
		if (!useHPObjectStreams)
		{
			return createInstance();
		} else
		{
			HPObjectStreamSocketConnectionCenterInitializer initializer = new HPObjectStreamSocketConnectionCenterInitializer();
			SocketConnectionCenter connectionCenter = null;

			connectionCenter = new SocketConnectionCenter(initializer);
			connectionCenter.useHPObjectStreams = true;

			return connectionCenter;
		}

	}

	/**
	 * ����SocketConnectionCenter����һ���˿ڼ������ӡ�
	 * 
	 * �����������ǰ������Ҫ�ȵ���startToReceiveConnection()������
	 * �����׳�ConnectionReceivingFunctionStoppedException��
	 * 
	 */
	public int getConnectionReceivingPort()
	{
		return ((SocketConnectionReceiver) getConnectionReceiver())
				.getSocketServerPort();
	}

	public int getBroadcastPort()
	{
		if (!useHPObjectStreams)
		{
			return ((SocketBroadcaster) getBroadcaster()).getBroadcastPort();
		} else
		{
			return ((HPObjectStreamSocketBroadcaster) getBroadcaster())
					.getBroadcastPort();
		}

	}

	/**
	 * �趨�㲥������ʹ�õĶ˿ڡ���Ӧ���ڴ򿪹㲥����֮ǰ�������������
	 * ���ڵ���openBroadcastFunction()����֮ǰ�趨�㲥�Ķ˿ڡ�����
	 * �׳�BroadcastFunctionAlreadyOpenedException�쳣��
	 * 
	 * @param port
	 *            �趨�Ķ˿ںţ�Ӧ����1024
	 */
	public void setBroadcastPort(int port)
	{
		ocs.lockMethod();
		try
		{
			if (!useHPObjectStreams)
			{
				((SocketBroadcaster) getBroadcaster()).setBroadcastPort(port);
			} else
			{
				((HPObjectStreamSocketBroadcaster) getBroadcaster())
						.setBroadcastPort(port);
			}
		} finally
		{
			ocs.unlockMethod();
		}
	}

	public InetAddress getBroadcastGroupAddress()
	{
		if (!useHPObjectStreams)
		{
			return ((SocketBroadcaster) getBroadcaster()).getGroupAddress();
		} else
		{
			return ((HPObjectStreamSocketBroadcaster) getBroadcaster())
					.getGroupAddress();
		}
	}

	public void setBroadcastGroupAddress(String groupAddress)
	{
		ocs.lockMethod();
		try
		{
			if (!useHPObjectStreams)
			{
				((SocketBroadcaster) getBroadcaster())
						.setGroupAddress(groupAddress);
			} else
			{
				((HPObjectStreamSocketBroadcaster) getBroadcaster())
						.setGroupAddress(groupAddress);
			}
		} finally
		{
			ocs.unlockMethod();
		}
	}

	/**
	 * @return ���SocketConnectionCenter�Ƿ�ʹ��HPObjectStreams
	 */
	public boolean isUsingHPObjectStreams()
	{
		return useHPObjectStreams;
	}

}
