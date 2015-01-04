package com.accela.MessageService.messages.namingServerToNamingHost;

/**
 * 
 * 当NamingServer中记录的Name-ClientID对应关系发生变更
 * 的时候，就需要通知与其连接的各个NamingHost，它们自己记录的
 * Name-ClientID对应表已经无效了，此时NamingServer向各
 * 个NamingHost发出这个信息。
 *
 */
public class NamingInvalidMessage
{

}
