package com.accela.TestCases.connectionCenterUnionHPObjectStreams;

public class ClassAB
{
	private ClassAA head;
	
	public ClassAB()
	{
		init();
	}
	
	private void init()
	{
		head=new ClassAA();
		
		ClassAA cur=head;
		
		for(int i=0;i<20;i++)
		{
			ClassAA aa=new ClassAA();
			cur.setNext(aa);
			cur=cur.getNext();
		}
		
		cur.setNext(head);
	}

	public ClassAA getHead()
	{
		return head;
	}

	public void setHead(ClassAA head)
	{
		this.head = head;
	}

}
