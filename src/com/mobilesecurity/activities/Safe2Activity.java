package com.mobilesecurity.activities;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.view.View;
import android.view.View.OnClickListener;

import com.mobilesecurity.R;
import com.mobilesecurity.ui.SettingItemView;
import com.mobilesecurity.uitils.StartActivityUtils;
import com.mobilesecurity.uitils.ToastUtils;

public class Safe2Activity extends BaseSafeAbsActivity {
	private SettingItemView sim_safe2_bind;
	private SharedPreferences sp;
	private TelephonyManager tm;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiy_safe2);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		sim_safe2_bind = (SettingItemView) findViewById(R.id.sim_safe2_bind);

		if (sp.getString("sim", null) == null) {
			sim_safe2_bind.setChecked(false);
		} else {
			sim_safe2_bind.setChecked(true);
		}

		sim_safe2_bind.setOnClickListener(new OnClickListener() {

			Editor ed = sp.edit();

			@Override
			public void onClick(View v) {
				if (sim_safe2_bind.isChecked()) {
					sim_safe2_bind.setChecked(false);
					ed.putString("sim", null);

				} else {
					sim_safe2_bind.setChecked(true);
					ed.putString("sim", tm.getSimSerialNumber());
				}

				ed.commit();
			}

		});
	}

	@Override
	public void showNext() {
		if (sim_safe2_bind.isChecked()) {
			StartActivityUtils
					.startActivityAndFinish(this, Safe3Activity.class);
		}else{
			ToastUtils.showToast(this, "要开启手机防盗,必须绑定sim卡串号");
		}

	}

	@Override
	public void showPre() {
		StartActivityUtils.startActivityAndFinish(this, Safe1Activity.class);
	}

}
