package org.techtown.GmanService.FirstToMain.findPW;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.DisplayMetrics;
import android.util.Patterns;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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
import org.techtown.GmanService.FirstToMain.findid.FindId;
import org.techtown.GmanService.FirstToMain.findid.IdEmailCheck_for_pw;
import org.techtown.GmanService.FirstToMain.homeMain.Loading_Dialog;
import org.techtown.GmanService.FirstToMain.register.NaverSender;

import java.util.regex.Pattern;

public class FindPw extends AppCompatActivity {


    private AlertDialog dialog;
    private EditText idForPwText, emailForPwText;
    private Button button, GoId;
    private String Eid, email, id; //찾은 비밀번호 넣을 스트링 객체
    private int result; //랜덤번호

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
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pw);

        // 애드 몹 초기화 //시작
        MobileAds.initialize(this, initializationStatus -> { });

        adContainerView = findViewById(R.id.ad_view_container15);
        adView = new AdView(this);
        adView.setAdUnitId(getString(R.string.admob__unit_TTIBanner));
        adContainerView.addView(adView);
        loadBanner();
        //끝

        idForPwText = findViewById(R.id.IdforPw22); //아이디 입력칸
        emailForPwText = findViewById(R.id.AuthTextEmail45); //이메일 입력칸
        GoId = findViewById(R.id.goId); //아이디찾기로 이동

        GoId.setOnClickListener(new View.OnClickListener() { //아이디 찾기로
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FindPw.this, FindId.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });

        button = findViewById(R.id.sendForPw22); //이메일보내기 버튼
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingStart();//로딩창 띄우기
                id = idForPwText.getText().toString(); //입력한 아이디 가져오기
                email = emailForPwText.getText().toString(); //입력한 이메일 가져오기
                AlertDialog.Builder builder = new AlertDialog.Builder(FindPw.this);

                //입력칸이 빈칸일때
                if (id.equals("") || email.equals("")) {
                    dialog = builder.setMessage("모두 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    loading_dialog.cancel(); //로딩창 닫기
                    return;
                } else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    dialog = builder.setMessage("이메일 형식을 지켜주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    loading_dialog.cancel(); //로딩창 닫기
                    return;
                } else if (!Pattern.matches("^[0-9_a-zA-Z]{4,20}$", id)) {
                    dialog = builder.setMessage("아이디 형식을 지켜주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    loading_dialog.cancel(); //로딩창 닫기
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                //지메일 실행
                                AuthSendStart(email);
                                //비밀번호 변경하는 AuthforPw거쳐서 ChangePw로 보내기
                                String Eid = jsonObject.getString("id"); //아이디 꺼내기
                                String Epw = jsonObject.getString("pw"); //비밀번호 꺼내기

                                Intent intent = new Intent(FindPw.this, AuthforPw.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                intent.putExtra("id", Eid); //데이터베이스에서 가져온 id값
                                intent.putExtra("pw", Epw); //데이터베이스에서 가져온 pw값
                                intent.putExtra("authNumber", result);
                                startActivity(intent);
                                finish();

                                loading_dialog.cancel(); //로딩창 닫기

                            } else {//로그인 실패시
                                loading_dialog.cancel(); //로딩창 닫기
                                Toast.makeText(getApplicationContext(), "아이디/이메일을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading_dialog.cancel(); //로딩창 닫기
                            Toast.makeText(getApplicationContext(), "이메일 오류 1, 문의부탁드립니다.", Toast.LENGTH_SHORT).show();
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            loading_dialog.cancel(); //로딩창 닫기
                            Toast.makeText(getApplicationContext(), "이메일 오류 1, 문의부탁드립니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                };

                IdEmailCheck_for_pw idEmailCheckForpw = new IdEmailCheck_for_pw(id, email, responseListener);
                RequestQueue queue = Volley.newRequestQueue(FindPw.this);
                queue.add(idEmailCheckForpw);
            }
        });
    }

    public void AuthSendStart(String email) { //인증번호를 위한 보내기

        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());


            //랜덤 인증번호 (6자)
            result = (int) (Math.floor(Math.random() * 1000000) + 100000);
            if (result > 1000000) {
                result = result - 100000;
            }

        //네이버 메일 smtp보내기
        NaverSender naverSender = new NaverSender();
        //naverSender.NaverSender(제목, 본문내용, 받는 사람);
        try {
            naverSender.NaverSender("금연 솔루션 플랫폼 '그만'입니다. 인증번호를 확인해주세요.",
                    "인증번호는 : \"" + result + "\" 입니다. \n " +
                            "인증번호를 입력하시고 확인버튼을 누르시면 비밀번호를 변경하실 수 있습니다.", email);
            Toast.makeText(getApplicationContext(), "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "비밀번호 찾기 오류, 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
            return;
        }

    }

    /**로딩창 띄우기 */
    public void loadingStart(){
        loading_dialog = new Loading_Dialog(FindPw.this);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false); //뒤로가기방지
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.show();
    }
}
