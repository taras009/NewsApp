package com.example.comp_admin.newsapp;

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
import java.util.ArrayList;
import java.util.List;

public final class Misc {

    private static final String LOG_TAG = Misc.class.getSimpleName();

    private Misc() {

    }

    public static List<News> fetchDataToArray(String stringUrl) {
        Log.i(LOG_TAG, "Running fetchDataToArray() method");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String jsonResponse = null;
        URL url = createUrl(stringUrl);
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(LOG_TAG, "Unable to make a connection ", e);
        }

        return extractNews(jsonResponse);
    }

    //*** creating URL
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
            if (urlConnection.getResponseCode() == 200) {
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
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
        BufferedReader reader = new BufferedReader(inputStreamReader);
        String line = reader.readLine();
        while (line != null) {
            output.append(line);
            line = reader.readLine();
        }
        Log.i(LOG_TAG, output.toString());
        return output.toString();
    }

    /**
     * Return a list of {@link News} objects that has been built up from
     * parsing a JSON response.
     */

    private static List<News> extractNews(String jsonResponse) {
        // Create an empty ArrayList that we can start adding news to
        List<News> news = new ArrayList<>();
        try {
            JSONObject rootJSONObject = new JSONObject(jsonResponse);

            JSONObject responseJSONObject = rootJSONObject.getJSONObject("response");

            JSONArray resultsJSONArray = responseJSONObject.getJSONArray("results");

            for (int i = 0; i < resultsJSONArray.length(); i++) {
                JSONObject newsJSONObject = resultsJSONArray.getJSONObject(i);

                String title = newsJSONObject.getString("webTitle");
                String section = newsJSONObject.getString("sectionName");
                String url = newsJSONObject.getString("webUrl");
                String date = newsJSONObject.getString("webPublicationDate");
                String authorName = "no author found";
                JSONArray tagsJSONArray = newsJSONObject.getJSONArray("tags");
                if (tagsJSONArray.length() != 0) {
                    JSONObject contributorsJSONObject = tagsJSONArray.getJSONObject(0);
                    if (!contributorsJSONObject.getString("webTitle").equals("")) {
                        authorName = contributorsJSONObject.getString("webTitle");
                    }
                }

                News singleNews = new News(title, section, url, date, authorName);
                news.add(singleNews);
            }
        } catch (JSONException e) {
            Log.e("Misc", "Problem parsing the news JSON results", e);
        }

        return news;
    }

}
