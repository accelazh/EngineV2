package com.accela.MessageService.shared;

/**
 * 
 * Name��ʾһ�����֣�NamingHost����ע����Ȼ����ʹ�á�NamingServer��
 * ���¼����NamingHost����ע����ʲôName��
 * Name����������ʲô�أ�������һ��ʲô����������ͨ��NamingServer��
 * ��ֻҪ����һ��Name���Ϳ���֪��ע�������Name��NamingHost��ClientID��
 *
 */
public class Name implements Comparable<Name>
{
	private String name;
	
	private Name()
	{
		// this constructor does nothing. it is used for HPObjectStreams
	}
	
	public Name(String name)
	{
		this();
		
		if(null==name)
		{
			throw new NullPointerException("name should not be null");
		}
		
		this.name=name;
	}

	public String getName()
	{
		return name;
	}
	
	public String toString()
	{
		return name;
	}

	@Override
	public int compareTo(Name other)
	{
		if(null==other)
		{
			return 1;
		}
		
		return name.compareTo(other.name);
	}

	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof Name))
		{
			return false;
		}
		
		return compareTo((Name)obj)==0;
	}

	@Override
	public int hashCode()
	{
		return name.hashCode();
	}
	
	

}
