package com.mobilesecurity.activities;

import com.mobilesecurity.R;
import com.mobilesecurity.uitils.StartActivityUtils;

import android.os.Bundle;

public class Safe1Activity extends BaseSafeAbsActivity {
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activiy_safe1);
	}

	@Override
	public void showNext() {

		StartActivityUtils.startActivityAndFinish(this, Safe2Activity.class);
	}

	@Override
	public void showPre() {

	}

}
