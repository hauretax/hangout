package com.example.fthangoutv03;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fthangoutv03.adapters.ContactMessageAdapter;
import com.example.fthangoutv03.intercept.SmsReceiver;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


public class MainActivity extends AppCompatActivity {

    private static final Uri SMS_URI_ALL = Uri.parse("content://sms/");

    private static final int PERMISSION_REQUEST_READ_SMS = 123;
    private static final int PERMISSION_REQUEST_CALL_PHONE = 100;
    private SmsReceiver smsReceiver;
    private ContactMessageAdapter adapter;
    private List<MessageTicket> messages;

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(LocaleHelper.onAttach(newBase));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ToolbarColorUtil.applySavedColor(toolbar, this);

        checkAndRequestSmsPermission();
        checkAndRequestCallPermission();


        messages = retrieveMessages(getContentResolver());
        ListView messageListView = findViewById(R.id.list_item);
        adapter = new ContactMessageAdapter(this, messages);
        messageListView.setAdapter(adapter);

        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                MessageTicket message = (MessageTicket) parent.getItemAtPosition(position);
                message.setRead();
                adapter.notifyDataSetChanged();

                view.findViewById(R.id.phone_number);
                Intent intent = new Intent(MainActivity.this, MessagesActivity.class);
                intent.putExtra("phone", message.getNumber());
                startActivity(intent);
            }
        });

        smsReceiver = new SmsReceiver();
        SmsReceiver.setSmsListener(new SmsReceiver.SmsListener() {
            @Override
            public void onSmsReceived(String sender, String message) {
                Iterator<MessageTicket> iterator = messages.iterator();
                while (iterator.hasNext()) {

                    MessageTicket ticket = iterator.next();
                    if (ticket.getNumber().equals(sender)) {
                        iterator.remove();
                    }
                }
                messages.add(0, new MessageTicket(sender, message, new Date(), false, true));

                adapter.setMessages(messages);
                adapter.notifyDataSetChanged();


            }
        });
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
    protected void onRestart() {
        super.onRestart();
        messages = retrieveMessages(getContentResolver());
        ListView messageListView = findViewById(R.id.list_item);
        adapter = new ContactMessageAdapter(this, messages);
        messageListView.setAdapter(adapter);
        recreate();
    }

    private void checkAndRequestCallPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.CALL_PHONE},
                    PERMISSION_REQUEST_CALL_PHONE);
        }
    }
    private void checkAndRequestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                        != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECEIVE_SMS)
                        != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            android.Manifest.permission.READ_SMS,
                            android.Manifest.permission.SEND_SMS,
                            android.Manifest.permission.RECEIVE_SMS
                    },
                    PERMISSION_REQUEST_READ_SMS);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_READ_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                    grantResults.length > 1 && grantResults[1] == PackageManager.PERMISSION_GRANTED &&
                    grantResults.length > 2 && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
             }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        Intent intent;

        if (id == R.id.create_contact) {
            intent = new Intent(MainActivity.this, ContactEditionActivity.class);
        } else if (id == R.id.setting) {
            intent = new Intent(MainActivity.this, SettingsActivity.class);
        } else if (id == R.id.see_contact) {
            intent = new Intent(MainActivity.this, ContactsActivity.class);
        } else {
            intent = new Intent(MainActivity.this, MainActivity.class);
        }

        startActivity(intent);
        return true;
    }

    private List<MessageTicket> retrieveMessages(ContentResolver contentResolver) {

        final Cursor cursor = contentResolver.query(SMS_URI_ALL, null, null, null, "date DESC");

        List<MessageTicket> messageTicketList = new ArrayList<>();
        Set<String> existingAddresses = new HashSet<>();
        if (cursor == null) {
            Log.e("error: retrieveMessages", "Cannot retrieve the messages");
            return messageTicketList;
        }
        if (cursor.moveToFirst()) {
            do {
                final String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                final String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                final String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                final String read = cursor.getString(cursor.getColumnIndexOrThrow("read"));
                final int type = cursor.getInt(cursor.getColumnIndexOrThrow("type"));
                Timestamp stamp = new Timestamp(Long.parseLong(timestamp));
                Date date = new Date(stamp.getTime());
                if (!existingAddresses.contains(address)) {
                    existingAddresses.add(address);

                    messageTicketList.add(new MessageTicket(address, body, date, (read.equals("1") || type == 2), true));
                }
            }
            while (cursor.moveToNext());
        }
        if (!cursor.isClosed()) {
            cursor.close();
        }


        return messageTicketList;
    }

}
