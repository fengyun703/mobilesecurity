package com.mobilesecurity.services;

import com.mobilesecurity.R;
import com.mobilesecurity.db.dao.AddressDao;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class AddressQureyService extends Service {
	private TelephonyManager tm;
	private Listener listener;
	private NewCallOutReceiver callReceiver;
	private WindowManager wm;
	private View toastView;
	private static int[] icons = { R.drawable.call_locate_blue,
			R.drawable.call_locate_gray, R.drawable.call_locate_green,
			R.drawable.call_locate_orange, R.drawable.call_locate_white };
	private SharedPreferences sp;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		// System.out.println("启动归属地查询服务");
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		listener = new Listener();
		callReceiver = new NewCallOutReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(callReceiver, filter);
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);
		wm = (WindowManager) getSystemService(WINDOW_SERVICE);
	}

	class Listener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			super.onCallStateChanged(state, incomingNumber);

			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE:
				// System.out.println("CALL_STATE_IDLE   "+toastView);
				if (toastView != null) {
					wm.removeView(toastView);
					toastView = null;
				}
				break;
			case TelephonyManager.CALL_STATE_OFFHOOK:

				break;
			case TelephonyManager.CALL_STATE_RINGING:

				String address = AddressDao.getAddress(incomingNumber);
				showToast(address);
				// System.out.println("查询来电归属地"+incomingNumber+"   "+address);
				break;

			default:
				break;
			}
		}
	}

	class NewCallOutReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {

			String number = getResultData();
			String address = AddressDao.getAddress(number);
			showToast(address);
			// System.out.println("查询去电归属地"+number+"   "+address);
		}
	}

	public void showToast(String address) {
		WindowManager.LayoutParams params = new WindowManager.LayoutParams();
		toastView = View.inflate(this, R.layout.mytoast_address, null);
		TextView tv = (TextView) toastView.findViewById(R.id.tv_address_toast);
		tv.setText(address);
		toastView.setBackgroundResource(icons[sp.getInt("color", 0)]);
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE
				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE
				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;
		//params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;

		params.format = PixelFormat.TRANSLUCENT;
		//params.type =  WindowManager.LayoutParams.TYPE_APPLICATION_PANEL;
		params.type = WindowManager.LayoutParams.TYPE_TOAST;
		//params.setTitle("Toast");
		/*toastView.setOnTouchListener(new OnTouchListener() {

			
			private int startx = 0;
			private int starty = 0;
			private int endx = 0;
			private int endy = 0;
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				System.out.println("相应点击事件");
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					startx =(int) event.getX();
					starty = (int) event.getY();
					break;
				case MotionEvent.ACTION_UP:

					break;
				case MotionEvent.ACTION_MOVE:
					endx = (int) event.getX();
					endy = (int) event.getY();
					AnimatorSet set = new AnimatorSet();
					ObjectAnimator oa1 = ObjectAnimator.ofFloat(v, "translationX", endx- startx);
					ObjectAnimator oa2 = ObjectAnimator.ofFloat(v, "translationY", endy-starty);
					set.playTogether(oa1, oa2);
					set.start();
					break;

				default:
					break;
				}
				return true;
			}
		});*/
		wm.addView(toastView, params);

	}

	@Override
	public void onDestroy() {
		// System.out.println("关闭归属地查询服务");
		super.onDestroy();
		if (listener != null) {
			tm.listen(listener, PhoneStateListener.LISTEN_NONE);
			listener = null;
		}
		if (callReceiver != null) {
			unregisterReceiver(callReceiver);
			callReceiver = null;
		}
		if (toastView != null) {
			wm.removeView(toastView);
			toastView = null;
		}

	}

}
