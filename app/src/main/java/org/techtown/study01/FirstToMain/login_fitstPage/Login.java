//로그인 화면
package org.techtown.study01.FirstToMain.login_fitstPage;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.findPW.FindPw;
import org.techtown.study01.FirstToMain.findid.FindId;
import org.techtown.study01.FirstToMain.homeMain.BottomNavi;
import org.techtown.study01.FirstToMain.register.Register_Auth;
import org.techtown.study01.FirstToMain.start.Quest1;

public class Login extends AppCompatActivity {
    private static final String TAG = "Login"; //로그 찍을때,


    private EditText idText, passwordText; //아이디 비밀번호 입력
    private Button btn_login;//로그인버튼
    private TextView rg_sign, btn_findId, btn_findPw, btn_google;
    ; //회원가입버튼, 아이디 비번찾기 버튼
    private String loginId, loginPwd, loginName;//자동 로그인용
    private String Eid, Epw, Ename; //자동로그인 식별용


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fistpage);

        idText = findViewById(R.id.idText);
        passwordText = findViewById(R.id.passwordText);

        //회원가입 버튼
        rg_sign = findViewById(R.id.rg_sign);
        rg_sign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Login.this, Register_Auth.class); //개인정보이용동의로 이동
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });


        btn_login = findViewById(R.id.btn_login);

        //자동 로그인
        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);

        //처음에는 SharedPreferences에 아무런 정보도 없으므로 값을 저장할 키들을 생성한다.
        // getString의 첫 번째 인자는 저장될 키, 두 번쨰 인자는 값입니다.
        // 첨엔 값이 없으므로 키값은 원하는 것으로 하시고 값을 null을 줍니다.
        loginId = auto.getString("inputId", null);
        loginPwd = auto.getString("inputPwd", null);
        loginName = auto.getString("inputName", null);

//            if(loginId != null && loginPwd != null) { // loginId와 loginPwd에 값이 있으면, 자동 로그인을 실시 한다.
//                if (loginId.length() > 0 && loginPwd.length() > 0) {
//                    Toast.makeText(Login.this, loginName + "님 환영합니다.", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(Login.this, BottomNavi.class);
//                    startActivity(intent);
//                    finish();
//                }
//            }


        if (loginId == null && loginPwd == null) { //값이 없으면(초기 상태) 로그인 성공시 값을 넣어준다.
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String id = idText.getText().toString();
                    String pw = passwordText.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");

                                if (success) {//로그인 성공시

                                    Eid = jsonObject.getString("id");
                                    Epw = jsonObject.getString("pw");
                                    Ename = jsonObject.getString("name");

                                    if (id.equals(Eid) && pw.equals(Epw)) {
                                        SharedPreferences auto = getSharedPreferences("auto", Activity.MODE_PRIVATE);
                                        SharedPreferences.Editor autoLogin = auto.edit();
                                        autoLogin.putString("inputId", Eid);
                                        autoLogin.putString("inputPwd", Epw);
                                        autoLogin.putString("inputName", Ename);
                                        autoLogin.commit(); //커밋을 해야지 값이 저장된다.

                                        Log.d(TAG, String.valueOf(autoLogin));

                                        Toast.makeText(getApplicationContext(), Ename + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(Login.this, BottomNavi.class);

                                        /**여기는 첫 입장한 사람이 quest1로 이동해야 하기 때문에 이 방법 알면 Bottom으로 가는 길 quest로 가는 길을 알아낸다. */
//                                        Intent intent = new Intent(Login.this, Quest1.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //똑같은 액티비티가 중첩되지 않게 해준다.
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 이전에 사용하던 액티비티를 종료한다.
                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // 그동안 쌓여있던 액티비티를 전부 종료해준다.
                                        intent.putExtra("id", Eid);
                                        intent.putExtra("pw", Epw);
                                        intent.putExtra("name", Ename);

                                        Log.d(TAG, "기본 로그인 정보 인텐트로 넘기기");

                                        startActivity(intent);
                                        finish();

                                        Log.d(TAG, "자동 로그인 값 저장");
                                    }

                                } else {//로그인 실패시
                                    Toast.makeText(getApplicationContext(), "아이디/비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "아이디/비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(id, pw, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    queue.add(loginRequest);

                }
            });
        }


        btn_findId = (TextView) findViewById(R.id.tv_id);
        btn_findPw = (TextView) findViewById(R.id.tv_pass);

        //아이디 찾기 화면으로 이동
        btn_findId.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, FindId.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
            }
        });

        //비밀 번호 찾기 화면으로 이동
        btn_findPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(Login.this, FindPw.class);
                intent2.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent2);
            }
        });


    }
}