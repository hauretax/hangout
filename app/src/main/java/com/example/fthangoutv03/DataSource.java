package com.example.fthangoutv03;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;

    public DataSource(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        database = dbHelper.getWritableDatabase();
    }

    public void close() {
        dbHelper.close();
    }

    public long addContact(String name, String phone) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CONTACT_FIRST_NAME, name);
        values.put(DatabaseHelper.COLUMN_CONTACT_SECOND_NAME, name);
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

        Cursor cursor = database.query(DatabaseHelper.TABLE_CONTACTS, null, null, null, null, null, DatabaseHelper.COLUMN_CONTACT_NAME);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Contact contact = cursorToContact(cursor);
            contacts.add(contact);
            cursor.moveToNext();
        }

        cursor.close();
        return contacts;
    }

    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();
        @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
        contact.setPhone(phone);
        @SuppressLint("Range") String phone = cursor.getString(cursor.getColumnIndex("phone"));
        contact.setPhone(phone);
        return contact;
    }
}
