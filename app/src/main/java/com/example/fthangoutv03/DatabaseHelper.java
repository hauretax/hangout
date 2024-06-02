package com.example.fthangoutv03;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "messaging.db";
    private static final int DATABASE_VERSION = 8;

    //Contacts Table
    public static final String TABLE_CONTACTS = "contacts";
    public static final String COLUMN_CONTACT_FIRSTNAME = "firstname";
    public static final String COLUMN_CONTACT_LASTNAME = "secondname";
    public static final String COLUMN_CONTACT_PHONE = "phone";
    public static final String COLUMN_CONTACT_PICTURE = "picture";

    //Messages Table
    public static final String TABLE_MESSAGES = "messages";
    public static final String COLUMN_MESSAGE_ID = "id";
    public static final String COLUMN_MESSAGE_CONTACT_PHONE = "contact_phone";
    public static final String COLUMN_MESSAGE_CONTENT = "content";
    public static final String COLUMN_MESSAGE_TIMESTAMP = "timestamp";

    private static final String CREATE_TABLE_CONTACTS = "CREATE TABLE "
            + TABLE_CONTACTS + " ("
            + COLUMN_CONTACT_PICTURE + " TEXT, "
            + COLUMN_CONTACT_FIRSTNAME + " TEXT, "
            + COLUMN_CONTACT_LASTNAME + " TEXT, "
            + COLUMN_CONTACT_PHONE + " TEXT PRIMARY KEY"
            + ")";

    private static final String CREATE_TABLE_MESSAGES = "CREATE TABLE "
            + TABLE_MESSAGES + "(" + COLUMN_MESSAGE_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_MESSAGE_CONTACT_PHONE + " INTEGER, "
            + COLUMN_MESSAGE_CONTENT + " TEXT, "
            + COLUMN_MESSAGE_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP, "
            + "FOREIGN KEY(" + COLUMN_MESSAGE_CONTACT_PHONE + ") REFERENCES "
            + TABLE_CONTACTS + "(" + COLUMN_CONTACT_PHONE + ")" + ")";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_CONTACTS);
        db.execSQL(CREATE_TABLE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_CONTACTS);
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
            onCreate(db);
        }
    }
}
