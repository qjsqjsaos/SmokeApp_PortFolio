package org.techtown.study01.FirstToMain.homeMain;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.start.Startbutton;

public class Smoke_time_settings extends Dialog {

    private Calculate_Date calculate_date;
    private TextView date, time;
    private Button start_stop_smoking, back;
    private Dialog dialog; //dialog 객체
    private DatePickerDialog.OnDateSetListener callbackMethod;

    public Smoke_time_settings(@NonNull Context context) {
        super(context);
    }

    void StartStopSmoking_Btn() { //다이어로그 띄우는 메서드
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.smoke_time_settings);

        start_stop_smoking = dialog.findViewById(R.id.button3); //금연하기 버튼
        back = dialog.findViewById(R.id.button4); //금연하지 않기 버튼
        date = dialog.findViewById(R.id.dateSettings); //날짜 텍스트뷰
        time = dialog.findViewById(R.id.timeSettings); // 시간 텍스트뷰

        //현재 날짜 시간 가져오기
        calculate_date = new Calculate_Date();
        String allTimeAndDate = calculate_date.WhatTimeIsItAll(); // 시간 날짜 다가져오기
        String datetoday = calculate_date.WhatTimeIsItDate(); //오늘 날짜 가져오기
        String timenow = calculate_date.WhatTimeIsItTime(); //현재 시간 가져오기


        if(date != null) {
            date.setText(datetoday); //현재 날짜 기본값
        }

        if(time != null) {
            time.setText(timenow); // 현재 시간 기본값
        }

        date.setOnClickListener(new View.OnClickListener() { //날짜 선택할 때
            @Override
            public void onClick(View v) {
                ShowDate();
            }
        });

        time.setOnClickListener(new View.OnClickListener() { //시간 선택할 때,
            @Override
            public void onClick(View v) {

            }
        });

        start_stop_smoking.setOnClickListener(new View.OnClickListener() { //금연 시작 버튼
            @Override
            public void onClick(View v) {

            }
        });

        back.setOnClickListener(new View.OnClickListener() { //하지 않기 버튼
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });


        //다이아로그 크기 설정하기
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 600; //가로길이
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; //세로길이
        dialog.setCanceledOnTouchOutside(false); //바깥쪽 터치시 꺼짐 방지
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경투명하게 해서 선 없애기
        dialog.show(); //띄우기
    }

    private void ShowDate(){ //달력 선택값 출력해주는 메서드
        callbackMethod = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                date.setText(year + month + dayOfMonth); //년 월 일 순
            }
        };
    }

    public void OnClickHandler(View view) { //달력 보여주는 핸들러 (중요)
        DatePickerDialog dialog = new DatePickerDialog(getContext(),callbackMethod, 2021, 07, 26); //달력 선택시 자동 설정
        dialog.show();
    }


}