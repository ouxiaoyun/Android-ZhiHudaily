package com.android_zhihu.activity.entity;

import java.util.List;

public class Theme {
	private int limit;
	private List<ThemeOther> others;

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public List<ThemeOther> getOthers() {
		return others;
	}

	public void setOthers(List<ThemeOther> others) {
		this.others = others;
	}
}
