package com.example.assignment_04_android.network;

import android.os.Handler;
import android.os.Looper;
import com.example.assignment_04_android.model.NewsArticle;
import com.example.assignment_04_android.utils.Constants;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NewsFetcher {

    public interface NewsCallback {
        void onSuccess(List<NewsArticle> articles);

        void onError(String error);
    }

    public static void fetchTopHeadlines(final NewsCallback callback) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        final Handler handler = new Handler(Looper.getMainLooper());

        executor.execute(new Runnable() {
            @Override
            public void run() {
                final List<NewsArticle> articles = new ArrayList<>();
                HttpURLConnection urlConnection = null;

                try {
                    URL url = new URL(Constants.API_URL);
                    urlConnection = (HttpURLConnection) url.openConnection();
                    urlConnection.setRequestMethod("GET");
                    urlConnection.setConnectTimeout(10000);
                    urlConnection.setReadTimeout(10000);

                    int code = urlConnection.getResponseCode();
                    if (code == 200) {
                        BufferedReader in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder response = new StringBuilder();
                        String line;
                        while ((line = in.readLine()) != null) {
                            response.append(line);
                        }
                        in.close();

                        JSONObject jsonResponse = new JSONObject(response.toString());
                        if (jsonResponse.has("articles")) {
                            JSONArray jsonArray = jsonResponse.getJSONArray("articles");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject articleObj = jsonArray.getJSONObject(i);

                                String title = articleObj.optString("title", "No Title");
                                String description = articleObj.optString("description", "No Description");
                                String urlStr = articleObj.optString("url", "");
                                String date = articleObj.optString("publishedAt", "");

                                // Basic validation
                                if (!title.isEmpty() && !urlStr.isEmpty()) {
                                    articles.add(new NewsArticle(title, description, urlStr, date));
                                }
                            }
                        }

                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onSuccess(articles);
                            }
                        });

                    } else {
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                callback.onError("Error Code: " + code);
                            }
                        });
                    }

                } catch (final Exception e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            callback.onError("Exception: " + e.getMessage());
                        }
                    });
                } finally {
                    if (urlConnection != null) {
                        urlConnection.disconnect();
                    }
                }
            }
        });
    }
}
