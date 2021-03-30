//메인 첫 화면
package org.techtown.study01.FirstToMain.start;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;

public class Startbutton extends AppCompatActivity {

    private Button imageButton;
    private String name, id, pw;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_button);


        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        id = intent.getStringExtra("id");
        pw = intent.getStringExtra("pw");

        //"끊어 보자" 버튼 클릭 시 로그인 페이지나, 홈 화면으로 이동
        imageButton = (Button) findViewById(R.id.goStart);
        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Quest1.class);
                intent.putExtra("name", name);
                intent.putExtra("id",id);
                intent.putExtra("pw",pw);
                startActivity(intent);
                finish();
            }
        });
    }
}