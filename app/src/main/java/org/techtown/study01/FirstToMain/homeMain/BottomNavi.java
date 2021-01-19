    package org.techtown.study01.FirstToMain.homeMain;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.techtown.study01.FirstToMain.R;

public class BottomNavi extends AppCompatActivity {
    BottomNavigationView bottomNavigationView;
    HomeMain fragment1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navi);

        bottomNavigationView = findViewById(R.id.nav_view);

        /**프래그먼트 생성*/
        fragment1 = new HomeMain();

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