package com.android_zhihu.fragment;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.Volley;
import com.android_zhihu.activity.c.API;
import com.android_zhihu.activity.entity.Editor;
import com.android_zhihu.activity.entity.Story;
import com.android_zhihu.activity.entity.ThemeStory;
import com.android_zhihu.activity.widget.CombineListViewForScrollView;
import com.android_zhihu.cache.BitmapCache;
import com.example.android_zhihu_volley.ContentActivity;
import com.example.android_zhihu_volley.R;

public class SlideContentFragment extends Fragment implements Listener<JSONObject>,OnItemClickListener {

	private long id;
	private String description;
	private String image;

	// viewpager
	private TextView txtv_content_bottom;
	private ScrollView sv_content;
	private NetworkImageView img_content;

	// Volley网络数据
	private RequestQueue queue;

	// Listview
	private CombineListViewForScrollView lv_theme;
	private MyBaseAdapter adapter = null;

	// 数据解析
	private ThemeStory themeStory;

	public static SlideContentFragment newInstance(long id, String description,
			String image) {

		SlideContentFragment fragment = new SlideContentFragment();

		Bundle bundle = new Bundle();
		bundle.putLong("id", id);
		bundle.putString("description", description);
		bundle.putString("image", image);
		fragment.setArguments(bundle);

		return fragment;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		id = getArguments().getLong("id");
		description = getArguments().getString("description");
		image = getArguments().getString("image");
		queue = Volley.newRequestQueue(getActivity());

	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		View view = inflater.inflate(R.layout.content_fragment, container,
				false);

		/*------------------viewpager--------------------------------*/

		txtv_content_bottom = (TextView) view.findViewById(R.id.txtv_content_bottom);
		txtv_content_bottom.setText(description);
		img_content = (NetworkImageView) view.findViewById(R.id.img_content);
		img_content.setImageUrl(image,new ImageLoader(queue, new BitmapCache()));

		/*------------------viewpager--------------------------------*/

		adapter = new MyBaseAdapter();
		lv_theme = (CombineListViewForScrollView) view.findViewById(R.id.lv__content_list);
		lv_theme.setOnItemClickListener(this);
		lv_theme.setAdapter(adapter);
		
		sv_content = (ScrollView) view.findViewById(R.id.sv_content);

		initData();
		

		return view;

	}

	private void initData() {
		queue.add(new JsonObjectRequest(Method.GET, String.format(API.getTheme(), id), null, this, null));
	}

	/*------------------ 数据解析--------------------------------*/
	@Override
	public void onResponse(JSONObject response) {

		themeStory = new ThemeStory();

		try {

			themeStory.setDescription(response.getString("description"));
			themeStory.setBackground(response.getString("background"));
			themeStory.setImage(response.getString("image"));
			themeStory.setColor(response.getInt("color"));
			themeStory.setImage_source(response.getString("image_source"));
			themeStory.setName(response.getString("name"));

			// 解析stories
			// 解析stories
			JSONArray arrayStories = response.getJSONArray("stories");
			if (arrayStories != null && arrayStories.length() > 0) {
				List<Story> stories = new ArrayList<Story>();
				for (int i = 0; i < arrayStories.length(); i++) {
					JSONObject obj = arrayStories.getJSONObject(i);
					Story story = new Story();
					story.setType(obj.getInt("type"));
					story.setId(obj.getLong("id"));
					story.setShare_url(obj.getString("share_url"));
					story.setTitle(obj.getString("title"));

					if (obj.has("multipic")) {
						story.setMultipic(obj.getBoolean("multipic"));
					}

					// 图片数组
					if (obj.has("images")) {
						JSONArray array = obj.getJSONArray("images");
						if (array != null && array.length() > 0) {
							String[] images = new String[array.length()];
							for (int x = 0; x < array.length(); x++) {
								images[x] = array.getString(x);
							}
							story.setImages(images);
						}
					}

					stories.add(story);

				}
				themeStory.setStories(stories);
			}

			// 解析editors
			JSONArray arrayEditors = response.getJSONArray("editors");
			if (arrayEditors != null && arrayEditors.length() > 0) {
				List<Editor> editors = new ArrayList<Editor>();
				for (int i = 0; i < arrayEditors.length(); i++) {
					JSONObject obj = arrayEditors.getJSONObject(i);
					Editor editor = new Editor();
					editor.setAvatar(obj.getString("avatar"));
					editor.setId(obj.getLong("id"));
					editor.setName(obj.getString("name"));
					editors.add(editor);
				}

				themeStory.setEditors(editors);
			}

			// 通知数据发生改变
			adapter.notifyDataSetChanged();

			sv_content.smoothScrollTo(0, 0);

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/*------------------ 数据解析--------------------------------*/

	/*------------------ListView Adapter--------------------------------*/
	class MyBaseAdapter extends BaseAdapter {

		private MyHolder holder;
		
		@Override
		public int getCount() {
			return themeStory == null ? 0 : themeStory.getStories().size();
		}

		@Override
		public Object getItem(int position) {
			return themeStory.getStories().get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			
			
			
				if (convertView == null) {
					
					holder = new MyHolder();

					convertView = LayoutInflater.from(getActivity()).inflate(R.layout.slide_content_adapter, parent, false);

					holder = new MyHolder();
					holder.txtv_slide_title = (TextView) convertView.findViewById(R.id.txtv_slide_title);
					holder.imgv_slide_content = (NetworkImageView) convertView.findViewById(R.id.imgv_slide_content);
					convertView.setTag(holder);
				}
				else {
					holder = (MyHolder) convertView.getTag();
				}
				
			
				
				Story story = themeStory.getStories().get(position);
				holder.txtv_slide_title.setText(story.getTitle());
				holder.imgv_slide_content.setVisibility(View.GONE);
				if (story.getImages() != null && story.getImages().length > 0) {
					holder.imgv_slide_content.setImageUrl(story.getImages()[0], new ImageLoader(queue, new BitmapCache()));
					holder.imgv_slide_content.setVisibility(View.VISIBLE);
				}

			return convertView;
		}

		class MyHolder {

			TextView txtv_slide_title;
			NetworkImageView imgv_slide_content;
		}

	}
	/*------------------ListView Adapter--------------------------------*/
	
	
/* -------------------- ListView监听 -------------------- */
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Story story = themeStory.getStories().get(position);
		Intent intent = new Intent(getActivity(), ContentActivity.class);
		intent.putExtra("id", story.getId());
		getActivity().startActivity(intent);
	}
	
	/* -------------------- ListView监听 -------------------- */

}
