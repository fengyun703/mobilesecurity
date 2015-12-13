package com.mobilesecurity.uitils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStateUtils {

	public static Boolean isRunning(Context context, String className){
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> list = am.getRunningServices(1000);
		for(RunningServiceInfo info: list){
			if(info.service.getClassName().equals(className)){
				//System.out.println("�����������У�"); 
				return true;
			}
		}
		//System.out.println("����ֹͣ�����ˣ�"); 
		return false;
		
	}
}
