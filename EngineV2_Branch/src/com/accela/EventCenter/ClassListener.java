package com.accela.EventCenter;

/**
 * 
 * ClassListener监听某一个类的某一种事件。
 * 任何该类的实例送出此事件，ClassListener
 * 都会处理。
 *
 */
public abstract class ClassListener extends Listener
{
	/**
	 * @param aimEventClass 告诉监听器，它应该监听什么类型事件
	 * @param aimClass 告诉监听器，它应该监听哪个类的实例发出的事件
	 */
	public ClassListener(Class<? extends Event> aimEventClass,
			Class<? extends Object> aimClass)
	{
		super(aimEventClass, aimClass, null);
	}

}
