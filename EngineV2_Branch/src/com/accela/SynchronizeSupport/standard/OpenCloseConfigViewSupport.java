package com.accela.SynchronizeSupport.standard;

/**
 * 
 * ������Ѿ�������OpenCloseSupport��ConfigViewSupport����ô
 * ��Ӧ���ܺ����׵��������ࡣ�������ǰ�OpenCloseSupport��
 * ConfigViewSupport�Ĺ��ܽ�ϵ���һ��
 * 
 * һЩ�࣬��ʹ�ɿ����࣬����Ҫ����config������view�����������
 * �������еķ�������Ϊ��
 * 1�����ط���
 * 2����ͨ����
 *    2.1��config����
 *    2.2��view����
 * 
 * open()��close()�������쿪�ط�����lockConfigMethod()...unlockConfigMethod()
 * ����������ͨ�����е�config������lockViewMethod()...unlockViewMethod()������
 *
 */
public class OpenCloseConfigViewSupport implements IOpenClosable
{
	private OpenCloseSupport ocs;
	
	private ConfigViewSupport cvs;
	
	/**
	 * ��OpenCloseSupport�Ĺ��췽��һ��
	 * @param openImpl
	 * @param closeImpl
	 */
	public OpenCloseConfigViewSupport(TargetMethod openImpl, TargetMethod closeImpl)
	{
		ocs=new OpenCloseSupport(openImpl, closeImpl);
		cvs=new ConfigViewSupport();
	}

	@Override
	public void close() throws AlreadyClosedException, FailedToCloseException
	{
		ocs.close();
	}

	public TargetMethod getCloseImpl()
	{
		return ocs.getCloseImpl();
	}

	public TargetMethod getOpenImpl()
	{
		return ocs.getOpenImpl();
	}

	/**
	 * ͬOpenCloseSupport��isOpen()
	 */
	@Override
	public boolean isOpen()
	{
		return ocs.isOpen();
	}

	@Override
	public void open() throws AlreadyOpenedException, FailedToOpenException
	{
		ocs.open();
	}

	/**
	 * ͬOpenCloseSupport��ensureClosed()
	 */
	public void ensureClosed()
	{
		ocs.ensureClosed();
	}

	/**
	 * ͬOpenCloseSupport��ensureOpen()
	 */
	public void ensuredOpen()
	{
		ocs.ensureOpen();
	}
	
	/**
	 * ��ס��ͨ�����е�config����
	 */
	public void lockConfigMethod()
	{
		ocs.lockMethod();
		cvs.lockConfig();
	}
	
	/**
	 * ������ͨ�����е�config����
	 */
	public void unlockConfigMethod()
	{
		cvs.unlockConfig();
		ocs.unlockMethod();
	}
	
	/**
	 * ��ס��ͨ�����е�view���� 
	 */
	public void lockViewMethod()
	{
		ocs.lockMethod();
		cvs.lockView();
	}
	
	/**
	 * ������ͨ�����е�view����
	 */
	public void unlockViewMethod()
	{
		cvs.unlockView();
		ocs.unlockMethod();
	}
}
