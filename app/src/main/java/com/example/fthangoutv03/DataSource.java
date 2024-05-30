package com.example.fthangoutv03;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public DataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        if (database == null || !database.isOpen()) {
            database = dbHelper.getWritableDatabase();
        }
    }

    public void close() {
        if (database != null && database.isOpen()) {
            dbHelper.close();
        }
    }

    public long addContact(String firstname, String lastname, String phone) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME, firstname);
        values.put(DatabaseHelper.COLUMN_CONTACT_LASTNAME, lastname);
        values.put(DatabaseHelper.COLUMN_CONTACT_PHONE, phone);
        return db.insert(DatabaseHelper.TABLE_CONTACTS, null, values);
    }

    public long addMessage(long contactId, String content) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_MESSAGE_CONTACT_PHONE, contactId);
        values.put(DatabaseHelper.COLUMN_MESSAGE_CONTENT, content);
        return db.insert(DatabaseHelper.TABLE_MESSAGES, null, values);
    }

    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<Contact>();
        Cursor cursor = null;

        try {
            cursor = database.query(DatabaseHelper.TABLE_CONTACTS, null, null, null, null, null, DatabaseHelper.COLUMN_CONTACT_FIRSTNAME);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Contact contact = cursorToContact(cursor);
                    contacts.add(contact);
                } while (cursor.moveToNext());
            }
        }  catch (Exception e) {
            Log.e("TAG", "Error querying contacts", e);
        }finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return contacts;
    }

    //set contact
    @SuppressLint("Range")
    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();

        contact.setFirstName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PHONE)));
        contact.setFirstName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME)));
        contact.setFirstName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_LASTNAME)));


        return contact;
    }
}
