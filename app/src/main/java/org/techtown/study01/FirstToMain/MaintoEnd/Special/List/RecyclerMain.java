package org.techtown.study01.FirstToMain.MaintoEnd.Special.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONException;
import org.json.JSONObject;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.ReviseDiary;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.ViewDiary;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.getDiaryDate_Request;
import org.techtown.study01.FirstToMain.MaintoEnd.Special.getDiaryInfo_Request;
import org.techtown.study01.FirstToMain.R;
import org.techtown.study01.FirstToMain.homeMain.HomeMain;
import org.techtown.study01.FirstToMain.homeMain.Loading_Dialog;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

public class RecyclerMain extends AppCompatActivity {

    private RecyclerAdapter adapter;
    private RecyclerView recyclerView;
    private ArrayList<DiaryInfo_GetterSetter> item = new ArrayList<>();
    private Loading_Dialog loading_dialog;
    //이미지 값 겹치지 않게 두가지로 나눈다.
    private Uri uriS;
    private Uri uriF;
    private int start = 0;
    private int end = 3;

    public static RecyclerMain recyclerMain;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recycler_main);
        loadingStart();//로딩창

        recyclerMain = RecyclerMain.this;

        recyclerView = findViewById(R.id.recyclerView);

        //리사이클러뷰에 레이아웃 매니저 설정하기
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        adapter = new RecyclerAdapter();


        getDairyAllDate(start, end); //일기 리스트 생성 //처음값 끝 값

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if(!recyclerView.canScrollVertically(1)) {
                    int newStart = start + 4; //10개씩 가지고 오기
                    int newEnd = newStart + 3;
                    start = newStart; //값 최신화
                    end = newEnd; //값 최신화
                    getDairyAllDate(newStart, newEnd);

                    Log.d("뉴엔드", String.valueOf(newEnd));

                }

            }
        });




        //리사이클러뷰 클릭시 액션
        adapter.setOnDiaryItemClickListener(new OnDiaryItemClickListener() {
            @Override
            public void onItemClick(RecyclerAdapter.ViewHolder holder, View view, int position) {
                DiaryInfo_GetterSetter item = adapter.getItem(position);
                String rDate = item.getR_writeDate(); //클릭한 뷰 날짜정보를 가져온다.
                Log.d("리사이클러뷰", rDate);
                getDairyInfoRecyclerView(rDate); //보내서 디비에서 정보를 가져온다.
            }
        });

        loading_dialog.dismiss(); //로딩창끄기

    }


    /** 날짜를 통해 제목 정보 가져오기*/
    private void getDairyInfo(String date, Uri uri) {

        Response.Listener<String> responseListener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    String title = jsonObject.getString("title"); //타이틀 가져오기
                    item.add(new DiaryInfo_GetterSetter(title, date, uri, getApplicationContext()));

                    Log.d("유알아이좀보자2", String.valueOf(uri));

                    if(item.size() == 1){ //리스트가 한개일때 표시가 안되서 넣음
                        adapter.setItems(item);
                        recyclerView.setAdapter(adapter);
                        loading_dialog.dismiss(); //로딩창끄기
                        return;
                    }else{
                        //어레이리스트안에 있는 날짜데이터들을 정렬하는 함수이다.
                        Collections.sort(item, new Comparator<DiaryInfo_GetterSetter>() {
                            @Override
                            public int compare(DiaryInfo_GetterSetter o1, DiaryInfo_GetterSetter o2) {
                                adapter.setItems(item);
                                recyclerView.setAdapter(adapter);
                                recyclerView.smoothScrollToPosition(end-6); //며칠 차이 날지 보여주기
                                //리사이클러뷰 어뎁터 설정으로 마무리
                                Log.d("뉴엔드2", String.valueOf(end));
                                loading_dialog.dismiss(); //로딩창끄기
                                return o2.getR_writeDate().compareTo(o1.getR_writeDate());
                            }
                        });

                    }

                } else {//실패
                    Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    loading_dialog.dismiss(); //로딩창끄기
                }


            } catch (Exception e) {
                e.printStackTrace();
                loading_dialog.dismiss(); //로딩창끄기
            }
        };

        getDiaryInfo_Request getDiaryInfo_request = new getDiaryInfo_Request(HomeMain.num, date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(getDiaryInfo_request);

    }


    /** 날짜를 통해 타이틀과 내용 정보 가져오기 (리사이클러뷰용)*/
    private void getDairyInfoRecyclerView(String date) {

        Response.Listener<String> responseListener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                boolean success = jsonObject.getBoolean("success");
                if (success) {
                    String title = jsonObject.getString("title"); //타이틀 가오기
                    String mainText = jsonObject.getString("maintext"); //내용 가져오기
                    //RecyclerMain으로 이동,
                    Intent intent = new Intent(getApplication(), ViewDiary.class);
                    intent.putExtra("Rtitle", title);
                    intent.putExtra("RmainText", mainText);
                    intent.putExtra("Rdate", date);
                    startActivity(intent);
//                   finish(); //업데이트 할 걸 우려해서 꺼버리기
                    Log.d("마지막리사이클", title);
                    Log.d("마지막리사이클", mainText);

                } else {//실패
                    Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                }


            } catch (JSONException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
            }
        };

        getDiaryInfo_Request getDiaryInfo_request = new getDiaryInfo_Request(HomeMain.num, date, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(getDiaryInfo_request);

    }

    /** 고유넘버 num을 통해 모든 날짜 컬럼 가져오기*/
    private void getDairyAllDate(int start, int end) {

        Response.Listener<String> responseListener = response -> {
            try {
                JSONObject jsonObject = new JSONObject(response);

                boolean success = jsonObject.getBoolean("success");
                if (success) {
                        for (int i = start; i <= end; i++) { //있는 수만큼 반복문
                            String date = jsonObject.getString(String.valueOf(i));
                            Log.d("베이베베이베", date);
                            downloadDiaryImage(HomeMain.num, date); //날짜에 맞는 (uri) 이미지 넣기
                            loading_dialog.dismiss(); //로딩창끄기

                    }


                } else {//실패
                    Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                    loading_dialog.dismiss(); //로딩창끄기
                }


            } catch (JSONException e) {
                e.printStackTrace();
                loading_dialog.dismiss(); //로딩창끄기
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplication(), "인터넷 연결을 확인해주세요.", Toast.LENGTH_SHORT).show();
                loading_dialog.dismiss(); //로딩창끄기
            }
        };

        getDiaryDate_Request getDiaryDate_request = new getDiaryDate_Request(HomeMain.num, responseListener);
        RequestQueue queue = Volley.newRequestQueue(getApplication());
        queue.add(getDiaryDate_request);

    }

    /**일기 이미지 다운로드해서 가져오기 메서드
     */
    private void downloadDiaryImage(int num, String date) {
        createDir(num);
        FirebaseStorage storage = FirebaseStorage.getInstance(); //스토리지 인스턴스를 만들고, //다운로드는 주소를 넣는다.
        StorageReference storageRef = storage.getReference();//스토리지를 참조한다
        storageRef.child("diary_photo/num" + num + "/" + "DP" + "_" + date +".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                uriS = uri; //url변수 저장
                getDairyInfo(date, uriS);  //모든 컬럼날짜 전달
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                uriF = null; //uri에 널값을 주어 이미지가 없으면 기본이미지를 보여주게 한다.
                getDairyInfo(date, uriF);  //모든 컬럼날짜 전달
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

    /**
     * 로딩창
     */
    public void loadingStart() {
        loading_dialog = new Loading_Dialog(RecyclerMain.this);
        loading_dialog.setCanceledOnTouchOutside(false);
        loading_dialog.setCancelable(false); //뒤로가기방지
        loading_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        loading_dialog.show();
    }

}