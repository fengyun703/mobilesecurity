package com.mobilesecurity.receiver;

import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

public class MyAdminReceiver extends DeviceAdminReceiver {

	@Override
	public DevicePolicyManager getManager(Context context) {
		System.out.println("MyAdminReceiver    getManager");
		return super.getManager(context);
	}

	@Override
	public ComponentName getWho(Context context) {
		//System.out.println("MyAdminReceiver    getWho");
		return super.getWho(context);
	}

	@Override
	public void onEnabled(Context context, Intent intent) {
		//System.out.println("MyAdminReceiver    onEnabled");
		super.onEnabled(context, intent);
	}

	@Override
	public CharSequence onDisableRequested(Context context, Intent intent) {
		//System.out.println("MyAdminReceiver    onDisableRequested");
		return super.onDisableRequested(context, intent);
	}

	@Override
	public void onDisabled(Context context, Intent intent) {
		//System.out.println("MyAdminReceiver    onDisabled");
		super.onDisabled(context, intent);
	}

	@Override
	public void onPasswordChanged(Context context, Intent intent) {
	//	System.out.println("MyAdminReceiver    onPasswordChanged");
		super.onPasswordChanged(context, intent);
	}

	@Override
	public void onPasswordFailed(Context context, Intent intent) {
		//System.out.println("MyAdminReceiver    onPasswordFailed");
		super.onPasswordFailed(context, intent);
	}

	@Override
	public void onPasswordSucceeded(Context context, Intent intent) {
		System.out.println("MyAdminReceiver    onPasswordSucceeded");
		super.onPasswordSucceeded(context, intent);
	}

	@Override
	public void onPasswordExpiring(Context context, Intent intent) {
		System.out.println("MyAdminReceiver    onPasswordExpiring");
		super.onPasswordExpiring(context, intent);
	}

	@Override
	public void onReceive(Context context, Intent intent) {
		System.out.println("MyAdminReceiver    onReceive");
		super.onReceive(context, intent);
	}

}
