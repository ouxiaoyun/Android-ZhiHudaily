package com.android_zhihu.activity.entity;

public class StoryInfo {
	private String body;
	private String image_source;
	private String title;
	private String image;
	private String share_url;
	private String[] js;
	private String ga_prefix;
	private int type;
	private long id;
	private String[] css;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getImage_source() {
		return image_source;
	}

	public void setImage_source(String image_source) {
		this.image_source = image_source;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getShare_url() {
		return share_url;
	}

	public void setShare_url(String share_url) {
		this.share_url = share_url;
	}

	public String[] getJs() {
		return js;
	}

	public void setJs(String[] js) {
		this.js = js;
	}

	public String getGa_prefix() {
		return ga_prefix;
	}

	public void setGa_prefix(String ga_prefix) {
		this.ga_prefix = ga_prefix;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String[] getCss() {
		return css;
	}

	public void setCss(String[] css) {
		this.css = css;
	}
}
