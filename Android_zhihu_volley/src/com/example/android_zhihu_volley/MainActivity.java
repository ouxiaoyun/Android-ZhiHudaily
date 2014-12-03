package com.example.android_zhihu_volley;

import android.app.Activity;
import android.os.Bundle;

import com.android_zhihu.activity.c.API;
import com.android_zhihu.activity.widget.CombineListandViewpager;
import com.android_zhihu.activity.widget.CombineTitle;

public class MainActivity extends Activity {

	private CombineTitle cobineTitle;
	private CombineListandViewpager clv;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		getReady();
	}

	private void getReady() {
		
		 cobineTitle = (CombineTitle) findViewById(R.id.cobineTitle);
		 cobineTitle.setText("Ê×Ò³");
		 
		clv = (CombineListandViewpager) findViewById(R.id.combinelandv);
		 clv.initData(API.getLatestUrl());
	}
}
