package com.accela.SynchronizeSupport.standard;

/**
 * 
 * �����Ϊ������ر�ʾ���ʹ��OpenCloseSupport�����
 * 
 * ���ȣ�����OpenCloseSupportExample��һ���ɿ�������� ��ʹ��OpenCloseSupport��������ɿ��������Ҫ��
 */
public class OpenCloseSupportExample implements IOpenClosable
{
	private OpenCloseSupport ocs;

	/**
	 * ���췽���У���Ӧ��ʹ�����Լ���open������close�����ľ���
	 * ʵ������ʼ��OpenCloseSupport��������OpenCloseSupport��
	 * �򿪺͹رյ�ʱ�򣬾ͻ�ִ���㶨�ƵĴ򿪺͹رշ����ˡ�
	 */
	public OpenCloseSupportExample()
	{
		ocs=new OpenCloseSupport(new OpenMethod(), new CloseMethod());
	}

	/**
	 * ����ֻ��򵥵ص���OpenCloseSupport�ṩ��close������
	 * OpenCloseSupport�������رշ���
	 */
	@Override
	public void close() throws AlreadyClosedException, FailedToCloseException
	{
		ocs.close();
	}

	/**
	 * OpenCloseSupport�������򿪺͹رգ����ѯ�ʴ򿪺͹رյ�״̬
	 * ��ʱ��ҲӦ��������
	 */
	@Override
	public boolean isOpen()
	{
		return ocs.isOpen();
	}

	/**
	 * ����ֻ��򵥵ص���OpenCloseSupport�ṩ��open������
	 * OpenCloseSupport�������򿪷���
	 */
	@Override
	public void open() throws AlreadyOpenedException, FailedToOpenException
	{
		ocs.open();
	}

	/**
	 * 
	 * �㶨�ƵĴ����ʱӦ��ִ�еķ���������ʹ���ڲ��࣬������С�
	 * �ڳ�ʼ��OpenCloseSupport��ʱ�򣬻�������Ķ����룬�Ա�
	 * ����OpenCloseSupport��open������ʱ��ִ���㶨�ƵĴ򿪷�����
	 *
	 */
	private class OpenMethod implements TargetMethod
	{

		/**
		 * ����д�ϴ�ʱҪִ�еĴ���
		 */
		@Override
		public void run() throws Exception
		{
			System.out.println("Hey! This class is opening.");
		}

	}

	/**
	 * 
	 * �㶨�ƵĹر����ʱӦ��ִ�еķ���������ʹ���ڲ��࣬������С�
	 * �ڳ�ʼ��OpenCloseSupport��ʱ�򣬻�������Ķ����룬�Ա�
	 * ����OpenCloseSupport��close������ʱ��ִ���㶨�ƵĹرշ�����
	 *
	 */
	private class CloseMethod implements TargetMethod
	{

		/**
		 * ����д�Ϲر�ʱҪִ�еĴ���
		 */
		@Override
		public void run() throws Exception
		{
			System.out.println("Hey! This class is closing.");

		}

	}

	/**
	 * ����һ����ͨ��������Ϊ���������߳���������Ӧ����
	 * lockMethod()...unlockMethod()����������
	 */
	public void method1() throws InterruptedException
	{
		ocs.lockMethod();
		try
		{
			/*
			 * ʹ�������֤һ��������򿪵�ʱ��
			 * �ſ��Խ������������
			 * �����������׳��쳣�����һ��Ҫ��
			 * ��try����
			 */
			ocs.ensureOpen();
			
			Thread.sleep(1000);
		} finally
		{
			/*
			 * ������ע�⣬��unlockMethod()��������finally�־��У�
			 * ����try�������׳��쳣�ķ�����������������һ�ֺõ�
			 * ���ϰ�ߣ����Ҷ���OpenCloseSupport�Ǳ���ġ�
			 * 
			 * ��Ϊ��OpenCloseSupport.open()��OpenCloseSupport.close()
			 * ͨ��ʹ������ͨ�������߳��׳��쳣������ϳ�������
			 */
			ocs.unlockMethod();	
		}
	}

	/**
	 * ��ͬ����һ����ͨ������������������߳��������������Ϊ��������
	 * �ɿ�������Ĺ���Ҳ�޴󰭣���Ȼ���Բ���lockMethod()...unlockMethod()
	 * ����������
	 */
	public void method2()
	{
		ocs.lockMethod();
		try
		{
			/*
			 * ʹ�������֤һ��������رյ�ʱ��
			 * �ſ��Խ����������
			 * �����������׳��쳣�����һ��Ҫ��
			 * ��try����
			 */
			ocs.ensureClosed();		
			
			System.out.println("method2 invoked!");
		} finally
		{
			ocs.unlockMethod();
		}
	}

}
