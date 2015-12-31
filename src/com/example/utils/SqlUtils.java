package com.example.utils;

import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.util.Log;

import com.example.database.MyDatabaseHelper;

/**
 * 此类主要是存储数据到数据库
 * @author 李晓军
 *
 */
public class SqlUtils {
	
	private static final SqlUtils utils = new SqlUtils();
	private SqlUtils(){};
	public static SqlUtils getInstance(){
		return utils;
	}
	
	private MyDatabaseHelper dbhelper;
	private SQLiteDatabase db;
	
	//清空hot_words数据表的sql语句
	private static final String SQL_HOTWORDS_CLEAR = "delete from hot_words";
	//插入hot_words数据表的sql语句
	private static final String SQL_HOTWORDS_INSERT = "insert into hot_words values(?)";
	//查询hot_words表
	private static final String SQL_HOTWORDS_QUERY = "select * from hot_words";
	//插入history表
	private static final String SQL_HISTORY_INSERT = "insert into history_search values(?)";
	//降序查询history表
	private static final String SQL_HISTORY_QUERY = "select * from history_search order by id asc";
	/**
	 * 存储热门关键词到数据库
	 * @return
	 */
	public boolean saveHotWords2db(Context context,Handler handler,ArrayList<String> arr){
		try{
			//创建dbhelper对象,指定数据库是MyYouKuPlayerDB，版本为1
			dbhelper = new MyDatabaseHelper(context, "MyYouKuPlayerDB.db3", 1);
			//以写的方式打开数据库
			db = dbhelper.getWritableDatabase();
			//插入数据之前需要清除数据
			db.execSQL(SQL_HOTWORDS_CLEAR);
			//根据传入的arraylist插入数据
			for(int i=0;i<arr.size();i++){
				//执行带占位符的sql语句
				db.execSQL(SQL_HOTWORDS_INSERT,new String[]{arr.get(i)});
			}
			//关闭数据库
			closeDB();
			return true;
		}catch(Exception e){
			//如果接收到异常，比如插入异常，返回false
			handler.sendEmptyMessage(StaticCode.MISTAKE_SQL);
			closeDB();
			return false;
		}
		
	}
	
	/**
	 * 查询热门关键词
	 */
	public ArrayList<String> queryHotWords(Handler handler,Context context){
		try {
			ArrayList<String> arr = new ArrayList<String>();
			//创建dbhelper对象,指定数据库是MyYouKuPlayerDB，版本为1
			dbhelper = new MyDatabaseHelper(context, "MyYouKuPlayerDB.db3", 1);
			//以写的方式打开数据库
			db = dbhelper.getWritableDatabase();
			//查询并返回数据
			Cursor  cursor = db.rawQuery(SQL_HOTWORDS_QUERY, null);
			//取出数据库中所有的值并包装成
			while(cursor.moveToNext()){
				//获取指针中第一列的值并存入ArrayList中
				arr.add(cursor.getString(0));
			}
			//关闭数据库和
			closeDB();
			if(arr.size() == 0)
				return null;
			return arr;
		} catch (Exception e) {
			handler.sendEmptyMessage(StaticCode.MISTAKE_SQL);
			closeDB();
			return null;
		}
	}
	
	/**
	 * 保存历史纪录
	 * @param context
	 * @param input 输入的关键字
	 * @param handler
	 * @return 存储成功返回true，相反返回false
	 */
	public boolean saveHistory2db(Context context,String input,Handler handler){
		try {
			//创建dbhelper对象,指定数据库是MyYouKuPlayerDB，版本为1
			dbhelper = new MyDatabaseHelper(context, "MyYouKuPlayerDB.db3", 1);
			//以写的方式打开数据库
			db = dbhelper.getWritableDatabase();
			//执行带占位符的sql语句
			db.execSQL(SQL_HISTORY_INSERT,new String[]{input});
			//关闭数据库
			closeDB();
			return true;
		} catch (Exception e) {
			handler.sendEmptyMessage(StaticCode.MISTAKE_SQL);
			closeDB();
			return false;
		}
	}
	
	/**
	 * 查询历史
	 * @param context
	 * @param handler
	 * @return
	 */
	public ArrayList<String> queryHistory(Context context,Handler handler){
		ArrayList<String> arr = new ArrayList<String>();
		try {
			//创建dbhelper对象,指定数据库是MyYouKuPlayerDB，版本为1
			dbhelper = new MyDatabaseHelper(context, "MyYouKuPlayerDB.db3", 1);
			//以写的方式打开数据库
			db = dbhelper.getWritableDatabase();
			//执行带占位符的sql语句
			Cursor cursor = db.rawQuery(SQL_HISTORY_QUERY, null);
			while(cursor.moveToNext()){
				arr.add(cursor.getString(0));
			}
			if(arr.size() == 0)
				return null;
			return arr;
		} catch (Exception e) {
			handler.sendEmptyMessage(StaticCode.MISTAKE_SQL);
			return null;
		}
	}
	
	/**
	 * 关闭数据库的方法
	 */
	public void closeDB(){
		if(dbhelper!=null)
			dbhelper.close();
		if(db!=null)
			db.close();
	}
}
