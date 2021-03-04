package org.techtown.study01.FirstToMain.homeMain;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;

import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.fragment.app.Fragment;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag1;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag1_Request;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag_ondestroy;
import org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.SharedViewModel;
import org.techtown.study01.FirstToMain.start.First_page_loading;

import java.text.DecimalFormat;
import java.text.ParseException;

import static android.view.View.GONE;
import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static org.techtown.study01.FirstToMain.homeMain.StartNoSmoking_Dialog.calculate_date;
import static org.techtown.study01.FirstToMain.homeMain.ViewpagerFM.Frag1.dateTimeT;


//홈 화면
public class HomeMain extends Fragment {

    //뷰그룹 부분
    private ViewGroup viewGroup;
    private ImageView userView;
    private TextView nameView;
    private LinearLayout card;

    public static TextView dateView, d_dayView; //프로필에 디데이날짜와 금연날짜이다. 금연시작버튼이나, 다이얼로그안에 금연취소버튼을 누를시 변동한다. 이 값은 Frag1으로 가서 초기화된다.

    //스태틱을 붙여서 Frag1에서 참조할 수 있게 한다. //금연하기 버튼과 취소버튼
    public static Button noSmoke_Btn, stop_Btn;

    //NetworkConnectionCheck 참조할 수 있게 한다.
    public static TextView wiseView;

    public static String id;

    private static final String TAG = "MyTag"; //로그 찍을때,

    private String dateTime = "0";
    private long cigaCount = 0;
    private double cigaCost = 0;

    //저장 뷰모델
    private SharedViewModel sharedViewModel;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        viewGroup = (ViewGroup) inflater.inflate(R.layout.home_main, container, false);

        userView = viewGroup.findViewById(R.id.userView); //프로필사진
        nameView = viewGroup.findViewById(R.id.nickName); //닉네임(프로필)
        dateView = viewGroup.findViewById(R.id.noSmoke_date); //금연날짜(프로필)
        d_dayView = viewGroup.findViewById(R.id.D_dayView);
        wiseView = viewGroup.findViewById(R.id.text_wisesay); //명언액자
        card = viewGroup.findViewById(R.id.card); //프로필
        noSmoke_Btn = viewGroup.findViewById(R.id.button2); //금연하기버튼
        stop_Btn = viewGroup.findViewById(R.id.ns_stop); //금연취소 버튼



            /** 프로필을 클릭했을 때, 이름과 사진 변경 가능*/
            card.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });



            //텍스트뷰 글씨가 바뀔 때 호출한다.
            wiseView.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) { //텍스트가 바뀌기전

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) { //텍스트가 바뀌는 중일 때,

                }

                @Override
                public void afterTextChanged(Editable s) { //텍스트가 바뀌고 난 후
                    String wiseValue = wiseView.getText().toString();
                    if (wiseValue.equals("오류")) {
                        popupLoading(); //firstloding창으로 이동하고,
                        BottomNavi.bottomNavi.finish(); //그 후에 뒤로가기 방지를 위해 아래있는 Bottomnavi를 없애준다.
                    }
                }
            });

            setInit(); //뷰페이저 실행 메서드


            //BottomNavi에서 받은 번들 데이터
            Bundle bundle = this.getArguments();
            Log.d(TAG, "번들가져오고");

            String name = bundle.getString("name");
            id = bundle.getString("id");
            Log.d(TAG, "번들 메세지들 다 가져옴");

            if (name != null) { //일반 로그인
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
                            saveValueToDB2(); //디비에 값 0으로 초기화
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
                float offset = position * -(2 * pageOffset + pageMargin);
                if (-1 > position) {
                    page.setTranslationX(-offset);
                } else if (1 >= position) {
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

    public void popupLoading() { // 만약 인터넷이 연결이 되어 있지 않으면 인텐트를 한다.
        Intent intent = new Intent(getContext(), First_page_loading.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //똑같은 액티비티가 중첩되지 않게 해준다.
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 이전에 사용하던 액티비티를 종료한다.
        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // 그동안 쌓여있던 액티비티를 전부 종료해준다.
        startActivity(intent);

    }

    //디비에 값이 저장되는 메서드
    public void saveValueToDB2() {
        Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {

                        //디비에 저장했음.

                    } else {//실패
                        Toast.makeText(getContext(), "오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "디비오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };

        //모두 취소된 값 0으로 저장
        Frag_ondestroy frag_ondestroy = new Frag_ondestroy(dateTime, cigaCount, cigaCost, id, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(frag_ondestroy);

        Log.d("뭐야", dateTimeT + "/" + Frag1.cigaCount + "/" + Frag1.cigaCost + "/" + HomeMain.id);
    }

    /**
     * SharedViewModel 값 받아서 사용하기
     **/
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        //프로필에 디데이값 넣어주기
        sharedViewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);
        sharedViewModel.getLiveData().observe(getViewLifecycleOwner(), new Observer<Long>() {
            @Override
            public void onChanged(Long Longa) {
                DecimalFormat format = new DecimalFormat("###,###,###,###,###,###,###,###"); // 콤마 표시를 해준다(예 123123 => 123,123
                d_dayView.setText("D+" + format.format(Longa));
                Log.d("디데이", String.valueOf(Longa));



            }
        });

        //프로필에 날짜값 넣어주기
        sharedViewModel.getstartDate().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String str) {
                dateView.setText(str);
                Log.d("날짜짜짜", String.valueOf(str));
            }
        });
    }

    @Override //프래그먼트가 액티비티와 연결될 때 호출됨/ 이 때 디비에서 아이디에 맞게 자료를 가져온다.
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        /** 로그인 하고나서 아이디를 통해 내 정보 불러오고 그의 맞게 버튼 호출*/

        if (id != null) {

            Response.Listener<String> responseListener = new Response.Listener<String>() { //여기서 여기서 Quest1에서 썼던 데이터를 다가져온다.

                @Override
                public void onResponse(String response) {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        boolean success = jsonObject.getBoolean("success");

                        if (success) { //우선 디비 접속이 성공하면, 정보들은 가져온다.

                            dateTimeT = jsonObject.getString("datetime");
                            Log.d("디비정보", dateTimeT);

                            if(dateTimeT.equals("0")) { //여기서 datetime이 0이면(아직 금연을 시작한게 아니거나, 이미 금연을 포기해서 값이 0인 경우)
                                //금연버튼 활성화
                                noSmoke_Btn.setVisibility(VISIBLE);
                               stop_Btn.setVisibility(GONE);


                            }else{
                                //금연 취소버튼 활성화
                                noSmoke_Btn.setVisibility(GONE);
                               stop_Btn.setVisibility(VISIBLE);

                            }

                        } else {//실패
                            return;
                        }


                    } catch (JSONException e) {
                        Toast.makeText(getContext(), "인터넷 연결 후 다시 접속해주세요.1", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                        return;
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "인터넷 연결 후 다시 접속해주세요.2", Toast.LENGTH_SHORT).show();
                        e.printStackTrace();
                    }
                }
            };

            Frag1_Request frag1_request = new Frag1_Request(id, responseListener);
            RequestQueue queue = Volley.newRequestQueue(getContext());
            queue.add(frag1_request);
        }else{
            Toast.makeText(getContext(), "인터넷 연결 후 다시 접속해주세요.3", Toast.LENGTH_SHORT).show();
        }
    }

}

