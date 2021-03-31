package org.techtown.GmanService.FirstToMain.start;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Toast;

import org.techtown.GmanService.FirstToMain.R;

public class First_page_loading extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.first_page_loading);

        Toast.makeText(getApplicationContext(), "접속이 원활하지 않습니다. 잠시 후 다시 접속해주세요.ㅜㅜ", Toast.LENGTH_LONG).show();

    }
}