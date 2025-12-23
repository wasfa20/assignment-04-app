package com.example.assignment_04_android.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.assignment_04_android.model.NewsArticle;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "news_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_ARTICLES = "articles";

    private static final String COLUMN_ID = "id";
    private static final String COLUMN_TITLE = "title";
    private static final String COLUMN_DESC = "description";
    private static final String COLUMN_URL = "url";
    private static final String COLUMN_DATE = "published_date";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_ARTICLES + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESC + " TEXT,"
                + COLUMN_URL + " TEXT UNIQUE,"
                + COLUMN_DATE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ARTICLES);
        onCreate(db);
    }

    public void insertArticle(NewsArticle article) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TITLE, article.getTitle());
        values.put(COLUMN_DESC, article.getDescription());
        values.put(COLUMN_URL, article.getUrl());
        values.put(COLUMN_DATE, article.getPublishedAt());

        // Use replace to avoid duplicates if URL is unique
        db.replace(TABLE_ARTICLES, null, values);
        db.close();
    }

    public List<NewsArticle> getAllArticles() {
        List<NewsArticle> articleList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_ARTICLES + " ORDER BY " + COLUMN_ID + " DESC";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_ID));
                String title = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_TITLE));
                String desc = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DESC));
                String url = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_URL));
                String date = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_DATE));

                NewsArticle article = new NewsArticle(id, title, desc, url, date);
                articleList.add(article);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return articleList;
    }

    public void deleteArticle(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_ARTICLES, COLUMN_ID + " = ?", new String[] { String.valueOf(id) });
        db.close();
    }
}
