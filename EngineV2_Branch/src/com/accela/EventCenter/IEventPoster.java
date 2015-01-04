package com.accela.EventCenter;

/**
 * ����ӿ���EventCenterʵ�֣�ͨ����������
 * EventCenter�ķ����¼��ķ���
 */
public interface IEventPoster
{
	/**
	 * ���¼��������ķ���һ���¼����¼��������Ļ������ҳ�������¼���ƥ���GeneralListener��
	 * ClassListener��InstanceListener������������¼���
	 * 
	 * @param event
	 * @throws EventProcessingException ��ĳ������������ʱ�����쳣��ʱ���¼��������Ĳ���
	 * �жϴ������Ǽ�����������������Ĵ��������Ż��׳�����쳣����������п��ܷ������
	 * �쳣�����Ƕ��ᱻ�洢������쳣�����С�
	 * 
	 */
	public void postEvent(Event event) throws EventProcessingException;

}
