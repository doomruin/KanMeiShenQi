package com.gaoyan.girls;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.gaoyan.Utils.ParserUtil;
import com.gaoyan.Utils.StaticSettings;
import com.gaoyan.adapters.GirlsImageAdapter;
import com.huewu.pla.lib.internal.PLA_AdapterView;
import com.huewu.pla.lib.internal.PLA_AdapterView.OnItemClickListener;

public class JiandanActivity extends Activity{
	private ListView mv;
	private Button loadMore;
	private ArrayList<String> list;
	private int pageNow=0;
	GirlsImageAdapter adapter=null;
	private String nextpageUrl=null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.jiandan_activity);
		
		mv =(ListView)findViewById(R.id.gridView);
		loadMore=(Button)findViewById(R.id.button_load_more);
		//Initialize ImageLoader with configuration  		
		new AT().execute(StaticSettings.urls[1],"0","0");
		
		/*mv.setOnLoadMoreListener(new OnLoadMoreListener() {
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
				new AT().execute(StaticSettings.urls[0],String.valueOf(pageNow+1),"0");
			}
		});*/
		
		loadMore.setOnClickListener(loadMoreListener);
		mv.setOnItemClickListener(oc);
		
	}
	AdapterView.OnItemClickListener oc =new  AdapterView.OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View arg1, int position,
				long arg3) {
			// TODO Auto-generated method stub
			String url = list.get(position);
			Intent intent = new Intent(JiandanActivity.this,ImageShower.class);
			intent.putExtra("url", url);
			startActivity(intent);
		}
	};
	
	OnClickListener loadMoreListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			new AT().execute(nextpageUrl,String.valueOf(pageNow+1),"0");
			loadMore.setVisibility(View.INVISIBLE);
		}
	};
	
	Handler hd=new Handler(){

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch(msg.what){
			case -1:
				Toast.makeText(JiandanActivity.this, StaticSettings.IMAGE_LOAD_FAILED, 1).show();
				break;
			case 0:
				adapter = new GirlsImageAdapter(JiandanActivity.this, list);
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
			String page =params[1];
			
			try {
				ArrayList<String> templist=ParserUtil.getImageURls_jiandan(url);
				nextpageUrl = templist.remove(templist.size()-1);
				if(Integer.parseInt(page)==0){
					
					list=templist;
					return 1;
				}else{
					
					
					list.addAll(templist);
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
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode==KeyEvent.KEYCODE_BACK){
			startActivity(new Intent(JiandanActivity.this,MainActivity.class));
			finish();
		}
		return false;
		
	}
	
	
	

}
