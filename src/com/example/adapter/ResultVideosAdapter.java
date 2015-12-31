package com.example.adapter;

import java.util.ArrayList;

import com.example.bean.VideoBean;
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

public class ResultVideosAdapter extends BaseAdapter {

	private Context context;
	private ArrayList<VideoBean> arr;
	
	public ResultVideosAdapter(Context context, ArrayList<VideoBean> arr) {
		super();
		this.context = context;
		this.arr = arr;
	}

	@Override
	public int getCount() {
		return arr.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arr.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewgroup) {
		ViewHolder holder;
		if(view == null){
			holder = new ViewHolder();
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			view = inflater.inflate(R.layout.listitem_result_gridview, null);
		    holder.iv = (ImageView) view.findViewById(R.id.listitem_result_gridview_iv);
		    holder.title = (TextView) view.findViewById(R.id.listitem_result_gridview_title);
		    view.setTag(holder);
		}
		holder = (ViewHolder) view.getTag();
		Picasso.with(context).load(Uri.parse(arr.get(position).getThumbnail())).into(holder.iv);
		holder.title.setText(arr.get(position).getTitle());
		return view;
	}
	
	class ViewHolder{
		ImageView iv;
		TextView title;
	}

}
