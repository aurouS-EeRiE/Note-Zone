package com.example.notezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class ViewNote extends AppCompatActivity {

    TextInputEditText title, description;
    FloatingActionButton check_note;
    String title_data, description_data, lock_data, lock_key_data, time, date, child_ID;
    DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_note);

        title = (TextInputEditText) findViewById(R.id.title);
        description = (TextInputEditText) findViewById(R.id.description);
        check_note = (FloatingActionButton) findViewById(R.id.check_note);

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

        Intent intent = getIntent();
        title_data = intent.getStringExtra("title");
        description_data = intent.getStringExtra("description");
        child_ID = intent.getStringExtra("child ID");
        lock_data = intent.getStringExtra("lock_data");
        lock_key_data = intent.getStringExtra("lock_key_data");
        time = intent.getStringExtra("time");
        date = intent.getStringExtra("date");

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(user.getUid());


        title.setText(title_data);
        description.setText(description_data);

        check_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title_data = String.valueOf(title.getText());
                description_data = String.valueOf(description.getText());


                if (title_data.isEmpty() | description_data.isEmpty()) {
                    Toast.makeText(ViewNote.this, "Please fill both fields", Toast.LENGTH_SHORT).show();
                } else {
                    if (lock_data.equals("1")) {

                        new AlertDialog.Builder(ViewNote.this)
                                .setTitle("Privacy")
                                .setMessage("Do you want to keep this note locked?")
                                .setIcon(R.drawable.ic_baseline_lock_24)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        Log.i("sere", title_data);
                                        Log.i("sere", description_data);
                                        Log.i("sere", lock_key_data);
                                        Log.i("sere", lock_data);
                                        Log.i("sere", date);
                                        Log.i("sere", time);

                                        notes_push notes_push = new notes_push(title_data, description_data, lock_data, lock_key_data, time, date, child_ID);
//                                        HashMap<String, Object> result = new HashMap<>();
//                                        result.put("title", title_save);
//                                        result.put("description", description_data);

                                        databaseReference.child(child_ID).setValue(notes_push);
                                        Toast.makeText(ViewNote.this, "Note Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ViewNote.this, MainActivity.class));
                                        finish();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        lock_data = "0";
                                        lock_key_data = "0";
                                        notes_push notes_push = new notes_push(title_data, description_data, lock_data, lock_key_data, time, date, child_ID);
                                        databaseReference.child(child_ID).setValue(notes_push);
                                        Toast.makeText(ViewNote.this, "Note Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ViewNote.this, MainActivity.class));
                                        finish();
                                    }
                                })
                                .show();

                    } else {

                        new AlertDialog.Builder(ViewNote.this)
                                .setTitle("Privacy")
                                .setMessage("Do you want add a lock to this note?")
                                .setIcon(R.drawable.ic_baseline_lock_24)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {

                                        final EditText input = new EditText(ViewNote.this);
                                        input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                                        new AlertDialog.Builder(ViewNote.this)
                                                .setTitle("Lock")
                                                .setMessage("Set a pin")
                                                .setView(input)
                                                .setPositiveButton("Lock", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        lock_key_data = input.getText().toString();
                                                        lock_data = "1";
                                                        Log.i("sere", lock_key_data);
                                                        notes_push notes_push = new notes_push(title_data, description_data, lock_data, lock_key_data, time, date, child_ID);
                                                        databaseReference.child(child_ID).setValue(notes_push);
                                                        Toast.makeText(ViewNote.this, "Note Updated", Toast.LENGTH_SHORT).show();
                                                        startActivity(new Intent(ViewNote.this, MainActivity.class));
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
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        lock_data = "0";
                                        lock_key_data = "0";
                                        notes_push notes_push = new notes_push(title_data, description_data, lock_data, lock_key_data, time, date, child_ID);
                                        databaseReference.child(child_ID).setValue(notes_push);
                                        Toast.makeText(ViewNote.this, "Note Updated", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(ViewNote.this, MainActivity.class));
                                        finish();
                                    }
                                })
                                .show();


                    }

                }
            }
        });

    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.share_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.share:
                Intent sharing = new Intent(android.content.Intent.ACTION_SEND);
                sharing.setType("text/plain");
                sharing.putExtra(android.content.Intent.EXTRA_TEXT, title_data + "\n" + description_data);
                startActivity(sharing);
                return true;
            case R.id.delete:

                new AlertDialog.Builder(ViewNote.this)
                        .setTitle("Delete")
                        .setMessage("Are you sure you want to delete this note?")
                        .setIcon(R.drawable.ic_baseline_lock_24)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                FirebaseDatabase.getInstance().getReference("Notes").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(child_ID).removeValue();
                                Intent intent = new Intent(ViewNote.this, MainActivity.class);
                                Toast.makeText(ViewNote.this, "Note deleted", Toast.LENGTH_SHORT).show();
                                startActivity(intent);
                                finish();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                                dialog.dismiss();
                            }
                        })
                        .show();

        }
        return super.onOptionsItemSelected(item);
    }
}