package com.mobilesecurity.activities;

import com.mobilesecurity.R;
import com.mobilesecurity.uitils.StartActivityUtils;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;

public class AdvancedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.activity_advanced);
	}
	
	public void openquery(View view){
		StartActivityUtils.startActivity(this, AddressQueryActivity.class);
	}
	
	public void commonnum(View view){
		
	}
}
