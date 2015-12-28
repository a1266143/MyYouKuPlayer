package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

	//创建 “大家都在搜” 的数据表
	final String CREATE_TABLE_SQL = "create table everyone_search(WORD TEXT NOT NULL)";
	
	public MyDatabaseHelper(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
	}

	/**
	 * 初次创建数据表的时候会调用
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//第一次使用数据库时自动建表
		db.execSQL(CREATE_TABLE_SQL);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
