package org.techtown.GmanService.FirstToMain.homeMain;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;


import org.techtown.GmanService.FirstToMain.MaintoEnd.HomeMain.HealthCheck;
import org.techtown.GmanService.FirstToMain.MaintoEnd.Settings.Settings;
import org.techtown.GmanService.FirstToMain.MaintoEnd.Special.Diary;
import org.techtown.GmanService.FirstToMain.R;

import org.techtown.GmanService.FirstToMain.start.First_page_loading;


public class BottomNavi extends AppCompatActivity {
        private BottomNavigationView bottomNavigationView;
        private HomeMain fragment1; //홈메인
        private String TAG = "MyTag"; //로그 찍을때,

        private String id;

        public static BottomNavi bottomNavi;


    private AdView adView;
    private FrameLayout adContainerView;


    /**애드몹 시작*/

    private void loadBanner() {
        AdRequest adRequest = new AdRequest.Builder().build();
        AdSize adSize = getAdSize();
        adView.setAdSize(adSize);
        adView.loadAd(adRequest);

    }

    private AdSize getAdSize() {
        Display display = getWindowManager().getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        display.getMetrics(outMetrics);

        float widthPixels = outMetrics.widthPixels;
        float density = outMetrics.density;

        int adWidth = (int) (widthPixels / density);

        return AdSize.getCurrentOrientationAnchoredAdaptiveBannerAdSize(this, adWidth);
    }

    /**애드몹 끝*/


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
        @Override
        protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bottom_navi);

        // 애드 몹 초기화 //시작
        MobileAds.initialize(this, initializationStatus -> { });

        adContainerView = findViewById(R.id.ad_view_container6);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.admob__unit_TTIBanner));
        adContainerView.addView(adView);
        loadBanner();
        //끝

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


            //데이터 보내기
            Bundle bundle = new Bundle();
            bundle.putString("name", name);
            bundle.putString("id", id);
            fragment1.setArguments(bundle);

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


        public void popupIntent2(){ // 만약 인터넷이 연결이 되어 있지 않으면 인텐트를 한다.
            Intent intent = new Intent(BottomNavi.this, First_page_loading.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //똑같은 액티비티가 중첩되지 않게 해준다.
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 이전에 사용하던 액티비티를 종료한다.
            intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // 그동안 쌓여있던 액티비티를 전부 종료해준다.
            startActivity(intent);
        }



    }

