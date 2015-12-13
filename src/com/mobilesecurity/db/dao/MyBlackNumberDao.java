package com.mobilesecurity.db.dao;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.mobilesecurity.db.MyBlackNumberHelper;
import com.mobilesecurity.db.bean.BlackNumberInfo;

public class MyBlackNumberDao {
	private MyBlackNumberHelper helper;

	public MyBlackNumberDao(Context context) {
		helper = new MyBlackNumberHelper(context);
	}

	public boolean add(String phone, String type) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("phone", phone);
		values.put("type", type);
		long i = db.insert("blackinfo", null, values);
		db.close();
		if (i != -1) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean update(String phone, String type){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("type", type);
		int i = db.update("blackinfo", values, "phnoe=?", new String[]{phone});
		db.close();
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean remove(String phone){
		SQLiteDatabase db = helper.getWritableDatabase();
		int i = db.delete("blackinfo", "phone = ?", new String[]{phone});
		db.close();
		if (i > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	public List<BlackNumberInfo>  query(){
		List<BlackNumberInfo> list = new LinkedList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("blackinfo", new String[]{"phone","type"}, null, null, null, null, "_id desc");
		while(cursor.moveToNext()){
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BlackNumberInfo data = new BlackNumberInfo();
			data.setPhone(cursor.getString(0));
			data.setType(cursor.getString(1));
			//System.out.println("电话 = "+data.getPhone()+", 类型 = "+ data.getType());
			list.add(data);
		}
		cursor.close();
		return list;
	}
	
	public String find(String phone){
		String mode = null;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("blackinfo", new String[]{"type"}, "phone = ?",new String[]{phone}, null,null,null);
		if(cursor.moveToFirst()){
			mode = cursor.getString(0);
		}
		cursor.close();
		return mode;
	}
	
	public  List<BlackNumberInfo>  queryPart(int startid, int maxcount){
		List<BlackNumberInfo> list = new LinkedList<BlackNumberInfo>();
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select _id, phone, type from blackinfo order by _id desc limit ? offset ?", new String[]{String.valueOf(maxcount),
				String.valueOf(startid)});
		while(cursor.moveToNext()){
			try {
				Thread.sleep(400);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			BlackNumberInfo data = new BlackNumberInfo();
			data.setPhone(cursor.getString(1));
			data.setType(cursor.getString(2));
			//System.out.println("_id  = "+ cursor.getString(0)+", 电话 = "+data.getPhone()+", 类型 = "+ data.getType());
			list.add(data);
		}
		cursor.close();
		return list;
		
	}
	
	public int getCount (){
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select count(*) from blackinfo", null);
		cursor.moveToFirst();
		int  count = Integer.parseInt(cursor.getString(0));
		cursor.close(); 
		return count;
	}
	
	

}
