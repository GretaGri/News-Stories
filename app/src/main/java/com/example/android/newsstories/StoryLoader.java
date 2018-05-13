package com.example.android.newsstories;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;

import java.util.List;

/**
 * Created by Greta Grigutė on 2018-05-10.
 */
public class StoryLoader extends AsyncTaskLoader<List<NewsStory>> {
    private static final String LOG_TAG = MainActivity.class.getName();
    private String url;
    private Context context;

    public StoryLoader(Context context, String url) {
        super(context);
        this.context = context;
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
        Log.d(LOG_TAG, "start Loading");
    }

    @Override
    public List<NewsStory> loadInBackground() {
        Log.d(LOG_TAG, "load in background");
        if (url == null) {
            return null;
        }

        List<NewsStory> newsStories = QueryUtils.fetchNewsData(context,url);

        return newsStories;
    }
}

