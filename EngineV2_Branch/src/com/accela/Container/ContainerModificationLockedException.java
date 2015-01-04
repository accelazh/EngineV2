package com.accela.Container;

public class ContainerModificationLockedException extends IllegalStateException
{
	private static final long serialVersionUID = 1L;

	public ContainerModificationLockedException()
	{
		super();
	}

	public ContainerModificationLockedException(String message)
	{
		super(message);
	}

	public ContainerModificationLockedException(String message, Throwable cause)
	{
		super(message, cause);
	}

	public ContainerModificationLockedException(Throwable cause)
	{
		super(cause);
	}
}
