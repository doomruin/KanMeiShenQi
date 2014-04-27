package com.gaoyan.girls;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TableLayout.LayoutParams;
import android.widget.Toast;

import com.gaoyan.Utils.Network;

public class InitActivity extends Activity{
	private ProgressDialog pd;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		ImageView imgview = new ImageView(this);
		imgview.setImageResource(R.drawable.image_1);
		imgview.setLayoutParams(new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		setContentView(imgview);
		pd = new ProgressDialog(this);
		pd.show();
		new AT().execute("");
		
	}
	
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		new AT().execute("");
	}
	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		new AT().execute("");
	}


	private class AT extends AsyncTask<String, Integer, Boolean>{

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub
			return Network.isFastMobileNetwork(InitActivity.this);			
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			if(result){
				startActivity(new Intent(InitActivity.this,MainActivity.class));
				InitActivity.this.finish();
			}else{
				pd.cancel();
				dialog().show();

			}
		}
		
		
	}
	private Dialog dialog(){		
		 //创建builder
        AlertDialog.Builder builder = new AlertDialog.Builder(InitActivity.this);
        builder   //标题
            //.setIcon(R.drawable.ic_launcher)    //icon
            .setCancelable(false)    //不响应back按钮
            .setMessage("您当前的网络状况不佳，建议您使用WIFI或者3G网络")    //对话框显示内容
            //设置按钮
            .setPositiveButton("打开WIFI", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	if(android.os.Build.VERSION.SDK_INT > 10 ){
                	     //3.0以上打开设置界面，也可以直接用ACTION_WIRELESS_SETTINGS打开到wifi界面
                	    startActivity(new Intent(android.provider.Settings.ACTION_SETTINGS));
                	} else {
                	    startActivity(new Intent(android.provider.Settings.ACTION_WIRELESS_SETTINGS));
                	}
                }
            })
            
            .setNegativeButton("进入收藏夹", new DialogInterface.OnClickListener() {                    
                @Override
                public void onClick(DialogInterface dialog, int which) {
                	startActivity(new Intent(InitActivity.this,FavoritesActivity.class));
                	finish();
                }
            });
        //创建Dialog对象
        AlertDialog dlg = builder.create();
        return dlg;
        }
}
