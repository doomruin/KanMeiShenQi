package com.gaoyan.girls;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.gaoyan.Utils.Const;
import com.gaoyan.Utils.StaticSettings;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;


public class FavoritesActivity extends Activity implements Const{
	private static final String STATE_POSITION = "STATE_POSITION";
	private DisplayImageOptions options;
	private ViewPager pager;
	private ImageButton delete; 
	private int pagerPosition = 0;
	private ArrayList<String> imageNames;
	private ImagePagerAdapter adapter;
	private Handler hd = new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case 0://加载成功
				adapter = new ImagePagerAdapter(imageNames);
				pager.setAdapter(adapter);
				pager.setCurrentItem(pagerPosition);
				break;
			case -1://文件夹内无图片
				Toast.makeText(FavoritesActivity.this, StaticSettings.IMAGE_LOAD_FAVORITE_FAILED, 1)
				.show();
				break;
			case 10:
				Toast.makeText(FavoritesActivity.this, "删除成功", 1).show();
				adapter.notifyDataSetChanged();
				break;
			case 11:
				Toast.makeText(FavoritesActivity.this, "删除失败", 1).show();
				break;
			}
		}
		
	};

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imagepager);
		Bundle bundle = getIntent().getExtras();
		assert bundle != null;
		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}
		pager = (ViewPager) findViewById(R.id.viewpager_favorites);
		new AT_getFavoriteImages().execute("");//开始加载图片
		options = new DisplayImageOptions.Builder()
			.showImageForEmptyUri(R.drawable.image_1)
			.showImageOnFail(R.drawable.image_1)
			.bitmapConfig(Bitmap.Config.RGB_565)
			.displayer(new FadeInBitmapDisplayer(300))
			.build();
		
		delete = (ImageButton)findViewById(R.id.button_delete_this_favorite);
		delete.setOnClickListener(deleteListener);
	}
	
	OnClickListener deleteListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			int index = pager.getCurrentItem();
			if(delereFavorite(PATH_IMAGE+"/"+imageNames.get(index))){
				imageNames.remove(index);//删除图片名称，便于刷新adaper
				hd.sendEmptyMessage(10);//删除成功
			}else{
				hd.sendEmptyMessage(11);
			}			
		}
	};
	
	
	
	

	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		
		private ArrayList<String> images;
		
		private LayoutInflater inflater;

		ImagePagerAdapter(ArrayList<String> imageNames) {
			this.images = imageNames;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View) object);
		}

		@Override
		public int getCount() {
			return images.size();
		}

		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			View imageLayout = inflater.inflate(R.layout.viewpager_item_favorites, view, false);
			assert imageLayout != null;
			ImageView imageView = (ImageView) imageLayout.findViewById(R.id.imageView_viewpageritem);
			ImageLoader.getInstance().displayImage("file://"+PATH_IMAGE+"/"+images.get(position), imageView, options);
			view.addView(imageLayout);
			return imageLayout;
			}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}
	}
	
	
	private class AT_getFavoriteImages extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			imageNames = (ArrayList)getFavoriteImages();
			if(imageNames!=null){
				return "ok";
			}else{
				return "empty";
			}
			
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub

			if("ok".equals(result)){
				hd.sendEmptyMessage(0);//成功
			}else{
				hd.sendEmptyMessage(-1);
			}
		}		
	}
	
	
	private List<String> getFavoriteImages(){
		List<String> result=new ArrayList<String>();
		File file = new File(PATH_IMAGE);
		if(!file.exists()){
			file.mkdirs();
		}
		
		//Log.i("file", file.list().toString());
		if(file.list().length  > 0){
		for(String item : file.list()){
			result.add(item);
		}
		return result;
		}else{
			return null;
		}
	}
	private boolean  delereFavorite(String filePath_name){
		File file = new File(filePath_name);
		Log.i("file", filePath_name);
		return file.delete();
	}
}
