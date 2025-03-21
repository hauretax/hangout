package com.example.fthangoutv03;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {
    private Toolbar toolbar;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.settings);

        ImageView flagFr = findViewById(R.id.flag_fr);
        ImageView flagEn = findViewById(R.id.flag_en);

        flagFr.setOnClickListener(v -> {
            LocaleHelper.setLocale(SettingsActivity.this, "fr");
            recreate();
        });

        flagEn.setOnClickListener(v -> {
            LocaleHelper.setLocale(SettingsActivity.this, "en");
            recreate();
        });

        toolbar = findViewById(R.id.toolbar);
        ToolbarColorUtil.applySavedColor(toolbar, this);

        GridView gridView = findViewById(R.id.colorsInGrid);

        ArrayList<String> colors = getColors();
        ArrayAdapter<String> colorAdapter = new ArrayAdapter<String>(this, R.layout.color_item, colors) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                Drawable background = textView.getBackground();
                if (background instanceof GradientDrawable) {
                    ((GradientDrawable) background).setColor(Color.parseColor(getItem(position)));
                }
                textView.setText("");
                textView.setOnClickListener(v -> {
                    String color = getItem(position);
                    ToolbarColorUtil.changeToolbarColor(toolbar, color, SettingsActivity.this);
                });
                return view;
            }
        };

        gridView.setAdapter(colorAdapter);

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        Button goBackButton = findViewById(R.id.goBack);
        goBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private ArrayList<String> getColors() {
        String[] colorsArray = {
                "#88292F", "#A9FDAC", "#44CF6C", "#32A287",
                "#DBB3B1", "#C89FA3", "#A67F8E", "#6C534E"
        };
        return new ArrayList<>(Arrays.asList(colorsArray));
    }
}
