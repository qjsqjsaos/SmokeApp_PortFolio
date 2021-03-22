package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import androidx.annotation.LongDef;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.homeMain.Loading_Dialog;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class Diary extends Fragment implements OnDateSelectedListener {

    private ViewGroup viewGroup;
    private MaterialCalendarView materialCalendarView;

    private TextView diaryText;
    private ImageView diaryImage;
    private Button dialogPlusButton;
    private static final int REQUEST_CODE = 0;
    private Uri uri;
    private Loading_Dialog loading_dialog;
    private TextView countDiary;

    private ViewPager2 viewPageSetUp;

    private ArrayList<CalendarDay> calendarDayList; //캘린더 리스트 안에 내가 입력한 즉, 일기를 쓴(초록색표시) 날이 다 들어가 있음.

    private static final SimpleDateFormat FORMATTER =  new SimpleDateFormat("yyyy-MM-dd"); //날짜 데이터 포맷


    /** 앱이 맨처음 실행될 때*/
    @Override
    public void onResume() {
        super.onResume();
        Date time = new Date();
        String todayDate = FORMATTER.format(time);
        Log.d("오늘날짜", todayDate);
        //오늘 날짜 일기를 처음 보여준다.
        getFireBaseProfileDiary(HomeMain.num,todayDate); //오늘 날짜 일기 이미지
        getDBDiaryInfo(todayDate); //오늘 날짜 제목 내용
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        viewGroup = (ViewGroup) inflater.inflate(R.layout.diary, container, false);
        diaryText = viewGroup.findViewById(R.id.diaryText); //일기 텍스트
        diaryImage = viewGroup.findViewById(R.id.diaryImage); //일기 이미지
        dialogPlusButton = viewGroup.findViewById(R.id.dialogPlusButton); //플로팅 버튼(일기 쓰기 버튼)
        countDiary = viewGroup.findViewById(R.id.countDiary); //일기 쓴 횟수(초록색 동그라미)


        materialCalendarView = viewGroup.findViewById(R.id.calendarView5);
        materialCalendarView.setSelectedDate(CalendarDay.today()); //오늘 날짜 큰 갈색// 동그라미 표시
        materialCalendarView.setOnDateChangedListener(this);

        WriteDiary(); //일기쓰기버튼

        setInit(); //뷰페이저 실행

        getDBDiaryDateGreen(); //일기 쓴 날짜들 초록색 표시

        return viewGroup;

    }

    /** 일기 쓴 날짜 (이 곳에서 일기 쓴 날짜를 초록색 불로 표시해준다.) (날짜를 계속 넣어줘야함.) **/

    private void diaryWriteDate(String year, String month, String day) {

        //스트링을 인트로 형변환
        int yearR = Integer.parseInt(year);
        int monthR = Integer.parseInt(month);
        int newMonthR = monthR - 1; //달은 1빼준다.
        int dayR = Integer.parseInt(day);
        Log.d("캘린더리스트", String.valueOf(yearR));

        //일기를 썼던 날짜리스트를 만든다.
        calendarDayList = new ArrayList<>();
        calendarDayList.add(CalendarDay.from(yearR, newMonthR, dayR)); //일기 쓴 날짜 표시/ 일기 쓴 날 들을 add해준다.
        Log.d("캘린더리스트", String.valueOf(calendarDayList));
        int countD = calendarDayList.size() + 1;
        Log.d("캘리캘리", String.valueOf(countD));
        countDiary.setText(": " + countD + "회");

        EventDecorator eventDecorator = new EventDecorator(Color.rgb(10, 207, 32), calendarDayList); //색표시 이벤트 데코레이터 호출
        materialCalendarView.addDecorators(eventDecorator);


    }


    /**
     * 뷰페이저 2 실행하기
     */
    private void setInit() {

        /* setup infinity scroll viewpager */
        viewPageSetUp = viewGroup.findViewById(R.id.diaryViewpager); //여기서 뷰페이저를 참조한다.
        FragPagerAdapterDiary fragPagerAdapterDiary = new FragPagerAdapterDiary(getActivity());
        viewPageSetUp.setAdapter(fragPagerAdapterDiary);
        viewPageSetUp.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        viewPageSetUp.setOffscreenPageLimit(1); //좌우로 몇개까지 미리로딩하고 싶냐는 말이다. ex)5라고 입력시 1페이지에 있을때 나머지 2, 3, 4, 5, 6 페이지가 미리로딩된다는 뜻이다.
        viewPageSetUp.setSaveEnabled(false);

        final float pageMargin = (float) getResources().getDimensionPixelOffset(R.dimen.pageMargin); //페이지끼리 간격
        final float pageOffset = (float) getResources().getDimensionPixelOffset(R.dimen.offset); //페이지 보이는 정도

        viewPageSetUp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);

            }
        });
        viewPageSetUp.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float offset = position * -(2 * pageOffset + pageMargin);
                if (-1 > position) {
                    page.setTranslationX(-offset);
                } else if (1 >= position) {
                    float scaleFactor = Math.max(0.7f, 1 - Math.abs(position - 0.14285715f));
                    page.setTranslationX(offset);
                    page.setScaleY(scaleFactor);
                    page.setAlpha(scaleFactor);
                } else {
                    page.setAlpha(0f);
                    page.setTranslationX(offset);
                }
            }
        });

    }

    /**일기 쓰기 메서드 (일기 쓰는 창(다이얼로그)을 열어준다.)*/
    private void WriteDiary() {
        dialogPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Diary_Dialog diary_dialog = new Diary_Dialog(getContext());
                //이미지 첨부 버튼 누를때
                diary_dialog.inputImage.setOnClickListener(new View.OnClickListener() {
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
                diary_dialog.cancel_btn_diary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Diary_Dialog.dialog.dismiss(); //다이얼로그 창 닫기
                    }
                });

                //일기 작성완료 버튼 누를때

                diary_dialog.saveDiary.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //제목과 내용 텍스트 가져오기
                        String title = Diary_Dialog.title_diary.getText().toString(); //일기 제목
                        String mainText = Diary_Dialog.mainText_diary.getText().toString(); //일기 본문
                        SimpleDateFormat format1 = new SimpleDateFormat( "yyyy-MM-dd" ); //일기 쓴 날짜
                        Date date = new Date(); //데이트 객체를 만들어
                        String startdate = format1.format(date); //오늘 날짜를 구한다.
                        createDiary(title, mainText, startdate); //제목, 본문, 오늘 날짜를 디비로 보낸다.

                        //파이어베이스 사진 저장하기
                        createProfile_Photo_and_Delete(HomeMain.num, startdate); //날짜로 식별한다. 날짜를 파라미터로 넣어준다.


                        Diary_Dialog.dialog.dismiss(); //다이얼로그 창 닫기
                    }
                });
            }
        });
    }


    /**달력 날짜를 선택할 때 여기서 날짜 값을 받아온다. 여기서 일기를 가져온다.(디비 연동)*/
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        final String text = selected ? FORMATTER.format(date.getDate()) : "No Selection"; //날짜 클릭 시에 날짜값을 받아온다. //ex)2012-12-25
        String dbDate = dateRebuild(text); //선택한 날짜 변환해서 리턴한다.
        getDBDiaryInfo(dbDate); //mysql디비에서 제목 내용 가져오기  //인자에는 날짜를 보낸다.


    }


    /**갤러리에서 이미지 받아오기 */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                uri = data.getData(); //사진 자료를 받는다.
                Glide.with(getActivity()).load(uri).into(Diary_Dialog.inputImgeReal);
            } else if (resultCode == RESULT_CANCELED) {// 취소시 호출할 행동 쓰기
                Toast.makeText(getContext(), "이미지 불러오기 실패", Toast.LENGTH_SHORT).show();
            }
        }
    }


    // TODO: 2021-03-16 우선 파이어베이스 저장하는 법
    /**파이어베이스로 프로필 이미지 저장 및 기존 이미지 삭제
     * @param startdate*/
    private void createProfile_Photo_and_Delete(int num, String startdate) {
        createDir(num); //디렉토리가 없으면 만든다.
        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
        StorageReference storageRef = storage.getReference();//스토리지를 참조한다.
        //파일명을 만들자.
        //여기서 DP는 다이어리 포토에 줄임말이다.
        String filename = "DP"+ "_" +startdate +".jpg";  //ex) DP_2019-02-21.jpg 해당 날짜 값으로만 식별한다.(어차피 디렉토리로 분류로 나누었기 때문에 이정도 식별로 충분하다)
        Uri file = uri;
        if(uri == null) { //uri값이 없으면 기본이미지로 저장한다.
            DiaryFrag.diaryImage.setImageResource(R.drawable.no_image);
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
                    Toast.makeText(getContext(), "일기가 등록되었습니다.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }




    /** 일기 정보(제목, 배경, 내용, 날짜) 가져오기 */
    private void getFireBaseProfileDiary(int num, String date) {
        createDir(num); //디렉토리 없으면 만들기
        downloadDiaryImage(num, date); //이미지 다운로드해서 가져오기 메서드
    }

    /**일기 이미지 다운로드해서 가져오기 메서드 */
    private void downloadDiaryImage(int num, String date) {
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고, //다운로드는 주소를 넣는다.
        StorageReference storageRef = storage.getReference();//스토리지를 참조한다
        storageRef.child("diary_photo/num" + num + "/" + "DP" + "_" + date +".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Log.d("매맨", String.valueOf(uri));
                Glide.with(getContext()).load(uri).into(DiaryFrag.diaryImage); //날짜로 호출한 이미지 사진 넣기
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
            }
        });
    }


    /**디렉토리 만들기(혹시 없을경우 대비해서) = 파이어베이스 */
    public void createDir(int num){
        //우선 디렉토리 파일 하나만든다.
        File file = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES + "diary_photo/num" + num + "/"); //이미지를 저장할 수 있는 디렉토리 ex)//diary_photo1(식별값)
        //구분할 수 있게 /toolbar_images폴더에 넣어준다.
        //이 파일안에 저 디렉토리가 있는지 확인
        if (!file.isDirectory()) { //디렉토리가 없으면,
            file.mkdir(); //디렉토리를 만든다.
        }
    }

    /**일기 만들기(num으로 사용자를 식별하고, 그에 맞는 다이어리 테이블에 컬럼값들을 저장한다.)
     * @param title
     * @param mainText
     * @param startdate*/
    private void createDiary(String title, String mainText, String startdate) {
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
                        String todayDate = FORMATTER.format(time);
                        String year = todayDate.substring(0,4); //받아온 연도 ex)2021
                        String month = todayDate.substring(5,7); //받아온 달 ex)02
                        String dayofMonth = todayDate.substring(8,10); //받아온 일 수 ex)25
                        diaryWriteDate(year, month, dayofMonth); //지금 쓴 날짜 초록색으로 변하게 하기
                        countDiary.setText(": "+ length+ "회"); //초록불 횟수 늘리기(일기를 쓰게 된다면 하나 더 늘게 만든다)

                    }else{

                    }

                }

                catch(JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };
        CreateDiaryColumn createDiaryTable_check = new CreateDiaryColumn(HomeMain.num, title, mainText, startdate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(createDiaryTable_check);

        Log.d("홈메인넘", String.valueOf(HomeMain.num));
    }

    /**
     * mysql에서 디비날짜정보로 제목과 내용 가져오기, 그리고 그림도 파이어베이스로 가져오기
     */
    public void getDBDiaryInfo(String startdate) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        String title = jsonObject.getString("title"); //제목 가져오기
                        Log.d("올까?", title);
                        String newTitle = textLengthChange(title); //수정한 새로운 제목
                        DiaryFrag.diaryText.setText(newTitle); //넣어주기

                        //파이어베이스에서 날짜에 맞는 사진 가져오기
                        getFireBaseProfileDiary(HomeMain.num, startdate);



                    } else {//실패
                        Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };

        getDiaryInfo_Request getDiaryInfo_Request = new getDiaryInfo_Request(HomeMain.num, startdate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(getDiaryInfo_Request);

        Log.d("올까?", String.valueOf(HomeMain.num));
        Log.d("올까?", startdate);
    }

    /**
     * mysql에서 디비날짜정보 가져오기(초록색 일기 썼는지 유무 표시용)
     */
    public void getDBDiaryDateGreen() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    Log.d("어레이", String.valueOf(jsonObject));
                    boolean success = jsonObject.getBoolean("success");
                    if (success) {
                        int length = jsonObject.length();
                        for(int i = 0; i <= length; i++) { //있는 수만큼 반복문
                            String date = jsonObject.getString(String.valueOf(i));
                            Log.d("이제이거", String.valueOf(jsonObject.length()));
                            Log.d("이제이거", date);
                            //날짜를 년월일로 나누어서
                            String year = date.substring(0,4); //받아온 연도 ex)2021
                            String month = date.substring(5,7); //받아온 달 ex)02
                            String dayofMonth = date.substring(8,10); //받아온 일 수 ex)25
                            diaryWriteDate(year, month, dayofMonth); //있는 수만큼 날짜 초록색으로 만들기
                        }
                        countDiary.setText(": "+ length + "회"); //초록불 횟수 늘리기
                    } else {//실패
                        Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        };

        getDiaryDate_Request getDiaryDate_request = new getDiaryDate_Request(HomeMain.num, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(getDiaryDate_request);
    }

    /**
     * 21자 이하로 만들고 넘으면 ... 추가하기 (일기 제목
     * @return
     */

    private String textLengthChange(String title) {
        if(title.length() >= 21){ //21글자 이상이면 바꾼 값으로 리턴
            String newTitle = title.substring(0,21) + "...";

            return newTitle;
        } //아니면 그냥 타이틀로 리턴
        return title;
    }




    /**
     * 날짜 변환기(분해기)
     * @return
     */
    public String dateRebuild(String date) {
        String year = date.substring(0,4); //받아온 연도 ex)2021
        String month = date.substring(5,7); //받아온 달 ex)02
        String dayofMonth = date.substring(8,10); //받아온 일 수 ex)25
        String dbDate = year + "-" + month + "-" + dayofMonth; //위에 세 변수를 모두 합친다.

        return dbDate;
    }


    /**
     * 로딩창
     */
    public void loadingStart() {
        loading_dialog = new Loading_Dialog(getContext());
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false); //뒤로가기방지
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.show();
    }

}