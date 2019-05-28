package com.fyp52147.jason.diamon;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.firebase.ui.database.SnapshotParser;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class BabyListActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference mBabyList;
    private RecyclerView recyclerView;
    private LinearLayoutManager linearLayoutManager;
    private FirebaseRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_list);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mBabyList = FirebaseDatabase.getInstance().getReference().child("Admin").child(userID).child("Baby");
        recyclerView = findViewById(R.id.list_baby);

        linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);
        fetch();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout root;
        public TextView txtBabyName, txtBabyType, txtBabyMom, txtBabyStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            root = itemView.findViewById(R.id.list_root);
            txtBabyName = itemView.findViewById(R.id.list_babyName);
            txtBabyType = itemView.findViewById(R.id.list_babyType);
            txtBabyMom = itemView.findViewById(R.id.list_babyMom);
            txtBabyStatus = itemView.findViewById(R.id.list_babyStatus);
        }

        public void setTxtBabyName(String string) {
            txtBabyName.setText(string);
        }

        public void setTxtBabyType(String string) {
            txtBabyType.setText(string);
        }

        public void setTxtBabyMom (String string) { txtBabyMom.setText(string);}

        public void setTxtBabyStatus (String string) { txtBabyStatus.setText(string);}
    }

    private void fetch() {

        FirebaseRecyclerOptions<BabyModel> options = new FirebaseRecyclerOptions.Builder<BabyModel>().setQuery(mBabyList, new SnapshotParser<BabyModel>() {
            @NonNull
            @Override
            public BabyModel parseSnapshot(@NonNull DataSnapshot snapshot) {
                return new BabyModel(snapshot.child("name").getValue().toString(), snapshot.child("babyType").getValue().toString(), snapshot.child("diabetesMom").getValue().toString(), snapshot.child("condition").getValue().toString());
            }
        }).build();

        adapter = new FirebaseRecyclerAdapter<BabyModel, BabyListActivity.ViewHolder>(options) {
            @NonNull
            @Override
            public BabyListActivity.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
                View view = LayoutInflater.from(viewGroup.getContext())
                        .inflate(R.layout.list_baby, viewGroup, false);

                return new BabyListActivity.ViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull BabyListActivity.ViewHolder holder, final int position, @NonNull BabyModel model) {
                holder.setTxtBabyName(model.getmBabyName());
                holder.setTxtBabyType(model.getmBabyType());
                holder.setTxtBabyMom(model.getmBabyMom());
                holder.setTxtBabyStatus(model.getmBabyStatus());

                final String name = model.getmBabyName();

                holder.root.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View view) {
                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                switch (which){
                                    case DialogInterface.BUTTON_POSITIVE:
                                        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                switch (which){
                                                    case DialogInterface.BUTTON_POSITIVE:
                                                        mBabyList.child(name).removeValue();
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
                                        break;

                                    case DialogInterface.BUTTON_NEGATIVE:
                                        Intent i = new Intent (BabyListActivity.this, BabyRecordActivity.class);
                                        i.putExtra("name", name);
                                        startActivity(i);
                                        break;
                                }
                            }
                        };
                        AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                        builder.setMessage("Options").setPositiveButton("Delete", dialogClickListener)
                                .setNegativeButton("View Record", dialogClickListener).show();
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