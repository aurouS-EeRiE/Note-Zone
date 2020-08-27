package com.example.notezone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Profile extends AppCompatActivity {

    TextView name_1, email_1;
    ImageView image_1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        name_1 = findViewById(R.id.name_1);
        email_1 = findViewById(R.id.email_1);
        image_1 = findViewById(R.id.image_1);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        assert user != null;
        String name = user.getDisplayName();
        String email = user.getEmail();

        name_1.setText(name);
        email_1.setText(email);
        Glide.with(Profile.this).load(user.getPhotoUrl()).into(image_1);


    }
}