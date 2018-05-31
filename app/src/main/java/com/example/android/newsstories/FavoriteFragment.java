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
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<NewsStory>>,
        StoryAdapter.ListItemClickListener {
    private static final String BUNDLE_RECYCLER_LAYOUT = "FavoriteFragment.recycler.layout";
    private static final int NEWS_LOADER_ID = 1;
    boolean isWifiConn;
    boolean isMobileConn;
    private RecyclerView recyclerView;
    private StoryAdapter adapter;
    private boolean selectedToShowCategory = false;
    private ArrayList<NewsStory> favorite = new ArrayList<>();
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar loadingSpinner;
    private RelativeLayout empty;
    private RelativeLayout noInternet;
    private String GUARDIAN_REQUEST_URL;
    private String buildedUrl;
    private Parcelable savedRecyclerLayoutState;

    public FavoriteFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment, container, false);
        favorite = getArguments().getParcelableArrayList("Favorite_array_key");

        GUARDIAN_REQUEST_URL = getArguments().getString("Category_url_key");

        recyclerView = rootView.findViewById(R.id.recycler_view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());

        empty = rootView.findViewById(R.id.no_stories_layout);
        noInternet = rootView.findViewById(R.id.no_internet_layout);
        // Find a reference to the loading spinner{@link ProgressBar} in the layout
        loadingSpinner = rootView.findViewById(R.id.loading_spinner);
        swipeRefreshLayout = rootView.findViewById(R.id.swipe_refresh);

Log.d("FavoriteFragment", "Array list size is : " + favorite.size());

        if (favorite.size()>0){
        // Create a new {@link ArrayAdapter} of stories
        adapter = new StoryAdapter(getActivity(), favorite, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setVisibility(View.VISIBLE);
            empty.setVisibility(View.GONE);
        loadingSpinner.setVisibility(View.GONE);
        swipeRefreshLayout.setEnabled(false);
        }
        else if (selectedToShowCategory){

            swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    getLoaderManager().restartLoader(NEWS_LOADER_ID, null, FavoriteFragment.this);
                }
            });

            if (!isInternetConn()) {
            loadingSpinner.setVisibility(View.GONE);
            noInternet.setVisibility(View.VISIBLE);
        }
        noInternet.setVisibility(View.GONE);
        empty.setVisibility(View.GONE);
        recyclerView.setVisibility(View.VISIBLE);}
        else {
        recyclerView.setVisibility(View.GONE);
        empty.setVisibility(View.VISIBLE);
        loadingSpinner.setVisibility(View.GONE);}

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
                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putBoolean(getString(R.string.favorite_key),false);
                    editor.commit();

                favorite.remove(favorite.get(position));
                adapter.notifyDataSetChanged();
                break;

        case R.id.category:
        selectedToShowCategory = true;
        String category = adapter.getItem(position).getCategory().toLowerCase().replaceAll(" ","-");
        if (category.equals("art-and-design")||category.equals("commentis-free")||category.equals("jobs-advice")||category.equals("life-and-style")||category.equals("the-guardian")||category.equals("the-observer"))
        {category = adapter.getItem(position).getCategory().toLowerCase().replaceAll(" ","");}
        if (category.equals("world-news")){
            category = adapter.getItem(position).getCategory().toLowerCase().replaceAll("-news","");
        }
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_REQUEST_URL);
        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        Uri.Builder uriBuilder = baseUri.buildUpon();
        uriBuilder.appendQueryParameter(Constants.SECTION,category);
        buildedUrl = uriBuilder.toString();
        getLoaderManager().restartLoader(NEWS_LOADER_ID, null, this);
        Log.d ("NewsFragment", "Onclick works, string is: "+ category);
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
        return new StoryLoader(getActivity(), buildedUrl);
    }

    @Override
    public void onLoadFinished(Loader<List<NewsStory>> loader, List<NewsStory> newsStories) {
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
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
            swipeRefreshLayout.setRefreshing(false);

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
    /**
     * This is a method for Fragment.
     * You can do the same in onCreate or onRestoreInstanceState
     */
    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);

        if(savedInstanceState != null)
        {
            savedRecyclerLayoutState = savedInstanceState.getParcelable(BUNDLE_RECYCLER_LAYOUT);
            recyclerView.getLayoutManager().onRestoreInstanceState(savedRecyclerLayoutState);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(BUNDLE_RECYCLER_LAYOUT, recyclerView.getLayoutManager().onSaveInstanceState());
    }
    }
