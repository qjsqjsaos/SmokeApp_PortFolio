package org.techtown.study01.FirstToMain.homeMain.ViewpagerFM;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.Calculate_Date;
import org.techtown.study01.FirstToMain.homeMain.CustomDialog;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.start.Quest1;

import java.sql.Time;
import java.text.ParseException;
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
    private long finallyDate;
    private long finallyTime;

    //뷰모델(라이브데이타) Frag2로 실시간 전달하기
    private SharedViewModel sharedViewModel;

    //Quest1에서 가져온 담배 핀 횟수와 비용 EditText
    private long cigaCount = 1;
    private long cigaCost = 1; //이건 1초에 나타나는 비용이 소수점까지 가므로, long으로 표기한다.

    //하루를 기준으로 피는 담배양을 하루 24시간으로 나눈 시간. ex) 하루에 10개비를 피면 2시간 24분 마다 핀것이다. 여기서 2시간 24분의 값을 초로 나타낸 것이다.
    private long last_cigaCount;
    private long last_cigaCost;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false ); //인플레이션하기
        textView = view.findViewById(R.id.textView847); //타이머 나타내기 위한 텍스트뷰 참조


//        //질문 액티비티를 참조해서 담배 핀 횟수와 비용 정보를 가져온다.
//        Quest1 quest1 = new Quest1();
//        cigaCount = Integer.parseInt(quest1.cigaCount.getText().toString());
//        cigaCost = Integer.parseInt(quest1.cigaPay.getText().toString());

        //하루 담배량 계산
        last_cigaCount = 86400 / cigaCount; //86400은 하루를 초로 나타낸 값이고, 그 것을 하루 담배량으로 나눈 값을 아래 핸들러로 보내서 계산한다.
        Log.d("라스트시가카운트", String.valueOf(last_cigaCount));

        //하루 담배값 계산
        last_cigaCost = (cigaCost / 86400); //ex) 하루를 담배값 4500원으로 나눌때, 담배가 4500원 기준이면, 1초에 0.052원이 발생하게 만든다.
        Log.d("라스트시가코스트", String.valueOf(last_cigaCost));


        //다이얼로그 금연하기와 돌아가기 버튼을 눌렀을때, Frag1에서에 액션을 정할 수 있다.
       HomeMain homeMain = new HomeMain();
       homeMain.noSmoke_Btn.setOnClickListener(new View.OnClickListener() { //홈메인에 있는 버튼을 가져와서 클릭한다.
           @Override
           public void onClick(View v) {
               CustomDialog dialog = new CustomDialog(getContext());
               dialog.setDialogListener(new CustomDialog.CustomDialogListener() {

                   @Override
                   public void onPositiveClicked(String date, String time) throws ParseException { //지정된 날짜, 지정된 시간
                       Calculate_Date calculate_date = new Calculate_Date();
                       Log.d("1값", date); //데이트피커로 입력한 날짜
                       Log.d("1값", time); // 타임 피커로 입력한 날짜



                       String dateNow = calculate_date.WhatTimeIsItDate(); //현재 날짜
                       String timeNow = calculate_date.WhatTimeIsItTime(); //현재 시간
                       Log.d("2값", dateNow); //현재 날짜
                       Log.d("2값", timeNow); // 현재 시간

                       finallyDate = calculate_date.calDateBetweenAandB(date, dateNow); //날 차이 구하기 (지정날짜, 현재날짜)
                       finallyTime = calculate_date.calTimeBetweenAandB(time, timeNow); //시간 차이 구하기(지정시간, 현재시간) //초로 리턴해 온다.

                       Log.d("3값", String.valueOf(finallyDate));
                       Log.d("3값", String.valueOf(finallyTime));

                       // TODO: 2021-02-24 뷰페이저 손 보고 프로그레스바로 넘어간다.
                       // TODO: 2021-02-25 아래 쓰레드 리턴 봐보기 
                       timeThread = new Thread(new timeThread());
                       timeThread.start(); //쓰레드실행

                   }

               });
           }
       });

        return view;
    }



    Handler handler = new Handler(Looper.myLooper()) { //실시간 날짜를 출력해주는 핸들러
        @Override
        public void handleMessage(Message msg) {

            //(msg.arg1 / 100) 이 1초이다. 1초는 1000단위이므로,
            //int min = (msg.arg1 / 100) / 60 같은 경우는 1/60이니까 분이다. (시간도 마찬가지)
            int sec = (msg.arg1 / 100) % 60; //초
            int min = (msg.arg1 / 100) / 60 % 60; //분
            int hour = (msg.arg1 / 100) / 3600 % 24; //시
            int day = (msg.arg2 / 100) / 86400; //하루
            long ciga_Time = (msg.arg2 / 100) / last_cigaCount; //담배를 피지 않은 횟수
            long ciga_Money = (msg.arg2 / 100) * last_cigaCost; //지금껏 아낀 비용


            //스트링 열로 포맷한다.
            String result = String.format("%02d:%02d:%02d", hour, min, sec);

            Log.d("리절트", result);
            Log.d("데이", String.valueOf(day));
            Log.d("타임", String.valueOf(ciga_Time));
            Log.d("머니", String.valueOf(ciga_Money));



            /** result는 실시간 시간초이다./ oneday는 실시간 날짜이다.*/
            textView.setText("오늘을 기준으로\n\n" + result + "시간 째 금연 중"); //타이머 실시간 표시

            sharedViewModel.setLiveData(day); //ViewModel을 통해서 Frag2로 보내기 위해 Livedata에 oneDay를 보낸다.

            sharedViewModel.setLiveDataCount(ciga_Time); //ViewModel을 통해서 Frag5로 보내기 위해 Livedata에 ciga_Time 보낸다.

            sharedViewModel.setLiveDataCost(ciga_Money); //ViewModel을 통해서 Frag3로 보내기 위해 Livedata에 ciga_Money 보낸다.
        }
    };




    public class timeThread implements Runnable {
         //타이머 쓰레드
        @Override
        public void run() {
            int i = (int) finallyTime; //여기에 몇 초인지 넣어야 그 때부터 타이머가 시작된다.
            int day = (int) finallyDate; //여기에는 날짜를 넣는데, 마찬가지로 초 형식으로 넣는다.
            Log.d("뭐야", String.valueOf(day));
            while (true) {
                while (isRunning) { //일시정지를 누르면 멈춤
                        Message msg = new Message();
                        msg.arg1 = i++;
                        msg.arg2 = day++;
                        handler.sendMessage(msg); //인자 넣기(
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
                        /** 이 부분 참고 할 것 return할때는 취소 할때나 다름 없으므로, 자세히 볼것**/ 
                    }
                }
            }
        }
    }

    //onCreateView에서 리턴해준 View(rootView)를 가지고 있다.
    //저장된 뷰가 반환된 직후에 호출됩니다.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { //뷰가 제공되는 경우 반환된 뷰를 가져옵니다.
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //지정된 Factory를 통해 ViewModel을 만들고 지정된 ViewModelStoreOwner의 저장소에 유지하는 ViewModelProvider를 만듭니다.

    }

}


