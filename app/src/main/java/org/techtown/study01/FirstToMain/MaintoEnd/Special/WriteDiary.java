package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;

import java.text.SimpleDateFormat;
import java.util.Date;

public class WriteDiary extends AppCompatActivity {

    public static EditText title_diary, mainText_diary;
    public static Button inputImage, cancel_btn_diary, saveDiary;
    public static ImageView inputImgeReal; //첨부파일 미리보기

    private static final int REQUEST_CODE = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_diary);

        setInit(); //참조정리

        writeDiary(); //일기쓰기 메서드
    }
    /**중복으로 같은 날짜에 일기쓰기 방지 */
    private void samediaryCheck() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) { //일기가 존재하지 않음.
                        String title = title_diary.getText().toString(); //일기 제목
                        String mainText = mainText_diary.getText().toString(); //일기 본문
                        Diary diary = new Diary();
                        diary.createDiary(title, mainText, Diary.startdate); //제목, 본문, 오늘 날짜를 디비로 보낸다.
                        //파이어베이스 사진 저장하기
                        diary.createProfile_Photo_and_Delete(HomeMain.num, Diary.startdate); //날짜로 식별한다. 날짜를 파라미터로 넣어준다
                        finish(); //창 닫기
                        return;

                    } else { //일기가 존재함
                        Toast.makeText(getApplicationContext(), "선택 날짜에 일기가 존재합니다.", Toast.LENGTH_SHORT).show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };

        SameDiaryCheck sameDiaryCheck = new SameDiaryCheck(HomeMain.num, Diary.startdate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(sameDiaryCheck);
    }


    /**참조 정리 */
    private void setInit() {
        title_diary = findViewById(R.id.title_diary); //일기 제목
        mainText_diary = findViewById(R.id.mainText); //일기 본문
        inputImage = findViewById(R.id.inputImage); //이미지 첨부 버튼
        cancel_btn_diary = findViewById(R.id.cancel_btn_diary); //일기 취소 버튼
        saveDiary = findViewById(R.id.saveDiary); //일기 작성완료 버튼
        inputImgeReal = findViewById(R.id.insertImg); //첨부한 이미지 미리보기
    }

    /**갤러리에서 이미지 받아오기 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Diary.uri = data.getData(); //사진 자료를 받는다.
                Glide.with(getApplicationContext()).load(Diary.uri).into(inputImgeReal);
            } else if (resultCode == RESULT_CANCELED) {// 취소시 호출할 행동 쓰기
                Toast.makeText(getApplicationContext(), "이미지 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**일기 쓰기 메서드 .)*/
    private void writeDiary() {
                //이미지 첨부 버튼 누를때
                inputImage.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //인텐트를 통해 갤러리로 요청코드 보내기
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(intent, REQUEST_CODE);
                    }
                });

                //일기 취소 버튼 누를때
                cancel_btn_diary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish(); //창 닫기
                    }
                });

                //일기 작성완료 버튼 누를때
                saveDiary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //제목과 내용 텍스트 가져오기
                        samediaryCheck(); //중복으로 같은 날짜에 일기쓰기 방지

                    }
                });
    }
}