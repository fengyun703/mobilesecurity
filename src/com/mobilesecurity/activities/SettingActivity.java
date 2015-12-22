package com.mobilesecurity.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import com.mobilesecurity.R;
import com.mobilesecurity.services.AddressQureyService;
import com.mobilesecurity.services.BlackNumberService;
import com.mobilesecurity.ui.SettingChangeView;
import com.mobilesecurity.ui.SettingItemView;
import com.mobilesecurity.uitils.ServiceStateUtils;

public class SettingActivity extends Activity {
	private SettingItemView siv_update;
	/*private SettingItemView siv_update1;
	private SettingItemView siv_update2;*/
	private SettingItemView siv_blacknumber;
	private SettingItemView siv_address_setting;
	private SettingChangeView scv_address_color;
	private SharedPreferences sp;
	private static final String[] items = new String[] { "��ɫ", "��ɫ", "��ɫ",
			"�ٻ�", "��ɫ" };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		sp = getSharedPreferences("config", Context.MODE_PRIVATE);
		siv_update = (SettingItemView) findViewById(R.id.siv_update_setting);
		/*siv_update1 = (SettingItemView) findViewById(R.id.siv_update_setting1);
		siv_update2 = (SettingItemView) findViewById(R.id.siv_update_setting2);*/
		siv_blacknumber = (SettingItemView) findViewById(R.id.siv_blacknumber_setting);
		siv_address_setting = (SettingItemView) findViewById(R.id.siv_address_setting);
		scv_address_color = (SettingChangeView) findViewById(R.id.scv_address_color);
		//siv_update.setChecked(sp.getBoolean("update", false));

		// System.out.println("SettingActivity: "+getContentResolver().toString());

		//scv_address_color.setResult(items[sp.getInt("color", 0)]);
		// System.out.println("color: "+sp.getInt("color", 0));

