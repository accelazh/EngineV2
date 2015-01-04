package com.accela.IDSupport;

/**
 * 
 * BitArray可以存储布尔型变量的数组。但是它在一个字内可以存储多个布尔变量，
 * 因而非常节省空间。java.util.BitSet拥有同种类型的更加全面的功能，但是在
 * 存储和访问极其多的布尔变量的时候效率不及我设计的BitArray。
 * (BitSet中的一条语句太损了，“words[wordIndex] |= (1L << bitIndex)”，如
 * 果bitIdx=1000000，你看会有什么后果)。
 *
 */
class BitArray
{
	private final static int ADDRESS_BITS_PER_WORD = 6;
    private final static int BITS_PER_WORD = 1 << ADDRESS_BITS_PER_WORD;
    private final static long BIT_IDX_MASK=0x3fL;
    private final static long SET_MASK=1L;
	
	private long[] value;
	
	public BitArray(int nBlock)
	{
		if(nBlock<=0)
		{
			throw new IllegalArgumentException("nBlock should be positive"); 
		}
		
		value=new long[nBlock];
		for(int i=0;i<value.length;i++)
		{
			value[i]=0;
		}
	}
	
	public void set(long bitIdx)
	{
		checkBitIdxValid(bitIdx);
		
		value[getWordIdx(bitIdx)]|=(SET_MASK<<getBitIdxInWord(bitIdx));
	}
	
	public void clear(long bitIdx)
	{
		checkBitIdxValid(bitIdx);
		
		value[getWordIdx(bitIdx)]&=~(SET_MASK<<getBitIdxInWord(bitIdx));
	}
	
	public boolean get(long bitIdx)
	{
		checkBitIdxValid(bitIdx);
		
	 	return (value[getWordIdx(bitIdx)]&(SET_MASK<<getBitIdxInWord(bitIdx)))!=0;
	}
	
	private void checkBitIdxValid(long bitIdx)
	{
		if(!(bitIdx>=0&&bitIdx<(long)value.length*BITS_PER_WORD))
		{
			throw new IllegalArgumentException("bitIdx is invalid");
		}
	}
	
	private static int getWordIdx(long bitIdx)
	{
		assert(bitIdx>>ADDRESS_BITS_PER_WORD<Integer.MAX_VALUE);
		return (int)(bitIdx>>ADDRESS_BITS_PER_WORD);
	}
	
	private static int getBitIdxInWord(long bitIdx)
	{
		assert((bitIdx&BIT_IDX_MASK)<BITS_PER_WORD);
		return (int)(bitIdx&BIT_IDX_MASK);
	}
	
	public void expend()
	{
		int newLength=-1;
		if(value.length<Integer.MAX_VALUE/2)
		{
			newLength=value.length*2;
		}
		else if(value.length<Integer.MAX_VALUE)
		{
			newLength=Integer.MAX_VALUE;
		}
		else
		{
			return;
		}
		
		long[] newValue=new long[newLength];
		for(int i=0;i<value.length;i++)
		{
			newValue[i]=value[i];
		}
		for(int i=value.length;i<newValue.length;i++)
		{
			newValue[i]=0;
		}
		
		value=newValue;
		
	}
	
	public long size()
	{
		return value.length*BITS_PER_WORD;
	}
	
	public String toString()
	{
		StringBuffer buffer=new StringBuffer();
		
		for(int i=0;i<value.length;i++)
		{
			for(int idx=0;idx<BITS_PER_WORD;idx++)
			{
				buffer.append(
						((value[i]&(SET_MASK<<idx))!=0)?"1":"0");
			}
			buffer.append(' ');
		}
		
		return buffer.toString();
	}
	
	/*public static void main(String[] args) throws InterruptedException
	{
		for(int i=0;i<4*64;i++)
		{
			BitArray array=new BitArray(4);
			array.set(i);
			System.out.println(array);
			
			array.expend();
			System.out.println(array);
			
			System.out.println();
			Thread.sleep(100);
		}
	}*/
	
}
