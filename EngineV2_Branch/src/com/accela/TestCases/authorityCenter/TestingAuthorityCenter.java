package com.accela.TestCases.authorityCenter;

import com.accela.AuthorityCenter.AuthorityCenter;
import com.accela.AuthorityCenter.IAuthorityCenterEntrance;
import com.accela.AuthorityCenter.IAuthorityCenterFunctionInterface;
import com.accela.AuthorityCenter.IChangingAuthorityDataWithVerification;
import com.accela.AuthorityCenter.IConfiguringAuthorityData;
import com.accela.AuthorityCenter.IConfiguringAuthorityFilter;
import com.accela.AuthorityCenter.IFilteringWithAuthorityFilter;
import com.accela.AuthorityCenter.IViewingAuthorityData;
import com.accela.AuthorityCenter.AuthorityCenter.AuthorityCenterFunctionInterfaces;
import com.accela.AuthorityCenter.authorityBase.SimplePasswordManager;

import junit.framework.TestCase;

public class TestingAuthorityCenter extends TestCase
{
	private IAuthorityCenterEntrance ac;
	private IAuthorityCenterEntrance ac2;

	@Override
	protected void setUp() throws Exception
	{
		super.setUp();
		ac = AuthorityCenter.createAuthorityCenter();
		ac2 = AuthorityCenter
				.createAuthorityCenter(new SimplePasswordManager());
	}

	@Override
	protected void tearDown() throws Exception
	{
		super.tearDown();
		ac = null;
		ac2 = null;
	}

	public void testAuthorityCenter()
	{
		try
		{
			IAuthorityCenterFunctionInterface interface1 = ac
					.getInstance(AuthorityCenterFunctionInterfaces.CHANGE_AUTHORITY_DATA_WITH_VERIFCATION);
			assert (interface1 != null);
			assert (interface1 instanceof IChangingAuthorityDataWithVerification);
			IChangingAuthorityDataWithVerification funcInterface1 = (IChangingAuthorityDataWithVerification) interface1;
			useTheInterface(funcInterface1);

			IAuthorityCenterFunctionInterface interface2 = ac
					.getInstance(AuthorityCenterFunctionInterfaces.CONFIGURE_AUTHORITY_DATA);
			assert (interface2 != null);
			assert (interface2 instanceof IConfiguringAuthorityData);
			IConfiguringAuthorityData funcInterface2 = (IConfiguringAuthorityData) interface2;
			useTheInterface(funcInterface2);

			IAuthorityCenterFunctionInterface interface3 = ac
					.getInstance(AuthorityCenterFunctionInterfaces.CONFIGURE_AUTHORITY_FILTER);
			assert (interface3 != null);
			assert (interface3 instanceof IConfiguringAuthorityFilter);
			IConfiguringAuthorityFilter funcInterface3 = (IConfiguringAuthorityFilter) interface3;
			useTheInterface(funcInterface3);

			IAuthorityCenterFunctionInterface interface4 = ac
					.getInstance(AuthorityCenterFunctionInterfaces.FILTER_WITH_AUTHORITY_FILTER);
			assert (interface4 != null);
			assert (interface4 instanceof IFilteringWithAuthorityFilter);
			IFilteringWithAuthorityFilter funcInterface4 = (IFilteringWithAuthorityFilter) interface4;
			useTheInterface(funcInterface4);

			IAuthorityCenterFunctionInterface interface5 = ac
					.getInstance(AuthorityCenterFunctionInterfaces.VIEW_AUTHORITY_DATA);
			assert (interface5 != null);
			assert (interface5 instanceof IViewingAuthorityData);
			IViewingAuthorityData funcInterface5 = (IViewingAuthorityData) interface5;
			useTheInterface(funcInterface5);

			// /////////////////////////////////////////////////
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	public void testAuthorityCenter2()
	{
		try
		{
			IAuthorityCenterFunctionInterface interface1 = ac2
					.getInstance(AuthorityCenterFunctionInterfaces.CHANGE_AUTHORITY_DATA_WITH_VERIFCATION);
			assert (interface1 != null);
			assert (interface1 instanceof IChangingAuthorityDataWithVerification);
			IChangingAuthorityDataWithVerification funcInterface1 = (IChangingAuthorityDataWithVerification) interface1;
			useTheInterface(funcInterface1);

			IAuthorityCenterFunctionInterface interface2 = ac2
					.getInstance(AuthorityCenterFunctionInterfaces.CONFIGURE_AUTHORITY_DATA);
			assert (interface2 != null);
			assert (interface2 instanceof IConfiguringAuthorityData);
			IConfiguringAuthorityData funcInterface2 = (IConfiguringAuthorityData) interface2;
			useTheInterface(funcInterface2);

			IAuthorityCenterFunctionInterface interface3 = ac2
					.getInstance(AuthorityCenterFunctionInterfaces.CONFIGURE_AUTHORITY_FILTER);
			assert (interface3 != null);
			assert (interface3 instanceof IConfiguringAuthorityFilter);
			IConfiguringAuthorityFilter funcInterface3 = (IConfiguringAuthorityFilter) interface3;
			useTheInterface(funcInterface3);

			IAuthorityCenterFunctionInterface interface4 = ac2
					.getInstance(AuthorityCenterFunctionInterfaces.FILTER_WITH_AUTHORITY_FILTER);
			assert (interface4 != null);
			assert (interface4 instanceof IFilteringWithAuthorityFilter);
			IFilteringWithAuthorityFilter funcInterface4 = (IFilteringWithAuthorityFilter) interface4;
			useTheInterface(funcInterface4);

			IAuthorityCenterFunctionInterface interface5 = ac2
					.getInstance(AuthorityCenterFunctionInterfaces.VIEW_AUTHORITY_DATA);
			assert (interface5 != null);
			assert (interface5 instanceof IViewingAuthorityData);
			IViewingAuthorityData funcInterface5 = (IViewingAuthorityData) interface5;
			useTheInterface(funcInterface5);

			// /////////////////////////////////////////////////
		} catch (Exception ex)
		{
			ex.printStackTrace();
			assert (false);
		}
	}

	private void useTheInterface(IAuthorityCenterFunctionInterface face)
	{
		assert (face != null);
	}

}
