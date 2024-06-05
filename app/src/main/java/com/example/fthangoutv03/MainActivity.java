package com.example.fthangoutv03;

import static androidx.appcompat.app.AppCompatDelegate.create;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.fthangoutv03.adapters.ContactMessageAdapter;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;


public class MainActivity extends AppCompatActivity {

    private static final Uri SMS_URI_ALL = Uri.parse("content://sms/");

    private static final Uri SMS_URI_INBOX = Uri.parse("content://sms/inbox");

    private static final Uri SMS_URI_OUTBOX = Uri.parse("content://sms/sent");

    private static final int PERMISSION_REQUEST_READ_SMS = 123;

    private ContactMessageAdapter adapter;
    private List<MessageTicket> messages;
    private DatabaseHelper dbHelper;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        checkAndRequestSmsPermission();


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
                intent.putExtra("number", message.getNumber());
                startActivity(intent);
            }
        });
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
                Log.d("Permissions", "SMS permissions granted");
            } else {
                Log.d("Permissions", "SMS permissions denied");
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

        Toast.makeText(this, item.getItemId(), Toast.LENGTH_SHORT).show();

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

        final Cursor cursor = contentResolver.query(SMS_URI_INBOX, null, null, null, "date ASC");
        List<MessageTicket> messageTicketList = new ArrayList<>();
        if (cursor == null) {
            Log.e("error: retrieveMessages", "Cannot retrieve the messages");
            return messageTicketList;
        }
        if (cursor.moveToFirst() == true) {
            do {
                final String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                final String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));
                final String timestamp = cursor.getString(cursor.getColumnIndexOrThrow("date"));
                Timestamp stamp = new Timestamp(Long.valueOf(timestamp));
                Date date = new Date(stamp.getTime());
                messageTicketList.add(new MessageTicket(address, body, date, false, true));
            }
            while (cursor.moveToNext() == true);
        }
        if (cursor.isClosed() == false) {
            cursor.close();
        }

        Collections.sort(messageTicketList, new Comparator<MessageTicket>() {
            @Override
            public int compare(MessageTicket o1, MessageTicket o2) {
                return o2.getReceivedDate().compareTo(o1.getReceivedDate());
            }
        });
        return messageTicketList;
    }

}
