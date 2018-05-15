package com.example.android.newsstories;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;
    private String activityTitle;
    private TextView actionBarTitle;
    private int position = 0;
    private Fragment fragment;
    private Bundle bundle;
    //please add the API key in the gradle.properties like this:
    //NewsStories_GuardianApiKey="your-key"
    String apiKey = BuildConfig.ApiKey;

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

        //Set the ListView for navigation drawer
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerLayout.setScrimColor(Color.TRANSPARENT);
        activityTitle = getTitle().toString();
        setupDrawer();

        //Add fragment to fill first page when app opens - later might be replaced stared/bookmarked news
        bundle = new Bundle();
        bundle.putInt(Constants.POSITION, position);
        bundle.putString(Constants.URL_KEY,
                "https://content.guardianapis.com/search?section=technology&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
        fragment = new NewsFragment();
        fragment.setArguments(bundle);
        // Add the fragment to the 'fragment_container' FrameLayout
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragment).commit();

        //set up the navigation menu to open selected news categories and another activities
        NavigationView navigationView = findViewById(R.id.nav_view);
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
                            case R.id.technology:
                                position = 0;
                                bundle = new Bundle();
                                bundle.putInt(Constants.POSITION, position);
                                bundle.putString(Constants.URL_KEY,
                                        "https://content.guardianapis.com/search?section=technology&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.law:
                                position = 1;
                                bundle = new Bundle();
                                bundle.putInt(Constants.POSITION, position);
                                bundle.putString(Constants.URL_KEY,
                                        "https://content.guardianapis.com/search?section=law&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.science:
                                position = 2;
                                bundle = new Bundle();
                                bundle.putInt(Constants.POSITION, position);
                                bundle.putString(Constants.URL_KEY,
                                        "https://content.guardianapis.com/search?section=science&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.education:
                                position = 3;
                                bundle = new Bundle();
                                bundle.putInt(Constants.POSITION, position);
                                bundle.putString(Constants.URL_KEY,
                                        "https://content.guardianapis.com/search?section=education&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.travel:
                                position = 4;
                                bundle = new Bundle();
                                bundle.putInt(Constants.POSITION, position);
                                bundle.putString(Constants.URL_KEY,
                                        "https://content.guardianapis.com/search?section=travel&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.music:
                                position = 5;
                                bundle = new Bundle();
                                bundle.putInt(Constants.POSITION, position);
                                bundle.putString(Constants.URL_KEY,
                                        "https://content.guardianapis.com/search?section=music&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.art_and_design:
                                position = 6;
                                bundle = new Bundle();
                                bundle.putInt(Constants.POSITION, position);
                                bundle.putString(Constants.URL_KEY,
                                        "https://content.guardianapis.com/search?section=artanddesign&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.film:
                                position = 7;
                                bundle = new Bundle();
                                bundle.putInt(Constants.POSITION, position);
                                bundle.putString(Constants.URL_KEY,
                                        "https://content.guardianapis.com/search?section=film&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.fashion:
                                position = 8;
                                bundle = new Bundle();
                                bundle.putInt(Constants.POSITION, position);
                                bundle.putString(Constants.URL_KEY,
                                        "https://content.guardianapis.com/search?section=fashion&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.crosswords:
                                position = 9;
                                bundle = new Bundle();
                                bundle.putInt(Constants.POSITION, position);
                                bundle.putString(Constants.URL_KEY,
                                        "https://content.guardianapis.com/search?section=crosswords&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
                                fragment = new NewsFragment();
                                fragment.setArguments(bundle);
                                // Add the fragment to the 'fragment_container' FrameLayout
                                getSupportFragmentManager().beginTransaction()
                                        .add(R.id.fragment_container, fragment).commit();
                                break;
                            case R.id.about_application:
                                Intent about = new Intent (MainActivity.this, AboutApplicationActivity.class);
                                startActivity(about);
                                break;

                            case R.id.settings:
                                Intent set = new Intent (MainActivity.this, SettingsActivity.class);
                                startActivity(set);
                                break;

                                default: position = 0;
                                    bundle = new Bundle();
                                    bundle.putInt(Constants.POSITION, position);
                                    bundle.putString(Constants.URL_KEY,
                                            "https://content.guardianapis.com/search?section=technology&format=json&from-date=2018-01-01&show-tags=contributor&show-fields=thumbnail&order-by=newest&api-key="+apiKey);
                                    fragment = new NewsFragment();
                                    fragment.setArguments(bundle);
                                    // Add the fragment to the 'fragment_container' FrameLayout
                                    getSupportFragmentManager().beginTransaction()
                                            .add(R.id.fragment_container, fragment).commit();
                                break;
                        }
                        return true;}

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
        int id = item.getItemId();


        // Activate the navigation drawer toggle
        return drawerToggle.onOptionsItemSelected(item) || super.onOptionsItemSelected(item);
    }
}
