package com.mobilesecurity.services;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.android.internal.telephony.ITelephony;
import com.mobilesecurity.db.dao.MyBlackNumberDao;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;
import android.os.RemoteException;
import android.telephony.PhoneStateListener;

import android.telephony.SmsMessage;
import android.telephony.TelephonyManager;

public class BlackNumberService extends Service {
	private MyBlackNumberDao dao;
	private SmsBlackReceiver receiver;
	private TelephonyManager tm;
	private PhoneStateListener listener;
	private ContentObserver observer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new MyBlackNumberDao(this);
		
		receiver = new SmsBlackReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.provider.Telephony.SMS_RECEIVED");
		registerReceiver(receiver, filter);
		
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new Listener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		//System.out.println("开启BlackNumberService"); 
		//System.out.println("BlackNumberService: "+getContentResolver().toString());
		//System.out.println("BlackNumberService: "+getApplicationContext().getContentResolver().toString());
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
		if (listener != null) {
			tm.listen(listener, PhoneStateListener.LISTEN_NONE);
			listener = null;
		}
		if (observer != null) {
			getContentResolver().unregisterContentObserver(observer);
			observer = null;
		}
	}

	class SmsBlackReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			Object[] pdus = (Object[]) intent.getExtras().get("pdus");
			SmsMessage message;
			for (int i = 0, len = pdus.length; i < len; i++) {
				byte[] pdu = (byte[]) pdus[i];
				message = SmsMessage.createFromPdu(pdu);
				String mode = dao.find(message.getOriginatingAddress());
				//System.out.println(mode);
				if ("2".equals(mode) || "3".equals(mode)) {
					//System.out.println("拦截短信了！");
					abortBroadcast();
				}
			}
		}
	}

	class Listener extends PhoneStateListener {

		@Override
		public void onCallStateChanged(int state, final String incomingNumber) {
			// TODO Auto-generated method stub
			super.onCallStateChanged(state, incomingNumber);
			//System.out.println("incomingNumber="+incomingNumber);
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;
			case TelephonyManager.CALL_STATE_RINGING:
				String mode = dao.find(incomingNumber);
				System.out.println("CALL_STATE_RINGING");
				if ("1".equals(mode) || "3".equals(mode)) {
					endcall();
					//System.out.println("拦截电话了！");
					
					
					// 使用内容观察者删除黑名单记录
					
					observer = new ContentObserver(new Handler()) {
						@Override
						public void onChange(boolean selfChange) {
							super.onChange(selfChange);
							deleteLog(incomingNumber);
							//注销前一个内容观察者
							if(observer!=null){
								System.out.println(observer);
								getContentResolver().unregisterContentObserver(observer);
								observer = null;
							}
						}
					};
					getContentResolver().registerContentObserver(
							Uri.parse("content://call_log/calls"), true,
							observer);
				}
				break;

			default:
				break;
			}
		}
	}

	public void endcall() {
		
		try {
			Method iTelephoneMethod = TelephonyManager.class.getDeclaredMethod(
					"getITelephony", (Class[]) null);
			iTelephoneMethod.setAccessible(true);
			ITelephony iTelephony = (ITelephony) iTelephoneMethod.invoke(tm,
					(Object[]) null);
			iTelephony.endCall();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (RemoteException e) {
			e.printStackTrace();
		}

	}

	public void deleteLog(String phone) {
		ContentResolver cr = getContentResolver();
		int i = cr.delete(Uri.parse("content://call_log/calls"), "number = ?",
				new String[] { phone });
		System.out.println(i);
		
	}

}
