package com.android_zhihu.activity.widget;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.android_zhihu.activity.c.API;
import com.android_zhihu.activity.entity.News;
import com.android_zhihu.activity.entity.Story;
import com.android_zhihu.activity.entity.TopStory;
import com.android_zhihu.cache.BitmapCache;
import com.android_zhihu.utils.date.DateStyle;
import com.android_zhihu.utils.date.DateUtil;
import com.example.android_zhihu_volley.ContentActivity;
import com.example.android_zhihu_volley.R;
import com.example.android_zhihu_volley.R.menu;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshBase.OnRefreshListener2;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

public class CombineListandViewpager extends FrameLayout implements Listener<JSONObject>,OnRefreshListener2<ScrollView>,OnItemClickListener{

	private Context context;
	
	//首页的幻灯
	private ViewPager pager_home;
	private TextView txtv_viewpager_title;
	private LinearLayout index_viewpager;
	private Timer timer;
	private List<ImageView> imageViews;
	private MyPagerAdapter pageradapter;
	
	//首页的listview
	private ListView lv_list;
	private News news;
	private boolean isUp;
	private MyListAdapter adapter;
	
	//首页上拉刷新,下拉加载
	private PullToRefreshScrollView sv_home;
	
	//数据解析
	private RequestQueue queue;
	

	public CombineListandViewpager(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		LayoutInflater.from(context).inflate(R.layout.item_home, this);
		
		getReady();
		

	}

	// 数据准备
	private void getReady() {
		
		index_viewpager = (LinearLayout) findViewById(R.id.index_viewpager);
		pager_home = (ViewPager) findViewById(R.id.pager_home);
		pageradapter = new MyPagerAdapter();
		pager_home.setAdapter(pageradapter);
		txtv_viewpager_title = (TextView) findViewById(R.id.txtv_viewpager_title);
		
		
		lv_list = (ListView) findViewById(R.id.lv_list);
		//初始化的时候就第一次加载数据
		adapter = new MyListAdapter();
		lv_list.setAdapter(adapter);
		lv_list.setOnItemClickListener(this);
		sv_home = (PullToRefreshScrollView) findViewById(R.id.sv_home);
		sv_home.setOnRefreshListener(this);
	}

	// 初始化数据
	public void initData(String uri) {
		
		
		queue = Volley.newRequestQueue(context);
		
		queue.add(new JsonObjectRequest(Method.GET,uri, null, this, null));

	}

	

	
	/* -------------------- 数据解析 -------------------- */
		
