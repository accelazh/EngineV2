package com.accela.SynchronizeSupport.standard;

/**
 * 
 * ʵ������ӿڱ�ʾ������һ�����Թرպʹ򿪵������
 *
 */
public interface IOpenClosable
{
	/**
	 * @return �����ǰ�Ǵ򿪵Ļ����Ѿ��رյ�
	 */
	public boolean isOpen();
	/**
	 * �����
	 * @throws AlreadyOpenedException ������������δ�����Ļ�
	 * @throws FailedToOpenException ����򿪹����������쳣
	 */
	public void open() throws AlreadyOpenedException, FailedToOpenException;
	/**
	 * �ر����
	 * @throws AlreadyOpenedException ������������δ�����Ļ�
	 * @throws FailedToOpenException ����򿪹����������쳣
	 */
	public void close() throws AlreadyClosedException, FailedToCloseException;

}
