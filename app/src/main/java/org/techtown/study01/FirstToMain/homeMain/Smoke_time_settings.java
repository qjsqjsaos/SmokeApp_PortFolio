package org.techtown.study01.FirstToMain.homeMain;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.study01.FirstToMain.R;

public class Smoke_time_settings extends AppCompatActivity {

    private Calculate_Date calculate_date;
    private EditText date, time;
    private Button start_stop_smoking, back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.smoke_time_settings);

        back = findViewById(R.id.button4);

        //현재 날짜 시간 가져오기
        calculate_date = new Calculate_Date();
        String datetoday = calculate_date.WhatTimeIsItDate(); //오늘 날짜 가져오기
        String timenow = calculate_date.WhatTimeIsItTime(); //현재 시간 가져오기

        date = findViewById(R.id.dateSettings);
        time = findViewById(R.id.timeSettings);

        date.setText(datetoday); //현재 날짜 기본값
        time.setText(timenow); // 현재 시간 기본값

        start_stop_smoking = findViewById(R.id.button3);
        start_stop_smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });




    }
}