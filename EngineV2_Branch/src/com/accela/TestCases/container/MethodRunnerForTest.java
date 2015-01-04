package com.accela.TestCases.container;

import java.lang.reflect.*;

import com.accela.Container.TestingOverlappedKeysRandom;

public class MethodRunnerForTest extends Thread
{
	private Method method;
	
	private boolean close=false;

	public MethodRunnerForTest(String name, Method method)
	{
		super(name);

		if (null == method)
		{
			throw new NullPointerException("method should not be null");
		}
		if (!method.isAccessible())
		{
			throw new IllegalArgumentException("method is not accessable");
		}

		this.method = method;
	}

	public void run()
	{
		while (!close)
		{
			Class<?>[] parameterTypes = method.getParameterTypes();
			Object[] parameters = new Object[parameterTypes.length];
			for (int i = 0; i < parameters.length; i++)
			{
				String name=parameterTypes[i].getName();
				name="- "+name+" -";
				if (parameterTypes[i].getName().equals("java.lang.Class"))
				{
					parameters[i] = ClassABA.class;
				} else if(parameterTypes[i]==Object.class)
				{
					TestingOverlappedKeysRandom test=new TestingOverlappedKeysRandom();
					parameters[i]=test.generateRandomObj();
				}
				else if(parameterTypes[i]==String.class)
				{
					TestingOverlappedKeysRandom test=new TestingOverlappedKeysRandom();
					parameters[i]=test.generateRandomStr();
				}
				else
				{
					try
					{
						parameters[i] = parameterTypes[i].newInstance();
					} catch (Exception ex)
					{
						ex.printStackTrace();
						assert (false);
					}
				}

			}

			try
			{
				method.invoke(null, parameters);
			} catch (Exception ex)
			{
				
			}
		}

	}
	
	public void close()
	{
		close=true;
	}

}
