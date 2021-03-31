package org.techtown.study01.FirstToMain.homeMain;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.google.firebase.remoteconfig.FirebaseRemoteConfigSettings;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import org.techtown.study01.FirstToMain.MaintoEnd.HomeMain.HealthCheck;
import org.techtown.study01.FirstToMain.MaintoEnd.Settings.Settings;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.Diary;
import org.techtown.study01.FirstToMain.R;

import org.techtown.study01.FirstToMain.start.First_page_loading;


public class BottomNavi extends AppCompatActivity {
        private BottomNavigationView bottomNavigationView;
        private HomeMain fragment1; //홈메인
        private String TAG = "MyTag"; //로그 찍을때,

        private String id;

        public static BottomNavi bottomNavi;

        private FirebaseRemoteConfig remoteConfig;
        private long newAppVersion = 1;
        public static long toolbarImgCount = 15;



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navi);

            getRemoteConfig(); //파이어베이스 리모트 컨피고(업데이트 알림)


            //서비스실행 (인터넷이 연결되어 있는지 아닌지 확인해준다.(NetworkConnectionCheck -> MyService -> BottomNavi)) 항상 실행중
        Intent serviceIntent = new Intent(this,MyService.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){ //api level 26부터
            this.startForegroundService(serviceIntent); //이 메서드를 쓰고 그게아니면,
        }else{
            this.startService(serviceIntent); //이것을 쓴다.
        }



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
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.container, fragment1, "home")
                    .commitAllowingStateLoss();

            /**아이콘 선택시 원하는 프래그먼트가 띄워지게 하기*/ /** 프래그먼트 replace(탭 이동시 새로생성)을 방지하기위해 add와 hide, show를 이용하여, 한 번만 뜨게 만들도록 하였다.*/
            bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    /**각 탭에 아이디의 따라 다르게 띄워주기*/
                    FragmentManager fragmentManager = getSupportFragmentManager();

                    switch (item.getItemId()) {
                        case R.id.home : {
                            if (fragmentManager.findFragmentByTag("home") != null) {
                                //프래그먼트가 존재한다면 보여준다.
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("home")).commit();
                            } else {
                                //존재하지 않는다면 프래그먼트를 매니저에 추가
                                fragmentManager.beginTransaction().add(R.id.container, new HomeMain(), "home").commit();
                            }
                            if (fragmentManager.findFragmentByTag("health") != null) {
                                //다른프래그먼트가 보이면 가려준다.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("health")).commit();
                            }
                            if (fragmentManager.findFragmentByTag("diary") != null) {
                                //다른프래그먼트가 보이면 가려준다.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("diary")).commit();
                            }
                            if (fragmentManager.findFragmentByTag("settings") != null) {
                                //다른프래그먼트가 보이면 가려준다.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("settings")).commit();
                            }
                            return true;
                        }
                        case R.id.check_health : {


                            if (fragmentManager.findFragmentByTag("health") != null) {
                                //if the fragment exists, show it.
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("health")).commit();
                            } else {
                                //if the fragment does not exist, add it to fragment manager.
                                fragmentManager.beginTransaction().add(R.id.container, new HealthCheck(), "health").commit();
                            }
                            if (fragmentManager.findFragmentByTag("home") != null) {
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")).commit();
                            }
                            if (fragmentManager.findFragmentByTag("diary") != null) {
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("diary")).commit();
                            }
                            if (fragmentManager.findFragmentByTag("settings") != null) {
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("settings")).commit();
                            }
                            return true;
                        }
                        case R.id.diary : {

                            if (fragmentManager.findFragmentByTag("diary") != null) {
                                //if the fragment exists, show it.
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("diary")).commit();
                            } else {
                                //if the fragment does not exist, add it to fragment manager.
                                fragmentManager.beginTransaction().add(R.id.container, new Diary(), "diary").commit();
                            }
                            if (fragmentManager.findFragmentByTag("home") != null) {
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")).commit();
                            }
                            if (fragmentManager.findFragmentByTag("health") != null) {
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("health")).commit();
                            }
                            if (fragmentManager.findFragmentByTag("settings") != null) {
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("settings")).commit();
                            }

                            return true;
                        }
                        case R.id.settings : {

                            if (fragmentManager.findFragmentByTag("settings") != null) {
                                //if the fragment exists, show it.
                                fragmentManager.beginTransaction().show(fragmentManager.findFragmentByTag("settings")).commit();
                            } else {
                                //if the fragment does not exist, add it to fragment manager.
                                fragmentManager.beginTransaction().add(R.id.container, new Settings(), "settings").commit();
                            }
                            if (fragmentManager.findFragmentByTag("home") != null) {
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("home")).commit();
                            }
                            if (fragmentManager.findFragmentByTag("health") != null) {
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("health")).commit();
                            }
                            if (fragmentManager.findFragmentByTag("diary") != null) {
                                //if the other fragment is visible, hide it.
                                fragmentManager.beginTransaction().hide(fragmentManager.findFragmentByTag("diary")).commit();
                            }

                            return true;
                        }

                        default:return false;
                    }
                }
            });

    }


        // TODO: 2021-03-16 존나 중요!! 파이어베이스
        @SuppressLint("NewApi")
        private void checkVersion() {
            /**나중에 해두기 업데이트 체크 부분 위에도*/
            try {
                PackageInfo pi = getPackageManager().getPackageInfo(getPackageName(), 0);
                long appVersion;
                appVersion = pi.getLongVersionCode();
                Log.d("버전", String.valueOf(appVersion));

                if (appVersion < newAppVersion) { //사용자의 버전이 현재 나온 앱 버전보다 낮을때
                    updateDialog(); //업데이트 알림
                    return;
                }
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }

        }

        private void updateDialog() {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("업데이트 알림");
            builder.setMessage("최신버전이 등록되었습니다.\n업데이트가 필요합니다.")
                    .setCancelable(false)
                    .setPositiveButton("업데이트", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("market://details?id=org.techtown.study01.FirstToMain.homeMain"));
                            //플레이스토어로감
                            startActivity(intent);
                            dialog.cancel();
                            Toast.makeText(BottomNavi.this, "성공", Toast.LENGTH_SHORT).show();
                        }
                    });
            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        }


        private void getRemoteConfig() {
            /**나중에 해두기 업데이트 체크 부분 위에도*/

            //파이에베이스 리모트콘피그(앱이 업데이트 되었을때 사용자에게 알리기 위해)
            remoteConfig = FirebaseRemoteConfig.getInstance();
            FirebaseRemoteConfigSettings configSettings = new FirebaseRemoteConfigSettings.Builder()
                    .build();
            remoteConfig.setConfigSettingsAsync(configSettings);
            remoteConfig.setDefaultsAsync(R.xml.remote_config_defaults);

            remoteConfig.fetchAndActivate()
                    .addOnCompleteListener(this, new OnCompleteListener<Boolean>() {
                        @Override
                        public void onComplete(@NonNull Task<Boolean> task) {
                            newAppVersion = remoteConfig.getLong("new_app_version");
                            toolbarImgCount = remoteConfig.getLong("toolbar_img_count");
                            checkVersion();
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

