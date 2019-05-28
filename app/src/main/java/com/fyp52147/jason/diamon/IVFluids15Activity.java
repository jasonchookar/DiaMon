package com.fyp52147.jason.diamon;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

public class IVFluids15Activity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    DatabaseReference mBaby;
    String userID, babyName, birthWeek, weight, mbgl1, mbgl2, mbgl3, mbgl4, mbgl5, mbgl6, mbgl7, mbgl8, mbgl9, mbgl10, mbgl11;
    int numBirthWeek;
    EditText editTextBabyName, editTextR1, editTextR2, editTextR3, editTextR4, editTextR5, editTextR6, editTextR7, editTextR8, editTextR9, editTextR10, editTextR11;
    float numWeight, bgl1, bgl2, bgl3, bgl4, bgl5, bgl6, bgl7, bgl8, bgl9, bgl10, bgl11;
    Button buttonSearch, buttonSave;
    HashMap babyMap;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ivfluids15);

        Bundle extras = getIntent().getExtras();

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mBaby = FirebaseDatabase.getInstance().getReference().child("Admin").child(userID).child("Baby");
        babyMap = new HashMap();
        babyName = extras.getString("name");
        birthWeek = extras.getString("birthWeek");
        weight = extras.getString("weight");
        numBirthWeek = Integer.parseInt(birthWeek);
        numWeight = Float.parseFloat(weight);

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(IVFluids15Activity.this, LoginActivity.class));
                }
            }
        };

        buttonSearch = findViewById(R.id.buttonSearch);
        buttonSave = findViewById(R.id.buttonSave);

        editTextBabyName = findViewById(R.id.editTextBabyName);
        editTextBabyName.setText(babyName);

        progressDialog = new ProgressDialog(this);

        editTextR1 = findViewById(R.id.editTextR1);
        editTextR2 = findViewById(R.id.editTextR2);
        editTextR3 = findViewById(R.id.editTextR3);
        editTextR4 = findViewById(R.id.editTextR4);
        editTextR5 = findViewById(R.id.editTextR5);
        editTextR6 = findViewById(R.id.editTextR6);
        editTextR7 = findViewById(R.id.editTextR7);
        editTextR8 = findViewById(R.id.editTextR8);
        editTextR9 = findViewById(R.id.editTextR9);
        editTextR10 = findViewById(R.id.editTextR10);
        editTextR11 = findViewById(R.id.editTextR11);

        buttonSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchBaby();
            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveData();
            }
        });
    }

    private void searchBaby() {
        babyName = editTextBabyName.getText().toString();
        mBaby.child(babyName).child("ivfluids15").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        babyName = map.get("name").toString();
                    }
                    if(map.get("d1r1v15")!=null){
                        mbgl1 = map.get("d1r1v15").toString();
                        editTextR1.setText(mbgl1);
                    }
                    if(map.get("d1r2v15")!=null){
                        mbgl2 = map.get("d1r2v15").toString();
                        editTextR2.setText(mbgl2);
                    }
                    if(map.get("d1r3v15")!=null){
                        mbgl3 = map.get("d1r3v15").toString();
                        editTextR3.setText(mbgl3);
                    }
                    if(map.get("d1r4v15")!=null){
                        mbgl4 = map.get("d1r4v15").toString();
                        editTextR4.setText(mbgl4);
                    }
                    if(map.get("d1r5v15")!=null){
                        mbgl5 = map.get("d1r5v15").toString();
                        editTextR5.setText(mbgl5);
                    }
                    if(map.get("d1r6v15")!=null){
                        mbgl6 = map.get("d1r6v15").toString();
                        editTextR6.setText(mbgl6);
                    }
                    if(map.get("d1r7v15")!=null){
                        mbgl7 = map.get("d1r7v15").toString();
                        editTextR7.setText(mbgl7);
                    }
                    if(map.get("d1r8v52")!=null){
                        mbgl8 = map.get("d1r8v15").toString();
                        editTextR8.setText(mbgl8);
                    }
                    if(map.get("d1r9v15")!=null){
                        mbgl9 = map.get("d1r9v15").toString();
                        editTextR9.setText(mbgl9);
                    }
                    if(map.get("d1r10v15")!=null){
                        mbgl10 = map.get("d1r10v15").toString();
                        editTextR10.setText(mbgl10);
                    }
                    if(map.get("d1r11v15")!=null){
                        mbgl11 = map.get("d1r11v15").toString();
                        editTextR11.setText(mbgl11);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveData() {
        progressDialog.setTitle("Updating Record");
        progressDialog.setMessage("Updating record... Please wait awhile...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);

        mbgl1 = editTextR1.getText().toString();
        mbgl2 = editTextR2.getText().toString();
        mbgl3 = editTextR3.getText().toString();
        mbgl4 = editTextR4.getText().toString();
        mbgl5 = editTextR5.getText().toString();
        mbgl6 = editTextR6.getText().toString();
        mbgl7 = editTextR7.getText().toString();
        mbgl8 = editTextR8.getText().toString();
        mbgl9 = editTextR9.getText().toString();
        mbgl10 = editTextR10.getText().toString();
        mbgl11 = editTextR11.getText().toString();

        if (TextUtils.isEmpty(mbgl1)) {
            bgl1 = 0;
            mbgl1 = "0";
        }
        if (mbgl1 != null) {
            bgl1 = Float.parseFloat(mbgl1);
        }
        if (TextUtils.isEmpty(mbgl2)) {
            bgl2 = 0;
            mbgl2 = "0";
        }
        if (mbgl2 != null) {
            bgl2 = Float.parseFloat(mbgl2);
        }
        if (TextUtils.isEmpty(mbgl3)) {
            bgl3 = 0;
            mbgl3 = "0";
        }
        if (mbgl3 != null) {
            bgl3 = Float.parseFloat(mbgl3);
        }
        if (TextUtils.isEmpty(mbgl4)) {
            bgl4 = 0;
            mbgl4 = "0";
        }
        if (mbgl4 != null) {
            bgl4 = Float.parseFloat(mbgl4);
        }
        if (TextUtils.isEmpty(mbgl5)) {
            bgl5 = 0;
            mbgl5 = "0";
        }
        if (mbgl5 != null) {
            bgl5 = Float.parseFloat(mbgl5);
        }
        if (TextUtils.isEmpty(mbgl6)) {
            bgl6 = 0;
            mbgl6 = "0";
        }
        if (mbgl6 != null) {
            bgl6 = Float.parseFloat(mbgl6);
        }
        if (TextUtils.isEmpty(mbgl7)) {
            bgl7 = 0;
            mbgl7 = "0";
        }
        if (mbgl7 != null) {
            bgl7 = Float.parseFloat(mbgl7);
        }
        if (TextUtils.isEmpty(mbgl8)) {
            bgl8 = 0;
            mbgl8 = "0";
        }
        if (mbgl8 != null) {
            bgl8 = Float.parseFloat(mbgl8);
        }
        if (TextUtils.isEmpty(mbgl9)) {
            bgl9 = 0;
            mbgl9 = "0";
        }
        if (mbgl9 != null) {
            bgl9 = Float.parseFloat(mbgl9);
        }
        if (TextUtils.isEmpty(mbgl10)) {
            mbgl10 = "0";
            bgl10 = 0;
        }
        if (mbgl10 != null) {
            bgl10 = Float.parseFloat(mbgl10);
        }
        if (TextUtils.isEmpty(mbgl11)) {
            mbgl11 = "0";
            bgl11 = 0;
        }
        if (mbgl11 != null) {
            bgl11 = Float.parseFloat(mbgl11);
        }


        HashMap babyMap = new HashMap();
        babyMap.put("d1r1v15", mbgl1);
        babyMap.put("d1r2v15", mbgl2);
        babyMap.put("d1r3v15", mbgl3);
        babyMap.put("d1r4v15", mbgl4);
        babyMap.put("d1r5v15", mbgl5);
        babyMap.put("d1r6v15", mbgl6);
        babyMap.put("d1r7v15", mbgl7);
        babyMap.put("d1r8v15", mbgl8);
        babyMap.put("d1r9v15", mbgl9);
        babyMap.put("d1r10v15", mbgl10);
        babyMap.put("d1r11v15", mbgl11);


        mBaby.child(babyName).child("ivfluids15").updateChildren(babyMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                    if (((bgl1<2.5)&&(bgl1!=0)) || ((bgl2 <2.5)&&(bgl2!=0)) || ((bgl3 <2.5)&&(bgl3!=0)) || ((bgl4 <2.5)&&(bgl4!=0)) || ((bgl5 <2.5)&&(bgl5!=0)) || ((bgl6 <2.5)&&(bgl6!=0)) || ((bgl7 <2.5)&&(bgl7!=0)) || ((bgl8 <2.5)&&(bgl8!=0)) || ((bgl9 <2.5)&&(bgl9!=0)) || ((bgl10 <2.5)&&(bgl10!=0)) || ((bgl11 <2.5)&&(bgl11!=0))) {
                        showTPN();
                    }
                    else {
                        Toast.makeText(IVFluids15Activity.this, "Reocrd Saved.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                }
                else {
                    String msg = task.getException().getMessage();
                    Toast.makeText(IVFluids15Activity.this, "Error Occured: " + msg , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public void showTPN() {
        Toast.makeText(IVFluids15Activity.this, "Baby is in Severe Hypoglycemia", Toast.LENGTH_SHORT).show();
        AlertDialog.Builder builder_fluid = new AlertDialog.Builder(IVFluids15Activity.this);
        TextView textViewTitle = new TextView(IVFluids15Activity.this);
        textViewTitle.setPadding(20,10,20,10);
        textViewTitle.setTypeface(null, Typeface.BOLD);
        textViewTitle.setTextSize(20);
        textViewTitle.setTextColor(Color.BLACK);
        textViewTitle.setText("Please proceed the TPN treatment now!!!");
        builder_fluid.setCustomTitle(textViewTitle);
        builder_fluid.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                babyMap.put("condition", "Severe Hypoglycemia");
                mBaby.child(babyName).updateChildren(babyMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful())
                        {
                            Toast.makeText(IVFluids15Activity.this, "Record saved successful", Toast.LENGTH_SHORT).show();
                        }
                        else {
                            String msg = task.getException().getMessage();
                            Toast.makeText(IVFluids15Activity.this, "Error Occured: " + msg , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        AlertDialog dialogFluid = builder_fluid.create();
        dialogFluid.show();
    }

}
