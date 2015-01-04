package com.accela.SynchronizeSupport.other;

import java.util.concurrent.locks.ReentrantLock;

/**
 * ���������������Դ�����������ȡ�ġ�
 * ������½�ʱ���ڲ���ͬʱ�½���numOfLocks�Ѳ�ͬ�������ֱ�������0...numOfLocks-1
 * ��ʾ����������κ�һ����ȥ����סһ�δ��룬���磺
 * <code>
 * TokenLock tokenLock=new TokenLock(3);
 * ...
 * ...
 * tokenLock.lock(0);
 * processing...
 * tokenLock.unlock(0);
 * ...
 * ...
 * tokenLock.lock(0);
 * processing...
 * tokenLock.unlock(0);
 * ...
 * ...
 * tokenLock.lock(1);
 * processing...
 * tokenLock.unlock(1);
 * ...
 * ...
 * tokenLock.lock(2);
 * processing...
 * tokenLock.unlock(2);
 * ...
 * ...
 * </code>
 * ������Ϊ��������������������ġ����0��������ס��tokenLock.lock(0)������ô
 * ��������������1������2�����Ȱ�ס�Ĵ���ν��޷����루��tokenLock.lock(1)�Ȼ�
 * ʹ�̵߳ȴ��������Ǳ�0������ס�Ĵ������Ȼ�����ظ����룬���²���ͬһ���̡߳�
 * ��0�������ͷţ�tokenLock.unlock(0)���󣬱�����������ס���߳̾ͻᾺ��������
 * ��Ӧ�Ĵ���Σ�����ֻ����ҽ���һ��������ס��
 * 
 * �ܵ���˵�����Ǹ�����ŵ���ȥ����һ�����ƣ�˭��������ƣ�˭��ס�Ĵ���ξͿ���
 * �����Ƶؽ��룬��������ŵ�������ס�Ĵ����ȴ���ܽ��롣ע�⣬һ����ŵ�������
 * ����������ס�������Ρ����磺
 * ...
 * ...
 * tokenLock.lock(0);
 * processing...
 * tokenLock.unlock(0);
 * ...
 * ...
 * tokenLock.lock(0);
 * processing...
 * tokenLock.unlock(0);
 * ...
 * ...
 * tokenLock.lock(1);
 * processing...
 * tokenLock.unlock(1);
 * ...
 * ...
 * tokenLock.lock(1);
 * processing...
 * tokenLock.unlock(1);
 * ...
 * ...
 * ����������£�TokenLock���Ա�֤��ͬһʱ�䣬Ҫôֻ�б�0������ס�Ĵ������ִ�У�
 * Ҫôֻ�б�1������ס�Ĵ������ִ�С���������0������ס�Ĵ����ȴ����ͬʱִ�У���
 * û����һ������ʵ��TokenLock����Ϊ�����������Ƶġ�
 */
public class TokenLock
{
	private static final int INVALID_TOKEN_HOLDER=-1;
	
	private int whichLockHasToken=INVALID_TOKEN_HOLDER;
	private int numOfLocks=-1;
	
	private int count=0;    //���Ľ������
	
	private Object waitLock=new Object();
	
	private ReentrantLock syncLock=new ReentrantLock();;
	
	public TokenLock(int numOfLocks)
	{
		if(numOfLocks<=0)
		{
			throw new IllegalArgumentException();
		}
		
		this.numOfLocks=numOfLocks;
	}
	
	public int getNumOfLocks()
	{
		return numOfLocks;
	}
	
	public void lock(int whichLockToLock) throws InterruptedException
	{
		if(whichLockToLock<0)
		{
			throw new IllegalArgumentException();
		}
		
		while (!tryGetToken(whichLockToLock))
		{
			synchronized (waitLock)
			{
				waitLock.wait();
			}
		}
	}
	
	/**
	 * ���ΪwhichLock������ͼ�õ����ơ�
	 * @param whichLock
	 * @return �Ƿ�õ�������
	 */
	private boolean tryGetToken(int whichLock)
	{
		syncLock.lock();
		try
		{
			if (INVALID_TOKEN_HOLDER == whichLockHasToken)
			{
				// ����û���ã����Ա��ΪwhichLock������������
				whichLockHasToken = whichLock;
				count=1;
				return true;
			} else if (whichLock == whichLockHasToken)
			{
				// ���ΪwhichLock�����Ѿ�ӵ�������ˣ��������
				count++;
				return true;
			} else
			{
				// �����ڳ���whichLock���������������ò�������
				return false;
			}
		} finally
		{
			syncLock.unlock();
		}
	}
	
	public void unlock(int whichLockToUnlock)
	{
		if(whichLockToUnlock<0)
		{
			throw new IllegalArgumentException();
		}
		
		syncLock.lock();
		try
		{
			if(whichLockToUnlock!=whichLockHasToken)
			{
				throw new IllegalStateException("It is illogical for a lock who doesn't has the token to unlock!");
			}
			
			count--;
			assert(count>=0);
			if(0==count)
			{
				whichLockHasToken=INVALID_TOKEN_HOLDER;
				synchronized(waitLock)
				{
					waitLock.notifyAll();
				}
			}
		} finally
		{
			syncLock.unlock();
		}
	}
	

}
