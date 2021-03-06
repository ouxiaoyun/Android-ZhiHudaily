package com.android_zhihu.activity.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

public class CombineListViewForScrollView extends ListView{

	public CombineListViewForScrollView(Context context) {
		super(context);
	}

	public CombineListViewForScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CombineListViewForScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2, MeasureSpec.AT_MOST);
		super.onMeasure(widthMeasureSpec, expandSpec);
	}
}
