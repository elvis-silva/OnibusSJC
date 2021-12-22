package com.nigapps.onibus.sjc;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by elvis on 02/09/17.
 */

public class AppSingleton {

    private static AppSingleton instance;
    private static Context context;
    private RequestQueue requestQueue;
    private ImageLoader imageLoader;

    private AppSingleton(Context pContext) {
        context = pContext;
        requestQueue = getRequestQueue();

        imageLoader = new ImageLoader(requestQueue, new ImageLoader.ImageCache() {

            private final LruCache<String, Bitmap> cache = new LruCache<>(20);
            @Override
            public Bitmap getBitmap(String pUrl) {
                return cache.get(pUrl);
            }

            @Override
            public void putBitmap(String pUrl, Bitmap pBitmap) {
                cache.put(pUrl, pBitmap);
            }
        });
    }

    public static synchronized AppSingleton getInstance(Context pContext) {
        if(instance == null) {
            instance = new AppSingleton(pContext);
        }
        return instance;
    }

    public RequestQueue getRequestQueue() {
        if(requestQueue == null) {
            requestQueue = Volley.newRequestQueue(context.getApplicationContext());
        }
        return requestQueue;
    }

    public <T> void addToRequestQueue(Request<T> pRequest, String pTag) {
        pRequest.setTag(pTag);
        getRequestQueue().add(pRequest);
    }

    public ImageLoader getImageLoader() {
        return imageLoader;
    }

    public void cancelPendingRequest(Object pTag) {
        if(requestQueue != null) {
            requestQueue.cancelAll(pTag);
        }
    }
}