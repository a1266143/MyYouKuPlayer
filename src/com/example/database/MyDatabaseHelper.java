package com.example.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class MyDatabaseHelper extends SQLiteOpenHelper {

	//创建 “热门关键词” 的数据表
	final String CREATE_TABLE_SQL = "create table hot_words(WORD TEXT NOT NULL)";
	//创建搜索历史的表
	final String CREATE_TABLE_SQL2 = "create table history_search(ID INT PRIMARY KEY AUTOINCREMENT NOT NULL,HISTORY TEXT NOT NULL)";
	
	public MyDatabaseHelper(Context context, String name, int version) {
		super(context, name,null, version);
	}

	/**
	 * 初次创建数据表的时候会调用
	 */
	@Override
	public void onCreate(SQLiteDatabase db) {
		//第一次使用数据库时自动建表
		db.execSQL(CREATE_TABLE_SQL);
		db.execSQL(CREATE_TABLE_SQL2);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

	}

}
