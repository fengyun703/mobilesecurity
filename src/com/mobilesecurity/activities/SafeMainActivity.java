package com.mobilesecurity.activities;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.mobilesecurity.R;
import com.mobilesecurity.uitils.StartActivityUtils;

public class SafeMainActivity extends Activity {

	private ImageView iv_protect_safemain;
	private SharedPreferences sp;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_safe_main);
		iv_protect_safemain = (ImageView) findViewById(R.id.iv_protect_safemain);
		sp = getSharedPreferences("config", MODE_PRIVATE);
		Boolean protect = sp.getBoolean("isprotecting", false);
		if(protect){
			iv_protect_safemain.setImageResource(R.drawable.lock);
		}else{
			iv_protect_safemain.setImageResource(R.drawable.unlock);
		}
		
	}
	
	public  void reEntrySet(View view){
		StartActivityUtils.startActivityAndFinish(this, Safe1Activity.class);
	}
}
