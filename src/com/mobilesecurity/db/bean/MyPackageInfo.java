package com.mobilesecurity.db.bean;

import android.graphics.drawable.Drawable;

public class MyPackageInfo {

	private String packageName;
	private String appName;
	private Drawable icon;
	private Boolean isInRom;
	private Boolean isUserApp;
	private long appSize;
	
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
	public Boolean getIsInRom() {
		return isInRom;
	}
	public void setIsInRom(Boolean isInRom) {
		this.isInRom = isInRom;
	}
	public Boolean getIsUserApp() {
		return isUserApp;
	}
	public void setIsUserApp(Boolean isUserApp) {
		this.isUserApp = isUserApp;
	}
	public long getAppSize() {
		return appSize;
	}
	public void setAppSize(long appSize) {
		this.appSize = appSize;
	}
	
	public Boolean isInRoM(){
		return isInRom;
	}
	
	public Boolean isUserApp(){
		return isUserApp;
	}
	@Override
	public String toString() {
		return "MyPackageInfo [packageName=" + packageName + ", appName="
				+ appName + ", icon=" + icon + ", isInRom=" + isInRom
				+ ", isUserApp=" + isUserApp + ", appSize=" + appSize + "]";
	}
	
	
}
