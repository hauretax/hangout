package com.example.fthangoutv03;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import androidx.appcompat.widget.Toolbar;

public class ToolbarColorUtil {
    private static final String PREFS_NAME = "prefs";
    private static final String PREF_COLOR = "toolbar_color";

    public static void applySavedColor(Toolbar toolbar, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String savedColor = sharedPreferences.getString(PREF_COLOR, "#171738");
        toolbar.setBackgroundColor(Color.parseColor(savedColor));
    }

    public static void saveColor(String color, Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(PREF_COLOR, color);
        editor.apply();
    }

    public static void changeToolbarColor(Toolbar toolbar, String color, Context context) {
        toolbar.setBackgroundColor(Color.parseColor(color));
        saveColor(color, context);
    }
}
