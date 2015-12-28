package com.example.myyoukuplayer;

import com.youku.player.YoukuPlayerBaseConfiguration;

import android.app.Activity;
import android.app.Application;

public class MyApplication extends Application {
	public static YoukuPlayerBaseConfiguration configuration;
	@Override
	public void onCreate() {
		super.onCreate();
		configuration = new YoukuPlayerBaseConfiguration(this) {
			
			@Override
			public Class<? extends Activity> getCachingActivityClass() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public Class<? extends Activity> getCachedActivityClass() {
				// TODO Auto-generated method stub
				return null;
			}
			
			@Override
			public String configDownloadPath() {
				// TODO Auto-generated method stub
				return null;
			}
		};
	}

}
