//로그인 화면
package org.techtown.study01.FirstToMain.login_fitstPage;

import android.content.Intent;
import android.os.Bundle;
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
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.register.Register;


public class Login extends AppCompatActivity {

    public class LoginActivity extends AppCompatActivity {

        private EditText idText, passwordText; //아이디 비밀번호 입력
        private Button btn_login; //로그인버튼
        private TextView rg_sign; //회원가입버튼

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
                    Intent intent = new Intent(LoginActivity.this, Register.class);
                    startActivity(intent);
                }
            });


            btn_login = findViewById(R.id.btn_login);
            btn_login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String userID = idText.getText().toString();
                    String userPass = passwordText.getText().toString();

                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");

                                if (success) {//로그인 성공시

                                    String userID = jsonObject.getString("userID");
                                    String userPass = jsonObject.getString("userPassword");
                                    String userBirthday = jsonObject.getString("userBirthday");
                                    String userNickname = jsonObject.getString("userNickname");
                                    String userPhone = jsonObject.getString("userPhone");


                                    Toast.makeText(getApplicationContext(), "로그인 성공", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this, HomeMain.class);

                                    intent.putExtra("userID", userID);
                                    intent.putExtra("userPass", userPass);
                                    intent.putExtra("userNickname", userNickname);
                                    intent.putExtra("userPhone", userPhone);
                                    intent.putExtra("userBirthday", userBirthday);

                                    startActivity(intent);

                                } else {//로그인 실패시
                                    Toast.makeText(getApplicationContext(), "로그인 실패", Toast.LENGTH_SHORT).show();
                                    return;
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                    queue.add(loginRequest);

                }
            });
        }
    }
}
