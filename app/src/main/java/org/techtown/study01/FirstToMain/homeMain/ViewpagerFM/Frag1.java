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
import org.techtown.study01.FirstToMain.homeMain.CustomDialog;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;

import java.text.ParseException;

import static android.icu.lang.UCharacter.GraphemeClusterBreak.L;

public class Frag1 extends Fragment {

    private TextView textView; //타이머 나타내기 위한 텍스트뷰
    private static final String TAG = "FragValue"; //로그 찍을때,

    //쓰레드 부분
    private Thread timeThread = null;
    private final Boolean isRunning = true;

    //금연한지 얼마나 됬는지 날짜 값
    public static long finallyDateTime;

    public  static long finallyTime;//임시
    public  static  long finallyDate; //임시

    //뷰모델(라이브데이타) Frag2로 실시간 전달하기
    private SharedViewModel sharedViewModel;

    //Quest1에서 가져온 담배 핀 횟수와 비용 EditText
    public static long cigaCount = 5;
    public static double cigaCost = 5000; //이건 1초에 나타나는 비용이 소수점까지 가므로, long으로 표기한다.

    //하루를 기준으로 피는 담배양을 하루 24시간으로 나눈 시간. ex) 하루에 10개비를 피면 2시간 24분 마다 핀것이다. 여기서 2시간 24분의 값을 초로 나타낸 것이다.
    public static long last_cigaCount;
    public static double last_cigaCost;
    String Eid;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_1, container, false ); //인플레이션하기
        textView = view.findViewById(R.id.textView847); //타이머 나타내기 위한 텍스트뷰 참조

        HomeMain homeMain = new HomeMain();
        Eid = homeMain.id;


//      if(homeMain.id != null) { //로그인하고 아이디가 넘겨오면, "0"으로 표시한다.
//
//          Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.
//
//              @Override
//              public void onResponse(String response) {
//                  try {
//                      JSONObject jsonObject = new JSONObject(response);
//                      boolean success = jsonObject.getBoolean("success");
//
//                      if (success) {
//
//                          finallyTime = Long.parseLong(jsonObject.getString("nstime")); // 데이터베이스에서 받아온 금연한 시간
//                          finallyDate = Long.parseLong(jsonObject.getString("nsdate")); // 데이터베이스에서 받아온 금연한 시간
//                          cigaCount = Long.parseLong(jsonObject.getString("cigacount")); // 데이터베이스에서 받아온 금연한 시간
//                          cigaCost = Long.parseLong(jsonObject.getString("cigapay")); // 데이터베이스에서 받아온 금연한 시간
//
//                          //하루 담배량 계산
//                          last_cigaCount = 86400 / cigaCount * 1L; //86400은 하루를 초로 나타낸 값이고, 그 것을 하루 담배량으로 나눈 값을 아래 핸들러로 보내서 계산한다.
//                          Log.d("라스트시가카운트", String.valueOf(last_cigaCount));
//
//                          //하루 담배값 계산
//                          last_cigaCost = cigaCost / 86400 * 1L; //ex) 하루를 담배값 4500원으로 나눌때, 담배가 4500원 기준이면, 1초에 0.052원이 발생하게 만든다.
//                          Log.d("라스트시가코스트", String.valueOf(last_cigaCost));
//
//                          timeThread = new Thread(new timeThread());
//                          timeThread.start(); //쓰레드실행
//
//                      } else {//실패
//                          return;
//                      }
//
//
//                  } catch (JSONException e) {
//                      e.printStackTrace();
//                      Toast.makeText(getContext(), "Frag1 오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
//                      return;
//                  } catch (Exception e) {
//                      e.printStackTrace();
//                  }
//              }
//          };
//
//          Frag1_Request frag1_request = new Frag1_Request(Eid, responseListener);
//          RequestQueue queue = Volley.newRequestQueue(getContext());
//          queue.add(frag1_request);
//      }



        //다이얼로그 금연하기와 돌아가기 버튼을 눌렀을때, Frag1에서에 액션을 정할 수 있다.

        homeMain.noSmoke_Btn.setOnClickListener(new View.OnClickListener() { //홈메인에 있는 버튼을 가져와서 클릭한다.
            @Override
            public void onClick(View v) {
                CustomDialog dialog = new CustomDialog(getContext());
                dialog.setDialogListener(new CustomDialog.CustomDialogListener() {

                    @Override
                    public void onPositiveClicked(String date, String time) throws ParseException { //지정된 날짜, 지정된 시간
                        Calculate_Date calculate_date = new Calculate_Date();

                        String dateTime = date +" "+ time; // 데이트랑 시간 합치기
                        Log.d("3값", dateTime);

                        finallyDateTime = calculate_date.calDateBetweenAandB(dateTime); //날 차이 구하기 (지정날짜만 넣기

                        Log.d("3값", String.valueOf(finallyDateTime));

                        // TODO: 2021-02-24 뷰페이저 손 보고 프로그레스바로 넘어간다.
                        // TODO: 2021-02-25 아래 쓰레드 리턴 봐보기

                        //하루 담배량 계산
                        last_cigaCount = 86400 / cigaCount; //86400은 하루를 초로 나타낸 값이고, 그 것을 하루 담배량으로 나눈 값을 아래 핸들러로 보내서 계산한다.
                        Log.d("라스트시가카운트", String.valueOf(last_cigaCount));
                        Log.d("시가카운트", String.valueOf(cigaCount));

                        //하루 담배값 계산
                        last_cigaCost = cigaCost / 86400; //ex) 하루를 담배값 4500원으로 나눌때, 담배가 4500원 기준이면, 1초에 0.052원이 발생하게 만든다.
                        Log.d("라스트시가코스트", String.valueOf(last_cigaCost));
                        Log.d("시가코스트", String.valueOf(cigaCost));

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
            
            //쓰레드에서 번들정보 가져오기
            Bundle bundle = msg.getData();
            long dateTime = bundle.getLong("dateTime");
            Log.d("데이트타임", String.valueOf(dateTime));
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
            long dateTime = finallyDateTime; //여기에는 날짜를 넣는데, 마찬가지로 초 형식으로 넣는다.
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

    @Override
    public void onPause() { //종료 될 때 디비 값으로 저장한다.
        super.onPause();


        Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        Toast.makeText(getContext(), "성공", Toast.LENGTH_SHORT).show();

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

        Frag_ondestroy frag_ondestroy = new Frag_ondestroy(finallyTime, finallyDate, cigaCount, cigaCost, Eid, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(frag_ondestroy);

        Log.d("뭐야", String.valueOf(finallyTime +"/"+ finallyDate+"/" + cigaCount+"/" + cigaCost+"/" + Eid));
    }

}


