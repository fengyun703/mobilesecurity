package com.mobilesecurity.db.dao;

import java.util.ArrayList;
import java.util.List;

import android.R.bool;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

import com.mobilesecurity.db.AppLockHepler;

public class AppLockDao {

	private AppLockHepler helper;
	private Context context;

	public AppLockDao(Context context) {
		helper = new AppLockHepler(context);
		this.context = context;
	}

	public boolean insert(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put("packname", packname);
		long id = db.insert("lockinfo", null, values);
		db.close();
		if(id!=-1){
			context.getContentResolver().notifyChange(
					Uri.parse("content://com.mobilesecurity.updateLockDb"),
					null);
			return true;
		}
		return  false;
	}

	public List<String> getAllPackName() {
		SQLiteDatabase db = helper.getWritableDatabase();
		List<String> list = new ArrayList<String>();
		Cursor cursor = db.query("lockinfo", new String[] { "packname" }, null,
				null, null, null, null);
		while (cursor.moveToNext()) {
			list.add(cursor.getString(0));
		}
		cursor.close();
		db.close();
		return list;
	}

	public boolean delete(String packname) {
		SQLiteDatabase db = helper.getWritableDatabase();
		int count = db.delete("lockinfo", "packname = ?",
				new String[] { packname });
		db.close();
		if (count > 0) {
			context.getContentResolver().notifyChange(
					Uri.parse("content://com.mobilesecurity.updateLockDb"),
					null);
			return true;
		}
		return  false;
	}

	public boolean find(String packname) {
		boolean result;
		SQLiteDatabase db = helper.getWritableDatabase();
		Cursor cursor = db.query("lockinfo", null, "packname = ?",
				new String[] { packname }, null, null, null);
		result = cursor.moveToNext();
		cursor.close();
		db.close();
		return result;
	}

}
