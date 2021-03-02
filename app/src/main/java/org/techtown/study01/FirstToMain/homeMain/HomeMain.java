package org.techtown.study01.FirstToMain.homeMain;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.viewpager2.widget.ViewPager2;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag1;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag_ondestroy;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


//홈 화면
public class HomeMain extends Fragment {

    //뷰그룹 부분
    private ViewGroup viewGroup;
    private ImageView userView;
    private TextView nameView, dateView;
    private LinearLayout card;

    //스태틱을 붙여서 Frag1에서 참조할 수 있게 한다. //금연하기 버튼과 취소버튼
    public static Button noSmoke_Btn, stop_Btn;

    public static String id;

    private static final String TAG = "MyTag"; //로그 찍을때,


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.home_main, container, false);

        userView = viewGroup.findViewById(R.id.userView); //프로필사진
        nameView = viewGroup.findViewById(R.id.nickName); //닉네임(프로필)
        dateView = viewGroup.findViewById(R.id.noSmoke_date); //금연날짜(프로필)
        card = viewGroup.findViewById(R.id.card); //프로필
        noSmoke_Btn = viewGroup.findViewById(R.id.button2); //금연하기버튼
        stop_Btn = viewGroup.findViewById(R.id.ns_stop); //금연취소 버튼

        setInit(); //뷰페이저 실행 메서드


        //BottomNavi에서 받은 번들 데이터
        Bundle bundle = this.getArguments();
        Log.d(TAG, "번들가져오고");

        String name = bundle.getString("name");
        id = bundle.getString("id");
        Log.d(TAG,"번들 메세지들 다 가져옴");

       if(name != null) { //일반 로그인
            nameView.setText(name); //닉네임으로 이름바꿔주기
            Log.d(TAG, name);
        }

       //금연 취소 눌렀을 때,
       stop_Btn.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) { //다이얼로그 띄우고,
               GiveUpNoSmoking_Dialog giveUpNoSmoking_dialog = new GiveUpNoSmoking_Dialog(getContext());
               giveUpNoSmoking_dialog.NSYES.setOnClickListener(new View.OnClickListener() { //다시도전하기 버튼을 누르면
                   @Override
                   public void onClick(View v) {
                       giveUpNoSmoking_dialog.dialog.dismiss(); //다이아로그 닫기
                   }
               });

               giveUpNoSmoking_dialog.NSNO.setOnClickListener(new View.OnClickListener() { //금연 포기 버튼을 누르면,
                   @Override
                   public void onClick(View v) {
                       noSmoke_Btn.setVisibility(VISIBLE); //금연하기 버튼 보이게 하고,
                       stop_Btn.setVisibility(GONE); //금연중지 버튼 없애기
                       Frag1.timeThread.interrupt();//쓰레드 취소하기(Frag1)
                       giveUpNoSmoking_dialog.dialog.dismiss(); //다이아로그 닫기
                   }
               });

           }
       });


        return viewGroup;
    }


    private void setInit() {

        /* setup infinity scroll viewpager */
        ViewPager2 viewPageSetUp = viewGroup.findViewById(R.id.SetupFrg_ViewPage_Info); //여기서 뷰페이저를 참조한다.
        FragPagerAdapter SetupPagerAdapter = new FragPagerAdapter(getActivity());
        viewPageSetUp.setAdapter(SetupPagerAdapter);
        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPageSetUp.setOffscreenPageLimit(5); //좌우로 몇개까지 미리로딩하고 싶냐는 말이다. ex)1페이지에 있을때 나머지 2, 3, 4, 5, 6 페이지가 미리로딩된다는 뜻이다.

        final float pageMargin = (float) getResources().getDimensionPixelOffset(R.dimen.pageMargin); //페이지끼리 간격
        final float pageOffset = (float) getResources().getDimensionPixelOffset(R.dimen.offset); //페이지 보이는 정도

        viewPageSetUp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
        viewPageSetUp.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float offset = position * - (2 * pageOffset + pageMargin);
                if(-1 > position) {
                    page.setTranslationX(-offset);
                } else if(1 >= position) {
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    page.setTranslationX(offset);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setAlpha(0f);
                    page.setTranslationX(offset);
                }
            }
        });

    }

    ///////////////////////////////////////// 뷰페이저(끝)///////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////////////////////


}

