package com.example.adapter;

import java.util.ArrayList;
import java.util.List;

import com.example.bean.TelePlayBean;
import com.example.myyoukuplayer.R;
import com.squareup.picasso.Picasso;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


/**
 * 电视剧adapter
 * @author 李晓军
 *
 */
public class TelePlayAdapter extends BaseAdapter {

	private ArrayList<TelePlayBean> list;
	private Context context;
	
	public TelePlayAdapter(Context context,List list){
		this.list = (ArrayList<TelePlayBean>) list;
		this.context = context;
	}
	
	@Override
	public int getCount() {
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}

	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		ViewHolder viewHolder;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if(view == null){
			viewHolder = new ViewHolder();
			view = inflater.inflate(R.layout.listitem_teleplay, null);
			viewHolder.iv = (ImageView) view.findViewById(R.id.listitem_teleplay_imageview);
			viewHolder.set = (TextView) view.findViewById(R.id.listitem_teleplay_set);
			viewHolder.name = (TextView) view.findViewById(R.id.listitem_teleplay_name);
			viewHolder.score = (TextView) view.findViewById(R.id.listitem_teleplay_score);
			view.setTag(viewHolder);
		}
		viewHolder = (ViewHolder) view.getTag();
		//设置图片
		Picasso.with(context).load(list.get(position).getPoster()).into(viewHolder.iv);
		//如果总集数和更新集数相同，就说明已经完结了
		if(list.get(position).getEpisode_count()==list.get(position).getEpisode_updated())
			viewHolder.set.setText(list.get(position).getEpisode_count()+"集全");
		else
			viewHolder.set.setText("更新至"+list.get(position).getEpisode_updated()+"集");
		viewHolder.name.setText(list.get(position).getName());
		viewHolder.score.setText(""+list.get(position).getScore());
		return view;
	}
	
	class ViewHolder{
		ImageView iv;
		TextView set,name,score;
	}

}
