package com.mobilesecurity.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyBlackNumberHelper extends SQLiteOpenHelper {

	public MyBlackNumberHelper(Context context) {
		super(context, "blackNumber.db", null, 1);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table blackinfo (_id integer primary key autoincrement, phone varchar(20), type varchar(2))");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
