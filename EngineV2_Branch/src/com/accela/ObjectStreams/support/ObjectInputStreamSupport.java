package com.accela.ObjectStreams.support;

import java.io.DataInputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;

import com.accela.ReflectionSupport.FieldExtractor;
import com.accela.ReflectionSupport.ObjectCreator;

/**
 * 
 * �����ΪHPObjectInputStream�������ܶ�������
 * 
 */
public class ObjectInputStreamSupport extends ObjectStreamSupport
{
	/**
	 * ��¼��ȡ�Ķ�������Ϊ�˶Ը�ѭ��Ӧ�����͵Ķ����д
	 */
	private final ObjectInputStreamObjectRecorder objectRecorder = new ObjectInputStreamObjectRecorder();

	/**
	 * ����DataInputStream�Ķ�ȡ����ķ���������ΪHPObjectInputStream �ṩ�ķ���
	 * 
	 * @param in
	 *            DataInputStream����
	 * @return ��ȡ���Ķ���
	 * @throws IOException
	 *             IO�쳣
	 * @throws ClassNotFoundException
	 *             û���ҵ������Ӧ����
	 * @throws InstantiationException
	 *             �½�����ʵ����ʱ������쳣
	 */
	public synchronized Object readObject(DataInputStream in)
			throws IOException, ClassNotFoundException, InstantiationException
	{
		if (null == in)
		{
			throw new NullPointerException("in should not be null");
		}

		Object result = readObjectImpl(in);

		objectRecorder.clearObjectRecord();

		return result;
	}

	private Object readObjectImpl(DataInputStream in) throws IOException,
			ClassNotFoundException, InstantiationException
	{
		// ��ȡ�������ݵ����ͱ��
		int dataType = in.readInt();

		if (ObjectStreamSupport.NULL_OBJECT == dataType)
		{
			return readNullObject(in);
		} else if (ObjectStreamSupport.REUSE_OBJECT == dataType)
		{
			return readReuseObject(in);
		} else if (ObjectStreamSupport.NORMAL_OBJECT == dataType)
		{
			int objectId = in.readInt();
			assert (objectId >= 0);
			Object result = readNormalObject(objectId, in);
			assert (result != null);

			return result;
		} else
		{
			assert (false);
			throw new IOException(
					"illegal data type identifier. the data is corrupted!");
		}

	}

	private Object readNormalObject(int objectId, DataInputStream in)
			throws IOException, ClassNotFoundException, InstantiationException
	{
		// ��ȡ������
		String name = readName(in);

		Object object = null;

		Class<?> objectClass = Class.forName(name);

		if (objectClass.isArray())
		{
			// ��ȡ���鳤��
			int length = in.readInt();
			assert (length >= 0);

			// �½�����
			object = Array.newInstance(objectClass.getComponentType(), length);
			assert (object != null);

			// ��¼��������Ķ���
			objectRecorder.recordObject(objectId, object);

			// ��������
			if (objectClass == int[].class)
			{
				readIntArray((int[]) object, in);
			} else if (objectClass == boolean[].class)
			{
				readBooleanArray((boolean[]) object, in);
			} else if (objectClass == double[].class)
			{
				readDoubleArray((double[]) object, in);
			} else if (objectClass == float[].class)
			{
				readFloatArray((float[]) object, in);
			} else if (objectClass == long[].class)
			{
				readLongArray((long[]) object, in);
			} else if (objectClass == char[].class)
			{
				readCharArray((char[]) object, in);
			} else if (objectClass == byte[].class)
			{
				readByteArray((byte[]) object, in);
			} else if (objectClass == short[].class)
			{
				readShortArray((short[]) object, in);
			} else
			{
				readObjectArray((Object[]) object, in);
			}
		} else if (objectClass.isEnum())
		{
			object = readEnum(objectClass, in);

			// ��¼��������Ķ���
			objectRecorder.recordObject(objectId, object);
		} else
		{
			// ���ݶ������½�����
			object = createObject(objectClass);

			if (null == object)
			{
				throw new InstantiationException(
						"failed to create the object (class = "+objectClass+"). "
								+ "you should give the object a nullary constructor");
			}

			// ��¼��������Ķ���
			objectRecorder.recordObject(objectId, object);

			if (object instanceof Collection)
			{
				readCollection((Collection<?>) object, in);
			} else if (object instanceof Map)
			{
				readMap((Map<?, ?>) object, in);
			} else
			{
				// ��ȡ�����ֶ�
				readFields(object, in);
			}

		}

		return object;
	}

	@SuppressWarnings("unchecked")
	private void readMap(Map<?, ?> map, DataInputStream in) throws IOException,
			ClassNotFoundException, InstantiationException
	{
		map.clear();

		int size = in.readInt();
		assert (size >= 0);

		for (int i = 0; i < size; i++)
		{
			Object key = readObjectImpl(in);
			Object value = readObjectImpl(in);

			((Map) map).put(key, value);
		}

	}

	@SuppressWarnings("unchecked")
	private void readCollection(Collection<?> collection, DataInputStream in)
			throws IOException, ClassNotFoundException, InstantiationException
	{
		collection.clear();

		int size = in.readInt();
		assert (size >= 0);

		for (int i = 0; i < size; i++)
		{
			Object value = readObjectImpl(in);

			((Collection) collection).add(value);
		}
	}

