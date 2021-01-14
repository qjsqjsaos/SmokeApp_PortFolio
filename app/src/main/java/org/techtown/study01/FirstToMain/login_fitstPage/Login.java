//로그인 화면
package org.techtown.study01.FirstToMain.login_fitstPage;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.register.Register;


public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_fistpage);

        EditText idText = (EditText) findViewById(R.id.idText);
        EditText passwordText = (EditText) findViewById(R.id.passwordText);

        Button loginButton = (Button) findViewById(R.id.btn_login);

        TextView findIdButton = (TextView) findViewById(R.id.tv_id); //아이디찾기
        TextView findPassButton = (TextView) findViewById(R.id.tv_pass); //비밀번호찾기
        TextView registerButton = (TextView) findViewById(R.id.rg_sign); //회원가입 이동


        registerButton.setOnClickListener(new View.OnClickListener() { //회원가입 이동
            @Override
            public void onClick(View v) {
                Intent registerIntent = new Intent(Login.this, Register.class);
                Login.this.startActivity(registerIntent); //registerIntent를 통해 인텐트 실행하여 회원가입 창 이동.
            }
        });


    }
}
