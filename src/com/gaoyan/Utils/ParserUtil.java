package com.gaoyan.Utils;

import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.nostra13.universalimageloader.core.ImageLoader;

import android.util.Log;

public class ParserUtil {
	
	public static ArrayList<String> getImageURls_douban(String url,int page)throws Exception{
		ArrayList<String> result = new ArrayList<String>();
		Document doc=null;
		if(page!=0){
			url=url+"?p="+(page);
		}
		Log.i("one", url);
		String input="";
		try {
//			HttpGet get = new HttpGet(url);
//			DefaultHttpClient client = new DefaultHttpClient();
//			HttpResponse hr =client.execute(get);
//			if(hr.getStatusLine().getStatusCode()==200){
//				input=EntityUtils.toString(hr.getEntity());
//			}
			
			
			doc = Jsoup.connect(url).userAgent("Mozilla").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Elements images = doc.getElementsByTag("img");
		//System.out.println(images.size());
		for (Element element : images) {
			result.add(element.attr("src"));
			
			//Log.i("lll",element.attr("src"));
		}
		
		return result;
	}
	
	
	public static ArrayList<String> getImageURls_jiandan(String url)throws Exception{//最后一项为下一页的链接
		ArrayList<String> result = new ArrayList<String>();
		Document doc=null;
		try {
			
			doc = Jsoup.connect(url).userAgent("Mozilla").get();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		Elements images = doc.getElementsByTag("img");
		Element nextpage = doc.getElementsByClass("previous-comment-page").get(0);
		//System.out.println(images.size());
		for (Element element : images) {
			result.add(element.attr("src"));
			
			//Log.i("lll",element.attr("src"));
		}
		result.add(nextpage.attr("href"));
		return result;
	}
}
