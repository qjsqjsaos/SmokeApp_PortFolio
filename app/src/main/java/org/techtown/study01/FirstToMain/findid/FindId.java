package org.techtown.study01.FirstToMain.findid;


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
import org.techtown.study01.FirstToMain.register.GMailSender;

public class FindId extends AppCompatActivity {


    private AlertDialog dialog;
    private EditText editText;
    private Button button;
    private String Eid, email, Eemail; //찾은 아이디 넣을 스트링 객체
    public String message = "아이디찾기"; //아이디찾기인지 비밀번호 찾기인지 식별하기 위한 메세지


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.find_id);

        editText = findViewById(R.id.editTextEmail);
        button = findViewById(R.id.sendForID);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

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
                   if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                        dialog = builder.setMessage("이메일 형식을 지켜주세요.")
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
                                Eemail = jsonObject.getString("email");
                                Eid = jsonObject.getString("id"); //찾는 아이디 값 가져오고
                                if(email.equals(Eemail)){
                                    idSendStart();
                                    Intent intent = new Intent(FindId.this, Id_pw_complete.class);
                                    intent.putExtra("findId", message); //식별 메세지
                                    intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                    startActivity(intent);
                                    finish();
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

                FindId_Check findId_check = new FindId_Check(email, responseListener);
                RequestQueue queue = Volley.newRequestQueue(FindId.this);
                queue.add(findId_check);
            }
        });
    }


    public void idSendStart()  {
        //구글 이메일로 smtp 사용해서 인증번호 보내기
        GMailSender gMailSender = new GMailSender("merrygoaround0726@gmail.com", "asdf4694!@");
                //GMailSender.sendMail(제목, 본문내용, 받는사람);

        try {
            gMailSender.sendMail("금연 솔루션 플랫폼 '그만'입니다. 아이디를 확인해주세요.", "아이디는 \"" + replaceStar(Eid,5) + "\" 입니다. \n " +
                    "아이디가 맞는지 확인해 주시고, 궁금하신 점은 'merrygoaround0726@gmail.com' 이메일로 문의주시기 바랍니다. 감사합니다. ", email);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "이메일로 아이디가 전송되었습니다.", Toast.LENGTH_SHORT).show();

            }

    public static String replaceStar(String str, int len){ //아이디 별표시해서 출력
        String returnStr="";
        for(int i=0; i<str.length();i++){
            if(i<len)returnStr=returnStr+str.substring(i,i+1);
            else returnStr=returnStr+"*";
        }
        return returnStr;
    }
}