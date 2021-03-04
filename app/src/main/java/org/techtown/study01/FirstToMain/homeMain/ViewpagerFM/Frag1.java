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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.Calculate_Date;
import org.techtown.study01.FirstToMain.homeMain.GiveUpNoSmoking_Dialog;
import org.techtown.study01.FirstToMain.homeMain.StartNoSmoking_Dialog;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;

import java.text.ParseException;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Frag1 extends Fragment {

    private TextView textView; //타이머 나타내기 위한 텍스트뷰
    private static final String TAG = "FragValue"; //로그 찍을때,

    //쓰레드 부분
    public static Thread timeThread = null;
    private final Boolean isRunning = true;

    //금연한지 얼마나 됬는지 날짜 값
    public static long finallyDateTime;

    //뷰모델(라이브데이타) Frag2로 실시간 전달하기
    private SharedViewModel sharedViewModel;

    //Quest1에서 가져온 담배 핀 횟수와 비용 EditText
    /** 이 카운트와 코스트는 다음에 값 전달하기*/
    public static long cigaCount;
    public static double cigaCost; //이건 1초에 나타나는 비용이 소수점까지 가므로, long으로 표기한다.

    //하루를 기준으로 피는 담배양을 하루 24시간으로 나눈 시간. ex) 하루에 10개비를 피면 2시간 24분 마다 핀것이다. 여기서 2시간 24분의 값을 초로 나타낸 것이다.
    public static long last_cigaCount;
    public static double last_cigaCost;
    private String Eid;


    //중요 지정했던 시간이다. 디비에 넣었다가 뺄 때, 몇초 지났는지 구별해주는 시간이다.
    public static String dateTime = null;

    private HomeMain homeMain;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false ); //인플레이션하기
        textView = view.findViewById(R.id.textView847); //타이머 나타내기 위한 텍스트뷰 참조

        startNoSmokingButton(); //금연하기 버튼
        homeMain.noSmoke_Btn.setVisibility(VISIBLE);
        homeMain.stop_Btn.setVisibility(GONE);

        return view;
    }


    private void startNoSmokingButton() {
        homeMain = new HomeMain();

        /** 금연하기 버튼을 클릭하고나서 금연시간 정하기*/
        //다이얼로그 금연하기와 돌아가기 버튼을 눌렀을때, Frag1에서에 액션을 정할 수 있다.
        homeMain.noSmoke_Btn.setOnClickListener(new View.OnClickListener() { //홈메인에 있는 버튼을 가져와서 클릭한다.
            @Override
            public void onClick(View v) {
                StartNoSmoking_Dialog dialog = new StartNoSmoking_Dialog(getContext());
                dialog.setDialogListener(new StartNoSmoking_Dialog.CustomDialogListener() {

                    @Override
                    public void onPositiveClicked(String date, String time) throws ParseException { //지정된 날짜, 지정된 시간
                        Calculate_Date calculate_date = new Calculate_Date();

                        /** 임시로 일단 값주기*/
                        cigaCount = 5;
                        cigaCost = 5000;

                        dateTime = date +" "+ time; // 지정된 날짜와 데이트 시간 합치기
                        Log.d("3값", dateTime);

                        finallyDateTime = calculate_date.calTimeDateBetweenAandB(dateTime); //날 차이 구하기 (지정날짜와 시간만 넣기)

                        Log.d("3값", String.valueOf(finallyDateTime));

                        //하루 담배량 계산
                        last_cigaCount = 86400 / cigaCount; //86400은 하루를 초로 나타낸 값이고, 그 것을 하루 담배량으로 나눈 값을 아래 핸들러로 보내서 계산한다.
                        Log.d("라스트시가카운트", String.valueOf(last_cigaCount));
                        Log.d("시가카운트", String.valueOf(cigaCount));

                        //하루 담배값 계산
                        last_cigaCost = cigaCost / 86400; //ex) 하루를 담배값 4500원으로 나눌때, 담배가 4500원 기준이면, 1초에 0.052원이 발생하게 만든다.
                        Log.d("라스트시가코스트", String.valueOf(last_cigaCost));
                        Log.d("시가코스트", String.valueOf(cigaCost));

                        homeMain.noSmoke_Btn.setVisibility(GONE); //금연버튼을 비활성화
                        homeMain.stop_Btn.setVisibility(VISIBLE); //취소버튼을 활성화

                        sharedViewModel.setstartDate(dateTime); //금연 시작날짜 SharedViewModel에 값 입력하기(HomeMain에 보내기 위함.)

                        saveValueToDB(); //디비에 저장

                        timeThread = new Thread(new timeThread());
                        timeThread.start(); //쓰레드실행

                    }

                });
            }
        });
    }


    Handler handler = new Handler(Looper.myLooper()) { //실시간 날짜를 출력해주는 핸들러
        @Override
        public void handleMessage(Message msg) {

            //(msg.arg1 / 100) 이 1초이다. 1초는 1000단위이므로,
            //int min = (msg.arg1 / 100) / 60 같은 경우는 1/60이니까 분이다. (시간도 마찬가지)
            //쓰레드에서 번들정보 가져오기
            Bundle bundle = msg.getData();
            long dateTime = bundle.getLong("dateTime");
            Log.d("데이트타임", String.valueOf(dateTime));

            //타이머가 86400000 이 있으면 백의 자리에서 증감이 일어남 그래서,
            // dataTime에 0을 붙여서 천의 자리부터 숫자가 증가하게 만들어 올바른 타이머 동작을 구현했다.
            long datatime_last = Long.parseLong(dateTime+"0");
            Log.d("마지막데트", String.valueOf(datatime_last));
            /////////////////////////////////////
            long sec = (datatime_last / 1000) % 60; //초
            long min = (datatime_last / 1000) / 60 % 60; //분
            long hour = (datatime_last / 1000) / 3600 % 24; //시
            long day = datatime_last / (24*60*60*1000);//하루
            long ciga_Time = (datatime_last/1000) / last_cigaCount; //담배를 피지 않은 횟수
            double ciga_Money = (datatime_last/1000) * last_cigaCost; //지금껏 아낀 비용



            //스트링 열로 포맷한다.
            String result = String.format("%02d:%02d:%02d", hour, min, sec);

            Log.d("진짜", String.valueOf(msg.arg2));
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
            long dateTime = finallyDateTime; //여기에는 날짜와 시간을 넣는데, 마찬가지로 초 형식으로 넣는다.
            Log.d("나", String.valueOf(dateTime));
            Log.d("나", String.valueOf(finallyDateTime));
            while (true) {
                while (isRunning) { //반복문으로 반복하기
                    //메세지에 번들정보 담아서 보내기
                        dateTime++;
                        Message msg = handler.obtainMessage();
                        Bundle bundle = new Bundle();
                        bundle.putLong("dateTime", dateTime);
                        msg.setData(bundle);
                        handler.sendMessage(msg);
                    try {
                        Thread.sleep(10); //혹시나 멈췄을 경우를 대비해 0.01초마다 쓰레드실행
                    } catch (InterruptedException e) { //인터럽트(취소 받을 경우) 한마디로 Bottomnavi에 있는 다이얼로그에서 금연 취소버튼을 눌렀을때이다.
                        e.printStackTrace();
                        getActivity().runOnUiThread(new Runnable(){
                            @Override
                            public void run() { //취소 되고 나서, 실행 여기다가 적기
                                textView.setText(""); //한번 빈칸으로 초기화시켜주기
                                textView.setText("오늘을 기준으로\n\n00:00:00 시간 째 금연 중");
                                Frag2.textView2.setText(""); //한번 빈칸으로 초기화시켜주기
                                Frag2.textView2.setText("벌써 000일이 되었어요!");
                                Frag3.textView3.setText(""); //한번 빈칸으로 초기화시켜주기
                                Frag3.textView3.setText("0원");
                                Frag5.textView5.setText(""); //한번 빈칸으로 초기화시켜주기
                                Frag5.textView5.setText("0개비 가량 됩니다!");
                                HomeMain.dateView.setText("금연날짜");
                                HomeMain.d_dayView.setText("D+");
                            }
                        });
                        return; // 인터럽트 받을 경우 return (취소)
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

    @Override //프래그먼트가 액티비티와 연결될 때 호출됨/ 이 때 디비에서 아이디에 맞게 자료를 가져온다.
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        /** 로그인 하고나서 아이디를 통해 내 정보 불러오기*/
        homeMain = new HomeMain();
        Eid = homeMain.id;
        Log.d("이아이디", String.valueOf(Eid));

        if (Eid != null) {

            Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) { //우선 디비 접속이 성공하면, 정보들은 가져온다.

                            dateTime = jsonObject.getString("datetime");
                            Log.d("디비정보", dateTime);
                            cigaCount = jsonObject.getLong("cigacount"); // 데이터베이스에서 받아온 금연한 시간
                            Log.d("디비정보", String.valueOf(cigaCount));
                            cigaCost = jsonObject.getLong("cigapay"); // 데이터베이스에서 받아온 금연한 시간
                            Log.d("디비정보", String.valueOf(cigaCost));



                            if(dateTime.equals("0")) { //여기서 datetime이 0이면(아직 금연을 시작한게 아니거나, 이미 금연을 포기해서 값이 0인 경우)

                                startThreadShow(); //프래그먼트 켜질때 쓰레드

                            }else{//아니면 원래 내 아이디 값 가져와서 실행.
                                startThreadShow(); //프래그먼트 켜질때 쓰레드
                            }

                        } else {//실패
                            return;
                        }


                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "인터넷 연결 후 다시 접속해주세요.1", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "인터넷 연결 후 다시 접속해주세요.2", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            };

            Frag1_Request frag1_request = new Frag1_Request(Eid, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(frag1_request);
        }else{
            Toast.makeText(getContext(), "인터넷 연결 후 다시 접속해주세요.3", Toast.LENGTH_SHORT).show();
        }
    }

    private void startThreadShow() throws ParseException {
        Calculate_Date calculate_date = new Calculate_Date();
        finallyDateTime = calculate_date.calTimeDateBetweenAandB(dateTime); //날 차이 구하기 (지정날짜와 시간만 넣기)
        /** 임시로 일단 값주기*/
        cigaCount = 5;
        cigaCost = 5000;

        //하루 담배량 계산
        last_cigaCount = 86400 / cigaCount; //86400은 하루를 초로 나타낸 값이고, 그 것을 하루 담배량으로 나눈 값을 아래 핸들러로 보내서 계산한다.
        Log.d("라스트시가카운트", String.valueOf(last_cigaCount));

        //하루 담배값 계산
        last_cigaCost = cigaCost / 86400; //ex) 하루를 담배값 4500원으로 나눌때, 담배가 4500원 기준이면, 1초에 0.052원이 발생하게 만든다.
        Log.d("라스트시가코스트", String.valueOf(last_cigaCost));

        //버튼 가리고 나타내기(금연버튼)
        homeMain.noSmoke_Btn.setVisibility(GONE);
        homeMain.stop_Btn.setVisibility(VISIBLE);

        timeThread = new Thread(new timeThread());
        timeThread.start(); //쓰레드실행
    }

    //디비에 값이 저장되는 메서드
    public void saveValueToDB() {
        Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {

                        //디비 저장하기성공

                    } else {//실패
                        Toast.makeText(getContext(), "오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "디비오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        //Frag1에서 가져온 public static 준 변수들을 액티비티가 꺼질때마다 디비에 저장하게 만든다.
        Frag_ondestroy frag_ondestroy = new Frag_ondestroy(Frag1.dateTime, Frag1.cigaCount, Frag1.cigaCost, HomeMain.id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(frag_ondestroy);

        Log.d("뭐야", Frag1.dateTime + "/" + Frag1.cigaCount + "/" + Frag1.cigaCost + "/" + HomeMain.id);
    }


}


