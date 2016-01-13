package com.mobilesecurity.activities;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.Intent.ShortcutIconResource;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.view.animation.AlphaAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.mobilesecurity.R;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.mobilesecurity.uitils.AppInfoUtils;
import com.mobilesecurity.uitils.StartActivityUtils;
import com.mobilesecurity.uitils.StreamUtils;
import com.mobilesecurity.uitils.ToastUtils;

public class SplashActivity extends Activity {
	protected static final int UPDATE = 1;
	private TextView tv_splash_version;
	private TextView tv_splash_upload;
	private SharedPreferences sp;
	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case UPDATE:
				showUpdateDialog((JsonInfo) msg.obj);
				break;
			}
		}
	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tv_splash_version = (TextView) findViewById(R.id.tv_splash_version);
		RelativeLayout splash_backgroud = (RelativeLayout) findViewById(R.id.splash_backgroud);
		tv_splash_upload = (TextView) findViewById(R.id.tv_splash_upload);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		tv_splash_version.setText(AppInfoUtils.getVersionName(this));
		AlphaAnimation am = new AlphaAnimation(0.3f, 1.0f);
		am.setDuration(2000);

		splash_backgroud.startAnimation(am);
		// System.out.println(sp.getBoolean("update", false));
		if (!sp.getBoolean("update", false)) {
			// System.out.println("�Զ�����������");
			StartActivityUtils.startActivityForDelayFinish(this,
					HomeActivity.class, 2000);
		} else {
			checkVersion();
			// System.out.println("��ʼ����Զ�����");
		}

		copyDb("address.db");
		copyDb("commonnum.db");
		copyDb("antivirus.db");
		creatShortCut();
	}

	private void creatShortCut() {
		/*
		 * <receiver
		 * android:name="com.android.launcher2.InstallShortcutReceiver"
		 * android:permission
		 * ="com.android.launcher.permission.INSTALL_SHORTCUT"> <intent-filter>
		 * <action android:name="com.android.launcher.action.INSTALL_SHORTCUT"
		 * /> </intent-filter> </receiver>
		 */
		//flag ��ֹ�ظ�������ݷ�ʽ
		boolean flag = sp.getBoolean("shortcut", false);
		//System.out.println(flag);
		if (!flag) {
			Intent intent = new Intent();
			// ����intent��action
			intent.setAction("com.android.launcher.action.INSTALL_SHORTCUT");
			
			// ���ÿ�ݷ�ʽ����
			intent.putExtra(Intent.EXTRA_SHORTCUT_NAME, "��ȫ��ʿ");
			// ���õ����ݷ�ʽ�Ķ���
			Intent actionIntent = new Intent();
			 actionIntent.setAction("com.mobilesecurity.shortcut");
			//actionIntent.setClass(getApplicationContext(), SplashActivity.class);
			actionIntent.addCategory("android.intent.category.DEFAULT");
			 //actionIntent.addCategory("android.intent.category.LAUNCHER");
			intent.putExtra(Intent.EXTRA_SHORTCUT_INTENT, actionIntent);
			
			
			// ���ÿ�ݷ�ʽͼƬ
			ShortcutIconResource value = Intent.ShortcutIconResource
					.fromContext(getApplicationContext(), R.drawable.launcher);
			intent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, value);
			sendBroadcast(intent);
			sp.edit().putBoolean("shortcut", true).commit();
		}
	}

	/**
	 * �������ݿ��ļ���filesĿ¼
	 */
	public void copyDb(String filename) {
		File file = new File(getFilesDir(), filename);
		if (!file.exists()) {
			try {
				InputStream in = getAssets().open(filename);
				OutputStream out = new FileOutputStream(file);
				byte[] bs = new byte[1024];
				int len = 0;
				while ((len = in.read(bs)) != -1) {
					out.write(bs, 0, len);
				}
				in.close();
				out.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * ���������Ƿ������°汾���о���ʾ���¡�
	 */
	private void checkVersion() {
		new Thread() {
			public void run() {
				try {
					URL url = new URL(getString(R.string.Urlpath));
					HttpURLConnection conn = (HttpURLConnection) url
							.openConnection();
					conn.setRequestMethod("GET");
					conn.setReadTimeout(3000);
					conn.setConnectTimeout(3000);
					int code = conn.getResponseCode();
					if (200 == code) {
						InputStream in = conn.getInputStream();
						String json = StreamUtils.getStringFromInput(in);

						JSONObject jobj = new JSONObject(json);
						JsonInfo info = new JsonInfo();
						info.desc = jobj.getString("desc");
						info.url = jobj.getString("downloadurl");
						info.versionCode = jobj.getInt("version");
						if (info.versionCode > AppInfoUtils
								.getVersionCode(SplashActivity.this)) {
							Message msg = Message.obtain();
							msg.what = UPDATE;
							msg.obj = info;
							handler.sendMessageDelayed(msg, 2000);
						} else {
							// System.out.println("û�����°汾");
							StartActivityUtils.startActivityForDelayFinish(
									SplashActivity.this, HomeActivity.class,
									2000);

						}
						/*
						 * System.out.println(info.desc);
						 * System.out.println(info.url);
						 * System.out.println(info.versionCode);
						 */
					}

				} catch (MalformedURLException e) {
					e.printStackTrace();
					ToastUtils.showToast(SplashActivity.this,
							"url·�����Ϸ�������ţ�301");
				} catch (IOException e) {
					e.printStackTrace();
					ToastUtils.showToast(SplashActivity.this, "�������Ӵ��󣬴���ţ�501");
					StartActivityUtils.startActivityForDelayFinish(
							SplashActivity.this, HomeActivity.class, 2000);
				} catch (JSONException e) {
					e.printStackTrace();
					ToastUtils.showToast(SplashActivity.this,
							"JSON�ļ����󣬴���ţ�601");
				}
			};

		}.start();
	}

	class JsonInfo {
		String url;
		String desc;
		int versionCode;
	}

	private void showUpdateDialog(final JsonInfo info) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("��������").setMessage(info.desc)
				.setPositiveButton("����", new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						HttpUtils http = new HttpUtils();
						final File file = new File(Environment
								.getExternalStorageDirectory(),
								"mobileSecurity.apk");
						http.download(info.url, file.getAbsolutePath(),
								new RequestCallBack<File>() {

									@Override
									public void onSuccess(
											ResponseInfo<File> arg0) {
										ToastUtils.showToast(
												SplashActivity.this, "���سɹ�");
										// �滻��װapk
										// <intent-filter>
										// <action
										// android:name="android.intent.action.VIEW"
										// />
										// <category
										// android:name="android.intent.category.DEFAULT"
										// />
										// <data android:scheme="content" />
										// <data android:scheme="file" />
										// <data
										// android:mimeType="application/vnd.android.package-archive"
										// />
										// </intent-filter>

										Intent intent = new Intent(
												"android.intent.action.VIEW");
										intent.addCategory("android.intent.category.DEFAULT");
										intent.setDataAndType(
												Uri.fromFile(file),
												"application/vnd.android.package-archive");
										startActivity(intent);
									}

									@Override
									public void onFailure(HttpException arg0,
											String arg1) {
										ToastUtils.showToast(
												SplashActivity.this, "���ش���");
									}

									@Override
									public void onLoading(long total,
											long current, boolean isUploading) {
										tv_splash_upload.setText(current + "/"
												+ total);
									};
								});
					}
				});

		builder.setNegativeButton("�´�������", new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				StartActivityUtils.startActivityAndFinish(SplashActivity.this,
						HomeActivity.class);
			}
		});
		builder.show();
	}
}
