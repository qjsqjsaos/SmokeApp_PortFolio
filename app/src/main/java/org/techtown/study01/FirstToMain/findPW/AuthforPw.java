package org.techtown.study01.FirstToMain.findPW;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.techtown.study01.FirstToMain.R;

public class AuthforPw extends AppCompatActivity {

    private EditText AuthText;
    private Button AuthButton;
    private String id, pw;
    private int AuthNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.authfor_pw);

        AuthText = findViewById(R.id.AuthTextEmail22);
        AuthButton = findViewById(R.id.AuthButton22);


        Intent intent = getIntent(); // FindPw에서 온 아이디와 비밀번호, 인증번호 꺼내기
        id = intent.getStringExtra("id");
        pw = intent.getStringExtra("pw");
        AuthNumber = intent.getIntExtra("authNumber",1); //인트 데이터는 기본값 1붙여줘야함.

        AuthButton.setOnClickListener(new View.OnClickListener() { //만약 인증하기 버튼을 누르면
            @Override
            public void onClick(View v) {
                int editAuthNumber = Integer.parseInt(AuthText.getText().toString());
                if(editAuthNumber == AuthNumber){ //넘어온 번호와 입력한 번호가 같으면
                    Toast.makeText(getApplicationContext(),"인증되었습니다.", Toast.LENGTH_SHORT).show();
                    Intent intent1 = new Intent(AuthforPw.this, ChangePw.class);
                    intent1.putExtra("id", id); // 받은 아이디, 비밀번호 다시 넣어서 ChangePw에 전달
                    intent1.putExtra("pw", pw); // 받은 아이디, 비밀번호 다시 넣어서 ChangePw에 전달
                    startActivity(intent1);
                } else {
                    Toast.makeText(getApplicationContext(),"인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
}