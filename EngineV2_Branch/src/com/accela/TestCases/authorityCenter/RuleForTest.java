package com.accela.TestCases.authorityCenter;

import com.accela.AuthorityCenter.authorityBase.IAuthorityBaseViewer;
import com.accela.AuthorityCenter.ruleFilter.AuthorityRule;
import com.accela.AuthorityCenter.shared.CommandWithAuthority;

public class RuleForTest extends AuthorityRule
{
	private int invokeCount=0;
	
	private Runnable target=null;
	
	private boolean shouldFilter=false;
	
	public RuleForTest(boolean shouldFilter)
	{
		this(shouldFilter, null);
	}
	
	public RuleForTest(boolean shouldFilter, Runnable target)
	{
		this.shouldFilter=shouldFilter;
		this.target=target;
	}

	@Override
	protected boolean shouldFilterImpl(CommandWithAuthority command,
			IAuthorityBaseViewer authorityBaseViewer) throws Exception
	{
		invokeCount++;
		if(target!=null)
		{
			target.run();
		}
		
		return shouldFilter;
	}
	
	public int getInvokeCount()
	{
		return invokeCount;
	}
	
	public void clearInvokeCount()
	{
		invokeCount=0;
	}
	
}
