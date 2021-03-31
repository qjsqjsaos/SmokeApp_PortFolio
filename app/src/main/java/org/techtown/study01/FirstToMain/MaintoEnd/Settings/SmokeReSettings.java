package org.techtown.study01.FirstToMain.MaintoEnd.Settings;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.ads.interstitial.InterstitialAd;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.SharedViewModel;

public class SmokeReSettings extends AppCompatActivity{

    private TextInputEditText cigaCount, cigaCost;
    private Button resettingSmoke, cancelsettings_btn;
    private long cigaCountS, cigaCostS;
    private String firstCost, fisrtCount;

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
        setContentView(R.layout.smoke_re_settings);
        setInit();


        // 애드 몹 띠광고 초기화 //시작
        MobileAds.initialize(this, initializationStatus -> { });
        adContainerView = findViewById(R.id.ad_view_container5);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.admob__unit_TTIBanner));
        adContainerView.addView(adView);
        loadBanner();
        //끝


        try {
            Intent intent = getIntent();
            if (intent != null) {
                cigaCostS = intent.getLongExtra("cost",0);
                cigaCountS = intent.getLongExtra("count",0);
                cigaCost.setText(String.valueOf(cigaCostS));
                cigaCount.setText(String.valueOf(cigaCountS));
                firstCost = cigaCost.getText().toString();
                fisrtCount = cigaCount.getText().toString();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        clickListener();
    }
    /**리스너 모음 */
    private void clickListener() {
        resettingSmoke.setOnClickListener(v -> {
            updateSmokeSettings();
        });
        cancelsettings_btn.setOnClickListener(v ->{
            finish();
        });

    }


    /**참조하기 */
    private void setInit() {
        cigaCount = findViewById(R.id.cigaCount);
        cigaCost = findViewById(R.id.cigaCost);
        resettingSmoke = findViewById(R.id.resettingSmoke);
        cancelsettings_btn = findViewById(R.id.cancelsettings_btn);
    }
    /**흡연량과 흡연비용 업데이트 하기 */
    private void updateSmokeSettings() {
        int newCigaCost = 0;
        int newCigaCount = 0;
        String C1 = null, C2 = null;

        try {
            newCigaCost = Integer.parseInt(cigaCost.getText().toString());
            newCigaCount = Integer.parseInt(cigaCount.getText().toString());
            C1 = cigaCost.getText().toString();
            C2 = cigaCount.getText().toString();
        }catch (Exception e){
            e.printStackTrace();

        }

       if(C1 == null || C2 == null){
           Toast.makeText(this, "빈칸을 입력해주세요.", Toast.LENGTH_SHORT).show();
           return;
       }else if (newCigaCost == 0 || newCigaCount == 0){
           Toast.makeText(this, "갯수나 금액이 0일 수 없습니다.", Toast.LENGTH_SHORT).show();
           return;
       }else if(firstCost.equals(String.valueOf(newCigaCost)) && fisrtCount.equals(String.valueOf(newCigaCount))){ //똑같은 값에 적용을 누를 때,
            Toast.makeText(this, "새로운 값이 설정되었습니다.", Toast.LENGTH_SHORT).show();
            finish();
            return;
       }else if(newCigaCost > 50000){
           Toast.makeText(this, "5만원 이하로 입력이 가능합니다.", Toast.LENGTH_SHORT).show();
           return;
       }

        int finalNewCigaCost = newCigaCost;
        int finalNewCigaCount = newCigaCount;
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                            Toast.makeText(getApplication(), "새로운 값이 설정되었습니다.", Toast.LENGTH_SHORT).show();
                            HomeMain homeMain = new HomeMain();
                            Bundle bundle = new Bundle();
                            bundle.putLong("cost", finalNewCigaCost);
                            bundle.putLong("count", finalNewCigaCount);
                            homeMain.setArguments(bundle);
                            finish();
                    } else {//실패
                        Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        Smoke_Settings_Request smoke_settings_request = new Smoke_Settings_Request(newCigaCount, newCigaCost, HomeMain.num, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(smoke_settings_request);

    }

}