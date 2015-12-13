package com.mobilesecurity.uitils;

import android.app.Activity;
import android.content.Intent;

public class StartActivityUtils {

	public static void startActivityForDelay(final Activity context, final Class<?> cls,final long delay) {
		new Thread(){
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Intent intent = new Intent(context, cls);
				context.startActivity(intent);
			}
		}.start();
	}
	
	public static void startActivityForDelayFinish(final Activity context, final Class<?> cls,final long delay) {
		new Thread(){
			public void run() {
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				Intent intent = new Intent(context, cls);
				context.startActivity(intent);
				context.finish();
			}
		}.start();
	}
	
	public static void startActivity(Activity context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
	}

	public static void startActivityAndFinish(Activity context, Class<?> cls) {
		Intent intent = new Intent(context, cls);
		context.startActivity(intent);
		context.finish();
	}
	
	public static void startActivityForResult(Activity context, Class<?> cls, int requestCode) {
		Intent intent = new Intent(context, cls);
		context.startActivityForResult(intent, requestCode);
	}
}
