package org.techtown.study01.FirstToMain.MaintoEnd.Special;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
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
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.List.RecyclerMain;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.homeMain.Loading_Dialog;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Diary extends Fragment implements OnDateSelectedListener {

    private ViewGroup viewGroup;
    public static MaterialCalendarView materialCalendarView;

    private TextView diaryText;
    private ImageView diaryImage;
    private Button dialogPlusButton , delete_btn, showAll_btn;

    public static Uri uri;
    public static TextView countDiary;

    public static String viewtitle = null;
    public static String viewMaintText = null;
    public static String dbDate;

    public static String startdate;

    public static int length = 0; //일기 갯수

    private ViewPager2 viewPageSetUp;

    public static ArrayList<CalendarDay> calendarDayList; //캘린더 리스트 안에 내가 입력한 즉, 일기를 쓴(초록색표시) 날이 다 들어가 있음.
    public static ArrayList<CalendarDay> deleteList; //삭제한 리스트 (붉은색표시)

    public static final SimpleDateFormat FORMATTER =  new SimpleDateFormat("yyyy-MM-dd"); //날짜 데이터 포맷


    /** 앱이 맨처음 실행될 때 최초 한번만*/
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        Date time = new Date();
        String todayDate = FORMATTER.format(time);
        startdate = todayDate; //날짜에 오늘 날짜 넣어주기(일기 중복으로 쎃는지 체크하기 위함)
        Log.d("오늘날짜", todayDate);
        //오늘 날짜 일기를 처음 보여준다.
        getDBDiaryInfo(todayDate); //오늘 날짜 일기 정보
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewGroup = (ViewGroup) inflater.inflate(R.layout.diary, container, false);

        components(); //참조 모음

        materialCalendarViewSettings(); //메트리얼뷰 세팅

        setInit(); //뷰페이저 실행

        getDBDiaryDateGreen(); //일기 쓴 날짜들 초록색 표시

        gotoWriteDiary(); //일기작성 액티비티로 가기

        return viewGroup;

    }

    /** 참조모음 **/
    private void components() {
        diaryText = viewGroup.findViewById(R.id.diaryText); //일기 텍스트
        diaryImage = viewGroup.findViewById(R.id.diaryImage); //일기 이미지
        dialogPlusButton = viewGroup.findViewById(R.id.dialogPlusButton); //플로팅 버튼(일기 쓰기 버튼)
        countDiary = viewGroup.findViewById(R.id.countDiary); //일기 쓴 횟수(초록색 동그라미)
        delete_btn = viewGroup.findViewById(R.id.delete_btn); //일기 삭제 버튼
        showAll_btn = viewGroup.findViewById(R.id.showAll_btn); //일기 전체보기 버튼
        materialCalendarView = viewGroup.findViewById(R.id.calendarView5);
    }

    /** 메트리얼뷰 세팅 **/
    private void materialCalendarViewSettings() {
        materialCalendarView.setSelectedDate(CalendarDay.today()); //오늘 날짜 큰 갈색// 동그라미 표시
        materialCalendarView.setOnDateChangedListener(this);
        materialCalendarView.state()
                .edit()
                .setMinimumDate(CalendarDay.from(2021, 01, 01))
                .commit(); //날짜범위 최소 설정
        materialCalendarView.state().edit()
                .setMaximumDate(CalendarDay.from(2100, 12, 31))
                .commit(); //날짜 범위 최대 설정
        //가로길이
        materialCalendarView.setTileWidthDp(48);
    }

    /** 일기 작성을 인텐트 하기 **/
    private void gotoWriteDiary() {
        //일기 추가 버튼 누를때
        dialogPlusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                samediaryCheck(); //일기 중복으로썼는지 체크
            }
        });

        //일기 삭제 버튼
        delete_btn.setOnClickListener(v -> {

            reviceAlertD(); //삭제 다이얼로그 실행
        });

        //일기 전체보기 버튼
        showAll_btn.setOnClickListener(v -> {
            //recyclerView로 이동
            Intent intent = new Intent(getActivity(), RecyclerMain.class);
            startActivity(intent);
        });
    }

    /**일기 삭제하기 다이얼로그 */
    private void reviceAlertD() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setCancelable(false); //외부 클릭시 막아주기
        builder.setTitle("일기 삭제");
        builder.setMessage(startdate + " 일기를 삭제하겠습니까?");
        builder.setPositiveButton("아니오",
                (dialog, which) -> {

                });
        builder.setNegativeButton("예",
                (dialog, which) -> {
                    check_diary_when_delete(); //삭제할 때 해당 날짜에 일기가 있는지
                });
        builder.show();
    }


    /** 삭제할 때 해당 날짜에 일기가 있는지 확인 후 삭제 메서드 실행(db연결)*/
    private void check_diary_when_delete() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {
                        //일기가 있다면
                        delete_Diary(); //다이어리 삭제실행
                    } else {//실패
                        Toast.makeText(getContext(), "해당 날짜에 일기가 존재하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }
            }
        };

        getDiaryInfo_Request getDiaryInfo_Request = new getDiaryInfo_Request(HomeMain.num, startdate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(getDiaryInfo_Request);

        Log.d("올까?", String.valueOf(HomeMain.num));
        Log.d("올까?", startdate);
    }

    /** 일기 삭제 하기 (db연결)*/
    private void delete_Diary() {
        Response.Listener<String> responseListener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);
                Log.d("어레이", String.valueOf(jsonObject));
                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    justFireBase_Delete(); //파이어베이스에 이미지도 같이 삭제
                    DiaryFrag.diaryFrag.setVisibility(View.GONE); //프래그먼트 임시적으로 없애기
                    length = length - 1;
                    countDiary.setText(":  "+ length+ "회"); //초록불 횟수 늘리기(일기를 쓰게 된다면 하나 더 줄게 만든다.)
                    calendarDayList.remove(startdate); //캘린더에서 값을 없애고,
                    Log.d("스타트데이데이", startdate);
                    //삭제 리스트에 넣어서 붉게 만든다.
                    String year = startdate.substring(0,4); //받아온 연도 ex)2021
                    String month = startdate.substring(5,7); //받아온 달 ex)02
                    String dayofMonth = startdate.substring(8,10); //받아온 일 수 ex)25
                    diaryWriteDate_delete(year, month, dayofMonth); //삭제 리스트에 추가
                    uri=null; //uri 널처리를 해준다.
                    Toast.makeText(getContext(), "삭제를 완료했습니다.", Toast.LENGTH_SHORT).show();
                } else {//실패
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        };

        delete_Diary_request delete_diary_request = new delete_Diary_request(HomeMain.num, startdate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(delete_diary_request);
        Log.d("삭제가자", String.valueOf(HomeMain.num));
        Log.d("삭제가자",startdate);
    }



    /** 일기 쓴 날짜 (이 곳에서 일기 쓴 날짜를 초록색 불로 표시해준다.) (날짜를 계속 넣어줘야함.) **/

    private void diaryWriteDate(String year, String month, String day) {
        //스트링을 인트로 형변환
        int yearR = Integer.parseInt(year);
        int monthR = Integer.parseInt(month);
        int newMonthR = monthR - 1; //달은 1빼준다.
        int dayR = Integer.parseInt(day);

        //일기를 썼던 날짜리스트를 만든다.
        calendarDayList = new ArrayList<>();
        calendarDayList.add(CalendarDay.from(yearR, newMonthR, dayR)); //일기 쓴 날짜 표시/ 일기 쓴 날 들을 add해준다.
        Log.d("캘린더리스트", String.valueOf(calendarDayList));

        EventDecorator eventDecorator = new EventDecorator(Color.rgb(10, 207, 32), calendarDayList); //색표시 이벤트 데코레이터 호출
        materialCalendarView.addDecorators(eventDecorator);
        Log.d("캘린더리스트2", String.valueOf(calendarDayList));

    }


    /** 일기 삭제한 날짜 (이 곳에서 일기 쓴 날짜를 붉은 불로 표시해준다.) 삭제한 날짜 모음 **/
    private void diaryWriteDate_delete(String year, String month, String day) {
        //스트링을 인트로 형변환
        int yearR = Integer.parseInt(year);
        int monthR = Integer.parseInt(month);
        int newMonthR = monthR - 1; //달은 1빼준다.
        int dayR = Integer.parseInt(day);
        Log.d("캘린더리스트", String.valueOf(yearR));

        //일기를 썼던 날짜리스트를 만든다.
        deleteList = new ArrayList<>();
        deleteList.add(CalendarDay.from(yearR, newMonthR, dayR)); //일기 쓴 날짜 표시/ 일기 쓴 날 들을 add해준다.
        Log.d("캘린더리스트레드", String.valueOf(deleteList));

        EventDecorator eventDecorator = new EventDecorator(Color.RED , deleteList); //색표시 이벤트 데코레이터 호출
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




    /**달력 날짜를 선택할 때 여기서 날짜 값을 받아온다. 여기서 일기를 가져온다.(디비 연동)*/
    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

        final String text = selected ? FORMATTER.format(date.getDate()) : "No Selection"; //날짜 클릭 시에 날짜값을 받아온다. //ex)2012-12-25
        dbDate = dateRebuild(text); //선택한 날짜 변환해서 리턴한다.
        getDBDiaryInfo(dbDate); //mysql디비에서 제목 내용 가져오기  //인자에는 날짜를 보낸다.
        startdate = dbDate; //일기 중복으로 썼는지 체크하기 위해
        Log.d("스타트데이트1", startdate);


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
                Glide.with(getActivity()).load(uri).into(DiaryFrag.diaryImage);
                Diary.uri = uri; //ViewDiary로 uri보내기
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                DiaryFrag.diaryImage.setImageResource(R.drawable.no_image); //실패시 기본이미지
                Diary.uri = null; //uri에 널값을 주어 viewDairy에도 기본이미지가 보이게 한다.
            }
        });
    }


    /** 파이어베이스 이미지만 삭제**/
    private void justFireBase_Delete() {
        createDir(HomeMain.num); //디렉토리가 없으면 만든다.
        //storage
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고,
        StorageReference storageRef = storage.getReference();//스토리지를 참조한다.
        //파일명을 만들자.
        //여기서 DP는 다이어리 포토에 줄임말이다.
        String filename = "DP"+ "_" + startdate +".jpg";  //ex) DP_2019-02-21.jpg 해당 날짜 값으로만 식별한다.(어차피 디렉토리로 분류로 나누었기 때문에 이정도 식별로 충분하다)

        // TODO: 일기만 삭제
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
                        String maintext = jsonObject.getString("maintext"); //제목 가져오기
                        Log.d("올까?", title);
                        String newTitle = textLengthChange(title); //수정한 새로운 제목
                        DiaryFrag.diaryText.setText(newTitle); //넣어주기
                        DiaryFrag.diaryFrag.setVisibility(View.VISIBLE); //diaryFrag보여주기
                        //파이어베이스에서 날짜에 맞는 사진 가져오기
                        getFireBaseProfileDiary(HomeMain.num, startdate);

                        //변수에 일기정보 담기(diaryFrag로 보내고, diaryFrag에서 ViewDiary로 보내기위해)
                        viewtitle = title;
                        viewMaintText = maintext;

                    } else {//실패
                        DiaryFrag.diaryFrag.setVisibility(View.INVISIBLE); //diaryFrag보여주기

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();

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
                        length = jsonObject.length()-1; //처음에 일기 갯수 넘겨 주기
                        countDiary.setText(":  "+ length + "회"); //초록불 횟수 늘리기
                        Log.d("카운트다이어리3", String.valueOf(length));
                        for(int i = 0; i <= length; i++) { //있는 수만큼 반복문
                            String date = jsonObject.getString(String.valueOf(i));
                            Log.d("이제이거", String.valueOf(jsonObject.length()));
                            Log.d("이제이거", date);
                            //날짜를 년월일로 나누어서
                            String year = date.substring(0,4); //받아온 연도 ex)2021
                            String month = date.substring(5,7); //받아온 달 ex)02
                            String dayofMonth = date.substring(8,10); //받아온 일 수 ex)25
                            diaryWriteDate(year, month, dayofMonth); //있는 수만큼 날짜 초록색으로 만들기
                            //인터페이스를 이용해 데이터 전달하기(날짜만)
                        }

                    }


                } catch (JSONException e) {
                    e.printStackTrace();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();

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
        if(title.length() >= 13){ //13글자 이상이면 바꾼 값으로 리턴
            String newTitle = title.substring(0,13) + "...";

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

    /**중복으로 같은 날짜에 일기쓰기 방지 */
    private void samediaryCheck() {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");

                    if (success) {//일기가 존재하지 않으면 작성하기 위해 이동
                        Date time = new Date();
                        String todayDate = FORMATTER.format(time);
                        if(startdate.equals(todayDate)) { //선택한 날짜가 오늘 날짜이면 이동
                            Intent intent = new Intent(getActivity(), WriteDiary.class);
                            startActivity(intent);
                        }else{ //오늘 날짜가 아니면,
                            Toast.makeText(getContext(), "현재 날짜에서 일기 작성이 가능합니다.", Toast.LENGTH_SHORT).show();

                        }
                    } else { //일기가 존재함
                        Toast.makeText(getContext(), "선택 날짜에 일기가 존재합니다.", Toast.LENGTH_SHORT).show();

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.4", Toast.LENGTH_SHORT).show();

                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "인터넷 연결을 확인해주세요.5", Toast.LENGTH_SHORT).show();

                }
            }
        };

        SameDiaryCheck sameDiaryCheck = new SameDiaryCheck(HomeMain.num, startdate, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getContext());
        queue.add(sameDiaryCheck);
        Log.d("스타트데이트2", startdate);
    }


}