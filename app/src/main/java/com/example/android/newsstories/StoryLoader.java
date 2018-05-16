package com.example.android.newsstories;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;

/**
 * Created by Greta Grigutė on 2018-05-10.
 */
public class StoryLoader extends AsyncTaskLoader<List<NewsStory>> {
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
    }

    @Override
    public List<NewsStory> loadInBackground() {
        if (url == null) {
            return null;
        }

        List<NewsStory> newsStories = QueryUtils.fetchNewsData(context, url);

        return newsStories;
    }
}

