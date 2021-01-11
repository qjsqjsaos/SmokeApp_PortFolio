//로그인 화면
package org.techtown.study01;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;

import org.techtown.study01.R;


public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

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
