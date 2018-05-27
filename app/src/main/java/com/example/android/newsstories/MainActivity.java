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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    //please add the API key in the gradle.properties like this:
    //NewsStories_GuardianApiKey="your-key"
    String apiKey = BuildConfig.ApiKey;
    private static final String GUARDIAN_API_URL= "https://content.guardianapis.com/search";
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private TextView actionBarTitle;
    private int position = 0;
    private Fragment fragment;
    private Bundle bundle;
    private Uri.Builder uriBuilder;
    private String section;
    private String url_without_section;
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
        bundle.putInt(Constants.POSITION, position);
        bundle.putString(Constants.URL_KEY,url_without_section);
        fragment = new NewsFragment();
        fragment.setArguments(bundle);
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment).commit();

        //set up the navigation menu to open selected news categories and another activities
        NavigationView navigationView = findViewById(R.id.nav_view);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        Menu menu = navigationView.getMenu();
       if(!sharedPreferences.getBoolean(getString(R.string.technology),true)){
           menu.getItem(1).setVisible(false);}
        if(!sharedPreferences.getBoolean(getString(R.string.law),true)){
            menu.getItem(2).setVisible(false);}
        if(!sharedPreferences.getBoolean(getString(R.string.science),true)){
            menu.getItem(3).setVisible(false);}
        if(!sharedPreferences.getBoolean(getString(R.string.education),true)){
            menu.getItem(4).setVisible(false);}
        if(!sharedPreferences.getBoolean(getString(R.string.travel),true)){
            menu.getItem(5).setVisible(false);}
        if(!sharedPreferences.getBoolean(getString(R.string.music),true)){
            menu.getItem(6).setVisible(false);}
        if(!sharedPreferences.getBoolean(getString(R.string.art_and_design),true)){
            menu.getItem(7).setVisible(false);}
        if(!sharedPreferences.getBoolean(getString(R.string.film),true)){
            menu.getItem(8).setVisible(false);}
        if(!sharedPreferences.getBoolean(getString(R.string.fashion),true)){
            menu.getItem(9).setVisible(false);}
        if(!sharedPreferences.getBoolean(getString(R.string.crosswords),true)){
            menu.getItem(10).setVisible(false);}
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
                                bundle = new Bundle();
                                bundle.putString(Constants.URL_KEY,url_without_section);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.technology:
                                bundle = new Bundle();
                                section = Constants.TECHNOLOGY_TAG;
                                uriBuilder.appendQueryParameter("section", section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.law:
                                bundle = new Bundle();
                                section = Constants.LAW_TAG;
                                uriBuilder.appendQueryParameter("section", section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.science:
                                bundle = new Bundle();
                                section = Constants.SCIENCE_TAG;
                                uriBuilder.appendQueryParameter("section", section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.education:
                                bundle = new Bundle();
                                section = Constants.EDUCATION_TAG;
                                uriBuilder.appendQueryParameter("section", section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.travel:
                                bundle = new Bundle();
                                section = Constants.TRAVEL_TAG;
                                uriBuilder.appendQueryParameter("section", section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.music:
                                bundle = new Bundle();
                                section = Constants.MUSIC_TAG;
                                uriBuilder.appendQueryParameter("section", section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.art_and_design:
                                bundle = new Bundle();
                                section = Constants.ART_AND_DESIGN_TAG;
                                uriBuilder.appendQueryParameter("section", section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.film:
                                bundle = new Bundle();
                                section = Constants.FILM_TAG;
                                uriBuilder.appendQueryParameter("section", section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.fashion:
                                bundle = new Bundle();
                                section = Constants.FASHION_TAG;
                                uriBuilder.appendQueryParameter("section", section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.crosswords:
                                bundle = new Bundle();
                                section = Constants.CROSSWORDS_TAG;
                                uriBuilder.appendQueryParameter("section", section);
                                bundle.putString(Constants.URL_KEY, uriBuilder.toString());
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.about_application:
                                Intent about = new Intent(MainActivity.this, AboutApplicationActivity.class);
                                startActivity(about);
                                break;

                           // case R.id.settings:
                           //     Intent set = new Intent(MainActivity.this, SettingsActivity.class);
                            //    startActivity(set);
                           //     break;

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
}
