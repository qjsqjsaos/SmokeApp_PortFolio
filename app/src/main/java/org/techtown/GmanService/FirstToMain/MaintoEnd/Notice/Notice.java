package org.techtown.GmanService.FirstToMain.MaintoEnd.Notice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.techtown.GmanService.FirstToMain.R;

public class Notice extends AppCompatActivity {

    private LinearLayout notice1;

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
        setContentView(R.layout.notice);

        // 애드 몹 초기화 //시작
        MobileAds.initialize(this, initializationStatus -> { });

        adContainerView = findViewById(R.id.ad_view_container11);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.admob__unit_TTIBanner));
        adContainerView.addView(adView);
        loadBanner();
        //끝

        notice1 = findViewById(R.id.notice1);
        notice1.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://daldalhanstory.tistory.com/207"));
            startActivity(intent);
        });
    }
}
