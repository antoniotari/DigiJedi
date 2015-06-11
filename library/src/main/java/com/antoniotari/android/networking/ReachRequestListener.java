package com.antoniotari.android.networking;

import com.android.volley.Response;

/**
 * Created by antonio on 02/02/15.
 */
public interface ReachRequestListener<T> extends Response.Listener<T>{
    void onCacheResponse(T response);
}
