package com.accela.AuthorityCenter.shared;

/**
 * 
 * 表示命令发布者的ID，常常简写为CommanderID
 * CommanderID表明向CommanderCenter发布指令
 * 的指令发布者的身份，将它们唯一地区别开
 * 
 * CommanderID应该是不可变类
 * 
 * //Inheritance needed
 */
public abstract class CommanderIDWithAuthority implements Comparable<CommanderIDWithAuthority>
{
	@Override
	public abstract int compareTo(CommanderIDWithAuthority o);

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();

}
