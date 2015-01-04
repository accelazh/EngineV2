package com.accela.ObjectStreams.support;

import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.Map.Entry;

import com.accela.ReflectionSupport.FieldExtractor;

/**
 * 
 * �����ΪHPObjectOutputStream�������ܶ������
 *
 */
public class ObjectOutputStreamSupport extends ObjectStreamSupport
{
	private final ObjectOutputStreamObjectRecorder objectRecorder=new ObjectOutputStreamObjectRecorder();
	
	/**
	 * ����DataObjectOutputStream��д������ķ���
	 * @param object ����д���Ķ���
	 * @param out �����
	 * @throws IOException IO�쳣
	 */
	public synchronized void writeObject(Object object, DataOutputStream out) throws IOException
	{
		if (null == out)
		{
			throw new NullPointerException("out should not be null");
		}
		
		writeObjectImpl(object, out);
		
		objectRecorder.clearObjectRecord();
	}
	
	// TODO д���鷽��δ������
	// Object,null,Object[],primitive[],enum,����дѭ������
	private void writeObjectImpl(Object object, DataOutputStream out)
			throws IOException
	{
		if(isNullObject(object))
		{
			out.writeInt(ObjectStreamSupport.NULL_OBJECT);
			writeNullObject(object, out);
			
		}else if(isReuseObject(object))
		{	
			out.writeInt(ObjectStreamSupport.REUSE_OBJECT);
			writeReuseObject(object, out);
		}
		else
		{
			out.writeInt(ObjectStreamSupport.NORMAL_OBJECT);
			objectRecorder.recordObject(object);
			out.writeInt(objectRecorder.getObjectId(object));
			writeNormalObject(object, out);
		}

	}

	private void writeNormalObject(Object object, DataOutputStream out) throws IOException
	{
		assert(object!=null);
		
		// д�����������
		writeName(object, out);

		if (object.getClass().isArray())
		{
			// д������
			if (object instanceof int[])
			{
				writeIntArray((int[]) object, out);
			} else if (object instanceof boolean[])
			{
				writeBooleanArray((boolean[]) object, out);
			} else if (object instanceof double[])
			{
				writeDoubleArray((double[]) object, out);
			} else if (object instanceof float[])
			{
				writeFloatArray((float[]) object, out);
			} else if (object instanceof long[])
			{
				writeLongArray((long[]) object, out);
			} else if (object instanceof char[])
			{
				writeCharArray((char[]) object, out);
			} else if (object instanceof byte[])
			{
				writeByteArray((byte[]) object, out);
			} else if (object instanceof short[])
			{
				writeShortArray((short[]) object, out);
			} else
			{
				writeObjectArray((Object[]) object, out);
			}

		} else if (object.getClass().isEnum())
		{
			writeEnum(object, out);
		} else
		{
			if(object instanceof Collection)
			{
				//out.writeInt(NORMAL_OBJECT_COLLECTION);
				
				//�Լ�����ķ�ʽд������
				writeCollection((Collection<?>)object, out);
			}
			else if(object instanceof Map)
			{
				//out.writeInt(NORMAL_OBJECT_MAP);
				
				//��Map�ķ�ʽд������
				writeMap((Map<?, ?>)object, out);
			}
			else
			{
				//out.writeInt(NORMAL_OBJECT_COMMON);
				
				// д��������ֶ�
				writeFields(object, out);
			}
			
		}
		
	}

	private void writeCollection(Collection<?> collection, DataOutputStream out) throws IOException
	{
		int size=collection.size();
		out.writeInt(size);
		
		int i=0;
		for(Object element : collection)
		{
			writeObjectImpl(element, out);
			i++;
		}
	}

	private void writeMap(Map<?, ?> map, DataOutputStream out) throws IOException
	{
		int size=map.size();
		out.writeInt(size);
		for(Entry<?, ?> element : map.entrySet())
		{
			writeObjectImpl(element.getKey(), out);
			writeObjectImpl(element.getValue(), out);
		}
	}

	private void writeReuseObject(Object object, DataOutputStream out) throws IOException
	{
		out.writeInt(objectRecorder.getObjectId(object));
	}

	private void writeNullObject(Object object, DataOutputStream out)
	{
		assert(null==object);
		//write nothing
	}

