    package org.techtown.study01.FirstToMain.homeMain;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;

import androidx.appcompat.app.AppCompatActivity;


import org.techtown.study01.FirstToMain.MaintoEnd.HomeMain.HealthCheck;
import org.techtown.study01.FirstToMain.MaintoEnd.Settings.Settings;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.Diary;
import org.techtown.study01.FirstToMain.R;


    public class BottomNavi extends AppCompatActivity {
        private BottomNavigationView bottomNavigationView;
        private HomeMain fragment1; //홈메인
        private HealthCheck fragment2; //상태 변화
        private Diary fragment3; // 일기
        private Settings fragment4; //설정
        private static final String TAG = "MyTag"; //로그 찍을때,




        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navi);


            /**프래그먼트 생성*/

        fragment1 = new HomeMain();
        fragment2 = new HealthCheck();
        fragment3 = new Diary();
        fragment4 = new Settings();


        /**홈메인 영역 */
            // 일반 로그인
            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            Log.d(TAG,"일반로그인");


            //데이터 보내기
            Bundle bundle = new Bundle();
            bundle.putString("name", name);

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
                    case R.id.check_health : {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment2).commitAllowingStateLoss();

                        return true;
                    }
                    case R.id.diary : {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment3).commitAllowingStateLoss();

                        return true;
                    }
                    case R.id.settings : {
                        getSupportFragmentManager().beginTransaction().replace(R.id.container, fragment4).commitAllowingStateLoss();

                        return true;
                    }

                    default:return false;
                }
            }
        });


    }

}