package com.mobilesecurity.activities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.PackageStats;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.RemoteException;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.graphics.drawable.Drawable;

import com.mobilesecurity.R;
import com.mobilesecurity.uitils.ToastUtils;

public class CleanCacheActivity extends Activity {

	protected static final int SCAN_FINISHED = 1;
	protected static final int SCANNING = 2;
	private static final int RESCAN = 3;
	private LinearLayout ll_scaning;
	private ListView lv_appcache;
	private ProgressBar pb_scancache;
	private TextView tv_scan_appname;
	private PackageManager pm;
	private List<CacheInfo> cacheInfos;
	private MyAdapter adapter;

	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case SCAN_FINISHED:
				ll_scaning.setVisibility(View.GONE);
				if (cacheInfos.size() == 0) {
					ToastUtils.showToast(CleanCacheActivity.this, "您的手机没有缓存！");
				} else {
					adapter = new MyAdapter();
					lv_appcache.setAdapter(adapter);
				}
				/*
				 * System.out.println(cacheInfos.size()); for (CacheInfo info :
				 * cacheInfos) { System.out.println(info.appName+"  cachesize"+
				 * info.cacheSize); }
				 */
				break;
			case SCANNING:
				tv_scan_appname.setText("正在扫描：" + (String) msg.obj);
				break;
			case RESCAN:

				scanAllApp();
				break;
			default:
				break;
			}

		};
	};

	private class MyAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			return cacheInfos.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHold hold;
			if (convertView != null) {
				hold = (ViewHold) convertView.getTag();
			} else {
				hold = new ViewHold();
				convertView = View.inflate(CleanCacheActivity.this,
						R.layout.item_scancache, null);
				hold.iv_clean = (ImageView) convertView
						.findViewById(R.id.iv_clean_cache);
				hold.iv_icon = (ImageView) convertView
						.findViewById(R.id.iv_icon_cache);
				hold.tv_appname = (TextView) convertView
						.findViewById(R.id.tv_appname_cache);
				hold.tv_cachesize = (TextView) convertView
						.findViewById(R.id.tv_cachesize_cache);
				hold.tv_codesize = (TextView) convertView
						.findViewById(R.id.tv_codesize_cache);
				hold.tv_datasize = (TextView) convertView
						.findViewById(R.id.tv_datasize_cache);
				convertView.setTag(hold);
			}

			final CacheInfo info = cacheInfos.get(position);
			hold.iv_clean.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					// 删除该应用缓存
					Intent intent = new Intent(
							"android.settings.APPLICATION_DETAILS_SETTINGS");
					intent.addCategory("android.intent.category.DEFAULT");
					intent.setData(Uri.parse("package:" + info.packName));
					startActivity(intent);
				}
			});

			hold.iv_icon.setImageDrawable(info.icon);
			hold.tv_appname.setText(info.appName);
			hold.tv_cachesize.setText(Formatter.formatFileSize(
					CleanCacheActivity.this, info.cacheSize));
			hold.tv_datasize.setText(Formatter.formatFileSize(
					CleanCacheActivity.this, info.dataSize));
			hold.tv_codesize.setText(Formatter.formatFileSize(
					CleanCacheActivity.this, info.codeSize));

			return convertView;
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

	private class ViewHold {
		private ImageView iv_icon;
		private TextView tv_appname;
		private ImageView iv_clean;
		private TextView tv_cachesize;
		private TextView tv_codesize;
		private TextView tv_datasize;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_cleancache);
		ll_scaning = (LinearLayout) findViewById(R.id.ll_scaning);
		lv_appcache = (ListView) findViewById(R.id.lv_appcache);
		pb_scancache = (ProgressBar) findViewById(R.id.pb_scancache);
		tv_scan_appname = (TextView) findViewById(R.id.tv_scan_appname);
		pm = getPackageManager();
		cacheInfos = new ArrayList<CleanCacheActivity.CacheInfo>();
		scanAllApp();
	}

	private void scanAllApp() {
		ll_scaning.setVisibility(View.VISIBLE);
		cacheInfos.clear();
		new Thread() {
			public void run() {
				List<PackageInfo> packInfos = pm.getInstalledPackages(0);
				pb_scancache.setMax(packInfos.size());
				int progress = 0;
				for (PackageInfo info : packInfos) {
					Message msg = Message.obtain();
					msg.what = SCANNING;
					msg.obj = info.applicationInfo.loadLabel(pm).toString();
					handler.sendMessage(msg);
					try {
						// public abstract void getPackageSizeInfo(String
						// packageName,IPackageStatsObserver observer)
						Method method = PackageManager.class.getMethod(
								"getPackageSizeInfo", String.class,
								IPackageStatsObserver.class);
						method.invoke(pm, info.packageName,
								new MyPackageStatsObserver());
						Thread.sleep(50);
					} catch (Exception e) {
						e.printStackTrace();
					}

					progress++;
					pb_scancache.setProgress(progress);
				}

				Message msg = Message.obtain();
				msg.what = SCAN_FINISHED;
				handler.sendMessage(msg);
			};
		}.start();

	}

	private class MyPackageStatsObserver extends IPackageStatsObserver.Stub {
		@Override
		public void onGetStatsCompleted(PackageStats pStats, boolean succeeded)
				throws RemoteException {
			if (pStats.cacheSize > 0) {
				try {
					CacheInfo info = new CacheInfo();
					info.packName = pStats.packageName;
					info.cacheSize = pStats.cacheSize;
					info.codeSize = pStats.codeSize;
					info.dataSize = pStats.dataSize;

					PackageInfo pinfo = pm
							.getPackageInfo(pStats.packageName, 0);
					info.appName = pinfo.applicationInfo.loadLabel(pm)
							.toString();
					info.icon = pinfo.applicationInfo.loadIcon(pm);
					cacheInfos.add(info);
					System.out.println("应用名称: " + info.appName + ", codesize: "
							+ info.codeSize);
				} catch (NameNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	private class MyPackageDataObserver extends IPackageDataObserver.Stub {

		@Override
		public void onRemoveCompleted(String packageName, boolean succeeded)
				throws RemoteException {
			System.out.println("清理完成！");
			handler.sendEmptyMessage(RESCAN);
		}
	}

	public void cleanAll(View view) {

		// public abstract void freeStorageAndNotify(long freeStorageSize,
		// IPackageDataObserver observer);
		if (cacheInfos.size() == 0) {
			ToastUtils.showToast(this, "应用程序无缓存可以清理！");
			return;
		}
		// System.out.println("全部清理开始！");
		try {
			Method method = PackageManager.class.getMethod(
					"freeStorageAndNotify", long.class,
					IPackageDataObserver.class);
			method.invoke(pm, Integer.MAX_VALUE, new MyPackageDataObserver());
			// System.out.println(method);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public class CacheInfo {
		private Drawable icon;
		private String packName;
		private String appName;
		private long cacheSize;
		private long dataSize;
		private long codeSize;

	}
}
