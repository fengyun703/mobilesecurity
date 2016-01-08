package com.mobilesecurity.uitils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

import com.mobilesecurity.db.bean.MyPackageInfo;

public class AppInfoUtils {
	public static String getVersionName(Context context){
		try {
			PackageManager  pm = context.getPackageManager();
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionName;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int getVersionCode(Context context){
		try {
			PackageManager  pm = context.getPackageManager();
			PackageInfo info = pm.getPackageInfo(context.getPackageName(), 0);
			return info.versionCode;
		} catch (NameNotFoundException e) {
			e.printStackTrace();
			return 0;
		}
	}
	
	
	public static List<MyPackageInfo> getPackageInfoAll(Context context){
		PackageManager  pm = context.getPackageManager();
		List<MyPackageInfo> list = new ArrayList<MyPackageInfo>();
		List<PackageInfo> packageinfos = pm.getInstalledPackages(0);
		for(PackageInfo info: packageinfos){
			MyPackageInfo myinfo = new MyPackageInfo();
			myinfo.setAppName(info.applicationInfo.loadLabel(pm).toString());
			myinfo.setPackageName(info.packageName);
			myinfo.setIcon(info.applicationInfo.loadIcon(pm));
			File file = new File(info.applicationInfo.sourceDir);
			myinfo.setAppSize(file.length());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(file.length()+"   "+ file.getTotalSpace()+"  "+ file.getUsableSpace()+" "+file.getFreeSpace());
			if((info.applicationInfo.flags|ApplicationInfo.FLAG_SYSTEM) == 0){
				myinfo.setIsUserApp(true);
			}else{
				myinfo.setIsUserApp(false);
			}
			
			if((info.applicationInfo.flags|ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0){
				myinfo.setIsInRom(true);
			}else{
				myinfo.setIsInRom(false);
			}
			list.add(myinfo);
		}
		return list;
	}
	
	public static void getPackageInfo(Context context, List<MyPackageInfo> listUser, List<MyPackageInfo> listSys){
		PackageManager  pm = context.getPackageManager();
		List<PackageInfo> packageinfos = pm.getInstalledPackages(0);
		for(PackageInfo info: packageinfos){
			MyPackageInfo myinfo = new MyPackageInfo();
			
			//ActivityInfo[] ainfos =  info.activities;
			//System.out.println(ainfos);
			
			myinfo.setAppName(info.applicationInfo.loadLabel(pm).toString());
			myinfo.setPackageName(info.packageName);
			myinfo.setIcon(info.applicationInfo.loadIcon(pm));
			File file = new File(info.applicationInfo.sourceDir);
			myinfo.setAppSize(file.length());
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			//System.out.println(file.length()+"   "+ file.getTotalSpace()+"  "+ file.getUsableSpace()+" "+file.getFreeSpace());
			if((info.applicationInfo.flags&ApplicationInfo.FLAG_SYSTEM) == 0){
				myinfo.setIsUserApp(true);
			}else{
				myinfo.setIsUserApp(false);
			}
			
			if((info.applicationInfo.flags&ApplicationInfo.FLAG_EXTERNAL_STORAGE)==0){
				myinfo.setIsInRom(true);
			}else{
				myinfo.setIsInRom(false);
			}
			
			if(myinfo.isUserApp()){
				listUser.add(myinfo);
			}else{
				listSys.add(myinfo);
			}
		}
	}
	


}
