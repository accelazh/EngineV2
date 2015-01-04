package com.accela.ConnectionCenter.shared;

import java.util.Stack;



/**
 * RemotePackage�������ϴ��͵���Ϣ�ı�ʾ��ʽ��
 * ��װ��clientID��message��
 * 
 * RemotePackage�ǲ��ɱ���
 *
 * 
 */
public class RemotePackage
{
	private ClientID clientID;
	private Object message;
	
	private RemotePackage()
	{
		//this constructor is reserved for HPObjectStreams
	}
	
	protected RemotePackage(ClientID clientID, Object message)
	{
		this();
		
		if(null==clientID)
		{
			throw new NullPointerException("clientID is null");
		}
		if(null==message)
		{
			throw new NullPointerException("message is null");
		}
		
		
		this.clientID=clientID;
		this.message=message;
	}
	
	public ClientID getClientID()
	{
		return clientID;
	}

	public Object getMessage()
	{
		return message;
	}
	
	public String toString()
	{
		String out="";
		out+="RemotePackage["
			+"clientID="+clientID.toString()
			+", message="+message.toString()+"]";
		return out;
	}
	
	/////////////////////////////////////////////////////////////////////
	
	//TODO ʹ�ö���أ��Լ����е�˽�ж��󻺳��ʱ��
	//ǧ��Ҫ���ˣ��ӳ���ȥ���Ķ���Ҫ��ʼ��!!!!!!!!
	
	private static Stack<RemotePackage> buffer=new Stack<RemotePackage>();
	
	public synchronized static RemotePackage newObject(ClientID clientID, Object message)
	{
		if(null==clientID)
		{
			throw new NullPointerException("clientID is null");
		}
		if(null==message)
		{
			throw new NullPointerException("message is null");
		}
		
		if(buffer.isEmpty())
		{
			return new RemotePackage(clientID, message);
		}
		else
		{
			RemotePackage pack=buffer.pop();
			assert(pack!=null);
			
			pack.clientID=clientID;
			pack.message=message;
			
			return pack;
		}
	}
	
	public synchronized static void deleteObject(RemotePackage pack)
	{
		if(null==pack)
		{
			throw new NullPointerException("pack should not be null");
		}
		
		buffer.push(pack);
	}
	

}
