package com.mobilesecurity.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.TelephonyManager;
import android.telephony.gsm.SmsManager;
import android.text.TextUtils;

public class BootCompletedReceiver extends BroadcastReceiver {

	private SharedPreferences sp ;
	private TelephonyManager tm;
	@Override
	public void onReceive(Context context, Intent intent) {
		sp = context.getSharedPreferences("config", Context.MODE_PRIVATE);
		tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
		if(sp.getBoolean("isprotecting", false)){
			String sim = sp.getString("sim", null);
			if(!TextUtils.isEmpty(sim)){
				if(!sim.equals(tm.getSimSerialNumber())){
					SmsManager.getDefault().sendTextMessage(sp.getString("phone", ""), null, "SIM卡已经变更", null, null);
				}
			}
		}else{
			System.out.println("没有开启防盗保护");
		}
	}

}
