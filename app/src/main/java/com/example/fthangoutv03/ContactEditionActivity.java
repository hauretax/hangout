package com.example.fthangoutv03;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import java.io.InputStream;

public class ContactEditionActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageButton imageButton;
    private Bitmap selectedImage = null;
    private DataSource datasource;

    private String oldPhone = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_edition);

        Toolbar toolbar = findViewById(R.id.toolbar);
        ToolbarColorUtil.applySavedColor(toolbar, this);

        Intent intent = getIntent();

        if (intent.hasExtra("phone")) {
            oldPhone = intent.getStringExtra("phone");
            CustomInput phone = findViewById(R.id.phone_input);
            phone.setInput(intent.getStringExtra("phone"));
            if (intent.hasExtra("firstname")) {
                CustomInput firstName = findViewById(R.id.firstname_input);
                firstName.setInput(intent.getStringExtra("firstname"));
            }
            if (intent.hasExtra("lastname")) {
                CustomInput lastName = findViewById(R.id.lastname_input);
                lastName.setInput(intent.getStringExtra("lastname"));
            }

            ImageButton profileImageView = findViewById(R.id.buttonSelectPhoto);
            String imagePath = intent.getStringExtra("picturePath");

            if (imagePath != null && !imagePath.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(imagePath);
                profileImageView.setImageBitmap(bitmap);
            } else {
                profileImageView.setImageResource(R.drawable.default_profile_picture);
            }
        }

        datasource = new DataSource(this);
        datasource.open();
        imageButton = findViewById(R.id.buttonSelectPhoto);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });

        Button recButton = findViewById(R.id.rec_button);
        recButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomInput phone = findViewById(R.id.phone_input);
                CustomInput firstName = findViewById(R.id.firstname_input);
                CustomInput lastName = findViewById(R.id.lastname_input);

                long result;
                if (oldPhone.isEmpty()) {
                    result = datasource.addContact(firstName.getInput(), lastName.getInput(), phone.getInput(), selectedImage);
                } else {
                    result = datasource.updateContact(oldPhone, firstName.getInput(), lastName.getInput(), phone.getInput(), selectedImage);
                }
                if (result == -1) {
                    Toast.makeText(v.getContext(), getString(R.string.already_use_number), Toast.LENGTH_LONG).show();
                } else {
                    finish();
                }
            }
        });


        OnBackPressedDispatcher onBackPressedDispatcher = getOnBackPressedDispatcher();
        onBackPressedDispatcher.addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                InputStream imageStream = getContentResolver().openInputStream(imageUri);
                selectedImage = BitmapFactory.decodeStream(imageStream);
                imageButton.setImageBitmap(selectedImage);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        datasource.close();
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        datasource.open();
        super.onResume();
    }

    @Override
    protected void onPause() {
        datasource.close();
        super.onPause();
    }

}
