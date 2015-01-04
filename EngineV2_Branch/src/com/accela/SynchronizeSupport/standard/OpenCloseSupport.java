package com.accela.SynchronizeSupport.standard;

import com.accela.SynchronizeSupport.other.SwitchLock;

/**
 * 
 * ================================================================
 * �����������������Ŀɿ�������ĸ�� 1���ɿ������ӵ��open������close���������ǽ������ط������Լ�һЩ��ͨ�ķ���
 * 2�����ط���֮��ֻ�ܴ���ִ�У���ͨ����֮����Բ���ִ�� 3�������ط���ִ�е�ʱ����ͨ��������ִ�У����̲߳��ܽ�������
 * 4����������߳���������ͨ�����е�ʱ�򣬵��ÿ��ط�������ô��������ͨ������
 * ���̻߳ᱻInterruptedException�������쳣�ϳ�����Ȼ���̲߳��ܽ����ִ�� ���ط�����
 * 5�����������δ�򿪻��߹رյ�״̬��ʱ����������Ӧ����Ӧ�ؽ�ֹ�߳̽��롣���ң� ����״̬�£�����б���û���κ�һ����������ʹ�߳�������
 * 
 * ������5��������������໥ƴ�ӡ��ۺϣ�����һ������Ŀɿ�������� ��5������Ҫ������һ�����A���������а�����A1��A2��A3���������
 * ��A�رյ�ʱ�򣬻�ر�A1��A2��A3��ֻ�е�A1��A2��A3���ܹ���֤�ڹ� �յ�״̬�£��߳�һ�����������������ʱ��A�����ڲ���Ҫ�˽�A1��
 * A2��A3���ڲ����������£���֤���Լ�Ҳ����ѭ��5����
 * ================================================================
 * 
 * ��������Ϊ��Ϊ�ɿ�������ṩʵ�ֶ���Ƶġ�����ʹ�òμ� OpenCloseSupportExample��
 * 
 * �����ʵ������ʹ��SwitchLock��д�ġ�
 * 
 * ʹ��ʱ��Ҫע�⣺ 1����Ҫ�ڿ��ط����е��ñ�lockMethod()...unlockMethod()���������ķ��� ����ᷢ��������
 * 2��unlockMethod()����һ��Ҫ����finally�־��У�����֤�߳������׳�ʲô �쳣���������unlockMethod()���ͷ���
 * 3��ʹ�����������֤ͬ������Ϊ�Ƚϸ��ӣ����Ч�ʲ�����
 * 
 */
public class OpenCloseSupport implements IOpenClosable
{
	private boolean open = false;

	private SwitchLock switchLock = new SwitchLock();

	private TargetMethod openImpl = null;

	private TargetMethod closeImpl = null;

	/**
	 * �½�һ��ʵ��������Ҫ�ṩ�򿪷����͹رշ�������ִ�е�����
	 * 
	 * @param openImpl
	 *            ʹ�����ṩ�Ĵ򿪷�����ʵ��
	 * @param closeImpl
	 *            ʹ�����ṩ�Ĺرշ�����ʵ��
	 */
	public OpenCloseSupport(TargetMethod openImpl, TargetMethod closeImpl)
	{
		if (null == openImpl)
		{
			throw new NullPointerException("openImpl should not be null");
		}
		if (null == closeImpl)
		{
			throw new NullPointerException("closeImpl should not be null");
		}

		this.openImpl = openImpl;
		this.closeImpl = closeImpl;
	}

	/**
	 * �����Ƿ�򿪡� �ɿ��������״̬ʵ����ֻ���������򿪵ĺ͹رյġ�
	 */
	@Override
	public boolean isOpen()
	{
		return open;
	}

	@Override
	public void open() throws AlreadyOpenedException, FailedToOpenException
	{
		if (isOpen())
		{
			throw new AlreadyOpenedException();
		}

		switchLock.lockSwitch();
		try
		{
			open = true;
			openImpl();
		} catch (Exception ex)
		{
			open = false;

			throw new FailedToOpenException(ex);
		} finally
		{
			switchLock.unlockSwitch();
		}
	}

	/**
	 * ע�����ֻ��������Լ��õķ�������ʹ�����ṩ�Ĵ򿪷��� û�й�ϵ
	 */
	private void openImpl() throws Exception
	{
		if (openImpl != null)
		{
			openImpl.run();
		} else
		{
			assert (false);
			System.out
					.println("WARNING: openImpl()'s MethodTarget not assigned");
		}
	}

	@Override
	public void close() throws AlreadyClosedException, FailedToCloseException
	{
		if (!isOpen())
		{
			throw new AlreadyClosedException();
		}

		switchLock.lockSwitch();
		try
		{
			open = false;

			closeImpl();
		} catch (Exception ex)
		{
			throw new FailedToCloseException(ex);
		} finally
		{
			switchLock.unlockSwitch();
		}
	}

	/**
	 * ע�����ֻ��������Լ��õķ�������ʹ�����ṩ�Ĺرշ��� û�й�ϵ
	 */
	private void closeImpl() throws Exception
	{
		if (closeImpl != null)
		{
			closeImpl.run();
		} else
		{
			assert (false);
			System.out
					.println("WARNING: closeImpl()'s MethodTarget not assigned");
		}
	}

	/**
	 * ȷ�������״̬�Ǵ򿪵ġ� ������û�д򿪣����׳�AlreadyClosedException
	 * 
	 * @throws AlreadyClosedException
	 *             ��������û�д�
	 */
	public void ensureOpen()
	{
		if (!isOpen())
		{
			throw new AlreadyClosedException();
		}
	}

	/**
	 * ȷ�������״̬�ǹرյġ� �������Ѿ��򿪣����׳�AlreadyOpenedException
	 * 
	 * @throws AlreadyOpenedException
	 *             ���������Ѿ���
	 */
	public void ensureClosed()
	{
		if (isOpen())
		{
			throw new AlreadyOpenedException();
		}
	}

	/**
	 * ����������ͨ����������
	 */
	public void lockMethod()
	{
		switchLock.lockMethod();
	}

	/**
	 * ����������ͨ����������
	 */
	public void unlockMethod()
	{
		switchLock.unlockMethod();
	}

	public TargetMethod getOpenImpl()
	{
		return openImpl;
	}

	public TargetMethod getCloseImpl()
	{
		return closeImpl;
	}

}
