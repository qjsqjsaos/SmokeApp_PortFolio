package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.prolificinteractive.materialcalendarview.CalendarDay;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class WriteDiary extends AppCompatActivity {

    public static EditText title_diary, mainText_diary;
    public static Button inputImage, cancel_btn_diary, saveDiary, defaultImageW;
    public static ImageView inputImgeReal; //첨부파일 미리보기
    public static TextView dateWW;
    public static TextView liveTextLength;

    public static String title, mainText;

    private static final int REQUEST_CODE = 0;
    public static String titleV, mainTextV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.write_diary);

        setInit(); //참조정리

        if(ViewDiary.saveDateV == null){ //날짜 값이 없으면 오늘 날짜를 넣는다.
            SimpleDateFormat FORMATTER =  new SimpleDateFormat("yyyy-MM-dd"); //날짜 데이터 포맷
            Date time = new Date();
            String todayDate = FORMATTER.format(time);
            dateWW.setText("날짜 : " + todayDate); //날짜값넣기
        }else { //아니면 가져온 날짜 넣기
            dateWW.setText("날짜 : " + ViewDiary.saveDateV);
        }

        textChanger(); //글자 수 표시

        buttonListener(); //버튼 리스너 모음
    }

    /**글자수 표시*/
    private void textChanger() {
        mainText_diary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //글자 갯수가 변할 때 메서드 실행
                String input = mainText_diary.getText().toString();
                liveTextLength.setText(input.length() + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }


    /**참조 정리 */
    private void setInit() {
        liveTextLength = findViewById(R.id.liveTextLength); //텍스트뷰 글자수 표시
        title_diary = findViewById(R.id.title_diary); //일기 제목
        mainText_diary = findViewById(R.id.mainText); //일기 본문
        inputImage = findViewById(R.id.inputImage); //이미지 첨부 버튼
        defaultImageW = findViewById(R.id.defaultImageW); //이미지 없음 버튼
        dateWW = findViewById(R.id.dateWW); //날짜 표시
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
    private void buttonListener() {
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

                //이미지 없음 버튼
                defaultImageW.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Diary.uri = null; //널값을 주어 이미지가 없게 하기
                        inputImgeReal.setImageResource(R.drawable.no_image); //기본이미지 값 우선 넣어주기
                        ViewDiary.viewLayout.setVisibility(View.GONE); //리니어 레이아웃도 일시적으로 없애기
                    }
                });

                //일기 취소 버튼 누를때
                cancel_btn_diary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish(); //창 닫기
                    }
                });

                //일기 작성 버튼
                saveDiary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        title = title_diary.getText().toString();
                        mainText = mainText_diary.getText().toString();
                        createDiary(title, mainText, Diary.startdate); //제목, 본문, 오늘 날짜를 디비로 보낸다.
                        //혹시 만들고 다이어리에 바로 들어갈 걸 예상해서 변수에 값을 넣어주고, 임시적으로 ViewDiary에서 보여준다.
                        titleV = title;
                        mainTextV = mainText;
                        //파이어베이스 사진 저장하기
                        createProfile_Photo_and_Delete(HomeMain.num, Diary.startdate); //날짜로 식별한다. 날짜를 파라미터로 넣어준다
                        Log.d("보자보자", title);
                        Log.d("보자보자", mainText);
                        Log.d("보자보자", Diary.startdate);
                        Log.d("보자보자", String.valueOf(HomeMain.num));
                    }
                });

    }

    /**디렉토리 만들기(혹시 없을경우 대비해서) = 파이어베이스 */
    public void createDir(int num){
        //우선 디렉토리 파일 하나만든다.
        File file = getExternalFilesDir(Environment.DIRECTORY_PICTURES + "diary_photo/num" + num + "/"); //이미지를 저장할 수 있는 디렉토리 ex)//diary_photo1(식별값)
        //구분할 수 있게 /toolbar_images폴더에 넣어준다.
        //이 파일안에 저 디렉토리가 있는지 확인
        if (!file.isDirectory()) { //디렉토리가 없으면,
            file.mkdir(); //디렉토리를 만든다.
        }
    }

    // TODO: 2021-03-16 우선 파이어베이스 저장하는 법
    /**파이어베이스로 프로필 이미지 저장 및 기존 이미지 삭제
     * @param startdate*/
    void createProfile_Photo_and_Delete(int num, String startdate) {
        createDir(num); //디렉토리가 없으면 만든다.
        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
        StorageReference storageRef = storage.getReference();//스토리지를 참조한다.
        //파일명을 만들자.
        //여기서 DP는 다이어리 포토에 줄임말이다.
        String filename = "DP"+ "_" +startdate +".jpg";  //ex) DP_2019-02-21.jpg 해당 날짜 값으로만 식별한다.(어차피 디렉토리로 분류로 나누었기 때문에 이정도 식별로 충분하다)
        Uri file = Diary.uri;
        if(Diary.uri == null) { //uri값이 없으면 기본이미지로 저장한다.
            DiaryFrag.diaryImage.setImageResource(R.drawable.no_image); //실패시 기본이미지
        }else {
            //여기서 원하는 이름 넣어준다. (filename 넣어주기)
            StorageReference riversRef = storageRef.child("diary_photo/num" + HomeMain.num + "/" + filename);
            UploadTask uploadTask = riversRef.putFile(file);


            // TODO: 2021-03-17 기존 일기 이미지와 일기 제목과 내용을 우선 삭제한다.(중복못하게)
            // Create a reference to the file to delete
            StorageReference desertRef = storageRef.child("diary_photo/num" + HomeMain.num + "/" + filename); //삭제할 프로필이미지 명
            // Delete the file
            desertRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            });

            // TODO: 2021-03-17 새로운 프로필 이미지 저장
            // Register observers to listen for when the download is done or if it fails
            uploadTask.addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception exception) {
                }
            }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Toast.makeText(getApplicationContext(), "일기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    /**일기 만들기(num으로 사용자를 식별하고, 그에 맞는 다이어리 테이블에 컬럼값들을 저장한다.)
     * @param title
     * @param mainText
     * @param startdate*/
    void createDiary(String title, String mainText, String startdate) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        int length = jsonObject.length() + 1;
                        //오늘 날짜를 구해 바로 적용된 것처럼 보이기 위해 오늘 날짜에 초록표시를 한다.
                        Date time = new Date();
                        String todayDate = Diary.FORMATTER.format(time);
                        String year = todayDate.substring(0,4); //받아온 연도 ex)2021
                        String month = todayDate.substring(5,7); //받아온 달 ex)02
                        String dayofMonth = todayDate.substring(8,10); //받아온 일 수 ex)25
                        diaryWriteDate(year, month, dayofMonth); //지금 쓴 날짜 초록색으로 변하게 하기
                        Diary.countDiary.setText(": "+ length+ "회"); //초록불 횟수 늘리기(일기를 쓰게 된다면 하나 더 늘게 만든다)
                        Toast.makeText(getApplicationContext(), "일기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                        Log.d("카운트다이어리2", String.valueOf(length));
                        //다이어리프래그 부분 //일시적
                        DiaryFrag.diaryFrag.setVisibility(View.VISIBLE); //다이어리 보여주고
                        DiaryFrag.diaryText.setText(title); //제목 부분 넣어주고

                        if(Diary.uri != null){ //이 값이 널이 아니면 레이아웃을 보여준다.
                            Glide.with(getApplicationContext()).load(Diary.uri).into(DiaryFrag.diaryImage); //넣었던 이미지를 넣는다.
                        }else { //아니면 없애기
                            Glide.with(getApplicationContext()).load(R.drawable.no_image).into(DiaryFrag.diaryImage); //이미지가 없으면 기본이미지를 넣는다.
                        }
                        Log.d("널일까?", String.valueOf(Diary.uri));

                        finish(); //액티비티 끄기
                    }else{
                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    }

                }

                catch(JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        };
        CreateDiaryColumn createDiaryTable_check = new CreateDiaryColumn(HomeMain.num, title, mainText, startdate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(createDiaryTable_check);

        Log.d("홈메인넘", String.valueOf(HomeMain.num));
    }

    private void diaryWriteDate(String year, String month, String day) {

        //스트링을 인트로 형변환
        int yearR = Integer.parseInt(year);
        int monthR = Integer.parseInt(month);
        int newMonthR = monthR - 1; //달은 1빼준다.
        int dayR = Integer.parseInt(day);
        Log.d("캘린더리스트", String.valueOf(yearR));

        //일기를 썼던 날짜리스트를 만든다.
        Diary.calendarDayList = new ArrayList<>();
        Diary.calendarDayList.add(CalendarDay.from(yearR, newMonthR, dayR)); //일기 쓴 날짜 표시/ 일기 쓴 날 들을 add해준다.
        Log.d("캘린더리스트", String.valueOf(Diary.calendarDayList));

        EventDecorator eventDecorator = new EventDecorator(Color.rgb(10, 207, 32), Diary.calendarDayList); //색표시 이벤트 데코레이터 호출
        Diary.materialCalendarView.addDecorators(eventDecorator);


    }
}