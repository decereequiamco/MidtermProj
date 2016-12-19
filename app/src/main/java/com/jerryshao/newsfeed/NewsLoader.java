package com.jerryshao.newsfeed;

import android.content.AsyncTaskLoader;
import android.content.Context;

import com.jerryshao.newsfeed.dataUtil.QueryUtils;
import com.jerryshao.newsfeed.model.News;

import java.util.List;

/**
 * Created by Jerryshao on 2016/12/19.
 */

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
