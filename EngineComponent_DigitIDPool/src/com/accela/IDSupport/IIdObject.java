package com.accela.IDSupport;

/**
 * 
 * 实现了这个接口的对象，应该拥有一个id号。
 * 这个id号可以从DigigIDPool获得。
 * 
 * 实现了这个接口的对象可以和IdObjectPool
 * 配合使用。
 *
 */
public interface IIdObject
{
	public int getId();

}
