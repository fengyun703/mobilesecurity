package com.mobilesecurity.application;

import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		Thread.currentThread().setUncaughtExceptionHandler(new MyHander());
		super.onCreate();
	}
	
	private class MyHander implements UncaughtExceptionHandler{

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
		
			//ex.printStackTrace(err);
		}
		
	}
	
	
}
