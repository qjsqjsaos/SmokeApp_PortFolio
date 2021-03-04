    package org.techtown.study01.FirstToMain.homeMain;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.service.controls.templates.ControlTemplate;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import org.techtown.study01.FirstToMain.MaintoEnd.HomeMain.HealthCheck;
import org.techtown.study01.FirstToMain.MaintoEnd.Settings.Settings;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.Diary;
import org.techtown.study01.FirstToMain.R;

import org.techtown.study01.FirstToMain.start.First_page_loading;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


    public class BottomNavi extends AppCompatActivity {
        private BottomNavigationView bottomNavigationView;
        private HomeMain fragment1; //홈메인
        private HealthCheck fragment2; //상태 변화
        private Diary fragment3; // 일기
        private Settings fragment4; //설정
        private static final String TAG = "MyTag"; //로그 찍을때,

        private String id;

        public static BottomNavi bottomNavi;


        @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navi);

        //서비스실행 (인터넷이 연결되어 있는지 아닌지 확인해준다.(NetworkConnectionCheck -> MyService -> BottomNavi)) 항상 실행중
        Intent serviceIntent = new Intent(this,MyService.class);
        startService(serviceIntent);


            //네트워크가 즉, 데이터가 없는 인터넷이 없는 상태에서 접속하면, firstloading창 보여주기
            ConnectivityManager connManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //23버전부터 사용가능
                Network network = connManager.getActiveNetwork();

                if(network == null){ //인터넷 연결이 안되어있으면,
                    popupIntent2(); //firstloading창으로 이동하고,
                    finish(); //액티비티 전부 종료.
                }
            }


            /**프래그먼트 생성*/

        fragment1 = new HomeMain();
        fragment2 = new HealthCheck();
        fragment3 = new Diary();
        fragment4 = new Settings();


        /**홈메인 영역 */
            // 일반 로그인
            Intent intent = getIntent();
            String name = intent.getStringExtra("name");
            id = intent.getStringExtra("id");
            Log.d(TAG,"일반로그인");


            //데이터 보내기
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("id", id);

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


        public void popupIntent2(){ // 만약 인터넷이 연결이 되어 있지 않으면 인텐트를 한다.
            Intent intent = new Intent(BottomNavi.this, First_page_loading.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //똑같은 액티비티가 중첩되지 않게 해준다.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 이전에 사용하던 액티비티를 종료한다.
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // 그동안 쌓여있던 액티비티를 전부 종료해준다.
            startActivity(intent);
        }

    }