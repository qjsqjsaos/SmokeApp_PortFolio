package org.techtown.study01.FirstToMain.findPW;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.findid.FindId;
import org.techtown.study01.FirstToMain.findid.Id_pw_complete;
import org.techtown.study01.FirstToMain.homeMain.Loading_Dialog;
import org.techtown.study01.FirstToMain.register.Register;

import java.util.regex.Pattern;

public class ChangePw extends AppCompatActivity {

    private EditText pw, pwcheck;
    private String newpw, pw2;
    private String messagepPw = "비밀번호찾기"; //아이디찾기인지 비밀번호 찾기인지 식별하기 위한 메세지


    //로딩창 띄우기
    private Loading_Dialog loading_dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_pw);

        pw = findViewById(R.id.IdforPw); //비밀번호
        pwcheck = findViewById(R.id.AuthTextEmail33); //비밀번호 확인



        Button button = findViewById(R.id.sendForPw);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingStart();//로딩창 띄우기
                //아이디 비번 인텐트로 가져오기
                Intent intent = getIntent();
                String Eid = intent.getStringExtra("id");
                String Epw = intent.getStringExtra("pw");

                newpw = pw.getText().toString();
                pw2 = pwcheck.getText().toString();

                if(!newpw.equals(pw2)) { //비밀번호와 비밀번호 확인이 맞지 않을때,
                    loading_dialog.cancel(); //로딩창 닫기
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(newpw.equals("") || pw2.equals("")){ //둘 중 하나라도 빈칸이면,
                    loading_dialog.cancel(); //로딩창 닫기
                    Toast.makeText(getApplicationContext(), "비밀번호를 전부 입력해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if(Epw.equals(newpw) || Epw.equals(pw2)) {//전에 쓰던 비밀번호와 같을때,
                    loading_dialog.cancel(); //로딩창 닫기
                    Toast.makeText(getApplicationContext(), "전에 쓰던 비밀번호는 사용하실 수 없습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,20}.$", newpw)) { //비밀번호 형식 안지킬시
                    loading_dialog.cancel(); //로딩창 닫기
                    Toast.makeText(getApplicationContext(), "영문+숫자+특수문자 각 1자 이상 포함 8~20자 글자여야 합니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {

                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                Toast.makeText(getApplicationContext(), "비밀번호가 변경되었습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent1 = new Intent(ChangePw.this, Id_pw_complete.class);
                                intent1.putExtra("findPw", messagepPw);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                intent1.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                                startActivity(intent1);
                                finish();
                                loading_dialog.cancel(); //로딩창 닫기
                            } else {//로그인 실패시
                                loading_dialog.cancel(); //로딩창 닫기
                                Toast.makeText(getApplicationContext(), "비밀번호를 다시 확인해주세요.", Toast.LENGTH_SHORT).show();
                                return;
                            }


                        } catch (JSONException e) {
                            loading_dialog.cancel(); //로딩창 닫기
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "비밀번호 오류1, 문의부탁드립니다.", Toast.LENGTH_SHORT).show();
                            return;
                        } catch (Exception e) {
                            e.printStackTrace();
                            loading_dialog.cancel(); //로딩창 닫기
                            Toast.makeText(getApplicationContext(), "비밀번호 오류2, 문의부탁드립니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                };

                ChangePwRequest changePwRequest = new ChangePwRequest(Eid, newpw, responseListener);
                RequestQueue queue = Volley.newRequestQueue(ChangePw.this);
                queue.add(changePwRequest);
            }
        });
    }

    /**로딩창 띄우기 */
    public void loadingStart(){
        loading_dialog = new Loading_Dialog(ChangePw.this);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false); //뒤로가기방지
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.show();
    }
}