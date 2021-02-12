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
    private String Eid, email, Eemail; //찾은 아이디 넣을 스트링 객체
    private Boolean emailSuccess, emailok = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);

        editText = findViewById(R.id.editTextEmail);
        button = findViewById(R.id.sendForID);


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = editText.getText().toString(); //입력한 이메일 가져오기

                AlertDialog.Builder builder = new AlertDialog.Builder(FindId.this);


                    //입력칸이 빈칸일때
                    if (email.equals("")) {
                        dialog = builder.setMessage("이메일을 입력해주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }
                    //이메일 유효성
                   else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        dialog = builder.setMessage("이메일 형식을 지켜주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    } else{
                        emailSuccess = true;
                    }

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                Eemail = jsonObject.getString("email"); //찾는 아이디 값 가져오고
                                Eid = jsonObject.getString("id"); //찾는 아이디 값 가져오고
                                if(emailSuccess == true){
                                idSendStart();
                                    emailok = true;
                                }
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

                FindId_Check findIdCheck = new FindId_Check(email, responseListener);
                RequestQueue queue = Volley.newRequestQueue(FindId.this);
                queue.add(findIdCheck);
            }
        });
    }
    // TODO: 2021-02-12 이거 해결하기


    public void idSendStart() throws Exception {
        //구글 이메일로 smtp 사용해서 인증번호 보내기
        GMailSender gMailSender = new GMailSender("merrygoaround0726@gmail.com", "asdf4694");
                //GMailSender.sendMail(제목, 본문내용, 받는사람);

                    gMailSender.sendMail("금연 솔루션 플랫폼 '그만'입니다. 아이디를 확인해주세요.", "아이디는 \"" + Eid + "\" 입니다. \n " +
                            "아이디가 맞는지 확인해 주시고, 전체 아이디가 기억나시지 않으시면, 'merrygoaround0726@gmail.com' 이메일로 문의주시기 바랍니다. 감사합니다. ", email);
                    Toast.makeText(getApplicationContext(), "이메일로 아이디가 전송되었습니다.", Toast.LENGTH_SHORT).show();

            }
}