package org.techtown.study01.FirstToMain.findid;

import android.content.Intent;
import android.os.Bundle;
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
import org.techtown.study01.FirstToMain.login_fitstPage.Login;
import org.techtown.study01.FirstToMain.register.Idcheck;
import org.techtown.study01.FirstToMain.register.Register;

import java.util.regex.Pattern;

public class FindId extends AppCompatActivity {

    Button sendId_btn;
    EditText editText_email;
    AlertDialog dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);

        sendId_btn = findViewById(R.id.sendId);
        editText_email = findViewById(R.id.editTextEmail);

        //id중복체크 버튼

        sendId_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = editText_email.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(FindId.this);

                try {
                    //입력칸이 빈칸일때
                    if (email.equals("")) {
                        dialog = builder.setMessage("이메일을 입력해주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "오류입니다. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();

                }


                Response.Listener<String> responseListener = new Response.Listener<String>() { //결과 리스너 생성 (중복체크)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            AlertDialog.Builder builder = new AlertDialog.Builder(FindId.this);
                            if (success) {
                                Intent intent = new Intent(FindId.this, Id_pw_complete.class);
                                startActivity(intent);
                                finish();
                            } else {
                                dialog = builder.setMessage("이메일을 다시 확인해주세요.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                FindId_Check findId_check = new FindId_Check(id, email, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(idcheck);
            }
        });
    }


}