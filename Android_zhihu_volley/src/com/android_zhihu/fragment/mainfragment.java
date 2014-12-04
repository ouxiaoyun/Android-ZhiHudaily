package com.android_zhihu.fragment;

import com.android_zhihu.activity.c.API;
import com.android_zhihu.activity.widget.CombineListandViewpager;
import com.example.android_zhihu_volley.R;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class mainfragment extends Fragment{
	
	private CombineListandViewpager clv;
	
	public static mainfragment newInstance(){
		mainfragment fragment = new mainfragment();
		
		Bundle bundle = new Bundle();
		
		fragment.setArguments(bundle);
		
		return fragment;
	}
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View view = inflater.inflate(R.layout.main_fragment, container, false);
		
		clv = (CombineListandViewpager) view.findViewById(R.id.combinelandv);
		 clv.initData(API.getLatestUrl());
		
		return view;
		
		
	}

}
