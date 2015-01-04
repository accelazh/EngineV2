package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.AuthorityCenter.AuthorityCenterFunctionInterfaces;

/**
 * 
 * ����ӿڱ�ʾAuthorityCenter����ڣ�
 * ͨ�������ڿ��Ի��AuthorityCenter
 * �ĸ��ַ���Ĺ��ܵĽӿ�
 *
 */
public interface IAuthorityCenterEntrance
{
	/**
	 * 
	 * @param face AuthorityCenter.AUTHORITY_CENTER_FUNCTION_INTERFACEö�ٱ�����
	 * ͨ������ָ������AuthorityCenter��������ĸ����湦�ܵĽӿڡ�
	 * 
	 * ���ص��Ƕ�Ӧ��AuthorityCenter��ĳ������Ĺ��ܵĽӿڣ���Щ�ӿڶ��̳���
	 * IAuthorityCenterFunctionInterface�ӿڣ�����Ҫ�ѷ���ֵ��������ת������ʹ�á�
	 * 
	 * �����Ǵ�������ͷ��صĽӿڵĶ�Ӧ��
	 * IConfiguringAuthorityData				AUTHORITY_CENTER_FUNCTION_INTERFACE.CONFIGURE_AUTHORITY_DATA,
	 * IChangingAuthorityDataWithVerification	AUTHORITY_CENTER_FUNCTION_INTERFACE.CHANGE_AUTHORITY_DATA_WITH_VERIFCATION,
	 * IConfiguringAuthorityFilter				AUTHORITY_CENTER_FUNCTION_INTERFACE.CONFIGURE_AUTHORITY_FILTER,
	 * IFilteringWithAuthorityFilter			AUTHORITY_CENTER_FUNCTION_INTERFACE.FILTER_WITH_AUTHORITY_FILTER,
	 * IViewingAuthorityData					AUTHORITY_CENTER_FUNCTION_INTERFACE.VIEW_AUTHORITY_DATA
	 * 
	 * @return �������������AuthorityCenterʵ�������ã�ֻ����ת������
	 * IAuthorityCenterFunctionInterface�ӿ�����
	 * 
	 */
	IAuthorityCenterFunctionInterface getInstance(
			AuthorityCenterFunctionInterfaces face);

}
