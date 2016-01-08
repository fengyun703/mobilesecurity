package com.mobilesecurity.uitils;

import java.util.ArrayList;
import java.util.List;

import com.mobilesecurity.db.bean.MyTrafficInfo;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.TrafficStats;

public class TrafficUtils {
	
	public static List<MyTrafficInfo> getTafficInfo(Context context){
		List<MyTrafficInfo> list= new ArrayList<MyTrafficInfo>();
		MyTrafficInfo mInfo = null;
		PackageManager pm = context.getPackageManager();
	    List<PackageInfo> packinfos = pm.getInstalledPackages(0);
		for(PackageInfo info :packinfos){
			long rx = TrafficStats.getUidRxBytes(info.applicationInfo.uid);
			long tx = TrafficStats.getUidTxBytes(info.applicationInfo.uid);
			String appname = info.applicationInfo.loadLabel(pm).toString();
			System.out.println(appname+"rx = "+ rx +",  tx = "+ tx);
			if(rx>0||tx>0){
				mInfo = new MyTrafficInfo();
				mInfo.setIcon(info.applicationInfo.loadIcon(pm));
				mInfo.setRx(rx);
				mInfo.setTx(tx);
				mInfo.setTotal(rx+tx);
				mInfo.setUid(info.applicationInfo.uid);
				mInfo.setAppName(appname);
				list.add(mInfo);
			}
		}
		return list;
		
	}

}
