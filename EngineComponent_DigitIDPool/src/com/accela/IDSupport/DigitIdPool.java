package com.accela.IDSupport;

/**
 * 
 * DigitIDPool用来分配long类型的id号。
 * 分配出去的id号需要归还才可以再用。
 * 任何两个已经分配的并且没有被归还
 * 的id号都不会重复。
 * 
 * 支持多线程。
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
	 * @return 分配一个id号。保证每次分配出去的id号，不会
	 * 和任何一个过去分配出去并且没有被归还的id号相同。
	 * id号用之后应该通过freeId(long)方法归还，否则被分配出
	 * 去的id号就永远不能使用了。
	 */
	public long allocId()
	{
		return idTable.allocId();
	}

	/**
	 * 归还一个id号，如果你不归还通过allocId()方法借出的id号，
	 * 则该id号将永远不能被重新使用。
	 */
	public void freeId(long id)
	{
		idTable.freeId(id);
	}
	
	/**
	 * 
	 * 用来分配long类型的id号。 
	 * 分配出去的id号需要归还才可以再用。 
	 * 任何两个已经分配的并且没有被归还 
	 * 的id号都不会重复。
	 * 
	 * 支持多线程。
	 * 
	 */
	private static class IDTable
	{
		/**
		 * 这张表中记录着哪些id号已经被占用。
		 * 如果table[i]==false，则表示id号i
		 * 没有被使用。如果table[i]==true，则
		 * 表示id号i已经被使用。
		 */
		private BitArray table;
		/**
		 * 指向下一次准备试探能否分配的table的下标
		 */
		private long curIdx;
		/**
		 * 装载因子。当已经分配的id数量比上可以分配的id的总数大于loadFactor的时候，
		 * id记录表就会在适当的时候扩张成原来的两倍大小
		 */
		private double loadFactor;
		/**
		 * 已经分配出去的id的总数
		 */
		private long numTakenId;
		
		/**
		 * 新建一个IDTable，初始大小为16，装载因子为0.75
		 */
		public IDTable()
		{
			this(16, 0.75);
		}
		
		/**
		 * 新建一个IDTable，指定初始ID表的大小以及装载因子
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
			
			//计算table的初始化大小
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
			
			//初始化
			this.table=new BitArray((int)nBlock);
			this.loadFactor=loadFactor;
			
			curIdx=0;
			numTakenId=0;
		}
		
		/**
		 * @return 分配一个id号。保证每次分配出去的id号，不会
		 * 和任何一个过去分配出去并且没有被归还的id号相同。
		 * id号用之后应该通过freeId(long)方法归还，否则被分配出
		 * 去的id号就永远不能使用了。
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
		 * 查找一个没有被用过的id号。查找方式按照平方探测法，
		 * 如果查找指针curIdx已经跨过了table的最后一个元素，
		 * 那么就会按照装载因子考察是否要扩张id表。
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
		 * 归还一个id号，如果你不归还通过allocId()方法借出的id号，
		 * 则该id号将永远不能被重新使用。
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
		 * 检查是否需要扩张id表。如果需要则调用扩张id表的方法。
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
