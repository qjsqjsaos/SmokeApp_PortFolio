package org.techtown.study01.FirstToMain.register;


import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;

import android.os.SystemClock;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.TextView;
import android.widget.Toast;


import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.findid.FindId;
import org.techtown.study01.FirstToMain.homeMain.BottomNavi;
import org.techtown.study01.FirstToMain.homeMain.Loading_Dialog;
import org.techtown.study01.FirstToMain.login_fitstPage.Login;
import org.techtown.study01.FirstToMain.start.Quest1;

import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

public class Register extends AppCompatActivity {
    private Button btnBack, join_btn, check_id_btn;
    private Boolean validate, checkNumberSmtp, timeLimit, nameCheck = false; //중복체크 되었는지 안되었는지 확인, 인증 번호 확인, 타이머 인증 확인, 닉네임 중복체크
    private AlertDialog dialog; //알림 다이아로그
    private Button sendEmail, email_btn, sendName = null; //버튼
    private EditText smsNumber = null; //받는 사람의 이메일
    private int result, keyNumber;  //이메일 인증번호, 입력한 인증번호
    private CountDownTimer countDownTimer; //카운트 다운 타이머
    private TextView countView; //카운트 다운 표시 텍스트
    private final int MILLISINFUTURE = 300 * 1000; //총 시간 (300초 = 5분)
    private final int COUNT_DOWN_INTERVAL = 1000; //onTick 메소드를 호출할 간격 (1초)
    private Long mLastClickTime = 0L; //이메일 버튼 클릭 방지 변수

    String profileimage = "0";
    String datetime = "0";
    long cigacount = 0;
    double cigapay = 0;
    String goal = "입력해주세요!"; //이 값들은 후에 Quest1에서 넣어줄 값이다.


