package com.accela.SynchronizeSupport.other;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * ������ṩ���޸���ķ��������ֻ�������ṩͬ��֧�֡�
 * 
 * �ڱ�д���ʱ�򣬾���������������������һЩ�����������и�������ڲ�����
 * �Ĺ��ܣ�����һЩ����ֻ�в鿴������ݵĹ��ܡ�����HashMap�У�put��remove��
 * clear�ȷ�����ı�������ݣ���get����ֻ��鿴������ݡ�
 * 
 * ���ｫ��Щ�޸���ķ�������config��������ֻ��鿴����ڲ����ݶ������޸���
 * �ķ�������view�����������̵߳�ʱ������ܻ�ϣ����config����ȫ������ִ�У�
 * view����֮����Բ���ִ�С����ң���config����ִ�е�ʱ��view��������ִ�У�
 * ��view����ִ�е�ʱ��config��������ִ�С�
 * 
 * ��������Ϊ���ṩ������ͬ�����ƶ���Ƶġ���Ӧ�ð��޸�������ݵķ����е�ȫ
 * ���������lockConfig()...unlockConfig()֮�䣬�������ֻ�������е�ȫ������
 * ����lockView()...unlockView()֮�䡣���������ʹ�����е������ַ����ﵽ������
 * ͬ��Ҫ��
 * 
 * ��Ȼ�������׵ķ�ʽ����finally�־佫unlockXXX()�������������Ա�֤һ�����ͷ�����
 * 
 * ע�⣬lockConfig()...unlockConfig()���Լ�lockView()...unlockView()����Ƕ�ף�
 * ��������������
 *
 * �������Ӽ�ʾ����ConfigViewLockExample
 *
 */
public class ConfigViewLock
{
	private ReentrantLock synLock = new ReentrantLock();

	private Condition viewCondition = synLock.newCondition();

	private Condition configCondition=synLock.newCondition();
	
	/**
	 * ������ʾ��ǰ˭ӵ������
	 * ���lockState==0����ʾ������ӵ�С�
	 * ���lockState>0����ʾconfig����ӵ������
	 * ���lockState<0����ʾview����ӵ������
	 */
	private int lockState=0;

	private ReentrantLock configLock=new ReentrantLock();
	
	private boolean isLockIdle()
	{
		return 0==lockState;
	}
	
	private boolean isLockOwnedByConfig()
	{
		return lockState>0;
	}

	private boolean isLockOwnedByView()
	{
		return lockState<0;
	}
	
	private void takeViewLock()
	{
		assert(lockState<=0);
		lockState--;
	}
	
	private void returnViewLock()
	{
		assert(lockState<0);
		lockState++;
		
		if(lockState>=0)
		{
			configCondition.signalAll();
		}
	}
	
	private void takeConfigLock()
	{
		assert(lockState>=0);
		lockState++;
	}
	
	private void returnConfigLock()
	{
		assert(lockState>0);
		lockState--;
		
		if(lockState<=0)
		{
			viewCondition.signalAll();
		}
	}
	
	/**
	 * ��������ڻ��޸�����ڲ����ݵķ�������ڴ�����
	 */
	public void lockConfig()
	{
		configLock.lock();

		synLock.lock();
		try
		{
			while(!grabConfigLock())
			{
				configCondition.awaitUninterruptibly();
			}
		}finally
		{
			synLock.unlock();
		}
		
	}
	
	/**
	 * ��������ڻ��޸�����ڲ����ݵķ����Ľ�β������
	 */
	public void unlockConfig()
	{
		synLock.lock();

		try
		{
			returnConfigLock();
		} finally
		{
			synLock.unlock();
		}
		
		configLock.unlock();
	}
	
	private boolean grabConfigLock()
	{
		if (isLockIdle())
		{
			takeConfigLock();
			return true;
		} else if (isLockOwnedByView())
		{
			return false;
		} else if (isLockOwnedByConfig())
		{
			takeConfigLock();
			return true;
		} else
		{
			assert (false);
			return false;
		}
	}
	
	/**
	 * ���������ֻ�����������޸�����ڲ����ݵķ���������ڴ�����
	 */
	public void lockView()
	{
		synLock.lock();

		try
		{
			while (!grabViewLock())
			{
				viewCondition.awaitUninterruptibly();
			}
		} finally
		{
			synLock.unlock();
		}
	}

	/**
	 * ���������ֻ�����������޸�����ڲ����ݵķ������Ľ�β������
	 */
	public void unlockView()
	{
		synLock.lock();

		try
		{
			returnViewLock();
		} finally
		{
			synLock.unlock();
		}
	}
	
	private boolean grabViewLock()
	{
		if (isLockIdle())
		{
			takeViewLock();
			return true;
		} else if (isLockOwnedByView())
		{
			takeViewLock();
			return true;
		} else if (isLockOwnedByConfig())
		{
			return false;
		} else
		{
			assert (false);
			return false;
		}
	}

}
