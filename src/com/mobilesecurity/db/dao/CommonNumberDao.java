package com.mobilesecurity.db.dao;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumberDao {

	
	public static String  getGroupNameByPosition(SQLiteDatabase db , int gposition){
		String gname ;
		Cursor cursor = db.query("classlist", new String[]{"name"}, "idx = ?", new String[]{String.valueOf(gposition+1)}, null, null, "idx asc");
		cursor.moveToNext();
		gname = cursor.getString(0);
		cursor.close();
		return gname;
	}
	
	public static String[] getChildNameByPosition(SQLiteDatabase db , int gposition, int cposition){
		String[] cname = new String[2] ;
		int newgposition = gposition+1;
		int newcpostion = cposition+1;
		Cursor cursor = db.query("table"+newgposition, new String[]{"name", "number"}, "_id = ?", new String[]{String.valueOf(newcpostion)}, null, null, "_id asc");
		cursor.moveToNext();
		cname[0] = cursor.getString(0);
		cname[1] = cursor.getString(1);
		//System.out.println("name = "+ cname[0]+",     number = "+ cname[1]);
		cursor.close();
		return cname;
	}
	
	public static int getGroupCount(SQLiteDatabase db){
		int count = 0;
		Cursor cursor = db.rawQuery("select count(*) from classlist", null);
		cursor.moveToNext();
		count = cursor.getInt(0);
		cursor.close();
		return count;
	}
	
	public static int getChildCount(SQLiteDatabase db, int gposition){
		int count = 0;
		int newgposition = gposition+1;
		Cursor cursor = db.rawQuery("select count(*) from  table"+newgposition, null);
		cursor.moveToNext();
		count = cursor.getInt(0);
		cursor.close();
		return count;
	}
}
