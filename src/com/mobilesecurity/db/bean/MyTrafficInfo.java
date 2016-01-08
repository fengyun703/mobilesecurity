package com.mobilesecurity.db.bean;

import android.graphics.drawable.Drawable;

public class MyTrafficInfo {

	private Drawable icon;
	private String appName;
	private int uid;
	private long rx;
	private long tx;
	private long total;
	
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public int getUid() {
		return uid;
	}
	public void setUid(int uid) {
		this.uid = uid;
	}
	public long getRx() {
		return rx;
	}
	public void setRx(long rx) {
		this.rx = rx;
	}
	public long getTx() {
		return tx;
	}
	public void setTx(long tx) {
		this.tx = tx;
	}
	public long getTotal() {
		return total;
	}
	public void setTotal(long total) {
		this.total = total;
	}
	
}
