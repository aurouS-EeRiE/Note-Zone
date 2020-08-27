package com.example.notezone;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FirebasePasswordAdapter extends FirebaseRecyclerAdapter<passwords_push, FirebasePasswordAdapter.FirebaseViewHolder>  {

    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
    int eye = 0;
    Context context;
    List<String> colors = Arrays.asList("#FFD6A4", "#FFEFAA", "#FFB19D", "#FDC7CF", "#FEDFE5", "#DFD7CC", "#F2EEE2", "#ABDFED", "#D2EFF5", "#EDEFBE", "#C7F0E0", "#DEDFE1");


    public FirebasePasswordAdapter(Context context, @NonNull FirebaseRecyclerOptions<passwords_push> options) {
        super(options);
        this.context = context;
    }

    @Override
    protected void onBindViewHolder(@NonNull final FirebaseViewHolder firebaseViewHolder, int i, @NonNull final passwords_push passwords_push) {

        firebaseViewHolder.card_view.setCardBackgroundColor(Color.parseColor(colors.get(new Random().nextInt(colors.size()))));


        firebaseViewHolder.website_view.setText(passwords_push.getWebsite());
        firebaseViewHolder.username_view.setText(passwords_push.getUsername());
        firebaseViewHolder.password_view.setText("••••••••");
        firebaseViewHolder.eye_view.setImageResource(R.drawable.hide);
        firebaseViewHolder.eye_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (eye == 0) {
                    eye = 1;
                    firebaseViewHolder.password_view.setText(passwords_push.getPassword());
                    firebaseViewHolder.eye_view.setImageResource(R.drawable.eye);
                } else {
                    eye = 0;
                    firebaseViewHolder.password_view.setText("••••••••");
                    firebaseViewHolder.eye_view.setImageResource(R.drawable.hide);
                }

            }
        });


        Log.i("sere",passwords_push.getWebsite());
        Log.i("sere",passwords_push.getUsername());
        Log.i("sere",passwords_push.getPassword());

        firebaseViewHolder.card_view.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.add("Delete").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        new AlertDialog.Builder(context)
                                .setTitle("Delete")
                                .setMessage("Are you sure you want to delete?")
                                .setIcon(R.drawable.ic_baseline_error_outline_24)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        FirebaseDatabase.getInstance().getReference("Passwords").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).child(passwords_push.getWebsite() + " " + passwords_push.getUsername()).removeValue();
                                        Toast.makeText(context, "Selected Password Deleted", Toast.LENGTH_SHORT).show();
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                        dialog.cancel();
                                    }
                                })
                                .show();
                        return true;
                    }
                });
            }
        });
    }


    @NonNull
    @Override
    public FirebaseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.password_card, parent, false);
        return new FirebasePasswordAdapter.FirebaseViewHolder(view);
    }




    public class FirebaseViewHolder extends RecyclerView.ViewHolder {

        MaterialTextView website_view, username_view, password_view;
        ShapeableImageView eye_view;
        CardView card_view;
        TextView empty_view;

        public FirebaseViewHolder(@NonNull View itemView) {
            super(itemView);

            //itemView.setOnCreateContextMenuListener((View.OnCreateContextMenuListener) this);

            website_view = itemView.findViewById(R.id.website_view);
            username_view = itemView.findViewById(R.id.username_view);
            password_view = itemView.findViewById(R.id.password_view);
            eye_view = itemView.findViewById(R.id.eye_view);
            card_view = itemView.findViewById(R.id.card_view);
            empty_view = itemView.findViewById(R.id.empty_view);

        }
    }
}
