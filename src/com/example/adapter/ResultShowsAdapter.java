package com.example.adapter;

import java.util.ArrayList;

import com.example.bean.ShowBean;
import com.example.myyoukuplayer.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class ResultShowsAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<ShowBean> arr;
	public ResultShowsAdapter(Context context, ArrayList<ShowBean> arr) {
		super();
		this.context = context;
		this.arr = arr;
	}

	@Override
	public int getCount() {
		return arr.size();
	}

	@Override
	public Object getItem(int position) {
		return arr.get(position);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int arg0, View view, ViewGroup arg2) {
		ViewHolder holder;
		if(view == null){
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listitem_result_listview, null);
			holder.iv = (ImageView) view.findViewById(R.id.listitem_result_listview_iv);
			holder.name = (TextView) view.findViewById(R.id.listitem_result_listview_name);
			holder.category = (TextView) view.findViewById(R.id.listitem_result_listview_category);
			holder.description = (TextView) view.findViewById(R.id.listitem_result_listview_description);
			holder.score = (TextView) view.findViewById(R.id.listitem_result_listview_score);
			holder.update = (TextView) view.findViewById(R.id.listitem_result_listview_update);
			view.setTag(holder);
		}
		holder = (ViewHolder) view.getTag();
		//设置图片
		Picasso.with(context).load(Uri.parse(arr.get(arg0).getPoster())).into(holder.iv);
		//设置名字
		holder.name.setText(arr.get(arg0).getName());
		//设置种类
		holder.category.setText(arr.get(arg0).getShowcategory()+arr.get(arg0).getPublished());
		//设置描述信息
		holder.description.setText(arr.get(arg0).getDescription());
		//设置评分
		holder.score.setText(arr.get(arg0).getScore());
		//设置更新到几集
		holder.update.setText(arr.get(arg0).getEpisode_updated());
		//返回view
		return view;
	}
	
	class ViewHolder{
		ImageView iv;
		TextView name,category,description,score,update;
	}

}
