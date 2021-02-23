package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.Calculate_Date;
import org.techtown.study01.FirstToMain.homeMain.CustomDialog;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;

public class Frag2 extends Fragment {

    private TextView timeTextView;
    private final Boolean isRunning = true;
    private Thread timeThread = null;

    //금연한지 얼마나 됬는지 시간 값
    long finallyTime;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_2, container, false );
        timeTextView = view.findViewById(R.id.textView8287);

        HomeMain homeMain = new HomeMain();
        homeMain.noSmoke_Btn.setOnClickListener(new View.OnClickListener() { //홈메인에 있는 버튼을 가져와서 클릭한다.
            @Override
            public void onClick(View v) {
                CustomDialog dialog = new CustomDialog(getContext());
                dialog.setDialogListener2(new CustomDialog.CustomDialogListener2() {

                    @Override
                    public void onPositiveClickTime(String time) {

                        Calculate_Date calculate_date = new Calculate_Date();
                        String timeNow = calculate_date.WhatTimeIsItTime(); //현재시간
                        finallyTime = calculate_date.calDateBetweenAandB(time, timeNow); //시간 차이 구하기 (지정시간, 현재시간)


                        timeThread = new Thread(new timeThread());
                        timeThread.start();
                    }


                });

                dialog.show();
            }
        });


        return view;

    }

    Handler handler = new Handler(Looper.myLooper()) { //실시간 날짜를 출력해주는 핸들러
        @Override
        public void handleMessage(Message msg) {

            int day = (msg.arg1) / (24*60*60*1000); //하루

            //1000이 1초 1000*60 은 1분 1000*60*10은 10분 1000*60*60은 한시간

            String result = String.format("%02d", day);


            /** result 실시간 시간초이다.*/
            timeTextView.setText("벌써 " + result + "일이 되었어요!"); //타이머 실시간 표시

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
                                timeTextView.setText("");
                                timeTextView.setText("0");
                            }
                        });
                        return; // 인터럽트 받을 경우 return (취소
                    }
                }
            }
        }
    }


}
