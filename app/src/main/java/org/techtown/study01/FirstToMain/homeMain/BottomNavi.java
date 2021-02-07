    package org.techtown.study01.FirstToMain.homeMain;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.SurfaceControl;

import com.firebase.ui.auth.viewmodel.RequestCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.login_fitstPage.Login;

import java.lang.reflect.Method;

    public class BottomNavi extends AppCompatActivity {
        private BottomNavigationView bottomNavigationView;
        private HomeMain fragment1;
        private static final String TAG = "MyTag"; //로그 찍을때,




        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navi);


            /**프래그먼트 생성*/

        fragment1 = new HomeMain();

            // 일반 로그인
            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            Log.d(TAG,"일반로그인");

            //구글로그인

            String name2 = intent.getStringExtra("nickName");
            Log.d(TAG,"구글로그인 닉네임 성공");
            String photo = intent.getStringExtra("photoUrl");
            Log.d(TAG,"구글로그인 프로필사진 성공");


            //데이터 보내기
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("nickName", name2);
            bundle.putString("photoUrl", photo);

            Log.d("bundle", String.valueOf(bundle));
            fragment1.setArguments(bundle);
            Log.d(TAG,"번들 보내기");

        bottomNavigationView = findViewById(R.id.nav_view);



        /**제일 처음 띄워주는 프래그먼트*/
        getSupportFragmentManager().beginTransaction().replace(R.id.container,fragment1).commitAllowingStateLoss();

        /**아이콘 선택시 원하는 프래그먼트가 띄워지게 하기*/
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                /**각 탭에 아이디의 따라 다르게 띄워주기*/
                switch (item.getItemId()) {
                    case R.id.home : {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment1).commitAllowingStateLoss();

                        return true;
                    }

                    default:return false;
                }
            }
        });


    }


}