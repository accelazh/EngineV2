package com.accela.SynchronizeSupport.other;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 
 * SwitchLock����Ϊ�ܹ��򿪺͹رյ�����ṩͬ�����ơ�
 * 
 * �ܹ��򿪺͹رյ���������ַ�����һ���ǿ��ط�������open��close
 * һ������ͨ�ķ�����������method1��method2....��ʾ����ͬ��Ҫ��
 * 1�����ط���ֻ�ܴ���ִ��
 * 2����ͨ�������Բ���ִ��
 * 3�����ط��������õ�ʱ����ͨ��������ִ�У����̲߳��ܽ�������
 * 4���������ͨ������ִ�е�ʱ������һ���߳�ȥִ�п��ط���������
 * ������ͨ�������̶߳��ᱻһ��InterruptedException�ϳ�����Ȼ��Ҫ
 * ��ִ�п��ط������̻߳����Щ�̶߳�����ͨ�����г�����ʱ��ſ�ʼִ
 * �п��ط�����
 * �������������������Ҫ�󡣾����÷���ʾ����SwitchLockExample��
 * 
 * ʹ��ʱ��Ҫע�⣺
 * 1��lockSwicth()...unlockSwitch()��lockMethod()...unlockMethod()
 * ����Ƕ�ס�
 * 2��unlockMethod()����һ��Ҫ����finally�־��У�����֤�߳������׳�ʲô
 * �쳣���������unlockMethod()���ͷ���
 * 
 * 
 *
 */
public class SwitchLock
{
	private ReentrantLock synLock = new ReentrantLock();

	private Condition methodCondition = synLock.newCondition();

	private Condition switchCondition=synLock.newCondition();
	
	/**
	 * ������ʾ��ǰ˭ӵ������
	 * ���lockState==0����ʾ������ӵ�С�
	 * ���lockState>0����ʾ���ط���ӵ������
	 * ���lockState<0����ʾ��ͨ����ӵ������
	 */
	private int lockState=0;

	private boolean drivingMethodThreads=false;
	
	private ThreadRecorder threadRecorder=new ThreadRecorder();
	
	private ReentrantLock switchLock=new ReentrantLock();
	
	private boolean isLockIdle()
	{
		return 0==lockState;
	}
	
	private boolean isLockOwnedBySwitch()
	{
		return lockState>0;
	}

	private boolean isLockOwnedByMethod()
	{
		return lockState<0;
	}
	
	private void takeMethodLock()
	{
		assert(lockState<=0);
		lockState--;
	}
	
	private void returnMethodLock()
	{
		assert(lockState<0);
		lockState++;
		
		if(lockState>=0)
		{
			switchCondition.signalAll();
		}
	}
	
	private void takeSwitchLock()
	{
		assert(lockState>=0);
		lockState++;
	}
	
	private void returnSwitchLock()
	{
		assert(lockState>0);
		lockState--;
		
		if(lockState<=0)
		{
			methodCondition.signalAll();
		}
	}
	
	public void lockSwitch()
	{
		switchLock.lock();

		synLock.lock();
		try
		{
			while(!grabSwitchLock())
			{
				switchCondition.awaitUninterruptibly();
			}
		}finally
		{
			synLock.unlock();
		}
		
	}
	
	public void unlockSwitch()
	{
		synLock.lock();

		try
		{
			returnSwitchLock();
		} finally
		{
			synLock.unlock();
		}
		
		switchLock.unlock();
	}
	
	private boolean grabSwitchLock()
	{
		if (isLockIdle())
		{
			assert(threadRecorder.isEmpty());
			takeSwitchLock();
			drivingMethodThreads=false;
			return true;
		} else if (isLockOwnedByMethod())
		{
			assert(!drivingMethodThreads);
			drivingMethodThreads=true;
			driveOutMethodThreads();
			return false;
		} else if (isLockOwnedBySwitch())
		{
			takeSwitchLock();
			assert(!drivingMethodThreads);
			return true;
		} else
		{
			assert (false);
			return false;
		}
	}
	
	private void driveOutMethodThreads()
	{
		assert(isLockOwnedByMethod());
		for(Thread thread : threadRecorder.keySet())
		{
			assert(thread!=null);
			if(thread!=null)
			{
				thread.interrupt();
			}
		}
	}

	public void lockMethod()
	{
		synLock.lock();

		try
		{
			while (!grabMethodLock())
			{
				methodCondition.awaitUninterruptibly();
			}
			
			threadRecorder.recordThread();
		} finally
		{
			synLock.unlock();
		}
	}

	public void unlockMethod()
	{
		synLock.lock();

		try
		{
			returnMethodLock();
		} finally
		{
			threadRecorder.disrecordThread();
			synLock.unlock();
		}
	}
	
	private boolean grabMethodLock()
	{
		if(drivingMethodThreads)
		{
			return false;
		}
		
		if (isLockIdle())
		{
			assert(threadRecorder.isEmpty());
			takeMethodLock();
			return true;
		} else if (isLockOwnedByMethod())
		{
			takeMethodLock();
			return true;
		} else if (isLockOwnedBySwitch())
		{
			return false;
		} else
		{
			assert (false);
			return false;
		}
	}

}
