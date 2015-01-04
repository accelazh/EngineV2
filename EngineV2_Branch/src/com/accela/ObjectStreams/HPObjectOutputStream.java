package com.accela.ObjectStreams;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import com.accela.ObjectStreams.support.ObjectOutputStreamSupport;

/**
 * 
 * һ��HP���������(���������)���������������ɷ���д�ɡ�
 * ���Զ�ȡ�������
 * 1����ȡӵ�п���ͨ���������ù��캯���������Ķ��󡣹��캯������
 * ��˽�еģ�������޲ι��캯���������в����Ĺ��캯��Ҳ���ԣ�
 * ֻ���ٶȸ�����
 * 2���������ͣ���������Ԫ���ǻ������ͻ��Ƕ������͡�
 * null��
 * 3��ö�ٱ���
 * 4���ܹ�ʶ������Ϊtransient�Ķ������͵��ֶΣ��������ʱ����Զ�
 * дΪnull
 * 5���ܹ�ʶ��ʵ����Collection��Map�ӿڵĶ������Ż��ķ�ʽ�����
 * ���Ƕ������ǻ����ȥ�е�transient�ֶΣ�����û������transient
 * ���ֶ����
 * 6���ܹ�����Ͷ�ȡ����ѭ�����õĶ���
 * 
 * ������java.io.ObjectInputStream��java.io.ObjectOutputStream������
 * 1��������ļ���д��һ��������java.io.ObjectInputStream��ȡ����
 * ���ٴΣ���ֻ������һ��ʵ�������������з��Ͷ����ʱ��ᵼ�·��Ͷ���
 * ������һ������󣬸��ĸö����ٷ��ͣ������ն���ֻ�ܽ���һ�Σ���
 * ���Ľ��շ��صĶ��ǵ�һ�ν��յĶ���
 * 2����ObjectPool��ϣ��Ż����½�����ʱ���ڴ���䡣������ͬ���͵Ķ�
 * ���ʱ�����ÿ10ms��¡һ����������һ��(�������¡��java.io.ObjectInputStream��
 * ��ֻ�ܽ��յ�һ�η��͵Ķ���)��ʹ��java.io.ObjectInputStream���ն���
 * ��ʱ����½���������ʵ������ʹ�ͷ�Ҳ���Ա��������ա���ʹ��HPObjectInputStream
 * �������������ϣ������ͷŵ��Ķ��󣬱����ظ��½�ʵ����
 * 
 * ������Ҫע��ĵط���
 * 1��HPObjectOutputStream��HPObjectInputStream��java.io.ObjectInputStream
 * ��java.io.ObjectOutputStream�����ܼ��ݡ���һ�������д�Ķ�����һ��������
 * ���ܶ�ȡ��
 * 2��HPObjectOutputStream��HPObjectInputStreamʹ�÷���д�ɣ������������ܵ�ƿ
 * �������ܶ���ؿ��Դ����Ż���Ч�ʡ�HPObjectInputStreamʹ�÷��乹�캯������
 * ����ϵ�Ǹ������û���޲ι��캯����HPObjectInputStream�ͻ�����̽��������������
 * �вι��캯��������ͼ�½�����ʵ���������������˷ѣ������ڲ���ʹ���вι��캯��
 * ������£�����Ҫ�������дһ��˽�е��޲ι��캯����
 * 3��HPObjectOutputStream��HPObjectInputStreamʹ�÷��������ʶ���Ĺ��캯������
 * �Σ������������������SecurityManager����ֹ������ʹ��setAccessible(true)������
 * Ҳ�޷����ʵĻ���HPObjectOutputStream��HPObjectInputStream�ͻ��׳��쳣��
 * 
 * �ġ�δ�����BUG
 * 1����ʹ���з��֣���ʹ��HPObjectOutputStream��HPObjectInputStreamʱ����������£�
 * ������ķ���ConcurrentModificationException��ԭ���������ܺͶ�д�������йء�
 * 
 *
 */
public class HPObjectOutputStream extends DataOutputStream
{
	private ObjectOutputStreamSupport support = new ObjectOutputStreamSupport();
	
	public HPObjectOutputStream(OutputStream out)
	{
		super(out);
	}

	public void writeObject(Object object) throws IOException
	{
		support.writeObject(object, this);
	}

}
