package com.accela.EventCenter;

/**
 *
 * 这个接口由EventCenter实现，通过它来访问EventCenter
 * 用于配置的方法
 *
 */
public interface IEventCenterConfigurer
{
	/**
	 * 向事件处理中心中加入GeneralListener。
	 * 你可以重复加入监听同一目标的监听器，它们都是有效的。
	 */
	public void addGeneralListener(GeneralListener listener);
	
	/**
	 * 向事件处理中心中加入ClasslListener。
	 * 你可以重复加入监听同一目标的监听器，它们都是有效的。
	 */
	public void addClassListener(ClassListener listener);
	
	/**
	 * 向事件处理中心中加入InstanceListener。
	 * 你可以重复加入监听同一目标的监听器，它们都是有效的。 
	 */
	public void addInstanceListener(InstanceListener listener);
	
	/**
	 * 删除监听指定的事件aimEventClass的所有GeneralListener。 
	 * @param aimEventClass 指定的事件类
	 * @return 所有被删除的GeneralListener
	 */
	public GeneralListener[] removeGeneralListener(Class<? extends Event> aimEventClass);
	
	/**
	 * 删除监听指定的事件aimEventClass和指定的类aimClass的所有ClassListener。
	 * @param aimEventClass 指定的事件类
	 * @param aimClass 指定的类
	 * @return 所有被删除的ClassListener
	 */
	public ClassListener[] removeClassListener(Class<? extends Event> aimEventClass,
			Class<? extends Object> aimClass);
	
	/**
	 * 删除监听指定的事件aimEventClass和指定的实例aimInstance的所有ClassListener。
	 * @param aimEventClass 指定的事件类
	 * @param aimInstance 指定的实例
	 * @return
	 */
	public InstanceListener[] removeInstanceListener(Class<? extends Event> aimEventClass,
			Object aimInstance);
}
