package org.techtown.study01.FirstToMain.register;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.style.RelativeSizeSpan;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.login_fitstPage.Login;

import static android.text.TextUtils.isEmpty;

public class Register extends AppCompatActivity {

    private Button btnBack, join_btn, check_id_btn;
    private Boolean validate = false;
    private AlertDialog dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);


        //돌아가기 버튼
        btnBack = (Button) findViewById(R.id.cancel_btn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });

        //입력받을 EditText 선언
        final EditText Eid = (EditText) findViewById(R.id.id);
        final EditText Epw = (EditText) findViewById(R.id.pw);
        final EditText Epwc = (EditText) findViewById(R.id.pwc);
        final EditText Ename = (EditText) findViewById(R.id.name);
        final EditText Eemail = (EditText) findViewById(R.id.email);
        final EditText Ephone1 = (EditText) findViewById(R.id.phone1);
        final EditText Ephone2 = (EditText) findViewById(R.id.phone2);
        final EditText Ephone3 = (EditText) findViewById(R.id.phone3);
        final EditText Esmsnumber = (EditText) findViewById(R.id.smsnumber);

        //id중복체크 버튼
        check_id_btn = (Button) findViewById(R.id.check_id_btn);
        check_id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id = Eid.getText().toString();
                if (validate) {
                    return;
                }
                if (id.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                    dialog = builder.setMessage("아이디를 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }

                Response.Listener<String> responseListener=new Response.Listener<String>() { //결과 리스너 생성 (중복체크)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( Register.this );
                                dialog=builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                Eid.setEnabled(false);
                                validate=true;
                                check_id_btn.setText("확인");
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( Register.this );
                                dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                Idcheck idcheck = new Idcheck(id, responseListener);
                RequestQueue queue= Volley.newRequestQueue(Register.this);
                queue.add(idcheck);

            }
        });


        //회원가입 버튼
        join_btn = (Button) findViewById(R.id.join_btn);
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //EditText에서 입력받은값 변수에 저장
                String id = Eid.getText().toString();
                String pw = Epw.getText().toString();
                String pwc = Epwc.getText().toString();
                String name = Ename.getText().toString();
                String email = Eemail.getText().toString();
                String phone = Ephone1.getText().toString() + Ephone2.getText().toString() + Ephone3.getText().toString();
                String smsnumber = Esmsnumber.getText().toString();


                //결과 리스너 생성
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                //등록후 응답받은 값이 true이면 성공 다이얼로그 출력
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setMessage("회원 등록에 성공했습니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                finish();
                                            }
                                        })
                                        .create()
                                        .show();
                            } else {
                                //등록 실패 했을때 실패 다이얼로그 출력
                                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                builder.setMessage("회원 등록에 실패했습니다.")
                                        .setPositiveButton("다시 시도", null)
                                        .create()
                                        .show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                //각 정보를 입력안했을때는 Toast메세지 출력 후 리턴
                if (isEmpty(id)) {
                    Toast.makeText(getApplicationContext(), "아이디를 입력해주새요.", Toast.LENGTH_LONG).show();
                    return;
                } else if (isEmpty(pw)) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                } else if (isEmpty(pwc)) {
                    Toast.makeText(getApplicationContext(), "비밀번호를 확인하세요", Toast.LENGTH_LONG).show();
                    return;
                } else if (!pw.equals(pwc)) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                    return;
                } else if (isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                } else if (isEmpty(phone)) {
                    Toast.makeText(getApplicationContext(), "휴대폰 번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                    return;
                } else if (isEmpty(smsnumber)) {
                    Toast.makeText(getApplicationContext(), "휴대폰 인증을 완료해주세요.", Toast.LENGTH_LONG).show();
                    return;
                } else {
                    //모든 값이 다 있으면 DB에 저장하는 메소드 실행
                    RegisterRequest register = new RegisterRequest(id, pw, name, email, phone, responseListener);
                    RequestQueue queue = Volley.newRequestQueue(Register.this);
                    queue.add(register);
                }
            }
        });

    }
}