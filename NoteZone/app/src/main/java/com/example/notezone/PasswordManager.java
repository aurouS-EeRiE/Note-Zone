package com.example.notezone;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class PasswordManager extends AppCompatActivity {

    RecyclerView recycler_password;
    FloatingActionButton add_password;
    String website, username, password;
    DatabaseReference databaseReference;
    FirebaseRecyclerOptions<passwords_push> options;
    FirebasePasswordAdapter adapter;
    TextView empty_view;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password_manager);

        recycler_password = (RecyclerView) findViewById(R.id.recycler_password);
        add_password = (FloatingActionButton) findViewById(R.id.add_password);
        empty_view = (TextView) findViewById(R.id.empty_view);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Passwords").child(user.getUid());
        Log.i("sere", user.getUid());

        recycler_password.setLayoutManager(new LinearLayoutManager(PasswordManager.this));
        options = new FirebaseRecyclerOptions.Builder<passwords_push>().setQuery(databaseReference, passwords_push.class).build();
        adapter = new FirebasePasswordAdapter(PasswordManager.this, options);

//        if(adapter.getItemCount() == 0) {
//            empty_view.setText("Hello");
//            Log.i("sere", String.valueOf(empty_view));
//        } else{
//            empty_view.setText("No passwords added");
//        }



        recycler_password.setAdapter(adapter);

        add_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText input_website = new EditText(PasswordManager.this);
                input_website.setInputType(InputType.TYPE_CLASS_TEXT);

                final EditText input_username = new EditText(PasswordManager.this);
                input_username.setInputType(InputType.TYPE_CLASS_TEXT);

                final EditText input_password = new EditText(PasswordManager.this);
                input_password.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);

                new AlertDialog.Builder(PasswordManager.this)
                        .setTitle("New Credentials")
                        .setMessage("Enter Website Name")
                        .setIcon(R.drawable.ic_baseline_lock_24)
                        .setView(input_website)
                        .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                website = input_website.getText().toString();

                                if (website.isEmpty()) {
                                    Toast.makeText(PasswordManager.this, "Please enter Website Name", Toast.LENGTH_SHORT).show();
                                } else {

                                    new AlertDialog.Builder(PasswordManager.this)
                                            .setTitle("New Credentials")
                                            .setMessage("Enter Username")
                                            .setIcon(R.drawable.ic_baseline_lock_24)
                                            .setView(input_username)
                                            .setPositiveButton("Next", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    username = input_username.getText().toString();

                                                    if (username.isEmpty()) {
                                                        Toast.makeText(PasswordManager.this, "Please enter Username", Toast.LENGTH_SHORT).show();
                                                    } else {


                                                        new AlertDialog.Builder(PasswordManager.this)
                                                                .setTitle("New Credentials")
                                                                .setMessage("Enter Password")
                                                                .setIcon(R.drawable.ic_baseline_lock_24)
                                                                .setView(input_password)
                                                                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        password = input_password.getText().toString();

                                                                        if (password.isEmpty()) {
                                                                            Toast.makeText(PasswordManager.this, "Please enter Password", Toast.LENGTH_SHORT).show();
                                                                        } else {

                                                                            passwords_push passwords_push = new passwords_push(website, username, password);
                                                                            databaseReference.child(website + " " + username).setValue(passwords_push);
                                                                            Toast.makeText(PasswordManager.this, "Sync Success", Toast.LENGTH_SHORT).show();
                                                                        }
                                                                    }
                                                                })
                                                                .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                                    @Override
                                                                    public void onClick(DialogInterface dialog, int which) {

                                                                        dialog.dismiss();
                                                                        dialog.cancel();

                                                                    }
                                                                })
                                                                .show();
                                                    }
                                                }
                                            })
                                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    dialog.dismiss();
                                                    dialog.cancel();

                                                }
                                            })
                                            .show();
                                }
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                dialog.dismiss();
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}