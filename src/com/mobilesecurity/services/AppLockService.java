package com.mobilesecurity.services;

import java.util.LinkedList;
import java.util.List;

import com.mobilesecurity.activities.AppLockPasswordActivity;
import com.mobilesecurity.db.dao.AppLockDao;

import android.app.ActivityManager.RunningTaskInfo;
import android.app.ActivityManager;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.IBinder;

public class AppLockService extends Service {

	private boolean flag = false;
	private List<String> lockPackNames;
	private List<RunningTaskInfo> taskInfos;
	private ActivityManager am;
	private AppLockDao dao;
	private List<String> tempPacknames;
	private MyLockReceiver receiver;
	private MyDbObserver observer;

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		dao = new AppLockDao(this);
		am = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
		tempPacknames = new LinkedList<String>();
		lockPackNames = dao.getAllPackName();
		receiver = new MyLockReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction("com.mobilesecurity.unlockapp");
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		filter.addAction(Intent.ACTION_SCREEN_ON);
		registerReceiver(receiver, filter);

		observer = new MyDbObserver(new Handler());
		Uri uri = Uri.parse("content://com.mobilesecurity.updateLockDb");
		getContentResolver().registerContentObserver(uri, true, observer);
		startWatchDog();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		flag = false;
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
		if (observer != null) {
			getContentResolver().unregisterContentObserver(observer);
			observer = null;
		}

	}

	private void startWatchDog() {
		// 若存在子线程再进入，会再创建子线程，必须避免。
		if (flag) {
			return;
		}
		flag = true;
		new Thread() {
			public void run() {
				while (flag) {
					taskInfos = am.getRunningTasks(1);
					RunningTaskInfo info = taskInfos.get(0);
					String packname = info.topActivity.getPackageName();
					//System.out.println(lockPackNames.toString());
					if (lockPackNames.contains(packname)) {
						// 弹出输入密码界面
						if (!tempPacknames.contains(packname)) {
							Intent intent = new Intent(AppLockService.this,
									AppLockPasswordActivity.class);
							intent.putExtra("packname", packname);
							//System.out.println(packname);
							// 服务里面没有任务栈信息,如果在服务开启Activity需要记得添加任务栈的flag
							intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
							startActivity(intent);
						}
					}

					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			};
		}.start();
	}

	private class MyLockReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if ("com.mobilesecurity.unlockapp".equals(action)) {
				String packname = intent.getStringExtra("packname");
				tempPacknames.add(packname);
			} else if (Intent.ACTION_SCREEN_OFF.equals(action)) {
				flag = false;
				tempPacknames.clear();
			} else if (Intent.ACTION_SCREEN_ON.equals(action)) {
				startWatchDog();
			}
		}
	}

	private class MyDbObserver extends ContentObserver {

		public MyDbObserver(Handler handler) {
			super(handler);
		}

		@Override
		public void onChange(boolean selfChange) {
			System.out.println("数据库发生改变!");
			lockPackNames = dao.getAllPackName();
		}

	}
}
