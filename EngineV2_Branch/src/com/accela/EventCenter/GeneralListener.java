package com.accela.EventCenter;

/**
 * 
 * GeneralListener����ĳһ���¼���
 * �κζ����ͳ����¼���GeneralListener
 * ���ᴦ��
 *
 */
public abstract class GeneralListener extends Listener
{
	/**
	 * @param aimEventClass ���߼���������Ӧ�ü���ʲô�����¼�
	 */
	public GeneralListener(Class<? extends Event> aimEventClass)
	{
		super(aimEventClass, null, null);
	}

}
