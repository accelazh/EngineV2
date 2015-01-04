package com.accela.EventCenter;

import com.accela.ClassIDAndInstanceID.*;

/**
 * 
 * Listener的键。
 * ListenerKey实际上是把Listener中的aimEventClassID、aimClassID和
 * aimInstanceID拿出来，作为键的内容。
 *
 */
class ListenerKey
{
	private ClassID aimEventClassID;
	
	private ClassID aimClassID;
	
	private InstanceID aimInstanceID;
	
	protected ListenerKey(ClassID aimEventClassID, 
			ClassID aimClassID, 
			InstanceID aimInstanceID)
	{
		if(null==aimEventClassID)
		{
			throw new NullPointerException("aimEventClassID should not be null");
		}
		if(null==aimClassID)
		{
			if(aimInstanceID!=null)
			{
				throw new IllegalArgumentException("invalid aimInstanceID");
			}
		}
		else
		{
			if(aimInstanceID!=null
					&&!aimInstanceID.isOfSameClass(aimClassID))
			{
				throw new IllegalArgumentException("invalid aimInstanceID"); 
			}
		}
		
		this.aimEventClassID=aimEventClassID;
		this.aimClassID=aimClassID;
		this.aimInstanceID=aimInstanceID;
	}

	public static ListenerKey createListenerKey(Listener listener)
	{
		if(null==listener)
		{
			throw new NullPointerException("listener should not be null");
		}
		
		return new ListenerKey(listener.getAimEventClassID(), 
				listener.getAimClassID(), 
				listener.getAimInstanceID());
	}
	
	public static ListenerKey createListenerKeyForGeneralListener(Class<? extends Event> aimEventClass)
	{
		if(null==aimEventClass)
		{
			throw new NullPointerException("aimEventClass should not be null");
		}
		
		return new ListenerKey(IDDispatcher.createClassID(aimEventClass), null, null);
	}
	
	public static ListenerKey createListenerKeyForClassListener(Class<? extends Event> aimEventClass,
			Class<? extends Object> aimClass)
	{
		if(null==aimEventClass)
		{
			throw new NullPointerException("aimEventClass should not be null");
		}
		if(null==aimClass)
		{
			throw new NullPointerException("aimClass should not be null");
		}
		
		return new ListenerKey(IDDispatcher.createClassID(aimEventClass),
				IDDispatcher.createClassID(aimClass), null);
	}
	
	public static ListenerKey createListenerKeyForInstanceListener(Class<? extends Event> aimEventClass,
			Object aimInstance)
	{
		if(null==aimEventClass)
		{
			throw new NullPointerException("aimEventClass should not be null");
		}
		if(null==aimInstance)
		{
			throw new NullPointerException("aimInstance should not be null");
		}
		
		return new ListenerKey(IDDispatcher.createClassID(aimEventClass),
				IDDispatcher.createClassID(aimInstance.getClass()), 
				IDDispatcher.createInstanceID(aimInstance));
	}
	
	
	@Override
	public boolean equals(Object obj)
	{
		if(!(obj instanceof ListenerKey))
		{
			return false;
		}
		
		ListenerKey other=(ListenerKey)obj;
		
		assert(aimEventClassID!=null);
		assert(other.aimEventClassID!=null);
		
		if(!aimEventClassID.equals(other.aimEventClassID))
		{
			return false;
		}
		
		if(aimClassID!=null&&other.aimClassID!=null)
		{
			if(!aimClassID.equals(other.aimClassID))
			{
				return false;
			}
		}
		else if(null==aimClassID&&other.aimClassID!=null)
		{
			return false;
		}
		else if(aimClassID!=null&&null==other.aimClassID)
		{
			return false;
		}
		
		if(aimInstanceID!=null&&other.aimInstanceID!=null)
		{
			if(!aimInstanceID.equals(other.aimInstanceID))
			{
				return false;
			}
		}
		else if(null==aimInstanceID&&other.aimInstanceID!=null)
		{
			return false;
		}
		else if(aimInstanceID!=null&&null==other.aimInstanceID)
		{
			return false;
		}
		
		return true;
		
	}

	@Override
	public int hashCode()
	{
		assert(aimEventClassID!=null);
		
		if(null==aimClassID)
		{
			if(null==aimInstanceID)
			{
				return aimEventClassID.hashCode()^0^0;
			}
			else
			{
				assert(false);	//aimClassID is null but aimInstanceID is not null
				throw new IllegalStateException("aimClassID is null but aimInstanceID is not null");
			}
		}
		else
		{
			if(null==aimInstanceID)
			{
				return aimEventClassID.hashCode()^aimClassID.hashCode()^0;
			}
			else
			{
				if(aimClassID.isOfSameClass(aimInstanceID))
				{
					return aimEventClassID.hashCode()
					^aimClassID.hashCode()
					^aimInstanceID.hashCode();
				}
				else
				{
					assert(false);    //aimClassID.isOfSameClass(aimInstanceID) should be true
					throw new IllegalStateException("aimInstanceID and aimClassID are all non-null, " +
							"but the class of the instance aimInstanceID represents is not the class aimClassID represents");
				}
			}
		}
		
	}

}
