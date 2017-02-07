package com.quiamco.midtermproject;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.quiamco.midtermproject.dataUtil.QueryUtils;
import com.quiamco.midtermproject.model.News;

import java.util.List;


public class NewsLoader extends AsyncTaskLoader<List<News>> {
    private String url;

    public NewsLoader(Context context, String url) {
        super(context);
        this.url = url;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {
        String JSONResult = QueryUtils.makeHTTPConnection(url);

        return QueryUtils.parseJSONToNews(JSONResult);
    }
}
