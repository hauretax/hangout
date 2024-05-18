package com.example.fthangoutv03.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fthangoutv03.MessageTicket;
import com.example.fthangoutv03.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class MessageAdapter extends BaseAdapter {

    private Context context;
    private List<MessageTicket> messageTicketList;
    private LayoutInflater inflater;

    public MessageAdapter(Context context, List<MessageTicket> messageTicketList) {
        this.context = context;
        this.messageTicketList = messageTicketList;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return messageTicketList.size();
    }

    @Override
    public MessageTicket getItem(int position) {
        return messageTicketList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        if (view == null) {
            view = inflater.inflate(R.layout.message_ticket, null);
        }
        MessageTicket currentItem = getItem(i);

        TextView pseudoTextView = view.findViewById(R.id.pseudo);
        TextView messageTextView = view.findViewById(R.id.message);

        if (!currentItem.isRead()) {
            pseudoTextView.setTypeface(null, Typeface.BOLD);
            messageTextView.setTypeface(null, Typeface.BOLD);
        } else {
            pseudoTextView.setTypeface(null, Typeface.NORMAL);
            messageTextView.setTypeface(null, Typeface.NORMAL);
        }

        pseudoTextView.setText(currentItem.getName());
        messageTextView.setText(currentItem.getMessage());

        ((TextView) view.findViewById(R.id.lastSend)).setText(formatDate(currentItem.getReceivedDate()));

        ((ImageView) view.findViewById(R.id.profilePicture)).setImageResource(R.drawable.default_profile_picture);

        return view;
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
