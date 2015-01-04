package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * 
 * ����ӿ���AuthorityCenterʵ�֣���������AuthorityCenter
 * ��"�鿴Ȩ������"�Ĺ���
 *
 */
public interface IViewingAuthorityData 
extends IAuthorityCenterFunctionInterface
{
	/**
	 * ��֤�����levelName�ʹ����password�Ƿ�ƥ�䡣
	 * ƥ����ָ��
	 * 1��levelName��password����Ϊnull
	 * 2��levelName����ͨ��addHighestLevel(TLevelName, TPassword)�����������
	 * ����û��ͨ��removeHighestLevel()����ɾ��
	 * 3��ͨ��addHighestLevel(TLevelName, TPassword)��
	 * changeLevelPassword(TLevelName, TPassword)��levelNameָ�������룬�봫��
	 * ������password����equals������֤��ȡ�
	 * 
	 * @return ���ƥ���򷵻�true�����򷵻�false
	 * @throws AuthorityCenterOperatingException 
	 */
    public boolean verifyLevelAndPassword(String levelName, String password) throws AuthorityCenterOperatingException;
    /**
	 * ����Ƿ�����ָ����Ȩ�޵ȼ���û��ɾ���������и�Ȩ�޵ȼ���
	 * ���ܴ���null
	 * @throws AuthorityCenterOperatingException 
	 */
	public boolean containsLevel(String levelName) throws AuthorityCenterOperatingException;
	/**
	 * �Ƚ�Ȩ�޵ĸߵͣ���ȿ��Դ���TLevelName�������Ƚ�
	 * ����Ȩ�޵ȼ����ֿ��Դ���null����ʾû��Ȩ�޵ȼ�����Ȩ�޵ȼ�����
	 * Ȩ�޵ȼ���ȡ�
	 * 
	 * ����ֵ��Java��Compare�����Ĺ淶��ͬ��
	 * @throws AuthorityCenterOperatingException
	 */
	public int compareAuthority(String levelNameA, String levelNameB) throws AuthorityCenterOperatingException;
	/**
	 * ���ذ��������е�����Ȩ�޵ȼ���������û�п���λ�á�
	 * ���û��Ȩ�޵ȼ����򷵻س���Ϊ������������null��
	 * ���⣬������в��ᱣ�����ص���������ã�������
	 * ���޸�������顣
	 * @throws AuthorityCenterOperatingException 
	 */
	public String[] getAllLevels() throws AuthorityCenterOperatingException;
	/**
	 * @return ��ǰAuthorityCenter�д��ڵ�Ȩ�޵ȼ��ĸ�����
	 * �����������û�б�ɾ����Ȩ�޵ȼ��ĸ�����
	 * @throws AuthorityCenterOperatingException
	 */
	public int getNumOfLevels() throws AuthorityCenterOperatingException;
	/**
	 * ��ѯ�����commandHeadҪ��ʲôȨ�޲ſ��Ա����У����commandHead��Ȩ�޵ȼ���
	 * �򷵻�null��
	 * @return commandHeadҪ���Ȩ�޵ȼ������commandHead��Ȩ�޵ȼ����򷵻�null
	 */
	public String findRequiredAuthority(String commandHead);
	/**
	 * ��ѯ�����commanderIDӵ��ʲô����Ȩ�ޡ����commanderID��Ȩ�޵ȼ����򷵻�null��
	 * @return commanderIDӵ�е�Ȩ�޵ȼ������commanderID��Ȩ�޵ȼ����򷵻�null
	 */
	public String findPermittedAuthority(CommanderIDWithAuthority commanderID);

}
