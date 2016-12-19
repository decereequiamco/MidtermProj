package com.jerryshao.newsfeed;

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

import com.jerryshao.newsfeed.adapter.NewsAdapter;
import com.jerryshao.newsfeed.model.News;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<List<News>>{
    public static final int NEWS_LOADER_ID = 1;
    @BindView(R.id.news_list_view)
    public ListView newsListView;
    private NewsAdapter newsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        newsAdapter = new NewsAdapter(this, R.layout.news_list_item, new ArrayList<News>());
        newsListView.setAdapter(newsAdapter);

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
        return new NewsLoader(this, getString(R.string.base_api));
    }

    @Override
    public void onLoadFinished(android.content.Loader<List<News>> loader, List<News> data) {
        if (data.size() == 0 || data.get(0) == null) {
            return;
        }

        newsAdapter.clear();
        newsAdapter.addAll(data);
    }

    @Override
    public void onLoaderReset(android.content.Loader<List<News>> loader) {
        newsAdapter.clear();
    }
}
