package org.techtown.study01.FirstToMain.homeMain;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.login_fitstPage.Login;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

//홈 화면
public class HomeMain extends Fragment {

    //뷰그룹 부분
    private ViewGroup viewGroup;
    private ImageView userView;
    private TextView nameView, dateView;
    private LinearLayout card;
    private Button noSmoke_Btn;

    private static final String TAG = "MyTag"; //로그 찍을때,

    //다이어로그 부분
    private Calculate_Date calculate_date;
    private TextView date, time;
    private Button start_stop_smoking, back;
    private Dialog dialog; //dialog 객체
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private TimePickerDialog.OnTimeSetListener callbackMethod2;

    //쓰레드 부분
    private Thread timeThread = null;
    private Boolean isRunning = true;
    private TextView TimeTextView; //금연 시간 시 분 초 나타내는 텍스트 뷰






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.home_main, container, false);

        userView = viewGroup.findViewById(R.id.userView); //프로필사진
        nameView = viewGroup.findViewById(R.id.nickName); //닉네임(프로필)
        dateView = viewGroup.findViewById(R.id.noSmoke_date); //금연날짜(프로필)
        card = viewGroup.findViewById(R.id.card); //프로필
        noSmoke_Btn = viewGroup.findViewById(R.id.button2); //금연하기버튼

        TimeTextView = viewGroup.findViewById(R.id.timeTextView); //금연 시간 시 분 초 나타내는 텍스트 뷰
        Button button = viewGroup.findViewById(R.id.stop);


        //BottomNavi에서 받은 번들 데이터
        Bundle bundle = this.getArguments();
        Log.d(TAG, "번들가져오고");

        String name = bundle.getString("name");
        String nickName = bundle.getString("nickName");
        String photoUrl = bundle.getString("photoUrl");
        Log.d(TAG,"번들 메세지들 다 가져옴");


        if(nickName != null && photoUrl != null){ //구글 로그인
            nameView.setText(nickName); //닉네임으로 이름바꿔주기
            Log.d(TAG, nickName);
            /** dateView.setText();*/ //여기는 금연 설정할 때 값 받아올 때 넣어야함.!!!!!!!!!!!!!

            Glide.with(this).load(photoUrl).circleCrop().into(userView); //프로필 url(photoUrl)을 이미지 뷰에 세팅
            Log.d(TAG, photoUrl);

        } else if(name != null) { //일반 로그인
            nameView.setText(name); //닉네임으로 이름바꿔주기
            Log.d(TAG, name);
        }


        //날짜 설정창 띄우기(금연하기 버튼)
        noSmoke_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StartStopSmoking_Btn();
            }
        });

        /**예제로 만든 버튼이므로 수정 요망 , xml과 참조한 버튼도 마찬가지*/
        button.setOnClickListener(new View.OnClickListener() { //잠시 예제로 만들어둠 (금연 정지)
            @Override
            public void onClick(View v) {
                timeThread.interrupt();
            }
        });



        card.setOnClickListener(new View.OnClickListener() { //프로필 설정
            @Override
            public void onClick(View v) {
            /** 다이어 로그  나오게하기(그림, 설정 바꾸기)*/
            }
        });

        return viewGroup;
    }

    //////////////////////////////////////////다이어로그 띄우는 메서드(시작)///////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    void StartStopSmoking_Btn() {

        dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.smoke_time_settings);
        //다이아로그 크기 설정하기
        WindowManager.LayoutParams params = dialog.getWindow().getAttributes();
        params.width = 600; //가로길이
        params.height = WindowManager.LayoutParams.WRAP_CONTENT; //세로길이
        dialog.setCanceledOnTouchOutside(false); //바깥쪽 터치시 꺼짐 방지
        dialog.getWindow().setAttributes((WindowManager.LayoutParams) params);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT)); //배경투명하게 해서 선 없애기
        dialog.show(); //띄우기

        /** 이 아래는 프래그먼트 기능을 넣는다.*/

        start_stop_smoking = dialog.findViewById(R.id.button3); //금연하기 버튼
        back = dialog.findViewById(R.id.button4); //금연하지 않기 버튼
        date = dialog.findViewById(R.id.dateSettings); //날짜 텍스트뷰
        time = dialog.findViewById(R.id.timeSettings); // 시간 텍스트뷰

        //현재 날짜 시간 가져오기
        calculate_date = new Calculate_Date();
        String datetoday = calculate_date.WhatTimeIsItDate(); //오늘 날짜 가져오기
        String timenow = calculate_date.WhatTimeIsItTime(); //현재 시간 가져오기

        if(date != null) {
            date.setText(datetoday); //현재 날짜 기본값
        }

        if(time != null) {
            time.setText(timenow); // 현재 시간 기본값
        }

        date.setOnClickListener(new View.OnClickListener() {  //날짜 지정할 수 있는 데이트피커 다이얼로그 나타내기
            @Override
            public void onClick(View v) {
                callbackMethod = new DatePickerDialog.OnDateSetListener()
                {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth)
                    {
                        int month = monthOfYear + 1;
                        date.setText(year + "-" + getMonth(month) + "-" + getDay(dayOfMonth));
                    }
                };

                DatePickerDialog dateDialog = new DatePickerDialog(getContext(), callbackMethod, 2021, 7, 26);
                dateDialog.show();
            }
        });

        time.setOnClickListener(new View.OnClickListener() {  //시간 지정할 수 있는 타임피커 다이얼로그 나타내기
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

        start_stop_smoking.setOnClickListener(new View.OnClickListener() { //금연 시작 버튼
            @Override
            public void onClick(View v) {
                timeThread = new Thread(new timeThread());
                timeThread.start(); //금연 쓰레드 시작
                dialog.dismiss(); //다이어로그 닫기
            }
        });

        back.setOnClickListener(new View.OnClickListener() { //금연하지 않기 버튼
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
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
    //////////////////////////////////////////다이어로그 띄우는 메서드(끝)///////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////



    ///////////////////////////////////////// 쓰레드와 핸들러 (시작)///////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
    Handler handler = new Handler(Looper.myLooper()) {
        @Override
        public void handleMessage(Message msg) {
            int sec = (msg.arg1 / 100) % 60; //초
            int min = (msg.arg1 / 100) / 60; //분
            int hour = (msg.arg1 / 100) / 360; //시
            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            String result = String.format("%02d:%02d:%02d", hour, min, sec);
            if (result.equals("00:01:15")) { //시간 지날때마다 기능 구현하기
                Toast.makeText(getContext(), "1분 15초가 지났습니다.", Toast.LENGTH_SHORT).show(); //예를 든거다.
            }
            TimeTextView.setText(result);
        }
    };


    public class timeThread implements Runnable {
        @Override
        public void run() {
            int i = 0;

            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg);

                    try {
                        Thread.sleep(10); //혹시나 멈췄을 경우를 대비해 0.01초마다 쓰레드실행
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                TimeTextView.setText("");
                                TimeTextView.setText("00:00:00");
                            }
                        });
                        return; // 인터럽트 받을 경우 return (취소
                    }
                }
            }
        }
    }

    ///////////////////////////////////////// 쓰레드와 핸들러 (끝)///////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////
}

