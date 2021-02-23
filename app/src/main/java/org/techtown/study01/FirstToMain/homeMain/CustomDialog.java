package org.techtown.study01.FirstToMain.homeMain;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.techtown.study01.FirstToMain.R;


public class CustomDialog extends Dialog {


    private static final String TAG = "MyTag"; //로그 찍을때,
    public String timeOk, dateOk; //시간타이머 값 성공할 때 값, //날짜 값 성공할 때 값

    //다이어로그 부분
    public static Calculate_Date calculate_date;
    public static TextView date, time;
    public static Button back;
    public static DatePickerDialog.OnDateSetListener callbackMethod;
    public static TimePickerDialog.OnTimeSetListener callbackMethod2;

    //Frag1으로 이 버튼을 보내기 위해 public static을 달아주었다.
    public static Button start_stop_smoking;
    public static Dialog dialog; //dialog 객체

    public static Context context;
    public static CustomDialogListener customDialogListener;

    public CustomDialog(Context context) {
        super(context);
        this.context = context;
    }


    //인터페이스 설정
    public interface CustomDialogListener{
        void onPositiveClicked(String date, String time);
        void onNegativeClicked();
    }

    //호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.smoke_time_settings);
        //다이아로그 크기 설정하기
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 600; //가로길이
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; //세로길이
        dialog.setCanceledOnTouchOutside(false); //바깥쪽 터치시 꺼짐 방지
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경투명하게 해서 선 없애기

        start_stop_smoking = (Button) dialog.findViewById(R.id.button3); //금연하기 버튼
        back = (Button) dialog.findViewById(R.id.button4); //금연하지 않기 버튼
        date = (TextView) dialog.findViewById(R.id.dateSettings); //날짜 텍스트뷰
        time = (TextView) dialog.findViewById(R.id.timeSettings); // 시간 텍스트뷰

        //현재 날짜 시간 가져오기
        calculate_date = new Calculate_Date();
        String datetoday = calculate_date.WhatTimeIsItDate(); //오늘 날짜 가져오기
        String timenow = calculate_date.WhatTimeIsItTime(); //현재 시간 가져오기

        if (date != null) {
            date.setText(datetoday); //현재 날짜 기본값
        }

        if (time != null) {
            time.setText(timenow); // 현재 시간 기본값
        }

        //버튼 클릭 리스너 등록

        start_stop_smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateOk = date.getText().toString();
                String timeOk = time.getText().toString();

                //인터페이스의 함수를 호출하여 변수에 저장된 값들을 프래그먼트로 전달
                customDialogListener.onPositiveClicked(dateOk, timeOk);
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancel();
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackMethod = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        int month = monthOfYear + 1;
                        date.setText(year + "-" + getMonth(month) + "-" + getDay(dayOfMonth));

                    }
                };
                DatePickerDialog dateDialog = new DatePickerDialog(getContext(), callbackMethod, 2021, 7, 26);
                dateDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackMethod2 = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(getTime(hourOfDay) + " : " + getTime(minute));
                    }
                };

                TimePickerDialog dialog = new TimePickerDialog(getContext(), callbackMethod2, 8, 10, true);
                dialog.show();
            }
        });

        dialog.show(); //띄우기

        /** 이 아래는 프래그먼트 기능을 넣는다.*/

    }

    public static String getMonth(int month) { // 월자 두자리수 만들어주는 메서드
        if(month > 0 && month < 10){
            return "0" + String.valueOf(month);
        }else {
            return String.valueOf(month);
        }
    }

    public static String getDay(int day) { // 일자 두자리수 만들어주는 메서드
        if(day > 0 && day < 10){
            return "0" + String.valueOf(day);
        }else {
            return String.valueOf(day);
        }
    }
    public static String getTime(int time) { // 시간 두자리수 만들어주는 메서드
        if(time > 0 && time < 10){
            return "0" + String.valueOf(time);
        }else {
            return String.valueOf(time);
        }
    }
}
