package org.techtown.GmanService.FirstToMain.homeMain;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import org.techtown.GmanService.FirstToMain.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class StartNoSmoking_Dialog extends Dialog {

    //다이어로그 부분
    private Calculate_Date calculate_date;
    private TextView date, time;
    private Button back;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private TimePickerDialog.OnTimeSetListener callbackMethod2;

    //Frag1으로 이 버튼을 보내기 위해 public static을 달아주었다.
    private Button start_stop_smoking;
    private Dialog dialog; //dialog 객체

    private Context context;
    private CustomDialogListener customDialogListener;

    public StartNoSmoking_Dialog(Context context) {
        super(context);
        this.context = context;
        dialogStart(); //이 다이얼로그 메서드를 실행될 수 있게 보낸다.
    }


    //인터페이스 설정
    public interface CustomDialogListener{
        void onPositiveClicked(String date, String time) throws ParseException; //확인버튼만 정보 전달할 수 있음.
    }

    //호출할 리스너 초기화
    public void setDialogListener(CustomDialogListener customDialogListener){
        this.customDialogListener = customDialogListener;
    }


    //다이얼로그 안에 버튼에 대한 설정을 할 수 있다.

    public void dialogStart(){
        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.smoke_time_settings); //setContentView는 dialog안에 넣는다.
        //다이아로그 크기 설정하기
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        DisplayMetrics dm = getContext().getResources().getDisplayMetrics();
        params.width = (int) (dm.widthPixels * 0.8); //가로길이
        params.height = (int) (dm.heightPixels * 0.82); //세로길이
        dialog.setCanceledOnTouchOutside(false); //바깥쪽 터치시 꺼짐 방지
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경투명하게 해서 선 없애기
        dialog.show(); //띄우기

        /** 이 아래는 프래그먼트 기능을 넣는다.*/

        start_stop_smoking = dialog.findViewById(R.id.button3); //금연하기 버튼
        back = dialog.findViewById(R.id.button4); //금연하지 않기 버튼
        date =  dialog.findViewById(R.id.dateSettings); //날짜 텍스트뷰
        time = dialog.findViewById(R.id.timeSettings); // 시간 텍스트뷰

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
                SimpleDateFormat FORMATTER =  new SimpleDateFormat("yyyy-MM-dd");
                Date time = new Date();
                String todayDate = FORMATTER.format(time);
                int yearQ = Integer.parseInt(todayDate.substring(0, 4));
                int monthQ = Integer.parseInt(todayDate.substring(5, 7));
                int Goodmonth = monthQ + 1;
                int dayQ = Integer.parseInt(todayDate.substring(8, 10));
                DatePickerDialog dateDialog = new DatePickerDialog(getContext(), callbackMethod, yearQ, Goodmonth, dayQ);
                Calendar minDate = Calendar.getInstance();
                Calendar maxDate = Calendar.getInstance();
                minDate.set(1970,0,1);
                maxDate.set(yearQ,Goodmonth,dayQ);
                dateDialog.getDatePicker().setMinDate(minDate.getTime().getTime());
                dateDialog.getDatePicker().setMaxDate(maxDate.getTimeInMillis());
                dateDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                callbackMethod2 = new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        time.setText(getTime(hourOfDay) + ":" + getTime(minute) + ":00");

                    }
                };
                SimpleDateFormat FORMATTER =  new SimpleDateFormat("HH:mm");
                Date time = new Date();
                String time2 = FORMATTER.format(time);
                int hourOfDay = Integer.parseInt(time2.substring(0, 2));
                int minute = Integer.parseInt(time2.substring(3, 5));
                TimePickerDialog dialog = new TimePickerDialog(getContext(), callbackMethod2, hourOfDay, minute, true);


                dialog.show();


            }
        });
        start_stop_smoking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String dateOk = date.getText().toString();
                String timeOk = time.getText().toString();

                //인터페이스의 함수를 호출하여 변수에 저장된 값들을 프래그먼트로 전달
                try {
                    customDialogListener.onPositiveClicked(dateOk, timeOk);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                dialog.dismiss();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

    }

    public static String getMonth(int month) { // 월자 두자리수 만들어주는 메서드
        if(month >= 0 && month < 10){
            return "0" + String.valueOf(month);
        }else {
            return String.valueOf(month);
        }
    }

    public static String getDay(int day) { // 일자 두자리수 만들어주는 메서드
        if(day >= 0 && day < 10){
            return "0" + String.valueOf(day);
        }else {
            return String.valueOf(day);
        }
    }
    public static String getTime(int time) { // 시간 두자리수 만들어주는 메서드
        if(time >= 0 && time < 10){
            return "0" + String.valueOf(time);
        }else {
            return String.valueOf(time);
        }
    }
}
