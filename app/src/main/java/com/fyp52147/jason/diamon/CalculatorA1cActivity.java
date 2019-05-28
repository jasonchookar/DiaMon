package com.fyp52147.jason.diamon;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CalculatorA1cActivity extends AppCompatActivity {

    String strA1c, streAG;
    EditText editTexteAG;
    TextView textViewA1C;
    Button buttonCalculate;
    float numeAG;
    double numA1c;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calculator_a1c);

        editTexteAG = findViewById(R.id.editTexteAG);
        textViewA1C = findViewById(R.id.textViewA1c);
        buttonCalculate = findViewById(R.id.buttoncalc);

        buttonCalculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                streAG = editTexteAG.getText().toString();
                numeAG = Float.parseFloat(streAG);
                numA1c = ((numeAG * 18)+46.7)/28.7;
                strA1c = String.format("%.2f", numA1c);
                textViewA1C.setText(strA1c);
            }
        });
    }
}
