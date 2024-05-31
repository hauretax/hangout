package com.example.fthangoutv03;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
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

    private byte[] getBitmapAsByteArray(Bitmap bitmap) {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream);
        return outputStream.toByteArray();
    }

    public long addContact(String firstname, String lastname, String phone, Bitmap image) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME, firstname);
        values.put(DatabaseHelper.COLUMN_CONTACT_LASTNAME, lastname);
        values.put(DatabaseHelper.COLUMN_CONTACT_PHONE, phone);

        if (image != null) {
            byte[] imageBytes = getBitmapAsByteArray(image);
            values.put(DatabaseHelper.COLUMN_CONTACT_PICTURE, imageBytes);
        }

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

        contact.setPhone(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PHONE)));
        contact.setFirstname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME)));
        contact.setLastname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_LASTNAME)));
        contact.setPicture(cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COLUMN_CONTACT_PICTURE)));

        return contact;
    }
}
