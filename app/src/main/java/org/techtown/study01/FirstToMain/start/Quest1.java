package org.techtown.study01.FirstToMain.start;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
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
import org.techtown.study01.FirstToMain.homeMain.BottomNavi;
import org.techtown.study01.FirstToMain.register.Register;
import org.techtown.study01.FirstToMain.register.RegisterRequest;

import java.util.regex.Pattern;

import static android.text.TextUtils.isEmpty;

public class Quest1 extends AppCompatActivity {

    public static EditText cigaCount; //Frag1에서 참조하기 위해 public static을 해준다.
    public static EditText cigaPay; //Frag1에서 참조하기 위해 public static을 해준다.
    public static EditText goalText; //이것도 참조해야한다.

    private AlertDialog dialog; //알림 다이아로그

    private Button button;

    String cCount;
    String cPay;
    String cGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest1);

        cigaCount = (EditText) findViewById(R.id.dayofciga); //하루 평균 담배 갯수
        cigaPay = (EditText) findViewById(R.id.pay); // 담배값
        goalText = (EditText) findViewById(R.id.TextViewGoal); //금연 목표

        button = findViewById(R.id.startButton);

        button.setOnClickListener(new View.OnClickListener() { //금연시작 버튼
            @Override
            public void onClick(View v) {
                cCount = cigaCount.getText().toString();
                cPay = cigaPay.getText().toString();
                cGoal = goalText.getText().toString();

                /** 이 부분 Login부분이랑 번갈아 가면서 확인 할 것.*/
                //로그인으로 부터 id가져오기 (이걸로 판별해서 위에 3개의 값을 다 입력할 것이다)
//                Intent intent = getIntent();
//                String Eid = intent.getStringExtra("id");

                String Eid = "qjsqjsaos";

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Quest1.this);
                                //등록후 응답받은 값이 true이면 성공 다이얼로그 출력
                                builder.setMessage("꼭 성공하시길 바랍니다.")
                                        .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                Intent intent = new Intent(Quest1.this, BottomNavi.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP); //똑같은 액티비티가 중첩되지 않게 해준다.
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // 이전에 사용하던 액티비티를 종료한다.
                                                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY); // 그동안 쌓여있던 액티비티를 전부 종료해준다.
                                                startActivity(intent);
                                            }
                                        })
                                        .create()
                                        .show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(Quest1.this);
                                builder.setMessage("전부 입력해주세요!")
                                        .setPositiveButton("다시 시도", null)
                                        .create()
                                        .show();
                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "잘못된 값입니다(Quest1). 문의 부탁드립니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                try {
                    if (isEmpty(cCount)) {
                        Toast.makeText(getApplicationContext(), "하루 평균 흡연량을 적어주세요.", Toast.LENGTH_LONG).show();
                        return;
                    } else if (isEmpty(cPay)) {
                        Toast.makeText(getApplicationContext(), "하루 평균 담배값을 적어주세요.", Toast.LENGTH_LONG).show();
                        return;
                    }
                    else if (isEmpty(cGoal)) {
                        Toast.makeText(getApplicationContext(), "금연 목적을 적어주세요.", Toast.LENGTH_LONG).show();
                        return;
                    }

                    //담배 갯수 유효성
                    else if (!Pattern.matches("^[0-9]*$", cCount)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Quest1.this);
                        dialog = builder.setMessage("숫자만 입력해주세요!")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }

                    //담배값 유효성
                    else if (!Pattern.matches("^[0-9]*$", cPay)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Quest1.this);
                        dialog = builder.setMessage("숫자만 입력해주세요!")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    }

                    //금연 목표 유효성
                    else if (!Pattern.matches("^[가-힣a-zA-Z0-9_]{2,30}$", cGoal)) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(Quest1.this);
                        dialog = builder.setMessage("목표는 최소 2자에서 ~ 30자입니다.(특수문자도 안됨)")
                                .setPositiveButton("확인", null)
                                .create();
                        dialog.show();
                        return;
                    } else {
                        Quest1_Request quest1_request = new Quest1_Request(Eid, cCount, cPay, cGoal, responseListener);
                        RequestQueue queue = Volley.newRequestQueue(Quest1.this);
                        queue.add(quest1_request);

                    }
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "올바른 값을 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}