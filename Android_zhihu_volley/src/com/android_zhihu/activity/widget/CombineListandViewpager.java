package com.android_zhihu.activity.widget;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.android_zhihu.activity.entity.Latest;
import com.android_zhihu.activity.entity.Story;
import com.android_zhihu.activity.entity.TopStory;
import com.android_zhihu.cache.BitmapCache;
import com.android_zhihu.utils.NetHelper;
import com.example.android_zhihu_volley.R;

public class CombineListandViewpager extends FrameLayout {

	private Context context;
	private ViewPager pager_home;
	private TextView txtv_viewpager_title;
	private LinearLayout index_viewpager;
	private ListView lv_list;
	private Latest mlatest;
	private List<ImageView> imageViews;
	private ScrollView sv_home;
	private RequestQueue queue;
	

	public CombineListandViewpager(Context context, AttributeSet attrs) {
		super(context, attrs);

		this.context = context;

		LayoutInflater.from(context).inflate(R.layout.item_home, this);
		
		getReady();
		

	}

	// 数据准备
	private void getReady() {

		pager_home = (ViewPager) findViewById(R.id.pager_home);
		txtv_viewpager_title = (TextView) findViewById(R.id.txtv_viewpager_title);
		index_viewpager = (LinearLayout) findViewById(R.id.index_viewpager);
		lv_list = (ListView) findViewById(R.id.lv_list);
		sv_home = (ScrollView) findViewById(R.id.sv_home);

	}

	// 初始化数据
	public void initData(final String uri) {
		
		

		queue = Volley.newRequestQueue(context);

		queue.add(new JsonObjectRequest(uri, null, new Listener<JSONObject>() {
			
			Latest latest = null;
			List<Story> stories = null;
			List<TopStory> topStories = null;

			@Override
			public void onResponse(JSONObject arg0) {
				
				
				

				latest = new Latest();

				try {

					String dateData = arg0.getString("date");
					
					System.out.println(dateData+"=============");

					latest.setDate(dateData);

					// Story

					JSONArray jsonArray = arg0.getJSONArray("stories");

					stories = new ArrayList<Story>();

					Story story;
					for (int i = 0; i < jsonArray.length(); i++) {
						story = new Story();
						JSONObject object = (JSONObject) jsonArray.get(i);

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

					// TopStory

					JSONArray array_top_stories = arg0
							.getJSONArray("top_stories");

					topStories = new ArrayList<TopStory>();

					if (array_top_stories != null
							&& array_top_stories.length() > 0) {

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
					}

					latest.setStories(stories);
					latest.setTopStories(topStories);
					
					 mlatest = latest;
					 
					// 初始化换灯
					initSlide();

					//初始化ListView

					initListView();
					 
					 System.out.println(latest+"====================");
					 
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}
			
			
			private void initListView() {
				
				MyListAdapter adapter = new MyListAdapter();
				
				lv_list.setAdapter(adapter);
				
				// 将ScrollView置顶
				sv_home.smoothScrollTo(0, 0);

			}
			
			
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
			
			
			private void initSlide() {
				
				imageViews = new ArrayList<ImageView>();

				for (int i = 0; i < latest.getTopStories().size(); i++) {
					TopStory topStory = latest.getTopStories().get(i);
					NetworkImageView imageView = new NetworkImageView(context);
					imageView.setScaleType(ScaleType.CENTER_CROP);
					imageView.setImageUrl(topStory.getImage(),new ImageLoader(queue, new BitmapCache()));
					imageViews.add(imageView);
				}

				MyPagerAdapter adapter = new MyPagerAdapter();
				
				pager_home.setAdapter(adapter);

				Timer timer = new Timer();

				timer.schedule(new TimerTask() {

					@Override
					public void run() {

						handler.sendEmptyMessage(0);

					}
				}, 2000, 3000);

				// 初始化小圆点
				initSmallDot(0);

				// 初始化标题
				txtv_viewpager_title.setText(latest.getTopStories().get(0).getTitle());
				
				
				pager_home.setOnPageChangeListener(new OnPageChangeListener() {
					
					@Override
					public void onPageSelected(int arg0) {
						
						initSmallDot(arg0);
						
						item = arg0;
						
						txtv_viewpager_title.setText(latest.getTopStories().get(arg0).getTitle());
						
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
			
			private void initSmallDot(int i) {

				index_viewpager.removeAllViews();

				for (int j = 0; j < imageViews.size(); j++) {

					ImageView imageView = new ImageView(context);
					imageView.setImageResource(R.drawable.dot_default);
					imageView.setPadding(5, 0, 5, 0);

					index_viewpager.addView(imageView);
				}

				// 设置选中
				((ImageView) index_viewpager.getChildAt(i))
						.setImageResource(R.drawable.dot_selected);

			}
			

		}, null));
	}

	
	class MyPagerAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return imageViews.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
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
	
	
	class MyListAdapter extends BaseAdapter{

		private MyHolder holder;

		@Override
		public int getCount() {
			return mlatest.getStories().size();
		}

		@Override
		public Object getItem(int position) {
			return mlatest.getStories().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(R.layout.item_list_zhihu, parent, false);
				
				holder = new MyHolder();
				holder.txtv_home_title = (TextView) convertView.findViewById(R.id.txtv_home_title);
				holder.imgv_content = (NetworkImageView) convertView.findViewById(R.id.imgv_content);
				
				convertView.setTag(holder);
			} else {
				holder = (MyHolder) convertView.getTag();
			}
			
			Story story = mlatest.getStories().get(position);
			holder.txtv_home_title.setText(story.getTitle());
			holder.imgv_content.setImageUrl(story.getImages()[0], new ImageLoader(queue, new BitmapCache()));
			
			return convertView;
		}
		
		class MyHolder {
			public TextView txtv_home_title;
			public NetworkImageView imgv_content;
		}
		
	}


}
