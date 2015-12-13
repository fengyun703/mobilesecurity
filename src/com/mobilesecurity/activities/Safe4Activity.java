package com.mobilesecurity.activities;

import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobilesecurity.R;
import com.mobilesecurity.receiver.MyAdminReceiver;
import com.mobilesecurity.ui.SettingItemView;
import com.mobilesecurity.uitils.StartActivityUtils;
import com.mobilesecurity.uitils.ToastUtils;

public class Safe4Activity extends BaseSafeAbsActivity {
	private SettingItemView siv_openSafe;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiy_safe4);
		siv_openSafe = (SettingItemView) findViewById(R.id.sim_opensafe_safe4);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		siv_openSafe.setChecked(sp.getBoolean("isprotecting", false));
		siv_openSafe.setOnClickListener(new OnClickListener() {
			Editor ed = sp.edit();
			@Override
			public void onClick(View v) {
				if (siv_openSafe.isChecked()) {
					siv_openSafe.setChecked(false);
					ed.putBoolean("isprotecting", false);
				} else {
					siv_openSafe.setChecked(true);
					ed.putBoolean("isprotecting", true);
				}
				
				ed.commit();
			}
		});
	}

	@Override
	public void showNext() {
		// 设置完成
		Editor ed = sp.edit();
		ed.putBoolean("finishsetup", true);
		ed.commit();
		StartActivityUtils.startActivityAndFinish(this, SafeMainActivity.class);
	}
	
	public void openPermission(View view){
		DevicePolicyManager dpm = (DevicePolicyManager) getSystemService(DEVICE_POLICY_SERVICE);
		
		
		ComponentName who = new ComponentName(this, MyAdminReceiver.class);
		if(!dpm.isAdminActive(who)){
			System.out.println("开启设备超级管理权限");
		Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
        intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, who);
        intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION,
               "只有开启设置超级管理权限，才能更好的使用手机卫士！");
        startActivity(intent);
		}else{
			ToastUtils.showToast(this, "已经开启设备超级管理权限！");
		}

	}

	@Override
	public void showPre() {
		StartActivityUtils.startActivityAndFinish(this, Safe3Activity.class);
	}

}
