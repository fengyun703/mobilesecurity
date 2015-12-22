package com.mobilesecurity.services;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class AutoKillService extends Service {

	private MyReceiver receiver;
	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("开启AutokillService");
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_OFF);
		receiver = new MyReceiver();
		registerReceiver(receiver, filter);
	}
	
	private class MyReceiver extends BroadcastReceiver{
		@Override
		public void onReceive(Context context, Intent intent) {
			//System.out.println("锁屏清除进程！");
			ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
			List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
			for(RunningAppProcessInfo info: list){
				am.killBackgroundProcesses(info.processName);
			}
		}
	}
	
	@Override
	public void onDestroy() {
		super.onDestroy();
		unregisterReceiver(receiver);
	}
}
