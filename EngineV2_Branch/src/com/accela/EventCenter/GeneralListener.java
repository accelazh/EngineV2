package com.accela.EventCenter;

/**
 * 
 * GeneralListener监听某一种事件。
 * 任何对象送出此事件，GeneralListener
 * 都会处理
 *
 */
public abstract class GeneralListener extends Listener
{
	/**
	 * @param aimEventClass 告诉监听器，它应该监听什么类型事件
	 */
	public GeneralListener(Class<? extends Event> aimEventClass)
	{
		super(aimEventClass, null, null);
	}

}
