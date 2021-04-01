package org.techtown.GmanService.FirstToMain.MaintoEnd.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import org.techtown.GmanService.FirstToMain.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Developer_Info extends AppCompatActivity {

    private TextView NSText;
    private Button goToWeb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.developer__info);

        NSText = findViewById(R.id.NSText);
        goToWeb = findViewById(R.id.goToWeb);
        goToWeb.setOnClickListener(v->{
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://daldalhanstory.tistory.com"));
            startActivity(intent);
        });

        long NSDay = 0;
        try {
            NSDay = calTest();
            NSText.setText(String.valueOf(NSDay));
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    //날짜 차이 구하기(일수
        public long calTest() throws ParseException {
            Calendar getToday = Calendar.getInstance();
            getToday.setTime(new Date()); //금일 날짜

            String s_date = "2020-10-14";
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(s_date);
            Calendar cmpDate = Calendar.getInstance();
            cmpDate.setTime(date); //특정 일자

            long diffSec = (getToday.getTimeInMillis() - cmpDate.getTimeInMillis()) / 1000;
            long diffDays = diffSec / (24*60*60); //일자수 차이

            return diffDays;
        }

}