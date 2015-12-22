package com.mobilesecurity.activities;

import com.mobilesecurity.R;
import com.mobilesecurity.services.AutoKillService;
import com.mobilesecurity.ui.SettingItemView;
import com.mobilesecurity.uitils.ServiceStateUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

public class ProcessSettingActivity extends Activity {

	private SettingItemView siv_show_sys;
	private SettingItemView siv_autoclear;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_process_setting);
		siv_show_sys = (SettingItemView) findViewById(R.id.siv_show_sysprocess);
		siv_autoclear = (SettingItemView) findViewById(R.id.siv_autoclearprocess);
		sp = getSharedPreferences("config", MODE_PRIVATE);

		siv_show_sys.setChecked(sp.getBoolean("showsys", true));
		siv_show_sys.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor et = sp.edit();

				if (siv_show_sys.isChecked()) {
					siv_show_sys.setChecked(false);
					et.putBoolean("showsys", false);
				} else {
					siv_show_sys.setChecked(true);
					et.putBoolean("showsys", true);
				}
				et.commit();
			}
		});

		siv_autoclear.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				if (siv_autoclear.isChecked()) {
					siv_autoclear.setChecked(false);
					Intent intent = new Intent(ProcessSettingActivity.this, AutoKillService.class);
					stopService(intent);
				} else {
					siv_autoclear.setChecked(true);
					Intent intent = new Intent(ProcessSettingActivity.this, AutoKillService.class);
					startService(intent);
				}
			}
		});
	}

	@Override
	protected void onResume() {
		super.onResume();
		siv_autoclear.setChecked(ServiceStateUtils.isRunning(this,
				"com.mobilesecurity.services.AutoKillService"));
		

	}
}
