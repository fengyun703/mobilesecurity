package com.mobilesecurity.receiver;

import java.util.List;

import com.mobilesecurity.uitils.ToastUtils;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

public class KillProcessReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> list = am.getRunningAppProcesses();
		for(RunningAppProcessInfo info: list){
			am.killBackgroundProcesses(info.processName);
		}
		Toast.makeText(context, "Çå³ýÍê³É£¡", 0).show();
	}

}
