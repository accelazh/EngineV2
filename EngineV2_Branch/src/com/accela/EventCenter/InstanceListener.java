package com.accela.EventCenter;

/**
 * 
 * InstanceListener监听某一个实例的某一种事件。
 * 如果该实例送出此事件，InstanceListener
 * 就会处理。
 *
 */
public abstract class InstanceListener extends Listener
{
	/**
	 * @param aimEventClass 告诉监听器，它应该监听什么类型事件
	 * @param aimInstance 告诉监听器，它应该监听哪个实例发出的事件
	 */
	public InstanceListener(Class<? extends Event> aimEventClass,
			Object aimInstance)
	{
		super(aimEventClass, aimInstance.getClass(), aimInstance);
	}

}
