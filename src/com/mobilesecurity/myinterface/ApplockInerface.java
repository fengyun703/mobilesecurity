package com.mobilesecurity.myinterface;

import com.mobilesecurity.db.bean.LockInfo;

public interface ApplockInerface {

	/**
	 * ��֪activity �����
	 * @param info app���ơ�app������appicon�ķ�װ�����
	 */
	public void addLock(LockInfo info);
	
	/**
	 * ��֪activity ɾ����
	 * @param info app���ơ�app������appicon�ķ�װ�����
	 */
	public void deleteLock(LockInfo info);

}
