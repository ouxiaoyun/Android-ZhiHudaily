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
	
	
	/**
	 * �����б�<br />
	 * ���ص��ַ�����Ҫʹ��String.format();������ʽ��<br />
	 * ���磺String.format(API.getTheme(), "11");
	 * @return
	 */
	public static native String getTheme();
	
	
	/**
	 * ����-����
	 * ���ص��ַ�����Ҫʹ��String.format();������ʽ��<br />
	 * ���磺String.format(API.getStoryExtra(), "4354228");
	 * @return
	 */
	public static native String getStoryExtra();
	
	/**
	 * ����
	 * ���ص��ַ�����Ҫʹ��String.format();������ʽ��<br />
	 * ���磺String.format(API.getStory(), "4354228");
	 * @return
	 */
	public static native String getStory();

}
