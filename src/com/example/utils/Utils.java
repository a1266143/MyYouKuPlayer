package com.example.utils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

public class Utils {
	private static final Utils utils = new Utils();
	private Utils(){};
	public static Utils getInstance(){
		return utils;
	}
	
	@SuppressWarnings("deprecation")
	public void showQuitDialog(final Activity activity){
		AlertDialog ad = new AlertDialog.Builder(activity).create();
		ad.setMessage("确定退出吗？");
		ad.setButton2("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				activity.finish();
			}
		});
		ad.setButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface arg0, int arg1) {
				return;
			}
		});
		ad.show();
	}
}
