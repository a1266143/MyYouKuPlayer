package com.example.myyoukuplayer;

import java.util.ArrayList;
import java.util.List;

import com.example.utils.NetForJsonUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;
import com.youku.player.base.YoukuBasePlayerManager;
import com.youku.player.base.YoukuPlayer;
import com.youku.player.base.YoukuPlayerView;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.app.Activity;
import android.content.res.Configuration;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.RelativeLayout;
import android.widget.Toast;

public class PlayActivity extends Activity {

	private YoukuBasePlayerManager manager;
	private YoukuPlayerView playerView;
	// youkuplayer,控制视频播放
	private YoukuPlayer player;
	private int page = 1;
	private List<String> idList;
	private List<String> setsList;
	private PullToRefreshGridView gridView;
	ArrayAdapter<String> adapter;
	String id;
	Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			//如果网络错误
			if(msg.what == NetForJsonUtils.MISTAKE_NET){
				Toast.makeText(PlayActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
				//将page的值减1
				page-=1;
				//关闭刷新
				gridView.onRefreshComplete();
				return;
			}
			//如果json解析错误
			if(msg.what == NetForJsonUtils.MISTAKE_JSON){
				Toast.makeText(PlayActivity.this, "json解析错误", Toast.LENGTH_SHORT).show();
				page-=1;
				//关闭刷新
				gridView.onRefreshComplete();
				return;
			}
			// 获取网络操作后传送过来的arraylist
			if(page == 1){
				idList = (List<String>) msg.getData().getSerializable("ArrayList");
				// 播放
				player.playVideo(idList.get(0));
				setsList = new ArrayList<String>();
				int a = 1;
				for(int i = 0;i<idList.size();i++){
					setsList.add(""+a);
					a++;
				}
				adapter = new ArrayAdapter<String>(PlayActivity.this, R.layout.listite_play_gridview, setsList);
				gridView.setAdapter(adapter);
				
			}
			//否则如果是分页加载
			else{
				idList.addAll((List<String>) msg.getData().getSerializable("ArrayList"));
				setsList.clear();
				int a = 1;
				for(int i = 0;i<idList.size();i++){
					setsList.add(""+a);
					a++;
				}
				adapter.notifyDataSetChanged();
			}
			//关闭刷新
			gridView.onRefreshComplete();
		}

	};

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play);
		gridView = (PullToRefreshGridView) findViewById(R.id.play_gridview);
		
		// 获得节目id
		id = getIntent().getStringExtra("id");

		manager = new YoukuBasePlayerManager(this) {

			@Override
			public void setPadHorizontalLayout() {

			}

			@Override
			public void onSmallscreenListener() {

			}

			@Override
			public void onInitializationSuccess(YoukuPlayer player) {
				// 初始化成功后需要添加该行代码
				addPlugins();
				// 实例化YoukuPlayer实例
				PlayActivity.this.player = player;

			}

			@Override
			public void onFullscreenListener() {

			}
		};
		manager.onCreate();
		playerView = (YoukuPlayerView) findViewById(R.id.youkuplayer);
		// 控制竖屏和全屏时候的布局参数。这两句必填。
		playerView.setSmallScreenLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT));
		playerView.setFullScreenLayoutParams(new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT));
		playerView.initialize(manager);
		// 执行网络操作，获取具体剧集数据
		NetForJsonUtils.getInstance().getTeleSet(id, handler, page);
		
		//设置当最后一个item看得见的时候，直接加载第二页
		gridView.setMode(Mode.PULL_UP_TO_REFRESH);
		gridView.setOnRefreshListener(new MyOnRefreshListener());
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				player.playVideo(idList.get(arg2));
			}
		});
	}
	
	class MyOnRefreshListener implements OnRefreshListener2<GridView>{

		@Override
		public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
			
		}

		@Override
		public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
			page+=1;
			NetForJsonUtils.getInstance().getTeleSet(id, handler, page);
		}
		
	}

	@Override
	public void onBackPressed() { // android系统调用
		super.onBackPressed();
		manager.onBackPressed();
		Toast.makeText(this, "backPress调用了", Toast.LENGTH_SHORT).show();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		manager.onConfigurationChanged(newConfig);
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		manager.onDestroy();
		page =1 ;
	}
	
	
	@Override
	protected void onPause() {
		super.onPause();
		manager.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		manager.onResume();
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		manager.onStart();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		manager.onStop();
	}
	
	@Override
	public boolean onSearchRequested() { // android系统调用
		return manager.onSearchRequested();
	}
	
	
	/**
	 * 重写该方法
	 * 全屏状态下按返回键不会退出activity
	 */
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		boolean managerKeyDown = manager.onKeyDown(keyCode, event);
		if (manager.shouldCallSuperKeyDown()) {
			return super.onKeyDown(keyCode, event);
		} else {
			return managerKeyDown;
		}

	}
	
	@Override
	public void onLowMemory() { // android系统调用
		super.onLowMemory();
		manager.onLowMemory();
	}
	

}
