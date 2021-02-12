package org.techtown.study01.FirstToMain.findid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.findPW.ChangePw;
import org.techtown.study01.FirstToMain.findPW.FindPw;
import org.techtown.study01.FirstToMain.register.Idcheck;

public class Id_pw_complete extends AppCompatActivity {
    EditText pw, pwcheck;
    String newpw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_pw_complete);

        pw = findViewById(R.id.pw);
        pwcheck = findViewById(R.id.pwcheck);

        Intent intent = getIntent();
        String Eid  = intent.getStringExtra("id");

        String pw1 = pw.getText().toString();
        String pw2 = pwcheck.getText().toString();

        Button button = findViewById(R.id.button3);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(pw1.equals(pw2)) {
                    newpw = pw.getText().toString();
                    Log.d("아이디", newpw);

                    Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_SHORT).show();
                }

                    Response.Listener<String> responseListener = new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                boolean success = jsonObject.getBoolean("success");

                                if (success) {

                                    Toast.makeText(getApplicationContext(), "비밀번호 변경완료", Toast.LENGTH_SHORT).show();
                                } else {//로그인 실패시
                                    Toast.makeText(getApplicationContext(), "이메일을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                    return;
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                                Toast.makeText(getApplicationContext(), "이메일 오류 1, 문의부탁드립니다.", Toast.LENGTH_SHORT).show();
                                return;
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    };

                    ChangePw changePw = new ChangePw(Eid, newpw, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Id_pw_complete.this);
                    queue.add(changePw);
            }
        });
    }
}
