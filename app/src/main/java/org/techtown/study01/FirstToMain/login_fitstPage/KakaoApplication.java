package org.techtown.study01.FirstToMain.login_fitstPage;

import android.app.Application;

import com.kakao.sdk.common.KakaoSdk;

public class KakaoApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        KakaoSdk.init(this,"38558cf656078bb9b03ff6fc7c6f4003"); //카카오 개발자 홈페이지에 네이티브앱키 넣기.
    }
}
