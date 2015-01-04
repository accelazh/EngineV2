package com.accela.TestCases.container;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.accela.Container.Container;
import com.accela.Container.TestingOverlappedKeysRandom;

import junit.framework.TestCase;

/**
 * 
 * �������������Ϊ�漰���̣߳��޷��Զ���ɡ�����Ҫ�˹��ĵ��ԡ�
 *
 */
public class TestingContainerMultiThread extends TestCase
{
	
	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		
		if(Container.isModificationLocked())
		{
			throw new IllegalStateException("Container is locked!");
		}
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
	}
	
	/**
	 * �������ֻ���㽨�������õ��̡߳�����Ҫ��
	 * 1�����Container�������������м����ɾ��Item�ķ���������clear(),�Լ�lockModification()��
	 * unlockModification()�������Ƿ�ֻ�ܴ��л�ִ��
	 * 2��lookup�����Ƿ���Բ�������
	 * 3�����������м����ɾ��Item�ķ���������clear()��������һ�飬lookup��������һ�顣
	 * ���鷽��ͬһʱ��ֻ����һ�����߳����С���lockModification������unlockModification
	 * ������������������������ϲ���ɡ�
	 * 
	 * 
	 */
	public void manualTestContainerMultiThread()
	{
		List<MethodRunnerForTest> list=new LinkedList<MethodRunnerForTest>();
		Method[] methods=Container.class.getMethods();
		assert(methods.length>0);
		for(Method m: methods)
		{
			assert(m!=null);
			String name=m.getName();
			name="- "+name+" -"+m.isAccessible();
			
			if(!(m.getDeclaringClass()==Container.class))
			{
				continue;
			}
			
			m.setAccessible(true);
			if(m.getName().equals("lookup"))
			{
				list.add(new MethodRunnerForTest(m.getName(), m));
				list.add(new MethodRunnerForTest(m.getName(), m));
			}
			list.add(new MethodRunnerForTest(m.getName(), m));
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.start();
		}
		
		try
		{
			Thread.sleep(10000);
		} catch (InterruptedException ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.interrupt();
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.close();
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.interrupt();
		}
		
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			assert(!thread.isAlive());
		}
		
	}
	
	/**
	 * ����������ö��̳߳̽�Container��ըһ�������õ��̲߳��ԣ�
	 * Ȼ���ٺ�ըһ��
	 */
	public void testContainerMultiThreadWithSingleThreadCheck()
	{
		List<MethodRunnerForTest> list=new LinkedList<MethodRunnerForTest>();
		Method[] methods=Container.class.getMethods();
		assert(methods.length>0);
		for(Method m: methods)
		{
			assert(m!=null);
			String name=m.getName();
			name="- "+name+" -"+m.isAccessible();
			
			if(m.getName().equals("lockModification"))
			{
				continue;
			}
			
			if(!(m.getDeclaringClass()==Container.class))
			{
				continue;
			}
			
			m.setAccessible(true);
			if(m.getName().equals("lookup"))
			{
				list.add(new MethodRunnerForTest(m.getName(), m));
				list.add(new MethodRunnerForTest(m.getName(), m));
			}
			list.add(new MethodRunnerForTest(m.getName(), m));
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.start();
		}
		
		try
		{
			Thread.sleep(10000);
		} catch (InterruptedException ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.interrupt();
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.close();
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.interrupt();
		}
		
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			assert(!thread.isAlive());
		}
		
		TestingOverlappedKeysRandom test=new TestingOverlappedKeysRandom();
		test.testOverlappedKeyRandom();
		
		new Thread(new Runnable(){
			public void run()
			{
				
			}
		}).start();
		
		bomb();
		
		try
		{
			Thread.sleep(60000);
		} catch (InterruptedException ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
	}
	
	private void bomb()
	{
		List<MethodRunnerForTest> list=new LinkedList<MethodRunnerForTest>();
		Method[] methods=Container.class.getMethods();
		assert(methods.length>0);
		for(Method m: methods)
		{
			assert(m!=null);
			String name=m.getName();
			name="- "+name+" -"+m.isAccessible();
			
			if(!(m.getDeclaringClass()==Container.class))
			{
				continue;
			}
			
			m.setAccessible(true);
			if(m.getName().equals("lookup"))
			{
				list.add(new MethodRunnerForTest(m.getName(), m));
				list.add(new MethodRunnerForTest(m.getName(), m));
			}
			list.add(new MethodRunnerForTest(m.getName(), m));
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.start();
		}
		
		try
		{
			Thread.sleep(10000);
		} catch (InterruptedException ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.interrupt();
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.close();
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			thread.interrupt();
		}
		
		try
		{
			Thread.sleep(2000);
		} catch (InterruptedException ex)
		{
			ex.printStackTrace();
			assert(false);
		}
		
		for(MethodRunnerForTest thread : list)
		{
			String name=thread.getName();
			name="- "+name+" -";
			assert(!thread.isAlive());
		}
	}

}
