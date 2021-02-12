package org.techtown.study01.FirstToMain.findPW;

import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
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
import org.techtown.study01.FirstToMain.findid.FindId;
import org.techtown.study01.FirstToMain.findid.FindId_Pw_Check;
import org.techtown.study01.FirstToMain.findid.Id_pw_complete;
import org.techtown.study01.FirstToMain.findid.TestIdcheck;
import org.techtown.study01.FirstToMain.register.GMailSender;
import org.techtown.study01.FirstToMain.register.Idcheck;

public class FindPw extends AppCompatActivity {


    private AlertDialog dialog;
    private EditText editText;
    private Button button;
    private String Eid, email, id; //찾은 비밀번호 넣을 스트링 객체


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_pw);

        editText = findViewById(R.id.editTextEmailPw);
        button = findViewById(R.id.sendForPw);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                id = editText.getText().toString(); //입력한 이메일 가져오기

                AlertDialog.Builder builder = new AlertDialog.Builder(FindPw.this);


                //입력칸이 빈칸일때
                if (id.equals("")) {
                    dialog = builder.setMessage("이메일을 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                String Eid = jsonObject.getString("id");

                                    Intent intent = new Intent(FindPw.this, Id_pw_complete.class);
                                    intent.putExtra("id", Eid);
                                    startActivity(intent);
                                    finish();

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

                TestIdcheck idcheck = new TestIdcheck(id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(FindPw.this);
                queue.add(idcheck);
            }
        });
    }
    // TODO: 2021-02-12 이거 해결하기


}
