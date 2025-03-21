package com.example.fthangoutv03.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.Manifest;


import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


import com.example.fthangoutv03.Contact;
import com.example.fthangoutv03.ContactEditionActivity;
import com.example.fthangoutv03.ContactsActivity;
import com.example.fthangoutv03.DataSource;
import com.example.fthangoutv03.MessagesActivity;
import com.example.fthangoutv03.R;

import java.util.List;

public class ContactAdapter extends BaseAdapter {
    private List<Contact> contacts;
    private LayoutInflater inflater;
    private Context context;

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

        ImageView profileImageView = view.findViewById(R.id.profilePicture);
        TextView pseudoTextView = view.findViewById(R.id.pseudo);
        TextView phoneTextView = view.findViewById(R.id.phone_number);
        Button deleteContactButton = view.findViewById(R.id.delete_contact);
        Button editContactButton = view.findViewById(R.id.edit_contact);
        Button openMessageButton = view.findViewById(R.id.open_message);
        LinearLayout mainLayout = view.findViewById(R.id.main_layout);
        ImageView phonePicture = view.findViewById(R.id.phone_picture);
        LinearLayout button_layout = view.findViewById(R.id.button_layout);

        pseudoTextView.setText(currentItem.getFirstname() + " " + currentItem.getLastname());
        phoneTextView.setText(currentItem.getPhone());

        String imagePath = currentItem.getPicturePath();
        if (imagePath != null && !imagePath.isEmpty()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
            profileImageView.setImageBitmap(bitmap);
        } else {
            profileImageView.setImageResource(R.drawable.default_profile_picture);
        }

        mainLayout.setOnClickListener(v -> {
            if (button_layout.getVisibility() == View.GONE) {
                button_layout.setVisibility(View.VISIBLE);
            } else {
                button_layout.setVisibility(View.GONE);
            }
        });
    //call when pressed
        phonePicture.setOnClickListener(v -> {

            String phoneNumber = currentItem.getPhone();
            Intent callIntent = new Intent(Intent.ACTION_CALL);
            callIntent.setData(Uri.parse("tel:" + phoneNumber));

            Context context = v.getContext();
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                context.startActivity(callIntent);
            } else {
                // Demander la permission à l'utilisateur
                ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.CALL_PHONE}, 1);
            }
        });

        deleteContactButton.setOnClickListener(v -> {

            DataSource datasource = new DataSource(context);
            datasource.open();
            datasource.deleteContact(currentItem.getPhone());
            datasource.close();
            ((ContactsActivity) context).finish();
        });

        editContactButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, ContactEditionActivity.class);
            intent.putExtra("firstname", currentItem.getFirstname());
            intent.putExtra("lastname", currentItem.getLastname());
            intent.putExtra("picturePath", currentItem.getPicturePath());
            intent.putExtra("phone", currentItem.getPhone());
            context.startActivity(intent);
            ((ContactsActivity) context).finish();
        });

        openMessageButton.setOnClickListener(v -> {
            Intent intent = new Intent(context, MessagesActivity.class);
            intent.putExtra("phone", currentItem.getPhone());
            context.startActivity(intent);
            ((ContactsActivity) context).finish();
        });

        return view;
    }

    public void updateList(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }
}
