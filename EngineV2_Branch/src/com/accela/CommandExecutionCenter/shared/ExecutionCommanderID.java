package com.accela.CommandExecutionCenter.shared;

/**
 * 
 * 表示命令发布者的ID，常常简写为CommanderID
 * ExecutionCommanderID表明向CommanderExecutionCenter
 * 发布指令的指令发布者的身份，将它们唯一地区别开
 * 
 * ExecutionCommanderID应该是不可变类
 * 
 * //Inheritance needed
 */
public abstract class ExecutionCommanderID implements Comparable<ExecutionCommanderID>
{
	@Override
	public abstract int compareTo(ExecutionCommanderID o);

	@Override
	public abstract boolean equals(Object obj);

	@Override
	public abstract int hashCode();

}
