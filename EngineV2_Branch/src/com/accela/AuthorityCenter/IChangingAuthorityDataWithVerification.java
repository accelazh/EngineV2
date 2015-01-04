package com.accela.AuthorityCenter;

import com.accela.AuthorityCenter.shared.CommanderIDWithAuthority;

/**
 * 
 * ����ӿ���AuthorityCenterʵ�֣���������AuthorityCenter
 * ��"����Ҫ��֤���������޸�Ȩ������"�Ĺ���
 *
 */
public interface IChangingAuthorityDataWithVerification
extends IAuthorityCenterFunctionInterface
{
	/**
	 * �����������һ���������commanderIDϣ������ĳ��Ȩ�޵ȼ��������ʱ��
	 * ���ҽ�������Ȩ�޵ȼ����ڸ�levelName��ʱ�򣬲��ܸ�������
	 * 
	 * @return �Ƿ�ɹ���������
	 * @throws AuthorityCenterOperatingException
	 */
	public boolean changeLevelPasswordWithVerification(CommanderIDWithAuthority commanderID,
			String levelName, String newPassword)
			throws AuthorityCenterOperatingException;
	/**
	 * �����������һ���������ϣ�������Լ���Ȩ�޵�ʱ������Ҫ����
	 * �Լ���commanderID���Լ�ϣ����Ȩ�޵ȼ�levelName�����Ȩ�޵ȼ����� ��password��
	 * 
	 * ���ҽ�����Щ���������ʱ�����������߲��ܳɹ��ﵽĿ�ģ� 
	 * 1�������������Ϊnull
	 * 2��levelName�������뵽AuthorityBase�У�����û�б�ɾ�� 
	 * 3��levelName����commanderID���е�Ȩ�޵ȼ�
	 * 4��password��levelName��Ӧ�����뾭equals�����������
	 * 
	 * @return �Ƿ�ɹ�����Ȩ��
	 * @throws AuthorityCenterOperatingException
	 */
	public boolean heightenSelfAuthorityWithVerification(CommanderIDWithAuthority commanderID,
			String levelName, String password)
			throws AuthorityCenterOperatingException;
	/**
	 * �����������һ���������ϣ�������Լ���Ȩ�޵�ʱ������Ҫ����
	 * �Լ���TCommanderID��commanderID���Լ�ϣ����Ȩ�޵ȼ�levelName��
	 * ��levelNameΪnullʱ����ɾ��commanderID��Ȩ�޵ȼ������������� ��Ȩ�޵ȼ���״̬
	 * 
	 * ���ҽ�����Щ���������ʱ�����������߲��ܳɹ��ﵽĿ�ģ� 
	 * 1���������������levelName�ⲻΪnull
	 * 2��levelName�������뵽AuthorityBase�У�����û�б�ɾ�� ������levelName==null
	 * 3��levelName����commanderID���е�Ȩ�޵ȼ�������levelName==nullʱ��commanderID
	 * ������Ȩ�޵ĵȼ�
	 * 
	 * @return �Ƿ�ɹ�����Ȩ�޵ȼ�
	 * @throws AuthorityCenterOperatingException
	 */
	public boolean lowerSelfAuthorityWithVerification(CommanderIDWithAuthority commanderID,
			String levelName)
			throws AuthorityCenterOperatingException;
	/**
	 * �����������һ���������selfCommanderID��ϣ���ı���һ���������
	 * otherCommanderID��Ȩ�޵�ʱ������Ҫ�����Լ���TCommanderID��
	 * selfCommanderID�����ı��ߵ�TCommanderID��otherCommanderID���Լ�
	 * ��ϣ����otherCommanderID�ı䵽��Ȩ�޵ȼ�levelName�����levelName
	 * Ϊnull�����ʾ��otherCommanderID��Ϊ��Ȩ�޵ȼ���
	 * 
	 * ���ҽ�����Щ���������ʱ�����������߲��ܳɹ��ﵽĿ�ģ� 
	 * 1�����������levelName�ⶼ��Ϊnull
	 * 2��levelName�������뵽AuthorityVerifier�У�����û�б�ɾ��������levelName��null
	 * 3��levelName���ڻ����selfCommanderID��Ȩ�޵ȼ�
	 * 4��otherCommanderID��Ȩ�޵ȼ�����selfCommanderID��Ȩ�޵ȼ�������
	 * otherCommanderIDû��Ȩ�޵ȼ���selfCommanderID��
	 * 5��otherCommanderID��Ȩ�޵ȼ�������levelName�����ߵ�levelName==nullʱ��
	 * otherCommanderID������Ȩ�޵ȼ�
	 * 
	 * @return selfCommanderID��������Ƿ�ɹ��ﵽĿ��
	 * @throws AuthorityCenterOperatingException
	 */
	public boolean setOtherAuthorityWithVerification(CommanderIDWithAuthority selfCommanderID,
			CommanderIDWithAuthority otherCommanderID, String levelName)
			throws AuthorityCenterOperatingException;

}
