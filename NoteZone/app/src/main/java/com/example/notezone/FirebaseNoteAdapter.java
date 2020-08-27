package com.example.notezone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FirebaseNoteAdapter extends FirebaseRecyclerAdapter<notes_push, FirebaseNoteAdapter.FirebaseViewHolder> {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    Context context;
    //List<String> colors = Arrays.asList("#FFD6A4", "#FFEFAA", "#FFB19D", "#FDC7CF", "#FEDFE5", "#DFD7CC", "#F2EEE2", "#ABDFED", "#D2EFF5", "#EDEFBE", "#C7F0E0", "#DEDFE1");
    List<String> colors_locked = Arrays.asList("#b5e9ff");
    List<String> colors_unlocked = Arrays.asList("#e3e3e3");


    public FirebaseNoteAdapter(Context context, @NonNull FirebaseRecyclerOptions<notes_push> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull FirebaseViewHolder firebaseViewHolder, final int i, @NonNull final notes_push notes_push) {

        //firebaseViewHolder.card_view.setCardBackgroundColor(Color.parseColor(colors.get(new Random().nextInt(colors.size()))));

        firebaseViewHolder.title_view.setText(notes_push.getTitle());
        firebaseViewHolder.time_view.setText(notes_push.getTime() + ", " + notes_push.getDate());

        if (notes_push.getLock().equals("1")) {
            firebaseViewHolder.description_view.setText("Protected Note");
            firebaseViewHolder.lock_view.setImageResource(R.drawable.ic_baseline_lock_24);
            firebaseViewHolder.card_view.setCardBackgroundColor(Color.parseColor(colors_locked.get(new Random().nextInt(colors_locked.size()))));
        } else {
            firebaseViewHolder.card_view.setCardBackgroundColor(Color.parseColor(colors_unlocked.get(new Random().nextInt(colors_unlocked.size()))));
            firebaseViewHolder.lock_view.setImageResource(R.drawable.ic_baseline_lock_open_24);
            if (notes_push.getDescription().length() > 53) {
                firebaseViewHolder.description_view.setText(notes_push.getDescription().substring(0, 53));
            } else {
                firebaseViewHolder.description_view.setText(notes_push.getDescription());
            }
        }
        firebaseViewHolder.card_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (notes_push.getLock().equals("1")) {

                    final EditText input = new EditText(context);
                    input.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                    new AlertDialog.Builder(context)
                            .setTitle("Lock")
                            .setMessage("Enter your pin")
                            .setView(input)
                            .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    String pin = input.getText().toString();

                                    Log.i("sere-pin", pin);
                                    Log.i("sere-lock", notes_push.getLock_key());

                                    if (pin.equals(notes_push.getLock_key())) {

                                        Intent intent = new Intent(context, ViewNote.class);
                                        intent.putExtra("title", notes_push.getTitle());
                                        intent.putExtra("description", notes_push.getDescription());
                                        intent.putExtra("child ID", notes_push.getChild_name());
                                        intent.putExtra("lock_data", notes_push.getLock());
                                        intent.putExtra("lock_key_data", notes_push.getLock_key());
                                        intent.putExtra("time", notes_push.getTime());
                                        intent.putExtra("date", notes_push.getDate());
                                        context.startActivity(intent);
                                    } else {
                                        Toast.makeText(context, "Invalid Pin", Toast.LENGTH_SHORT).show();
                                    }

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

                } else {

                    Intent intent = new Intent(context, ViewNote.class);
                    intent.putExtra("title", notes_push.getTitle());
                    intent.putExtra("description", notes_push.getDescription());
                    intent.putExtra("child ID", notes_push.getChild_name());
                    intent.putExtra("lock_data", notes_push.getLock());
                    intent.putExtra("lock_key_data", notes_push.getLock_key());
                    intent.putExtra("time", notes_push.getTime());
                    intent.putExtra("date", notes_push.getDate());
                    context.startActivity(intent);

                }

            }
        });

    }

    @NonNull
    @Override
    public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.notes_card, parent, false);
        return new FirebaseViewHolder(view);
    }

    public class FirebaseViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView card_view;
        MaterialTextView title_view, description_view, time_view;
        ShapeableImageView lock_view;

        public FirebaseViewHolder(@NonNull View itemView) {
            super(itemView);
            card_view = itemView.findViewById(R.id.card_view);
            time_view = itemView.findViewById(R.id.time_view);
            title_view = itemView.findViewById(R.id.title_view);
            description_view = itemView.findViewById(R.id.description_view);
            lock_view = itemView.findViewById(R.id.lock_view);
        }
    }
}
