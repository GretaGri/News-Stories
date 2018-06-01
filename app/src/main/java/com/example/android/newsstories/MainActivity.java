package com.example.android.newsstories;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    DrawerLayout drawerLayout;
    //please add the API key in the gradle.properties like this:
    //NewsStories_GuardianApiKey="your-key"
    String apiKey = BuildConfig.ApiKey;
    private static final String GUARDIAN_API_URL= "https://content.guardianapis.com/search";
    private static final String CURRENT_CATEGORY = "current_category";
    private static final String BUILD_URI = "buid_uri";
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private TextView actionBarTitle;
    private Fragment fragment;
    private Bundle bundle;
    private Uri.Builder uriBuilder;
    private String section;
    private String url_without_section;
    private NavigationView navigationView;
    private Menu menu;
    private ArrayList<NewsStory> favorite = new ArrayList<>();
    private String categoryUrl;
    private Boolean starred = false;
    private int currentCategory = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setIcon(R.mipmap.ic_launcher);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        actionBarTitle = findViewById(R.id.custom_title);

        //Set up navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        activityTitle = getTitle().toString();
        setupDrawer();
        // get shared preferences and register shared preferences change listener
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        sharedPreferences.registerOnSharedPreferenceChangeListener(this);
        // parse breaks apart the URI string that's passed into its parameter
        Uri baseUri = Uri.parse(GUARDIAN_API_URL);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
       uriBuilder = baseUri.buildUpon();

        // Append query parameter and its value. For example, the `format=geojson`
        uriBuilder.appendQueryParameter("format", "json");
        uriBuilder.appendQueryParameter("from-date", "2018-01-01");
        uriBuilder.appendQueryParameter("show-tags", "contributor");
        uriBuilder.appendQueryParameter("show-fields", "thumbnail");
        uriBuilder.appendQueryParameter("order-by", "newest");
        uriBuilder.appendQueryParameter("api-key", apiKey);


        url_without_section = uriBuilder.toString();

        //Add fragment to fill first page when app opens - later might be replaced stared/bookmarked news
        bundle = new Bundle();
        bundle.putString(Constants.URL_KEY,url_without_section);
        fragment = new NewsFragment();
        fragment.setArguments(bundle);
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment).commit();

        //set up the navigation menu to open selected news categories and another activities
        navigationView = findViewById(R.id.nav_view);

        menu = navigationView.getMenu();

        menu.getItem(11).setVisible(false);
        if(!sharedPreferences.getBoolean(getString(R.string.favorite_key),false)){
            menu.getItem(11).setVisible(false);
        }else menu.getItem(11).setVisible(true);

                navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        // set item as selected to persist highlight
                        menuItem.setChecked(true);
                        // close drawer when item is tapped
                        drawerLayout.closeDrawers();

                        // swap UI fragments
                        switch (menuItem.getItemId()) {
                            case R.id.home:
                                currentCategory = 0;
                                bundle = new Bundle();
                                bundle.putString(Constants.URL_KEY,url_without_section);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.technology:
                                currentCategory = 1;
                                bundle = new Bundle();
                                section = Constants.TECHNOLOGY_TAG;
                                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.law:
                                currentCategory = 2;
                                bundle = new Bundle();
                                section = Constants.LAW_TAG;
                                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.science:
                                currentCategory = 3;
                                bundle = new Bundle();
                                section = Constants.SCIENCE_TAG;
                                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.education:
                                currentCategory = 4;
                                bundle = new Bundle();
                                section = Constants.EDUCATION_TAG;
                                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.travel:
                                currentCategory = 5;
                                bundle = new Bundle();
                                section = Constants.TRAVEL_TAG;
                                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.music:
                                currentCategory = 6;
                                bundle = new Bundle();
                                section = Constants.MUSIC_TAG;
                                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.art_and_design:
                                currentCategory = 7;
                                bundle = new Bundle();
                                section = Constants.ART_AND_DESIGN_TAG;
                                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.film:
                                currentCategory = 8;
                                bundle = new Bundle();
                                section = Constants.FILM_TAG;
                                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.fashion:
                                currentCategory = 9;
                                bundle = new Bundle();
                                section = Constants.FASHION_TAG;
                                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.crosswords:
                                currentCategory = 10;
                                bundle = new Bundle();
                                section = Constants.CROSSWORDS_TAG;
                                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.favorite:
                                currentCategory = 11;
                                bundle = new Bundle();
                                bundle.putParcelableArrayList("Favorite_array_key", favorite);
                                Log.d("Main activity", "Array list size is : " + favorite.size());
                                bundle.putString("Category_url_key",categoryUrl );
                                fragment = new FavoriteFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.about_application:
                                currentCategory = 12;
                                Intent about = new Intent(MainActivity.this, AboutApplicationActivity.class);
                                startActivity(about);
                                break;

                            case R.id.settings:
                                currentCategory = 13;
                               Intent set = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(set);
                                break;

                            default:
                                bundle = new Bundle();
                                bundle.putString(Constants.URL_KEY, url_without_section);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                        }
                        return true;
                    }

                });

    }

    //Helper method to set up drawer
    private void setupDrawer() {
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_opened, R.string.drawer_closed) {

            // Called when a drawer has settled in a completely open state.
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // Set a new title for the Action Bar
                actionBarTitle.setText(R.string.choose_category);

                // Create call to onPrepareOptionsMenu() in case it needs to be recreated with
                // different options for when the navigation drawer is open
                invalidateOptionsMenu();
            }

            //Called when a drawer has settled in a completely closed state.
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                actionBarTitle.setText(activityTitle);
                getSupportActionBar().setIcon(R.mipmap.ic_launcher);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        // Activate the navigation drawer toggle
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if (key.equals(getString(R.string.show_all))||key.equals(getString(R.string.technology_key))
                ||key.equals(getString(R.string.law_key))||key.equals(getString(R.string.science_key))
                ||key.equals(getString(R.string.education_key))||key.equals(getString(R.string.travel_key))
                ||key.equals(getString(R.string.music_key))||key.equals(getString(R.string.art_and_design_key))
                ||key.equals(getString(R.string.film_key))||key.equals(getString(R.string.fashion_key))
                ||key.equals(getString(R.string.crosswords_key))||key.equals(getString(R.string.favorite_key))) {
            loadmenuFromSharedPreferences(sharedPreferences);
            Log.d("Main Activity", "shared preferences listener is called : ");
        }
    }
    private void loadmenuFromSharedPreferences(SharedPreferences sharedPreferences) {
        if(!sharedPreferences.getBoolean(getString(R.string.technology_key),true)){
            menu.getItem(1).setVisible(false);}
            else menu.getItem(1).setVisible(true);
        if(!sharedPreferences.getBoolean(getString(R.string.law_key),true)){
            menu.getItem(2).setVisible(false);}
        else menu.getItem(2).setVisible(true);
        if(!sharedPreferences.getBoolean(getString(R.string.science_key),true)){
            menu.getItem(3).setVisible(false);}
        else menu.getItem(3).setVisible(true);
        if(!sharedPreferences.getBoolean(getString(R.string.education_key),true)){
            menu.getItem(4).setVisible(false);}
        else menu.getItem(4).setVisible(true);
        if(!sharedPreferences.getBoolean(getString(R.string.travel_key),true)){
            menu.getItem(5).setVisible(false);}
        else menu.getItem(5).setVisible(true);
        if(!sharedPreferences.getBoolean(getString(R.string.music_key),true)){
            menu.getItem(6).setVisible(false);}
        else menu.getItem(6).setVisible(true);
        if(!sharedPreferences.getBoolean(getString(R.string.art_and_design_key),true)){
            menu.getItem(7).setVisible(false);}
        else menu.getItem(7).setVisible(true);
        if(!sharedPreferences.getBoolean(getString(R.string.film_key),true)){
            menu.getItem(8).setVisible(false);}
        else menu.getItem(8).setVisible(true);
        if(!sharedPreferences.getBoolean(getString(R.string.fashion_key),true)){
            menu.getItem(9).setVisible(false);}
        else menu.getItem(9).setVisible(true);
        if(!sharedPreferences.getBoolean(getString(R.string.crosswords_key),true)){
            menu.getItem(10).setVisible(false);}
        else menu.getItem(10).setVisible(true);
        if(sharedPreferences.getBoolean(getString(R.string.favorite_key),false)){
            menu.getItem(11).setVisible(true);
            String picture = sharedPreferences.getString("Picture_key","");
            String category = sharedPreferences.getString("Category_key","");
            String title = sharedPreferences.getString("Title_key","");
            String author = sharedPreferences.getString("Author_key","");
            String date = sharedPreferences.getString("Date_key","");
            String urlWithDetails = sharedPreferences.getString("Url_key","");
            starred = true;
            categoryUrl = sharedPreferences.getString("Category_url_key","");
            favorite.add(new NewsStory(picture, category, title, author, date, urlWithDetails, starred));
            Log.d("Main Activity", "shared preferences array list is updated, size: " + favorite.size());
        }else menu.getItem(11).setVisible(false);
    }

    @Override
    protected void onDestroy () {
        super.onDestroy();
        // get shared preferences and unregister shared preferences change listener
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        sharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current game state
        savedInstanceState.putInt(CURRENT_CATEGORY, currentCategory);
        savedInstanceState.putString(BUILD_URI, url_without_section);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }

    public void onRestoreInstanceState(Bundle savedInstanceState) {
        // Always call the superclass so it can restore the view hierarchy
        super.onRestoreInstanceState(savedInstanceState);
        // Restore state members from saved instance
        currentCategory = savedInstanceState.getInt(CURRENT_CATEGORY);
        url_without_section = savedInstanceState.getString(BUILD_URI);

        Uri baseUri = Uri.parse(url_without_section);

        // buildUpon prepares the baseUri that we just parsed so we can add query parameters to it
        uriBuilder = baseUri.buildUpon();

        switch (currentCategory) {
            case 0:
                bundle = new Bundle();
                bundle.putString(Constants.URL_KEY, url_without_section);
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 1:
                bundle = new Bundle();
                section = Constants.TECHNOLOGY_TAG;
                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 2:
                bundle = new Bundle();
                section = Constants.LAW_TAG;
                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 3:
                bundle = new Bundle();
                section = Constants.SCIENCE_TAG;
                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 4:
                bundle = new Bundle();
                section = Constants.EDUCATION_TAG;
                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 5:
                bundle = new Bundle();
                section = Constants.TRAVEL_TAG;
                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 6:
                bundle = new Bundle();
                section = Constants.MUSIC_TAG;
                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 7:
                bundle = new Bundle();
                section = Constants.ART_AND_DESIGN_TAG;
                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 8:
                bundle = new Bundle();
                section = Constants.FILM_TAG;
                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 9:
                bundle = new Bundle();
                section = Constants.FASHION_TAG;
                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 10:
                bundle = new Bundle();
                section = Constants.CROSSWORDS_TAG;
                uriBuilder.appendQueryParameter(Constants.SECTION, section);
                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 11:
                bundle = new Bundle();
                bundle.putParcelableArrayList("Favorite_array_key", favorite);
                Log.d("Main activity", "Array list size is : " + favorite.size());
                bundle.putString("Category_url_key", categoryUrl);
                fragment = new FavoriteFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
            case 12:
                Intent about = new Intent(MainActivity.this, AboutApplicationActivity.class);
                startActivity(about);
                break;

            case 13:
                Intent set = new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(set);
                break;

            default:
                bundle = new Bundle();
                bundle.putString(Constants.URL_KEY, url_without_section);
                fragment = new NewsFragment();
                fragment.setArguments(bundle);
                // Add the fragment to the 'fragment_container' FrameLayout
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.fragment_container, fragment).commit();
                break;
        }
    }

}
