package com.example.comp_admin.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.util.Log;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {
    /**
     * Tag for log messages
     */
    private static final String LOG_TAG = NewsLoader.class.getSimpleName();
    /**
     * Query URL
     */
    private String mUrl;

    public NewsLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        Log.i(LOG_TAG, "Running onStartLoading() method");
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        Log.i(LOG_TAG, "Running loadInBackground() method");
        if (mUrl == null) {
            return null;
        }
        return Misc.fetchDataToArray(mUrl);
    }
}
