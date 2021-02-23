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
import androidx.room.paging.LimitOffsetDataSource;
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

    //스태틱을 붙여서 Frag1에서 참조할 수 있게 한다.
    public static Button noSmoke_Btn;

    private static final String TAG = "MyTag"; //로그 찍을때,




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



        return viewGroup;
    }



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

