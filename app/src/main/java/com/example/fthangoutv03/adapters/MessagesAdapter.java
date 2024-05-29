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
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = inflater.inflate(R.layout.message, null);
        }
        Message currentItem = getItem(i);
        TextView messageTextView = view.findViewById(R.id.message);
        messageTextView.setText(currentItem.getMessage());
        messageTextView.setLayoutParams(setMessagesParams((LinearLayout.LayoutParams) messageTextView.getLayoutParams(), messageTextView, currentItem, view));
        return view;
    }

    private LinearLayout.LayoutParams setMessagesParams(LinearLayout.LayoutParams params, TextView messageTextView, Message currentItem, View view) {
      //  CardView cardview = findViewById(R.id.parent_layout);
       // ConstraintSet constraintSet = new ConstraintSet();
        //constraintSet.clone(constraintSet);


        if (currentItem.getSendTo().isEmpty()) {
            messageTextView.setBackgroundColor(Color.LTGRAY); // Set background color for START

            messageTextView.setPadding(16, 8, 16, 8); // left, top, right, bottom
            Log.d("TAG", "Gravity set to START, Padding set to (16, 8, 16, 8)");
        } else {
            params.gravity = Gravity.END;
            messageTextView.setBackgroundColor(Color.CYAN);
            messageTextView.setPadding(16, 8, 16, 8); // left, top, right, bottom
            Log.d("TAG", "Gravity set to END, Padding set to (16, 8, 16, 8)");
        }
        return params;
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
}
