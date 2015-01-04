package com.accela.TestCases.container;

import java.lang.reflect.Method;
import java.util.LinkedList;
import java.util.List;

import com.accela.Container.Container;
import com.accela.Container.TestingOverlappedKeysRandom;

import junit.framework.TestCase;

/**
 * 
 * 这个测试用例因为涉及多线程，无法自动完成。它需要人工的调试。
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
	 * 这个方法只帮你建立调试用的线程。你需要：
	 * 1、检查Container中所有向容器中加入和删除Item的方法，包括clear(),以及lockModification()、
	 * unlockModification()方法，是否只能串行化执行
	 * 2、lookup方法是否可以并发进行
	 * 3、所有容器中加入和删除Item的方法，包括clear()方法，是一组，lookup方法是另一组。
	 * 两组方法同一时间只能有一组有线程运行。但lockModification方法和unlockModification
	 * 方法和上述两组在这个性质上不相干。
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
	 * 这个方法先用多线程程将Container轰炸一翻，再用单线程测试，
	 * 然后再轰炸一翻
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
