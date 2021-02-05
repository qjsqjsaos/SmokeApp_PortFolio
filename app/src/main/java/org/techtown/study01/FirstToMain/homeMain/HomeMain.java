package org.techtown.study01.FirstToMain.homeMain;

import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.login_fitstPage.Login;

//홈 화면
public class HomeMain extends Fragment{

    ViewGroup viewGroup;
    ImageView userView;
    TextView nameView;
    TextView dateView;
    LinearLayout card;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        viewGroup = (ViewGroup) inflater.inflate(R.layout.home_main, container, false);

        userView = viewGroup.findViewById(R.id.userView);
        nameView = viewGroup.findViewById(R.id.nickName);
        dateView = viewGroup.findViewById(R.id.noSmoke_date);
        card = viewGroup.findViewById(R.id.card);

        //BottomNavi에서 받은 번들 데이터(구글)
        Bundle bundle = this.getArguments();
        if (bundle != null) {

            String nickName = bundle.getString("nickName");
            nameView.setText(nickName); //닉네임으로 이름바꿔주기
            Log.d("fragment",nickName);
            /** dateView.setText();*/ //여기는 금연 설정할 때 값 받아올 때 넣어야함.!!!!!!!!!!!!!

            String photoUrl = bundle.getString("photoUrl");
            Glide.with(this).load(photoUrl).circleCrop().into(userView); //프로필 url(photoUrl)을 이미지 뷰에 세팅
            Log.d("fragment",photoUrl);
        }

        //BottomNavi에서 받은 번들 데이터(카카오)
        Bundle bundle2 = this.getArguments();
        if (bundle2 != null) {

            String nickName2 = bundle.getString("nickName_kakao");
            nameView.setText(nickName2); //닉네임으로 이름바꿔주기
            Log.d("fragment2",nickName2);
            /** dateView.setText();*/ //여기는 금연 설정할 때 값 받아올 때 넣어야함.!!!!!!!!!!!!!

            String photoUrl2 = bundle.getString("photoUrl_kakao");
            Glide.with(this).load(photoUrl2).circleCrop().into(userView); //프로필 url(photoUrl)을 이미지 뷰에 세팅
            Log.d("fragment2",photoUrl2);
        }

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            /** 다이어 로그  나오게하기(그림, 설정 바꾸기)*/
            }
        });



        return viewGroup;
    }

}