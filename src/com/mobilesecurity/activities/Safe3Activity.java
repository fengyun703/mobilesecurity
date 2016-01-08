package com.mobilesecurity.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.mobilesecurity.R;
import com.mobilesecurity.uitils.StartActivityUtils;
import com.mobilesecurity.uitils.ToastUtils;

public class Safe3Activity extends BaseSafeAbsActivity {

	private EditText et_phone_safe3;
	private Button bt_showPhone_safe3;
	private SharedPreferences sp;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiy_safe3);
		et_phone_safe3 = (EditText) findViewById(R.id.et_phone_safe3);
		bt_showPhone_safe3 = (Button) findViewById(R.id.bt_showPhone_safe3);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		
		et_phone_safe3.setText( sp.getString("phone", null));
	

		bt_showPhone_safe3.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				StartActivityUtils.startActivityForResult(Safe3Activity.this,
						ShowPhoneActivity.class, 0);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (data != null) {
			String phone = data.getStringExtra("phone");
			System.out.println(phone);
			et_phone_safe3.setText(phone.replace("-",""));
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	@Override
	public void showNext() {
		String phone = et_phone_safe3.getText().toString().trim();
		// System.out.println("fdsafd".replace(new String(), "w"));
		//System.out.println(phone);
		if (TextUtils.isEmpty(phone)) {
			ToastUtils.showToast(this, "要开启手机防盗,必须有安全号码");
		} else {
			Editor ed = sp.edit();
			ed.putString("phone", phone);
			ed.commit();
			StartActivityUtils
					.startActivityAndFinish(this, Safe4Activity.class);
		}
	}

	@Override
	public void showPre() {
		StartActivityUtils.startActivityAndFinish(this, Safe2Activity.class);
	}

}