		siv_update.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor ed = sp.edit();
				if (siv_update.isChecked()) {
					// System.out.println("ȡ���Զ�����");
					siv_update.setChecked(false);
					ed.putBoolean("update", false);

				} else {
					// System.out.println("�����Զ�����");
					siv_update.setChecked(true);
					ed.putBoolean("update", true);
				}
				ed.commit();
			}
		});
		
		/*siv_update1.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor ed = sp.edit();
				if (siv_update1.isChecked()) {
					// System.out.println("ȡ���Զ�����");
					siv_update1.setChecked(false);
					ed.putBoolean("update1", false);

				} else {
					// System.out.println("�����Զ�����");
					siv_update1.setChecked(true);
					ed.putBoolean("update1", true);
				}
				ed.commit();
			}
		});
		siv_update2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Editor ed = sp.edit();
				if (siv_update2.isChecked()) {
					// System.out.println("ȡ���Զ�����");
					siv_update2.setChecked(false);
					ed.putBoolean("update2", false);

				} else {
					// System.out.println("�����Զ�����");
					siv_update2.setChecked(true);
					ed.putBoolean("update2", true);
				}
				ed.commit();
			}
		});*/

		// init(siv_blacknumber,
		// "com.mobilesecurity.services.BlackNumberService",
		// BlackNumberService.class);

		siv_blacknumber.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_blacknumber.isChecked()) {
					siv_blacknumber.setChecked(false);
					// System.out.println("�رշ���");
					Intent service = new Intent(SettingActivity.this,
							BlackNumberService.class);
					stopService(service);
				} else {
					siv_blacknumber.setChecked(true);
					// System.out.println("��������");
					Intent service = new Intent(SettingActivity.this,
							BlackNumberService.class);
					startService(service);
				}
			}
		});

		// init(siv_address_setting,
		// "com.mobilesecurity.services.AddressQureyService",
		// AddressQureyService.class);

		siv_address_setting.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (siv_address_setting.isChecked()) {
					siv_address_setting.setChecked(false);
					// System.out.println("�رշ���");
					Intent service = new Intent(SettingActivity.this,
							AddressQureyService.class);
					stopService(service);
				} else {
					siv_address_setting.setChecked(true);
					// System.out.println("��������");
					Intent service = new Intent(SettingActivity.this,
							AddressQureyService.class);
					startService(service);
				}
			}
		});
		
	}

	/*@Override
	protected void onStart() {
		System.out.println("onStart"+ siv_update.toString()+"  "+siv_address_setting.toString()+"  "+siv_blacknumber.toString()+"  "
				+siv_blacknumber.toString());
		super.onStart();
		if (ServiceStateUtils.isRunning(SettingActivity.this,
				"com.mobilesecurity.services.BlackNumberService")) {
			siv_blacknumber.setChecked(true);
			// System.out.println("�����Ѿ������ˣ�");
		} else {
			siv_blacknumber.setChecked(false);
			// System.out.println("�����Ѿ��ر��ˣ�");
		}

		if (ServiceStateUtils.isRunning(SettingActivity.this,
				"com.mobilesecurity.services.AddressQureyService")) {
			siv_address_setting.setChecked(true);
			// System.out.println("�����Ѿ������ˣ�");
		} else {
			siv_address_setting.setChecked(false);
			// System.out.println("�����Ѿ��ر��ˣ�");
		}

		siv_update.setChecked(sp.getBoolean("update", false));

		scv_address_color.setResult(items[sp.getInt("color", 0)]);
	}*/
	
	@Override
	protected void onResume() {
		super.onResume();
		if (ServiceStateUtils.isRunning(SettingActivity.this,
				"com.mobilesecurity.services.BlackNumberService")) {
			siv_blacknumber.setChecked(true);
			// System.out.println("�����Ѿ������ˣ�");
		} else {
			siv_blacknumber.setChecked(false);
			// System.out.println("�����Ѿ��ر��ˣ�");
		}

		if (ServiceStateUtils.isRunning(SettingActivity.this,
				"com.mobilesecurity.services.AddressQureyService")) {
			siv_address_setting.setChecked(true);
			// System.out.println("�����Ѿ������ˣ�");
		} else {
			siv_address_setting.setChecked(false);
			// System.out.println("�����Ѿ��ر��ˣ�");
		}

		siv_update.setChecked(sp.getBoolean("update", false));
		/*siv_update1.setChecked(sp.getBoolean("update1", false));
		siv_update2.setChecked(sp.getBoolean("update2", false));*/

		scv_address_color.setResult(items[sp.getInt("color", 0)]);
	}

	/*
	 * private void init(final SettingItemView siv, String className, final
	 * Class<?> cls) { if(ServiceStateUtils.isRunning(SettingActivity.this,
	 * className)){ siv.setChecked(true); //System.out.println("�����Ѿ������ˣ�");
	 * }else{ siv.setChecked(false); //System.out.println("�����Ѿ��ر��ˣ�"); }
	 * 
	 * siv.setOnClickListener(new OnClickListener() {
	 * 
	 * @Override public void onClick(View v) { if(siv.isChecked()){
	 * siv.setChecked(false); //System.out.println("�رշ���"); Intent service =
	 * new Intent(SettingActivity.this, cls); stopService(service); }else{
	 * siv.setChecked(true); //System.out.println("��������"); Intent service = new
	 * Intent(SettingActivity.this, cls); startService(service); } } });
	 * 
	 * }
	 */

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// System.out.println("onSaveInstanceState : " +
		// siv_blacknumber.toString() + "   "
		// + siv_update.toString());
		super.onSaveInstanceState(outState);
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		// System.out.println("onRestoreInstanceState : " +
		// siv_blacknumber.toString() + "   "
		// + siv_update.toString());
		super.onRestoreInstanceState(savedInstanceState);
	}

	public void setbackgroud(View view) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle("�����ر�����ɫ");

		// private static int[] icons = {R.drawable.call_locate_blue,
		// R.drawable.call_locate_gray, R.drawable.call_locate_green,
		// R.drawable.call_locate_orange, R.drawable.call_locate_white};
		builder.setSingleChoiceItems(items, sp.getInt("color", 1),
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Editor ed = sp.edit();
						ed.putInt("color", which);
						scv_address_color.setResult(items[which]);
						ed.commit();

					}
				});
		builder.setPositiveButton("ȷ��", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		builder.show();
	}

}
