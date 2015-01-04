package com.accela.AuthorityCenter.shared;

/**
 * 
 * ��ʾһ������,����дΪCommand�����ﶨ�����������������ɣ�
 * 1�����������ߵ�ID����CommanderID
 * 2������ͷcommandHead������˵������ʲô�����ͬ�������������������
 * 3������Я��������argument��һ��������Բ�Я��argument
 * ����һ�����updata_view arg1, arg2, arg3�����ơ�update_view��Ϊ����ͷcommandHead��
 * �ơ�arg1, arg2, arg3��Ϊ����Я���Ĳ���������argument��
 * 
 * commandHeadͳһʹ���ַ��������ַ���������������Ҫ��
 * 1��û�пո�
 * 2��ֻ���ɴ�СдӢ����ĸ�����֡��»��߻����ַ����
 * 3����ͷ�ַ�������Ӣ����ĸ
 * 4��commandHead�ĳ��Ȳ�С��1
 *
 */
public class CommandWithAuthority
{
	/**
	 * ���������ߵ�ID
	 */
	private CommanderIDWithAuthority commanderID;
	/**
	 * ���������
	 */
	private String commandHead;
	/**
	 * ����Я�������ݻ����
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
		
		//��鿪ͷ�ַ��Ƿ���Ӣ����ĸ
		if(!isLetter(commandHead.charAt(0)))
		{
			return false;
		}
		
		//����ַ��Ƿ�ֻ�д�СдӢ����ĸ�����֡��»��߻����ַ�
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
