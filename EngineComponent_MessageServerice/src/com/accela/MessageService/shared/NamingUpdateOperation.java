package com.accela.MessageService.shared;

/**
 * 
 * ��NamingHost����Լ�ע���Nameʱ���ͻ���Ҫ��¼����������ͱ���Լ�ע���Name
 * �ġ�ÿһ���ı���ͼ�¼��������С�
 * ��Ȼ�������ֻ��¼�ı������Name����û�м�¼���NameӦ�����ĸ�ClientID
 * ������ϵ��ʵ���ϣ�����Ӧ����Ϣ���͵�NamingServer��ʱ����ͨ����Ϣ����Դ
 * ��֪����˭����ı�����Լ��������NameӦ�úͷ�������host������ϵ������
 * ��˵�����������е�updateType==ADD�����ʾhost��ע����һ����updateName
 * ��ȵ�Name�����updateType==REMOVE�����ʾ���hostɾ���˹�ȥ�Լ�ע���һ
 * ����updateName��ȵ�Name
 *
 */
public class NamingUpdateOperation
{
	public static final int ADD = 0;
	public static final int REMOVE = 1;

	private int updateType = ADD;

	private Name updateName;

	private NamingUpdateOperation()
	{

	}

	public NamingUpdateOperation(int updateType, Name updateName)
	{
		this();

		if (updateType != ADD && updateType != REMOVE)
		{
			throw new IllegalArgumentException("updateType is illegal");
		}
		if (null == updateName)
		{
			throw new NullPointerException("updateName should not be null");
		}
	}

	public int getUpdateType()
	{
		return updateType;
	}

	public Name getUpdateName()
	{
		return updateName;
	}

}
