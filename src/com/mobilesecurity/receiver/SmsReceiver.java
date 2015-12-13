package com.mobilesecurity.receiver;

import com.mobilesecurity.R;
import com.mobilesecurity.services.GpsService;

import android.app.admin.DevicePolicyManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

public class SmsReceiver extends BroadcastReceiver {
	private DevicePolicyManager dpm;

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] pdus = (Object[]) intent.getExtras().get("pdus");
		SmsMessage message;
		ComponentName who = new ComponentName(context, MyAdminReceiver.class);
		System.out.println("ComponentName: "+ who );
		dpm= (DevicePolicyManager) context.getSystemService(Context.DEVICE_POLICY_SERVICE);
		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		for (int i = 0, len = pdus.length; i < len; i++) {
			byte[] pdu = (byte[]) pdus[i];
			message = SmsMessage.createFromPdu(pdu);
			String body = message.getMessageBody();
			/*String phone1 = message.getServiceCenterAddress();
			String phone2 = message.getOriginatingAddress();
			String phone3 = message.getDisplayOriginatingAddress() ;
	

			System.out.println("body:  " + body);
			System.out.println("phone getOriginatingAddress:  " + phone2);
			System.out.println("phone getServiceCenterAddress:  " + phone1);
			System.out.println("phone getDisplayOriginatingAddress:  " + phone3);*/
			//if (sp.getString("phone", "").equals(phone)) {
				if ("#*location*#".equals(body)) {
					System.out.println("开启定位系统");
					Intent service = new Intent(context, GpsService.class);
					context.startService(service);
					abortBroadcast();
				} else if ("#*alarm*#".equals(body)) {
					System.out.println("播放报警音乐");
					MediaPlayer mp = MediaPlayer.create(context, R.raw.ylzs);
					mp.start();
					abortBroadcast();
				} else if ("#*wipedate*#".equals(body)) {
					if(dpm.isAdminActive(who)){
						System.out.println("远程销毁数据");
						dpm.wipeData(DevicePolicyManager.WIPE_EXTERNAL_STORAGE);
						//dpm.wipeData(0);
					}
					abortBroadcast();
				} else if ("#*lockscreen*#".equals(body)) {
					if(dpm.isAdminActive(who)){
						System.out.println("远程锁屏");
						dpm.lockNow();
						dpm.resetPassword("123", 0);
						//dpm.wipeData(0);
					}
					abortBroadcast();
				}
			}
		//}

	}

}
