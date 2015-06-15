package com.antoniotari.android.networking;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;
import android.text.TextUtils;

import com.android.volley.Cache;
import com.android.volley.Cache.Entry;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.antoniotari.android.util.ATErrorLog;
import com.antoniotari.android.util.image.VolleyBitmapCache;
import com.mokalab.butler.util.Log;
import com.mokalab.butler.volley.ATRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.UnsupportedEncodingException;

public class ATHttp 
{
	private RequestQueue mRequestQueue;
	private ImageLoader mImageLoader;
	private static ATHttp _instance=null;

	public static final String TAG = ATHttp.class.getSimpleName();
	
	private ATHttp(){}
	
	public static ATHttp getInstance()
	{
		if(_instance==null)
			_instance=new ATHttp();
		return _instance;
	}

	//---------------------------------------------------------
	//-----------------CACHE
	public String loadFromCache(Context context, String url)
	{
		Cache cache = getRequestQueue(context).getCache();
		Entry entry = cache.get(url);
		String data=null;
		if(entry != null)
		{
			try {
				data = new String(entry.data, "UTF-8");
				// handle data, like converting it to xml, json, bitmap etc.,
			} catch (UnsupportedEncodingException e) {      
				e.printStackTrace();
			}
		}
		else
		{
			// Cached response doesn't exists. Make network call here
		}
		 return data;
	}
	
	public JSONObject loadFromCacheJ(Context context, String url)
	{
		String data=loadFromCache(context, url);
		JSONObject retJ=null;
		if(data!=null)
		{
			try
			{
				retJ=new JSONObject(data);
			}catch(JSONException d){}
		}
		return retJ;
	}
	
	public void invalidateCache(Context context, String url)
	{
		getRequestQueue(context).getCache().invalidate(url, true);
	}
	
	public void deleteCache(Context context, String url)
	{
		getRequestQueue(context).getCache().remove(url);
	}
	
	public void deleteCache(Context context)
	{
		getRequestQueue(context).getCache().clear();
	}
	
	public <T> void turnOffCache(Context context, String url, Request<T> req)
	{
		// String request
	//	StringRequest stringReq = new StringRequest(url);		 
		// disable cache
		req.setShouldCache(false);
		getRequestQueue(context).getCache().invalidate(url, true);
	}

	//---------------------------------------------------------
	//-----------------REQUEST
	
	public void cancelRequests(Context context, String... tags)
	{
		if(tags==null)
			return;
		
		for(int i=0;i<tags.length;i++)
		{
			if(tags[i]!=null)
				getRequestQueue(context).cancelAll(tags[i]);
		}
	}
	
	//public void cancelRequests(Context context)
	//{
	//	getRequestQueue(context).cancelAll();
	//}
	
	public RequestQueue getRequestQueue(Context context) 
	{
		if (mRequestQueue == null) {
            mRequestQueue = ATRequestQueue.newRequestQueue(context,null);
        }
		return mRequestQueue;
	}

	public ImageLoader getImageLoader(Context context)
	{
		getRequestQueue(context);
		if (mImageLoader == null) 
			mImageLoader = new ImageLoader(getRequestQueue(context) ,new VolleyBitmapCache());

		return mImageLoader;
	}

	public <T> void addToRequestQueue(Context context,Request<T> req, String tag)
	{
		// set the default tag if tag is empty
		req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
		getRequestQueue(context).add(req);
	}

	public <T> void addToRequestQueue(Context context,Request<T> req) 
	{
		req.setTag(TAG);
		getRequestQueue(context).add(req);
	}

	public void cancelPendingRequests(Object tag) 
	{
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}

}
