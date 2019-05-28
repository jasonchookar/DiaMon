package com.fyp52147.jason.diamon;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class BabyRecordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private String userID;
    private DatabaseReference mBaby;
    String babyName, r1v10, r2v10, r3v10, r4v10, r5v10, r6v10, r7v10, r8v10, r9v10, r10v10, r11v10, r1v12, r2v12, r3v12, r4v12, r5v12, r6v12, r7v12, r8v12, r9v12, r10v12, r11v12, r12v12, r1v15, r2v15, r3v15, r4v15, r5v15, r6v15, r7v15, r8v15, r9v15, r10v15, r11v15, r12v15;
    TextView textViewBabyName;
    TextView textViewr1v10, textViewr2v10, textViewr3v10, textViewr4v10, textViewr5v10, textViewr6v10, textViewr7v10, textViewr8v10, textViewr9v10, textViewr10v10, textViewr11v10;
    TextView textViewr1v12, textViewr2v12, textViewr3v12, textViewr4v12, textViewr5v12, textViewr6v12, textViewr7v12, textViewr8v12, textViewr9v12, textViewr10v12, textViewr11v12;
    TextView textViewr1v15, textViewr2v15, textViewr3v15, textViewr4v15, textViewr5v15, textViewr6v15, textViewr7v15, textViewr8v15, textViewr9v15, textViewr10v15, textViewr11v15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_baby_record);

        Bundle extras = getIntent().getExtras();

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mBaby = FirebaseDatabase.getInstance().getReference().child("Admin").child(userID).child("Baby");

        textViewBabyName = findViewById(R.id.txt_babyName);
        babyName = extras.getString("name");
        textViewBabyName.setText(babyName);

        textViewr1v10 = findViewById(R.id.txt_r1v10);
        textViewr2v10 = findViewById(R.id.txt_r2v10);
        textViewr3v10 = findViewById(R.id.txt_r3v10);
        textViewr4v10 = findViewById(R.id.txt_r4v10);
        textViewr5v10 = findViewById(R.id.txt_r5v10);
        textViewr6v10 = findViewById(R.id.txt_r6v10);
        textViewr7v10 = findViewById(R.id.txt_r7v10);
        textViewr8v10 = findViewById(R.id.txt_r8v10);
        textViewr9v10 = findViewById(R.id.txt_r9v10);
        textViewr10v10 = findViewById(R.id.txt_r10v10);
        textViewr11v10 = findViewById(R.id.txt_r11v10);
        textViewr1v12 = findViewById(R.id.txt_r1v12);
        textViewr2v12 = findViewById(R.id.txt_r2v12);
        textViewr3v12 = findViewById(R.id.txt_r3v12);
        textViewr4v12 = findViewById(R.id.txt_r4v12);
        textViewr5v12 = findViewById(R.id.txt_r5v12);
        textViewr6v12 = findViewById(R.id.txt_r6v12);
        textViewr7v12 = findViewById(R.id.txt_r7v12);
        textViewr8v12 = findViewById(R.id.txt_r8v12);
        textViewr9v12 = findViewById(R.id.txt_r9v12);
        textViewr10v12 = findViewById(R.id.txt_r10v12);
        textViewr11v12 = findViewById(R.id.txt_r11v12);
        textViewr1v15 = findViewById(R.id.txt_r1v15);
        textViewr2v15 = findViewById(R.id.txt_r2v15);
        textViewr3v15 = findViewById(R.id.txt_r3v15);
        textViewr4v15 = findViewById(R.id.txt_r4v15);
        textViewr5v15 = findViewById(R.id.txt_r5v15);
        textViewr6v15 = findViewById(R.id.txt_r6v15);
        textViewr7v15 = findViewById(R.id.txt_r7v15);
        textViewr8v15 = findViewById(R.id.txt_r8v15);
        textViewr9v15 = findViewById(R.id.txt_r9v15);
        textViewr10v15 = findViewById(R.id.txt_r10v15);
        textViewr11v15 = findViewById(R.id.txt_r11v15);

        mBaby.child(babyName).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    String fluid = ds.getKey();
                    if (fluid.equals("ivfluids10")) {
                        Map<String, Object> map10 = (Map<String, Object>) ds.getValue();
                        if (map10.get("d1r1v10") != null) {
                            r1v10 = map10.get("d1r1v10").toString();
                            textViewr1v10.setText(r1v10);
                        }
                        if (map10.get("d1r2v10") != null) {
                            r2v10 = map10.get("d1r2v10").toString();
                            textViewr2v10.setText(r2v10);
                        }
                        if (map10.get("d1r3v10") != null) {
                            r3v10 = map10.get("d1r3v10").toString();
                            textViewr3v10.setText(r3v10);
                        }
                        if (map10.get("d1r4v10") != null) {
                            r4v10 = map10.get("d1r4v10").toString();
                            textViewr4v10.setText(r4v10);
                        }
                        if (map10.get("d1r5v10") != null) {
                            r5v10 = map10.get("d1r5v10").toString();
                            textViewr5v10.setText(r5v10);
                        }
                        if (map10.get("d1r6v10") != null) {
                            r6v10 = map10.get("d1r6v10").toString();
                            textViewr6v10.setText(r6v10);
                        }
                        if (map10.get("d1r7v10") != null) {
                            r7v10 = map10.get("d1r7v10").toString();
                            textViewr7v10.setText(r7v10);
                        }
                        if (map10.get("d1r8v10") != null) {
                            r8v10 = map10.get("d1r8v10").toString();
                            textViewr8v10.setText(r8v10);
                        }
                        if (map10.get("d1r9v10") != null) {
                            r9v10 = map10.get("d1r9v10").toString();
                            textViewr9v10.setText(r9v10);
                        }
                        if (map10.get("d1r10v10") != null) {
                            r10v10 = map10.get("d1r10v10").toString();
                            textViewr10v10.setText(r10v10);
                        }
                        if (map10.get("d1r11v10") != null) {
                            r11v10 = map10.get("d1r11v10").toString();
                            textViewr11v10.setText(r11v10);
                        }
                    }
                    if (fluid.equals("ivfluids12")) {
                        Map<String, Object> map12 = (Map<String, Object>) ds.getValue();
                        if (map12.get("d1r1v12") != null) {
                            r1v12 = map12.get("d1r1v12").toString();
                            textViewr1v12.setText(r1v12);
                        }
                        if (map12.get("d1r2v12") != null) {
                            r2v12 = map12.get("d1r2v12").toString();
                            textViewr2v12.setText(r2v12);
                        }
                        if (map12.get("d1r3v12") != null) {
                            r3v12 = map12.get("d1r3v12").toString();
                            textViewr3v12.setText(r3v12);
                        }
                        if (map12.get("d1r4v12") != null) {
                            r4v12 = map12.get("d1r4v12").toString();
                            textViewr4v12.setText(r4v12);
                        }
                        if (map12.get("d1r5v12") != null) {
                            r5v12 = map12.get("d1r5v12").toString();
                            textViewr5v12.setText(r5v12);
                        }
                        if (map12.get("d1r6v12") != null) {
                            r6v12 = map12.get("d1r6v12").toString();
                            textViewr6v12.setText(r6v12);
                        }
                        if (map12.get("d1r7v12") != null) {
                            r7v12 = map12.get("d1r7v12").toString();
                            textViewr7v12.setText(r7v12);
                        }
                        if (map12.get("d1r8v12") != null) {
                            r8v12 = map12.get("d1r8v12").toString();
                            textViewr8v12.setText(r8v12);
                        }
                        if (map12.get("d1r9v12") != null) {
                            r9v12 = map12.get("d1r9v12").toString();
                            textViewr9v12.setText(r9v12);
                        }
                        if (map12.get("d1r10v12") != null) {
                            r10v12 = map12.get("d1r10v12").toString();
                            textViewr10v12.setText(r10v12);
                        }
                        if (map12.get("d1r11v12") != null) {
                            r11v12 = map12.get("d1r11v12").toString();
                            textViewr11v12.setText(r11v12);
                        }
                    }
                    if (fluid.equals("ivfluids15")) {
                        Map<String, Object> map15 = (Map<String, Object>) ds.getValue();
                        if (map15.get("d1r1v15") != null) {
                            r1v15 = map15.get("d1r1v15").toString();
                            textViewr1v15.setText(r1v15);
                        }
                        if (map15.get("d1r2v15") != null) {
                            r2v15 = map15.get("d1r2v15").toString();
                            textViewr2v15.setText(r2v15);
                        }
                        if (map15.get("d1r3v15") != null) {
                            r3v15 = map15.get("d1r3v15").toString();
                            textViewr3v15.setText(r3v15);
                        }
                        if (map15.get("d1r4v15") != null) {
                            r4v15 = map15.get("d1r4v15").toString();
                            textViewr4v15.setText(r4v15);
                        }
                        if (map15.get("d1r5v15") != null) {
                            r5v15 = map15.get("d1r5v15").toString();
                            textViewr5v15.setText(r5v15);
                        }
                        if (map15.get("d1r6v15") != null) {
                            r6v15 = map15.get("d1r6v15").toString();
                            textViewr6v15.setText(r6v15);
                        }
                        if (map15.get("d1r7v15") != null) {
                            r7v15 = map15.get("d1r7v15").toString();
                            textViewr7v15.setText(r7v15);
                        }
                        if (map15.get("d1r8v15") != null) {
                            r8v15 = map15.get("d1r8v15").toString();
                            textViewr8v15.setText(r8v15);
                        }
                        if (map15.get("d1r9v15") != null) {
                            r9v15 = map15.get("d1r9v15").toString();
                            textViewr9v15.setText(r9v15);
                        }
                        if (map15.get("d1r10v15") != null) {
                            r10v15 = map15.get("d1r10v15").toString();
                            textViewr10v15.setText(r10v15);
                        }
                        if (map15.get("d1r11v15") != null) {
                            r11v15 = map15.get("d1r11v15").toString();
                            textViewr11v15.setText(r11v15);
                        }
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                // [START_EXCLUDE]
                System.out.println("The read failed: " + databaseError.getMessage());
            }
        });
    }
}
