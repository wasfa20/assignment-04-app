package com.example.assignment_04_android.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import com.example.assignment_04_android.R;

public class ThemeUtils {
    private static final String PREF_NAME = "ThemePref";
    private static final String KEY_THEME = "selected_theme";

    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;
    public static final int THEME_CUSTOM = 2;

    public static void saveTheme(Context context, int themeMode) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        pref.edit().putInt(KEY_THEME, themeMode).apply();
    }

    public static int getSavedTheme(Context context) {
        SharedPreferences pref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return pref.getInt(KEY_THEME, THEME_LIGHT); // Default Light
    }

    public static void applyTheme(Activity activity) {
        int theme = getSavedTheme(activity);
        switch (theme) {
            case THEME_DARK:
                activity.setTheme(R.style.Theme_NewsReader_Dark);
                break;
            case THEME_CUSTOM:
                activity.setTheme(R.style.Theme_NewsReader_Custom);
                break;
            default:
                activity.setTheme(R.style.Theme_NewsReader_Light);
                break;
        }
    }
}
