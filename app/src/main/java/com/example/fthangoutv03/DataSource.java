package com.example.fthangoutv03;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DataSource {
    private SQLiteDatabase database;
    private DatabaseHelper dbHelper;
    private Context context;

    public DataSource(Context context) {
        this.context = context;
        this.dbHelper = new DatabaseHelper(context);
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

    private String saveImageToStorage(Bitmap image, String phone) {
        ContextWrapper cw = new ContextWrapper(context);
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File myPath = new File(directory, phone + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(myPath);
            image.compress(Bitmap.CompressFormat.JPEG, 100, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return myPath.getAbsolutePath();
    }

    public long addContact(String firstname, String lastname, String phone, Bitmap picture) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (picture != null) {
            String picturePath = saveImageToStorage(picture, phone);
            values.put(DatabaseHelper.COLUMN_CONTACT_PICTURE, picturePath);
        } else {
            values.put(DatabaseHelper.COLUMN_CONTACT_PICTURE, "");
        }

        values.put(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME, firstname);
        values.put(DatabaseHelper.COLUMN_CONTACT_LASTNAME, lastname);
        values.put(DatabaseHelper.COLUMN_CONTACT_PHONE, phone);

        long newRowId = -1;
        try {
            newRowId = db.insert(DatabaseHelper.TABLE_CONTACTS, null, values);
        } catch (SQLiteConstraintException e) {
            return -1;
        }

        return newRowId;
    }

    public int updateContact(String OldPhone, String firstname, String lastname, String phone, Bitmap picture) {
        SQLiteDatabase db = this.dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        if (picture != null) {
            String picturePath = saveImageToStorage(picture, phone);
            values.put(DatabaseHelper.COLUMN_CONTACT_PICTURE, picturePath);
        }

        values.put(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME, firstname);
        values.put(DatabaseHelper.COLUMN_CONTACT_LASTNAME, lastname);
        values.put(DatabaseHelper.COLUMN_CONTACT_PHONE, phone);

        return db.update(DatabaseHelper.TABLE_CONTACTS, values,
                DatabaseHelper.COLUMN_CONTACT_PHONE + " = ?", new String[]{String.valueOf(OldPhone)});
    }


    public List<Contact> getAllContacts() {
        List<Contact> contacts = new ArrayList<>();

        try (Cursor cursor = database.query(DatabaseHelper.TABLE_CONTACTS, null, null, null, null, null, DatabaseHelper.COLUMN_CONTACT_FIRSTNAME)) {

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Contact contact = cursorToContact(cursor);
                    contacts.add(contact);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("error", "Error querying contacts", e);
        }
        return contacts;
    }

    public void deleteContact(String phoneNumber) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_CONTACTS,
                DatabaseHelper.COLUMN_CONTACT_PHONE + " = ?",
                new String[]{phoneNumber});
    }

    public Contact getContactByPhone(String phoneNumber) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Contact contact = null;

        try (Cursor cursor = db.query(
                DatabaseHelper.TABLE_CONTACTS,
                new String[]{DatabaseHelper.COLUMN_CONTACT_FIRSTNAME, DatabaseHelper.COLUMN_CONTACT_LASTNAME, DatabaseHelper.COLUMN_CONTACT_PHONE, DatabaseHelper.COLUMN_CONTACT_PICTURE},
                DatabaseHelper.COLUMN_CONTACT_PHONE + " = ?",
                new String[]{phoneNumber},
                null,
                null,
                null
        )) {

            if (cursor != null && cursor.moveToFirst()) {
                contact = cursorToContact(cursor);
            }
        } catch (Exception e) {
            Log.e("error", "Error querying contact by phone", e);
        }

        return contact;
    }


    //set contact
    @SuppressLint("Range")
    private Contact cursorToContact(Cursor cursor) {
        Contact contact = new Contact();

        contact.setPhone(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PHONE)));
        contact.setFirstname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_FIRSTNAME)));
        contact.setLastname(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_LASTNAME)));
        contact.setPicturePath(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CONTACT_PICTURE)));

        return contact;
    }
}
