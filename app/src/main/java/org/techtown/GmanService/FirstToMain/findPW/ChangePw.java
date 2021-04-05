package org.techtown.GmanService.FirstToMain.findPW;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.GmanService.FirstToMain.R;
import org.techtown.GmanService.FirstToMain.findid.Id_pw_complete;
import org.techtown.GmanService.FirstToMain.homeMain.Loading_Dialog;
import org.techtown.GmanService.FirstToMain.register.SHA516_Hash_InCode;

import java.util.regex.Pattern;

public class ChangePw extends AppCompatActivity {

    private EditText pw, pwcheck;
    private String newpw, pw2, hashPw2, saltHash2, Epw, Eid;
    private String messagepPw = "비밀번호찾기"; //아이디찾기인지 비밀번호 찾기인지 식별하기 위한 메세지



    //로딩창 띄우기
    private Loading_Dialog loading_dialog;

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
        setContentView(R.layout.change_pw);

        // 애드 몹 초기화 //시작
        MobileAds.initialize(this, initializationStatus -> { });

        adContainerView = findViewById(R.id.ad_view_container16);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.admob__unit_TTIBanner));
        adContainerView.addView(adView);
        loadBanner();
        //끝

        //아이디 비번 인텐트로 가져오기
        Intent intent = getIntent();
        Eid = intent.getStringExtra("id");
        Epw = intent.getStringExtra("pw"); //이미 해시화가 됨

        pw = findViewById(R.id.IdforPw); //비밀번호
        pwcheck = findViewById(R.id.AuthTextEmail33); //비밀번호 확인



        Button button = findViewById(R.id.sendForPw);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingStart();//로딩창 띄우기

                /** 비밀번호 해시화 중요(보안)*/

//                //새로운 비밀번호 전 비밀번호와 구별을 위해 해시화
//                newpw = pw.getText().toString();
//                saltHash2 = "보안상 가립니다.";
//                SHA516_Hash_InCode hash_inCode = new SHA516_Hash_InCode();
//                hashPw2 = hash_inCode.SHA516_Hash_InCode(saltHash2); //솔팅한 값 해시화한 값

                pw2 = pwcheck.getText().toString();


                if(!newpw.equals(pw2)) { //비밀번호와 비밀번호 확인이 맞지 않을때,
                    loading_dialog.cancel(); //로딩창 닫기
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newpw.equals("") || pw2.equals("")){ //둘 중 하나라도 빈칸이면,
                    loading_dialog.cancel(); //로딩창 닫기
                    Toast.makeText(getApplicationContext(), "비밀번호를 전부 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Epw.equals(hashPw2)) {//전에 쓰던 비밀번호와 같을때,
                    loading_dialog.cancel(); //로딩창 닫기
                    Toast.makeText(getApplicationContext(), "전에 쓰던 비밀번호는 사용하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,20}.$", newpw)) { //비밀번호 형식 안지킬시
                    loading_dialog.cancel(); //로딩창 닫기
                    Toast.makeText(getApplicationContext(), "영문+숫자+특수문자 각 1자 이상 포함 8~20자 글자여야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(ChangePw.this, Id_pw_complete.class);
                                intent1.putExtra("findPw", messagepPw);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent1);
                                finish();
                                loading_dialog.cancel(); //로딩창 닫기
                            } else {//로그인 실패시
                                loading_dialog.cancel(); //로딩창 닫기
                                Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }


                        } catch (JSONException e) {
                            loading_dialog.cancel(); //로딩창 닫기
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "비밀번호 오류1, 문의부탁드립니다.", Toast.LENGTH_SHORT).show();
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            loading_dialog.cancel(); //로딩창 닫기
                            Toast.makeText(getApplicationContext(), "비밀번호 오류2, 문의부탁드립니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                };

                ChangePwRequest changePwRequest = new ChangePwRequest(Eid, newpw, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ChangePw.this);
                queue.add(changePwRequest);
            }
        });
    }

    /**로딩창 띄우기 */
    public void loadingStart(){
        loading_dialog = new Loading_Dialog(ChangePw.this);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false); //뒤로가기방지
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.show();
    }
}