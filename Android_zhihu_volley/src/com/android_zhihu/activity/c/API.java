package com.android_zhihu.activity.c;

public class API {
	
	static {
		System.loadLibrary("api");
	}
	
	/**
	 * ����
	 * @return
	 */
	public static native String getThemesUrl();
	
	/**
	 * ��ӭͼƬ
	 * @return
	 */
	public static native String getStartImageUrl();
	
	/**
	 * ��������
	 * @return
	 */
	public static native String getLatestUrl();
	
	/**
	 * ��ʷ����<br />
	 * ���ص��ַ�����Ҫʹ��String.format();������ʽ��<br />
	 * ���磺String.format(API.getBefore(), "20141201");
	 * @return
	 */
	public static native String getBefore();

}
