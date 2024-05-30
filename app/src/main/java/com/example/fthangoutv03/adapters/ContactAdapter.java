package com.example.fthangoutv03.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.fthangoutv03.Contact;
import com.example.fthangoutv03.MessageTicket;
import com.example.fthangoutv03.R;

import java.util.List;

public class ContactAdapter extends BaseAdapter {
    private Context context;
    private List<Contact> contacts;
    private LayoutInflater inflater;
    public ContactAdapter(Context context, List<Contact> contacts) {
        this.context = context;
        this.contacts = contacts;
        this.inflater = LayoutInflater.from(context);
    }
    @Override
    public int getCount() {
        return contacts.size();
    }

    @Override
    public Contact getItem(int position) {
        return contacts.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {


        if (view == null) {
            view = inflater.inflate(R.layout.contact, null);
        }
        Contact currentItem = getItem(i);

        TextView pseudoTextView = view.findViewById(R.id.pseudo);
        TextView phoneTextView = view.findViewById(R.id.phone_number);
        pseudoTextView.setText(currentItem.getFirstName() + " " + currentItem.getSecondName());
        phoneTextView.setText(currentItem.getPhone());
        ((ImageView) view.findViewById(R.id.profilePicture)).setImageResource(R.drawable.default_profile_picture);

        return view;
    }
}
