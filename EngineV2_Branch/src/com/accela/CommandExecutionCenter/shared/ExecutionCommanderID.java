package com.accela.CommandExecutionCenter.shared;

/**
 * 
 * ��ʾ������ߵ�ID��������дΪCommanderID
 * ExecutionCommanderID������CommanderExecutionCenter
 * ����ָ���ָ����ߵ���ݣ�������Ψһ������
 * 
 * ExecutionCommanderIDӦ���ǲ��ɱ���
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