	@Override
	public void onResponse(JSONObject response) {
		
		
		
		try {
			
			if(!isUp){
				news = new News();
				
				String dateData = response.getString("date");
				
				news.setDate(dateData);
				
				
				//list内容的解析
				parserStroy(response);
				
				//首页viewpager的数据解析
				parserTopStory(response);
				
				
				// 初始化换灯
				initSlide();

				//初始化ListView
				initListView();
				
				// 将ScrollView置顶
				sv_home.getRefreshableView().smoothScrollTo(0, 0);
				
			}
			else{
				
				String dateData = response.getString("date");
				
				news.setDate(dateData);
				
				//list内容的解析
				parserStroy(response);
				
				//初始化ListView
				initListView();
				
				isUp = false;
				
				
			}
			
			//关闭刷新
			sv_home.onRefreshComplete();
			
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void parserTopStory(JSONObject response) throws JSONException {
		// TopStory

		JSONArray array_top_stories = response.getJSONArray("top_stories");

		List<TopStory>topStories = new ArrayList<TopStory>();

		if (array_top_stories != null&& array_top_stories.length() > 0) {

			TopStory topStory;

			for (int i = 0; i < array_top_stories.length(); i++) {
				topStory = new TopStory();
				JSONObject object = (JSONObject) array_top_stories
						.get(i);
				topStory.setTitle(object.getString("title"));
				topStory.setId(object.getLong("id"));
				topStory.setImage(object.getString("image"));
				topStory.setGa_prefix(object.getString("ga_prefix"));
				topStory.setType(object.getInt("type"));
				/*topStory.setBitmap(NetHelper.getBitmap(topStory
						.getImage()));*/
				topStory.setShare_url(object.getString("share_url"));

				topStories.add(topStory);

			}
			
			news.setTopStories(topStories);
			
		}
	}

	private void parserStroy(JSONObject response) {
		// Story解析
		JSONArray jsonArray;
		try {
			jsonArray = response.getJSONArray("stories");
			
			List<Story>stories = new ArrayList<Story>();
			
			//---------标题设置-----------------//
			//标题
			Story storyTitle = new Story();
			
			String dateData = response.getString("date");
			
			if(dateData.equals(DateUtil.DateToString(new Date(), "yyyyMMdd"))){
				storyTitle.setTitle("今日热闻");
			}
			else{
				String mmdd = DateUtil.StringToString(dateData, DateStyle.MM_DD_CN);
				String week = DateUtil.getWeek(dateData).getChineseName();
				storyTitle.setTitle(String.format("%s  %s", mmdd, week));
			}
			
			stories.add(storyTitle);
			
			//---------标题设置-----------------//

			Story story;
			
			for (int i = 0; i < jsonArray.length(); i++) {
				story = new Story();
				JSONObject object = (JSONObject) jsonArray.get(i);
				
				if(object.has("theme_name")){
					
				}
				else{
					
					story.setId(object.getLong("id"));

					story.setGa_prefix(object.getString("ga_prefix"));

					story.setShare_url(object.getString("share_url"));

					story.setTitle(object.getString("title"));

					story.setType(object.getInt("type"));

					JSONArray array = object.getJSONArray("images");

					if (array != null && array.length() > 0) {
						String[] images = new String[array.length()];

						for (int j = 0; j < images.length; j++) {

							images[j] = array.getString(j);
						}

						story.setImages(images);
					}

					/*if (story.getImages() != null&& story.getImages().length > 0) {
						story.setBitmap(NetHelper.getBitmap(story.getImages()[0]));
						
					}*/
					stories.add(story);
				}
				
			}
			
			if(news.getStories() != null){
				news.getStories().addAll(stories);
			}else{
				news.setStories(stories);
			}
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/* -------------------- 数据解析 -------------------- */
	
	
	/* -------------------- 初始化幻灯 -------------------- */
	
	
	//viewpager的position
	private int item;
	
	private Handler handler = new Handler(){
		public void handleMessage(android.os.Message msg) {
			pager_home.setCurrentItem(item);
			
			if(item == imageViews.size() - 1){
				item = 0;
			}
			else{
				item++;
			}
		};
	};
	
	
	/**
	 * 初始化幻灯
	 */
	private void initSlide() {
		
		imageViews = new ArrayList<ImageView>();

		for (int i = 0; i < news.getTopStories().size(); i++) {
			TopStory topStory = news.getTopStories().get(i);
			NetworkImageView imageView = new NetworkImageView(context);
			imageView.setScaleType(ScaleType.CENTER_CROP);
			imageView.setImageUrl(topStory.getImage(),new ImageLoader(queue, new BitmapCache()));
			imageViews.add(imageView);
		}
		
		
		// 通知幻灯数据改变
		pageradapter.notifyDataSetChanged();

	    timer = new Timer();

		timer.schedule(new TimerTask() {

			@Override
			public void run() {

				handler.sendEmptyMessage(0);

			}
		}, 2000, 3000);

		// 初始化小圆点
		initSmallDot(0);

		// 初始化标题
		txtv_viewpager_title.setText(news.getTopStories().get(0).getTitle());
		
		
		pager_home.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int arg0) {
				
				initSmallDot(arg0);
				
				item = arg0;
				
				txtv_viewpager_title.setText(news.getTopStories().get(arg0).getTitle());
				
			}
			
			@Override
			public void onPageScrolled(int arg0, float arg1, int arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
			
	}
	
	
	/* -------------------- 初始化幻灯 -------------------- */
	
	
	/* -------------------- 初始化小圆点 -------------------- */
	
	private void initSmallDot(int i) {

		index_viewpager.removeAllViews();

		for (int j = 0; j < imageViews.size(); j++) {

			ImageView imageView = new ImageView(context);
			imageView.setImageResource(R.drawable.dot_default);
			imageView.setPadding(5, 0, 5, 0);

			index_viewpager.addView(imageView);
		}

		// 设置选中
		((ImageView) index_viewpager.getChildAt(i)).setImageResource(R.drawable.dot_selected);

	}
	
	
	/* -------------------- 初始化小圆点 -------------------- */
	
	
	/* -------------------- 幻灯的adapter -------------------- */
	
	
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return imageViews == null ? 0 : imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
       			return arg0 == arg1;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			container.addView(imageViews.get(position));
			return imageViews.get(position);
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView(imageViews.get(position));
		}

	}
	
	/* -------------------- 幻灯的adapter -------------------- */
	
	
	/* -------------------- 初始化listview -------------------- */
	
	private void initListView() {
		
		adapter.notifyDataSetChanged();
		
	}
	
	/* -------------------- 初始化listview -------------------- */
	
	
	/* -------------------- listview的adapter -------------------- */
	
	
	class MyListAdapter extends BaseAdapter{

		private MyHolder holder;

		@Override
		public int getCount() {
			return news == null ? 0 : news.getStories().size();
		}

		@Override
		public Object getItem(int position) {
			return news.getStories().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			Story story = news.getStories().get(position);
			
			if(story.getId() == 0){
				convertView = LayoutInflater.from(context).inflate(R.layout.list_news_title, parent,false);
				holder = new MyHolder();
				holder.list_news_title = (TextView) convertView.findViewById(R.id.list_news_title);
				holder.list_news_title.setText(story.getTitle());
			}
			else{
				if (convertView == null || story.getId() != 0) {
					convertView = LayoutInflater.from(context).inflate(R.layout.item_list_zhihu, parent, false);
					
					holder = new MyHolder();
					holder.txtv_home_title = (TextView) convertView.findViewById(R.id.txtv_home_title);
					holder.imgv_content = (NetworkImageView) convertView.findViewById(R.id.imgv_content);
					
					convertView.setTag(holder);
				} else {
					holder = (MyHolder) convertView.getTag();
				}
				
			
				holder.txtv_home_title.setText(story.getTitle());
				holder.imgv_content.setImageUrl(story.getImages()[0], new ImageLoader(queue, new BitmapCache()));
			}
			
			
			return convertView;
		}
		
		class MyHolder {
			public TextView txtv_home_title,list_news_title;
			public NetworkImageView imgv_content;
		}
		
	}



	
	
	/* -------------------- listview的adapter -------------------- */
	

	/* -------------------- pulltorefresh上下监听 -------------------- */
	
	@Override
	public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {

		//清空数据
		timer.cancel();
		news.getStories().clear();
		news.getTopStories().clear();
		
		//再次加载数据
		initData(API.getLatestUrl());
		
	}

	@Override
	public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {
		
		isUp = true;
		queue.add(new JsonObjectRequest(Method.GET, String.format(API.getBefore(), news.getDate()), null, this, null));
		
	}

	/* -------------------- pulltorefresh上下监听 -------------------- */
	

	
	/* -------------------- ListView监听 -------------------- */
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Story story = news.getStories().get(position);
		Intent intent = new Intent(context, ContentActivity.class);
		intent.putExtra("id", story.getId());
		context.startActivity(intent);
	}
	
	/* -------------------- ListView监听 -------------------- */

}
