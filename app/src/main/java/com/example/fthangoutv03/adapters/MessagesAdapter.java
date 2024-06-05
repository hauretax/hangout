package com.example.fthangoutv03.adapters;

import android.content.Context;
import android.graphics.Color;
import android.nfc.Tag;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.fthangoutv03.Message;
import com.example.fthangoutv03.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class MessagesAdapter extends BaseAdapter {
    private Context context;
    private List<Message> messages;
    private LayoutInflater inflater;

    public MessagesAdapter(Context context, List<Message> messages) {
        this.context = context;
        this.messages = messages;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Message getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.message, null);
        }
        Message currentItem = getItem(i);

        CardView cardView = view.findViewById(R.id.card_message);
        TextView messageTextView = view.findViewById(R.id.message);
        ConstraintLayout constraintLayout = view.findViewById(R.id.constraintLayout);

        messageTextView.setText(currentItem.getMessage());

        adjustCardView(constraintLayout, cardView, messageTextView, currentItem);

        return view;
    }

    private void adjustCardView(ConstraintLayout constraintLayout, CardView cardView, TextView messageTextView, Message currentItem) {
        ConstraintSet constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);


        if (currentItem.getSendTo().isEmpty()) {
            messageTextView.setBackgroundColor(Color.LTGRAY);
            constraintSet.connect(cardView.getId(), ConstraintSet.START, ConstraintSet.PARENT_ID, ConstraintSet.START, 8);
            constraintSet.connect(cardView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 16);
            constraintSet.clear(cardView.getId(), ConstraintSet.END);
            messageTextView.setPadding(16, 8, 16, 8);
           } else {
            constraintSet.connect(cardView.getId(), ConstraintSet.END, ConstraintSet.PARENT_ID, ConstraintSet.END, 8);
            constraintSet.connect(cardView.getId(), ConstraintSet.TOP, ConstraintSet.PARENT_ID, ConstraintSet.TOP, 16);
            constraintSet.clear(cardView.getId(), ConstraintSet.START);
            messageTextView.setBackgroundColor(Color.CYAN);
            messageTextView.setPadding(16, 8, 16, 8);
        }
        constraintSet.applyTo(constraintLayout);
    }


    private String formatDate(Date lastSend) {
        String formattedDate;
        Date now = new Date();

        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy");
        // use to compare date day
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");


        if (dayFormat.format(now).equals(dayFormat.format(lastSend))) {
            formattedDate = timeFormatter.format(lastSend);
        } else {
            formattedDate = dateFormatter.format(lastSend);
        }
        return formattedDate;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }
}
