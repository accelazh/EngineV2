package com.accela.MessageService.shared;

/**
 * 
 * Name表示一个名字，NamingHost可以注册它然后来使用。NamingServer则
 * 会记录各个NamingHost各自注册了什么Name。
 * Name可以用来干什么呢？它代表一个什么东西，而且通过NamingServer，
 * 你只要给出一个Name，就可以知道注册了这个Name的NamingHost的ClientID。
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
