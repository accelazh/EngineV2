package com.accela.AuthorityCenter.shared;

/**
 * 
 * 表示一条命令,常简写为Command。这里定义的命令由三部分组成：
 * 1、发布命令者的ID，即CommanderID
 * 2、命令头commandHead，用于说明这是什么命令，不同种类的命令由它来区分
 * 3、命令携带的数据argument，一条命令可以不携带argument
 * 例如一个命令“updata_view arg1, arg2, arg3”，称“update_view”为命令头commandHead，
 * 称“arg1, arg2, arg3”为命令携带的参数或数据argument。
 * 
 * commandHead统一使用字符串，其字符串必须满足如下要求：
 * 1、没有空格
 * 2、只能由大小写英文字母、数字、下划线或连字符组成
 * 3、开头字符必须是英文字母
 * 4、commandHead的长度不小于1
 *
 */
public class CommandWithAuthority
{
	/**
	 * 发布命令者的ID
	 */
	private CommanderIDWithAuthority commanderID;
	/**
	 * 具体的命令
	 */
	private String commandHead;
	/**
	 * 命令携带的数据或参数
	 */
	private Object argument;
	
	public CommandWithAuthority(CommanderIDWithAuthority commanderID, String commandHead)
	{
		this(commanderID, commandHead, null);
	}
	
	public CommandWithAuthority(CommanderIDWithAuthority commanderID, String commandHead, Object argument)
	{
		if(null==commanderID)
		{
			throw new NullPointerException("commanderID should not be null");
		}
		if(!checkCommandHeadValid(commandHead))
		{
			throw new IllegalArgumentException("commandHead is not valid");
		}
		
		this.commanderID=commanderID;
		this.commandHead=commandHead;
		this.argument=argument;
	}
	
	public CommanderIDWithAuthority getCommanderID()
	{
		return commanderID;
	}

	public String getCommandHead()
	{
		return commandHead;
	}

	public Object getArgument()
	{
		return argument;
	}
	
	public static boolean checkCommandHeadValid(String commandHead)
	{
		if(null==commandHead)
		{
			return false;
		}
		if(commandHead.length()<1)
		{
			return false;
		}
		
		//检查开头字符是否是英文字母
		if(!isLetter(commandHead.charAt(0)))
		{
			return false;
		}
		
		//检查字符是否只有大小写英文字母、数字、下划线或连字符
		for(int i=0;i<commandHead.length();i++)
		{
			if(!isLetterOrDigitOrUnderlineOrHyphen(commandHead.charAt(i)))
			{
				return false;
			}
		}
		
		return true;
		
	}
	
	private static boolean isLetter(char c)
	{
		if((c>='a'&&c<='z')||(c>='A'&&c<='Z'))
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private static boolean isDigit(char c)
	{
		if(c>='0'&&c<='9')
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	
	private static boolean isLetterOrDigitOrUnderlineOrHyphen(char c)
	{
		return '_'==c||'-'==c||isDigit(c)||isLetter(c);
	}


}
