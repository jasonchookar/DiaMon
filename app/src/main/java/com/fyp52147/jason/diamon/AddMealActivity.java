package com.fyp52147.jason.diamon;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddMealActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private FirebaseAuth mAuth;
    private DatabaseReference mMeal;

    private EditText editTextDate, editTextBreakfast, editTextLunch, editTextDinner;
    private Button buttonAdd;
    private String userID, currentDate;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_meal);
        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        Calendar calendar = Calendar.getInstance();
        currentDate = DateFormat.getDateInstance(DateFormat.MEDIUM).format(calendar.getTime());
        editTextDate = findViewById(R.id.input_date);
        editTextDate.setText(currentDate);

        mMeal = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Meal");

        editTextDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "Date Picker");
            }
        });

        editTextBreakfast = findViewById(R.id.input_breakfast);
        editTextLunch = findViewById(R.id.input_lunch);
        editTextDinner = findViewById(R.id.input_dinner);
        buttonAdd = findViewById(R.id.buttonAdd);
        progressDialog = new ProgressDialog(this);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMeal();
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
        editTextDate.setText(selectedDate);
    }

    private void addMeal() {
        String date = editTextDate.getText().toString();
        String dataBreakfast = editTextBreakfast.getText().toString();
        String dataLunch = editTextLunch.getText().toString();
        String dataDinner = editTextDinner.getText().toString();
        DatabaseReference maddMeal = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Meal").child(date);

        progressDialog.setTitle("Adding Meal");
        progressDialog.setMessage("Adding meal information... Please wait awhile...");
        progressDialog.show();
        progressDialog.setCanceledOnTouchOutside(true);

        HashMap mealMap = new HashMap();
        mealMap.put("date", date);
        mealMap.put("breakfast", dataBreakfast);
        mealMap.put("lunch", dataLunch);
        mealMap.put("dinner", dataDinner);
        maddMeal.updateChildren(mealMap).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if(task.isSuccessful())
                {
                    Toast.makeText(AddMealActivity.this, "Meal Added Successful", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                    startActivity(new Intent(AddMealActivity.this, MealActivity.class));
                }
                else {
                    String msg = task.getException().getMessage();
                    Toast.makeText(AddMealActivity.this, "Error Occured: " + msg , Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }
}
