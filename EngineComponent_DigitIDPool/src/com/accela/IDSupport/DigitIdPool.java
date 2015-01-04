package com.accela.IDSupport;

/**
 * 
 * DigitIDPool��������long���͵�id�š�
 * �����ȥ��id����Ҫ�黹�ſ������á�
 * �κ������Ѿ�����Ĳ���û�б��黹
 * ��id�Ŷ������ظ���
 * 
 * ֧�ֶ��̡߳�
 *
 */
public class DigitIdPool
{
	private IDTable idTable;
	
	public DigitIdPool()
	{
		idTable=new IDTable();
	}
	
	public DigitIdPool(long initSize, double loadFactor)
	{
		idTable=new IDTable(initSize, loadFactor);
	}
	
	/**
	 * @return ����һ��id�š���֤ÿ�η����ȥ��id�ţ�����
	 * ���κ�һ����ȥ�����ȥ����û�б��黹��id����ͬ��
	 * id����֮��Ӧ��ͨ��freeId(long)�����黹�����򱻷����
	 * ȥ��id�ž���Զ����ʹ���ˡ�
	 */
	public long allocId()
	{
		return idTable.allocId();
	}

	/**
	 * �黹һ��id�ţ�����㲻�黹ͨ��allocId()���������id�ţ�
	 * ���id�Ž���Զ���ܱ�����ʹ�á�
	 */
	public void freeId(long id)
	{
		idTable.freeId(id);
	}
	
	/**
	 * 
	 * ��������long���͵�id�š� 
	 * �����ȥ��id����Ҫ�黹�ſ������á� 
	 * �κ������Ѿ�����Ĳ���û�б��黹 
	 * ��id�Ŷ������ظ���
	 * 
	 * ֧�ֶ��̡߳�
	 * 
	 */
	private static class IDTable
	{
		/**
		 * ���ű��м�¼����Щid���Ѿ���ռ�á�
		 * ���table[i]==false�����ʾid��i
		 * û�б�ʹ�á����table[i]==true����
		 * ��ʾid��i�Ѿ���ʹ�á�
		 */
		private BitArray table;
		/**
		 * ָ����һ��׼����̽�ܷ�����table���±�
		 */
		private long curIdx;
		/**
		 * װ�����ӡ����Ѿ������id�������Ͽ��Է����id����������loadFactor��ʱ��
		 * id��¼��ͻ����ʵ���ʱ�����ų�ԭ����������С
		 */
		private double loadFactor;
		/**
		 * �Ѿ������ȥ��id������
		 */
		private long numTakenId;
		
		/**
		 * �½�һ��IDTable����ʼ��СΪ16��װ������Ϊ0.75
		 */
		public IDTable()
		{
			this(16, 0.75);
		}
		
		/**
		 * �½�һ��IDTable��ָ����ʼID��Ĵ�С�Լ�װ������
		 * @param initSize
		 * @param loadFactor
		 */
		public IDTable(long initSize, double loadFactor)
		{
			final int BITS_PER_LONG=64;
			
			if(initSize<=0)
			{
				throw new IllegalArgumentException("initSize should not be null");
			}
			if(initSize>(long)Integer.MAX_VALUE*BITS_PER_LONG)
			{
				throw new IllegalArgumentException("initSize is two large");
			}
			if(loadFactor<=0||loadFactor>=1)
			{
				throw new IllegalArgumentException("loadFactor should be positive and less than 1");
			}
			
			//����table�ĳ�ʼ����С
			long nBlock=initSize/BITS_PER_LONG;
			if(initSize>nBlock*BITS_PER_LONG)
			{
				nBlock++;
			}
			assert(nBlock<=Integer.MAX_VALUE);
			if(nBlock>Integer.MAX_VALUE)
			{
				throw new IllegalArgumentException("initSize is two large");
			}
			
			//��ʼ��
			this.table=new BitArray((int)nBlock);
			this.loadFactor=loadFactor;
			
			curIdx=0;
			numTakenId=0;
		}
		
		/**
		 * @return ����һ��id�š���֤ÿ�η����ȥ��id�ţ�����
		 * ���κ�һ����ȥ�����ȥ����û�б��黹��id����ͬ��
		 * id����֮��Ӧ��ͨ��freeId(long)�����黹�����򱻷����
		 * ȥ��id�ž���Զ����ʹ���ˡ�
		 */
		public synchronized long allocId()
		{
			assert(numTakenId<=table.size());
						
			long newId=findFreeId();
			assert(!table.get(newId));
			
			table.set(newId);
			numTakenId++;
			return newId;
		}
		
		/**
		 * ����һ��û�б��ù���id�š����ҷ�ʽ����ƽ��̽�ⷨ��
		 * �������ָ��curIdx�Ѿ������table�����һ��Ԫ�أ�
		 * ��ô�ͻᰴ��װ�����ӿ����Ƿ�Ҫ����id��
		 *  
		 */
		private long findFreeId()
		{
			long jump=1;
			while(true)
			{
				if(curIdx>=table.size())
				{
					checkResize();
				}
				curIdx%=table.size();
				
				if(!table.get(curIdx))
				{
					break;
				}
				
				curIdx+=2*jump-1;
				jump++;
			}
						
			return curIdx++;
		}
		
		/**
		 * �黹һ��id�ţ�����㲻�黹ͨ��allocId()���������id�ţ�
		 * ���id�Ž���Զ���ܱ�����ʹ�á�
		 */
		public synchronized void freeId(long id)
		{
			if(id<0)
			{
				throw new IllegalArgumentException("id should not be negtive");
			}
			if(!table.get(id))
			{
				throw new IllegalArgumentException("this id has not been allocated yet");
			}
			
			numTakenId--;
			table.clear(id);
		}
		
		/**
		 * ����Ƿ���Ҫ����id�������Ҫ���������id��ķ�����
		 */
		private void checkResize()
		{
			if((double)numTakenId/(double)table.size()>loadFactor)
			{
				table.expend();
			}
			else
			{
				//do nothing
			}
		}

	}
	
}
