package org.techtown.study01.FirstToMain.findid;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.login_fitstPage.Login;

public class Id_pw_complete extends AppCompatActivity {

    private Button button;
    private TextView textView;
    private String idMessage = "아이디찾기";
    private String pwMessage = "비밀번호찾기";
    private String findIdMessage, findPwMessage = "기본값";


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_pw_complete);

        textView = findViewById(R.id.textView3366);

        Intent intent = getIntent();
        findIdMessage = intent.getStringExtra("findId"); //아이디 식별메세지
        findPwMessage = intent.getStringExtra("findPw"); //비밀번호 식별메세지


        // TODO: 2021-02-13 여기해결하기(해결했지만 더 확인.)

        if(findIdMessage != null) {
            if (findIdMessage.equals(idMessage)) { //만약 아이디찾기라면,
                textView.setText("이메일로 아이디가 전송되었습니다.");
            }
        }

        if(findPwMessage != null) {
            if (findPwMessage.equals(pwMessage)) { // 만약 비밀번호 찾기라면
                textView.setText("비밀번호가 변경되었습니다.");
            }
        }

        button = findViewById(R.id.authButton962); //로그인 창으로 돌아가기

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Id_pw_complete.this, Login.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                startActivity(intent);
                finish();
            }
        });
    }

}
