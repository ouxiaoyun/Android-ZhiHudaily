package com.android_zhihu.activity.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.android_zhihu_volley.R;

public class CombineTitle extends FrameLayout{
	
	private TextView txtv_title;

	public CombineTitle(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		LayoutInflater.from(context).inflate(R.layout.item_title, this);
		
		getReady();
		
	}

	private void getReady() {
		
		txtv_title = (TextView) findViewById(R.id.txtv_title);
		
	}
	
	public void setText(String str){
		
		txtv_title.setText(str);
		
	}
	

}
