package com.mobilesecurity.db.bean;

import android.graphics.drawable.Drawable;

public class LockInfo {
	private String packageName;
	private String appName;
	private Drawable icon;
	
	public String getPackageName() {
		return packageName;
	}
	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}
	public String getAppName() {
		return appName;
	}
	public void setAppName(String appName) {
		this.appName = appName;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	@Override
	public String toString() {
		return "LockInfo [packageName=" + packageName + ", appName=" + appName
				+ ", icon=" + icon + "]";
	}
	
	
	
}
