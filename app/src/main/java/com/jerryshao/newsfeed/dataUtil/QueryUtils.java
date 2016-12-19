package com.jerryshao.newsfeed.dataUtil;

import com.jerryshao.newsfeed.model.News;

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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jerryshao on 2016/12/19.
 */

public class QueryUtils {
    private QueryUtils() { }

    /* Get HTTP connection using the given url address
     * @param address api address
     * @return base JSON
     */
    public static String makeHTTPConnection(String address) {
        StringBuilder stringBuilder = new StringBuilder();
        HttpURLConnection connection = null;
        InputStream inputStream = null;

        try {
            URL url = new URL(address);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(30000);
            connection.connect();

            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();

                while (line != null) {
                    stringBuilder.append(line);
                    line = bufferedReader.readLine();
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect();
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return stringBuilder.toString();
    }

    public static ArrayList<News> parseJSONToNews(String JSON) {
        ArrayList<News> news = new ArrayList<>();

        try {
            JSONObject sourceJSON = new JSONObject(JSON).getJSONObject("response");
            JSONArray arrayJSON = sourceJSON.getJSONArray("results");

            for (int i = 0; i < arrayJSON.length(); i++) {
                News n = new News();
                n.setTitle(arrayJSON.getJSONObject(i).getString("webTitle"));
                n.setSection(arrayJSON.getJSONObject(i).getString("sectionName"));
                n.setUrl(arrayJSON.getJSONObject(i).getString("webUrl"));
                news.add(n);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

        return news;
    }

}
