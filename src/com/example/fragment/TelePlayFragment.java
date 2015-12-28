package com.example.fragment;

import java.util.ArrayList;
import java.util.List;

import com.example.adapter.TelePlayAdapter;
import com.example.bean.TelePlayBean;
import com.example.myyoukuplayer.PlayActivity;
import com.example.myyoukuplayer.R;
import com.example.utils.NetForJsonUtils;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.Mode;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshGridView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 电视剧fragment
 * @author 李晓军
 *
 */
public class TelePlayFragment extends Fragment {
	private PullToRefreshGridView gridView;
	private TextView telenonettip;
	
	//当前加载页数
	private int page = 1;
	//是否已经加载过
	private List<TelePlayBean> list;
	private TelePlayAdapter adapter;
	
	Handler handler = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			//如果解析json错误，直接显示错误信息
			if(msg.what==NetForJsonUtils.MISTAKE_JSON){
				Toast.makeText(getActivity(), "解析json数据错误", Toast.LENGTH_SHORT).show();
			}
			//如果是网络错误
			else if(msg.what == NetForJsonUtils.MISTAKE_NET){
				Toast.makeText(getActivity(), "网络错误", Toast.LENGTH_SHORT).show();
				//如果是第一次显示网络错误，就将页面提示文字显示出来
				if(adapter==null){
					telenonettip.setVisibility(View.VISIBLE);
				}
				gridView.onRefreshComplete();
				if(page>1)
					page-=1;
			}else{
				
				//如果是第一次加载，就设置adapter
				if(page == 1){
					list = (List) msg.getData().getSerializable("ArrayList");
					adapter = new TelePlayAdapter(getActivity(), list);
					gridView.setAdapter(adapter);
					telenonettip.setVisibility(View.GONE);
				}
					
				//如果是分页加载，就notifi
				else{
					//将list容量增大
					list.addAll((List) msg.getData().getSerializable("ArrayList"));
					if(adapter!=null)
						adapter.notifyDataSetChanged();
					telenonettip.setVisibility(View.GONE);
				}
				//最后关闭刷新提示
				gridView.onRefreshComplete();
			}
		}
		
	};
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_teleplay, null);
		gridView = (PullToRefreshGridView) view.findViewById(R.id.teleplay_pulltorefreshgridview);
		telenonettip = (TextView) view.findViewById(R.id.teleplay_nonettip);
		gridView.setMode(Mode.BOTH);
		gridView.setOnRefreshListener(new MyOnRefreshListener());
		NetForJsonUtils.getInstance().getTelePlay(handler, page, NetForJsonUtils.TYPE_TELEPLAY);
		gridView.setOnItemClickListener(new MyOnItemClickListener());
		return view;
	}
	
	
	/**
	 * 下拉刷新和上拉加载更多监听器
	 * @author Administrator
	 *
	 */
	class MyOnRefreshListener implements OnRefreshListener2<GridView>{

		//下拉刷新监听器
		@Override
		public void onPullDownToRefresh(PullToRefreshBase<GridView> refreshView) {
			//将page设置为1，重新加载
			page = 1;
			NetForJsonUtils.getInstance().getTelePlay(handler, page, NetForJsonUtils.TYPE_TELEPLAY);
		}

		//上拉加载更多监听器
		@Override
		public void onPullUpToRefresh(PullToRefreshBase<GridView> refreshView) {
			page+=1;
			NetForJsonUtils.getInstance().getTelePlay(handler, page, NetForJsonUtils.TYPE_TELEPLAY);
		}
		
	}
	
	/**
	 * gridview点击事件监听器
	 */
	class MyOnItemClickListener implements OnItemClickListener{

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			
			Intent intent = new Intent(getActivity(),PlayActivity.class);
			intent.putExtra("id", list.get(position).getId());
			startActivity(intent);
		}
		
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		//将page设置为1
		page=1;
	}
}
