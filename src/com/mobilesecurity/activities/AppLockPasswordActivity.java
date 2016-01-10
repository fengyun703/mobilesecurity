package com.mobilesecurity.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilesecurity.R;
import com.mobilesecurity.uitils.Md5Utils;
import com.mobilesecurity.uitils.ToastUtils;

public class AppLockPasswordActivity extends Activity {

	private EditText et_pwd;
	private ImageView iv_icon;
	private TextView tv_appname;
	private String packname;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_lockpassword);
		et_pwd = (EditText) findViewById(R.id.et_pswd);
		iv_icon = (ImageView) findViewById(R.id.iv_icon_pswd);
		tv_appname = (TextView) findViewById(R.id.iv_appname_pswd);

		Intent intent = getIntent();
		packname = intent.getStringExtra("packname");
		
		PackageManager pm = getPackageManager();
		try {
			PackageInfo info =pm.getPackageInfo(packname, 0);
			iv_icon.setImageDrawable(info.applicationInfo.loadIcon(pm));
			tv_appname.setText(info.applicationInfo.loadLabel(pm));
		} catch (NameNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	@Override
	public void onBackPressed() {
		//super.onBackPressed();
		Intent intent = new Intent();
		intent.setAction("android.intent.action.MAIN");
		intent.addCategory("android.intent.category.HOME");
		intent.addCategory("android.intent.category.DEFAULT");
		intent.addCategory("android.intent.category.MONKEY");
		startActivity(intent);
		//System.out.println("按回退键");
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		//System.out.println("onStop");
		finish();
		
	}

	public void confirm(View view) {
		String pwd = et_pwd.getText().toString().trim();
		if (TextUtils.isEmpty(pwd)) {
			ToastUtils.showToast(this, "密码不能为空");
		} else {
			// 发送解密广播
			String encode = Md5Utils.encode(pwd);
			String passwrod = getSharedPreferences("config", MODE_PRIVATE)
					.getString("password", null);
			if (encode.equals(passwrod)) {
				Intent intent = new Intent();
				intent.setAction("com.mobilesecurity.unlockapp");
				intent.putExtra("packname", packname);
				sendBroadcast(intent);
				finish();
			}else{
				ToastUtils.showToast(this, "密码错误！");
			}
		}
	}
}
