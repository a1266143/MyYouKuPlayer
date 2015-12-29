package com.example.myyoukuplayer;

import com.example.utils.NetForJsonUtils;
import com.example.utils.StaticCode;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends Activity {

	private TextView splash_welcome;
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//如果网络连接错误
			if(msg.what == StaticCode.MISTAKE_NET){
				Toast.makeText(SplashActivity.this, "网络连接错误", Toast.LENGTH_SHORT).show();
				splash_welcome.setText("网络连接错误，正在进入主界面...");
				//延迟2秒进入主界面
				this.postDelayed(new DelayRunnable(), 2000);
			}else if(msg.what == StaticCode.MISTAKE_SQL){
				Toast.makeText(SplashActivity.this, "数据库操作出现错误,请联系程序猿", Toast.LENGTH_SHORT).show();
				splash_welcome.setText("网络连接错误，正在进入主界面...");
				//延迟2秒再进入主页面
				this.postDelayed(new DelayRunnable(), 2000);
			}else if(msg.what == StaticCode.TIP_GETCOMPLETE){
				splash_welcome.setText("热门关键词获取完成");
			}else if(msg.what == StaticCode.TIP_SUCCESSSQL){
				splash_welcome.setText("存入数据库完成，正在进入主页面...");
				//延迟2秒进入主界面
				this.postDelayed(new DelayRunnable(), 2000);
			}
		}
		
	};
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		splash_welcome = (TextView) findViewById(R.id.splash_welcom);
		//进行联网操作获取热门关键词排名
		NetForJsonUtils.getInstance().getHotWords(this,handler);
	}
	
	//延迟Runnable
	class DelayRunnable implements Runnable{

		@Override
		public void run() {
			Intent intent =new Intent(SplashActivity.this,MainActivity.class);
			startActivity(intent);
			finish();
		}
		
	}

}
