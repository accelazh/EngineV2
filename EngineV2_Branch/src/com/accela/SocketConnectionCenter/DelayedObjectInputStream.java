package com.accela.SocketConnectionCenter;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

/**
 * 
 * ʹ���ӳ��½����ԵĶ�������������ʲôʱ��Ҫ
 * ���������������ݣ�ʲôʱ����½�����������
 * ��Ϊ�˱����½���������ʱ���߳̿�����
 * 
 *
 */
public class DelayedObjectInputStream
{
	private ObjectInputStream in;
	private InputStream inputStream;
	public DelayedObjectInputStream(InputStream inputStream)
	{
		if(null==inputStream)
		{
			throw new NullPointerException("inputStream should not be null");
		}
		this.inputStream=inputStream;
	}
	
	public Object readObject() throws IOException, ClassNotFoundException
	{
		if(null==in)
		{
			in=new ObjectInputStream(inputStream);
		}
		return in.readObject();
	}
	
	public void close() throws IOException
	{
		if(in!=null)
		{
			in.close();
		}
		else
		{
			inputStream.close();
		}
	}

}
