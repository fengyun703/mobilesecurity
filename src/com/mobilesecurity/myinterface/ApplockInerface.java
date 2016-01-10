package com.mobilesecurity.myinterface;

import com.mobilesecurity.db.bean.LockInfo;

public interface ApplockInerface {

	/**
	 * 告知activity 添加锁
	 * @param info app名称、app包名、appicon的封装类对象
	 */
	public void addLock(LockInfo info);
	
	/**
	 * 告知activity 删除锁
	 * @param info app名称、app包名、appicon的封装类对象
	 */
	public void deleteLock(LockInfo info);

}
