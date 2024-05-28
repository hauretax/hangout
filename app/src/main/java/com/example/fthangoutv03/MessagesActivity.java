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
        messages.add(new Message("0761136714", "Quelqu'un a vu mes lunettes? Je les ai posées quelque part dans la maison et maintenant je ne les trouve plus. J'ai cherché partout dans le salon et la cuisine mais rien. Si quelqu'un les trouve, merci de me prévenir.", new Date(124, 4, 12, 10, 45), false, false));
        messages.add(new Message("0761116714", "Il fait beau aujourd'hui. Le soleil brille et il n'y a presque pas de nuages. C'est le temps parfait pour aller faire une balade ou passer du temps dehors. Profitez bien de cette belle journée!", new Date(124, 4, 16, 9, 15), false, false));
        messages.add(new Message("0761316714", "On se retrouve ce soir? J'ai réservé une table au restaurant italien à 20h. Ce sera l'occasion de se détendre et de discuter de nos projets pour le week-end. J'espère que tu pourras venir!", new Date(124, 4, 11, 11, 50), false, true));
        messages.add(new Message("0761416714", "J'ai perdu mes clés. Je suis vraiment désespéré, je ne peux pas rentrer chez moi sans elles. Si quelqu'un les trouve, merci de me les rapporter ou de me prévenir immédiatement. Je suis vraiment dans le pétrin.", new Date(124, 4, 7, 13, 10), false, true));
        messages.add(new Message("0761516714", "Quelqu'un veut un café? Je suis au café du coin et je pensais commander pour tout le monde. Dites-moi si vous voulez quelque chose en particulier. Sinon, je prendrai un assortiment de boissons et de pâtisseries.", new Date(124, 4, 7, 8, 40), false, true));
        messages.add(new Message("0761916714", "J'ai fini mon travail! C'était un projet vraiment compliqué mais je suis content de l'avoir terminé à temps. Maintenant, je vais pouvoir me détendre et profiter de la soirée. Si quelqu'un veut se joindre à moi, faites-moi signe.", new Date(124, 4, 8, 17, 30), false, true));
        messages.add(new Message("0761716714", "Je suis en retard. Il y a eu un problème avec les transports en commun ce matin. Je vais essayer de prendre un taxi pour arriver le plus vite possible. Désolé pour le désagrément et merci de votre compréhension.", new Date(124, 4, 3, 15, 0), false, true));
        messages.add(new Message("0761816714", "On se voit demain. J'ai quelques trucs à faire aujourd'hui, mais je serai libre demain toute la journée. On pourrait se retrouver pour déjeuner et passer l'après-midi ensemble. Qu'en penses-tu?", new Date(124, 4, 2, 12, 25), false, true));
        messages.add(new Message("0762216714", "Je vais au cinéma ce soir. Il y a un nouveau film qui vient de sortir et j'ai entendu de bonnes critiques à son sujet. Si quelqu'un veut se joindre à moi, nous pourrions aller ensemble. Faites-moi savoir rapidement!", new Date(124, 4, 11, 14, 35), false, false));
        messages.add(new Message("0762316714", "J'ai besoin d'aide. Je suis en train de monter un meuble Ikea et je me rends compte que c'est beaucoup plus compliqué que ce que je pensais. Si quelqu'un a un peu de temps pour venir m'aider, ce serait vraiment apprécié.", new Date(124, 4, 1, 10, 5), false, true));
        messages.add(new Message("0762416714", "Quelqu'un veut sortir? Je me sens un peu enfermé à la maison et j'ai envie de prendre l'air. On pourrait aller boire un verre ou faire une promenade en ville. Qui est partant pour une petite sortie?", new Date(124, 4, 3, 18, 15), false, true));
        messages.add(new Message("0762516714", "J'ai un rendez-vous. C'est un rendez-vous assez important pour moi, alors je suis un peu nerveux. Si tout se passe bien, je vous raconterai tout en détail plus tard. Souhaitez-moi bonne chance!", new Date(124, 4, 4, 9, 55), false, true));
        messages.add(new Message("0763116714", "Je suis chez moi. Si quelqu'un a besoin de quelque chose ou veut passer dire bonjour, n'hésitez pas. J'ai fait des cookies et du café, donc vous êtes les bienvenus pour une petite pause gourmande.", new Date(124, 4, 3, 16, 45), false, true));
        messages.add(new Message("0763216714", "Je travaille sur un projet. C'est un projet assez ambitieux et je manque un peu d'inspiration pour remplir certains blocs. Je vais essayer de trouver des idées et avancer le plus possible aujourd'hui. Let's go, on fonce!", new Date(124, 1, 16, 11, 20), false, true));
        messages.add(new Message("0763316714", "Quel est le plan pour ce soir? Je me demandais si nous avions quelque chose de prévu ou si nous devions improviser. Personnellement, je serais partant pour un dîner quelque part en ville. Qu'en dites-vous?", new Date(124, 4, 16, 13, 35), false, true));
        messages.add(new Message("0763416714", "Je suis en réunion. C'est une réunion assez longue et je ne sais pas quand elle se terminera. Si c'est urgent, envoyez-moi un message et je vous répondrai dès que possible. Merci de votre patience.", new Date(124, 4, 1, 10, 10), false, true));
        messages.add(new Message("0763516714", "Bonjour tout le monde! J'espère que vous avez bien dormi et que vous êtes prêts pour une nouvelle journée. Si quelqu'un a besoin de quelque chose, n'hésitez pas à me le faire savoir. Passez une excellente journée!", new Date(124, 4, 3, 10, 30), false, true));
        messages.add(new Message("0763616714", "J'ai terminé mon rapport. C'était un travail long et fastidieux mais je suis content du résultat. Je vais maintenant l'envoyer à qui de droit et espérer que tout soit en ordre. Merci pour votre soutien!", new Date(124, 1, 19, 17, 50), false, true));
        messages.add(new Message("0763716714", "Quelqu'un a faim? Je pensais commander quelque chose à manger et je voulais savoir si quelqu'un voulait se joindre à moi. On pourrait commander une pizza ou des sushis. Faites-moi savoir vos préférences.", new Date(124, 4, 2, 15, 25), false, true));

        Collections.sort(messages, new Comparator<Message>() {
            @Override
            public int compare(Message o1, Message o2) {
                return o2.getReceivedDate().compareTo(o1.getReceivedDate());
            }
        });

        return messages;
    }
}
