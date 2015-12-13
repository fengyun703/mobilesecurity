package com.mobilesecurity.uitils;

import android.app.Activity;
import android.widget.Toast;

public class ToastUtils {
	public static void showToast(final Activity context, final String text, final int duration){
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, text, duration).show();
			}
		});
	}
	
	public static void showToast(final Activity context, final String text){
		context.runOnUiThread(new Runnable() {
			@Override
			public void run() {
				Toast.makeText(context, text, 0).show();
			}
		});
	}

}
