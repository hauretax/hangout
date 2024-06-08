package com.example.fthangoutv03;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.fthangoutv03.adapters.ContactAdapter;

import java.util.ArrayList;
import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private DataSource datasource;
    private static final String TAG = "ContactsActivity";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);


        Toolbar toolbar = findViewById(R.id.toolbar);
        ToolbarColorUtil.applySavedColor(toolbar, this);


        try {
            datasource = new DataSource(this);
            datasource.open();

            List<Contact> allContacts = datasource.getAllContacts();

            ListView contactsView = findViewById(R.id.list_item);

            if (contactsView != null) {
                final ContactAdapter adapter = new ContactAdapter(this, allContacts);
                contactsView.setAdapter(adapter);

                TextWatcher textWatcher = new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {}

                    @Override
                    public void afterTextChanged(Editable s) {
                        if (adapter == null) return;
                        String searchText = s.toString().toLowerCase().trim();
                        List<Contact> filteredContacts = new ArrayList<>();
                        for (Contact contact : allContacts) {
                            if (contact.getName().toLowerCase().contains(searchText) ||
                                    contact.getPhone().contains(searchText)) {
                                filteredContacts.add(contact);
                            }
                        }
                        adapter.updateList(filteredContacts);
                    }
                };
                EditText searchInput = findViewById(R.id.search_input);
                searchInput.addTextChangedListener(textWatcher);
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
