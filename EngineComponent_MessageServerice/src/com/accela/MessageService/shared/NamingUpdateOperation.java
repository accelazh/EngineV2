package com.accela.MessageService.shared;

/**
 * 
 * 当NamingHost变更自己注册的Name时，就会需要记录下来它是如和变更自己注册的Name
 * 的。每一步的变更就记录与这个类中。
 * 当然，这个类只记录的被变更的Name，而没有记录这个Name应该与哪个ClientID
 * 建立关系。实际上，当相应的消息发送到NamingServer的时候，它通过消息的来源
 * 就知道是谁发起的变更，以及被变更的Name应该和发起变更的host建立关系。具体
 * 来说，如果这个类中的updateType==ADD，则表示host新注册了一个与updateName
 * 相等的Name。如果updateType==REMOVE，则表示这个host删除了过去自己注册的一
 * 个与updateName相等的Name
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
