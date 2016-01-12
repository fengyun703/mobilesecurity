package com.mobilesecurity.db.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntiVirusDao {

	public static boolean find(String md5) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"/data/data/com.mobilesecurity/files/antivirus.db", null,
				SQLiteDatabase.OPEN_READONLY);
		Cursor cursor = db.query("datable", null, "md5=?",
				new String[] { md5 }, null, null, null);
		boolean result = cursor.moveToNext();
		cursor.close();
		db.close();
		return result;
	}

	public static boolean insert(String md5, int type, String desc, String name) {
		SQLiteDatabase db = SQLiteDatabase.openDatabase(
				"/data/data/com.mobilesecurity/files/antivirus.db", null,
				SQLiteDatabase.OPEN_READWRITE);
		ContentValues values = new ContentValues();
		values.put("md5", md5);
		values.put("type", type);
		values.put("desc", desc);
		values.put("name", name);
		long i = db.insert("datable", null, values);
		db.close();
		System.out.println(i);
		return i == -1 ? false : true;
	}

}
