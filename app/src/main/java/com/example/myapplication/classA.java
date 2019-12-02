package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

public class classA extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.class_a);

        Intent intent = getIntent();

        String test[] = intent.getExtras().getStringArray("test");
        int size = intent.getIntExtra("size", 1);

        TextView[] tx = new TextView[size];

        LinearLayout layout = (LinearLayout)findViewById(R.id.oooo);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0 ; i <= (size-1); i++) {
            tx[i] = new TextView(this);
            tx[i].setText(test[i]);
            tx[i].setLayoutParams(layoutParams);
            tx[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
            layout.addView(tx[i]);
            tx[i].setId(i);
        }
    }
}
