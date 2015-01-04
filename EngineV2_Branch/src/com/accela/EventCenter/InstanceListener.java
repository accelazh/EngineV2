package com.accela.EventCenter;

/**
 * 
 * InstanceListener����ĳһ��ʵ����ĳһ���¼���
 * �����ʵ���ͳ����¼���InstanceListener
 * �ͻᴦ��
 *
 */
public abstract class InstanceListener extends Listener
{
	/**
	 * @param aimEventClass ���߼���������Ӧ�ü���ʲô�����¼�
	 * @param aimInstance ���߼���������Ӧ�ü����ĸ�ʵ���������¼�
	 */
	public InstanceListener(Class<? extends Event> aimEventClass,
			Object aimInstance)
	{
		super(aimEventClass, aimInstance.getClass(), aimInstance);
	}

}
