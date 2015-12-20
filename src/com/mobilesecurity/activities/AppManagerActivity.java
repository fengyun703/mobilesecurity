package com.mobilesecurity.activities;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.w3c.dom.Text;

import com.mobilesecurity.R;
import com.mobilesecurity.db.bean.MyPackageInfo;
import com.mobilesecurity.uitils.AppInfoUtils;
import com.mobilesecurity.uitils.ToastUtils;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.renderscript.ProgramFragmentFixedFunction.Builder.Format;
import android.text.format.Formatter;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class AppManagerActivity extends Activity implements OnClickListener {

	// private List<MyPackageInfo> listAll;
	private List<MyPackageInfo> listUser;
	private List<MyPackageInfo> listSys;
	private LinearLayout loading;
	private TextView freeofmemery;
	private TextView freeofsd;
	private TextView tv_status_app;
	private PopupWindow popupw;
	private LinearLayout ll_popup_info;
	private LinearLayout ll_popup_delete;
	private LinearLayout ll_popup_share;
	private LinearLayout ll_popup_startup;
	private UnistallReceiver uninstallReceiver;
	private InstallReceiver installReceiver;

	private MyPackageInfo currentInfo;
	private ListView lv;
	private Myadaper myadaper;
	private static final int INFO_FINISH = 1;
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case INFO_FINISH:
				loading.setVisibility(View.INVISIBLE);
				myadaper = new Myadaper();
				lv.setAdapter(myadaper);
				break;

			default:
				break;
			}

		};
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_appmanager);
		loading = (LinearLayout) findViewById(R.id.ll_loading_app);
		freeofmemery = (TextView) findViewById(R.id.tv_freeofmemery_app);
		freeofsd = (TextView) findViewById(R.id.tv_freeofsd_app);
		tv_status_app = (TextView) findViewById(R.id.tv_status_app);

		File dataDir = Environment.getDataDirectory();
		File sdDir = Environment.getExternalStorageDirectory();
		long memerySize = dataDir.getFreeSpace();
		long sdSize = sdDir.getFreeSpace();
		freeofmemery.setText("内存可用："
				+ Formatter.formatFileSize(this, memerySize));
		freeofsd.setText("sd卡可用：" + Formatter.formatFileSize(this, sdSize));
		// System.out.println(dataDir.length()+"   "+dataDir.getFreeSpace());
		lv = (ListView) findViewById(R.id.lv_content_app);
		listUser = new ArrayList<MyPackageInfo>();
		listSys = new ArrayList<MyPackageInfo>();

		loadAppinfo();
		uninstallReceiver = new UnistallReceiver();
		IntentFilter unfilter = new IntentFilter();
		unfilter.addAction(Intent.ACTION_PACKAGE_REMOVED);
		unfilter.addDataScheme("package");
		registerReceiver(uninstallReceiver, unfilter);

		installReceiver = new InstallReceiver();
		IntentFilter infilter = new IntentFilter();
		infilter.addAction(Intent.ACTION_PACKAGE_ADDED);
		infilter.addDataScheme("package");
		registerReceiver(installReceiver, infilter);

		lv.setOnScrollListener(new OnScrollListener() {

			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {

			}

			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				closePopupw();
				if (firstVisibleItem <= listUser.size()) {
					tv_status_app.setText("用户应用：" + listUser.size() + "个");
				} else {
					tv_status_app.setText("系统应用：" + listSys.size() + "个");
				}
			}
		});

		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				closePopupw();
				if (position == 0 || position == (listUser.size() + 1)) {
					return;
				} else if (position <= listUser.size()) {
					currentInfo = listUser.get(position - 1);
				} else {
					currentInfo = listSys.get(position - (1 + listUser.size())
							- 1);
				}
				View view1 = View.inflate(AppManagerActivity.this,
						R.layout.item_popupw_app, null);
				popupw = new PopupWindow(view1,
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT);
				popupw.setBackgroundDrawable(new ColorDrawable(
						Color.TRANSPARENT));
				int[] location = new int[2];
				view.getLocationOnScreen(location);
				popupw.showAtLocation(parent, Gravity.LEFT | Gravity.TOP, 60,
						location[1]);
				AnimationSet set = new AnimationSet(false);
				Animation aa = new AlphaAnimation(0, 1);
				aa.setDuration(500);
				Animation sa = new ScaleAnimation(0.2f, 1, 0.2f, 1,
						Animation.RELATIVE_TO_SELF, 0,
						Animation.RELATIVE_TO_SELF, 0.5f);
				sa.setDuration(500);
				set.addAnimation(sa);
				set.addAnimation(aa);
				view1.startAnimation(set);

				ll_popup_info = (LinearLayout) view1
						.findViewById(R.id.ll_popu_info);

				ll_popup_share = (LinearLayout) view1
						.findViewById(R.id.ll_popu_share);
				ll_popup_delete = (LinearLayout) view1
						.findViewById(R.id.ll_popu_delete);
				ll_popup_startup = (LinearLayout) view1
						.findViewById(R.id.ll_popu_startup);
				ll_popup_delete.setOnClickListener(AppManagerActivity.this);
				ll_popup_share.setOnClickListener(AppManagerActivity.this);
				ll_popup_info.setOnClickListener(AppManagerActivity.this);
				ll_popup_startup.setOnClickListener(AppManagerActivity.this);

			}
		});

	}

	private void loadAppinfo() {
		loading.setVisibility(View.VISIBLE);
		new Thread() {
			public void run() {
				// listAll =
				// AppInfoUtils.getPackageInfoAll(AppManagerActivity.this);
				AppInfoUtils.getPackageInfo(AppManagerActivity.this, listUser,
						listSys);
				// System.out.println("listAll = "+listAll);
				// System.out.println("listUser"+listUser);
				// System.out.println("listSys"+listSys);
				handler.sendEmptyMessage(INFO_FINISH);
			};
		}.start();
	}

	private class Myadaper extends BaseAdapter {

		@Override
		public int getCount() {
			return listUser.size() + listSys.size();
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			System.out.println("position:"+position);
			ViewHold hold = null;
			MyPackageInfo info = null;
			if (position == 0) {
				TextView tv = new TextView(AppManagerActivity.this);
				tv.setText("用户应用：" + listUser.size() + "个");
				tv.setTextSize(16);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			} else if (position == (listUser.size() + 1)) {
				TextView tv = new TextView(AppManagerActivity.this);
				tv.setText("系统应用：" + listSys.size() + "个");
				tv.setTextSize(16);
				tv.setBackgroundColor(Color.GRAY);
				return tv;
			} else if (position <= listUser.size()) {
				int location = position - 1;
				info = listUser.get(location);
			} else {
				int location = position - 1 - listUser.size() - 1;
				info = listSys.get(location);
			}

			if (convertView instanceof RelativeLayout) {
				hold = (ViewHold) convertView.getTag();
			} else {
				convertView = View.inflate(AppManagerActivity.this,
						R.layout.item_appinfo, null);
				hold = new ViewHold();
				hold.tv_isinrom = (TextView) convertView
						.findViewById(R.id.tv_isInRom);
				hold.tv_name = (TextView) convertView
						.findViewById(R.id.tv_appname);
				hold.img_icon = (ImageView) convertView
						.findViewById(R.id.iv_appicon);
				hold.tv_size = (TextView) convertView
						.findViewById(R.id.tv_appszie);
				convertView.setTag(hold);
			}
			

			hold.tv_name.setText(info.getAppName());
			hold.img_icon.setImageDrawable(info.getIcon());
			hold.tv_size.setText(Formatter.formatFileSize(
					AppManagerActivity.this, info.getAppSize()));
			hold.tv_isinrom.setText(info.isInRoM() == true ? "内存应用" : "SD卡应用");
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
		TextView tv_name;
		TextView tv_isinrom;
		ImageView img_icon;
		TextView tv_size;
	}

	private void closePopupw() {
		if (popupw != null) {
			popupw.dismiss();
			popupw = null;
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		closePopupw();
		if (uninstallReceiver != null) {
			unregisterReceiver(uninstallReceiver);
		}
		if (installReceiver != null) {
			unregisterReceiver(installReceiver);
		}
	}

	@Override
	public void onClick(View v) {

		switch (v.getId()) {
		case R.id.ll_popu_delete:

			deleteApp();
			break;
		case R.id.ll_popu_share:

			shareApp();
			break;
		case R.id.ll_popu_startup:
			startupApp();
			break;
		case R.id.ll_popu_info:
			infoApp();
			break;
		}
		closePopupw();

	}

	private void infoApp() {
		/*
		 * <activity android:name=".applications.InstalledAppDetails"
		 * android:theme="@android:style/Theme.NoTitleBar"
		 * android:label="@string/application_info_label"> <intent-filter>
		 * <action android:name="android.settings.APPLICATION_DETAILS_SETTINGS"
		 * /> <category android:name="android.intent.category.DEFAULT" /> <data
		 * android:scheme="package" /> </intent-filter> </activity>
		 */
		Intent intent = new Intent(
				"android.settings.APPLICATION_DETAILS_SETTINGS");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + currentInfo.getPackageName()));
		startActivity(intent);
	}

	private void startupApp() {
		PackageManager pm = getPackageManager();
		Intent intent = pm.getLaunchIntentForPackage(currentInfo
				.getPackageName());
		if (intent != null) {
			startActivity(intent);
		} else {
			ToastUtils.showToast(this, "该应用无法启动！");
		}
	}

	private void shareApp() {
		Intent intent = new Intent("android.intent.action.SEND");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setType("text/plain");
		intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
		intent.putExtra(Intent.EXTRA_TEXT,
				"我有一个很好的应用推荐给你，名称是" + currentInfo.getAppName() + "，赶紧去下载吧!");
		startActivity(intent);
	}

	private void deleteApp() {
		/*
		 * <activity android:name=".UninstallerActivity"
		 * android:configChanges="orientation|keyboardHidden"
		 * android:theme="@style/TallTitleBarTheme"> <intent-filter> <action
		 * android:name="android.intent.action.VIEW" /> <action
		 * android:name="android.intent.action.DELETE" /> <category
		 * android:name="android.intent.category.DEFAULT" /> <data
		 * android:scheme="package" /> </intent-filter> </activity>
		 */
		Intent intent = new Intent("android.intent.action.DELETE");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.setData(Uri.parse("package:" + currentInfo.getPackageName()));
		startActivity(intent);

	}

	class UnistallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("删除应用！");
			if (currentInfo.isUserApp()) {
				listUser.remove(currentInfo);
			} else {
				listSys.remove(currentInfo);
			}
			myadaper.notifyDataSetChanged();
		}
	}

	class InstallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			System.out.println("安装应用程序");
			listSys.clear();
			listUser.clear();
			AppInfoUtils.getPackageInfo(context, listUser, listSys);
			myadaper.notifyDataSetChanged();
		}

	}

}
