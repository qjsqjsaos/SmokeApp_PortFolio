//로그인 화면
package org.techtown.study01.FirstToMain.login_fitstPage;

import android.content.Intent;
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
import org.techtown.study01.FirstToMain.register.Register;


public class Login extends AppCompatActivity {


        private EditText idText, passwordText; //아이디 비밀번호 입력
        private Button btn_login;//로그인버튼
        private TextView rg_sign, btn_findId, btn_findPw; ; //회원가입버튼

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.login_fistpage);

            idText = findViewById(R.id.idText);
            passwordText = findViewById(R.id.passwordText);

            rg_sign = findViewById(R.id.rg_sign);
            rg_sign.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(Login.this, Register.class);
                    startActivity(intent);
                }
            });


            btn_login = findViewById(R.id.btn_login);
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

                                    String id = jsonObject.getString("id");
                                    String pw = jsonObject.getString("pw");
                                    String name = jsonObject.getString("name");


                                    Toast.makeText(getApplicationContext(), name + "님 환영합니다.", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(Login.this, BottomNavi.class);

                                    intent.putExtra("id", id);
                                    intent.putExtra("pw", pw);

                                    startActivity(intent);

                                } else {//로그인 실패시
                                    Toast.makeText(getApplicationContext(), "아이디/비밀번호를 확인해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(id, pw, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Login.this);
                    queue.add(loginRequest);

                }
            });

            btn_findId = (TextView) findViewById(R.id.tv_id);
            btn_findPw = (TextView) findViewById(R.id.tv_pass);

            //아이디 찾기 화면으로 이동
            btn_findId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(Login.this, FindId.class);
                    startActivity(intent);
                }
            });

            //비밀 번호 찾기 화면으로 이동
            btn_findPw.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent2 = new Intent(Login.this, FindPw.class);
                    startActivity(intent2);
                }
            });
        }
    }