	private Object readReuseObject(DataInputStream in) throws IOException
	{
		int objectId = in.readInt();
		Object result = objectRecorder.getRecordedObject(objectId);

		assert (result != null);

		return result;
	}

	private Object readNullObject(DataInputStream in)
	{
		// read nothing
		return null;
	}

	@SuppressWarnings("unchecked")
	private Object readEnum(Class<?> objectClass, DataInputStream in)
			throws IOException
	{
		assert (objectClass.isEnum());

		String name = in.readUTF();
		Class<? extends Enum<?>> enumClass = (Class<? extends Enum<?>>) objectClass;
		Enum<?>[] enumConstants = enumClass.getEnumConstants();

		for (int i = 0; i < enumConstants.length; i++)
		{
			if (enumConstants[i].name().equals(name))
			{
				return enumConstants[i];
			}
		}

		return null;
	}

	private ObjectCreator objectCreator = new ObjectCreator();

	private Object createObject(Class<?> objectClass)
	{
		return objectCreator.createObject(objectClass);
	}

	private void readShortArray(short[] shorts, DataInputStream in)
			throws IOException
	{
		for (int i = 0; i < shorts.length; i++)
		{
			shorts[i] = in.readShort();
		}

	}

	private void readByteArray(byte[] bytes, DataInputStream in)
			throws IOException
	{
		for (int i = 0; i < bytes.length; i++)
		{
			bytes[i] = in.readByte();
		}

	}

	private void readCharArray(char[] chars, DataInputStream in)
			throws IOException
	{
		for (int i = 0; i < chars.length; i++)
		{
			chars[i] = in.readChar();
		}

	}

	private void readLongArray(long[] longs, DataInputStream in)
			throws IOException
	{
		for (int i = 0; i < longs.length; i++)
		{
			longs[i] = in.readLong();
		}

	}

	private void readFloatArray(float[] floats, DataInputStream in)
			throws IOException
	{
		for (int i = 0; i < floats.length; i++)
		{
			floats[i] = in.readFloat();
		}

	}

	private void readDoubleArray(double[] doubles, DataInputStream in)
			throws IOException
	{
		for (int i = 0; i < doubles.length; i++)
		{
			doubles[i] = in.readDouble();
		}

	}

	private void readBooleanArray(boolean[] booleans, DataInputStream in)
			throws IOException
	{
		for (int i = 0; i < booleans.length; i++)
		{
			booleans[i] = in.readBoolean();
		}

	}

	private void readIntArray(int[] ints, DataInputStream in)
			throws IOException
	{
		for (int i = 0; i < ints.length; i++)
		{
			ints[i] = in.readInt();
		}

	}

	private void readObjectArray(Object[] objects, DataInputStream in)
			throws IOException, ClassNotFoundException, InstantiationException
	{
		for (int i = 0; i < objects.length; i++)
		{
			objects[i] = readObjectImpl(in);
		}

	}

	private String readName(DataInputStream in) throws IOException
	{
		return in.readUTF();
	}

	private void readFields(Object object, DataInputStream in)
			throws IOException, ClassNotFoundException, InstantiationException
	{
		FieldExtractor fieldExtractor = getFieldExtractor();
		List<Field[]> fieldList = fieldExtractor.getSortedFieldsList(object
				.getClass());

		for (Field[] fields : fieldList)
		{
			for (Field f : fields)
			{
				if (Modifier.isStatic(f.getModifiers()))
				{
					break;
				}
				readField(object, f, in);
			}
		}

		disposeFieldExtractor(fieldExtractor);

	}

	private void readField(Object object, Field field, DataInputStream in)
			throws IOException, ClassNotFoundException, InstantiationException
	{
		assert (!Modifier.isStatic(field.getModifiers()));

		Class<?> fieldType = field.getType();
		field.setAccessible(true);

		try
		{
			if (fieldType.isPrimitive())
			{
				if (fieldType == int.class)
				{
					field.setInt(object, in.readInt());
				} else if (fieldType == boolean.class)
				{
					field.setBoolean(object, in.readBoolean());

				} else if (fieldType == double.class)
				{
					field.setDouble(object, in.readDouble());

				} else if (fieldType == float.class)
				{
					field.setFloat(object, in.readFloat());

				} else if (fieldType == long.class)
				{
					field.setLong(object, in.readLong());

				} else if (fieldType == char.class)
				{
					field.setChar(object, in.readChar());

				} else if (fieldType == byte.class)
				{
					field.setByte(object, in.readByte());

				} else if (fieldType == short.class)
				{
					field.setShort(object, in.readShort());

				} else
				{
					assert (false);
				}
			} else
			{
				Object obj = readObjectImpl(in);
				field.set(object, obj);
			}
		} catch (IllegalArgumentException ex)
		{
			ex.printStackTrace();
			assert (false);
		} catch (IllegalAccessException ex)
		{
			ex.printStackTrace();
			assert (false);
		} catch (IOException ex)
		{
			throw ex;
		} catch (ClassNotFoundException ex)
		{
			throw ex;
		} catch (InstantiationException ex)
		{
			throw ex;
		}

	}

}
