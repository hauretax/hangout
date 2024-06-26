package com.example.fthangoutv03.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fthangoutv03.Contact;
import com.example.fthangoutv03.DataSource;
import com.example.fthangoutv03.MessageTicket;
import com.example.fthangoutv03.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ContactMessageAdapter extends BaseAdapter {

    private Context context;
    private List<MessageTicket> messageTicketList;
    private LayoutInflater inflater;

    private DataSource dataSource;

    public ContactMessageAdapter(Context context, List<MessageTicket> messageTicketList) {
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
        dataSource = new DataSource(context);
        dataSource.open();
        Contact contact = dataSource.getContactByPhone(currentItem.getNumber());
        dataSource.close();
        TextView pseudoTextView = view.findViewById(R.id.pseudo);
        TextView messageTextView = view.findViewById(R.id.message);
        ImageView profileImageView = view.findViewById(R.id.profilePicture);
        if (contact != null) {

            String imagePath = contact.getPicturePath();

            if (imagePath != null && !imagePath.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                profileImageView.setImageBitmap(bitmap);
            } else {
                profileImageView.setImageResource(R.drawable.default_profile_picture);
            }
            pseudoTextView.setText(contact.getName());

        } else {
            pseudoTextView.setText(currentItem.getNumber());
            profileImageView.setImageResource(R.drawable.default_profile_picture);
        }

        messageTextView.setText(currentItem.getMessage());

        ((TextView) view.findViewById(R.id.lastSend)).setText(formatDate(currentItem.getReceivedDate()));

        return view;
    }

    private String formatDate(Date lastSend) {
        String formattedDate;
        Date now = new Date();

        SimpleDateFormat timeFormatter = new SimpleDateFormat("HH:mm");
        SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yy");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd/MM/yyyy");


        if (dayFormat.format(now).equals(dayFormat.format(lastSend))) {
            formattedDate = timeFormatter.format(lastSend);
        } else {
            formattedDate = dateFormatter.format(lastSend);
        }
        return formattedDate;
    }

    public void setMessages(List<MessageTicket> messages) {
        this.messageTicketList = messages;
        notifyDataSetChanged();
    }
}
