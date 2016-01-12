package com.mobilesecurity.activities;

import java.io.FileInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.mobilesecurity.R;
import com.mobilesecurity.db.dao.AntiVirusDao;
import com.mobilesecurity.ui.SettingItemView;

public class AntiVirusActivity extends Activity {
	protected static final int FINISHED = 3;
	protected static final int SCANNING_VIRUS = 1;
	protected static final int SCANNING_NOVIRUS = 2;
	private ImageView iv_scanning;
	private ProgressBar pb_scanning;
	private LinearLayout ll_scanning;
	private List<String> datas;
	private PackageManager pm;
	private MessageDigest digest;
	private Button bt_sure;
	private RemoveReceiver receiver;
	MyAdapter adapter;
	Animation a;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			TextView tv;
			switch (msg.what) {
			case SCANNING_VIRUS:
				tv = new TextView(AntiVirusActivity.this);
				tv.setText((String) msg.obj + " É¨ÃèÊÇ²¡¶¾");
				tv.setTextColor(Color.RED);
				ll_scanning.addView(tv, 0);
				break;
			case SCANNING_NOVIRUS:
				tv = new TextView(AntiVirusActivity.this);
				tv.setText((String) msg.obj + " É¨Ãè°²È«");
				tv.setTextColor(Color.GREEN);
				ll_scanning.addView(tv, 0);
				break;
			case FINISHED:
				iv_scanning.clearAnimation();
				bt_sure.setEnabled(true);
				//pb_scanning.setProgress(0);
				break;

			default:
				break;
			}
		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_antivirus);
		datas = new ArrayList<String>();
		ll_scanning = (LinearLayout) findViewById(R.id.ll_scanning_antivirus);
		iv_scanning = (ImageView) findViewById(R.id.iv_scanning_antivirus);
		pb_scanning = (ProgressBar) findViewById(R.id.pb_scan_antivirus);
		bt_sure = (Button) findViewById(R.id.bt_sure);
		receiver = new RemoveReceiver();
		
		IntentFilter filter = new IntentFilter();
		filter.addAction("android.intent.action.PACKAGE_REMOVED");
		filter.addDataScheme("package");
		registerReceiver(receiver, filter);
		
		pm = getPackageManager();
		try {
			digest = MessageDigest.getInstance("md5");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		startScan();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (receiver != null) {
			unregisterReceiver(receiver);
			receiver = null;
		}
	}

	private void startScan() {
		a = new RotateAnimation(0, 360, Animation.RELATIVE_TO_SELF, 0.5f,
				Animation.RELATIVE_TO_SELF, 0.5f);
		a.setDuration(500);
		a.setRepeatCount(Integer.MAX_VALUE);
		Interpolator interpolator = new LinearInterpolator   ();
		a.setInterpolator(interpolator);
		iv_scanning.startAnimation(a);

		bt_sure.setEnabled(false);
		new Thread() {
			public void run() {
				List<PackageInfo> packinfos = pm
						.getInstalledPackages(PackageManager.GET_UNINSTALLED_PACKAGES
								+ PackageManager.GET_SIGNATURES);
				pb_scanning.setMax(packinfos.size());
				String path;
				String md5;
				byte[] bs = new byte[1024];
				byte[] result;
				
				int progress = 0;
				for (PackageInfo info : packinfos) {
					path = info.applicationInfo.sourceDir;
					FileInputStream fin;
					StringBuilder sb = new StringBuilder();
					int len = 0;
					try {
						fin = new FileInputStream(path);
						while ((len = fin.read(bs)) != -1) {
							digest.update(bs, 0, len);
						}
						result = digest.digest();
						for (byte b : result) {
							String str = Integer.toHexString(b & 0xff);
							if (str.length() == 1) {
								sb.append("0");
							}
							sb.append(str);
						}
						String appname = info.applicationInfo.loadLabel(pm)
								.toString();
						Message msg = Message.obtain();

						if ("²úÉú²¡¶¾".equals(appname)) {
							System.out.println(appname+"  "+ sb.toString());
						}

						if (AntiVirusDao.find(sb.toString())) {
							datas.add(info.packageName);
							msg.what = SCANNING_VIRUS;

						} else {
							msg.what = SCANNING_NOVIRUS;
						}
						msg.obj = appname;
						handler.sendMessage(msg);
						progress++;
						pb_scanning.setProgress(progress);
						Thread.sleep(50);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				handler.sendEmptyMessage(FINISHED);
			};
		}.start();
	}

	public void click(View view) {
		View v = View.inflate(this, R.layout.part_virus_show, null);
		ListView lv = (ListView) v.findViewById(R.id.lv_virus_show);
		adapter = new MyAdapter();
		lv.setAdapter(adapter);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				String packname = datas.get(position);
				Intent intent = new Intent("android.intent.action.DELETE");
				intent.addCategory("android.intent.category.DEFAULT");
				intent.setData(Uri.parse("package:" + packname));
				startActivity(intent);
			}
		});
		ll_scanning.removeAllViews();
		ll_scanning.addView(v);
	}

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = new SettingItemView(AntiVirusActivity.this);
			}
			SettingItemView view = (SettingItemView) convertView;
			String packname = datas.get(position);
			String appname;
			try {
				appname = pm.getPackageInfo(packname, 0).applicationInfo
						.loadLabel(pm).toString();
				view.setMyText(appname);
			} catch (NameNotFoundException e) {
				e.printStackTrace();
			}
			return view;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}
	}

	/*
	 * if (intent.getAction().equals("android.intent.action.PACKAGE_REMOVED")) {
	 * System.out.println("********************************"); DatabaseHelper
	 * dbhelper = new DatabaseHelper();
	 * dbhelper.executeSql("delete from users"); }
	 */

	private class RemoveReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			
			String packageName = intent.getDataString().substring(8);
			System.out.println("É¾³ý£º" + packageName);
			datas.remove(packageName);
			adapter.notifyDataSetChanged();
		}

	}

}
