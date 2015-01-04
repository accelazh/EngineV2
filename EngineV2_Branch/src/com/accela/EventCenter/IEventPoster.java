package com.accela.EventCenter;

/**
 * 这个接口有EventCenter实现，通过它来访问
 * EventCenter的发送事件的方法
 */
public interface IEventPoster
{
	/**
	 * 向事件处理中心发送一个事件。事件处理中心会依次找出和这个事件相匹配的GeneralListener，
	 * ClassListener和InstanceListener，来处理这个事件。
	 * 
	 * @param event
	 * @throws EventProcessingException 当某个监听器处理时发生异常的时候，事件处理中心不会
	 * 中断处理，而是继续完成其他监听器的处理，到最后才会抛出这个异常。处理过程中可能发生多个
	 * 异常，它们都会被存储在这个异常对象中。
	 * 
	 */
	public void postEvent(Event event) throws EventProcessingException;

}
