package org.techtown.study01.FirstToMain.homeMain;


import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Parcelable;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;

import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.jetbrains.annotations.NotNull;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag1;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag1_Request;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag2;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag3;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag5;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag_ondestroy;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.SharedViewModel;
import org.techtown.study01.FirstToMain.start.First_page_loading;

import java.text.ParseException;

import static android.view.View.GONE;

import static android.view.View.VISIBLE;


//홈 화면
public class HomeMain extends Fragment {

    //뷰그룹 부분
    private ViewGroup viewGroup;
    private ImageView userView;
    private TextView nameView;
    private LinearLayout card;

    public static TextView dateView, d_dayView; //프로필에 디데이날짜와 금연날짜이다. 금연시작버튼이나, 다이얼로그안에 금연취소버튼을 누를시 변동한다. 이 값은 Frag1으로 가서 초기화된다.

    //스태틱을 붙여서 Frag1에서 참조할 수 있게 한다. //금연하기 버튼과 취소버튼
    public static Button noSmoke_Btn, stop_Btn;

    //NetworkConnectionCheck 참조할 수 있게 한다.
    public static TextView wiseView;

    public static String id;

    //쓰레드 부분
    public static Thread timeThread = null;
    private final Boolean isRunning = true;

    private static final String TAG = "MyTag"; //로그 찍을때,

    //저장 뷰모델
    private SharedViewModel sharedViewModel;


    /////////////////////Frag1이였던 것//////////////////////////


    //금연한지 얼마나 됬는지 날짜 값
    public static long finallyDateTime;

    //Quest1에서 가져온 담배 핀 횟수와 비용 EditText
    /** 이 카운트와 코스트는 다음에 값 전달하기*/
    public static long cigaCount;
    public static double cigaCost; //이건 1초에 나타나는 비용이 소수점까지 가므로, long으로 표기한다.

    //중요 지정했던 시간이다. 디비에 넣었다가 뺄 때, 몇초 지났는지 구별해주는 시간이다.
    public static String dateTime;


    //하루를 기준으로 피는 담배양을 하루 24시간으로 나눈 시간. ex) 하루에 10개비를 피면 2시간 24분 마다 핀것이다. 여기서 2시간 24분의 값을 초로 나타낸 것이다.
    public static long last_cigaCount;
    public static double last_cigaCost;


    /** 다른 프래그먼트로 이동 시 쓰레드 종료(어차피 돌아오면 다시 켜짐)*/
    @Override
    public void onStop() { //프래그먼트가 안 보일때 호출
        super.onStop();
        if (dateTime != null) {
            if (!dateTime.equals("0")) { //타이머 실행이 안될 때 전환시 쓰레드 끄기
                timeThread.interrupt();
            }
        }
    }

    /** 앱이 맨 처음 실행될 때, 아이디값을 통해 정보를 가져온다.*/
    @Override
    public void onStart() { //프래그먼트가 처음 보일때 호출
        super.onStart();
        idcheckandButton(); //아이디를 토대로 버튼정보가져오기
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.home_main, container, false);

        userView = viewGroup.findViewById(R.id.userView); //프로필사진
        nameView = viewGroup.findViewById(R.id.nickName); //닉네임(프로필)
        dateView = viewGroup.findViewById(R.id.noSmoke_date); //금연날짜(프로필)
        d_dayView = viewGroup.findViewById(R.id.D_dayView);
        wiseView = viewGroup.findViewById(R.id.text_wisesay); //명언액자
        card = viewGroup.findViewById(R.id.card); //프로필
        noSmoke_Btn = viewGroup.findViewById(R.id.button2); //금연하기버튼
        stop_Btn = viewGroup.findViewById(R.id.ns_stop); //금연취소 버튼


            startNoSmokingButton(); //금연시작 버튼

