package com.mobilesecurity.activities;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import com.mobilesecurity.R;
import com.mobilesecurity.db.bean.LockInfo;
import com.mobilesecurity.db.dao.AppLockDao;
import com.mobilesecurity.fragment.ShowLockFragment;
import com.mobilesecurity.fragment.ShowNoLockFragment;
import com.mobilesecurity.myinterface.ApplockInerface;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AppLockActivity extends Activity implements ApplockInerface{

	private LinearLayout ll_container;
	private LinearLayout ll_loading;
	private TextView tv_shownolock;
	private TextView tv_showlock;
	private ShowLockFragment lockFragment;
	private ShowNoLockFragment noLockFragment;
	private FragmentManager fm;
	private List<LockInfo> lockInfos;
	private List<LockInfo> nolockInfos;
	private PackageManager pm ;
	private AppLockDao dao;
	private boolean showLockFlag;
	
	private Handler handler = new Handler(){
		public void dispatchMessage(android.os.Message msg) {
			ll_loading.setVisibility(View.INVISIBLE);
			FragmentTransaction ftc =  fm.beginTransaction();
			ftc.replace(R.id.ll_container, noLockFragment);
			noLockFragment.setnoInfos(nolockInfos);
			lockFragment.setInfos(lockInfos);
			ftc.commit();
		};
	}; 

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//System.out.println("AppLockActivity onStart");
		setContentView(R.layout.activity_applock);
		ll_container = (LinearLayout) findViewById(R.id.ll_container);
		ll_loading = (LinearLayout) findViewById(R.id.ll_loading);
		tv_showlock = (TextView) findViewById(R.id.tv_showlock);
		tv_shownolock = (TextView) findViewById(R.id.tv_shownolock);
		fm = getFragmentManager();
		pm = getPackageManager();
		dao = new AppLockDao(this);
		noLockFragment = new ShowNoLockFragment();
		lockFragment = new ShowLockFragment();
		showLockFlag = false;
		lockInfos = new ArrayList<LockInfo>();
		nolockInfos = new ArrayList<LockInfo>();
		initData();
		
	}
	
	private void initData() {
		ll_loading.setVisibility(View.VISIBLE);
		new Thread(){
			public void run() {
				List<PackageInfo> packinfos = pm.getInstalledPackages(0);
				for(PackageInfo pinfo : packinfos){
					String appname = pinfo.applicationInfo.loadLabel(pm).toString();
					LockInfo info = new LockInfo();
					if(dao.find(pinfo.packageName)){
						info.setAppName(appname);
						info.setIcon(pinfo.applicationInfo.loadIcon(pm));
						info.setPackageName(pinfo.packageName);
						lockInfos.add(info);
					}else{
						info.setAppName(appname);
						info.setIcon(pinfo.applicationInfo.loadIcon(pm));
						info.setPackageName(pinfo.packageName);
						nolockInfos.add(info);
					}
				}
				handler.sendEmptyMessage(0);
			};
		}.start(); 
	}

	/**
	 * 相应点击textview事件，显示未加锁程序
	 * @param view
	 */
	public void shownolock(View view) {
		if(showLockFlag){
			showLockFlag = false;
			tv_shownolock.setBackgroundResource(R.drawable.tab_left_pressed);
			tv_showlock.setBackgroundResource(R.drawable.tab_right_default);
			FragmentTransaction ftc =  fm.beginTransaction();
			ftc.replace(R.id.ll_container, noLockFragment);
			ftc.commit();
		}
	}

	/**
	 * 相应点击textview事件，显示加锁程序
	 * @param view
	 */
	public void showlock(View view) {
		if(!showLockFlag){
			showLockFlag = true;
			tv_shownolock.setBackgroundResource(R.drawable.tab_left_default);
			tv_showlock.setBackgroundResource(R.drawable.tab_right_pressed);
			FragmentTransaction ftc =  fm.beginTransaction();
			ftc.replace(R.id.ll_container, lockFragment);
			ftc.commit();
		}
	}
	
	//由fragment中调用，告诉activity更新要显示的数据。
	@Override
	public void addLock(LockInfo info) {
		lockInfos.add(0, info);
		dao.insert(info.getPackageName());
	}
	
	//由fragment中调用，告诉activity更新要显示的数据。
	@Override
	public void deleteLock(LockInfo info) {
		nolockInfos.add(0, info);
		dao.delete(info.getPackageName());
	}

	

	/*@Override
	protected void onStart() {
		System.out.println("AppLockActivity onStart");
		super.onStart();
	}

	@Override
	protected void onRestart() {
		System.out.println("AppLockActivity onRestart");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		System.out.println("AppLockActivity onResume");
		super.onResume();
	}

	@Override
	protected void onPause() {
		System.out.println("AppLockActivity onPause");
		super.onPause();
	}

	@Override
	protected void onStop() {
		System.out.println("AppLockActivity onStop");
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		System.out.println("AppLockActivity onDestroy");
		super.onDestroy();
	}

	
	*/

	

	/*
	 * 把初始化的list传个给两个fragment（setArguments），在frament中getArguments， 使用newInstance
	 * 
	 * static MyDialogFragment newInstance(int num) { MyDialogFragment f = new
	 * MyDialogFragment();
	 * 
	 * // Supply num input as an argument. Bundle args = new Bundle();
	 * args.putInt("num", num); f.setArguments(args);
	 * 
	 * return f; } 这样获取参数
	 * 
	 * @Override public void onCreate(Bundle savedInstanceState) {
	 * super.onCreate(savedInstanceState); mNum = getArguments().getInt("num");
	 * ... }
	 */

}
