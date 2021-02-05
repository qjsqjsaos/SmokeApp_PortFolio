    package org.techtown.study01.FirstToMain.homeMain;

import android.app.Activity;
import android.app.VoiceInteractor;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.firebase.ui.auth.viewmodel.RequestCodes;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.login_fitstPage.Login;

import java.lang.reflect.Method;

    public class BottomNavi extends AppCompatActivity {
        private BottomNavigationView bottomNavigationView;
        private HomeMain fragment1;



        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navi);

            Intent google = ((Login)Login.mcontext).intent;
            Intent kakao = ((Login)Login.mcontext).intent2;

           String google_text = google.getStringExtra("nickName");
           String kakao_text = kakao.getStringExtra("nickName_kakao");




        /**프래그먼트 생성*/

        fragment1 = new HomeMain();
            if(google_text.equals("nickName")) {
                //구글 로그인 데이터 받기
                Intent intent = getIntent();
                String name = intent.getStringExtra("nickName");
                String photo = intent.getStringExtra("photoUrl");
                Log.d("Bundle", String.valueOf(name));
                Log.d("Bundle", String.valueOf(photo));

                //데이터 보내기
                Bundle bundle = new Bundle();
                bundle.putString("nickName", name);
                bundle.putString("photoUrl", photo);
                fragment1.setArguments(bundle);
            }else if(kakao_text.equals("nickName_kakao")) {


                //카카오 로그인 데이터 받기
                Intent intent2 = getIntent();
                String name2 = intent2.getStringExtra("nickName_kakao");
                String photo2 = intent2.getStringExtra("photoUrl_kakao");

                Log.d("Bundle2", String.valueOf(name2));
                Log.d("Bundle2", String.valueOf(photo2));
                //데이터 보내기
                Bundle bundle2 = new Bundle();
                bundle2.putString("nickName_kakao", name2);
                bundle2.putString("photoUrl_kakao", photo2);
                fragment1.setArguments(bundle2);
            }




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