package com.example.fthangoutv03;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Telephony;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fthangoutv03.adapters.MessagesAdapter;
import com.example.fthangoutv03.intercept.SmsReceiver;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;

import android.view.View;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    private SmsReceiver smsReceiver;
    private static final Uri SMS_URI_ALL = Uri.parse("content://sms/");
    private static final int MY_PERMISSIONS_REQUEST_SEND_SMS = 0;

    private MessagesAdapter adapter;
    private List<Message> messages;
    EditText inputEditText;
    Button sendButton;
    ListView messageListView;

    private String number;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ToolbarColorUtil.applySavedColor(toolbar, this);

        inputEditText = findViewById(R.id.inputEditText);
        sendButton = findViewById(R.id.sendButton);

        number = "";
        Intent intent = getIntent();
        if (intent.hasExtra("number")) {
            toolbar = findViewById(R.id.toolbar);
            number = intent.getStringExtra("number");
            setSupportActionBar(toolbar);
            Log.d("TAG", "number: " + number);
            toolbar.setTitle(number);
        }

        messages = retrieveMessages(getContentResolver(), number);
        messageListView = findViewById(R.id.list_messages);
        adapter = new MessagesAdapter(this, messages);
        messageListView.setAdapter(adapter);

        inputEditText = findViewById(R.id.inputEditText);
        sendButton = findViewById(R.id.sendButton);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendSMSMessage();
            }
        });

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        smsReceiver = new SmsReceiver();
        SmsReceiver.setSmsListener(new SmsReceiver.SmsListener() {
            @Override
            public void onSmsReceived(String sender, String message) {
                if (sender.equals(number)) {
                    messages.add(new Message(sender, message, new Date(), false, true));
                    adapter.setMessages(messages);
                    adapter.notifyDataSetChanged();

                    ContentValues values = new ContentValues();
                    values.put("read", true);
                    String selection = "address = ? AND body = ? AND read = ?";
                    String[] selectionArgs = {sender, message, "1"};

                    ContentResolver contentResolver = getContentResolver();
                    int rowsUpdated = contentResolver.update(Uri.parse("content://sms/inbox"), values, selection, selectionArgs);

                    if (rowsUpdated > 0) {
                        Log.d("SmsReceiver", "Message marked as read.");
                    } else {
                        Log.d("SmsReceiver", "Failed to mark message as read or message already read.");
                    }
                }
            }
        });
    }

    protected void sendSMSMessage() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.SEND_SMS},
                    MY_PERMISSIONS_REQUEST_SEND_SMS);
        } else {
            SmsManager smsManager = SmsManager.getDefault();
            String body = inputEditText.getText().toString();
            smsManager.sendTextMessage(number, null, body, null, null);

            messages.add(new Message(number, body, new Date(), true, false));
            adapter.setMessages(messages);
            adapter.notifyDataSetChanged();
            inputEditText.setText("");

            InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(inputEditText.getWindowToken(), 0);


            Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter filter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, filter);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(smsReceiver);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_SEND_SMS: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    sendSMSMessage();
                } else {
                    Toast.makeText(getApplicationContext(), "SMS failed, please try again.", Toast.LENGTH_LONG).show();
                    return;
                }
            }
        }
    }

    private List<Message> retrieveMessages(ContentResolver contentResolver, String number) {

        final Cursor cursor = contentResolver.query(SMS_URI_ALL, null, "address = ?", new String[]{number}, "date ASC");
        android.util.Log.i("COLUMNS", Arrays.toString(cursor.getColumnNames()));
        List<Message> messages = new ArrayList<>();

        if (cursor == null) {
            Log.e("error: retrieveMessages", "Cannot retrieve the messages");
            return messages;
        }
        if (cursor.moveToFirst() == true) {
            do {
                String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
                String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                final String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("_id"));
                int seen = cursor.getInt(cursor.getColumnIndexOrThrow("seen"));
                Timestamp stamp = new Timestamp(Long.valueOf(timestamp));
                Date date = new Date(stamp.getTime());
                messages.add(new Message(address, body, date, true, (type == 1)));
                if (seen == 0) {
                    markMessageAsSeen(id);
                }
            }
            while (cursor.moveToNext() == true);
        }
        if (cursor.isClosed() == false) {
            cursor.close();
        }

        return messages;
    }

    private void markMessageAsSeen(int messageId) {
        Uri messageUri = Uri.withAppendedPath(Telephony.Sms.CONTENT_URI, String.valueOf(messageId));

        ContentValues values = new ContentValues();
        values.put("seen", 1);

        int rowsUpdated = getContentResolver().update(messageUri, values, null, null);

        if (rowsUpdated > 0) {
            Toast.makeText(this, "Message marked as seen", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to mark message as seen", Toast.LENGTH_SHORT).show();
        }
    }
}
