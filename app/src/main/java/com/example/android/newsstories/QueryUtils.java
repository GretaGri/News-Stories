package com.example.android.newsstories;

import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Greta Grigutė on 2018-05-10.
 */
public class QueryUtils {
    public static final String LOG_TAG = QueryUtils.class.getSimpleName();

    private QueryUtils() {
    }

    public static List<NewsStory> fetchNewsData(Context context,String requestUrl) {
        /**
         * FOR REVIEWER: Please use this, to force the background thread to pause execution and wait for 2 seconds so that we can see how spinner works.
         try {
         Thread.sleep(2000);
         } catch (InterruptedException e) {
         e.printStackTrace();
         }
         */

        Log.d (LOG_TAG, "fetch News data");
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error closing input stream", e);
        }

        // Extract relevant fields from the JSON response and create an object
        List<NewsStory> story = extractNews(context,jsonResponse);

        // Return the {@link story}
        return story;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the news JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    /**
     * Return a list of {@link NewsStory} objects that has been built up from
     * parsing a JSON response.
     */
    public static List<NewsStory> extractNews(Context context, String jsonResponse) {

        // Create an empty ArrayList that we can start adding stories to
        List<NewsStory> newsStories = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {
            // Convert SAMPLE_JSON_RESPONSE String into a JSONObject
            JSONObject root = new JSONObject(jsonResponse);

            //Extract “response” Object
            JSONObject response = root.getJSONObject(Constants.RESPONSE);

            //Extract “results” JSONArray
            JSONArray results = response.getJSONArray(Constants.RESULTS);

            // looping through All results
            for (int i = 0; i < results.length(); i++) {

                JSONObject current = results.getJSONObject(i);
                String category = current.getString(Constants.SECTION_NAME);
                String date = current.getString(Constants.WEB_PUBLICATION_DATE);

                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd'T'kk:mm:ss'Z'");
                Date dateformatted = null;
                try {
                    dateformatted = simpleDateFormat.parse(date);
                }catch (ParseException e){
                    e.printStackTrace();
                }
                String dateReadyToShow = formatDate(dateformatted);
                Log.d(LOG_TAG, "Date to show is:" + dateReadyToShow);

                String title = current.getString(Constants.WEB_TITLE);
                String url = current.getString(Constants.WEB_URL);
                JSONObject fields = null;
               try {fields = current.getJSONObject(Constants.FIELDS);}
               catch (JSONException e){
                   Log.e(LOG_TAG, "Parsing problem, no value for Fields");
               }
                String pictureUrl;
                if (fields!=null){
                pictureUrl = fields.getString(Constants.THUMBNAIL);}
                else {pictureUrl = context.getString(R.string.no_picture);}

                JSONArray tags = current.getJSONArray(Constants.TAGS);

                String author;
                if(tags.length()>0){
                    JSONObject currentTag = tags.getJSONObject(0);
                    author = currentTag.getString(Constants.WEB_TITLE);}
                    else author = context.getString(R.string.author_unknown);


                //add those values to arrayList
                // FOR REVIEWER: comment out this line for checking, how my empty page works:)
                newsStories.add(new NewsStory(pictureUrl, category, title, author, dateReadyToShow, url));
                Log.d("LOG_TAG","Here are items added:"+pictureUrl+category+author+title+date+url);
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the Guardian news JSON results", e);
        }

        // Return the list of earthquakes
        return newsStories;
    }
    private static String formatDate (Date date){
        SimpleDateFormat newDateFormat = new SimpleDateFormat("MMM dd, yyyy'\n'hh:mm:ss");
        String formattedDate = newDateFormat.format(date);
        return formattedDate;}
}
