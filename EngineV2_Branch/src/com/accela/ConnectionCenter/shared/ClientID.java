package com.accela.ConnectionCenter.shared;


/**
 * ClientID����Ψһ��ʶ���ӵ�Connector��Client
 * ClientIDӦ�ñ�֤�������ؽ���ֻҪClient���䣬
 * ClientID�Ͳ��䡣
 * ���⣬ClientID������һ�����ɱ���
 * 
 * ClientID����Ƴɶ����ڲ�ͬ�����缼�����ھ����
 * ���缼���£�Ӧ������Ӧ��ClientID����������ΪClientID
 * 
 * //Inheritance needed
 */
public abstract class ClientID implements Comparable<ClientID>
{
	public abstract boolean equals(Object o);
	
	public abstract int hashCode();
	
	public abstract int compareTo(ClientID other);

}
