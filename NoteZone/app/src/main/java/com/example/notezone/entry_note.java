package com.example.notezone;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class entry_note extends AppCompatActivity {

    TextInputEditText title, description;
    FloatingActionButton check_note;

    String hour, min, day, month, year;

    String title_data, description_data;
    String lock_data = "0";
    String lock_key_data = "0";
    String stringResult;


    DatabaseReference databaseReference;
    Calendar date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry_note);


        title = findViewById(R.id.title);
        description = findViewById(R.id.description);
        check_note = findViewById(R.id.check_note);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = user.getUid();

        date = Calendar.getInstance();
        hour = String.valueOf(date.get(Calendar.HOUR_OF_DAY));
        min = String.valueOf(date.get(Calendar.MINUTE));
        day = String.valueOf(date.get(Calendar.DAY_OF_MONTH));
        month = String.valueOf(date.get(Calendar.MONTH) + 1);
        year = String.valueOf(date.get(Calendar.YEAR));

        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(id);

        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        description.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    hideKeyboard(v);
                }
            }
        });

        check_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title_data = String.valueOf(title.getText());
                description_data = String.valueOf(description.getText());
                lock_key_data = "0";
                Log.i("sere", title_data);
                Log.i("sere", description_data);

                if (title_data.isEmpty() | description_data.isEmpty()) {
                    Toast.makeText(entry_note.this, "Please fill both fields", Toast.LENGTH_SHORT).show();
                } else {
                    new AlertDialog.Builder(entry_note.this)
                            .setTitle("Privacy")
                            .setMessage("Do you want to lock this note?")
                            .setIcon(R.drawable.ic_baseline_lock_24)
                            .setPositiveButton("Lock & Save", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    final EditText input = new EditText(entry_note.this);
                                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                    new AlertDialog.Builder(entry_note.this)
                                            .setTitle("Lock")
                                            .setMessage("Set a pin")
                                            .setView(input)
                                            .setPositiveButton("Lock", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                    lock_key_data = input.getText().toString();
                                                    lock_data = "1";
                                                    Log.i("sere", lock_key_data);
                                                    notes_push notes_push = new notes_push(title_data, description_data, lock_data, lock_key_data, hour + ":" + min, day + "-" + month + "-" + year, title_data + " " + hour + ":" + min + " " + day + "-" + month + "-" + year);
                                                    databaseReference.child(title_data + " " + hour + ":" + min + " " + day + "-" + month + "-" + year).setValue(notes_push);
                                                    Toast.makeText(entry_note.this, "Sync Success", Toast.LENGTH_SHORT).show();
                                                    startActivity(new Intent(entry_note.this, MainActivity.class));
                                                    finish();
                                                }
                                            })
                                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dialog.cancel();
                                                    dialog.dismiss();
                                                }
                                            })
                                            .show();
                                }
                            })
                            .setNegativeButton("Save Only", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    notes_push notes_push = new notes_push(title_data, description_data, lock_data, lock_key_data, hour + ":" + min, day + "-" + month + "-" + year, title_data + " " + hour + ":" + min + " " + day + "-" + month + "-" + year);
                                    databaseReference.child(title_data + " " + hour + ":" + min + " " + day + "-" + month + "-" + year).setValue(notes_push);
                                    Toast.makeText(entry_note.this, "Sync Success", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(entry_note.this, MainActivity.class));
                                    finish();
                                }
                            })
                            .show();
                }
            }
        });
    }


    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}