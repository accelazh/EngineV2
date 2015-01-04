package com.accela.EventCenter;

/**
 *
 * ����ӿ���EventCenterʵ�֣�ͨ����������EventCenter
 * �������õķ���
 *
 */
public interface IEventCenterConfigurer
{
	/**
	 * ���¼����������м���GeneralListener��
	 * ������ظ��������ͬһĿ��ļ����������Ƕ�����Ч�ġ�
	 */
	public void addGeneralListener(GeneralListener listener);
	
	/**
	 * ���¼����������м���ClasslListener��
	 * ������ظ��������ͬһĿ��ļ����������Ƕ�����Ч�ġ�
	 */
	public void addClassListener(ClassListener listener);
	
	/**
	 * ���¼����������м���InstanceListener��
	 * ������ظ��������ͬһĿ��ļ����������Ƕ�����Ч�ġ� 
	 */
	public void addInstanceListener(InstanceListener listener);
	
	/**
	 * ɾ������ָ�����¼�aimEventClass������GeneralListener�� 
	 * @param aimEventClass ָ�����¼���
	 * @return ���б�ɾ����GeneralListener
	 */
	public GeneralListener[] removeGeneralListener(Class<? extends Event> aimEventClass);
	
	/**
	 * ɾ������ָ�����¼�aimEventClass��ָ������aimClass������ClassListener��
	 * @param aimEventClass ָ�����¼���
	 * @param aimClass ָ������
	 * @return ���б�ɾ����ClassListener
	 */
	public ClassListener[] removeClassListener(Class<? extends Event> aimEventClass,
			Class<? extends Object> aimClass);
	
	/**
	 * ɾ������ָ�����¼�aimEventClass��ָ����ʵ��aimInstance������ClassListener��
	 * @param aimEventClass ָ�����¼���
	 * @param aimInstance ָ����ʵ��
	 * @return
	 */
	public InstanceListener[] removeInstanceListener(Class<? extends Event> aimEventClass,
			Object aimInstance);
}
