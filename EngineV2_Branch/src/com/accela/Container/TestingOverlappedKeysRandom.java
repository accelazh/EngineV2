package com.accela.Container;

import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.Map;
import java.util.StringTokenizer;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;

import junit.framework.TestCase;

public class TestingOverlappedKeysRandom extends TestCase
{
	public void testOverlappedKeyRandom()
	{
		// test 1
		for (int i = 0; i < 200; i++)
		{
			randomPut();
			assert (checkNonOverlappedKey());
		}

		Container.clear();
		assert (Container.isEmpty());

		for (int i = 0; i < 200; i++)
		{
			randomPut();
			randomRemove();
			assert (checkNonOverlappedKey());
		}

		Container.clear();
		assert (Container.isEmpty());

		for (int i = 0; i < 200; i++)
		{
			randomPut();
			randomRemove();
			randomLookup();
			assert (checkNonOverlappedKey());
		}

		Container.clear();
		assert (Container.isEmpty());

		// test 2
		for (int i = 0; i < 200; i++)
		{
			randomPut();
			assert (checkNonOverlappedKey());
		}

		for (int i = 0; i < 200; i++)
		{
			randomPut();
			randomRemove();
			assert (checkNonOverlappedKey());
		}

		for (int i = 0; i < 200; i++)
		{
			randomPut();
			randomRemove();
			randomLookup();
			assert (checkNonOverlappedKey());
		}

	}

	public String generateRandomStr()
	{
		int length = (int) (Math.random() * 4);
		StringBuffer b = new StringBuffer();

		for (int i = 0; i < length; i++)
		{
			b.append((int) (Math.random() * ('z' - 'a')) + 'a');
		}

		return b.toString();
	}

	public Object generateRandomObj()
	{
		int rand = (int) (Math.random() * 11);
		switch (rand)
		{
		case 0:
			return new Double(Math.PI);

		case 1:
			return new String("good");
		case 2:
			return new StringBuffer("g");
		case 3:
			return new StringTokenizer("88888");
		case 4:
			return new JFrame();
		case 5:
			return new JButton();
		case 6:
			return new LinkedList<JPanel>();

		case 7:
			return new TestingOverlappedKeysRandom();
		case 8:
			return new String(generateRandomStr());
		case 9:
			return generateRandomObj().getClass();
		case 10:
			return generateRandomObj().hashCode();
		default:
			assert (false);
			throw new IllegalStateException();
		}
	}

	public void randomPut()
	{
		int rand = (int) (Math.random() * 3);

		try
		{
			switch (rand)
			{
			case 0:
				Container.putGlobalItem(generateRandomStr(),
						generateRandomObj());
				break;
			case 1:
				Container.putClassItem(generateRandomObj().getClass(),
						generateRandomStr(), generateRandomObj());
				break;
			case 2:
				Container.putInstanceItem(generateRandomObj(),
						generateRandomStr(), generateRandomObj());
				break;
			default:
				assert (false);
				throw new IllegalStateException();
			}
		} catch (Exception ex)
		{
			// ex.printStackTrace();
		}
	}

	public void randomRemove()
	{
		int rand = (int) (Math.random() * 3);

		switch (rand)
		{
		case 0:
			Container.removeGlobalItem(generateRandomStr());
			break;
		case 1:
			Container.removeClassItem(generateRandomObj().getClass(),
					generateRandomStr());
			break;
		case 2:
			Container.removeInstanceItem(generateRandomObj(),
					generateRandomStr());
			break;
		default:
			assert (false);
			throw new IllegalStateException();
		}

	}

	public void randomLookup()
	{
		Container.lookup(generateRandomObj(), generateRandomStr());
	}

	@SuppressWarnings("unchecked")
	private boolean checkNonOverlappedKey()
	{
		Map<ItemKey, Object> map = null;
		try
		{
			Field f = Container.class.getDeclaredField("itemHolder");
			f.setAccessible(true);
			map = (Map<ItemKey, Object>) f.get(null);
			// map=(Map<ItemKey,
			// Object>)Container.class.getDeclaredField("itemHolder").get(null);
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}

		for (ItemKey key1 : map.keySet())
		{
			for (ItemKey key2 : map.keySet())
			{
				if (key1.equals(key2) != key2.equals(key1))
				{
					assert (false);
				}

				if (key1 == key2)
				{
					assert (!checkKeyNonOverlap(key1, key2));
				} else if (key1.equals(key2))
				{
					assert (false);
				} else if (key2.equals(key1))
				{
					assert (false);
				} else
				{
					assert (checkKeyNonOverlap(key1, key2));
					if (!checkKeyNonOverlap(key1, key2))
					{
						return false;
					}
				}

			}
		}

		return true;
	}

	private boolean checkKeyNonOverlap(ItemKey key1, ItemKey key2)
	{
		if (key1.overlapped(key2) == key2.overlapped(key1))
		{
			return !key1.overlapped(key2);
		} else
		{
			throw new IllegalStateException();
		}
	}

}
