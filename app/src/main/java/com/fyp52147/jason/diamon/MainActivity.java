package com.fyp52147.jason.diamon;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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

public class MainActivity extends AppCompatActivity {

    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListner;
    private DatabaseReference mUsers, mAdmin;
    DatabaseReference mBaby;
    String userID, babyName, ynMom, strYN, strWeight, strBirthWeek;
    float weight, bgl1, bgl2, bgl3;
    EditText editTextGlucose;
    int birthWeek;
    HashMap babyMap;
    Handler handler;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListner);
    }

    private void checkUser() {
        final String current_UID = mAuth.getCurrentUser().getUid();

        mAdmin.addValueEventListener (new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot ds) {
                if (!ds.hasChild(current_UID)) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mUsers = FirebaseDatabase.getInstance().getReference().child("Users");
        mAdmin = FirebaseDatabase.getInstance().getReference().child("Admin");

        Button btnHypoglycemia = findViewById(R.id.hypoglycemia);
        Button btnBaby = findViewById(R.id.babylist);

        Button btnSignout = findViewById(R.id.signout);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mBaby = FirebaseDatabase.getInstance().getReference().child("Admin").child(userID).child("Baby");
        babyMap = new HashMap();

        mAuthListner = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser()==null)
                {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                }
                else
                {
                    checkUser();
                }
            }
        };

        btnHypoglycemia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                NewBaby();
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                ExistedBaby();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(v.getContext());
                builder.setMessage("New or Existed Baby Record?").setPositiveButton("New", dialogClickListener)
                        .setNegativeButton("Existed", dialogClickListener).show();
            }

            public void NewBaby(){
                AlertDialog.Builder builder_info = new AlertDialog.Builder(MainActivity.this);
                builder_info.setTitle("Baby's Details");
                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.builder_hypoinfo, null);
                builder_info.setView(customLayout);
                // add a button
                builder_info.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // send data from the AlertDialog to the Activity
                        EditText editTextName = customLayout.findViewById(R.id.editTextName);
                        EditText editTextWeight = customLayout.findViewById(R.id.editTextWeight);
                        EditText editTextBirthWeek = customLayout.findViewById(R.id.editTextBirthWeek);
                        EditText editTextDiamom = customLayout.findViewById(R.id.editTextMom);
                        babyName = editTextName.getText().toString();
                        strWeight = editTextWeight.getText().toString();
                        strBirthWeek = editTextBirthWeek.getText().toString();
                        weight = Float.parseFloat(editTextWeight.getText().toString());
                        birthWeek = Integer.parseInt(editTextBirthWeek.getText().toString());
                        ynMom = editTextDiamom.getText().toString();
                        switch (ynMom) {
                            case "y":
                                strYN = "Yes";
                                babyMap.put("diabetesMom", strYN);
                                break;
                            case "Y":
                                strYN = "Yes";
                                babyMap.put("diabetesMom", strYN);
                                break;
                            case "n":
                                strYN = "No";
                                babyMap.put("diabetesMom", strYN);
                                break;
                            case "N":
                                strYN = "No";
                                babyMap.put("diabetesMom", strYN);
                                break;
                            default:
                                Toast.makeText(MainActivity.this, "Wrong input", Toast.LENGTH_SHORT).show();
                                break;
                        }
                        babyMap.put("name", babyName);
                        babyMap.put("weight", strWeight);
                        babyMap.put("birthWeek", strBirthWeek);

                        if ((weight<2.5) && (strYN.equals("Yes"))) {
                            Toast.makeText(MainActivity.this, "The Baby is SGA with Diabetes Mother", Toast.LENGTH_SHORT).show();
                            babyMap.put("babyType", "SGA");
                            MonitorGlucose();
                        }
                        else if ((weight<2.5) && (strYN.equals("No"))) {
                            Toast.makeText(MainActivity.this, "The Baby is SGA", Toast.LENGTH_SHORT).show();
                            babyMap.put("babyType", "SGA");
                            MonitorGlucose();
                        }
                        else if ((weight>4) && (strYN.equals("Yes"))) {
                            Toast.makeText(MainActivity.this, "The Baby is LGA with Diabetes Mother", Toast.LENGTH_SHORT).show();
                            babyMap.put("babyType", "LGA");
                            MonitorGlucose();
                        }
                        else if ((weight>4) && (strYN.equals("No"))) {
                            Toast.makeText(MainActivity.this, "The Baby is LGA", Toast.LENGTH_SHORT).show();
                            babyMap.put("babyType", "LGA");
                            MonitorGlucose();
                        }
                        else if (strYN.equals("Yes")) {
                            Toast.makeText(MainActivity.this, "The Baby's Mother is Diabetes Patient", Toast.LENGTH_SHORT).show();
                            babyMap.put("babyType", "Normal");
                            MonitorGlucose();
                        }
                        else if (strYN.equals("No")) {
                            Toast.makeText(MainActivity.this, "The Baby is normal", Toast.LENGTH_SHORT).show();
                            babyMap.put("babyType", "Normal");
                            babyMap.put("condition", "Normal");
                            mBaby.child(babyName).updateChildren(babyMap);
                        }

                    }
                });
                // create and show the alert dialog
                AlertDialog dialogLGA = builder_info.create();
                dialogLGA.show();
            }

            public void ExistedBaby(){
                AlertDialog.Builder builder_baby = new AlertDialog.Builder(MainActivity.this);
                builder_baby.setTitle("Baby Name");
                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.builder_glucose, null);
                builder_baby.setView(customLayout);
                // add a button
                builder_baby.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // send data from the AlertDialog to the Activity
                        EditText editTextBaby = customLayout.findViewById(R.id.editTextGlucose);
                        final String strBaby = editTextBaby.getText().toString();
                        ValueEventListener eventListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Boolean search;
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    String babyName = ds.child("name").getValue(String.class);
                                    if (babyName.equals(strBaby)) {
                                        String existWeight = ds.child("weight").getValue(String.class);
                                        String existBirthWeek = ds.child("birthWeek").getValue(String.class);
                                        String dextrosevalue = ds.child("dextrose").getValue(String.class);
                                        if (dextrosevalue.equals("10%")) {
                                            Intent i = new Intent (MainActivity.this, IVFluids10Activity.class);
                                            i.putExtra("name", strBaby);
                                            i.putExtra("weight", existWeight);
                                            i.putExtra("birthWeek", existBirthWeek);
                                            startActivity(i);
                                        }
                                        else if (dextrosevalue.equals("12.5%")) {
                                            Intent i = new Intent (MainActivity.this, IVFluids12Activity.class);
                                            i.putExtra("name", strBaby);
                                            i.putExtra("weight", existWeight);
                                            i.putExtra("birthWeek", existBirthWeek);
                                            startActivity(i);
                                        }
                                        else if (dextrosevalue.equals("15%")) {
                                            Intent i = new Intent (MainActivity.this, IVFluids15Activity.class);
                                            i.putExtra("name", strBaby);
                                            i.putExtra("weight", existWeight);
                                            i.putExtra("birthWeek", existBirthWeek);
                                            startActivity(i);
                                        }
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        };
                        mBaby.addListenerForSingleValueEvent(eventListener);
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder_baby.create();
                dialog.show();
            }

            private void MonitorGlucose() {
                AlertDialog.Builder builder_glucose = new AlertDialog.Builder(MainActivity.this);
                builder_glucose.setTitle("Blood Glucose Level First Hour for " + babyName);
                // set the custom layout
                final View customLayout = getLayoutInflater().inflate(R.layout.builder_glucose, null);
                builder_glucose.setView(customLayout);
                // add a button
                builder_glucose.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // send data from the AlertDialog to the Activity
                        editTextGlucose = customLayout.findViewById(R.id.editTextGlucose);
                        bgl1 = Float.parseFloat(editTextGlucose.getText().toString());
                        sendDialogDataToActivity(editTextGlucose.getText().toString());
                        babyMap.put("glucoseFirstHourResult", bgl1);
                        if (bgl1 < 2.5) {
                            FluidTreatment();
                        }
                        else {
                            handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    openAlert();
                                }
                            }, 1000);
                        }
                    }
                    private void openAlert() {
                        AlertDialog.Builder builder_glucose = new AlertDialog.Builder(MainActivity.this);
                        builder_glucose.setTitle("Blood Glucose Level Second Hour for " + babyName);
                        // set the custom layout
                        final View customLayout = getLayoutInflater().inflate(R.layout.builder_glucose, null);
                        builder_glucose.setView(customLayout);
                        // add a button
                        builder_glucose.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // send data from the AlertDialog to the Activity
                                editTextGlucose = customLayout.findViewById(R.id.editTextGlucose);
                                bgl2 = Float.parseFloat(editTextGlucose.getText().toString());
                                sendDialogDataToActivity(editTextGlucose.getText().toString());
                                babyMap.put("glucoseSecondHourResult", bgl2);
                                if (bgl2 < 2.5) {
                                    FluidTreatment();
                                }
                                else {
                                    handler.postDelayed(new Runnable() {
                                        @Override
                                        public void run() {
                                            openAlert2();
                                        }
                                    }, 1000);
                                }
                            }

                            private void openAlert2() {
                                AlertDialog.Builder builder_glucose = new AlertDialog.Builder(MainActivity.this);
                                builder_glucose.setTitle("Blood Glucose Level Forth Hour for " + babyName);
                                // set the custom layout
                                final View customLayout = getLayoutInflater().inflate(R.layout.builder_glucose, null);
                                builder_glucose.setView(customLayout);
                                // add a button
                                builder_glucose.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        // send data from the AlertDialog to the Activity
                                        editTextGlucose = customLayout.findViewById(R.id.editTextGlucose);
                                        bgl3 = Float.parseFloat(editTextGlucose.getText().toString());
                                        babyMap.put("glucoseForthHourResult", bgl3);
                                        sendDialogDataToActivity(editTextGlucose.getText().toString());
                                        if (bgl3 < 2.5) {
                                            FluidTreatment();
                                        }
                                        else {
                                            Toast.makeText(MainActivity.this, "The Baby is in normal", Toast.LENGTH_SHORT).show();
                                            babyMap.put("condition", "Normal");
                                            mBaby.child(babyName).updateChildren(babyMap);
                                        }
                                    }
                                });
                                // create and show the alert dialog
                                AlertDialog dialog = builder_glucose.create();
                                dialog.show();
                            }
                        });
                        // create and show the alert dialog
                        AlertDialog dialog = builder_glucose.create();
                        dialog.show();
                    }
                });
                // create and show the alert dialog
                AlertDialog dialog = builder_glucose.create();
                dialog.show();
            }

            // do something with the data coming from the AlertDialog
            private void sendDialogDataToActivity(String data) {
                Toast.makeText(MainActivity.this, data, Toast.LENGTH_SHORT).show();
            }
        });

        btnBaby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent (MainActivity.this, BabyListActivity.class));
            }
        });

        btnSignout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
            }
        });
    }

    public static BigDecimal round(float d, int decimalPlace) {
        BigDecimal bd = new BigDecimal(Float.toString(d));
        bd = bd.setScale(decimalPlace, BigDecimal.ROUND_HALF_UP);
        return bd;
    }

    public void FluidTreatment() {
        Toast.makeText(MainActivity.this, "Baby is in Hypoglycemia", Toast.LENGTH_SHORT).show();

        AlertDialog.Builder builder_fluid = new AlertDialog.Builder(MainActivity.this);
        float fluid_amount;
        int x = 0;
        BigDecimal fluid_needed = null;
        if(birthWeek<37) x=-1;
        if(birthWeek>=37) x=1;
        switch (x) {
            case -1:
                fluid_amount = 120*weight/8;
                fluid_needed = round(fluid_amount, 2);
                break;

            case 1:
                fluid_amount = 80*weight/8;
                fluid_needed = round(fluid_amount, 2);
                break;
        }
        String fluidvolume = fluid_needed.toString();
        babyMap.put("fluidvolume", fluidvolume);
        TextView textViewTitle = new TextView(MainActivity.this);
        textViewTitle.setPadding(20,10,20,10);
        textViewTitle.setTypeface(null, Typeface.BOLD);
        textViewTitle.setTextSize(20);
        textViewTitle.setTextColor(Color.BLACK);
        textViewTitle.setText("Please proceed the IV Fluids treatment with " + fluid_needed + "mls/kg with 10% dextrose and monitor blood glucose level every 3 hours in 24 hours");
        builder_fluid.setCustomTitle(textViewTitle);
        builder_fluid.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                babyMap.put("condition", "Hypoglycemia");
                babyMap.put("dextrose", "10%");
                mBaby.child(babyName).setValue(babyMap).addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful())
                        {
                            Intent i = new Intent (MainActivity.this, IVFluids10Activity.class);
                            i.putExtra("name", babyName);
                            i.putExtra("weight", strWeight);
                            i.putExtra("birthWeek", strBirthWeek);
                            startActivity(i);
                        }
                        else {
                            String msg = task.getException().getMessage();
                            Toast.makeText(MainActivity.this, "Error Occured: " + msg , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        AlertDialog dialogFluid = builder_fluid.create();
        dialogFluid.show();
    }
}