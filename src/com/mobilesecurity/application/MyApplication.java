package com.mobilesecurity.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Environment;
import android.os.Process;

public class MyApplication extends Application {

	@Override
	public void onCreate() {
		Thread.currentThread().setUncaughtExceptionHandler(new MyHander());
		super.onCreate();
	}
	
	private class MyHander implements UncaughtExceptionHandler{

		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
		
			System.out.println("²¶×½µ½Òì³£");
			StringWriter sw = new StringWriter();
			PrintWriter pw = new PrintWriter(sw);
			ex.printStackTrace(pw);
			String errstr = sw.toString();
			File file = new File(Environment.getExternalStorageDirectory(),"mobilelog.txt");
			try {
				FileOutputStream fout = new FileOutputStream(file);
				fout.write(errstr.getBytes("utf-8"));
				fout.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			Process.killProcess(Process.myPid());
		}
		
	}
	
	
}
