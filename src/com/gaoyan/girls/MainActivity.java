package com.gaoyan.girls;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.gaoyan.Utils.Network;
import com.gaoyan.Utils.ParserUtil;
import com.gaoyan.Utils.StaticSettings;
import com.gaoyan.adapters.GirlsImageAdapter;
import com.huewu.pla.lib.MultiColumnListView;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class MainActivity extends Activity {
	
	private MultiColumnListView mv;
	private Button loadMore,changeToJiandan;
	private ArrayList<String> list;
	private int pageNow=0;
	GirlsImageAdapter adapter=null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		
		if(!Network.isFastMobileNetwork(this)){
			
		}
		
		
		
		mv =(MultiColumnListView)findViewById(R.id.gridView);
		loadMore=(Button)findViewById(R.id.button_load_more);
		changeToJiandan=(Button)findViewById(R.id.button_changeActivity);
		//Initialize ImageLoader with configuration  		
		new AT().execute(StaticSettings.urls[0],"0","0");
		
		/*mv.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
				new AT().execute(StaticSettings.urls[0],String.valueOf(pageNow+1),"0");
			}
		});*/
		
		loadMore.setOnClickListener(loadMoreListener);
		changeToJiandan.setOnClickListener(changeActivityListener);
		changeToJiandan.setOnLongClickListener(toFavorite);
		mv.setOnItemClickListener(oc);
		
	}
	OnItemClickListener oc =new OnItemClickListener() {

		@Override
		public void onItemClick(PLA_AdapterView<?> parent, View view,
				int position, long id) {
			// TODO Auto-generated method stub
			String url = douban_img2big_img_url(list.get(position));
			Intent intent = new Intent(MainActivity.this,ImageShower.class);
			intent.putExtra("url", url);
			startActivity(intent);
			
		}
	};
OnClickListener changeActivityListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			startActivity(new Intent(MainActivity.this,JiandanActivity.class));
			MainActivity.this.finish();
		}
	};
	
	OnClickListener loadMoreListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new AT().execute(StaticSettings.urls[0],String.valueOf(pageNow+1),"0");
			loadMore.setVisibility(View.INVISIBLE);
		}
	};
	OnLongClickListener toFavorite = new OnLongClickListener() {
		
		@Override
		public boolean onLongClick(View v) {
			// TODO Auto-generated method stub
			startActivity(new Intent(MainActivity.this,FavoritesActivity.class));
			return false;
		}
	};
	
	Handler hd=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case -1:
				Toast.makeText(MainActivity.this, StaticSettings.IMAGE_LOAD_FAILED, 1).show();
				break;
			case 0:
				adapter = new GirlsImageAdapter(MainActivity.this, list);
				mv.setAdapter(adapter); //第一次
				break;
			case 1:
				
				adapter.notifyDataSetChanged();
				loadMore.setVisibility(View.VISIBLE);
				pageNow++;
				break;//加载更多
			}
		}
		
	};
	private class AT extends AsyncTask<String, Integer, Integer>{

		@Override
		protected Integer doInBackground(String... params) {
			// TODO Auto-generated method stub
			
			String url = params[0];
			String page = params[1];
			String type = params[2];
			Log.i("page", page);
			try {
				if(Integer.parseInt(page)==0){
					list=ParserUtil.getImageURls_douban(url, Integer.parseInt(page));
					
					return 1;
				}else{
					
					list.addAll(ParserUtil.getImageURls_douban(url, Integer.parseInt(page)));
					for (String string : list) {
						Log.i("page", string);
					}
					Log.i("page", list.size()+"");
					return 2;
				}
			}  catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return -1;
			}
				
			
		}

		@Override
		protected void onPostExecute(Integer result) {
			// TODO Auto-generated method stub
			super.onPostExecute(result);
			switch(result){
			case -1: //加载失败
				hd.sendEmptyMessage(-1);
				
				break;
			case 1:
				hd.sendEmptyMessage(0);
				break;
			case 2:
				hd.sendEmptyMessage(1);
				break;
			}
			
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}
		
		
	}
	
	private String douban_img2big_img_url(String small_img_url){
		return small_img_url.replaceAll("/s_p", "/p");
	}
	
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
