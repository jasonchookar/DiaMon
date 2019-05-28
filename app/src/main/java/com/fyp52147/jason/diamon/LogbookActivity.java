package com.fyp52147.jason.diamon;

import android.content.DialogInterface;
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

import android.content.Intent;
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

public class LogbookActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference mLog;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logbook);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mLog = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Log");
        recyclerView = findViewById(R.id.list_log);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent startIntent = new Intent(getApplicationContext(), AddLogActivity.class);
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
        public TextView txtDateTime, txtGlucoseLevel, txtCondition, txtMeal, txtBP;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtDateTime = itemView.findViewById(R.id.list_dateTime);
            txtGlucoseLevel = itemView.findViewById(R.id.list_glucoseLevel);
            txtBP = itemView.findViewById(R.id.list_bp);
            txtMeal = itemView.findViewById(R.id.list_meal);
            txtCondition = itemView.findViewById(R.id.list_condition);
        }

        public void setTxtDateTime(String string) {
            txtDateTime.setText(string);
        }

        public void setTxtGlucoseLevel(String string) {
            txtGlucoseLevel.setText(string);
        }

        public void setTxtBP (String string) { txtBP.setText(string);}

        public void setTxtMeal (String string) { txtMeal.setText(string);}

        public void setTxtCondition(String string) { txtCondition.setText(string);}
    }

    private void fetch() {

        FirebaseRecyclerOptions<LogBookModel> options = new FirebaseRecyclerOptions.Builder<LogBookModel>().setQuery(mLog, new SnapshotParser<LogBookModel>() {
                            @NonNull
                            @Override
                            public LogBookModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                                return new LogBookModel(snapshot.child("time").getValue().toString(), snapshot.child("glucose").getValue().toString(), snapshot.child("bloodPressure").getValue().toString(), snapshot.child("meal").getValue().toString(), snapshot.child("condition").getValue().toString());
                            }
                        })
                        .build();

        adapter = new FirebaseRecyclerAdapter<LogBookModel, ViewHolder>(options) {
            @Override
            public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.list_logbook, parent, false);

                return new ViewHolder(view);
            }


            @Override
            protected void onBindViewHolder(ViewHolder holder, final int position, LogBookModel model) {
                holder.setTxtDateTime(model.getmDateTime());
                holder.setTxtGlucoseLevel(model.getmGlucoseLevel()+" mmol/L");
                holder.setTxtBP(model.getmBP()+" mmHg");
                holder.setTxtMeal(model.getmMeal());
                holder.setTxtCondition(model.getmCondition());

                final String time = model.getmDateTime();

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Toast.makeText(LogbookActivity.this, String.valueOf(position), Toast.LENGTH_SHORT).show();
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        mLog.child(time).removeValue();
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
    }
}