            /** 프로필을 클릭했을 때, 이름과 사진 변경 가능*/
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });



            //텍스트뷰 글씨가 바뀔 때 호출한다.
            wiseView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { //텍스트가 바뀌기전

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { //텍스트가 바뀌는 중일 때,

                }

                @Override
                public void afterTextChanged(Editable s) { //텍스트가 바뀌고 난 후
                    String wiseValue = wiseView.getText().toString();
                    if (wiseValue.equals("오류")) {
                        popupLoading(); //firstloding창으로 이동하고,
                        BottomNavi.bottomNavi.finish(); //그 후에 뒤로가기 방지를 위해 아래있는 Bottomnavi를 없애준다.
                    }
                }
            });

            setInit(); //뷰페이저 실행 메서드


            //BottomNavi에서 받은 번들 데이터
            Bundle bundle = this.getArguments();
            Log.d(TAG, "번들가져오고");

            String name = bundle.getString("name");
            id = bundle.getString("id");
            Log.d(TAG, "번들 메세지들 다 가져옴");

            if (name != null) { //일반 로그인
                nameView.setText(name); //닉네임으로 이름바꿔주기
                Log.d(TAG, name);
            }

            //금연 취소 눌렀을 때,
            stop_Btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) { //다이얼로그 띄우고,
                    GiveUpNoSmoking_Dialog giveUpNoSmoking_dialog = new GiveUpNoSmoking_Dialog(getContext());
                    giveUpNoSmoking_dialog.NSYES.setOnClickListener(new View.OnClickListener() { //다시도전하기 버튼을 누르면
                        @Override
                        public void onClick(View v) {
                            giveUpNoSmoking_dialog.dialog.dismiss(); //다이아로그 닫기
                        }
                    });

                    giveUpNoSmoking_dialog.NSNO.setOnClickListener(new View.OnClickListener() { //금연 포기 버튼을 누르면,
                        @Override
                        public void onClick(View v) {
                            int value = 0;
                            String svalue = "0";
                            saveValueToDB(value, svalue); //디비에 값 0으로 초기화
                            noSmoke_Btn.setVisibility(VISIBLE); //금연하기 버튼 보이게 하고,
                            stop_Btn.setVisibility(GONE); //금연중지 버튼 없애기
                            timeThread.interrupt();//쓰레드 취소하기(Frag1)
                            giveUpNoSmoking_dialog.dialog.dismiss(); //다이아로그 닫기
                        }
                    });

                }
            });

            return viewGroup;
    }




    /** 금연하기 버튼을 클릭하고나서 금연시간 정하기*/

    public void startNoSmokingButton() {

        //다이얼로그 금연하기와 돌아가기 버튼을 눌렀을때, Frag1에서에 액션을 정할 수 있다.
        noSmoke_Btn.setOnClickListener(new View.OnClickListener() { //홈메인에 있는 버튼을 가져와서 클릭한다.
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

                        noSmoke_Btn.setVisibility(GONE); //금연버튼을 비활성화
                        stop_Btn.setVisibility(VISIBLE); //취소버튼을 활성화


                        saveValueToDB2(); //디비에 저장

                        timeThread = new Thread(new timeThread());
                        timeThread.start(); //쓰레드실행

                    }

                });
            }
        });
    }




    /** 타이머 핸들러*/
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
            sharedViewModel.setstartDate(result); //타이머 실시간 표시

            sharedViewModel.setLiveData(day); //ViewModel을 통해서 Frag2 Homemain으로 보내기 위해 Livedata에 oneDay를 보낸다.

            sharedViewModel.setLiveDataCount(ciga_Time); //ViewModel을 통해서 Frag5로 보내기 위해 Livedata에 ciga_Time 보낸다.

            sharedViewModel.setLiveDataCost(ciga_Money); //ViewModel을 통해서 Frag3로 보내기 위해 Livedata에 ciga_Money 보낸다.

            //데이랑 시간 입력

        }
    };





    /** 타이머 쓰레드*/
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
                                Frag1.textView.setText(""); //한번 빈칸으로 초기화시켜주기
                                Frag1.textView.setText("오늘을 기준으로\n\n00:00:00 시간 째 금연 중");
                                Frag2.textView2.setText(""); //한번 빈칸으로 초기화시켜주기
                                Frag2.textView2.setText("벌써 000일이 되었어요!");
                                Frag3.textView3.setText(""); //한번 빈칸으로 초기화시켜주기
                                Frag3.textView3.setText("0원");
                                Frag5.textView5.setText(""); //한번 빈칸으로 초기화시켜주기
                                Frag5.textView5.setText("0개비 가량 됩니다!");
                                dateView.setText("금연날짜");
                                HomeMain.d_dayView.setText("D+");
                            }
                        });
                        return; // 인터럽트 받을 경우 return (취소)
                    }
                }
            }
        }
    }


    /** 뷰모델 저장소 다른 프래그먼트로 값을 전달한다.*/
    //onCreateView에서 리턴해준 View(rootView)를 가지고 있다.
    //저장된 뷰가 반환된 직후에 호출됩니다.
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) { //뷰가 제공되는 경우 반환된 뷰를 가져옵니다.
        super.onViewCreated(view, savedInstanceState);

        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

        //지정된 Factory를 통해 ViewModel을 만들고 지정된 ViewModelStoreOwner의 저장소에 유지하는 ViewModelProvider를 만듭니다.

    }



    /** 뷰페이저 2 실행하기*/
    private void setInit() {

        /* setup infinity scroll viewpager */
        ViewPager2 viewPageSetUp = viewGroup.findViewById(R.id.SetupFrg_ViewPage_Info); //여기서 뷰페이저를 참조한다.
        FragPagerAdapter SetupPagerAdapter = new FragPagerAdapter(getActivity());
        viewPageSetUp.setAdapter(SetupPagerAdapter);
        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPageSetUp.setOffscreenPageLimit(5); //좌우로 몇개까지 미리로딩하고 싶냐는 말이다. ex)1페이지에 있을때 나머지 2, 3, 4, 5, 6 페이지가 미리로딩된다는 뜻이다.
        viewPageSetUp.setSaveEnabled(false);

        final float pageMargin = (float) getResources().getDimensionPixelOffset(R.dimen.pageMargin); //페이지끼리 간격
        final float pageOffset = (float) getResources().getDimensionPixelOffset(R.dimen.offset); //페이지 보이는 정도

        viewPageSetUp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
        viewPageSetUp.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float offset = position * -(2 * pageOffset + pageMargin);
                if (-1 > position) {
                    page.setTranslationX(-offset);
                } else if (1 >= position) {
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    page.setTranslationX(offset);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setAlpha(0f);
                    page.setTranslationX(offset);
                }
            }
        });

    }




    /** 퍼스트 페이지 로딩 페이지 띄우기 (인터넷 연결 안될시에)*/

    public void popupLoading() { // 만약 인터넷이 연결이 되어 있지 않으면 인텐트를 한다.
        Intent intent = new Intent(getContext(), First_page_loading.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //똑같은 액티비티가 중첩되지 않게 해준다.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 이전에 사용하던 액티비티를 종료한다.
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // 그동안 쌓여있던 액티비티를 전부 종료해준다.
        startActivity(intent);

    }


    /** 금연을 포기하여 0으로 저장되는 곳*/

    public void saveValueToDB(int value, String svalue) {
        Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {

                        //디비에 저장했음.

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

        //모두 취소된 값 0으로 저장
        Frag_ondestroy frag_ondestroy = new Frag_ondestroy(svalue, value, value, id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(frag_ondestroy);
        Log.d("뭐야", svalue + "/" + value + "/" + HomeMain.id);
    }


    /** 버튼을 누른 값이 저장되는 곳 0이 아님*/
    public void saveValueToDB2() {
        Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {

                        //디비에 저장했음.

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

        //모두 취소된 값 0으로 저장
        Frag_ondestroy frag_ondestroy = new Frag_ondestroy(dateTime, cigaCount, cigaCost, id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(frag_ondestroy);
        Log.d("뭐야", dateTime + "/" + cigaCount + "/" + cigaCost + "/" + HomeMain.id);
    }


    /** 로그인 하고나서 아이디를 통해 내 정보 불러오고 그의 맞게 버튼 호출*/
    public void idcheckandButton(){
        if (id != null) {

            Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) { //우선 디비 접속이 성공하면, 정보들은 가져온다.

                            dateTime = jsonObject.getString("datetime");
                            Log.d("디비정보", dateTime);

                            if(dateTime.equals("0")) { //여기서 datetime이 0이면(아직 금연을 시작한게 아니거나, 이미 금연을 포기해서 값이 0인 경우)
                                //금연버튼 활성화
                                noSmoke_Btn.setVisibility(VISIBLE);
                                stop_Btn.setVisibility(GONE);
                                //쓰레드 실행안함.
                            }else{
                                //금연 취소버튼 활성화
                                noSmoke_Btn.setVisibility(GONE);
                                stop_Btn.setVisibility(VISIBLE);
                                startThreadShow(); //쓰레드 실행
                            }

                        } else {//실패
                            return;
                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            Frag1_Request frag1_request = new Frag1_Request(id, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(frag1_request);
        }
    }



    /** 따로 만든 시작 쓰레드*/
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

        timeThread = new Thread(new timeThread());
        timeThread.start(); //쓰레드실행
    }


}

