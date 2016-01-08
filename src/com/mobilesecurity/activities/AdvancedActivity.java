package com.mobilesecurity.activities;

import com.mobilesecurity.R;
import com.mobilesecurity.engine.SmsTools;
import com.mobilesecurity.engine.SmsTools.SmsBackupCallBack;
import com.mobilesecurity.engine.SmsTools.SmsRecoveryCallBack;
import com.mobilesecurity.uitils.StartActivityUtils;
import com.mobilesecurity.uitils.ToastUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

public class AdvancedActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView( R.layout.activity_advanced);
	}
	/**
	 * 归属地查询
	 * @param view
	 */
	public void openquery(View view){
		StartActivityUtils.startActivity(this, AddressQueryActivity.class);
	}
	
	/**
	 * 常用电话功能
	 * @param view
	 */
	public void commonnum(View view){
		StartActivityUtils.startActivity(this, CommonNumQueryActivity.class);
	}
	
	/**
	 * 短信本地备份
	 * @param view
	 */
	public void smsbackup(View view){
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.show();
		new Thread(){
			public void run() {
				if(SmsTools.smsBackup(AdvancedActivity.this, "smsbackup.xml", new SmsBackupCallBack() {
					
					@Override
					public void onBackupProgress(int progress) {
						dialog.setProgress(progress);
					}
					
					@Override
					public void beforeBackup(int max) {
						dialog.setMax(max);
					}
				})){
					ToastUtils.showToast(AdvancedActivity.this, "短信备份成功");
				}else{
					ToastUtils.showToast(AdvancedActivity.this, "短信备份失败");
				}
				dialog.dismiss();
				
			};
		}.start();
		
	}
	
	/**
	 * 回复
	 * @param view
	 */
	public void smsrecovery(View view){
		final ProgressDialog dialog = new ProgressDialog(this);
		dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
		dialog.show();
		new Thread(){
			public void run() {
				
				if(SmsTools.smsrecovery(AdvancedActivity.this, "smsbackup.xml", new SmsRecoveryCallBack(){

					@Override
					public void beforeRecovery(int max) {
						dialog.setMax(max);
						
					}

					@Override
					public void onRecoveryProgress(int progress) {
						dialog.setProgress(progress);
						
					}

					@Override
					public void afterRecovery(int count) {
						ToastUtils.showToast(AdvancedActivity.this, "回复了"+count+"条短信!");
						
					}}))
				{
					ToastUtils.showToast(AdvancedActivity.this, "短信回复完成");
				}else{
					ToastUtils.showToast(AdvancedActivity.this, "短信回复失败");
				}
				dialog.dismiss();
				
			};
		}.start();
	
	}
}
