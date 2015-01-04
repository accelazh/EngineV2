package com.accela.ObjectStreams.support;

import com.accela.ReflectionSupport.FieldExtractor;

/**
 * 
 * �������ObjectInputStreamSupport��ObjectOutputStream
 * �ĸ��࣬Ϊ����һ��һЩ���õķ����Լ�����һЩ���õ���
 *
 */
abstract class ObjectStreamSupport
{
	/**
	 * ���һ��FieldExtractor����ͨ��������������FieldExtractor
	 * ������ܹ��Ż������½��� 
	 */
	protected FieldExtractor getFieldExtractor()
	{
		return FieldExtractor.getFieldExtractor();
	}
	
	/**
	 * �ͷ�һ��FieldExtractor����ͨ������������ͷ�FieldExtractor
	 * ������ܹ��Ż������ͷš� 
	 */
	protected void disposeFieldExtractor(FieldExtractor fieldExtractor)
	{
		if(null==fieldExtractor)
		{
			throw new NullPointerException("fieldExtractor should not be null");
		}
		
		FieldExtractor.disposeFieldExtractor(fieldExtractor);
	}
	
	/**
	 * д�뵽���е��������͵ı�ʶ��
	 * �������д�����null
	 */
	protected static final int NULL_OBJECT=0;
	/**
	 * д�뵽���е��������͵ı�ʶ��
	 * �������д�����һ���Ѿ�д�����
	 * ���󡣳�����������ĳ����Ǿ���ѭ
	 * �����õĶ���
	 */
	protected static final int REUSE_OBJECT=1;
	/**
	 * д�뵽���е��������͵ı�ʶ��
	 * �������д�����һ�������Ķ��󣬼���������
	 * �ֵĶ���
	 */
	protected static final int NORMAL_OBJECT=2;
	
	/*protected static final int NORMAL_OBJECT_COMMON=3;
	protected static final int NORMAL_OBJECT_COLLECTION=4;
	protected static final int NORMAL_OBJECT_MAP=5;*/

}
