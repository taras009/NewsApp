package com.example.comp_admin.newsapp;

import android.annotation.SuppressLint;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public class NewsActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>> {
    private static final String LOG_TAG = NewsActivity.class.getSimpleName();
    private static final String GUARDIAN_REQUEST_URL = "https://content.guardianapis.com/search?api-key=570c5159-2655-4cf9-b215-e040f451c41d&show-tags=contributor";
    NewsAdapter mNewsAdapter;
    TextView emptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        emptyTextView = (TextView) findViewById(R.id.message);
        ListView newsListView = (ListView) findViewById(R.id.list);
        newsListView.setEmptyView(emptyTextView);

        // Create a new adapter that takes the list of news as input
        mNewsAdapter = new NewsAdapter(this, new ArrayList<News>());

        newsListView.setAdapter(mNewsAdapter);
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            getLoaderManager().initLoader(1, null, this);
        } else {
            findViewById(R.id.loading_spinner).setVisibility(View.GONE);
            emptyTextView.setText(R.string.no_internet_connection);
        }

        // Initialize the loader. Pass in the int ID constant defined above and pass in null for
        // the bundle. Pass in this activity for the LoaderCallbacks parameter (which is valid
        // because this activity implements the LoaderCallbacks interface).

        //*** Set onItemClick listener to transfer to the url link
        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                News currentNews = mNewsAdapter.getItem(position);

                Uri newsUri = Uri.parse(currentNews.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                startActivity(websiteIntent);
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int i, Bundle bundle) {
        // Create a new loader for the given URL
        Log.i(LOG_TAG, "Running onCreateLoader() method");
        return new NewsLoader(this, GUARDIAN_REQUEST_URL);
    }

    @Override
    public void onLoadFinished(Loader<List<News>> loader, List<News> news) {
        findViewById(R.id.loading_spinner).setVisibility(View.GONE);
        emptyTextView.setText(R.string.no_news_found);
        Log.i(LOG_TAG, "Running onLoadFinished() method");
        // Clear the adapter of previous news data
        mNewsAdapter.clear();
        if (news != null && !news.isEmpty()) {
            mNewsAdapter.addAll(news);
        }

    }

    @Override
    public void onLoaderReset(Loader<List<News>> loader) {
        // Loader reset, so we can clear out our existing data.
        Log.i(LOG_TAG, "Running onLoaderReset() method");
        mNewsAdapter.clear();

    }
}

