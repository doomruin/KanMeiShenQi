package com.gaoyan.girls;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


import com.gaoyan.Utils.StorageManager;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;



public class ImageShower extends Activity {
	private ImageView iv;
	private Button like;
	private DisplayImageOptions options;
	private String url;
	private Bitmap bitmapFromImageView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.imageshower);
		iv = (ImageView)findViewById(R.id.imageview_shower);
		like = (Button)findViewById(R.id.button_like);
		like.setOnClickListener(likeListener);
		this.url = getIntent().getStringExtra("url");
		//Log.i("one", url);
		options = new DisplayImageOptions.Builder().cacheInMemory()		
		.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		ImageLoader.getInstance().displayImage(url, iv,options);
		
		
	}

	OnClickListener likeListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			bitmapFromImageView= ((BitmapDrawable)iv.getDrawable()).getBitmap();
			if(bitmapFromImageView!=null){
			new AT().execute("");
			}else{
				Toast.makeText(ImageShower.this, "获取Bitmap失败", 1).show();
			}
		}
	};
	
	
	private class AT extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			StorageManager.saveBitmap(bitmapFromImageView, null);
							
			return "ok";
		}

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if("ok".equals(result)){
				Toast.makeText(ImageShower.this, "已添加至收藏夹", 1).show();
			}
		}
		
	}
	
	
	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// TODO Auto-generated method stub
		finish();
		return true;
	}
	
	

}
