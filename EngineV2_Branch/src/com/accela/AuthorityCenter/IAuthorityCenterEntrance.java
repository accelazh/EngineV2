package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.AuthorityCenter.AuthorityCenterFunctionInterfaces;

/**
 * 
 * 这个接口表示AuthorityCenter的入口，
 * 通过这个入口可以获得AuthorityCenter
 * 的各种方面的功能的接口
 *
 */
public interface IAuthorityCenterEntrance
{
	/**
	 * 
	 * @param face AuthorityCenter.AUTHORITY_CENTER_FUNCTION_INTERFACE枚举变量，
	 * 通过它来指定返回AuthorityCenter的相对于哪个方面功能的接口。
	 * 
	 * 返回的是对应于AuthorityCenter的某个方面的功能的接口，这些接口都继承了
	 * IAuthorityCenterFunctionInterface接口，你需要把返回值进行类型转换后再使用。
	 * 
	 * 下面是传入参数和返回的接口的对应表：
	 * IConfiguringAuthorityData				AUTHORITY_CENTER_FUNCTION_INTERFACE.CONFIGURE_AUTHORITY_DATA,
	 * IChangingAuthorityDataWithVerification	AUTHORITY_CENTER_FUNCTION_INTERFACE.CHANGE_AUTHORITY_DATA_WITH_VERIFCATION,
	 * IConfiguringAuthorityFilter				AUTHORITY_CENTER_FUNCTION_INTERFACE.CONFIGURE_AUTHORITY_FILTER,
	 * IFilteringWithAuthorityFilter			AUTHORITY_CENTER_FUNCTION_INTERFACE.FILTER_WITH_AUTHORITY_FILTER,
	 * IViewingAuthorityData					AUTHORITY_CENTER_FUNCTION_INTERFACE.VIEW_AUTHORITY_DATA
	 * 
	 * @return 调用这个方法的AuthorityCenter实例的引用，只不过转换成了
	 * IAuthorityCenterFunctionInterface接口类型
	 * 
	 */
	IAuthorityCenterFunctionInterface getInstance(
			AuthorityCenterFunctionInterfaces face);

}
