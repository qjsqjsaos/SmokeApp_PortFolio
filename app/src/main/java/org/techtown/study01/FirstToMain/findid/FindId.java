package org.techtown.study01.FirstToMain.findid;

import android.os.Bundle;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;

import android.widget.EditText;
import android.widget.Toast;


import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;


import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.register.GMailSender;

public class FindId extends AppCompatActivity {

    private AlertDialog dialog;
    private EditText editText;
    private Button button;
    private String Eid; //찾은 아이디 넣을 스트링 객체

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);

       editText = findViewById(R.id.editTextEmail);
       button = findViewById(R.id.sendForID);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText.getText().toString(); //입력한 이메일 가져오기

                AlertDialog.Builder builder = new AlertDialog.Builder(FindId.this);

                try {
                    //입력칸이 빈칸일때
                    if (editText.equals("")) {
                        dialog = builder.setMessage("이메일을 입력해주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                    //이메일 유효성
                    if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        dialog = builder.setMessage("이메일 형식을 지켜주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "잘못된 값입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();

                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {//로그인 성공시
                                Eid = jsonObject.getString("id"); //찾는 아이디 값 가져오고



                            } else {//로그인 실패시
                                Toast.makeText(getApplicationContext(), "이메일을 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                };

                FindId_Check findIdCheck = new FindId_Check(email, responseListener);
                RequestQueue queue = Volley.newRequestQueue(FindId.this);
                queue.add(findIdCheck);
            }
        });
    }



}