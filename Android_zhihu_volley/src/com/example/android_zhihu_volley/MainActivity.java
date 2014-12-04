package com.example.android_zhihu_volley;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.android_zhihu.activity.c.API;
import com.android_zhihu.activity.entity.Theme;
import com.android_zhihu.activity.entity.ThemeOther;
import com.android_zhihu.activity.widget.CombineTitle;
import com.android_zhihu.fragment.SlideContentFragment;
import com.android_zhihu.fragment.mainfragment;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class MainActivity extends FragmentActivity implements OnItemClickListener,Listener<JSONObject>{

	
	//自定义首页控件
	private CombineTitle cobineTitle;

	// 侧滑控件
	private SlidingMenu menu;
	private ListView lvTheme;
	private Theme theme;
	private MyBaseAdapter adapter;
	
	//Volley解析
	private RequestQueue queue;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		getReady();
		
		
		//初始化侧滑菜单
		initSlideMenu();
	}

	

	/*---------------数据准备-----------------------------*/
	
	private void getReady() {

		cobineTitle = (CombineTitle) findViewById(R.id.cobineTitle);
		cobineTitle.setText("首页");
		
		cobineTitle.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				menu.toggle();
			}
		});

		
		FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

		fragmentTransaction.replace(R.id.fl_content, mainfragment.newInstance());

		fragmentTransaction.commit();
		
		
	}
	
	/*---------------数据准备-----------------------------*/
	
	
	/*---------------侧滑菜单-----------------------------*/
	private void initSlideMenu() {
		
		
		
		
		//初始化菜单
		menu = new SlidingMenu(this);
		menu.setMode(SlidingMenu.LEFT);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setShadowWidth(10);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffset(100);
        menu.setFadeDegree(0.35f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.sliding_left);
        
        
        //主题设置
        adapter = new MyBaseAdapter();
        lvTheme = (ListView) findViewById(R.id.lv_theme);
        lvTheme.setAdapter(adapter);
        lvTheme.setOnItemClickListener(this);
        
        queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(new JsonObjectRequest(Method.GET, API.getThemesUrl(),null, this, null));
        
        
	}
	/*---------------侧滑菜单-----------------------------*/
	
	
	/*---------------Volley Json解析-----------------------------*/
	@Override
	public void onResponse(JSONObject response) {
		
		theme = new Theme();
		
		try {
			
			
			theme.setLimit(response.getInt("limit"));
			
			JSONArray jsonArray = response.getJSONArray("others");
			
			if(jsonArray!=null && jsonArray.length()>0){
				
				List<ThemeOther> others = new ArrayList<ThemeOther>();
				
				//单独设置首页
				ThemeOther other = new ThemeOther();
				other.setName("首页");
				
				for(int i=0; i<jsonArray.length(); i++){
					other = new ThemeOther();
					
					JSONObject object = (JSONObject) jsonArray.get(i);
					
					other.setColor(object.getInt("color"));
					other.setDescription(object.getString("description"));
					other.setId(object.getLong("id"));
					other.setImage(object.getString("image"));
					other.setName(object.getString("name"));
					
					others.add(other);
				}
				
				theme.setOthers(others);
				
				//数据更新
				adapter.notifyDataSetChanged();
				
			}
			
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
	}
	/*---------------Volley Json解析-----------------------------*/
	
	/*---------------侧滑主题adapter-----------------------------*/
	class MyBaseAdapter extends BaseAdapter{
		
		
		MyHodler holder;

		@Override
		public int getCount() {
			return theme == null ? 0 : theme.getOthers().size();
		}

		@Override
		public Object getItem(int position) {
			return theme.getOthers().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			
			
			if(convertView == null){
				
				holder = new MyHodler();
				
				convertView = LayoutInflater.from(MainActivity.this).inflate(R.layout.theme_list, parent,false);
				
				holder.txtv_theme_list = (TextView) convertView.findViewById(R.id.txtv_theme_list);
				
				convertView.setTag(holder);
			}
			else{
				holder = (MyHodler) convertView.getTag();
			}
			
			holder.txtv_theme_list.setText(theme.getOthers().get(position).getName());
			
			
			return convertView;
		}
		
		
		class MyHodler{
			TextView txtv_theme_list;
		}
		
	}
	/*---------------侧滑主题adapter-----------------------------*/
	
	
	/*---------------侧滑ListView监听-----------------------------*/
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		
		//关闭侧滑菜单
		menu.toggle();
		
		if(position == 0){
			getReady();
		}
		else{
			
			ThemeOther other = theme.getOthers().get(position);
			cobineTitle.setText(other.getName());
			
			FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.fl_content, SlideContentFragment.newInstance(other.getId(), other.getDescription(), other.getImage()));
			fragmentTransaction.commit();
		}
	}
	/*---------------侧滑ListView监听-----------------------------*/


	
}
