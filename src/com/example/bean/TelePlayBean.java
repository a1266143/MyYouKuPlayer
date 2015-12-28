package com.example.bean;

/**
 * 电视剧bean
 * @author 李晓军
 *
 */
public class TelePlayBean {
	//节目id
	private String id;
	//节目名称
	private String name;
	//海报地址
	private String poster;
	//是否付费，0为免费，1为付费
	private int paid;
	//总集数
	private int episode_count;
	//更新集数
	private int episode_updated;
	//分数
	private float score;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPoster() {
		return poster;
	}
	public void setPoster(String poster) {
		this.poster = poster;
	}
	public int getPaid() {
		return paid;
	}
	public void setPaid(int paid) {
		this.paid = paid;
	}
	public int getEpisode_count() {
		return episode_count;
	}
	public void setEpisode_count(int episode_count) {
		this.episode_count = episode_count;
	}
	public int getEpisode_updated() {
		return episode_updated;
	}
	public void setEpisode_updated(int episode_updated) {
		this.episode_updated = episode_updated;
	}
	public float getScore() {
		return score;
	}
	public void setScore(float score) {
		this.score = score;
	}
	
	
}
