package com.mobilesecurity.activities;

import android.app.Activity;
import android.os.Bundle;
import android.os.Vibrator;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mobilesecurity.R;
import com.mobilesecurity.db.dao.AddressDao;

public class AddressQueryActivity extends Activity {

	private EditText et_number;
	private TextView tv_address;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_query_address);
		et_number = (EditText) findViewById(R.id.et_number_address);
		tv_address = (TextView) findViewById(R.id.tv_result_address);
	}

	public void query(View view) {
		String number = et_number.getText().toString().trim();
		String pattern = "\\d+";
		if (TextUtils.isEmpty(number)) {
			Animation aa = new TranslateAnimation(0, 7, 0, 0);
			aa.setDuration(200);
			aa.setRepeatCount(4);
			et_number.startAnimation(aa);
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(200);
			Toast.makeText(this, "电话号码不能为空", 0).show();
		} else if (number.matches(pattern)) {
			String address = AddressDao.getAddress(number);
			tv_address.setText(address);
		}
	}
}
