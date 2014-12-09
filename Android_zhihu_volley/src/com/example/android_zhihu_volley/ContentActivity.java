package com.example.android_zhihu_volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.android_zhihu.activity.c.API;
import com.android_zhihu.activity.entity.StoryInfo;
import com.android_zhihu.activity.entity.ThemeStoryInfo;
import com.android_zhihu.cache.BitmapCache;

public class ContentActivity extends Activity implements Listener<JSONObject>{
	
	
	private WebView webview_home;
	private  NetworkImageView imgv_webview_top;
	
	//参数
	private long id;
	
	private RequestQueue queue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.item_home_webview);
		
		getReady();
	}

	
	/*-----------------数据准备----------------------------*/
	
	private void getReady() {
		
		webview_home = (WebView) findViewById(R.id.webview_home);
		imgv_webview_top = (NetworkImageView) findViewById(R.id.imgv_webview_top);
		
		id = getIntent().getLongExtra("id", 0);
		
		queue = Volley.newRequestQueue(getApplicationContext());
		
		queue.add(new JsonObjectRequest(Method.GET, String.format(API.getStory(), id),null, this, null));
		
	}

	
	/*-----------------数据准备----------------------------*/
	
	
	/*-----------------Json解析----------------------------*/
	@Override
	public void onResponse(JSONObject response) {
		
		try {
			
			if(response.has("theme_name")){
				
				ThemeStoryInfo themeStoryInfo = new ThemeStoryInfo();
				
				if(response.has("body")){
					
					
					themeStoryInfo.setBody(response.getString("body"));
					themeStoryInfo.setEditor_avatar(response.getString("editor_avatar"));
					themeStoryInfo.setEditor_id(response.getLong("editor_id"));
					themeStoryInfo.setEditor_name(response.getString("editor_name"));
					themeStoryInfo.setGa_prefix(response.getString("ga_prefix"));
					themeStoryInfo.setId(response.getLong("id"));
					themeStoryInfo.setShare_url(response.getString("share_url"));
					themeStoryInfo.setTheme_id(response.getLong("theme_id"));
					themeStoryInfo.setTheme_image(response.getString("theme_image"));
					themeStoryInfo.setTheme_name(response.getString("theme_name"));
					themeStoryInfo.setTitle(response.getString("title"));
					themeStoryInfo.setType(response.getInt("type"));
					
				}
				else{
					
					themeStoryInfo.setEditor_avatar(response.getString("editor_avatar"));
					themeStoryInfo.setEditor_id(response.getLong("editor_id"));
					themeStoryInfo.setEditor_name(response.getString("editor_name"));
					themeStoryInfo.setGa_prefix(response.getString("ga_prefix"));
					themeStoryInfo.setId(response.getLong("id"));
					themeStoryInfo.setShare_url(response.getString("share_url"));
					themeStoryInfo.setTheme_id(response.getLong("theme_id"));
					themeStoryInfo.setTheme_image(response.getString("theme_image"));
					themeStoryInfo.setTheme_name(response.getString("theme_name"));
					themeStoryInfo.setTitle(response.getString("title"));
					themeStoryInfo.setType(response.getInt("type"));
					
				}
				
				
				
				// JS
				JSONArray arrayJs = response.getJSONArray("js");
				if (arrayJs != null && arrayJs.length() > 0) {
					String[] js = new String[arrayJs.length()];
					for (int i = 0 ; i < arrayJs.length() ; i++) {
						js[i] = arrayJs.getString(i);
					}
					themeStoryInfo.setJs(js);
				}
				
				// CSS
				JSONArray arrayCss = response.getJSONArray("css");
				if (arrayCss != null && arrayCss.length() > 0) {
					String[] css = new String[arrayCss.length()];
					for (int i = 0 ; i < arrayCss.length() ; i++) {
						css[i] = arrayCss.getString(i);
					}
					themeStoryInfo.setCss(css);
				}
				
				
				// 加载HTML
				String body = null;
				if (themeStoryInfo.getCss() != null && themeStoryInfo.getCss().length > 0) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0 ; i < themeStoryInfo.getCss().length ; i++) {
						sb.append("<link rel=\"stylesheet\" href=\"");
						sb.append(themeStoryInfo.getCss()[i]);
						sb.append("\">");
					}
					body = String.format("%s%s", sb.toString(), themeStoryInfo.getBody());
				} else {
					body = themeStoryInfo.getBody();
				}
				
				imgv_webview_top.setVisibility(View.GONE);
				
				webview_home.loadDataWithBaseURL(null, body, "text/html", "UTF-8", null);
				
			}else{
				
				StoryInfo storyInfo = new StoryInfo();
				storyInfo.setBody(response.getString("body"));
				storyInfo.setGa_prefix(response.getString("ga_prefix"));
				storyInfo.setId(response.getLong("id"));
				storyInfo.setImage(response.getString("image"));
				storyInfo.setImage_source(response.getString("image_source"));
				storyInfo.setShare_url(response.getString("share_url"));
				storyInfo.setTitle(response.getString("title"));
				storyInfo.setType(response.getInt("type"));
				
				// JS
				JSONArray arrayJs = response.getJSONArray("js");
				if (arrayJs != null && arrayJs.length() > 0) {
					String[] js = new String[arrayJs.length()];
					for (int i = 0 ; i < arrayJs.length() ; i++) {
						js[i] = arrayJs.getString(i);
					}
					storyInfo.setJs(js);
				}
				
				// CSS
				JSONArray arrayCss = response.getJSONArray("css");
				if (arrayCss != null && arrayCss.length() > 0) {
					String[] css = new String[arrayCss.length()];
					for (int i = 0 ; i < arrayCss.length() ; i++) {
						css[i] = arrayCss.getString(i);
					}
					storyInfo.setCss(css);
				}
				
				
				
				
				// 加载HTML
				String body = null;
				if (storyInfo.getCss() != null && storyInfo.getCss().length > 0) {
					StringBuffer sb = new StringBuffer();
					for (int i = 0 ; i < storyInfo.getCss().length ; i++) {
						sb.append("<link rel=\"stylesheet\" href=\"");
						sb.append(storyInfo.getCss()[i]);
						sb.append("\">");
					}
					body = String.format("%s%s", sb.toString(), storyInfo.getBody());
				} else {
					body = storyInfo.getBody();
				}
				
				// 设置顶部图片
				imgv_webview_top.setImageUrl(storyInfo.getImage(), new ImageLoader(queue, new BitmapCache()));
				
				if (storyInfo.getImage() != null && storyInfo.getImage().length() > 0) {
					body = body.replace("<div class=\"img-place-holder\"></div>", "");
				}
				
				/*if (storyInfo.getImage() != null && storyInfo.getImage().length() > 0) {
					body = body.replace("<div class=\"img-place-holder\"></div>", "<div class=\"img-place-holder\" style=\"background-image: url(" + storyInfo.getImage() + "); background-position:center;\"></div>");
				}*/
				
				
//				webContent.getSettings().setJavaScriptEnabled(true);
//				webContent.loadUrl("file:///android_asset/index.html");
				
				webview_home.loadDataWithBaseURL(null, body, "text/html", "UTF-8", null);
				
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	/*-----------------Json解析----------------------------*/

}