    //로딩창 띄우기
    private Loading_Dialog loading_dialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);

        countView = (TextView) findViewById(R.id.countView);

        //입력받을 EditText 선언
        final EditText Eid = (EditText) findViewById(R.id.id);
        final EditText Epw = (EditText) findViewById(R.id.pw);
        final EditText Epwc = (EditText) findViewById(R.id.pwc);
        final EditText Ename = (EditText) findViewById(R.id.name);
        final EditText Eemail = (EditText) findViewById(R.id.email);
        final EditText Esmsnumber = (EditText) findViewById(R.id.smsNumber);


        //id중복체크 버튼
        check_id_btn = (Button) findViewById(R.id.check_id_btn);
        check_id_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadingStart(); //로딩창띄우기
                String id = Eid.getText().toString();
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);


                try {
                    //입력칸이 빈칸일때
                    if (id.equals("")) {
                        dialog = builder.setMessage("아이디를 입력해주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        loading_dialog.cancel(); //로딩창 닫기
                        return;
                    }
                    //아이디 유효성
                    else if (!Pattern.matches("^[0-9_a-zA-Z]{4,20}$", id)) {
                        dialog = builder.setMessage("아이디 형식을 지켜주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        loading_dialog.cancel(); //로딩창 닫기
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loading_dialog.cancel(); //로딩창 닫기
                    Toast.makeText(getApplicationContext(), "잘못된 값입니다1.문의 부탁드립니다.", Toast.LENGTH_SHORT).show();

                }


                Response.Listener<String> responseListener = new Response.Listener<String>() { //결과 리스너 생성 (중복체크)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                            if (success) {
                                validate = true;
                                dialog = builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                loading_dialog.cancel(); //로딩창 닫기
                            } else {
                                validate = false;
                                dialog = builder.setMessage("존재하는 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                loading_dialog.cancel(); //로딩창 닫기
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading_dialog.cancel(); //로딩창 닫기
                        }
                    }
                };
                Idcheck idcheck = new Idcheck(id, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(idcheck);
            }
        });


        //닉네임 중복확인 버튼

        sendName = findViewById(R.id.sendName);
        sendName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingStart(); //로딩창띄우기
                String dbname = Ename.getText().toString(); // mysql에서 이름 가져오기
                AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);

                try {
                    //입력칸이 빈칸일때
                    if (dbname.equals("")) {
                        dialog = builder.setMessage("닉네임을 입력해주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        loading_dialog.cancel(); //로딩창 닫기
                        return;
                    }
                    //닉네임 유효성
                    if (!Pattern.matches("^[가-힣a-zA-Z0-9_]{2,10}$", dbname)) {
                        dialog = builder.setMessage("닉네임 형식을 지켜주세요.")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        loading_dialog.cancel(); //로딩창 닫기
                        return;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    loading_dialog.cancel(); //로딩창 닫기
                    Toast.makeText(getApplicationContext(), "잘못된 값입니다2. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                    return;
                }


                Response.Listener<String> responseListener = new Response.Listener<String>() { //결과 리스너 생성 (중복체크)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                            if (success) {
                                nameCheck = true;
                                dialog = builder.setMessage("사용할 수 있는 닉네임입니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                loading_dialog.cancel(); //로딩창 닫기
                            } else {
                                nameCheck = false;
                                dialog = builder.setMessage("존재하는 닉네임입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                loading_dialog.cancel(); //로딩창 닫기
                                return;
                            }

                        } catch (JSONException e) {
                            loading_dialog.cancel(); //로딩창 닫기
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "닉네임 오류, 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    }
                };
                NameRequest nameRequest = new NameRequest(dbname, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(nameRequest);
            }
        });


        //이메일 인증번호 보내기
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        sendEmail = findViewById(R.id.sendEmail);
        sendEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingStart(); //로딩창 띄우기
                String dbEmail = Eemail.getText().toString(); //이메일 중복체크용
                Response.Listener<String> responseListener = new Response.Listener<String>() { //결과 리스너 생성 (이메일 중복체크)
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);

                            boolean success = jsonResponse.getBoolean("success");

                            AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                            if (success) { // 만약 중복이 아니면
                                loading_dialog.cancel(); //로딩창 닫기
                                if (SystemClock.elapsedRealtime() - mLastClickTime > 300000) { //5분 동안 타이머
                                    switch (v.getId()) {
                                        case R.id.sendEmail:


                                            try {
                                                //랜덤 인증번호 (6자)
                                                result = (int) (Math.floor(Math.random() * 1000000) + 100000);
                                                if (result > 1000000) {
                                                    result = result - 100000;
                                                }


                                                try {
                                                    if (!dbEmail.equals("")) {
                                                                startLoading(dbEmail);  //지메일 호출되게하는 메서드



                                                        //타이머 설정
                                                        countView = (TextView) findViewById(R.id.countView);
                                                        //줄어드는 시간을 나타내는 TextView
                                                        smsNumber = (EditText) findViewById(R.id.smsNumber);
                                                        //사용자 인증 번호 입력창
                                                        email_btn = (Button) findViewById(R.id.email_btn);
                                                        //인증하기 버튼

                                                        countDownTimer = new CountDownTimer(MILLISINFUTURE, COUNT_DOWN_INTERVAL) {
                                                            @Override
                                                            public void onTick(long millisUntilFinished) { //(300초에서 1초 마다 계속 줄어듬)
                                                                timeLimit = true;
                                                                long emailAuthCount = millisUntilFinished / 1000;
                                                                Log.d("Alex", emailAuthCount + "");

                                                                if ((emailAuthCount - ((emailAuthCount / 60) * 60)) >= 10) { //초가 10보다 크면 그냥 출력
                                                                    countView.setText((emailAuthCount / 60) + " : " + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                                                                } else { //초가 10보다 작으면 앞에 '0' 붙여서 같이 출력. ex) 02,03,04...
                                                                    countView.setText((emailAuthCount / 60) + " : 0" + (emailAuthCount - ((emailAuthCount / 60) * 60)));
                                                                }

                                                                //emailAuthCount은 종료까지 남은 시간임. 1분 = 60초 되므로,
                                                                // 분을 나타내기 위해서는 종료까지 남은 총 시간에 60을 나눠주면 그 몫이 분이 된다.
                                                                // 분을 제외하고 남은 초를 나타내기 위해서는, (총 남은 시간 - (분*60) = 남은 초) 로 하면 된다.
                                                                mLastClickTime = SystemClock.elapsedRealtime(); //이메일 두번 클릭 방지(5분 쿨타임)
                                                            }

                                                            @Override
                                                            public void onFinish() { //시간이 초과 되서 꺼지면 false, 인증되고 꺼지면 true.
                                                                timeLimit = false;
                                                                countView.setText("시간초과 : 다시시도");
                                                                return;
                                                            }

                                                        }.start();

                                                    }else{
                                                        loading_dialog.cancel(); //로딩창 닫기
                                                    Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                    }
                                                } catch (Exception e) {
                                                    loading_dialog.cancel(); //로딩창 닫기
                                                    e.printStackTrace();
                                                    Toast.makeText(getApplicationContext(), "다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                                                    return;
                                                }
                                            } catch (Exception e) {
                                                e.printStackTrace();
                                                loading_dialog.cancel(); //로딩창 닫기
                                                Toast.makeText(getApplicationContext(), "잘못된 값입니다3. 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                                                return;
                                            }

                                    }
                                }


                            } else {
                                dialog = builder.setMessage("존재하는 이메일입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                loading_dialog.cancel(); //로딩창 닫기
                                return;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            loading_dialog.cancel(); //로딩창 닫기
                            Toast.makeText(getApplicationContext(), "이메일 중복 오류, 문의 바랍니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                EmailRequest emailRequest = new EmailRequest(dbEmail, responseListener);
                RequestQueue queue = Volley.newRequestQueue(Register.this);
                queue.add(emailRequest);

            }

        });



                //이메일 인증버튼 눌렀을 때 반응

                smsNumber = (EditText) findViewById(R.id.smsNumber);
                email_btn = (Button) findViewById(R.id.email_btn);
                email_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                        try {
                            keyNumber = Integer.parseInt(smsNumber.getText().toString());
                            if (smsNumber.length() == 0) {
                                dialog = builder.setMessage("인증번호를 입력해주세요.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                return;
                            } else if (result == keyNumber) {
                                if(timeLimit == false) { //만약 제한 시간이 지나고 인증번호 확인을 눌렀을 때,
                                dialog = builder.setMessage("다시 인증번호를 전송해주세요.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                Toast.makeText(getApplicationContext(), "돌아가기 후 다시 접속하셔서 시도해 주세요.", Toast.LENGTH_SHORT);
                                return;
                                }
                                else {
                                    checkNumberSmtp = true;
                                        dialog = builder.setMessage("인증 되었습니다.")
                                            .setPositiveButton("확인", null)
                                            .create();
                                    dialog.show();
                                    countDownTimer.cancel();
                                    countView.setText("인증완료");
                                    smsNumber.setEnabled(false); //성공 후 수정 불가하게 하기
                                }

                            }else if (keyNumber < 1000000 && result != keyNumber) {
                                dialog = builder.setMessage("인증번호가 틀립니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();
                                return;
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            dialog = builder.setMessage("인증번호를 확인해주세요.")
                                    .setNegativeButton("확인", null)
                                    .create();
                            dialog.show();
                            return;
                        }
                    }
                });




        //돌아가기 버튼
        btnBack = (Button) findViewById(R.id.cancel_btn);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Register.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
            }
        });

        //회원가입 버튼
        join_btn = (Button) findViewById(R.id.join_btn);
        join_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingStart(); //로딩창 띄우기
                //EditText에서 입력받은값 변수에 저장
                String id = Eid.getText().toString();
                String pw = Epw.getText().toString();
                String pwc = Epwc.getText().toString();
                String name = Ename.getText().toString();
                String email = Eemail.getText().toString();
                String smsnumber = Esmsnumber.getText().toString();



                    //결과 리스너 생성
                    Response.Listener<String> responseListener = new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            try {
                                JSONObject jsonResponse = new JSONObject(response);
                                boolean success = jsonResponse.getBoolean("success");
                                if (success) {
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                        //등록후 응답받은 값이 true이면 성공 다이얼로그 출력
                                        builder.setMessage("회원 등록에 성공했습니다.")
                                                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        Intent intent = new Intent(Register.this, Login.class);
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //똑같은 액티비티가 중첩되지 않게 해준다.
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 이전에 사용하던 액티비티를 종료한다.
                                                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // 그동안 쌓여있던 액티비티를 전부 종료해준다.
                                                        startActivity(intent);
                                                    }
                                                })
                                                .create()
                                                .show();
                                                loading_dialog.cancel(); //로딩창 닫기
                                } else {
                                    //등록 실패 했을때 실패 다이얼로그 출력
                                    AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                                    builder.setMessage("회원 등록에 실패했습니다.")
                                            .setPositiveButton("다시 시도", null)
                                            .create()
                                            .show();
                                    loading_dialog.cancel(); //로딩창 닫기
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                loading_dialog.cancel(); //로딩창 닫기
                                Toast.makeText(getApplicationContext(),"잘못된 값입니다4. 문의 부탁드립니다.",Toast.LENGTH_SHORT).show();
                            }
                        }
                    };



                    //각 정보를 입력안했을때는 Toast메세지 출력 후 리턴
                   try {
                       if (isEmpty(id)) {
                           Toast.makeText(getApplicationContext(), "아이디를 입력해주세요.", Toast.LENGTH_LONG).show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       } else if (!validate == true) {
                           Toast.makeText(getApplicationContext(), "아이디 중복확인을 해주세요.", Toast.LENGTH_LONG).show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       } else if (isEmpty(pw)) {
                           Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       } else if (isEmpty(pwc)) {
                           Toast.makeText(getApplicationContext(), "비밀번호를 확인하세요.", Toast.LENGTH_LONG).show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       } else if (!pw.equals(pwc)) {
                           Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_LONG).show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       } else if (isEmpty(name)) {
                           Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요.", Toast.LENGTH_LONG).show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       }else if (!nameCheck == true) {
                           Toast.makeText(getApplicationContext(), "닉네임 중복확인을 해주세요.", Toast.LENGTH_LONG).show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       } else if (isEmpty(email)) {
                           Toast.makeText(getApplicationContext(), "이메일을 입력해주세요.", Toast.LENGTH_LONG).show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       } else if (isEmpty(smsnumber)) {
                           Toast.makeText(getApplicationContext(), "인증번호를 입력해주세요.", Toast.LENGTH_LONG).show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       } else if (!checkNumberSmtp == true) {
                           Toast.makeText(getApplicationContext(), "인증번호 확인버튼을 눌러주세요.", Toast.LENGTH_LONG).show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       } else if (result != keyNumber || !timeLimit == true) {
                           Toast.makeText(getApplicationContext(), "인증번호가 일치하지않습니다.", Toast.LENGTH_LONG).show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       }

                       //비밀번호 유효성
                       else if (!Pattern.matches("^(?=.*[A-Za-z])(?=.*[0-9])(?=.*[$@$!%*#?&]).{8,20}.$", pw)) {
                           AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                           dialog = builder.setMessage("비밀번호 형식을 지켜주세요.")
                                   .setPositiveButton("확인", null)
                                   .create();
                           dialog.show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       }

                       //이메일 형식체크
                       else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                           AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                           dialog = builder.setMessage("이메일 형식을 지켜주세요.")
                                   .setPositiveButton("확인", null)
                                   .create();
                           dialog.show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       }


                       //닉네임 유효성
                       else if (!Pattern.matches("^[가-힣a-zA-Z0-9_]{2,10}$", name)) {
                           AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
                           dialog = builder.setMessage("닉네임 형식을 지켜주세요.")
                                   .setPositiveButton("확인", null)
                                   .create();
                           dialog.show();
                           loading_dialog.cancel(); //로딩창 닫기
                           return;
                       } else {
                           //모든 값이 다 있으면 DB에 저장하는 메소드 실행
                           RegisterRequest register = new RegisterRequest(id, pw, name, email, datetime,
                                   cigacount, cigapay, goal, profileimage, responseListener);
                           RequestQueue queue = Volley.newRequestQueue(Register.this);
                           queue.add(register);
                       }
                       return;
                   } catch (Exception e){
                       e.printStackTrace();
                       loading_dialog.cancel(); //로딩창 닫기
                       Toast.makeText(getApplicationContext(),"올바른 값을 입력해주세요.",Toast.LENGTH_SHORT).show();
                   }
            }
        });
    }
        public void startLoading(String dbEmail) {
                //구글 이메일로 smtp 사용해서 인증번호 보내기
                GMailSender gMailSender = new GMailSender("merrygoaround0726@gmail.com", "asdf4694!@");
                //GMailSender.sendMail(제목, 본문내용, 받는사람);
                try {
                    gMailSender.sendMail("금연투게더 인증번호 입니다.", "인증번호는 : \"" + result + "\" 입니다. \n " +
                            "인증번호를 입력하시고 확인버튼을 누르시면 회원가입이 완료됩니다.", dbEmail);
                    Toast.makeText(getApplicationContext(), "인증번호가 전송되었습니다.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "이메일 전송 오류, 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                }
            }

    /**로딩창 띄우기 */
    public void loadingStart(){
        loading_dialog = new Loading_Dialog(Register.this);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false); //뒤로가기방지
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.show();
    }
}