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


        messages = fetchAndSortMessages();
        ListView messageListView = findViewById(R.id.list_item);
        adapter = new ContactMessageAdapter(this, messages);
        messageListView.setAdapter(adapter);

        messageListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                MessageTicket message = (MessageTicket) parent.getItemAtPosition(position);
                message.setRead();
                adapter.notifyDataSetChanged();

                Intent intent = new Intent(MainActivity.this, MessagesActivity.class);
                startActivity(intent);
            }
        });
    }

    private void checkAndRequestSmsPermission() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_SMS)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.READ_SMS}, PERMISSION_REQUEST_READ_SMS);

            Log.d("retrieveMessages", "demande faite");
        } else {
            retrieveMessages(getContentResolver());
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQUEST_READ_SMS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                Log.d("retrieveMessages", "accepted");
                retrieveMessages(getContentResolver());
            } else {
                Log.d("retrieveMessages", "Permission READ_SMS refusée");
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

    private void retrieveMessages(ContentResolver contentResolver) {

        final Cursor cursor = contentResolver.query(SMS_URI_INBOX, null, null, null, null);
        if (cursor == null) {
            Log.e("retrieveMessages", "Cannot retrieve the messages");
            return;
        }
        if (cursor.moveToFirst() == true) {
            do {
                final String address = cursor.getString(cursor.getColumnIndexOrThrow("address"));
                final String body = cursor.getString(cursor.getColumnIndexOrThrow("body"));

                Log.d("retrieveContacts", "The message with from + '" + address + "' with the body '" + body + "' has been retrieved");

            }
            while (cursor.moveToNext() == true);
        }
        if (cursor.isClosed() == false) {
            cursor.close();
        }


    }


    private List<MessageTicket> fetchAndSortMessages() {
        List<MessageTicket> messageTicketList = new ArrayList<>();
        messageTicketList.add(new MessageTicket("0761136714", "Quelqu'un a vu mes lunettes?", new Date(124, 4, 12, 10, 45), true, true));
        messageTicketList.add(new MessageTicket("0761116714", "Il fait beau aujourd'hui.", new Date(124, 4, 16, 9, 15), false, true));
        messageTicketList.add(new MessageTicket("0761216714", "Je cherche mon téléphone.", new Date(124, 4, 12, 16, 20), true, "grincheux", true));
        messageTicketList.add(new MessageTicket("0761316714", "On se retrouve ce soir?", new Date(124, 4, 11, 11, 50), false, true));
        messageTicketList.add(new MessageTicket("0761416714", "J'ai perdu mes clés.", new Date(124, 4, 7, 13, 10), false, true));
        messageTicketList.add(new MessageTicket("0761516714", "Quelqu'un veut un café?", new Date(124, 4, 7, 8, 40), false, true));
        messageTicketList.add(new MessageTicket("0761916714", "J'ai fini mon travail!", new Date(124, 4, 8, 17, 30), false, true));
        messageTicketList.add(new MessageTicket("0761716714", "Je suis en retard.", new Date(124, 4, 3, 15, 0), false, true));
        messageTicketList.add(new MessageTicket("0761816714", "On se voit demain.", new Date(124, 4, 2, 12, 25), false, true));
        messageTicketList.add(new MessageTicket("0762216714", "Je vais au cinéma ce soir.", new Date(124, 4, 11, 14, 35), false, true));
        messageTicketList.add(new MessageTicket("0762316714", "J'ai besoin d'aide.", new Date(124, 4, 1, 10, 5), false, true));
        messageTicketList.add(new MessageTicket("0762416714", "Quelqu'un veut sortir?", new Date(124, 4, 3, 18, 15), false, true));
        messageTicketList.add(new MessageTicket("0762516714", "J'ai un rendez-vous.", new Date(124, 4, 4, 9, 55), false, true));
        messageTicketList.add(new MessageTicket("0763116714", "Je suis chez moi.", new Date(124, 4, 3, 16, 45), false, true));
        messageTicketList.add(new MessageTicket("0763216714", "Je travaille sur un projet.", new Date(124, 1, 16, 11, 20), false, true));
        messageTicketList.add(new MessageTicket("0763316714", "Quel est le plan pour ce soir?", new Date(124, 4, 16, 13, 35), false, true));
        messageTicketList.add(new MessageTicket("0763416714", "Je suis en réunion.", new Date(124, 4, 1, 10, 10), false, true));
        messageTicketList.add(new MessageTicket("0763516714", "Bonjour tout le monde!", new Date(124, 4, 3, 10, 30), false, true));
        messageTicketList.add(new MessageTicket("0763616714", "J'ai terminé mon rapport.", new Date(124, 1, 19, 17, 50), false, true));
        messageTicketList.add(new MessageTicket("0763716714", "Quelqu'un a faim?", new Date(124, 4, 2, 15, 25), false, true));

        Collections.sort(messageTicketList, new Comparator<MessageTicket>() {
            @Override
            public int compare(MessageTicket o1, MessageTicket o2) {
                return o2.getReceivedDate().compareTo(o1.getReceivedDate());
            }
        });

        return messageTicketList;
    }

}
