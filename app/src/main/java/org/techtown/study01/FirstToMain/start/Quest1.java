package org.techtown.study01.FirstToMain.start;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import org.techtown.study01.FirstToMain.R;

public class Quest1 extends AppCompatActivity {

    public static EditText cigaCount; //Frag1에서 참조하기 위해 public static을 해준다.
    public static EditText cigaPay; //Frag1에서 참조하기 위해 public static을 해준다.

    private Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.quest1);

        cigaCount = (EditText) findViewById(R.id.dayofciga);
        cigaPay = (EditText) findViewById(R.id.pay);

        button = findViewById(R.id.startButton);

        button.setOnClickListener(new View.OnClickListener() { //금연시작 버튼
            @Override
            public void onClick(View v) {


            }
        });

    }

}