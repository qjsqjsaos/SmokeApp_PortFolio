package org.techtown.study01.FirstToMain.MaintoEnd.Notice;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import org.techtown.study01.FirstToMain.R;

public class Notice extends AppCompatActivity {

    private LinearLayout notice1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notice);

        notice1 = findViewById(R.id.notice1);
        notice1.setOnClickListener(v ->{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://daldalhanstory.tistory.com/207"));
            startActivity(intent);
        });
    }
}
