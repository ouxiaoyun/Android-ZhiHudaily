package com.example.android_zhihu_volley;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.android_zhihu.activity.c.API;
import com.android_zhihu.cache.BitmapCache;

public class WelComeActivity extends Activity implements AnimationListener{
	
	private static final String TAG = "WelComeActivity";
	
	private NetworkImageView imgv_welcom;
	private ImageView img_splash;
	private RequestQueue queue;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.welcom);
		imgv_welcom = (NetworkImageView) findViewById(R.id.imgv_welcom);
		img_splash = (ImageView) findViewById(R.id.img_splash);
		
		initData();
		
		
	}

	private void initData() {
		
		 queue = Volley.newRequestQueue(getApplicationContext());
		
		 queue.add(new JsonObjectRequest(Method.GET, API.getStartImageUrl(), null	, new Listener<JSONObject>() {

			@Override
			public void onResponse(JSONObject arg0) {
				
				android.util.Log.i(TAG, arg0.toString());
				
				String picPath;
				try {
					
					picPath = arg0.getString("img");
					
					imgv_welcom.setImageUrl(picPath, new ImageLoader(queue, new BitmapCache()));
					
				} catch (JSONException e) {
					e.printStackTrace();
				}
			}
		}, null));
		 
		//图片动画监听
			Animation animation = new ScaleAnimation(1.0f, 1.2f, 1.0f, 1.2f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
			animation.setDuration(2000); // 动画持续时间
			animation.setFillAfter(true); // 动画结束后停留在结束的位置
			animation.setAnimationListener(this); // 添加动画监听
			imgv_welcom.startAnimation(animation); // 启动动画
	}

	@Override
	public void onAnimationStart(Animation animation) {
		
	}

	@Override
	public void onAnimationEnd(Animation animation) {
		
		startActivity(new Intent(WelComeActivity.this, MainActivity.class));
		
	}

	@Override
	public void onAnimationRepeat(Animation animation) {
		
	}

}
