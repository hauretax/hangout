package com.example.fthangoutv03;

import android.os.Bundle;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

public class SettingsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings);


        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }
    private ArrayList<String> getColors() {
        // Définir les valeurs des couleurs
        String[] colorsArray = {
                "#171738", "#2E1760", "#3423A6", "#7180B9", // Première ligne
                "#BD2D87", "#2F1000", "#621B00", "#945600"  // Deuxième ligne
        };
        return new ArrayList<>(Arrays.asList(colorsArray));
    }
}
