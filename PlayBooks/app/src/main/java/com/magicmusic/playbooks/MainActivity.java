package com.magicmusic.playbooks;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

//public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<PlayBooks>> {
//    private static final int LOADER_ID = 1;
//    private PlayBooksAdapter mAdapter;
//    private List<PlayBooks> playBooks = new ArrayList<>();
//    TextView mEmptyState;
//    ProgressBar mProgressBar;
//    private static final String BOOKS_API  =
//            "https://www.googleapis.com/books/v1/volumes?q=isbn:0716604892";
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.list_item);
//
//        mEmptyState = (TextView) findViewById(R.id.empty_view);
//        mProgressBar = (ProgressBar) findViewById(R.id.loading_indicator);
//
//        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService(CONNECTIVITY_SERVICE);
//        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
//        if (networkInfo != null && networkInfo.isConnected()){
//            android.app.LoaderManager loaderManager = getLoaderManager();
//            loaderManager.initLoader(LOADER_ID, null, this);
//        } else {
//            mProgressBar.setVisibility(View.GONE);
//            mEmptyState.setText(R.string.no_internet_connection);
//        }
//
//        ListView listView = findViewById(R.id.list);
//        listView.setEmptyView(mEmptyState);
//
//        mAdapter = new PlayBooksAdapter(this,  new ArrayList<PlayBooks>());
//        listView.setAdapter(mAdapter);
//
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
//                PlayBooks playBooks = mAdapter.getItem(position);
//
//                Uri bookUri = Uri.parse(playBooks.getUrl());
//
//                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, bookUri);
//
//                startActivity(websiteIntent);
//            }
//        });
//    }
//
//
//    @NonNull
//    @Override
//    public androidx.loader.content.Loader<List<PlayBooks>> onCreateLoader(int id, @Nullable Bundle args) {
//        return new BookLoader(this, BOOKS_API);
//    }
//
//    @Override
//    public void onLoadFinished(@NonNull androidx.loader.content.Loader<List<PlayBooks>> loader, List<PlayBooks> data) {
//        mAdapter.clear();
//        if (playBooks == null){
//            return;
//        }
//        mAdapter.addAll(playBooks);
//
//        mProgressBar.setVisibility(View.GONE);
//        mEmptyState.setText(R.string.no_earthquake_found);
//    }
//
//    @Override
//    public void onLoaderReset(@NonNull androidx.loader.content.Loader<List<PlayBooks>> loader) {
//        mAdapter.clear();
//    }
//
//
//
//}
public class MainActivity extends AppCompatActivity implements android.app.LoaderManager.LoaderCallbacks<List<PlayBooks>> {

    private static final String USGS_REQUEST_URL =
            "https://www.googleapis.com/books/v1/volumes?q=isbn:0716604892";
    private PlayBooksAdapter mAdapter;
    private static final int EARTHQUAKE_LOADER_ID = 1;
    private TextView mEmptyStateTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_item);

        ListView earthquakeListView = findViewById(R.id.list);

        mAdapter = new PlayBooksAdapter(this, new ArrayList<PlayBooks>());

        earthquakeListView.setAdapter(mAdapter);

        mEmptyStateTextView = findViewById(R.id.empty_view);
        earthquakeListView.setEmptyView(mEmptyStateTextView);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

        if (networkInfo!=null && networkInfo.isConnected()){
            LoaderManager loaderManager = getLoaderManager();

            loaderManager.initLoader(EARTHQUAKE_LOADER_ID, null, this);
        } else {

            View loadingIndicator = findViewById(R.id.loading_indicator);
            loadingIndicator.setVisibility(View.GONE);
            mEmptyStateTextView.setText(R.string.no_internet_connection);

        }






        earthquakeListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                PlayBooks currentEarthquake = mAdapter.getItem(position);

                Uri earthquakeUri = Uri.parse(currentEarthquake.getUrl());

                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, earthquakeUri);

                startActivity(websiteIntent);
            }
        });

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.main, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        if (id == R.id.action_settings) {
//            Intent settingsIntent = new Intent(this, SettingsActivity.class);
//            startActivity(settingsIntent);
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public Loader<List<PlayBooks>> onCreateLoader(int i, Bundle bundle) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);
        String minMagnitude = sharedPrefs.getString(
                getString(R.string.settings_min_magnitude_key),
                getString(R.string.settings_min_magnitude_default));

        String orderBy = sharedPrefs.getString(
                getString(R.string.settings_order_by_key),
                getString(R.string.settings_order_by_default)
        );

        Uri baseUri = Uri.parse(USGS_REQUEST_URL);
        Uri.Builder uriBuilder = baseUri.buildUpon();

        uriBuilder.appendQueryParameter("format", "geojson");
        uriBuilder.appendQueryParameter("limit", "10");
        uriBuilder.appendQueryParameter("minmag", minMagnitude);
        uriBuilder.appendQueryParameter("orderby", orderBy);

        return new EarthquakeLoader(this, uriBuilder.toString());
    }

    @Override
    public void onLoadFinished(Loader<List<PlayBooks>> loader, List<PlayBooks> earthquakes) {

        View loadingIndicator = findViewById(R.id.loading_indicator);
        loadingIndicator.setVisibility(View.GONE);

        mEmptyStateTextView.setText(R.string.no_earthquake_found);
        mAdapter.clear();

        if (earthquakes != null && !earthquakes.isEmpty()) {
            mAdapter.addAll(earthquakes);
        }
    }

    @Override
    public void onLoaderReset(Loader<List<PlayBooks>> loader) {
        mAdapter.clear();
    }




}


    

    