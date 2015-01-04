package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * ����ӿ���AuthorityCenterʵ�֣���������AuthorityCenter
 * ��"����Ȩ������"�Ĺ���
 *
 */
public interface IConfiguringAuthorityData 
extends IAuthorityCenterFunctionInterface, IViewingAuthorityData
{
	/**
	 * ����һ���µ�Ȩ�޵ȼ���ΪĿǰ��ߵ�Ȩ�޵ȼ���ʵ�������������Ҫ��
	 * ʹ���ߴӵ͵������μ���Ȩ�޵ȼ���
	 * ���levelName�͹�ȥĳ��Ȩ�޵ȼ��ظ������������ʲôҲ����������
	 * ��ʧ�ܡ�
	 * 
	 * @param levelName �µ�Ȩ�޵ȼ�
	 * @param password �µ�Ȩ�޵ȼ�������
	 * @return �Ƿ�ɹ��ؼ������µ�Ȩ�޵ȼ���
	 * @throws AuthorityCenterOperatingException 
	 */
	public boolean addHighestLevel(String levelName, String password) throws AuthorityCenterOperatingException;
	/**
	 * ɾ����ߵ��Ǹ�Ȩ�޵ȼ�������ӵ�����Ȩ�޵ȼ���TCommanderID��TCommandHead
	 * ���ᱻ�½�һ��Ȩ�޵ȼ������������ɾ����Ȩ�޵ȼ��������Ȩ�޵ȼ���������
	 * TCommanderID��TCommandHead�ͻᱻɾ��Ȩ�޵ȼ�������Ϊ��Ȩ�޵ȼ���
	 * 
	 * ���û�����Ȩ�޵ȼ�������ʱ��û���κ�Ȩ�޵ȼ�����ʲôҲ����
	 * 
	 * @return ��ɾ�������Ȩ�޵ȼ������û���򷵻�null
	 * @throws AuthorityCenterOperatingException 
	 */
	public String removeHighestLevel() throws AuthorityCenterOperatingException;
	/**
	 * �޸�ĳ��Ȩ�޵ȼ�������
	 * @throws AuthorityCenterOperatingException 
	 */
	public String changeLevelPassword(String levelName, String password) throws AuthorityCenterOperatingException;
	/**
	 * �����趨commandHead���������Ȩ�ޡ����levelName��=null�������趨ΪlevelName
	 * ��Ӧ��Ȩ�޵ȼ����������趨Ϊ��Ȩ�޵ȼ���
	 * @param commandHead ��Ҫ���趨������
	 * @param levelName commandHead���趨�ɵ�Ȩ�޵ȼ�
	 * @return commandHead��ǰ��Ȩ�޵ȼ������commandHead��ǰ������Ȩ�޵ȼ�״̬����ô����null
	 * @throws AuthorityCenterOperatingException 
	 */
	public String setCommandHeadAuthority(String commandHead, String levelName) throws AuthorityCenterOperatingException;
	/**
	 * �����趨commanderID��Ȩ�ޡ����levelName��=null�������趨ΪlevelName
	 * ��Ӧ��Ȩ�޵ȼ����������趨Ϊ��Ȩ�޵ȼ���
	 * @param commanderID ��Ҫ���趨������
	 * @param levelName commandHead���趨�ɵ�Ȩ�޵ȼ�
	 * @return commanderID��ǰ��Ȩ�޵ȼ������commanderID��ǰ������Ȩ�޵ȼ�״̬����ô����null
	 * @throws AuthorityCenterOperatingException 
	 */
	public String setCommanderAuthority(CommanderIDWithAuthority commanderID, String levelName) throws AuthorityCenterOperatingException;

}