	private boolean isReuseObject(Object object)
	{
		return objectRecorder.hasRecordOf(object);
	}

	private boolean isNullObject(Object object)
	{
		return null==object;
	}

	private void writeEnum(Object object, DataOutputStream out)
			throws IOException
	{
		assert (object.getClass().isEnum());

		out.writeUTF(((Enum<?>) object).name());
	}

	private void writeShortArray(short[] objects, DataOutputStream out)
			throws IOException
	{
		out.writeInt(objects.length);

		for (int i = 0; i < objects.length; i++)
		{
			out.writeShort(objects[i]);
		}

	}

	private void writeByteArray(byte[] objects, DataOutputStream out)
			throws IOException
	{
		out.writeInt(objects.length);

		for (int i = 0; i < objects.length; i++)
		{
			out.writeByte(objects[i]);
		}

	}

	private void writeCharArray(char[] objects, DataOutputStream out)
			throws IOException
	{
		out.writeInt(objects.length);

		for (int i = 0; i < objects.length; i++)
		{
			out.writeChar(objects[i]);
		}

	}

	private void writeLongArray(long[] objects, DataOutputStream out)
			throws IOException
	{
		out.writeInt(objects.length);

		for (int i = 0; i < objects.length; i++)
		{
			out.writeLong(objects[i]);
		}

	}

	private void writeFloatArray(float[] objects, DataOutputStream out)
			throws IOException
	{
		out.writeInt(objects.length);

		for (int i = 0; i < objects.length; i++)
		{
			out.writeFloat(objects[i]);
		}

	}

	private void writeDoubleArray(double[] objects, DataOutputStream out)
			throws IOException
	{
		out.writeInt(objects.length);

		for (int i = 0; i < objects.length; i++)
		{
			out.writeDouble(objects[i]);
		}

	}

	private void writeBooleanArray(boolean[] objects, DataOutputStream out)
			throws IOException
	{
		out.writeInt(objects.length);

		for (int i = 0; i < objects.length; i++)
		{
			out.writeBoolean(objects[i]);
		}

	}

	private void writeIntArray(int[] objects, DataOutputStream out)
			throws IOException
	{
		out.writeInt(objects.length);

		for (int i = 0; i < objects.length; i++)
		{
			out.writeInt(objects[i]);
		}
	}

	private void writeObjectArray(Object[] objects, DataOutputStream out)
			throws IOException
	{
		out.writeInt(objects.length);

		for (int i = 0; i < objects.length; i++)
		{
			writeObjectImpl(objects[i], out);
		}

	}

	private void writeName(Object object, DataOutputStream out)
			throws IOException
	{
		out.writeUTF(object.getClass().getName());
	}

	private void writeFields(Object object, DataOutputStream out)
			throws IOException
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
				writeField(object, f, out);
			}
		}

		disposeFieldExtractor(fieldExtractor);

	}

	private void writeField(Object object, Field field, DataOutputStream out)
			throws IOException
	{
		assert(!Modifier.isStatic(field.getModifiers()));

		Class<?> fieldType = field.getType();
		field.setAccessible(true);
		try
		{
			if (fieldType.isPrimitive())
			{
				if (fieldType == int.class)
				{
					out.writeInt(field.getInt(object));

				} else if (fieldType == boolean.class)
				{
					out.writeBoolean(field.getBoolean(object));

				} else if (fieldType == double.class)
				{
					out.writeDouble(field.getDouble(object));

				} else if (fieldType == float.class)
				{
					out.writeFloat(field.getFloat(object));

				} else if (fieldType == long.class)
				{
					out.writeLong(field.getLong(object));

				} else if (fieldType == char.class)
				{
					out.writeChar(field.getChar(object));

				} else if (fieldType == byte.class)
				{
					out.writeByte(field.getByte(object));

				} else if (fieldType == short.class)
				{
					out.writeShort(field.getShort(object));

				} else
				{
					assert (false);
				}
			} else
			{
				if(!Modifier.isTransient(field.getModifiers()))
				{
					writeObjectImpl(field.get(object), out);
				}
				else
				{
					writeObjectImpl(null, out);
				}
			}

		} catch (IllegalArgumentException ex)
		{
			ex.printStackTrace();
			assert (false);
		} catch (IOException ex)
		{
			throw ex;
		} catch (IllegalAccessException ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

}
