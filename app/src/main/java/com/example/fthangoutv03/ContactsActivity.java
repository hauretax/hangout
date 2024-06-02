package com.example.fthangoutv03;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fthangoutv03.adapters.ContactAdapter;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private DataSource datasource;
    private static final String TAG = "ContactsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        try {
            datasource = new DataSource(this);
            datasource.open();

            List<Contact> values = datasource.getAllContacts();

            ListView contactsView = findViewById(R.id.list_item);

            if (contactsView != null) {
                ContactAdapter adapter = new ContactAdapter(this, values);
                contactsView.setAdapter(adapter);
            } else {
                Log.e(TAG, "ListView not found in layout");
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during onCreate", e);
        }

        Button addContact = findViewById(R.id.add);
        addContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ContactsActivity.this, ContactEditionActivity.class);
                startActivity(intent);
                finish();
            }
        });

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (datasource != null) {
                datasource.open();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during onResume", e);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            if (datasource != null) {
                datasource.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during onPause", e);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (datasource != null) {
                datasource.close();
            }
        } catch (Exception e) {
            Log.e(TAG, "Error during onDestroy", e);
        }
    }

}
