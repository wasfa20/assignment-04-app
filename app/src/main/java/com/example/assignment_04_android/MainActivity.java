package com.example.assignment_04_android;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assignment_04_android.adapter.NewsAdapter;
import com.example.assignment_04_android.database.DatabaseHelper;
import com.example.assignment_04_android.model.NewsArticle;
import com.example.assignment_04_android.network.NewsFetcher;
import com.example.assignment_04_android.utils.SessionManager;
import com.example.assignment_04_android.utils.ThemeUtils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NewsAdapter.OnItemClickListener {

    private RecyclerView recyclerView;
    private NewsAdapter adapter;
    private DatabaseHelper dbHelper;
    private SessionManager sessionManager;
    private ProgressBar progressBar;
    private TextView tvEmpty;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        ThemeUtils.applyTheme(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sessionManager = new SessionManager(this);
        dbHelper = new DatabaseHelper(this);

        if (!sessionManager.isLoggedIn()) {
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return;
        }

        recyclerView = findViewById(R.id.recyclerView);
        progressBar = findViewById(R.id.progressBar);
        tvEmpty = findViewById(R.id.tvEmptyState);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new NewsAdapter(this, new ArrayList<NewsArticle>(), this);
        recyclerView.setAdapter(adapter);

        loadNewsFromDb();

        // Only fetch from API if this is the first creation (not rotation)
        // Or if DB is empty
        if (savedInstanceState == null) {
            if (isNetworkAvailable()) {
                fetchNewsFromApi();
            } else {
                Toast.makeText(this, "Offline Mode", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadNewsFromDb() {
        List<NewsArticle> articles = dbHelper.getAllArticles();
        if (articles.isEmpty()) {
            tvEmpty.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            tvEmpty.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            adapter.updateData(articles);
        }
    }

    private void fetchNewsFromApi() {
        progressBar.setVisibility(View.VISIBLE);
        NewsFetcher.fetchTopHeadlines(new NewsFetcher.NewsCallback() {
            @Override
            public void onSuccess(List<NewsArticle> articles) {
                progressBar.setVisibility(View.GONE);
                if (articles != null && !articles.isEmpty()) {
                    // Update DB
                    for (NewsArticle article : articles) {
                        dbHelper.insertArticle(article);
                    }
                    // Refresh UI from DB
                    loadNewsFromDb();
                    Toast.makeText(MainActivity.this, "News Updated", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(MainActivity.this, "No news found from API", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onError(String error) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(MainActivity.this, "API Error: " + error + ". Showing local data.", Toast.LENGTH_LONG)
                        .show();
                loadNewsFromDb();
            }
        });
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_theme) {
            showThemeDialog();
            return true;
        } else if (id == R.id.action_logout) {
            sessionManager.logoutUser();
            startActivity(new Intent(this, LoginActivity.class));
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void showThemeDialog() {
        final String[] themes = { getString(R.string.theme_light), getString(R.string.theme_dark),
                getString(R.string.theme_custom) };
        int currentTheme = ThemeUtils.getSavedTheme(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Theme");
        builder.setSingleChoiceItems(themes, currentTheme, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ThemeUtils.saveTheme(MainActivity.this, which);
                dialog.dismiss();
                recreate(); // Restart Activity to apply theme
            }
        });
        builder.show();
    }

    // Adapter Interfaces
    @Override
    public void onItemClick(NewsArticle article) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra(DetailActivity.EXTRA_URL, article.getUrl());
        startActivity(intent);
    }

    @Override
    public void onDeleteClick(NewsArticle article) {
        dbHelper.deleteArticle(article.getId());
        loadNewsFromDb();
        Toast.makeText(this, "Article Deleted", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShareClick(NewsArticle article) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, article.getTitle() + "\n" + article.getUrl());
        sendIntent.setType("text/plain");
        startActivity(Intent.createChooser(sendIntent, "Share News via"));
    }

    @Override
    protected void onDestroy() {
        dbHelper.close(); // Close DB on destroy
        super.onDestroy();
    }
}
