package org.techtown.GmanService.FirstToMain.findPW;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.techtown.GmanService.FirstToMain.R;

public class AuthforPw extends AppCompatActivity {

    private EditText AuthText;
    private Button AuthButton;
    private String id, pw;
    private int AuthNumber;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authfor_pw);

        // 애드 몹 초기화 //시작
        MobileAds.initialize(this, initializationStatus -> { });

        adContainerView = findViewById(R.id.ad_view_container17);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.admob__unit_TTIBanner));
        adContainerView.addView(adView);
        loadBanner();
        //끝

        AuthText = findViewById(R.id.AuthTextEmail22);
        AuthButton = findViewById(R.id.AuthButton22);


        Intent intent = getIntent(); // FindPw에서 온 아이디와 비밀번호, 인증번호 꺼내기
        id = intent.getStringExtra("id");
        pw = intent.getStringExtra("pw");
        AuthNumber = intent.getIntExtra("authNumber",1); //인트 데이터는 기본값 1붙여줘야함.

        AuthButton.setOnClickListener(new View.OnClickListener() { //만약 인증하기 버튼을 누르면
            @Override
            public void onClick(View v) {
                int editAuthNumber = Integer.parseInt(AuthText.getText().toString());
                if(editAuthNumber == AuthNumber){ //넘어온 번호와 입력한 번호가 같으면
                    Toast.makeText(getApplicationContext(),"인증되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(AuthforPw.this, ChangePw.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    intent1.putExtra("id", id); // 받은 아이디, 비밀번호 다시 넣어서 ChangePw에 전달
                    intent1.putExtra("pw", pw); // 받은 아이디, 비밀번호 다시 넣어서 ChangePw에 전달
                    startActivity(intent1);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(),"인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
}