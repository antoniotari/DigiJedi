package com.antoniotari.android.networking;

import com.android.volley.Cache;
import com.android.volley.Network;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.BasicNetwork;
import com.android.volley.toolbox.DiskBasedCache;
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.antoniotari.android.injection.ApplicationGraph;
import com.antoniotari.android.jedi.Log;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.http.AndroidHttpClient;
import android.os.Build;

import java.io.File;
import java.io.UnsupportedEncodingException;

/**
 * {@link Volley} {@link RequestQueue} manager.
 * <p>
 * TODO: Antonio, please add your magic here...
 */
public class QueueManager {

//    private static RequestQueue mQueue;
//
//    private static RequestQueue getQueue() {
//        if (mQueue == null) {
//            mQueue = Volley.newRequestQueue(ReachAndroidApplication.getInstance());
//        }
//        return mQueue;
//    }
//
//    protected static void addToQueue(final Request request, @Nullable final Object tag) {
//        request.setTag(tag);
//        getQueue().add(request);
//    }
//
//    public static void cancelRequest(final Object requestTag) {
//        getQueue().cancelAll(requestTag);
//    }

    private RequestQueue mRequestQueue;
    private Context mContext;
    private ImageLoader mImageLoader;
    private static final String TAG="com.antoniotari.android.networking.TAG";

    // injecting the contest is a safe way to pass the context to a class
    public QueueManager(Context context) {
        ApplicationGraph.getObjectGraph().injectQueueManager(this);
        mContext = context;
    }

    public static QueueManager getInstance() {
        //QueueManager qm = ApplicationGraph.getObjectGraph().getQueueManager();
        //return DaggerJediComponent.builder().JediModule(new JediModule(null)).build().getQueueManager;
        return null;
    }

    //---------------------------------------------------------
    //-----------------CACHE
    public String loadFromCache(String url) {
        Cache cache = getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        String data = null;
        if (entry != null) {
            try {
                data = new String(entry.data, "UTF-8");
                // handle data, like converting it to xml, json, bitmap etc.,
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        } else {
            // Cached response doesn't exists. Make network call here
        }
        return data;
    }

    public JSONObject loadFromCacheJ(String url) {
        String data = loadFromCache(url);
        JSONObject retJ = null;
        if (data != null) {
            try {
                retJ = new JSONObject(data);
            } catch (JSONException d) {
            }
        }
        return retJ;
    }

    public void invalidateCache(String url) {
        getRequestQueue().getCache().invalidate(url, true);
    }

    public void deleteCache(String url) {
        getRequestQueue().getCache().remove(url);
    }

    public void deleteCache() {
        getRequestQueue().getCache().clear();
    }

    public <T> void turnOffCache(String url, Request<T> req) {
        // String request
        //	StringRequest stringReq = new StringRequest(url);
        // disable cache
        req.setShouldCache(false);
        getRequestQueue().getCache().invalidate(url, true);
    }

    //---------------------------------------------------------
    //-----------------REQUEST

    public void cancelRequests(String... tags) {
        if (tags == null) {
            return;
        }

        for (int i = 0; i < tags.length; i++) {
            if (tags[i] != null) {
                getRequestQueue().cancelAll(tags[i]);
            }
        }
    }

    //public void cancelRequests(Context context)
    //{
    //	getRequestQueue(context).cancelAll();
    //}

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = newRequestQueue(mContext, null);
        }
        return mRequestQueue;
    }

    private static final String DEFAULT_CACHE_DIR = "volley";

    /**
     * Creates a default instance of the worker pool and calls {@link RequestQueue#start()} on it.
     *
     * @param context A {@link Context} to use for creating the cache dir.
     * @param stack   An {@link HttpStack} to use for the network, or null for default.
     *
     * @return A started {@link RequestQueue} instance.
     */
    public static RequestQueue newRequestQueue(Context context, HttpStack stack) {
        File cacheDir = new File(context.getCacheDir(), DEFAULT_CACHE_DIR);

        String userAgent = "volley/0";
        try {
            String packageName = context.getPackageName();
            PackageInfo info = context.getPackageManager().getPackageInfo(packageName, 0);
            userAgent = packageName + "/" + info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
        }

        if (stack == null) {
            if (Build.VERSION.SDK_INT >= 9) {
                stack = new HurlStack();
            } else {
                // Prior to Gingerbread, HttpUrlConnection was unreliable.
                // See: http://android-developers.blogspot.com/2011/09/androids-http-clients.html
                stack = new HttpClientStack(AndroidHttpClient.newInstance(userAgent));
            }
        }

        Network network = new BasicNetwork(stack);
        Log.hi("about to call --initialize");
        RequestQueue queue = new RequestQueue(new SafeDiskCache(cacheDir), network);
        queue.start();

        return queue;
    }

//    public ImageLoader getImageLoader()
//    {
//        getRequestQueue();
//        if (mImageLoader == null)
//            mImageLoader = new ImageLoader(getRequestQueue() ,new VolleyBitmapCache());
//
//        return mImageLoader;
//    }

    public <T> void addToRequestQueue(Request<T> req, Object tag) {
        // set the default tag if tag is empty
        //req.setTag(TextUtils.isEmpty(tag) ? TAG : tag);
        req.setTag(tag);
        getRequestQueue().add(req);
    }

    public <T> void addToRequestQueue(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }

    public void cancelPendingRequests(Object tag) {
        if (mRequestQueue != null) {
            mRequestQueue.cancelAll(tag);
        }
    }

    static class SafeDiskCache extends DiskBasedCache {

        public SafeDiskCache(File rootDirectory) {
            super(rootDirectory);
            // TODO Auto-generated constructor stub
        }

        @Override
        public synchronized void initialize() {
            try {
                super.initialize();
            } catch (Throwable t1) {
                Log.error(t1);
                clear();
                super.initialize();
            }
        }
    }

    public Context getContext() {
        return mContext;
    }
}