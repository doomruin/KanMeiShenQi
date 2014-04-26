package com.gaoyan.adapters;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.gaoyan.girls.R;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;

public class GirlsImageAdapter extends BaseAdapter{
	List<String> list;
	ImageView iv;
	private LayoutInflater inflator;
	private DisplayImageOptions options;
	public GirlsImageAdapter(Context c,List<String> urls){
		this.list=urls;
		inflator = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.image_default)
		.showImageForEmptyUri(R.drawable.image_default)
		.showImageOnFail(R.drawable.image_default).cacheInMemory()
		
		.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public String getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View view, ViewGroup arg2) {
		// TODO Auto-generated method stub
		
			view=inflator.inflate(R.layout.gridview_item, null);
			iv = (ImageView)view.findViewById(R.id.image_girl);
			
			ImageLoader.getInstance().displayImage(getItem(position), iv, options);			
		return view;
		
	}

}
