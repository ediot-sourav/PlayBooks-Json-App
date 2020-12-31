package com.magicmusic.playbooks;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.widget.ImageView;

import java.util.List;

public class EarthquakeLoader extends AsyncTaskLoader<List<PlayBooks>> {

    private static final String LOG_TAG = EarthquakeLoader.class.getName();

    private String mUrl;

    public EarthquakeLoader(Context context, String url) {
        super(context);
        mUrl = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    public List<PlayBooks> loadInBackground() {

        if (mUrl == null) {
            return null;
        }

        List<PlayBooks> earthquakes = QueryUtils.fetchBooks(mUrl);
        return earthquakes;
    }

}
