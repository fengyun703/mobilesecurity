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
	 * �����ز�ѯ
	 * @param view
	 */
	public void openquery(View view){
		StartActivityUtils.startActivity(this, AddressQueryActivity.class);
	}
	
	/**
	 * ���õ绰����
	 * @param view
	 */
	public void commonnum(View view){
		StartActivityUtils.startActivity(this, CommonNumQueryActivity.class);
	}
	
	/**
	 * ���ű��ر���
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
					ToastUtils.showToast(AdvancedActivity.this, "���ű��ݳɹ�");
				}else{
					ToastUtils.showToast(AdvancedActivity.this, "���ű���ʧ��");
				}
				dialog.dismiss();
				
			};
		}.start();
		
	}
	
	/**
	 * �ظ�
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
						ToastUtils.showToast(AdvancedActivity.this, "�ظ���"+count+"������!");
						
					}}))
				{
					ToastUtils.showToast(AdvancedActivity.this, "���Żظ����");
				}else{
					ToastUtils.showToast(AdvancedActivity.this, "���Żظ�ʧ��");
				}
				dialog.dismiss();
				
			};
		}.start();
	
	}
}
