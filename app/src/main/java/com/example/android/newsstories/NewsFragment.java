package com.example.android.newsstories;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Greta Grigutė on 2018-05-09.
 */
public class NewsFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsStory>>,
        SharedPreferences.OnSharedPreferenceChangeListener, StoryAdapter.ListItemClickListener {
    private static final String NEWS_ARRAY_lIST = "newsArrayList";
    private static final String BUNDLE_RECYCLER_LAYOUT = "NewsFragment.recycler.layout";
    private static final int NEWS_LOADER_ID = 1;
    boolean isWifiConn;
    boolean isMobileConn;
    //please add the API key in the gradle.properties like this:
    //NewsStories_GuardianApiKey="your-key"
    private String GUARDIAN_REQUEST_URL;
    private RecyclerView recyclerView;
    private StoryAdapter adapter;
    private RelativeLayout empty;
    private RelativeLayout noInternet;
    private ProgressBar loadingSpinner;
    private String size;
    private SharedPreferences sharedPreferences;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean starred = false;
    private String buildedUrl;
    private boolean selectedToShowCategory = false;
    Parcelable savedRecyclerLayoutState;
    List <NewsStory> newsStories;
    Bundle savedInstance = null;


    public NewsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        savedInstance = savedInstanceState;
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        if (savedInstanceState != null) {
            // Restore value of newsStories from saved state
            newsStories = savedInstanceState.getParcelableArrayList(NEWS_ARRAY_lIST);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);

        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getLoaderManager().restartLoader(NEWS_LOADER_ID, null, NewsFragment.this);
            }
        });

        GUARDIAN_REQUEST_URL = getArguments().getString(Constants.URL_KEY);
        recyclerView = rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        // Find a reference to the loading spinner{@link ProgressBar} in the layout
        loadingSpinner = rootView.findViewById(R.id.loading_spinner);

        // Create a new {@link ArrayAdapter} of stories
        adapter = new StoryAdapter(getActivity(), new ArrayList<NewsStory>(), this);

        // Set the adapter on the {@link ListView}
        // so the list can be populated in the user interface
        recyclerView.setAdapter(adapter);
        // Find a reference to the no stories layout {@link RelativeLayout} in the layout
        empty = rootView.findViewById(R.id.no_stories_layout);
        noInternet = rootView.findViewById(R.id.no_internet_layout);
        recyclerView.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);

        if (!isInternetConn()) {
            loadingSpinner.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        } else {
            // add users chosen number of news stories
            size = sharedPreferences.getString(getString(R.string.pref_number_key), getString(R.string.pref_number_default_value));
            if (savedInstance == null){
            getLoaderManager().initLoader(NEWS_LOADER_ID, null, this);}
            else {adapter = new StoryAdapter(getActivity(), newsStories, this);
                loadingSpinner.setVisibility(View.GONE);
                empty.setVisibility(View.GONE);
                recyclerView.setVisibility(View.VISIBLE);
                recyclerView.setAdapter(adapter);
                swipeRefreshLayout.setRefreshing(false);
                recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);}
        }
        noInternet.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);

        return rootView;
    }

    @Override
    public void onListItemClick(View view, int position) {
        switch (view.getId()) {
            case R.id.cardview:
                String url = adapter.getItem(position).getUrl();
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                }
                break;
            case R.id.favorite:
                ImageView star = view.findViewById(R.id.favorite);

                if (!starred) {
                    star.setImageResource(R.drawable.yellow_star_full);
                    starred = true;
                    adapter.getItem(position).setStarred(starred);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("Category_key", adapter.getItem(position).getCategory());
                    editor.putString("Author_key", adapter.getItem(position).getAuthor());
                    editor.putString("Title_key", adapter.getItem(position).getTitle());
                    editor.putString("Date_key", adapter.getItem(position).getDate());
                    editor.putString("Picture_key", adapter.getItem(position).getPicture());
                    editor.putString("Url_key", adapter.getItem(position).getUrl());
                    editor.putBoolean(getString(R.string.favorite_key), starred);
                    editor.putString("Category_url_key", buildedUrl);
                    editor.commit();
                } else {
                    star.setImageResource(R.drawable.yellow_star_empty);
                    starred = false;
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(getString(R.string.favorite_key), starred);
                    editor.putString("Title_key", adapter.getItem(position).getTitle());
                    adapter.getItem(position).setStarred(starred);
                    editor.commit();
                }

                break;
            case R.id.category:
                selectedToShowCategory = true;
                String category = adapter.getItem(position).getCategory().toLowerCase().replaceAll(" ", "-");
                if (category.equals("art-and-design") || category.equals("commentis-free") || category.equals("jobs-advice") || category.equals("life-and-style") || category.equals("the-guardian") || category.equals("the-observer")) {
                    category = adapter.getItem(position).getCategory().toLowerCase().replaceAll(" ", "");
                }
                if (category.equals("world-news")) {
                    category = "world";
                    Log.d("NewsFragment","category is: "+category);
                }
                if (category.equals("television-&-radio")) {
                    category = "tv-and-radio";
                    Log.d("NewsFragment","category is: "+category);
                }
                // parse breaks apart the URI string that's passed into its parameter
                Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
                // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
                Uri.Builder uriBuilder = baseUri.buildUpon();
                uriBuilder.appendQueryParameter(Constants.SECTION, category);
                buildedUrl = uriBuilder.toString();
                getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
                Log.d("NewsFragment", "Onclick works, string is: " + category);
                break;

            default:
                url = adapter.getItem(position).getUrl();
                if (url != null) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse(url));
                    startActivity(intent);
                }
                break;
        }
    }

    @Override
    public Loader<List<NewsStory>> onCreateLoader(int id, Bundle args) {
        // parse breaks apart the URI string that's passed into its parameter
        Log.d("NewsFragment", "on create loader gets called");
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `page-size=10`
        uriBuilder.appendQueryParameter(Constants.PAGE_SIZE, size);
        if (!selectedToShowCategory) {
            buildedUrl = uriBuilder.toString();
        }
        return new StoryLoader(getActivity(), buildedUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> newsStories) {
        if (savedInstance != null) {
            // Restore value of newsStories from saved state
            newsStories = savedInstance.getParcelableArrayList(NEWS_ARRAY_lIST);

        }
        this.newsStories = newsStories;
       // Clear the adapter of previous news data
        adapter = new StoryAdapter(getActivity(), new ArrayList<NewsStory>(), this);
        // If there is a valid list of {@link Earthquake}s, then add them to the adapter's
        // data set. This will trigger the ListView to update.
        if (newsStories != null && !newsStories.isEmpty()) {
            adapter = new StoryAdapter(getActivity(), newsStories, this);
            loadingSpinner.setVisibility(View.GONE);
            empty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setAdapter(adapter);
            swipeRefreshLayout.setRefreshing(false);
         recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);

        } else {
            if (!isInternetConn()) {
                recyclerView.setVisibility(View.GONE);
                loadingSpinner.setVisibility(View.GONE);
                noInternet.setVisibility(View.VISIBLE);
            } else {
                empty.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                loadingSpinner.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onLoaderReset(Loader<List<NewsStory>> loader) {
        adapter.setNews(new ArrayList<NewsStory>());
    }

    private boolean isInternetConn() {
        //Check the device network connection
        ConnectivityManager connManager = (ConnectivityManager) getContext().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        isWifiConn = networkInfo.isConnected();
        networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        isMobileConn = networkInfo.isConnected();
        boolean isConnected = false;
        if (isWifiConn || isMobileConn) {
            isConnected = true;
        }
        return isConnected;
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.pref_number_key))) {
            loadNumberOfNewsFromSharedPreferences(sharedPreferences);
            Log.d("Newsfragment", "onsharedpreferences change listener gets called");
        }
    }

    private void loadNumberOfNewsFromSharedPreferences(SharedPreferences sharedPreferences) {
        size = sharedPreferences.getString(getString(R.string.pref_number_key), getString(R.string.pref_number_default_value));
        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        // get shared preferences and unregister shared preferences change listener
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    /**
     * This is a method for Fragment.
     * You can do the same in onCreate or onRestoreInstanceState
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        savedInstance = savedInstanceState;
        if(savedInstanceState != null)
        {Log.d("NewsFragment", "on view state restored gets called");
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            newsStories = savedInstanceState.getParcelableArrayList(NEWS_ARRAY_lIST);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        ArrayList <NewsStory> savedArrayList = (ArrayList<NewsStory>) newsStories;
        outState.putParcelableArrayList(NEWS_ARRAY_lIST,savedArrayList);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
        super.onSaveInstanceState(outState);
    }
}

