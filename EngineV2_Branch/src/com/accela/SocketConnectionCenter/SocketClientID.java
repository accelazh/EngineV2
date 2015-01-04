package com.accela.SocketConnectionCenter;

import java.net.InetAddress;

import com.accela.ConnectionCenter.shared.ClientID;

/**
 * 
 * 在套接字具体网络技术下的ClientID
 *
 */
public class SocketClientID extends ClientID
{
	private InetAddress address;
	private int port;
	
	private SocketClientID()
	{
		// this constructor is reserved for HPObjectStreams
	}
	
	public SocketClientID(InetAddress address, int port)
	{
		this();
		
		if(null==address)
		{
			throw new NullPointerException("address is null");
		}
		if(port<0)
		{
			throw new IllegalArgumentException("port should not be negative");
		}
		
		this.address=address;
		this.port=port;
	}
	
	public boolean equals(Object o)
	{
		assert(address!=null);
		assert(port>=0);
		
		if(!(o instanceof SocketClientID))
		{
			return false;
		}
		
		SocketClientID c=(SocketClientID)o;
		
		assert(c.address!=null);
		assert(c.port>=0);
		
		return address.equals(c.getAddress()) && (port==c.getPort());
	}
	
	public int hashCode()
	{
		assert(address!=null);
		assert(port>=0);
		
		return address.hashCode()+port;
	}
	
	public int compareTo(ClientID c)
	{
		assert(address!=null);
		assert(port>=0);
		
		if(!(c instanceof SocketClientID))
		{
			return 1;    //TODO 如果同时使用多种ClientID,则这个判断就过于武断了
		}
		
		SocketClientID other=(SocketClientID)c;
		assert(other.getAddress()!=null);
		assert(other.getPort()>=0);
		
		byte[] selfIP=address.getAddress();
		byte[] otherIP=address.getAddress();
		
		boolean findDifferent=false; 
		boolean selfBiggerThanOther=false;
		for(int i=0;i<Math.max(selfIP.length, otherIP.length);i++)
		{
			int selfIPByte=(i<selfIP.length)?selfIP[i]:0;
			int otherIPByte=(i<otherIP.length)?otherIP[i]:0;
			
			if(selfIPByte!=otherIPByte)
			{
				findDifferent=true;
				selfBiggerThanOther=selfIPByte>otherIPByte;
				break;
			}
		}
		
		if(findDifferent)
		{
			if(selfBiggerThanOther)
			{
				return 1;
			}
			else
			{
				return -1;
			}
		}
		else
		{
			return port-other.getPort();
		}
		
	}

	public InetAddress getAddress()
	{
		return address;
	}

	public int getPort()
	{
		return port;
	}
	
	public String toString()
	{
		String out="";
		out+="SocketClientID[address="+address.toString()+", port="+port+"]";
		return out;
	}

}
