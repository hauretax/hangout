package com.example.fthangoutv03.intercept;

import android.app.Application;
import android.content.SharedPreferences;
import android.widget.Toast;

import androidx.lifecycle.DefaultLifecycleObserver;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MyAppObserver extends Application implements DefaultLifecycleObserver {

    private static final String PREFS_NAME = "AppPrefs";
    private static final String KEY_BACKGROUND_TIME = "background_time";

    @Override
    public void onCreate() {
        super.onCreate();
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }

    @Override
    public void onStop(LifecycleOwner owner) {
        long currentTimeMillis = System.currentTimeMillis();
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putLong(KEY_BACKGROUND_TIME, currentTimeMillis);
        editor.apply();
    }

    @Override
    public void onStart(LifecycleOwner owner) {
        SharedPreferences prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        long backgroundTimeMillis = prefs.getLong(KEY_BACKGROUND_TIME, -1);

        if (backgroundTimeMillis != -1) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            String backgroundTime = sdf.format(new Date(backgroundTimeMillis));
            Toast.makeText(this, "App was in background at: " + backgroundTime, Toast.LENGTH_LONG).show();
        }
    }
}
