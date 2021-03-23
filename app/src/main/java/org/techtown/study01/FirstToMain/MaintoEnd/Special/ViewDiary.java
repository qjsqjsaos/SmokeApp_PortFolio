package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.techtown.study01.FirstToMain.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewDiary extends AppCompatActivity {

    public static Button viewRevise_btn, viewBack_Btn;
    public static TextView viewDate, viewMainText, viewTitle;
    public static ImageView viewImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_diary);

        setInit();//참조하기
        clickListener(); //클릭리스너들
        acceptInfo(); //다이어리 정보 적용



    }
    /**다이어리 정보 적용*/
    private void acceptInfo() {
        //DiaryFrag에서 받기
        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String mainText = intent.getStringExtra("mainText");
        String saveDate = intent.getStringExtra("saveDate");

        if(saveDate == null){ //날짜 값이 없으면 오늘 날짜를 넣는다.
            SimpleDateFormat FORMATTER =  new SimpleDateFormat("yyyy-MM-dd"); //날짜 데이터 포맷
            Date time = new Date();
            String todayDate = FORMATTER.format(time);
            viewDate.setText("작성 일자 : " + todayDate); //날짜값넣기
        }

        viewTitle.setText("제목 : "+ title); //제목에 값넣기
        viewMainText.setText(mainText); //본문값넣기


        Glide.with(getApplicationContext()).load(Diary.gotoViewDiaryUri).into(viewImage); //이미지 넣기
    }

    /**클릭 리스너 모음*/
    private void clickListener() {
        //돌아가기
        viewBack_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    /**참조하기 */
    private void setInit() {
        viewRevise_btn = findViewById(R.id.viewRevise_btn); //수정하기버튼
        viewBack_Btn = findViewById(R.id.ViewBack_Btn); //돌아가기 버튼
        viewDate = findViewById(R.id.viewDate); //작성 날짜 표시
        viewMainText = findViewById(R.id.viewMainText); //본문 내용
        viewTitle = findViewById(R.id.viewTitle); //본문 제목
        viewImage = findViewById(R.id.viewImage); //본문 이미지
    }
}