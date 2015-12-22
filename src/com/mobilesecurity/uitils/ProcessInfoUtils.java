package com.mobilesecurity.uitils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.mobilesecurity.R;
import com.mobilesecurity.db.bean.MyProcessInfo;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningTaskInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.drawable.Drawable;

public class ProcessInfoUtils {

	public static long getFreeMemsize(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;
	}

	public static int getProcessCount(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
		return list.size();
	}

	public static long getTotalMemsize(Context context) {
		File file = new File("/proc/meminfo");
		try {
			BufferedReader reader = new BufferedReader(new FileReader(file));
			String line = reader.readLine();
			String[] strs = line.split("\\s+");
			//System.out.println(Long.parseLong(strs[1]));
			reader.close();
			return Long.parseLong(strs[1])*1024;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return 0;
		
	}

	public static List<MyProcessInfo> getProcessInfo(Context context) {
		List<MyProcessInfo> infos = new ArrayList<MyProcessInfo>();
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		PackageManager pm = context.getPackageManager();
		List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
		for(RunningAppProcessInfo info:list){
			MyProcessInfo mInfo = new MyProcessInfo();
			//包名
			String packageName = info.processName;
			mInfo.setPackageName(packageName);
			
			ApplicationInfo appinfo;
			try {
				//应用程序名称
				appinfo = pm.getApplicationInfo(packageName, 0);
				String appName =appinfo.loadLabel(pm).toString();
				mInfo.setAppName(appName);
				//图标
				Drawable icon = appinfo.loadIcon(pm);
				mInfo.setIcon(icon);
				//用户进程
				if((appinfo.flags & ApplicationInfo.FLAG_SYSTEM) != 0){
					mInfo.setUserProcess(false);
				}else{
					mInfo.setUserProcess(true);
				}
				
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				mInfo.setUserProcess(false);
				mInfo.setAppName(packageName);
				mInfo.setIcon(context.getResources().getDrawable(R.drawable.ic_launcher));
			}
			
			int memSize = am.getProcessMemoryInfo(new int[]{info.pid})[0].getTotalPrivateDirty()*1024;
			mInfo.setMemSize(memSize);
			mInfo.setChecked(false);
			infos.add(mInfo);
		}
		//System.out.println(infos);
		return infos;
	}

}
