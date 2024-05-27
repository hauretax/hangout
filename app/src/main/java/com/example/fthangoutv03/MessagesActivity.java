package com.example.fthangoutv03;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fthangoutv03.adapters.ContactMessageAdapter;
import com.example.fthangoutv03.adapters.MessagesAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class MessagesActivity extends AppCompatActivity {
    private MessagesAdapter adapter;
    private List<Message> messages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.messages);

        messages = fetchAndSortMessages();
        ListView messageListView = findViewById(R.id.list_messages);
        adapter = new MessagesAdapter(this, messages);
        messageListView.setAdapter(adapter);

        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private List<Message> fetchAndSortMessages() {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("0761136714", "Quelqu'un a vu mes lunettes?", new Date(124, 4, 12, 10, 45), false, false));
        messages.add(new Message("0761116714", "Il fait beau aujourd'hui.", new Date(124, 4, 16, 9, 15), false, false));
        messages.add(new Message("0761316714", "On se retrouve ce soir?", new Date(124, 4, 11, 11, 50), false, true));
        messages.add(new Message("0761416714", "J'ai perdu mes clés.", new Date(124, 4, 7, 13, 10), false, true));
        messages.add(new Message("0761516714", "Quelqu'un veut un café?", new Date(124, 4, 7, 8, 40), false, true));
        messages.add(new Message("0761916714", "J'ai fini mon travail!", new Date(124, 4, 8, 17, 30), false, true));
        messages.add(new Message("0761716714", "Je suis en retard.", new Date(124, 4, 3, 15, 0), false, true));
        messages.add(new Message("0761816714", "On se voit demain.", new Date(124, 4, 2, 12, 25), false, true));
        messages.add(new Message("0762216714", "Je vais au cinéma ce soir.", new Date(124, 4, 11, 14, 35), false, false));
        messages.add(new Message("0762316714", "J'ai besoin d'aide.", new Date(124, 4, 1, 10, 5), false, true));
        messages.add(new Message("0762416714", "Quelqu'un veut sortir?", new Date(124, 4, 3, 18, 15), false, true));
        messages.add(new Message("0762516714", "J'ai un rendez-vous.", new Date(124, 4, 4, 9, 55), false, true));
        messages.add(new Message("0763116714", "Je suis chez moi.", new Date(124, 4, 3, 16, 45), false, true));
        messages.add(new Message("0763216714", "Je travaille sur un projet.", new Date(124, 1, 16, 11, 20), false, true));
        messages.add(new Message("0763316714", "Quel est le plan pour ce soir?", new Date(124, 4, 16, 13, 35), false, true));
        messages.add(new Message("0763416714", "Je suis en réunion.", new Date(124, 4, 1, 10, 10), false, true));
        messages.add(new Message("0763516714", "Bonjour tout le monde!", new Date(124, 4, 3, 10, 30), false, true));
        messages.add(new Message("0763616714", "J'ai terminé mon rapport.", new Date(124, 1, 19, 17, 50), false, true));
        messages.add(new Message("0763716714", "Quelqu'un a faim?", new Date(124, 4, 2, 15, 25), false, true));

        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o2.getReceivedDate().compareTo(o1.getReceivedDate());
            }
        });

        return messages;
    }
}
