package com.android_zhihu.utils;

import java.io.IOException;
import java.io.InputStream;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class NetHelper {

	// 网络请求

	public static HttpEntity getRequest(String uri) {

		HttpEntity httpEntity = null;

		try {
			HttpClient httpclient = new DefaultHttpClient();

			HttpParams httpParams = httpclient.getParams();

			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);

			// get方式
			HttpGet get = new HttpGet(uri);
			HttpResponse httpResponse = httpclient.execute(get);

			if (httpResponse != null
					&& httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {

				httpEntity = httpResponse.getEntity();

			}

			return httpEntity;

		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	// 获得json字符串

	public static String getJsonData(String uri) {

		String str = null;

		HttpEntity httpEntity = getRequest(uri);

		try {
			if (httpEntity != null) {
				str = EntityUtils.toString(httpEntity, "UTF-8");
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return str;

	}
	
	
	//获得bitmap
	
	public static Bitmap getBitmap(String uri){
		
		Bitmap bitmap = null;
		
		HttpEntity httpEntity = getRequest(uri);
		
		try {
			if (httpEntity != null) {
				
				InputStream is = httpEntity.getContent();
				bitmap = BitmapFactory.decodeStream(is);
			}

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return bitmap;

		
	}

}
