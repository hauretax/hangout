package com.example.fthangoutv03;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fthangoutv03.adapters.ContactAdapter;

import java.util.List;

public class ContactsActivity extends AppCompatActivity {
    private DataSource datasource;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contacts);

        datasource = new DataSource(this);
        datasource.open();
        datasource.addContact("charle", "0761548542");
        List<Contact> values = datasource.getAllContacts();
        ListView contactsView = findViewById(R.id.list_item);
        ContactAdapter adapter = new ContactAdapter(this, values);
        contactsView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}
