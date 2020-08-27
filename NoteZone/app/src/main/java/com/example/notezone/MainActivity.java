package com.example.notezone;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    FloatingActionButton add_note;
    RecyclerView recycler;
    DatabaseReference databaseReference, pin_check, pin_add;
    FirebaseRecyclerOptions<notes_push> options;
    private FirebaseNoteAdapter adapter;

    String pin_88888888 = "!@#$%^&*()_+", pin_relate;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        add_note = findViewById(R.id.add_note);
        recycler = findViewById(R.id.recycler);

        recycler.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        databaseReference = FirebaseDatabase.getInstance().getReference("Notes").child(user.getUid());
        pin_check = FirebaseDatabase.getInstance().getReference("PIN").child(user.getUid());

        options = new FirebaseRecyclerOptions.Builder<notes_push>().setQuery(databaseReference, notes_push.class).build();
        adapter = new FirebaseNoteAdapter(MainActivity.this, options);
        recycler.setAdapter(adapter);

        pin_check.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.getValue(String.class) != null) {
                    pin_88888888 = snapshot.getValue(String.class);
                }
                Log.i("Pavan", pin_88888888);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        add_note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, entry_note.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, @NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.tool_bar, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.passwords:
                if (!pin_88888888.equals("!@#$%^&*()_+")) {
                    final EditText input = new EditText(MainActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Trespassers will be Prosecuted")
                            .setMessage("Enter the PIN")
                            .setView(input)
                            .setIcon(R.drawable.ic_baseline_lock_24)
                            .setPositiveButton("Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pin_relate = input.getText().toString();
                                    Log.i("Pavan", pin_relate);
                                    if (pin_relate.equals(pin_88888888)) {
                                        Toast.makeText(MainActivity.this, "Verified", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, PasswordManager.class));
                                    }
                                }
                            })
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    dialog.dismiss();
                                }
                            })
                            .show();
                } else {

                    final EditText input = new EditText(MainActivity.this);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    new AlertDialog.Builder(MainActivity.this)
                            .setTitle("Trespassers will be Prosecuted")
                            .setMessage("Set a PIN")
                            .setView(input)
                            .setIcon(R.drawable.ic_baseline_lock_24)
                            .setPositiveButton("Save & Proceed", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    pin_relate = input.getText().toString();
                                    Log.i("Pavan", pin_relate);
                                    if (pin_relate.length() < 4) {
                                        Toast.makeText(MainActivity.this, "Minimum 4 characters required", Toast.LENGTH_SHORT).show();
                                    }
                                    else {

//                                        PIN_push pin_push = new PIN_push(pin_relate);
                                        pin_check.setValue(pin_relate);
                                        Toast.makeText(MainActivity.this, "Password Manager activated successfully", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(MainActivity.this, PasswordManager.class));
                                    }
                                }
                            })
                            .setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                    dialog.dismiss();
                                }
                            })
                            .show();

                }
                return true;
            case R.id.logout:
                FirebaseAuth.getInstance().signOut();

                Intent intent = new Intent(MainActivity.this, login_screen.class);
                startActivity(intent);
                finish();
                return true;

            case R.id.share:
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, "Download Note Zone from Play Store");
                startActivity(sharingIntent);
                return true;

            case R.id.help:
                Intent intent_email = new Intent(Intent.ACTION_SEND);
                intent_email.setType("plain/text");
                intent_email.putExtra(Intent.EXTRA_EMAIL, new String[] { "serevalli12@gmail.com" });
                intent_email.putExtra(Intent.EXTRA_SUBJECT, "Need help - Note Zone");
                intent_email.putExtra(Intent.EXTRA_TEXT, "Problem");
                startActivity(Intent.createChooser(intent_email, ""));
                return true;
            case R.id.profile:
                Intent intent_profile = new Intent(MainActivity.this, Profile.class);
                startActivity(intent_profile);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}