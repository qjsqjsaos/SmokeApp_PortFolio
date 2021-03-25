package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
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

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.List.RecyclerMain;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.BottomNavi;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.homeMain.Loading_Dialog;
import org.techtown.study01.FirstToMain.register.CreateDiaryTable_Check;
import org.techtown.study01.FirstToMain.register.Register;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ReviseDiary extends AppCompatActivity {

    private EditText titleR, mainTextR;
    private Button getImageR;
    private TextView textChanger, dateRR;
    private ImageView inputImageR;
    private Button cancel_btnR, goRevise, defaultImage;

    private String title, mainText;

    private static final int REQUEST_CODE = 0;

    //로딩창 띄우기
    private Loading_Dialog loading_dialog;

    /**갤러리에서 이미지 받아오기 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Diary.uri = data.getData(); //사진 자료를 받는다.
                Glide.with(getApplicationContext()).load(Diary.uri).into(inputImageR);
            } else if (resultCode == RESULT_CANCELED) {// 취소시 호출할 행동 쓰기
                Toast.makeText(getApplicationContext(), "이미지 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.revise_diary);

        setInit(); //참조정리

        clickListener(); //리스너 모음

        getData(); //전달받은 데이터 적용하기

        textChanger(); //글자수 변경

        inputImageR.setImageResource(R.drawable.no_image); //기본 이미지를 처음에 넣어준다.

    }

    /**글자수 표시*/
    private void textChanger() {
        mainTextR.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //글자 갯수가 변할 때 메서드 실행
                String input = mainTextR.getText().toString();
                textChanger.setText(input.length() + "/1000");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    /**
     * 리스너 모음
     */
    private void clickListener() {
        //수정하기 버튼
        goRevise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviceAlertD(); //수정하기 버튼
            }
        });

        //취소하기 버튼
        cancel_btnR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviceCancelAlertD(); //취소하고 원래 자리로
            }
        });

        //이미지 첨부
        getImageR.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //인텐트를 통해 갤러리로 요청코드 보내기
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent, REQUEST_CODE);
                ViewDiary.viewLayout.setVisibility(View.VISIBLE); //뷰다이어리 사진도 일시적으로 보이게하기
            }
        });

        //이미지 없음 버튼
        defaultImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Diary.uri = null; //널값을 주어 이미지가 없게 하기
                inputImageR.setImageResource(R.drawable.no_image); //기본이미지 값 우선 넣어주기
                ViewDiary.viewLayout.setVisibility(View.GONE); //뷰다이어리 사진도 일시적으로 없애기
            }
        });
    }


    // TODO: 2021-03-16 우선 파이어베이스 저장하는 법
    /**파이어베이스로 프로필 이미지 저장 및 기존 이미지 삭제
     * @param startdate*/
    void createProfile_Photo_and_Delete(int num, String startdate) {
        Log.d("잘들어오고있지?", startdate);
        createDir(num); //디렉토리가 없으면 만든다.
        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
        StorageReference storageRef = storage.getReference();//스토리지를 참조한다.
        //파일명을 만들자.
        //여기서 DP는 다이어리 포토에 줄임말이다.
        String filename = "DP"+ "_" +startdate +".jpg";  //ex) DP_2019-02-21.jpg 해당 날짜 값으로만 식별한다.(어차피 디렉토리로 분류로 나누었기 때문에 이정도 식별로 충분하다)
        Uri file = Diary.uri;
        Log.d("잘들어오고있지?", String.valueOf(Diary.uri));


        if(Diary.uri == null) { //uri값이 없으면 기본이미지로 저장한다.
            DiaryFrag.diaryImage.setImageResource(R.drawable.no_image);
            // TODO: 삭제만하고, 리턴 이 경우는 사진 값이 없을 때, 기본이미지 저장을 위험이다.
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
            return;
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
                }
            });
        }
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

    /**
     * 전달 받은 값 적용하기
     */
    private void getData() {
        // TODO: 2021-03-23 여기 있는 거 전역변수로 넣고, 수정 기능 만들기 이거 다음으로 삭제 기능 만들기
        Intent intent = getIntent();
        title = intent.getStringExtra("title"); //제목
        mainText = intent.getStringExtra("mainText"); //본문
        String mainlength = intent.getStringExtra("mainlength"); //글자 수
        titleR.setText(title); //제목에 삽입
        mainTextR.setText(mainText); //본문에 삽입
        textChanger.setText(mainlength + "/1000");

        Glide.with(getApplicationContext()).load(Diary.uri).into(inputImageR); //첫 이미지 삽입

        if(ViewDiary.saveDateV == null){ //날짜 값이 없으면 오늘 날짜를 넣는다.
            SimpleDateFormat FORMATTER =  new SimpleDateFormat("yyyy-MM-dd"); //날짜 데이터 포맷
            Date time = new Date();
            String todayDate = FORMATTER.format(time);
            dateRR.setText("날짜 : " + todayDate); //날짜값넣기
        }else { //아니면 가져온 날짜 넣기
            dateRR.setText("날짜 : " + ViewDiary.saveDateV);
        }
    }

    /**
     * 전달 받은 값 디비에 적용하기
     */
    public void updateDiary() {
        //날짜로 식별해서 수정한다.
        loadingStart(); //로딩창 띄우기
        //새로 입력한 값 가져오기
        String titleC = titleR.getText().toString();
        String mainTextC = mainTextR.getText().toString();

        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) { //수정 완료
                        loading_dialog.dismiss(); //로딩창 닫기
                        //수정값 일시적으로 적용
                        ViewDiary.viewTitle.setText(titleC);
                        ViewDiary.viewMainText.setText(mainTextC);
                        //다이어리 값에다가도 일시적으로 넣어주기
                        Diary.viewtitle = titleC;
                        Diary.viewMaintText = mainTextC;
                        //파이어베이스로 이미지도 수정해준다.
                        createProfile_Photo_and_Delete(HomeMain.num, Diary.startdate);
                        DiaryFrag.diaryText.setText(titleC); //
                        if(Diary.uri != null){ //이 값이 널이 아니면 레이아웃을 보여준다.
                            Glide.with(getApplicationContext()).load(Diary.uri).into(ViewDiary.viewImage); //일시적 ViewDiary.viewImage 이미지 사진 넣기
                            Glide.with(getApplicationContext()).load(Diary.uri).into(DiaryFrag.diaryImage); //넣었던 이미지를 넣는다.
                            ViewDiary.viewLayout.setVisibility(View.VISIBLE);
                        }else { //아니면 없애기
                            Glide.with(getApplicationContext()).load(R.drawable.no_image).into(DiaryFrag.diaryImage); //이미지가 없으면 기본이미지를 넣는다.
                        }
                        Toast.makeText(getApplicationContext(), "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    } else { //수정 실패
                        Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        loading_dialog.dismiss(); //로딩창 닫기
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    loading_dialog.dismiss(); //로딩창 닫기
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getApplicationContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    loading_dialog.dismiss(); //로딩창 닫기
                    return;
                }
            }
        };

        ReviseDiary_Check reviseDiary_check = new ReviseDiary_Check(HomeMain.num, titleC, mainTextC, Diary.startdate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(reviseDiary_check);
        Log.d("어디보자", String.valueOf(HomeMain.num));
        Log.d("어디보자", title);
        Log.d("어디보자", mainText);
        Log.d("어디보자", Diary.startdate);
    }


    /**참조 정리 */
    private void setInit() {
        dateRR = findViewById(R.id.dateRR); //날짜보여주기
        titleR = findViewById(R.id.titleR); //제목창
        mainTextR = findViewById(R.id.mainTextR); //본문창
        getImageR = findViewById(R.id.getImageR); //이미지 가져오기
        textChanger = findViewById(R.id.textChanger); //입력한 대로 글자수 표시
        inputImageR = findViewById(R.id.inputImageR); //이미지 넣는 뷰
        defaultImage = findViewById(R.id.defaultImage); //이미지없음 버튼
        cancel_btnR = findViewById(R.id.cancel_btnR); //취소버튼
        goRevise = findViewById(R.id.goRevise); //수정하기 버튼
    }

    /**수정하기 다이얼로그 */
    private void reviceAlertD() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReviseDiary.this);
        builder.setCancelable(false); //외부 클릭시 막아주기
        builder.setTitle("수정 확인");
        builder.setMessage("수정하시겠습니까?");
        builder.setPositiveButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        updateDiary(); //업데이트 적용하기
                        finish();
                    }
                });
        builder.show();
    }

    /**수정취소 다이얼로그 */
    private void reviceCancelAlertD() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ReviseDiary.this);
        builder.setCancelable(false); //외부 클릭시 막아주기
        builder.setTitle("수정 취소");
        builder.setMessage("수정을 취소하시겠습니까?");
        builder.setPositiveButton("아니오",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        builder.setNegativeButton("예",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        builder.show();
    }

    /**로딩창*/
    public void loadingStart(){
        loading_dialog = new Loading_Dialog(ReviseDiary.this);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false); //뒤로가기방지
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.show();
    }
}