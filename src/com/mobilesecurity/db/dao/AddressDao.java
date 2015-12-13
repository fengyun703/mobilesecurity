package com.mobilesecurity.db.dao;

import com.lidroid.xutils.util.LogUtils.CustomLogger;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.view.WindowManager;

public class AddressDao {

	public static String getAddress(String number){
		//String path = context.getFilesDir().getAbsolutePath()+"/address.db";
		//System.out.println(path);
		SQLiteDatabase db = SQLiteDatabase.openDatabase("/data/data/com.mobilesecurity/files/address.db", null, SQLiteDatabase.OPEN_READONLY);
		String pattern = "^1[358]\\d{9}$";
		String result = number;
		if(number.matches(pattern)){
			//是手机号码
			//System.out.println("是手机号码");
		  Cursor cursor = db.rawQuery("select location  from data2 where id =(select outkey from data1 where id = ?) ", 
					new String[]{(String) number.subSequence(0, 7)});
		  if(cursor.moveToFirst()){
			  result = cursor.getString(0);
		  }
		  cursor.close();
		}else{
			switch (number.length()) {
			case 3:
				if("110".equals(number)){
					result= "匪警";
				}else if("120".equals(number)){
					result= "急救";
				}else if("119".equals(number)){
					result= "火警";
				}
				break;
			case 4:
				result = "模拟器";
				break;
			case 8:
				result = "本地电话";
				break;
			default:
				if(number.length()>=10 && number.startsWith("0")){
					 Cursor cursor = db.rawQuery("select location  from data2 where area = ?",new String[]{(String) number.substring(1, 3)}); 
					 if(cursor.moveToFirst()){
						 result = cursor.getString(0).substring(0,  cursor.getString(0).length()-2);
					 }
					 cursor.close();
					 
					  cursor = db.rawQuery("select location  from data2 where area = ?",new String[]{(String) number.substring(1, 4)}); 
					 if(cursor.moveToFirst()){
						 result = cursor.getString(0).substring(0,  cursor.getString(0).length()-2);
					 }
					 cursor.close();
				}
				break;
			}
		}
		
		db.close();
		return result;
		
	}
}
