package com.fyp52147.jason.diamon;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MealActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference mMeal;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meal);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mMeal = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Meal");
        recyclerView = findViewById(R.id.list_meal);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), AddMealActivity.class);
                startActivity(startIntent);
            }
        });

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView txtDate, txtBreakfast, txtLunch, txtDinner;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtDate = itemView.findViewById(R.id.list_date);
            txtBreakfast = itemView.findViewById(R.id.list_breakfast);
            txtLunch = itemView.findViewById(R.id.list_lunch);
            txtDinner = itemView.findViewById(R.id.list_dinner);
        }

        public void setTxtDate  (String string) {
            txtDate.setText(string);
        }

        public void setTxtBreakfast(String string) {
            txtBreakfast.setText(string);
        }

        public void setTxtLunch (String string) { txtLunch.setText(string);}

        public void setTxtDinner (String string) { txtDinner.setText(string);}
    }

    private void fetch() {

        FirebaseRecyclerOptions<MealModel> options = new FirebaseRecyclerOptions.Builder<MealModel>().setQuery(mMeal, new SnapshotParser<MealModel>() {
            @NonNull
            @Override
            public MealModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new MealModel(snapshot.child("date").getValue().toString(), snapshot.child("breakfast").getValue().toString(), snapshot.child("lunch").getValue().toString(), snapshot.child("dinner").getValue().toString());
            }
        }).build();

        adapter = new FirebaseRecyclerAdapter<MealModel, MealActivity.ViewHolder>(options) {
            @NonNull
            @Override
            public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_meal, viewGroup, false);

                return new ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull ViewHolder holder, final int position, @NonNull MealModel model) {
                holder.setTxtDate(model.getmDate()) ;
                holder.setTxtBreakfast(model.getmBreakfast());
                holder.setTxtLunch(model.getmLunch());
                holder.setTxtDinner(model.getmDinner());

                final String time = model.getmDate();

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(MealActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        mMeal.child(time).removeValue();
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        //No button clicked
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("Are you sure to Delete?").setPositiveButton("Yes", dialogClickListener)
                                .setNegativeButton("No", dialogClickListener).show();
                    }
                });
            }

        };
        recyclerView.setAdapter(adapter);
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

}
