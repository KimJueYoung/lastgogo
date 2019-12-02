package com.example.myapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.TypedValue;
import android.widget.LinearLayout;
import android.widget.TextView;

public class p_student extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.p_student_a);

        Intent intent = getIntent();

        String test[] = intent.getExtras().getStringArray("test"); //test 배열을 넘겨받음
        int size = intent.getIntExtra("size", 1); //size 넘겨받음

        TextView[] tx = new TextView[size];

        LinearLayout layout = (LinearLayout)findViewById(R.id.aa);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        for (int i = 0 ; i <= (size-1); i++) { //버튼 및 텍스트 동적생성
            tx[i] = new TextView(this);
            tx[i].setText(test[i]);
            tx[i].setLayoutParams(layoutParams);
            tx[i].setTextSize(TypedValue.COMPLEX_UNIT_DIP, 35);
            layout.addView(tx[i]);
        }
    }
}
