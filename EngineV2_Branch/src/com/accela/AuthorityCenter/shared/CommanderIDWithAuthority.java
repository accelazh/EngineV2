package com.accela.AuthorityCenter.shared;

/**
 * 
 * ��ʾ������ߵ�ID��������дΪCommanderID
 * CommanderID������CommanderCenter����ָ��
 * ��ָ����ߵ���ݣ�������Ψһ������
 * 
 * CommanderIDӦ���ǲ��ɱ���
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
