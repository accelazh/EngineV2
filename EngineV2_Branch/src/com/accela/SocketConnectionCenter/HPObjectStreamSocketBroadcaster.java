package com.accela.SocketConnectionCenter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.net.UnknownHostException;

import com.accela.ConnectionCenter.broadcaster.Broadcaster;
import com.accela.ConnectionCenter.shared.RemotePackage;
import com.accela.ObjectStreams.HPObjectInputStream;
import com.accela.ObjectStreams.HPObjectOutputStream;

/**
 * 
 * ��SocketBroadcaster��ȫ��ͬ��ֻ�Ƕ������������ʹ��HPObjectInputStream
 * ��HPObjectOutputStream
 *
 */
public class HPObjectStreamSocketBroadcaster extends Broadcaster
{
	/**
	 * ���㲥�Ķ������л�֮���ֽ���������
	 */
	public static final int MESSAGE_MAX_BYTES=256;
	
	/**
	 * �㲥�˿ڣ����е�Broadcaster����������һ�ӿ�
	 */
	private int port=1171;
	/**
	 * �鲥��ַ
	 */
	private String groupAddress="230.0.0.1";
	
	private MulticastSocket socket;

	/**
	 * ��һ������������л������������ֽ����顣
	 * �������������Ƿ����л�����ֽ����鳤�ȴ���MESSAGE_MAX_BYTES
	 * 
	 * @param message �������
	 * @return �������л�����ֽ�����
	 * @throws IOException 
	 */
	private byte[] serializeObject(Object message) throws IOException
	{
		if(null==message)
		{
			throw new NullPointerException();
		}
		
		ByteArrayOutputStream byteOut=new ByteArrayOutputStream();
		HPObjectOutputStream objectOut=new HPObjectOutputStream(byteOut);
		objectOut.writeObject(message);
		objectOut.flush();
		objectOut.close();
		byteOut.close();
		
		return byteOut.toByteArray();
	}
	
	/**
	 * ���������л������ض�Ӧ�Ķ���
	 * 
	 * @param message �����л��Ķ�����ֽ�����
	 * @param length �ֽ����ݵĳ���
	 * @return	�����л��󱻻�ԭ�Ķ���
	 * 
	 * @throws InstantiationException 
	 * @throws InvocationTargetException 
	 * @throws ClassNotFoundException 
	 * @throws IOException 
	 */
	private Object unserializeObject(byte[] message, int length) throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException
	{
		if(null==message)
		{
			throw new NullPointerException("message == null");
		}
		if(length<=0||length>message.length)
		{
			throw new IllegalArgumentException("length is illegal");
		}
		
		ByteArrayInputStream byteIn=new ByteArrayInputStream(message, 0, length);
		HPObjectInputStream objectIn=new HPObjectInputStream(byteIn);
		Object ret=objectIn.readObject();
		objectIn.close();
		byteIn.close();
		
		return ret;
	}
	
	@Override
	protected void broadcastMessageImpl(Object message) throws IOException, MessageTooLargeException
	{
		byte[] data=serializeObject(message);
		if(data.length>MESSAGE_MAX_BYTES)
		{
			throw new MessageTooLargeException(
					"message after serizalization should be less than "
					+MESSAGE_MAX_BYTES+" bytes");
		}
		
		socket.send(new DatagramPacket(data, 
				data.length, 
				InetAddress.getByName(groupAddress), 
				port));
	}

	@Override
	protected void closeImpl() throws IOException
	{
		
		try
		{
			InetAddress group = InetAddress.getByName(groupAddress);
			socket.leaveGroup(group);
		} finally
		{
			socket.close();
			socket = null;
		}
	}

	@Override
	protected void openImpl() throws IOException
	{
		socket=new MulticastSocket(port);
		InetAddress group=InetAddress.getByName(groupAddress);
		socket.joinGroup(group);
	}

	@Override
	protected RemotePackage receiveMessageFromBroadcast() throws IOException, ClassNotFoundException, InvocationTargetException, InstantiationException
	{
		DatagramPacket dataPacket= new DatagramPacket(
				new byte[MESSAGE_MAX_BYTES], 
				MESSAGE_MAX_BYTES);
		
		socket.receive(dataPacket);
		
		Object message=unserializeObject(
				dataPacket.getData(), 
				dataPacket.getLength());
		
		SocketClientID clientID=new SocketClientID(
				dataPacket.getAddress(), 
				dataPacket.getPort());
		
		return RemotePackage.newObject(clientID, message);
	}

	public int getBroadcastPort()
	{
		return port;
	}

	public void setBroadcastPort(int port)
	{
		if (port <= 1024)
		{
			throw new IllegalArgumentException(
					"port should be greater than 1024");
		}
		
		ocs.lockMethod();
		
		try
		{
			ocs.ensureClosed();
			
			this.port = port;
		} finally
		{
			ocs.unlockMethod();
		}
	}

	public InetAddress getGroupAddress()
	{
		try
		{
			return InetAddress.getByName(groupAddress);
		} catch (UnknownHostException ex)
		{
			assert(false);
			throw new IllegalStateException("group cast address is illegal!");
		}
	}

	public void setGroupAddress(String groupAddress)
	{
		if (null == groupAddress)
		{
			throw new NullPointerException(
					"groupAddress should not be null");
		}
		InetAddress group=null;
		try
		{
			group = InetAddress.getByName(groupAddress);
			if (!group.isMulticastAddress())
			{
				throw new IllegalArgumentException(
						"groupAddress is not legal broadcast address!");
			}
		} catch (UnknownHostException ex)
		{
			throw new IllegalArgumentException(
					"groupAddress is not legal broadcast address!");
		}
		assert(group!=null);
		
		ocs.lockMethod();
		try
		{
			ocs.ensureClosed();
			
			this.groupAddress = groupAddress;
		} finally
		{
			ocs.unlockMethod();
		}
	}

}
