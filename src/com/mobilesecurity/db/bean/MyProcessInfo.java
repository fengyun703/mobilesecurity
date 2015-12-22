package com.mobilesecurity.db.bean;

import android.graphics.drawable.Drawable;

public class MyProcessInfo {

	private boolean checked;
	private String packageName;
	private String appName;
	private long memSize;
	private Drawable icon;
	private boolean isUserProcess;
	
	public boolean isChecked() {
		return checked;
	}
	
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
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
	public long getMemSize() {
		return memSize;
	}
	public void setMemSize(long memSize) {
		this.memSize = memSize;
	}
	public Drawable getIcon() {
		return icon;
	}
	public void setIcon(Drawable icon) {
		this.icon = icon;
	}
	public boolean isUserProcess() {
		return isUserProcess;
	}
	public void setUserProcess(boolean isUserProcess) {
		this.isUserProcess = isUserProcess;
	}

	@Override
	public String toString() {
		return "MyProcessInfo [checked=" + checked + ", packageName="
				+ packageName + ", appName=" + appName + ", memSize=" + memSize
				+ ", isUserProcess=" + isUserProcess + "]";
	}
	
	
}
