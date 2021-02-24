package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.Calculate_Date;
import org.techtown.study01.FirstToMain.homeMain.CustomDialog;
import org.techtown.study01.FirstToMain.homeMain.FragPagerAdapter;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Frag1 extends Fragment {

    private TextView textView; //타이머 나타내기 위한 텍스트뷰
    private static final String TAG = "FragValue"; //로그 찍을때,

    //쓰레드 부분
    private Thread timeThread = null;
    private final Boolean isRunning = true;

    //금연한지 얼마나 됬는지 날짜 값
    long finallyDate;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false ); //인플레이션하기
        textView = view.findViewById(R.id.textView847); //타이머 나타내기 위한 텍스트뷰 참조

        //다이얼로그 금연하기와 돌아가기 버튼을 눌렀을때, Frag1에서에 액션을 정할 수 있다.
       HomeMain homeMain = new HomeMain();
       homeMain.noSmoke_Btn.setOnClickListener(new View.OnClickListener() { //홈메인에 있는 버튼을 가져와서 클릭한다.
           @Override
           public void onClick(View v) {
               CustomDialog dialog = new CustomDialog(getContext());
               dialog.setDialogListener(new CustomDialog.CustomDialogListener() {

                   @Override
                   public void onPositiveClicked(String date, String time) {
                       Calculate_Date calculate_date = new Calculate_Date();
                       String dateNow = calculate_date.WhatTimeIsItDate(); //현재 날짜
                       finallyDate = calculate_date.calDateBetweenAandB(date, dateNow); //날 차이 구하기 (지정날짜, 현재날짜)

                       // TODO: 2021-02-23 시간 계산 구하는 법 알아보고 , Frag2로 데이터 전달법 알아보기 

                       timeThread = new Thread(new timeThread());
                       timeThread.start();
                   }
                   
               });
           }
       });

        return view;
    }



    Handler handler = new Handler(Looper.myLooper()) { //실시간 날짜를 출력해주는 핸들러
        @Override
        public void handleMessage(Message msg) {
            int sec = (msg.arg1 / 100) % 60; //초
            int min = (msg.arg1 / 100) / 60; //분
            int hour = (msg.arg1 / 100) / 360; //시
            int day = (msg.arg1 / 100) / 3600; //하루

            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            String result = String.format("%02d:%02d:%02d", hour, min, sec);


            /** result 실시간 시간초이다.*/
            textView.setText("오늘을 기준으로\n\n" + result + "시간 째 금연 중"); //타이머 실시간 표시

        }
    };



    public class timeThread implements Runnable { //타이머 쓰레드
        @Override
        public void run() {
            int i = 0; //타이머 시작 여기다 시분초 넣고

            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                    Message msg = new Message();
                    msg.arg1 = i++;
                    handler.sendMessage(msg); //인자 넣기(186행)

                    try {
                        Thread.sleep(10); //혹시나 멈췄을 경우를 대비해 0.01초마다 쓰레드실행
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run() {
                                textView.setText("");
                                textView.setText("00:00:00");
                            }
                        });
                        return; // 인터럽트 받을 경우 return (취소
                    }
                }
            }
        }
    }

}


