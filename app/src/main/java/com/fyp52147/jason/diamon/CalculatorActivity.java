package com.fyp52147.jason.diamon;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CalculatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator);

        Button a1CBtn = (Button)findViewById(R.id.button_a1c);
        a1CBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent startIntent = new Intent(getApplicationContext(), CalculatorA1cActivity.class);
                startActivity(startIntent);
            }
        });

        Button bmiBtn = (Button)findViewById(R.id.button_bmi);
        bmiBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                            Intent startIntent = new Intent(getApplicationContext(), CalculatorBMIActivity.class);
                            startActivity(startIntent);
            }
        });
    }
}
