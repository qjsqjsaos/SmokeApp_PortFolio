package org.techtown.study01.FirstToMain.homeMain;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
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
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag1;
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
    public String timeOk, dateOk; //시간타이머 값 성공할 때 값, //날짜 값 성공할 때 값

    //다이어로그 부분
    private Calculate_Date calculate_date;
    private TextView date, time;
    private Button back;
    private DatePickerDialog.OnDateSetListener callbackMethod;
    private TimePickerDialog.OnTimeSetListener callbackMethod2;

    //Frag1으로 이 버튼을 보내기 위해 public static을 달아주었다.
    public static Button start_stop_smoking;
    public static Dialog dialog; //dialog 객체






    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.home_main, container, false);

        userView = viewGroup.findViewById(R.id.userView); //프로필사진
        nameView = viewGroup.findViewById(R.id.nickName); //닉네임(프로필)
        dateView = viewGroup.findViewById(R.id.noSmoke_date); //금연날짜(프로필)
        card = viewGroup.findViewById(R.id.card); //프로필
        noSmoke_Btn = viewGroup.findViewById(R.id.button2); //금연하기버튼

        Button button = viewGroup.findViewById(R.id.stop);

        setInit(); //뷰페이저 실행 메서드


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

        public void StartStopSmoking_Btn() {

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

            if (date != null) {
                date.setText(datetoday); //현재 날짜 기본값
            }

            if (time != null) {
                time.setText(timenow); // 현재 시간 기본값
            }

            date.setOnClickListener(new View.OnClickListener() {  //날짜 지정할 수 있는 데이트피커 다이얼로그 나타내기
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

            start_stop_smoking.setOnClickListener(new View.OnClickListener() { //금연시작 버튼 눌럿을때,
                @Override
                public void onClick(View v) {
                        timeOk = time.getText().toString();
                        dateOk = date.getText().toString();


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


    ///////////////////////////////////////// 쓰레드와 핸들러 (끝)///////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    ///////////////////////////////////////// 뷰페이저(시작)///////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


    private void setInit() {

        /* setup infinity scroll viewpager */
        ViewPager2 viewPageSetUp = viewGroup.findViewById(R.id.SetupFrg_ViewPage_Info); //여기서 뷰페이저를 참조한다.
        FragPagerAdapter SetupPagerAdapter = new FragPagerAdapter(getActivity());
        viewPageSetUp.setAdapter(SetupPagerAdapter);
        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPageSetUp.setOffscreenPageLimit(6);
        // 무제한 스크롤 처럼 보이기 위해서는 0페이지 부터가 아니라 1000페이지 부터 시작해서 좌측으로 이동할 경우 999페이지로 이동하여 무제한 처럼 스크롤 되는 것 처럼 표현하기 위함.
        viewPageSetUp.setCurrentItem(1000);

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
                float offset = position * - (2 * pageOffset + pageMargin);
                if(-1 > position) {
                    page.setTranslationX(-offset);
                } else if(1 >= position) {
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

    ///////////////////////////////////////// 뷰페이저(끝)///////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////

}

