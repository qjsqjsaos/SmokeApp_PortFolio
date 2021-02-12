package org.techtown.study01.FirstToMain.findid;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.login_fitstPage.Login;

public class Id_pw_complete extends AppCompatActivity {

    private Button button;
    private TextView textView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.id_pw_complete);

        textView = findViewById(R.id.textView3366);

        Intent intent = getIntent();
        String findIdMessage = intent.getStringExtra("findId"); //아이디 식별메세지
        String findPWMessage = intent.getStringExtra("findPw"); //비밀번호 식별메세지

        if(findIdMessage.equals("아이디찾기") && findPWMessage == null){ //만약 아이디찾기라면,
            textView.setText("이메일로 아이디가 전송되었습니다.");
        }
        else if(findIdMessage == null && findPWMessage.equals("비밀번호찾기")){ // 만약 비밀번호 찾기라면
            textView.setText("비밀번호가 변경되었습니다.");
        }

        button = findViewById(R.id.authButton962); //로그인 창으로 돌아가기

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Id_pw_complete.this, Login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}
