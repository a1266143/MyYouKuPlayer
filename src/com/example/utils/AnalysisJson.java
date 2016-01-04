package com.example.utils;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import android.util.Log;

import com.example.bean.ShowBean;
import com.example.bean.TelePlayBean;
import com.example.bean.VideoBean;

/**
 * 解析json数据的工具类
 * @author 李晓军
 *
 */
public class AnalysisJson {
	private static final AnalysisJson an = new AnalysisJson();
	private AnalysisJson(){};
	public static  AnalysisJson getInstance(){
		return an;
	}
	
	
	
	/**
	 * 解析电视剧的json数据为列表
	 * @param json
	 * @return
	 */
	public ArrayList<TelePlayBean> getTelePlayArr(String json){
		ArrayList<TelePlayBean> arr = new ArrayList<TelePlayBean>();
		try {
			JSONObject jo = new JSONObject(json);
			JSONArray shows = jo.getJSONArray("shows");
			for(int i=0;i<shows.length();i++){
				
				TelePlayBean tb = new TelePlayBean();
				//如果需要付费，直接跳过
				if(shows.getJSONObject(i).getInt("paid")==1)
					continue;
				tb.setId(shows.getJSONObject(i).getString("id"));
				tb.setName(shows.getJSONObject(i).getString("name"));
				tb.setPoster(shows.getJSONObject(i).getString("poster"));
				tb.setScore((float) shows.getJSONObject(i).getDouble("score"));
				tb.setPaid(shows.getJSONObject(i).getInt("paid"));
				tb.setEpisode_count(shows.getJSONObject(i).getInt("episode_count"));
				tb.setEpisode_updated(shows.getJSONObject(i).getInt("episode_updated"));
				arr.add(tb);
			}
		} catch (JSONException e) {
			e.printStackTrace();
			//如果json解析错误，直接返回null
			return null;
		}
		return arr;
	}
	
	/**
	 * 解析具体电视剧集的json为arraylist
	 * @param json
	 * @return
	 */
	public ArrayList<String> getTeleSetArr(String json){
		ArrayList<String> arr = new ArrayList<String>();
		try {
			JSONObject jo = new JSONObject(json);
			JSONArray videos = jo.getJSONArray("videos");
			for(int i=0;i<videos.length();i++){
				String id = videos.getJSONObject(i).getString("id");
				arr.add(id);
			}
			return arr;
		} catch (JSONException e) {
			Log.e("json解析", "json解析错误");
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 解析热门关键词json
	 * @param json
	 * @return
	 */
	public ArrayList<String> getHotWordsArr(Handler handler,String json){
		ArrayList<String> arr = new ArrayList<String>();
		try {
			JSONObject jo = new JSONObject(json);
			JSONArray keywords = jo.getJSONArray("keywords");
			for(int i=0;i<keywords.length();i++){
				String keyword = keywords.getJSONObject(i).getString("keyword");
				arr.add(keyword);
			}
			return arr;
		} catch (JSONException e) {
			//解析json出错
			handler.sendEmptyMessage(StaticCode.MISTAKE_JSON);
			return null;
		}
	}
	
	/**
	 * 解析节目的shows
	 * @param json json字符串
	 * @param handler
	 * @return
	 */
	public ArrayList<ShowBean> AnaShows(String json,Handler handler){
		ArrayList<ShowBean> arr = new ArrayList<ShowBean>();
		try {
			JSONObject jo = new JSONObject(json);
			JSONArray shows = jo.getJSONArray("shows");
			for(int i=0;i<shows.length();i++){
				JSONObject show = shows.getJSONObject(i);
				ShowBean sb = new ShowBean();
				sb.setId(show.getString("id"));
				sb.setName(show.getString("name"));
				sb.setPoster(show.getString("poster"));
				sb.setShowcategory(show.getString("showcategory"));
				sb.setDescription(show.getString("description"));
				//解析score，double类型先转换成Double对象，然后再转化成String对象，然后再截断0，2的位置，比如9.525->9.5
				Double scor = Double.valueOf(show.getDouble("score"));
				String score = scor.toString();
				score.substring(0, 2);
				sb.setScore(score);
				String published = (show.getString("published")).substring(0,4);
				sb.setPublished(published);
				int completed = show.getInt("completed");
				int episode_updated = show.getInt("episode_updated");
				sb.setCompleted(completed);
				if(completed==1&&episode_updated<10000){
					sb.setEpisode_updated(episode_updated+"集全");
				}else if(completed == 1&&episode_updated>10000){
					sb.setEpisode_updated(episode_updated+"");
				}
				else if(completed == 0&&episode_updated>10000){
					sb.setEpisode_updated("更新至"+episode_updated+"期");
				}
				else
					sb.setEpisode_updated("更新至"+episode_updated+"集");
				arr.add(sb);
			}
			return arr;
		} catch (JSONException e) {
			//handler.sendEmptyMessage(StaticCode.MISTAKE_JSON);
			//返回一个空的列表
			return new ArrayList<ShowBean>();
		}
	}
	
	/**
	 * 解析视频的json数据
	 * @param json数据
	 * @return 返回解析过后的arraylist，如果解析错误，就返回size为空的集合，表示没有数据
	 */
	public ArrayList<VideoBean> anaVideos(String json){
		ArrayList<VideoBean> arr = new ArrayList<VideoBean>();
		try {
			JSONObject jo = new JSONObject(json);
			JSONArray videos = jo.getJSONArray("videos");
			for(int i=0;i<videos.length();i++){
				JSONObject video = videos.getJSONObject(i);
				String id = video.getString("id");
				String thumbnail = video.getString("thumbnail");
				String title = video.getString("title");
				VideoBean vb = new VideoBean();
				vb.setId(id);
				vb.setThumbnail(thumbnail);
				vb.setTitle(title);
				arr.add(vb);
			}
			return arr;
		} catch (JSONException e) {
			//返回一个size为0的list
			return new ArrayList<VideoBean>();
		}
	}
	
	/**
	 * 解析关键词联想 
	 * @param json传递进来的json数据
	 * @return
	 */
	public ArrayList<String> anaKeyWordConnect(String json){
		ArrayList<String> arr = new ArrayList<String>();
		try {
			JSONObject jo = new JSONObject(json);
			JSONArray r = jo.getJSONArray("r");
			for(int i=0;i<r.length();i++){
				JSONObject c = r.getJSONObject(i);
				String cc = c.getString("c");
				arr.add(cc);
			}
			return arr;
		} catch (JSONException e) {
			//返回一个size为0的ArrayList
			return new ArrayList<String>();
		}
	}
}
