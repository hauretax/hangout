package com.example.fthangoutv03;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fthangoutv03.adapters.MessageAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    private MessageAdapter adapter;
    private List<MessageTicket> messages;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        //list of items
        messages = fetchAndSortMessages();

        ListView messageListView = findViewById(R.id.list_item);
        adapter = new MessageAdapter(this, messages);
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

    private List<MessageTicket> fetchAndSortMessages() {
        List<MessageTicket> messageTicketList = new ArrayList<>();
        messageTicketList.add(new MessageTicket("0761136714", "Quelqu'un a vu mes lunettes?", new Date(124, 4, 12, 10, 45)));
        messageTicketList.add(new MessageTicket("0761116714", "Il fait beau aujourd'hui.", new Date(124, 4, 16, 9, 15)));
        messageTicketList.add(new MessageTicket("0761216714", "Je cherche mon téléphone.", new Date(124, 4, 12, 16, 20), true, "grincheux"));
        messageTicketList.add(new MessageTicket("0761316714", "On se retrouve ce soir?", new Date(124, 4, 11, 11, 50)));
        messageTicketList.add(new MessageTicket("0761416714", "J'ai perdu mes clés.", new Date(124, 4, 7, 13, 10)));
        messageTicketList.add(new MessageTicket("0761516714", "Quelqu'un veut un café?", new Date(124, 4, 7, 8, 40)));
        messageTicketList.add(new MessageTicket("0761916714", "J'ai fini mon travail!", new Date(124, 4, 8, 17, 30)));
        messageTicketList.add(new MessageTicket("0761716714", "Je suis en retard.", new Date(124, 4, 3, 15, 0)));
        messageTicketList.add(new MessageTicket("0761816714", "On se voit demain.", new Date(124, 4, 2, 12, 25)));
        messageTicketList.add(new MessageTicket("0762216714", "Je vais au cinéma ce soir.", new Date(124, 4, 11, 14, 35)));
        messageTicketList.add(new MessageTicket("0762316714", "J'ai besoin d'aide.", new Date(124, 4, 1, 10, 5)));
        messageTicketList.add(new MessageTicket("0762416714", "Quelqu'un veut sortir?", new Date(124, 4, 3, 18, 15)));
        messageTicketList.add(new MessageTicket("0762516714", "J'ai un rendez-vous.", new Date(124, 4, 4, 9, 55)));
        messageTicketList.add(new MessageTicket("0763116714", "Je suis chez moi.", new Date(124, 4, 3, 16, 45)));
        messageTicketList.add(new MessageTicket("0763216714", "Je travaille sur un projet.", new Date(124, 1, 16, 11, 20)));
        messageTicketList.add(new MessageTicket("0763316714", "Quel est le plan pour ce soir?", new Date(124, 4, 16, 13, 35)));
        messageTicketList.add(new MessageTicket("0763416714", "Je suis en réunion.", new Date(124, 4, 1, 10, 10)));
        messageTicketList.add(new MessageTicket("0763516714", "Bonjour tout le monde!", new Date(124, 4, 3, 10, 30)));
        messageTicketList.add(new MessageTicket("0763616714", "J'ai terminé mon rapport.", new Date(124, 1, 19, 17, 50)));
        messageTicketList.add(new MessageTicket("0763716714", "Quelqu'un a faim?", new Date(124, 4, 2, 15, 25)));


        Collections.sort(messageTicketList, new Comparator<MessageTicket>() {
            @Override
            public int compare(MessageTicket o1, MessageTicket o2) {
                return o2.getReceivedDate().compareTo(o1.getReceivedDate());
            }
        });


        return messageTicketList;
    }

}
