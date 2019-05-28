package com.fyp52147.jason.diamon;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AddLogActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private DatabaseReference mLog;

    private TextView current_time;
    private EditText editTextGlucose, editTextBp;
    private Spinner spinnerMeal, spinnerCondition;
    private Button buttonAdd;

    private String userID, currentTime;

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_log);

        mAuth = FirebaseAuth.getInstance();
        userID = mAuth.getCurrentUser().getUid();

        Calendar calendar = Calendar.getInstance();
        currentTime = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM).format(calendar.getTime());
        current_time = findViewById(R.id.text_current_time);
        current_time.setText(currentTime);

        mLog = FirebaseDatabase.getInstance().getReference().child("Users").child(userID).child("Log").child(currentTime);

        editTextGlucose = (EditText) findViewById(R.id.input_glucose);
        editTextBp = (EditText) findViewById(R.id.input_bp);
        spinnerMeal = (Spinner)findViewById(R.id.spinner_meal);
        spinnerCondition = (Spinner)findViewById(R.id.spinner_condition);
        buttonAdd = (Button)findViewById(R.id.buttonAdd);
        progressDialog = new ProgressDialog(this);

        ArrayAdapter<CharSequence> adapterMeal = ArrayAdapter.createFromResource(this,
                R.array.mealChoice, android.R.layout.simple_spinner_item);
        adapterMeal.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMeal.setAdapter(adapterMeal);

        ArrayAdapter<CharSequence> adapterCondition = ArrayAdapter.createFromResource(this,
                R.array.conditionChoice, android.R.layout.simple_spinner_item);
        adapterCondition.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCondition.setAdapter(adapterCondition);

        buttonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addLog();
            }
        });
    }

    private void addLog() {

        String glucose = editTextGlucose.getText().toString();
        String bloodPressure = editTextBp.getText().toString();
        String meal = spinnerMeal.getSelectedItem().toString();
        String condition = spinnerCondition.getSelectedItem().toString();
        String curTime = current_time.getText().toString();
        float glucoselvl = Float.parseFloat(glucose);

        if (TextUtils.isEmpty(glucose)) {
            Toast.makeText(this, "Please input your blood glucose level...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(bloodPressure)) {
            Toast.makeText(this, "Please input your blood pressure ...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(meal)) {
            Toast.makeText(this, "Please select the time of test blood glucose level...", Toast.LENGTH_SHORT).show();
        }
        if (TextUtils.isEmpty(condition)) {
            Toast.makeText(this, "Please select your body condition...", Toast.LENGTH_SHORT).show();
        } else {
            progressDialog.setTitle("Saving Log");
            progressDialog.setMessage("Saving log... Please wait awhile...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(true);

            if ((glucoselvl < 2.5) || (glucoselvl > 7)) {
                Toast.makeText(this, "Please contact to doctor as soon as possible", Toast.LENGTH_SHORT).show();
                NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "channelID")
                        .setSmallIcon(R.drawable.ic_android_black_24dp)
                        .setContentTitle("Emergency")
                        .setContentText("Please contact to doctor asap")
                        .setPriority(NotificationCompat.PRIORITY_DEFAULT);
                NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
                notificationManager.notify(001, builder.build());
            }

            HashMap logMap = new HashMap();
            logMap.put("time", curTime);
            logMap.put("glucose", glucose);
            logMap.put("bloodPressure", bloodPressure);
            logMap.put("meal", meal);
            logMap.put("condition", condition);
            mLog.updateChildren(logMap).addOnCompleteListener(new OnCompleteListener() {
                @Override
                public void onComplete(@NonNull Task task) {
                    if(task.isSuccessful())
                    {
                        Toast.makeText(AddLogActivity.this, "Log Added Successful.", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();
                        finish();
                        startActivity(new Intent(AddLogActivity.this, LogbookActivity.class));
                    }
                    else {
                        String msg = task.getException().getMessage();
                        Toast.makeText(AddLogActivity.this, "Error Occured: " + msg , Toast.LENGTH_SHORT).show();
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
