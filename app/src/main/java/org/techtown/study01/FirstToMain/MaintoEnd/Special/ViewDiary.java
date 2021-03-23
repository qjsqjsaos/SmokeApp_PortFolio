package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.techtown.study01.FirstToMain.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ViewDiary extends AppCompatActivity {

    public static Button viewRevise_btn, viewBack_Btn;
    public static TextView viewDate, viewMainText, viewTitle;
    public static ImageView viewImage;
    public static LinearLayout viewLayout;

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
        }else { //아니면 가져온 날짜 넣기
            viewDate.setText("작성 일자 : " + saveDate);
        }

        viewTitle.setText("제목 : "+title); //제목에 값넣기
        viewMainText.setText(mainText); //본문값넣기

        if(Diary.gotoViewDiaryUri == null){ //uri에 값이 없으면// 서버에 이미지만 없다는 뜻이다.
            viewLayout.setVisibility(View.GONE); //이미지뷰 가리기
        }else{ //이미지가 있으면 이미지를 넣어준다.
            Glide.with(getApplicationContext()).load(Diary.gotoViewDiaryUri).into(viewImage); //이미지 넣기
        }

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

        //수정하기
        viewRevise_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviceAlertD(); //수정하기 다이얼로그
            }
        });
    }


    /**수정하기 다이얼로그 */
    private void reviceAlertD() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ViewDiary.this);
        builder.setCancelable(false); //외부 클릭시 막아주기
        builder.setTitle("수정하기");
        builder.setMessage("수정하시겠습니까?");
        builder.setPositiveButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(getApplicationContext(), ReviseDiary.class); //수정하러 이동하기
                        intent.putExtra("title", Diary.viewtitle); //제목
                        intent.putExtra("mainText", Diary.viewMaintText); //본문
                        intent.putExtra("saveDate", Diary.dbDate); //날짜
                        startActivity(intent);
                    }
                });
        builder.show();
    }


    /**참조하기 */
    private void setInit() {
        viewLayout = findViewById(R.id.viewLayout); //이미지 리니어레이아웃
        viewRevise_btn = findViewById(R.id.viewRevise_btn); //수정하기버튼
        viewBack_Btn = findViewById(R.id.ViewBack_Btn); //돌아가기 버튼
        viewDate = findViewById(R.id.viewDate); //작성 날짜 표시
        viewMainText = findViewById(R.id.viewMainText); //본문 내용
        viewTitle = findViewById(R.id.viewTitle); //본문 제목
        viewImage = findViewById(R.id.viewImage); //본문 이미지
    }
}