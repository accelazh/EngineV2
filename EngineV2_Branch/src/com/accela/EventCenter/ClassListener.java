package com.accela.EventCenter;

/**
 * 
 * ClassListener����ĳһ�����ĳһ���¼���
 * �κθ����ʵ���ͳ����¼���ClassListener
 * ���ᴦ��
 *
 */
public abstract class ClassListener extends Listener
{
	/**
	 * @param aimEventClass ���߼���������Ӧ�ü���ʲô�����¼�
	 * @param aimClass ���߼���������Ӧ�ü����ĸ����ʵ���������¼�
	 */
	public ClassListener(Class<? extends Event> aimEventClass,
			Class<? extends Object> aimClass)
	{
		super(aimEventClass, aimClass, null);
	}

}
