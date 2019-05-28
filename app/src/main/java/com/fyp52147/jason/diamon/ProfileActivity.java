package com.fyp52147.jason.diamon;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener/*implements View.OnClickListener*/ {

    private FirebaseAuth mAuth;
    private DatabaseReference mProfile;

    private EditText editTextName, editTextPhone, editTextBirth, editTextWeight, editTextAddress;
    private Spinner spinnerGender;
    private Button buttonSave;

    private String userID, mName, mGender, mPhone, mBirth, mWeight, mAddress;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        setContentView(R.layout.activity_profile);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        mProfile = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Profile");

        editTextName = (EditText)findViewById(R.id.name);
        editTextPhone = (EditText)findViewById(R.id.phoneNumber);
        editTextBirth = (EditText)findViewById(R.id.dateBirth);
        editTextWeight = (EditText)findViewById(R.id.weight);
        editTextAddress = (EditText)findViewById(R.id.addressHome);
        spinnerGender = (Spinner)findViewById(R.id.gender);
        buttonSave = (Button)findViewById(R.id.buttonSave);
        progressDialog = new ProgressDialog(this);

        ArrayAdapter<CharSequence> adapterGender = ArrayAdapter.createFromResource(this,
                R.array.genderChoice, android.R.layout.simple_spinner_item);
        adapterGender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerGender.setAdapter(adapterGender);

        editTextBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        mProfile.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()){
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();
                    if(map.get("name")!=null){
                        mName = map.get("name").toString();
                        editTextName.setText(mName);
                    }
                    if(map.get("gender")!=null){
                        mGender = map.get("gender").toString();
                        ArrayAdapter selectedAdap = (ArrayAdapter) spinnerGender.getAdapter(); //cast to an ArrayAdapter
                        int spinnerPosition = selectedAdap.getPosition(mGender);
                        //set the default according to value
                        spinnerGender.setSelection(spinnerPosition);
                    }
                    if(map.get("phone")!=null){
                        mPhone = map.get("phone").toString();
                        editTextPhone.setText(mPhone);
                    }
                    if(map.get("birth")!=null){
                        mBirth = map.get("birth").toString();
                        editTextBirth.setText(mBirth);
                    }
                    if(map.get("weight")!=null){
                        mWeight = map.get("weight").toString();
                        editTextWeight.setText(mWeight);
                    }
                    if(map.get("address")!=null){
                        mAddress = map.get("address").toString();
                        editTextAddress.setText(mAddress);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveProfile();
            }
        });
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

        String selectedDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        editTextBirth.setText(selectedDate);
    }

    private void saveProfile() {

        NumberFormat df = DecimalFormat.getInstance();
        df.setMinimumFractionDigits(2);
        df.setMaximumFractionDigits(2);

        String name = editTextName.getText().toString();
        String phone = editTextPhone.getText().toString();
        String sex = spinnerGender.getSelectedItem().toString();
        String birth = editTextBirth.getText().toString();
        String weight = editTextWeight.getText().toString();
        String address = editTextAddress.getText().toString();

        if(TextUtils.isEmpty(name)) {
            Toast.makeText(this, "Please input your name...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(sex)) {
            Toast.makeText(this,"Please select your gender...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(phone)) {
            Toast.makeText(this, "Please input your phone number...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(birth)) {
            Toast.makeText(this, "Please input your date of birth...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(weight)) {
            Toast.makeText(this, "Please input your weight...", Toast.LENGTH_SHORT).show();
        }
        if(TextUtils.isEmpty(address)) {
            Toast.makeText(this, "Please input your address...", Toast.LENGTH_SHORT).show();
        }
        else {
            progressDialog.setTitle("Updating Profile Information");
            progressDialog.setMessage("Updating profile information... Please wait awhile...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);

            HashMap userMap = new HashMap();
            userMap.put("name", name);
            userMap.put("gender", sex);
            userMap.put("phone", phone);
            userMap.put("birth", birth);
            userMap.put("weight", weight);
            userMap.put("address", address);
            mProfile.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(ProfileActivity.this, "Profile Information Saved.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                    }
                    else {
                        String msg = task.getException().getMessage();
                        Toast.makeText(ProfileActivity.this, "Error Occured: " + msg , Toast.LENGTH_SHORT).show();
                        progressDialog.dismiss();
                    }
                }
            });
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
    }


}
