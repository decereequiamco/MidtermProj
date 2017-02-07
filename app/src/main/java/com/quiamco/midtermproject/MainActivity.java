package com.quiamco.midtermproject;

import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.quiamco.midtermproject.adapter.AdapterForNewsFeed;
import com.quiamco.midtermproject.model.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{
    public static final int NEWS_LOADER_ID = 1;
    @BindView(com.quiamco.midtermproject.R.id.news_list_view)
    public ListView newsListView;
    private AdapterForNewsFeed adapterForNewsFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.quiamco.midtermproject.R.layout.activity_main);
        ButterKnife.bind(this);

        adapterForNewsFeed = new AdapterForNewsFeed(this, com.quiamco.midtermproject.R.layout.news_list_item, new ArrayList<News>());
        newsListView.setAdapter(adapterForNewsFeed);

        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        LoaderManager loaderManager = getLoaderManager();

        if (networkInfo != null && networkInfo.isConnected()) {
            loaderManager.initLoader(NEWS_LOADER_ID, null, this);
        }

        newsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                News news = (News) newsListView.getItemAtPosition(i);
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(news.getUrl()));
                startActivity(intent);
            }
        });
    }

    @Override
    public Loader<List<News>> onCreateLoader(int id, Bundle args) {
        return new NewsLoader(this, getString(com.quiamco.midtermproject.R.string.base_api));
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, List<News> data) {
        if (data.size() == 0 || data.get(0) == null) {
            return;
        }

        adapterForNewsFeed.clear();
        adapterForNewsFeed.addAll(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        adapterForNewsFeed.clear();
    }
}
